package bw.co.knowvera.document.processor;

import bw.co.knowvera.QueueObject;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import bw.co.knowvera.document.DocumentAnalyticsStatus;
import bw.co.knowvera.document.DocumentDTO;
import bw.co.knowvera.document.DocumentService;
import bw.co.knowvera.document.DocumentVerificationStatus;
import bw.co.knowvera.properties.RabbitProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class InformationConfirmationService {

  private final DocumentService documentService;
  private final RabbitTemplate rabbitTemplate;
  private final RabbitProperties rabbitProperties;

  @RabbitListener(queues = "${app.rabbitmq.informationConfirmationQueue}")
  public void handleInformationConfirmation(QueueObject queueObject) {

    log.info("Processing extracted information for document ID: {}", queueObject.objectId());
    DocumentDTO document = documentService.verifyData(queueObject.objectId(), "AI-AGENT");
    if (document != null) {
      // Example: Log the extracted information
      log.info("Extracted Information for Document ID {}: {}", queueObject.objectId(),
          document.getExtractedInformation());
    } else {
      log.warn("Document not found for ID: {}", queueObject.objectId());
      return;
    }

    switch (document.getTarget()) {
      case KYC_RECORD:
       this.triggerKycRecordVerification(document);
        break;
      case INDIVIDUAL:
       this.triggerIndividualVerification(document);
        break;
      case ORGANISATION:
       this.triggerOrganisationVerification(document);
        break;
      // Add more cases here for different targets as needed
      default:
        log.warn("Unknown target type: {}", document.getTarget());
    }

    document.setAnalyticsStatus(DocumentAnalyticsStatus.INFORMATION_CONFIRMATION_COMPLETE);

    document = documentService.save(document);

  }

  /**
   * If the document is rejected/verified, we want to trigger the KYC verification
   * process
   * to update the KYC record status accordingly (e.g., set to REJECTED or
   * MANUAL_REVIEW based on the document's verification status). This ensures that
   * the KYC record reflects the latest status of the document and can trigger any
   * necessary workflows or notifications based on the KYC record's status.
   */
  private void triggerKycRecordVerification(DocumentDTO document) {

    if (document.getVerificationStatus() == DocumentVerificationStatus.REJECTED
        || document.getVerificationStatus() == DocumentVerificationStatus.VERIFIED) {
      log.info("Triggering KYC verification for KYC record ID: {}", document.getTargetId());

      this.rabbitTemplate.convertAndSend(
          rabbitProperties.getKycVerificationQueueExchange(),
          rabbitProperties.getKycVerificationQueueRoutingKey(),
          new QueueObject(document.getTargetId(), document.getTarget(), document.getTargetId()));
    }
  }

  /**
   * Triggers the organisation verification process for the given document.
   * This method is a placeholder for future implementation.
   * 
   * @param document the document for which to trigger organisation verification
   */
  private void triggerOrganisationVerification(DocumentDTO document) {

    if (document.getVerificationStatus() == DocumentVerificationStatus.REJECTED
        || document.getVerificationStatus() == DocumentVerificationStatus.VERIFIED) {
      log.info("Triggering organisation verification for organisation ID: {}", document.getTargetId());

      this.rabbitTemplate.convertAndSend(
          rabbitProperties.getOrganisationVerificationQueueExchange(),
          rabbitProperties.getOrganisationVerificationQueueRoutingKey(),
          new QueueObject(document.getTargetId(), document.getTarget(), document.getTargetId()));
    }
  }

  private void triggerIndividualVerification(DocumentDTO document) {

    if (document.getVerificationStatus() == DocumentVerificationStatus.REJECTED
        || document.getVerificationStatus() == DocumentVerificationStatus.VERIFIED) {
      log.info("Triggering individual verification for individual ID: {}", document.getTargetId());

      this.rabbitTemplate.convertAndSend(
          rabbitProperties.getIndividualVerificationQueueExchange(),
          rabbitProperties.getIndividualVerificationQueueRoutingKey(),
          new QueueObject(document.getTargetId(), document.getTarget(), document.getTargetId()));
    }
  }
}
