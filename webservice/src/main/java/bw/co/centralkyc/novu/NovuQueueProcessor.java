package bw.co.centralkyc.novu;

import java.util.Map;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import bw.co.centralkyc.keycloak.KeycloakUserService;
import bw.co.centralkyc.settings.SettingsService;
import co.novu.Novu;
import co.novu.models.components.CreateSubscriberRequestDto;
import co.novu.models.components.To2;
import co.novu.models.components.TriggerEventRequestDto;
import co.novu.models.components.TriggerEventRequestDtoOverrides;
import co.novu.models.errors.ErrorDto;
import co.novu.models.operations.EventsControllerTriggerResponse;
import co.novu.models.operations.SubscribersControllerGetSubscriberResponse;
import lombok.RequiredArgsConstructor;

@Service
public class NovuQueueProcessor {
    
  private final Novu novuSdk;
  private final KeycloakUserService keycloakUserService;
  private final SettingsService settingsService;

  public NovuQueueProcessor(Novu novuSdk, KeycloakUserService keycloakUserService,
      SettingsService settingsService) {
    this.novuSdk = novuSdk;
    this.keycloakUserService = keycloakUserService;
    this.settingsService = settingsService;
  }

  @Value("${app.novu.queue.newUserNovuId}")
  private String novuNewUserId;

  @Value("${app.novu.queue.newOrgUserNovuId}")
  private String novuNewOrgUserId;

  @Value("${app.novu.queue.newOrgClientRequestNovuId}")
  private String novuNewOrgClientRequestId;

  @Value("${app.novu.queue.newKycRecordNovuId}")
  private String novuNewKycRecordId;

  private void checkSubscriberExists(Map<String, String> payload) {
    try {
      SubscribersControllerGetSubscriberResponse response = novuSdk.subscribers()
          .get()
          .subscriberId(payload.get("email"))
          .call();

      if (!response.subscriberResponseDto().isPresent()) {
        novuSdk.subscribers()
            .create()
            .body(CreateSubscriberRequestDto.builder()
                .subscriberId(payload.get("email"))
                .email(payload.get("email"))
                .firstName(payload.get("firstName"))
                .lastName(payload.get("surname"))
                .build())
            .call();
      }

    } catch(ErrorDto ex) {
      // Most SDKs throw an exception when the subscriber is not found (404)
      if (ex.code() == 404) {
        novuSdk.subscribers()
            .create()
            .body(CreateSubscriberRequestDto.builder()
                .subscriberId(payload.get("email"))
                .email(payload.get("email"))
                .firstName(payload.get("firstName"))
                .lastName(payload.get("surname"))
                .build())
            .call();
      } else {
        throw ex;
      }
    } catch (Exception ex) {
      // Most SDKs throw an exception when the subscriber is not found (404)

      throw ex;

    } 
  }

  @RabbitListener(queues = "${app.novu.queue.newUserQueue}")
  public void processNewUserQueue(Map<String, String> payload) {

    checkSubscriberExists(payload);

    EventsControllerTriggerResponse res = novuSdk.trigger()
        .body(TriggerEventRequestDto.builder()
            .workflowId(novuNewUserId)
            .to(To2.of(payload.get("email")))
            .payload((Map)payload)
            .overrides(TriggerEventRequestDtoOverrides.builder()
                .build())
            .build())
        .call();

    if (res.triggerEventResponseDto().isPresent()) {
      // handle response
    }
  }

  @RabbitListener(queues = "${app.novu.queue.newOrgUserQueue}")
  public void processNewOrgUserQueue(Map<String, String> payload) {

    checkSubscriberExists(payload);

    EventsControllerTriggerResponse res = novuSdk.trigger()
        .body(TriggerEventRequestDto.builder()
            .workflowId(novuNewOrgUserId)
            .to(To2.of(payload.get("email")))
            .payload((Map)payload)
            .overrides(TriggerEventRequestDtoOverrides.builder()
                .build())
            .build())
        .call();
  }

  @RabbitListener(queues = "${app.novu.queue.newOrgClientRequestQueue}")
  public void processNewOrgClientRequestQueue(Map<String, String> payload) {

    checkSubscriberExists(payload);

    EventsControllerTriggerResponse res = novuSdk.trigger()
        .body(TriggerEventRequestDto.builder()
            .workflowId(novuNewOrgClientRequestId)
            .to(To2.of(payload.get("email")))
            .payload((Map)payload)
            .overrides(TriggerEventRequestDtoOverrides.builder()
                .build())
            .build())
        .call();
  }

  @RabbitListener(queues = "${app.novu.queue.newKycRecordQueue}")
  public void processNewKycRecordQueue(Map<String, String> payload) {

    checkSubscriberExists(payload);

    // EventsControllerTriggerResponse res = novuSdk.trigger()
    //     .body(TriggerEventRequestDto.builder()
    //         .workflowId(novuNewKycRecordId)
    //         .to(To2.of(payload.get("email")))
    //         .payload((Map)payload)
    //         .overrides(TriggerEventRequestDtoOverrides.builder()
    //             .build())
    //         .build())
    //     .call();
  }
}
