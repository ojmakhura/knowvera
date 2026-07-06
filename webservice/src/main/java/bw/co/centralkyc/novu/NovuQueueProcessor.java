package bw.co.centralkyc.novu;

import org.springframework.stereotype.Service;

import bw.co.centralkyc.keycloak.KeycloakUserService;
import bw.co.centralkyc.settings.SettingsService;
import co.novu.Novu;
import co.novu.models.components.CreateSubscriberRequestDto;
import co.novu.models.errors.ErrorDto;
import co.novu.models.operations.SubscribersControllerGetSubscriberResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NovuQueueProcessor {
    
  private final Novu novuSdk;
  private final KeycloakUserService keycloakUserService;
  private final SettingsService settingsService;

  private void checkSubscriberExists(CreateSubscriberRequestDto subscriber) {
    try {
      SubscribersControllerGetSubscriberResponse response = novuSdk.subscribers()
          .get()
          .subscriberId(subscriber.subscriberId())
          .call();

      if (!response.subscriberResponseDto().isPresent()) {
        novuSdk.subscribers()
            .create()
            .body(CreateSubscriberRequestDto.builder()
                .subscriberId(subscriber.subscriberId())
                .email(subscriber.email().get())
                .firstName(subscriber.firstName().get())
                .lastName(subscriber.lastName().get())
                .build())
            .call();
      }

    } catch(ErrorDto ex) {
      // Most SDKs throw an exception when the subscriber is not found (404)
      if (ex.code() == 404) {
        novuSdk.subscribers()
            .create()
            .body(CreateSubscriberRequestDto.builder()
                .subscriberId(subscriber.subscriberId())
                .email(subscriber.email().get())
                .firstName(subscriber.firstName().get())
                .lastName(subscriber.lastName().get())
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
}
