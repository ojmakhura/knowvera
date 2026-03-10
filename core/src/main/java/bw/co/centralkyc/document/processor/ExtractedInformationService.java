package bw.co.centralkyc.document.processor;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import bw.co.centralkyc.document.DocumentDTO;
import bw.co.centralkyc.document.DocumentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class ExtractedInformationService {

    private final DocumentService documentService;

    @RabbitListener(queues = "${app.rabbitmq.extractedInformationQueue}")
    public void processExtractedInformation(String documentId) {
        // Implement your logic to process the extracted information here
        // For example, you could perform validation, enrichment, or trigger further workflows based on the extracted data

        log.info("Processing extracted information for document ID: {}", documentId);
        DocumentDTO document = documentService.findById(documentId);
        if (document != null) {
            // Example: Log the extracted information
            log.info("Extracted Information for Document ID {}: {}", documentId, document.getExtractedInformation());
        } else {
            log.warn("Document not found for ID: {}", documentId);  
        }

    }
}
