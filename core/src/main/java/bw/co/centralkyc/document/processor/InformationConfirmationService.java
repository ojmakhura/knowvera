package bw.co.centralkyc.document.processor;

import bw.co.centralkyc.QueueObject;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import bw.co.centralkyc.document.DocumentAnalyticsStatus;
import bw.co.centralkyc.document.DocumentDTO;
import bw.co.centralkyc.document.DocumentService;
import bw.co.centralkyc.kyc.KycRecordDTO;
import bw.co.centralkyc.kyc.KycRecordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class InformationConfirmationService {

  private final DocumentService documentService;
  private final KycRecordService kycRecordService;

  @RabbitListener(queues = "${app.rabbitmq.informationConfirmationQueue}")
  public void handleInformationConfirmation(QueueObject queueObject) {
    // Implement your logic to process the extracted information here
    // For example, you could perform validation, enrichment, or trigger further
    // workflows based on the extracted data

    log.info("Processing extracted information for document ID: {}", queueObject.documentId());
    DocumentDTO document = documentService.verifyData(queueObject.documentId(), "AI-AGENT");
    if (document != null) {
      // Example: Log the extracted information
      log.info("Extracted Information for Document ID {}: {}", queueObject.documentId(),
          document.getExtractedInformation());
    } else {
      log.warn("Document not found for ID: {}", queueObject.documentId());
    }

    switch (document.getTarget()) {
      case KYC_RECORD:
        KycRecordDTO record = kycRecordService.runVerification(document.getId(), "AI-AGENT");
        break;
      case INDIVIDUAL:
        break;
      case ORGANISATION:
        // processOrganisationInformation(document);
        break;
      // Add more cases here for different targets as needed
      default:
        log.warn("Unknown target type: {}", document.getTarget());
    }

    document.setAnalyticsStatus(DocumentAnalyticsStatus.INFORMATION_CONFIRMATION_COMPLETE);

    document = documentService.save(document);

  }

}
