package bw.co.knowvera.messaging;

import bw.co.knowvera.TargetEntity;
import bw.co.knowvera.form.submission.FormSubmission;
import bw.co.knowvera.form.submission.FormSubmissionRepository;
import bw.co.knowvera.individual.IndividualRepository;
import bw.co.knowvera.kyc.KycRecord;
import bw.co.knowvera.organisation.OrganisationRepository;
import bw.co.knowvera.settings.Settings;
import bw.co.knowvera.settings.SettingsRepository;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Executor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionOperations;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * Queues the novu-form-required notification (docs/novu/workflows): tells a client that an
 * organisation onboarding it has a new form for it to fill, which is now part of its KYC record.
 * Sent when a form is published after the client's KYC record already exists; a client that is
 * doing its KYC sees the form there and gets no message.
 *
 * <p>Only submission ids leave the caller's thread. The messages are built after the caller's
 * transaction commits, on {@code virtualThreadExecutor}, from submissions reloaded in a read-only
 * transaction of their own (see {@link ClientRequestNotification}).
 */
@Component
public class FormRequiredNotification {

  private static final Logger log = LoggerFactory.getLogger(FormRequiredNotification.class);
  private static final DateTimeFormatter DUE_FORMAT = DateTimeFormatter.ofPattern("d MMMM yyyy", Locale.ENGLISH);

  @Value("${app.novu.queue.formRequiredQueueExchange:x.novu-form-required-queue}")
  private String exchange;

  @Value("${app.novu.queue.formRequiredQueueRoutingKey:novu-form-required}")
  private String routingKey;

  private final SettingsRepository settingsRepository;
  private final IndividualRepository individualRepository;
  private final OrganisationRepository organisationRepository;
  private final FormSubmissionRepository formSubmissionRepository;
  private final ConfirmedPublisher publisher;
  private final Executor executor;
  private final TransactionOperations readOnlyTransaction;

  /** The constructor Spring uses; the other one lets tests run the background work inline. */
  @Autowired
  public FormRequiredNotification(SettingsRepository settingsRepository, IndividualRepository individualRepository,
      OrganisationRepository organisationRepository, FormSubmissionRepository formSubmissionRepository,
      ConfirmedPublisher publisher, @Qualifier("virtualThreadExecutor") Executor executor,
      PlatformTransactionManager transactionManager) {
    this(settingsRepository, individualRepository, organisationRepository, formSubmissionRepository, publisher,
        executor, readOnly(transactionManager));
  }

  FormRequiredNotification(SettingsRepository settingsRepository, IndividualRepository individualRepository,
      OrganisationRepository organisationRepository, FormSubmissionRepository formSubmissionRepository,
      ConfirmedPublisher publisher, Executor executor, TransactionOperations readOnlyTransaction) {
    this.settingsRepository = settingsRepository;
    this.individualRepository = individualRepository;
    this.organisationRepository = organisationRepository;
    this.formSubmissionRepository = formSubmissionRepository;
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
   * Tells the client of each submission that it has a form to fill, once the caller's
   * transaction (if any) has committed. Returns at once; a failure to notify never fails the caller.
   *
   * @param submissionIds the ids of the submissions just added to KYC records
   */
  public void queue(Collection<UUID> submissionIds) {
    if (submissionIds == null || submissionIds.isEmpty()) {
      return;
    }
    List<UUID> ids = List.copyOf(submissionIds);
    Runnable send = () -> executor.execute(() -> {
      try {
        readOnlyTransaction.executeWithoutResult(status -> send(ids));
      } catch (RuntimeException e) {
        log.error("Queueing form notifications failed", e);
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

  /** Builds and queues the messages, in a transaction of their own. */
  void send(List<UUID> submissionIds) {
    Settings settings = settingsRepository.findAll().stream().findFirst().orElse(null);
    Instant now = Instant.now();
    for (UUID id : submissionIds) {
      FormSubmission submission = formSubmissionRepository.findById(id).orElse(null);
      if (submission == null) {
        log.warn("Form submission {} no longer exists: no notification sent", id);
        continue;
      }
      Map<String, String> payload = buildPayload(submission, settings, now);
      if (payload == null) {
        log.warn("The client of form submission {} has no email address: no notification sent", id);
        continue;
      }
      String messageId = publisher.publish(exchange, routingKey, payload);
      log.info("Queued form notification for submission {} (message {})", id, messageId);
    }
  }

  /**
   * The message payload, or null when the client of the KYC record has no email address. Blank
   * values are left out.
   */
  Map<String, String> buildPayload(FormSubmission submission, Settings settings, Instant now) {
    Recipient recipient = recipientOf(submission.getKycRecord());
    if (recipient == null || StringUtils.isBlank(recipient.email())) {
      return null;
    }
    Map<String, String> payload = new HashMap<>();
    payload.put("email", recipient.email().trim());
    putIfPresent(payload, "recipientName", recipient.name());
    putIfPresent(payload, "formName", submission.getForm().getName());
    putIfPresent(payload, "organisationName", submission.getForm().getOrganisation().getName());
    putIfPresent(payload, "reference", submission.getKycRecord().getRef());
    if (submission.getDueDate() != null) {
      payload.put("dueDate", DUE_FORMAT.format(submission.getDueDate()));
    }
    // Without a portal link the message still tells the client to sign in; the template shows
    // the button only when there is a link
    String portal = settings != null ? StringUtils.stripEnd(StringUtils.trimToNull(settings.getKycPortalLink()), "/") : null;
    if (portal != null) {
      payload.put("formUrl", portal + "/forms/" + submission.getId());
      payload.put("platformUrl", portal);
    }
    // The Novu template prints these without Liquid filters, so the defaults are sent from here.
    payload.put("platformName",
        StringUtils.defaultIfBlank(settings != null ? settings.getPlatformName() : null, "Knowvera"));
    payload.put("supportContact",
        StringUtils.defaultIfBlank(settings != null ? settings.getSupportContact() : null, "our support team"));
    payload.put("currentYear", String.valueOf(now.atZone(ZoneId.systemDefault()).getYear()));
    return payload;
  }

  private record Recipient(String email, String name) {
  }

  /** The client the KYC record is about: the individual, or the organisation's contact address. */
  private Recipient recipientOf(KycRecord record) {
    UUID clientId;
    try {
      clientId = UUID.fromString(record.getTargetId());
    } catch (IllegalArgumentException | NullPointerException e) {
      return null;
    }
    if (record.getTarget() == TargetEntity.ORGANISATION) {
      return organisationRepository.findById(clientId)
          .map(o -> new Recipient(o.getContactEmailAddress(), o.getName()))
          .orElse(null);
    }
    return individualRepository.findById(clientId)
        .map(i -> new Recipient(i.getEmailAddress(), ClientRequestNotification.fullName(i)))
        .orElse(null);
  }

  private static void putIfPresent(Map<String, String> payload, String key, String value) {
    if (StringUtils.isNotBlank(value)) {
      payload.put(key, value);
    }
  }
}
