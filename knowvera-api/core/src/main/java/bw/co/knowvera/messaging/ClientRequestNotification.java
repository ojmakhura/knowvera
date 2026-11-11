package bw.co.knowvera.messaging;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Executor;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionOperations;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.transaction.support.TransactionTemplate;

import bw.co.knowvera.TargetEntity;
import bw.co.knowvera.individual.Individual;
import bw.co.knowvera.individual.IndividualRepository;
import bw.co.knowvera.organisation.OrganisationRepository;
import bw.co.knowvera.organisation.client.ClientRequest;
import bw.co.knowvera.organisation.client.ClientRequestRepository;
import bw.co.knowvera.settings.Settings;
import bw.co.knowvera.settings.SettingsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * Queues the novu-new-org-client-request invitation (docs/novu/workflows) for new client
 * requests. The link carries the request's single-use token, so neither the token nor the link is
 * ever logged.
 *
 * <p>Only request ids and tokens leave the caller's thread. The invitations are built after the
 * caller's transaction commits, on {@code virtualThreadExecutor}, from entities reloaded in a
 * read-only transaction of their own: entities of the caller's (closed) persistence context
 * cannot load lazy associations there, and before the commit the new requests are not visible.
 */
@Component
public class ClientRequestNotification {

  private static final Logger log = LoggerFactory.getLogger(ClientRequestNotification.class);

  private static final DateTimeFormatter EXPIRY_FORMAT = DateTimeFormatter.ofPattern("d MMMM yyyy", Locale.ENGLISH);

  @Value("${app.registration-url}")
  private String registrationUrl;

  /** Lifetime of the emailed link; must match ClientRequestServiceImpl's check. */
  @Value("${app.request-token-link-ttl:7d}")
  private Duration requestLinkTtl;

  @Value("${app.novu.queue.newOrgClientRequestQueueExchange}")
  private String novuNewOrgClientRequestQueueExchange;

  @Value("${app.novu.queue.newOrgClientRequestQueueRoutingKey}")
  private String novuNewOrgClientRequestQueueRoutingKey;

  private final SettingsRepository settingsRepository;
  private final IndividualRepository individualRepository;
  private final OrganisationRepository organisationRepository;
  private final ClientRequestRepository clientRequestRepository;
  private final ConfirmedPublisher publisher;
  private final Executor executor;
  private final TransactionOperations readOnlyTransaction;

  /** The constructor Spring uses; the other one lets tests run the background work inline. */
  @Autowired
  public ClientRequestNotification(SettingsRepository settingsRepository, IndividualRepository individualRepository,
      OrganisationRepository organisationRepository, ClientRequestRepository clientRequestRepository,
      ConfirmedPublisher publisher, @Qualifier("virtualThreadExecutor") Executor executor,
      PlatformTransactionManager transactionManager) {
    this(settingsRepository, individualRepository, organisationRepository, clientRequestRepository, publisher,
        executor, readOnly(transactionManager));
  }

  ClientRequestNotification(SettingsRepository settingsRepository, IndividualRepository individualRepository,
      OrganisationRepository organisationRepository, ClientRequestRepository clientRequestRepository,
      ConfirmedPublisher publisher, Executor executor, TransactionOperations readOnlyTransaction) {
    this.settingsRepository = settingsRepository;
    this.individualRepository = individualRepository;
    this.organisationRepository = organisationRepository;
    this.clientRequestRepository = clientRequestRepository;
    this.publisher = publisher;
    this.executor = executor;
    this.readOnlyTransaction = readOnlyTransaction;
  }

  private static TransactionOperations readOnly(PlatformTransactionManager transactionManager) {
    TransactionTemplate template = new TransactionTemplate(transactionManager);
    template.setReadOnly(true);
    return template;
  }

  /**
   * Sends an invitation for each saved request that has a new token, once the caller's
   * transaction (if any) has committed. Returns at once.
   *
   * @param tokenMap the plain token of each request, by target id; requests without one (already
   *                 invited) get no message
   */
  public void queueEmailNotificationsForRequests(List<ClientRequest> clientRequests,
      Map<String, String> tokenMap, String organisation) {

    // Read the ids now, while the entities are still attached; nothing else crosses threads
    Map<UUID, String> tokens = new LinkedHashMap<>();
    for (ClientRequest request : clientRequests) {
      String token = tokenMap.get(request.getTargetId());
      if (StringUtils.isBlank(token) || request.getId() == null) {
        log.debug("Client request {} has no new token: no invitation sent", request.getId());
        continue;
      }
      tokens.put(request.getId(), token);
    }
    if (tokens.isEmpty()) {
      return;
    }

    Runnable send = () -> executor.execute(() -> {
      try {
        readOnlyTransaction.executeWithoutResult(status -> send(tokens, organisation));
      } catch (RuntimeException e) {
        log.error("Queueing client request invitations failed", e);
      }
    });

    if (TransactionSynchronizationManager.isSynchronizationActive()) {
      TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
        @Override
        public void afterCommit() {
          send.run();
        }
      });
    } else {
      send.run();
    }
  }

  /** Builds and queues the invitations, in a transaction of their own. */
  void send(Map<UUID, String> tokens, String organisation) {

    Settings settings = settingsRepository.findAll().stream().findFirst().orElse(null);
    if (settings == null) {
      log.warn("No application settings: client request invitations use the default platform name and support contact");
    }

    Instant now = Instant.now();
    tokens.forEach((requestId, token) -> {

      ClientRequest request = clientRequestRepository.findById(requestId).orElse(null);
      if (request == null) {
        log.warn("Client request {} no longer exists: no invitation sent", requestId);
        return;
      }

      Map<String, String> payload = buildPayload(request, token, organisation, settings, now);
      if (payload == null) {
        log.warn("Client request {} ({} {}) has no recipient email address: no invitation sent",
            request.getId(), request.getTarget(), request.getTargetId());
        return;
      }

      String messageId = publisher.publish(novuNewOrgClientRequestQueueExchange, novuNewOrgClientRequestQueueRoutingKey,
          payload);
      log.info("Queued invitation for client request {} (message {})", request.getId(), messageId);
    });
  }

  /**
   * The invitation payload, or null when the request's target has no email address. Blank values
   * are left out.
   */
  Map<String, String> buildPayload(ClientRequest request, String token, String organisation, Settings settings,
      Instant issuedAt) {

    Recipient recipient = recipientOf(request);
    if (recipient == null || StringUtils.isBlank(recipient.email())) {
      return null;
    }

    Map<String, String> payload = new HashMap<>();
    payload.put("email", recipient.email().trim());
    putIfPresent(payload, "recipientName", recipient.name());
    putIfPresent(payload, "organisationName",
        StringUtils.isNotBlank(organisation) ? organisation
            : request.getOrganisation() != null ? request.getOrganisation().getName() : null);
    payload.put("kycPortalLink", String.format("%s/%s?token=%s", registrationUrl, request.getId(), token));
    putIfPresent(payload, "reference", request.getRef());
    if (requestLinkTtl != null) {
      payload.put("linkExpiresAt", EXPIRY_FORMAT.format(issuedAt.plus(requestLinkTtl).atZone(ZoneId.systemDefault())));
    }
    if (settings != null) {
      putIfPresent(payload, "platformUrl", settings.getKycPortalLink());
    }
    // The Novu template prints these without Liquid filters, so the defaults are sent from here.
    payload.put("platformName",
        StringUtils.defaultIfBlank(settings != null ? settings.getPlatformName() : null, "Knowvera"));
    payload.put("supportContact",
        StringUtils.defaultIfBlank(settings != null ? settings.getSupportContact() : null, "our support team"));
    payload.put("currentYear", String.valueOf(issuedAt.atZone(ZoneId.systemDefault()).getYear()));
    return payload;
  }

  private record Recipient(String email, String name) {
  }

  private Recipient recipientOf(ClientRequest request) {

    UUID targetId;
    try {
      targetId = UUID.fromString(request.getTargetId());
    } catch (IllegalArgumentException | NullPointerException e) {
      return null;
    }

    if (request.getTarget() == TargetEntity.ORGANISATION) {
      return organisationRepository.findById(targetId)
          .map(o -> new Recipient(o.getContactEmailAddress(), o.getName()))
          .orElse(null);
    }

    return individualRepository.findById(targetId)
        .map(i -> new Recipient(i.getEmailAddress(), fullName(i)))
        .orElse(null);
  }

  static String fullName(Individual individual) {

    StringBuilder name = new StringBuilder();
    for (String part : new String[] { individual.getFirstName(), individual.getMiddleName(),
        individual.getSurname() }) {
      if (StringUtils.isNotBlank(part)) {
        if (name.length() > 0) {
          name.append(' ');
        }
        name.append(part.trim());
      }
    }
    return name.toString();
  }

  private static void putIfPresent(Map<String, String> payload, String key, String value) {
    if (StringUtils.isNotBlank(value)) {
      payload.put(key, value);
    }
  }
}
