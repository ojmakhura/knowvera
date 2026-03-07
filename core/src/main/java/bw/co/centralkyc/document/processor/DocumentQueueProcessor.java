package bw.co.centralkyc.document.processor;

import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import bw.co.centralkyc.document.DocumentDTO;
import bw.co.centralkyc.document.DocumentService;
import bw.co.centralkyc.minio.MinioService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DocumentQueueProcessor {

    private final static Logger log = LoggerFactory.getLogger(DocumentQueueProcessor.class);

    private final DocumentProcessorService documentProcessorService;
    private final DocumentService documentService;
    private final MinioService minioService;
    
    // @RabbitListener(queues = "${app.rabbitmq.documentQueue}")
    public void handleDocumentProcessing(String documentId) {

        log.info("Received document for processing with id: {}", documentId);
        DocumentDTO document = documentService.findById(documentId);

        try {
            InputStream stream = minioService.downloadFile(document.getUrl());
            String extractedText = documentProcessorService.extractText(stream.readAllBytes());

            System.out.println("****************************************************************************");
            System.out.println(extractedText);
            System.out.println("****************************************************************************");
        } catch (Exception e) {
            log.error("Error processing document with id: {}", documentId, e);
            // Optionally, send to a fallback queue for manual review
        }

    }
}
