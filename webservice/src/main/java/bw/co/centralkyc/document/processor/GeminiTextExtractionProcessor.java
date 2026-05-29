package bw.co.centralkyc.document.processor;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import bw.co.centralkyc.QueueObject;
import bw.co.centralkyc.document.DocumentAnalyticsStatus;
import bw.co.centralkyc.document.DocumentDTO;
import bw.co.centralkyc.document.DocumentService;
import bw.co.centralkyc.extractor.GeminiExtractorService;
import bw.co.centralkyc.minio.MinioService;
import bw.co.centralkyc.properties.RabbitProperties;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GeminiTextExtractionProcessor {

    private static final Logger log = LoggerFactory.getLogger(GeminiTextExtractionProcessor.class);

    private final DocumentService documentService;
    private final MinioService minioService;
    private final GeminiExtractorService geminiExtractorService;
    private final RabbitTemplate rabbitTemplate;
    private final RabbitProperties rabbitProperties;

    @RabbitListener(queues = "${app.rabbitmq.geminiTextExtractionQueue}")
    public void handleDocumentProcessing(QueueObject queueObject) {
        try {
            DocumentDTO document = documentService.findById(queueObject.objectId());
            if (document == null) {
                throw new IllegalArgumentException("Document not found for id: " + queueObject.objectId());
            }

            String objectName = resolveObjectName(document.getUrl());
            byte[] pdfBytes;
            try (InputStream stream = minioService.downloadFile(objectName)) {
                if (stream == null) {
                    throw new IOException("Minio returned null stream for object: " + objectName);
                }
                pdfBytes = stream.readAllBytes();
            }

            String extractedText = geminiExtractorService.extractTextFromPdf(pdfBytes);
            document.setFileContent(extractedText);
            document.setAnalyticsStatus(DocumentAnalyticsStatus.TEXT_EXTRACTION_COMPLETE);
            documentService.save(document);

            rabbitTemplate.convertAndSend(
                    rabbitProperties.getTextCleanupQueueExchange(),
                    rabbitProperties.getTextCleanupQueueRoutingKey(),
                    queueObject);
        } catch (Exception e) {
            log.error("Failed to process Gemini text extraction for document {}", queueObject.objectId(), e);
            sendToDeadLetter(queueObject);
        }
    }

    private String resolveObjectName(String rawUrlOrObjectName) {
        if (rawUrlOrObjectName == null || rawUrlOrObjectName.trim().isEmpty()) {
            throw new IllegalArgumentException("Document URL/object name is null or blank");
        }

        String candidate = rawUrlOrObjectName.trim();
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
