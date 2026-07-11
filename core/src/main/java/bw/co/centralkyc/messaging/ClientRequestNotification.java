package bw.co.centralkyc.messaging;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import bw.co.centralkyc.TargetEntity;
import bw.co.centralkyc.individual.Individual;
import bw.co.centralkyc.individual.IndividualRepository;
import bw.co.centralkyc.organisation.client.ClientRequest;
import bw.co.centralkyc.organisation.client.ClientRequestDTO;
import bw.co.centralkyc.settings.Settings;
import bw.co.centralkyc.settings.SettingsRepository;
// import bw.co.roguesystems.comm.ContentType;
// import bw.co.roguesystems.comm.MessagingPlatform;
// import bw.co.roguesystems.comm.message.CommMessageDTO;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
@Slf4j
public class ClientRequestNotification {

  @Value("${app.registration-url}")
  private String registrationUrl;

  @Value("${app.novu.queue.newOrgClientRequestQueueExchange}")
  private String novuNewOrgClientRequestQueueExchange;

  @Value("${app.novu.queue.newOrgClientRequestQueueRoutingKey}")
  private String novuNewOrgClientRequestQueueRoutingKey;

  private final SettingsRepository settingsRepository;
  private final IndividualRepository individualRepository;
  private final RabbitTemplate rabbitTemplate;

  // private final String accountCreationTemplate = """
  // <!DOCTYPE html>
  // <html>
  // <head>
  // <meta charset="UTF-8">
  // <title>Account Creation Notification</title>
  // </head>
  // <body style="font-family: Arial, sans-serif; line-height: 1.6; color: #333;">
  // <p>Dear {{recipientName}},</p>

  // <p>
  // Your account has been successfully created on our KYC platform.
  // </p>

  // <p>
  // You can now log in and manage your KYC information securely using the
  // following details:
  // URL: <a href="{{platformUrl}}" style="color: #1a73e8;">{{platformUrl}}</a>
  // Username: {{username}}<br/>
  // Temporary Password: {{temporaryPassword}}
  // </p>

  // <p>
  // If you have any questions or require assistance, please contact
  // <strong>{{supportContact}}</strong>.
  // </p>

  // <p>
  // Kind regards,<br/>

  // KYC Platform Support Team<br/>
  // {{platformName}}<br/>
  // <a href="{{platformUrl}}" style="color: #1a73e8;">{{platformUrl}}</a>
  // </p>
  // </body>
  // </html>
  // """;

  @Async("virtualThreadExecutor")
  public void queueEmailNotificationsForRequests(List<ClientRequest> clientRequests,
      Map<String, String> tokenMap, String organisation) {

    Settings settings = settingsRepository.findAll().stream().findFirst().orElseThrow();
    
    for (ClientRequest request : clientRequests) {
      String token = tokenMap.get(request.getTargetId());
      // Create and queue email notification with the token
      // For now, just print to console (not recommended for production)
      log.info("Queue email notification for Request ID: " +
          request.getId() + ", Token: " + token);

      Map<String, Object> messagePayload = new HashMap<>();
      messagePayload.put("organisationName", organisation);
      messagePayload.put("platformName", settings.getPlatformName());
      messagePayload.put("platformUrl", settings.getKycPortalLink());
      messagePayload.put("supportContact", settings.getSupportContact());
      messagePayload.put("recipientName", request.getTargetId());
      messagePayload.put("kycPortalLink", String.format("%s/%s?token=%s", registrationUrl, request.getId(), token));

      rabbitTemplate.convertAndSend(novuNewOrgClientRequestQueueExchange, novuNewOrgClientRequestQueueRoutingKey, messagePayload);
    }
  }

  // @RabbitListener(queues = "${app.novu.queue.newUserQueue}")
  // public void queueAccountCreationNotification(List<ClientRequestDTO>
  // clientRequests, Map<String, String> tokenMap, String organisation) {

  // Settings settings =
  // settingsRepository.findAll().stream().findFirst().orElseThrow();

  // String subject = "Account Created on KYC Platform";

  // String tmp = accountCreationTemplate
  // .replace("{{organisationName}}", organisation)
  // .replace("{{platformName}}", settings.getPlatformName())
  // .replace("{{platformUrl}}", settings.getKycPortalLink())
  // .replace("{{supportContact}}", settings.getSupportContact());

  // for (ClientRequestDTO request : clientRequests) {

  // // Create and queue email notification
  // // For now, just print to console (not recommended for production)
  // System.out.println("Queue account creation notification for Request ID: " +
  // request.getId());

  // CommMessageDTO message = new CommMessageDTO();
  // message.setPlatform(MessagingPlatform.EMAIL);
  // message.setContentType(ContentType.MIME);
  // message.setSubject(subject);

  // StringBuilder name = new StringBuilder();
  // String username = null;

  // if(request.getTarget() == TargetEntity.INDIVIDUAL) {

  // Individual individual =
  // individualRepository.findById(UUID.fromString(request.getTargetId())).orElseThrow();

  // name.append(individual.getFirstName());
  // if(individual.getMiddleName() != null &&
  // !individual.getMiddleName().isBlank()) {
  // name.append(" ").append(individual.getMiddleName());
  // }
  // name.append(" ").append(individual.getSurname());

  // username = individual.getEmailAddress();
  // } else if(request.getOrganisation() != null) {
  // // name = request.getOrganisation().getRegisteredName();
  // // username = request.getOrganisation().getContactEmail();
  // }

  // tmp = tmp.replace("{{recipientName}}", name.toString())
  // .replace("{{username}}", username)
  // .replace("{{temporaryPassword}}", tokenMap.get(request.getTargetId())); //
  // Placeholder

  // System.out.println(tmp);

  // message.setText(tmp);
  // }
  // }

}
