package bw.co.knowvera.messaging;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import bw.co.knowvera.TargetEntity;
import bw.co.knowvera.individual.Individual;
import bw.co.knowvera.individual.IndividualRepository;
import bw.co.knowvera.organisation.OrganisationRepository;
import bw.co.knowvera.organisation.client.ClientRequest;
import bw.co.knowvera.settings.Settings;
import bw.co.knowvera.settings.SettingsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Queues the novu-new-org-client-request invitation (docs/novu/workflows) for new client
 * requests. The link carries the request's single-use token, so neither the token nor the link is
 * ever logged.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class ClientRequestNotification {

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
  private final RabbitTemplate rabbitTemplate;

  /**
   * @param tokenMap the plain token of each request, by target id; requests without one (already
   *                 invited) get no message
   */
  @Async("virtualThreadExecutor")
  public void queueEmailNotificationsForRequests(List<ClientRequest> clientRequests,
      Map<String, String> tokenMap, String organisation) {

    Settings settings = settingsRepository.findAll().stream().findFirst().orElse(null);
    if (settings == null) {
      log.warn("No application settings: client request invitations have no platform name or support contact");
    }

    Instant now = Instant.now();
    for (ClientRequest request : clientRequests) {

      String token = tokenMap.get(request.getTargetId());
      if (StringUtils.isBlank(token)) {
        log.debug("Client request {} has no new token: no invitation sent", request.getId());
        continue;
      }

      Map<String, String> payload = buildPayload(request, token, organisation, settings, now);
      if (payload == null) {
        log.warn("Client request {} ({} {}) has no recipient email address: no invitation sent",
            request.getId(), request.getTarget(), request.getTargetId());
        continue;
      }

      log.info("Queueing invitation for client request {}", request.getId());
      rabbitTemplate.convertAndSend(novuNewOrgClientRequestQueueExchange, novuNewOrgClientRequestQueueRoutingKey,
          payload);
    }
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
      putIfPresent(payload, "platformName", settings.getPlatformName());
      putIfPresent(payload, "platformUrl", settings.getKycPortalLink());
      putIfPresent(payload, "supportContact", settings.getSupportContact());
    }
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
