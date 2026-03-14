package bw.co.centralkyc.document.processor;

import bw.co.centralkyc.QueueObject;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import bw.co.centralkyc.document.DocumentDTO;
import bw.co.centralkyc.document.DocumentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class InformationConfirmationService {

    private final DocumentService documentService;

    @RabbitListener(queues = "${app.rabbitmq.informationConfirmationQueue}")
    public void handleInformationConfirmation(QueueObject queueObject) {
        // Implement your logic to process the extracted information here
        // For example, you could perform validation, enrichment, or trigger further workflows based on the extracted data

        log.info("Processing extracted information for document ID: {}", queueObject.documentId());
        DocumentDTO document = documentService.findById(queueObject.documentId());
        if (document != null) {
            // Example: Log the extracted information
            log.info("Extracted Information for Document ID {}: {}", queueObject.documentId(), document.getExtractedInformation());
        } else {
            log.warn("Document not found for ID: {}", queueObject.documentId());
        }

    }
}
