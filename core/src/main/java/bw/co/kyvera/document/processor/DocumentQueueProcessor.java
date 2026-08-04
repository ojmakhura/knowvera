package bw.co.kyvera.document.processor;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import bw.co.kyvera.QueueObject;
import bw.co.kyvera.document.DocumentAnalyticsStatus;
import bw.co.kyvera.document.DocumentDTO;
import bw.co.kyvera.document.DocumentService;
import bw.co.kyvera.minio.MinioService;
import bw.co.kyvera.properties.RabbitProperties;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DocumentQueueProcessor {

    private final static Logger log = LoggerFactory.getLogger(DocumentQueueProcessor.class);

    private final DocumentProcessorService documentProcessorService;
    private final DocumentService documentService;
    private final MinioService minioService;
    private final RabbitTemplate rabbitTemplate;
    private final RabbitProperties rabbitProperties;

    @RabbitListener(queues = "${app.rabbitmq.textExtractionQueue}")
    public void handleDocumentProcessing(QueueObject queueObject) {
        try {
            DocumentDTO document = documentService.findById(queueObject.objectId());
            System.out.println();
            if (document == null) {
                throw new IllegalArgumentException("Document not found for id: " + queueObject.objectId());
            }

            String objectName = resolveObjectName(document.getUrl());
            byte[] pdfBytes;

            // Use a more robust way to read the stream
            try (InputStream stream = minioService.downloadFile(objectName)) {
                if (stream == null) {
                    throw new IOException("Minio returned null stream for object: " + objectName);
                }
                pdfBytes = stream.readAllBytes();
            }

            // --- VALIDATION START ---
            if (pdfBytes.length < 10) {
                throw new IOException("File too small to be a PDF. Size: " + pdfBytes.length);
            }

            // Check for PDF Magic Number: %PDF- (hex: 25 50 44 46 2D)
            String header = new String(pdfBytes, 0, 5);
            if (!"%PDF-".equals(header)) {
                throw new IOException("File does not start with %PDF- header. It is likely corrupt or not a PDF.");
            }
            // --- VALIDATION END ---

            // Proceed with processing
            documentProcessorService.extractText(pdfBytes).thenAccept(text -> {
                document.setFileContent(text);
                document.setAnalyticsStatus(DocumentAnalyticsStatus.TEXT_EXTRACTION_COMPLETE);
                documentService.save(document);

                rabbitTemplate.convertAndSend(rabbitProperties.getTextCleanupQueueExchange(),
                    rabbitProperties.getTextCleanupQueueRoutingKey(), queueObject);
                    
            }).exceptionally(ex -> {
                log.error("Async extraction failed for id: {}", queueObject.objectId(), ex);
                sendToDeadLetter(queueObject);
                return null;
            });

        } catch (Exception e) {
            log.error("Failed to process document {}", queueObject.objectId(), e);
            sendToDeadLetter(queueObject);
        }
    }

    private String resolveObjectName(String rawUrlOrObjectName) {
        if (rawUrlOrObjectName == null || rawUrlOrObjectName.trim().isEmpty()) {
            throw new IllegalArgumentException("Document URL/object name is null or blank");
        }

        String candidate = rawUrlOrObjectName.trim();

        // Support either stored object names or full URLs persisted in the document
        // record.
        if (candidate.startsWith("http://") || candidate.startsWith("https://")) {
            try {
                String path = new URI(candidate).getPath();
                if (path == null || path.isBlank()) {
                    throw new IllegalArgumentException("Document URL has no object path: " + candidate);
                }
                candidate = path.startsWith("/") ? path.substring(1) : path;
            } catch (URISyntaxException e) {
                throw new IllegalArgumentException("Invalid document URL: " + candidate, e);
            }
        }

        if (candidate.isBlank()) {
            throw new IllegalArgumentException("Resolved object name is blank");
        }

        return candidate;
    }

    private void sendToDeadLetter(QueueObject queueObject) {
        rabbitTemplate.convertAndSend(
                rabbitProperties.getDocumentHandlerDeadLetterExchange(),
                rabbitProperties.getDocumentHandlerDeadLetterRoutingKey(),
                queueObject);
    }
}
