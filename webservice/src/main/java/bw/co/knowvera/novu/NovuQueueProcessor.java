package bw.co.knowvera.novu;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import co.novu.Novu;
import co.novu.models.components.SubscriberPayloadDto;
import co.novu.models.components.To2;
import co.novu.models.components.TriggerEventRequestDto;

/**
 * Triggers the Novu workflows queued by the API (payloads: docs/novu/workflows). The subscriber is
 * sent inline with each trigger, so Novu creates or updates it. A failed trigger is retried and
 * then dead-lettered by {@link NovuQueueConfig#CONTAINER_FACTORY}; the AMQP message id is the
 * transactionId, so a retry of a trigger Novu already accepted is not delivered twice.
 */
@Service
public class NovuQueueProcessor {

  private static final Logger LOGGER = LoggerFactory.getLogger(NovuQueueProcessor.class);

  private final Novu novuSdk;

  public NovuQueueProcessor(Novu novuSdk) {
    this.novuSdk = novuSdk;
  }

  @Value("${app.novu.base-url}")
  private String novuBaseUrl;

  @Value("${app.novu.queue.newUserNovuId}")
  private String novuNewUserId;

  @Value("${app.novu.queue.newOrgUserNovuId}")
  private String novuNewOrgUserId;

  @Value("${app.novu.queue.newOrgClientRequestNovuId}")
  private String novuNewOrgClientRequestId;

  @Value("${app.novu.queue.newKycRecordNovuId}")
  private String novuNewKycRecordId;

  @RabbitListener(queues = "${app.novu.queue.newUserQueue}", containerFactory = NovuQueueConfig.CONTAINER_FACTORY)
  public void processNewUserQueue(Map<String, String> payload,
      @Header(name = AmqpHeaders.MESSAGE_ID, required = false) String messageId) {
    trigger(novuNewUserId, payload, messageId);
  }

  @RabbitListener(queues = "${app.novu.queue.newOrgUserQueue}", containerFactory = NovuQueueConfig.CONTAINER_FACTORY)
  public void processNewOrgUserQueue(Map<String, String> payload,
      @Header(name = AmqpHeaders.MESSAGE_ID, required = false) String messageId) {
    trigger(novuNewOrgUserId, payload, messageId);
  }

  @RabbitListener(queues = "${app.novu.queue.newOrgClientRequestQueue}",
      containerFactory = NovuQueueConfig.CONTAINER_FACTORY)
  public void processNewOrgClientRequestQueue(Map<String, String> payload,
      @Header(name = AmqpHeaders.MESSAGE_ID, required = false) String messageId) {
    trigger(novuNewOrgClientRequestId, payload, messageId);
  }

  /** Proposed workflow (docs/novu/workflows/novu-new-kyc-record.json): nothing publishes to it yet. */
  @RabbitListener(queues = "${app.novu.queue.newKycRecordQueue}", containerFactory = NovuQueueConfig.CONTAINER_FACTORY)
  public void processNewKycRecordQueue(Map<String, String> payload) {
    LOGGER.debug("Ignoring {} message: the workflow is not implemented yet", novuNewKycRecordId);
  }

  private void trigger(String workflowId, Map<String, String> payload, String messageId) {

    if (payload == null || StringUtils.isBlank(payload.get("email"))) {
      LOGGER.warn("Dropping {} message {}: it has no recipient email", workflowId, messageId);
      return;
    }

    TriggerEventRequestDto request = triggerRequest(workflowId, payload, messageId);
    try {
      novuSdk.trigger().body(request).call();
    } catch (RuntimeException e) {
      // The retry and dead-letter steps do not say why; this is where the cause shows
      LOGGER.warn("Novu trigger of {} (message {}) at {} failed: {}", workflowId, messageId, novuBaseUrl,
          e.toString());
      throw e;
    }
    LOGGER.debug("Triggered {} (transaction {})", workflowId, request.transactionId().orElse(null));
  }

  /**
   * The trigger for {@code payload}: subscriberId and email are the recipient's email address;
   * the name is firstName/surname, or recipientName for client requests.
   */
  static TriggerEventRequestDto triggerRequest(String workflowId, Map<String, String> payload, String messageId) {

    String email = payload.get("email").trim();
    String firstName = StringUtils.firstNonBlank(payload.get("firstName"), payload.get("recipientName"));

    SubscriberPayloadDto.Builder subscriber = SubscriberPayloadDto.builder()
        .subscriberId(email)
        .email(email);
    if (StringUtils.isNotBlank(firstName)) {
      subscriber.firstName(firstName);
    }
    if (StringUtils.isNotBlank(payload.get("surname"))) {
      subscriber.lastName(payload.get("surname"));
    }

    return TriggerEventRequestDto.builder()
        .workflowId(workflowId)
        .to(To2.of(subscriber.build()))
        .payload(new HashMap<String, Object>(payload))
        .transactionId(StringUtils.isNotBlank(messageId) ? messageId : UUID.randomUUID().toString())
        .build();
  }
}
