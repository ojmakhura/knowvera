package bw.co.centralkyc.document.processor;

import java.io.IOException;

import bw.co.centralkyc.document.DocumentValidationResults;
import bw.co.centralkyc.document.DocumentVerificationStatus;
import org.apache.commons.lang3.StringUtils;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import bw.co.centralkyc.QueueObject;
import bw.co.centralkyc.document.DocumentDTO;
import bw.co.centralkyc.document.DocumentService;
import bw.co.centralkyc.lmstudio.CompletionResponse;
import bw.co.centralkyc.properties.RabbitProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import tools.jackson.databind.json.JsonMapper;

import java.awt.image.BufferedImage;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
@RequiredArgsConstructor
public class DocumentProcessorService {

    @Value("${app.tessdata-prefix}")
    private String tessdataPrefix;

    @Value("${app.tessdata-langs}")
    private String tessdataLangs;

    private final RabbitTemplate rabbitTemplate;
    private final RabbitProperties rabbitProperties;
    private final DocumentService documentService;
    private final JsonMapper jsonMapper;

    @Async("virtualThreadExecutor")
    public CompletableFuture<String> extractText(byte[] pdfBytes) {
        return CompletableFuture.supplyAsync(() -> {
            validatePdfBytes(pdfBytes);

            // Use a single try-with-resources for the PDDocument
            try (PDDocument document = Loader.loadPDF(pdfBytes)) {

                // 1. Try standard extraction
                PDFTextStripper stripper = new PDFTextStripper();
                String extractedText = stripper.getText(document);

                // 2. Fallback to OCR if text is empty
                if (extractedText == null || extractedText.trim().isEmpty()) {
                    log.info("No text found; starting OCR process.");
                    return performOcr(document);
                }

                return extractedText;
            } catch (IOException e) {
                log.warn("Invalid or corrupted PDF payload", e);
                throw new IllegalArgumentException("Invalid PDF payload", e);
            } catch (Exception e) {
                log.error("PDF Processing failed", e);
                throw new RuntimeException("Failed to parse PDF", e);
            }
        });
    }

    private void validatePdfBytes(byte[] pdfBytes) {
        if (pdfBytes == null || pdfBytes.length < 5) {
            throw new IllegalArgumentException("PDF payload is empty or too small");
        }

        // Fast signature check to avoid sending non-PDF content into PDFBox.
        if (!(pdfBytes[0] == '%' && pdfBytes[1] == 'P' && pdfBytes[2] == 'D' && pdfBytes[3] == 'F'
                && pdfBytes[4] == '-')) {
            throw new IllegalArgumentException("File is not a valid PDF (missing %PDF- header)");
        }
    }

    private String performOcr(PDDocument document) throws IOException, TesseractException {
        StringBuilder sb = new StringBuilder();
        PDFRenderer renderer = new PDFRenderer(document);
        ITesseract tesseract = new Tesseract();
        tesseract.setDatapath(tessdataPrefix);
        tesseract.setLanguage(tessdataLangs);

        for (int page = 0; page < document.getNumberOfPages(); page++) {
            BufferedImage image = renderer.renderImageWithDPI(page, 300);
            sb.append(tesseract.doOCR(image)).append("\n");
        }
        return sb.toString();
    }

    @Async("virtualThreadExecutor")
    public void processLmCompletionResponse(CompletionResponse response, DocumentDTO document) {

        // Process the response and extract the JSON data
        response.getChoices().forEach(choice -> {
            log.info("Received response from LmStudioExtractor: {}",
                    choice.getMessage().getContent());
            // Here you can implement logic to update the document with the extracted
            if (StringUtils.isNotBlank(choice.getMessage().getContent())) {
                try {
                    // Assuming the response content is a JSON string representing the extracted

                    Map<String, Object> extractedInfo = parseLmStudioResponse(choice.getMessage().getContent());
                    document.setExtractedInformation(extractedInfo);
                    documentService.save(document);

                    // Send this to the next queue for further processing
                    rabbitTemplate.convertAndSend(
                            rabbitProperties.getDocumentConfirmationQueueExchange(),
                            rabbitProperties.getDocumentConfirmationQueueRoutingKey(),
                            new QueueObject(document.getId(), document.getTarget(), document.getTargetId()));
                } catch (Exception e) {
                    log.error("Failed to parse LmStudioExtractor response for document ID: {}",
                            document.getId(), e);
                }
            } else {
                log.warn("LmStudioExtractor response is empty for document ID: {}",
                        document.getId());

            }
        });
    }

    private Map<String, Object> parseLmStudioResponse(String responseContent) {
        try {
            Pattern pattern = Pattern.compile("\\{.*\\}", Pattern.DOTALL);
            Matcher matcher = pattern.matcher(responseContent);

            if (matcher.find()) {
                String jsonString = matcher.group();
                System.out.println("Extracted JSON String:\n" + jsonString);

                // Optional: parse into a Map
                Map<String, Object> jsonMap = jsonMapper.readValue(jsonString, Map.class);
                System.out.println("\nParsed JSON Map:\n" + jsonMap);

                return jsonMap;
            } else {
                System.out.println("No JSON found in the response.");
                return Map.of();
            }
        } catch (Exception e) {
            log.error("Failed to parse LmStudio response content", e);
            return Map.of(); // Return an empty map on parsing failure
        }
    }

    @Async("virtualThreadExecutor")
    public void processDocumentConfirmation(CompletionResponse response, DocumentDTO document) {

        log.info("Processing document confirmation for document ID: {}", document.getId());

        response.getChoices().forEach(choice -> {

            if (choice.getMessage() != null && choice.getMessage().getContent() != null) {

                Map<String, Object> extractedInfo = parseLmStudioResponse(choice.getMessage().getContent());
                if (!extractedInfo.isEmpty()) {
                    // Example: Log the extracted information
                    log.info("Document Confirmation for Document ID {}: {}", document.getId(), extractedInfo);
                    DocumentValidationResults results = jsonMapper.convertValue(extractedInfo,
                            DocumentValidationResults.class);
                    document.setValidationResults(results);

                    if (results.getMatch()) {
                        document.setVerificationStatus(DocumentVerificationStatus.VERIFIED);

                        // Send this to the next queue for further processing
                        rabbitTemplate.convertAndSend(
                                rabbitProperties.getInformationConfirmationQueueExchange(),
                                rabbitProperties.getInformationConfirmationQueueRoutingKey(),
                                new QueueObject(document.getId(), document.getTarget(), document.getTargetId()));
                    } else {

                        if (results.getScore() < 0.3) {
                            document.setVerificationStatus(DocumentVerificationStatus.REJECTED);
                        } else {
                            document.setVerificationStatus(DocumentVerificationStatus.MANUAL_REVIEW);
                        }
                    }

                    documentService.save(document);

                    this.rabbitTemplate.convertAndSend(
                            rabbitProperties.getInformationConfirmationQueueExchange(),
                            rabbitProperties.getInformationConfirmationQueueRoutingKey(),
                            new QueueObject(document.getId(), document.getTarget(), document.getTargetId()));
                }
            }
        });

    }

    @Async("virtualThreadExecutor")
    public void updateFileContent(CompletionResponse response, DocumentDTO document) {
        log.info("Updating file content for document ID: {}", document.getId());

        response.getChoices().forEach(choice -> {
            if (choice.getMessage() != null && choice.getMessage().getContent() != null) {
                String updatedContent = choice.getMessage().getContent();
                document.setFileContent(updatedContent);
                documentService.save(document);
                log.info("Updated file content for document ID: {}", document.getId());

                QueueObject queueObject = new QueueObject(
                        document.getId(),
                        document.getTarget(),
                        document.getTargetId());

                rabbitTemplate.convertAndSend(
                    rabbitProperties.getTextProcessingQueueExchange(),
                    rabbitProperties.getTextProcessingQueueRoutingKey(),
                        queueObject);
            }
        });

    }
}
