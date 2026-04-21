package bw.co.centralkyc.messaging;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
import bw.co.roguesystems.comm.ContentType;
import bw.co.roguesystems.comm.MessagingPlatform;
import bw.co.roguesystems.comm.message.CommMessageDTO;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ClientRequestNotification {

    @Value("${app.registration-url}")
    private String registrationUrl;

    private final SettingsRepository settingsRepository;
    private final IndividualRepository individualRepository;

    private final String requestEmailTemplate = """
            <!DOCTYPE html>
            <html>
            <head>
              <meta charset="UTF-8">
              <title>KYC Registration Invitation</title>
            </head>
            <body style="font-family: Arial, sans-serif; line-height: 1.6; color: #333;">
              <p>Dear {{recipientName}},</p>

              <p>
                You are invited to complete your registration on our secure KYC platform
                following a request from <strong>{{organisationName}}</strong>.
              </p>

              <p>
                To ensure timely access to services, please note that this invitation is
                <strong>valid for {{expiryDays}} days</strong>. If registration is not completed
                within this period, the invitation link may expire and you may need to request
                a new one through <strong>{{organisationName}}</strong>.
              </p>

              <p>
                Completing your registration will allow you to securely submit your
                identification and verification documents, as required to access or continue
                using services provided by <strong>{{organisationName}}</strong>.
              </p>

              <p><strong>How to get started:</strong></p>
              <ol>
                <li>
                  Click the link below to access the KYC platform:<br/>
                  <a href="{{kycPortalLink}}" style="color: #1a73e8;">{{kycPortalLink}}</a>
                </li>
                <li>Complete your registration by creating your account</li>
                <li>Log in and upload the requested documents</li>
                <li>Submit your information for review</li>
              </ol>

              <p>
                All information you provide will be handled securely and used strictly for
                identity verification purposes, in accordance with applicable data protection
                and privacy regulations.
              </p>

              <p>
                If you have any questions or require assistance during the registration
                process, please contact <strong>{{supportContact}}</strong>.
              </p>

              <p>
                Kind regards,<br/>
                
                KYC Platform Support Team<br/>
                {{platformName}}<br/>
                <a href="{{platformUrl}}" style="color: #1a73e8;">{{platformUrl}}</a>
              </p>
            </body>
            </html>
                """;

    private final String accountCreationTemplate = """
            <!DOCTYPE html>
            <html>
            <head>
              <meta charset="UTF-8">
              <title>Account Creation Notification</title>
            </head>
            <body style="font-family: Arial, sans-serif; line-height: 1.6; color: #333;">
              <p>Dear {{recipientName}},</p>

              <p>
                Your account has been successfully created on our KYC platform.
              </p>

              <p>
                You can now log in and manage your KYC information securely using the following details:
                URL: <a href="{{platformUrl}}" style="color: #1a73e8;">{{platformUrl}}</a>
                Username: {{username}}<br/>
                Temporary Password: {{temporaryPassword}}
              </p>

              <p>
                If you have any questions or require assistance, please contact
                <strong>{{supportContact}}</strong>.
              </p>

              <p>
                Kind regards,<br/>
                
                KYC Platform Support Team<br/>
                {{platformName}}<br/>
                <a href="{{platformUrl}}" style="color: #1a73e8;">{{platformUrl}}</a>
              </p>
            </body>
            </html>
                """;
    
    @Async("virtualThreadExecutor")
    public void queueEmailNotificationsForRequests(List<ClientRequest> clientRequests,
            Map<String, String> tokenMap, String organisation) {

        Settings settings = settingsRepository.findAll().stream().findFirst().orElseThrow();
        // TODO: Implementation for queuing email notifications
        List<CommMessageDTO> notifiedRequests = new ArrayList<>();
        String subject = "Client Request Notification from " + organisation;

        String tmp = requestEmailTemplate
                .replace("{{organisationName}}", organisation)
                .replace("{{platformName}}", settings.getPlatformName())
                .replace("{{platformUrl}}", settings.getKycPortalLink())
                .replace("{{supportContact}}", settings.getSupportContact());

        for (ClientRequest request : clientRequests) {
            String token = tokenMap.get(request.getTargetId());
            // Create and queue email notification with the token
            // For now, just print to console (not recommended for production)
            System.out.println("Queue email notification for Request ID: " + request.getId() + ", Token: " + token);

            CommMessageDTO message = new CommMessageDTO();
            message.setPlatform(MessagingPlatform.EMAIL);
            message.setContentType(ContentType.MIME);
            message.setSubject(subject);

            tmp = tmp.replace("{{recipientName}}", request.getTargetId())
                    .replace("{{kycPortalLink}}",
                            String.format("%s/%s?token=%s", registrationUrl, request.getId(), token)); // Placeholder

            System.out.println(tmp);

            message.setText(tmp);

            notifiedRequests.add(message);
        }

    }

    @Async("virtualThreadExecutor")
    public void queueAccountCreationNotification(List<ClientRequestDTO> clientRequests, Map<String, String> tokenMap, String organisation) {

        Settings settings = settingsRepository.findAll().stream().findFirst().orElseThrow();

        String subject = "Account Created on KYC Platform";

        String tmp = accountCreationTemplate
                .replace("{{organisationName}}", organisation)
                .replace("{{platformName}}", settings.getPlatformName())
                .replace("{{platformUrl}}", settings.getKycPortalLink())
                .replace("{{supportContact}}", settings.getSupportContact());

        for (ClientRequestDTO request : clientRequests) {

            // Create and queue email notification
            // For now, just print to console (not recommended for production)
            System.out.println("Queue account creation notification for Request ID: " + request.getId());

            CommMessageDTO message = new CommMessageDTO();
            message.setPlatform(MessagingPlatform.EMAIL);
            message.setContentType(ContentType.MIME);
            message.setSubject(subject);

            StringBuilder name = new StringBuilder();
            String username = null;

            if(request.getTarget() == TargetEntity.INDIVIDUAL) {

                Individual individual = individualRepository.findById(UUID.fromString(request.getTargetId())).orElseThrow();

                name.append(individual.getFirstName());
                if(individual.getMiddleName() != null && !individual.getMiddleName().isBlank()) {
                    name.append(" ").append(individual.getMiddleName());
                }
                name.append(" ").append(individual.getSurname());
                
                username = individual.getEmailAddress();
            } else if(request.getOrganisation() != null) {
                // name = request.getOrganisation().getRegisteredName();
                // username = request.getOrganisation().getContactEmail();
            }

            tmp = tmp.replace("{{recipientName}}", name.toString())
                    .replace("{{username}}", username)
                    .replace("{{temporaryPassword}}", tokenMap.get(request.getTargetId())); // Placeholder

            System.out.println(tmp);

            message.setText(tmp);
        }
    }

}
