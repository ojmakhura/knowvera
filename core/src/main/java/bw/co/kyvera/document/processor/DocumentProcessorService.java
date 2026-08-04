package bw.co.kyvera.document.processor;

import java.io.IOException;

import bw.co.kyvera.document.DocumentValidationResults;
import bw.co.kyvera.document.DocumentVerificationStatus;
import bw.co.kyvera.gemini.GeminiService;
import bw.co.kyvera.kyc.KycRecord;
import bw.co.kyvera.kyc.KycRecordRepository;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import bw.co.kyvera.QueueObject;
import bw.co.kyvera.document.DocumentAnalyticsStatus;
import bw.co.kyvera.document.DocumentDTO;
import bw.co.kyvera.document.DocumentService;
import bw.co.kyvera.llm.LmStudioResponseChoice;
import bw.co.kyvera.llm.OllamaResponse;
import bw.co.kyvera.llm.LmStudioResponse;
import bw.co.kyvera.matcher.UniversalStringMatcher;
import bw.co.kyvera.properties.RabbitProperties;
import bw.co.kyvera.settings.SettingsDTO;
import bw.co.kyvera.settings.SettingsService;
import bw.co.kyvera.settings.Tool;
import bw.co.kyvera.settings.ToolSelectorDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import tools.jackson.databind.json.JsonMapper;

import java.awt.image.BufferedImage;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;

@Service
@Slf4j
@RequiredArgsConstructor
public class DocumentProcessorService {

    @Value("${app.tessdata-prefix}")
    private String tessdataPrefix;

    @Value("${app.tessdata-langs}")
    private String tessdataLangs;

    @Value("${app.llm.id}")
    private String llmId;

    private final KycRecordRepository kycRecordRepository;
    private final RabbitTemplate rabbitTemplate;
    private final RabbitProperties rabbitProperties;
    private final DocumentService documentService;
    private final JsonMapper jsonMapper;
    private final UniversalStringMatcher stringMatcher;
    private final SettingsService settingsService;
    private final GeminiService geminiService;

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

                    SettingsDTO settings = settingsService.loadSettings();

                    List<ToolSelectorDTO> textExtractionTools = settings.getTextExtractionTools();

                    if (CollectionUtils.isEmpty(textExtractionTools)) {
                        return performOcr(document);
                    } else {

                        if (textExtractionTools.get(0).getTool() == Tool.TESSERACT) {
                            return performOcr(document);
                        } else if (textExtractionTools.get(0).getTool() == Tool.GEMINI) {
                            try {
                                return geminiExtraction(pdfBytes);
                            } catch (Exception ex) {
                                log.warn("Gemini extraction failed; falling back to OCR.", ex);
                                return performOcr(document);
                            }
                        } else {
                            log.warn("No valid text extraction tool configured. Skipping extraction.");
                            throw new IllegalArgumentException("No valid text extraction tool configured.");
                        }
                    }
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

    private String geminiExtraction(byte[] pdfBytes) throws IOException {
        if (pdfBytes == null || pdfBytes.length == 0) {
            throw new IllegalArgumentException("PDF payload is empty");
        }

        String encodedPdf = Base64.getEncoder().encodeToString(pdfBytes);

        Message systemMessage = new SystemMessage(
                "You are an OCR assistant. Extract all visible text from the provided PDF content and return only plain text.");

        Message userMessage = new UserMessage(
                "Extract text from this base64-encoded PDF. Return only the extracted text with line breaks preserved as much as possible. "
                        + "Do not include explanations, markdown, or JSON.\n\n"
                        + encodedPdf);

        Prompt prompt = new Prompt(List.of(systemMessage, userMessage));
        ChatResponse response = geminiService.generate(prompt);

        if (response == null || response.getResult() == null || response.getResult().getOutput() == null
                || StringUtils.isBlank(response.getResult().getOutput().getText())) {
            throw new IllegalStateException("Gemini returned an empty extraction response");
        }

        return response.getResult().getOutput().getText();
    }

    /**
     * Process extracted information from the document. This method is called
     * asynchronously after receiving the response from the LLM that performed
     * information extraction. It updates the document with the extracted
     * information and then sends a message to the information confirmation queue
     * for further processing.
     *
     * @param response
     * @param document
     */
    @Async("virtualThreadExecutor")
    public CompletableFuture<Boolean> processExtractedData(Object response, DocumentDTO document) {
        if (llmId == null) {
            log.warn("LLM ID is not configured. Skipping LLM completion response processing for document ID: {}",
                    document.getId());
            return CompletableFuture.completedFuture(false);
        }

        boolean continueProcessing = true;

        if (response instanceof ChatResponse geminiResponse) {
            document = handleProcessLmCompletionResponse(geminiResponse, document);
        } else if (response instanceof LmStudioResponse lmStudioResponse) {
            document = handleProcessLmCompletionResponse(lmStudioResponse, document);
        } else if (response instanceof OllamaResponse ollamaResponse) {
            // For Ollama, we can directly handle the response without needing to extract
            // choices
            document = handleProcessLmCompletionResponse(ollamaResponse, document);
        } else {
            log.warn("Unknown LLM ID: {}. Skipping LLM completion response processing for document ID: {}",
                    llmId, document.getId());

            continueProcessing = false;
        }

        return CompletableFuture.completedFuture(continueProcessing);
    }

    private DocumentDTO handleProcessLmCompletionResponse(ChatResponse response, DocumentDTO document) {

        log.info("Received response from LLM: {}", response.getResult().getOutput().getText());

        if (StringUtils.isNotBlank(response.getResult().getOutput().getText())) {
            document = this.doHandleProcessLmCompletionResponse(response.getResult().getOutput().getText(), document);
        } else {
            log.warn("LLM response is empty for document ID: {}", document.getId());
        }

        return document;
    }

    private DocumentDTO handleProcessLmCompletionResponse(OllamaResponse response, DocumentDTO document) {
        log.info("Received response from OllamaExtractor: {}",
                response.getMessage().getContent());

        if (StringUtils.isNotBlank(response.getMessage().getContent())) {
            document = this.doHandleProcessLmCompletionResponse(response.getMessage().getContent(), document);
        } else {
            log.warn("OllamaExtractor response is empty for document ID: {}",
                    document.getId());
        }

        return document;
    }

    private DocumentDTO handleProcessLmCompletionResponse(LmStudioResponse response, DocumentDTO document) {

        // Process the response and extract the JSON data

        for (LmStudioResponseChoice choice : response.getChoices()) {
            log.info("Received response from LmStudioExtractor: {}",
                    choice.getMessage().getContent());
            // Here you can implement logic to update the document with the extracted
            if (StringUtils.isNotBlank(choice.getMessage().getContent())) {
                document = this.doHandleProcessLmCompletionResponse(choice.getMessage().getContent(), document);
            } else {
                log.warn("LmStudioExtractor response is empty for document ID: {}",
                        document.getId());

            }
        }

        return document;
    }

    private DocumentDTO doHandleProcessLmCompletionResponse(String content, DocumentDTO document) {
        try {
            // Assuming the response content is a JSON string representing the extracted

            Map<String, Object> extractedInfo = parseLmStudioResponse(content);
            document.setExtractedInformation(extractedInfo);
            document.setAnalyticsStatus(DocumentAnalyticsStatus.INFORMATION_EXTRACTION_COMPLETE);
            document = documentService.save(document);
        } catch (Exception e) {
            log.error("Failed to parse LmStudioExtractor response for document ID: {}",
                    document.getId(), e);
        }

        return document;
    }

    private Map<String, Object> parseLmStudioResponse(String responseContent) {
        try {
            Pattern pattern = Pattern.compile("\\{.*\\}", Pattern.DOTALL);
            Matcher matcher = pattern.matcher(responseContent);

            if (matcher.find()) {
                String jsonString = matcher.group();
                System.out.println("Extracted JSON String:\n" + jsonString);

                // Optional: parse into a Map
                Map<?, ?> rawJsonMap = jsonMapper.readValue(jsonString, Map.class);
                Map<String, Object> jsonMap = new HashMap<>();
                rawJsonMap.forEach((key, value) -> jsonMap.put(String.valueOf(key), value));

                if (!jsonMap.containsKey("score") && jsonMap.containsKey("signalScores")) {
                    Map<?, ?> rawSignalScores = (Map<?, ?>) jsonMap.get("signalScores");
                    double score = rawSignalScores.values().stream()
                            .map(Integer.class::cast)
                            .mapToInt(Integer::intValue)
                            .sum() / (double) rawSignalScores.size();
                    jsonMap.put("score", score);
                }

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

    /**
     * Process the document confirmation asynchronously. This method is called after
     * the information confirmation step is complete. It updates the document's
     * verification status based on the validation results and sends messages to the
     * appropriate queues for further processing (e.g., KYC verification) if needed.
     * 
     * @param response
     * @param document
     */
    @Async("virtualThreadExecutor")
    public CompletableFuture<Boolean> processDocumentConfirmation(Object response, DocumentDTO document) {
        if (llmId == null) {
            log.warn("LLM ID is not configured. Skipping file content update for document ID: {}",
                    document.getId());
            return CompletableFuture.completedFuture(false);
        }

        boolean continueProcessing = true;

        if (response instanceof ChatResponse geminiResponse) {

            document = handleProcessDocumentConfirmation(geminiResponse, document);
        } else if (response instanceof LmStudioResponse lmStudioResponse) {
            document = handleProcessDocumentConfirmation(lmStudioResponse, document);

        } else if (response instanceof OllamaResponse ollamaResponse) {
            document = handleProcessDocumentConfirmation(ollamaResponse, document);
        } else {
            log.warn("Unknown LLM ID: {}. Skipping file content update for document ID: {}",
                    llmId, document.getId());

            continueProcessing = false;
        }

        if (continueProcessing) {

            /**
             * Regardless of the verification status, we want to trigger the information
             * confirmation process to ensure that any necessary workflows or notifications
             * that depend on the information confirmation step are executed. This allows
             * the system to maintain a consistent flow of processing and ensures that all
             * necessary steps are completed in a timely manner.
             */
            this.rabbitTemplate.convertAndSend(
                    rabbitProperties.getInformationConfirmationQueueExchange(),
                    rabbitProperties.getInformationConfirmationQueueRoutingKey(),
                    new QueueObject(document.getId(), document.getTarget(), document.getTargetId()));
        }
        return CompletableFuture.completedFuture(true);
    }

    private DocumentDTO handleProcessDocumentConfirmation(ChatResponse response, DocumentDTO document) {

        log.info("Processing document confirmation for document ID: {}", document.getId());

        if (response.getResult() != null && response.getResult().getOutput() != null
                && StringUtils.isNotBlank(response.getResult().getOutput().getText())) {
            document = this.handleProcessDocumentConfirmation(response.getResult().getOutput().getText(), document);
        }

        return document;
    }

    private DocumentDTO handleProcessDocumentConfirmation(OllamaResponse response, DocumentDTO document) {

        log.info("Processing document confirmation for document ID: {}", document.getId());

        if (response.getMessage() != null && response.getMessage().getContent() != null) {
            document = this.handleProcessDocumentConfirmation(response.getMessage().getContent(), document);
        }

        return document;
    }

    private DocumentDTO handleProcessDocumentConfirmation(LmStudioResponse response, DocumentDTO document) {

        log.info("Processing document confirmation for document ID: {}", document.getId());

        for (LmStudioResponseChoice choice : response.getChoices()) {
            log.info("Received response from LmStudioExtractor for confirmation: {}",
                    choice.getMessage().getContent());

            if (choice.getMessage() != null && choice.getMessage().getContent() != null) {
                document = this.handleProcessDocumentConfirmation(choice.getMessage().getContent(), document);
            }
        }

        return document;
    }

    private DocumentDTO handleProcessDocumentConfirmation(String content, DocumentDTO document) {
        Map<String, Object> extractedInfo = parseLmStudioResponse(content);
        if (!extractedInfo.isEmpty()) {
            // Example: Log the extracted information
            log.info("Document Confirmation for Document ID {}: {}", document.getId(), extractedInfo);
            DocumentValidationResults results = jsonMapper.convertValue(extractedInfo,
                    DocumentValidationResults.class);

            double typeSimilarity = stringMatcher.calculateSimilarity(results.getDetectedType(),
                    results.getExpectedType());

            if (typeSimilarity >= 0.8) {
                results.setTypeMatch(true);
            } else {
                results.setTypeMatch(false);
                document.setVerificationStatus(DocumentVerificationStatus.REJECTED);
            }

            document.setValidationResults(results);
            document.setAnalyticsStatus(DocumentAnalyticsStatus.TYPE_CONFIRMATION_COMPLETE);

            if (document.getVerificationStatus() == DocumentVerificationStatus.REJECTED) {
                log.info("Document ID {} rejected due to type mismatch (similarity: {})",
                        document.getId(), typeSimilarity);

                document = documentService.save(document);
                dispatchVerificationQueue(document);
                // TODO: Send a notification to the user about the rejection and the reason for
                // it
                return document; // Skip further processing for this document

            }
            if (results.getMatch()) {
                document.setVerificationStatus(DocumentVerificationStatus.VERIFIED);
            } else {

                if (results.getScore() < 0.3) {
                    document.setVerificationStatus(DocumentVerificationStatus.REJECTED);
                } else {
                    document.setVerificationStatus(DocumentVerificationStatus.MANUAL_REVIEW);
                }
            }

            document = documentService.save(document);

        }

        return document;
    }

    private void dispatchVerificationQueue(DocumentDTO document) {
        if (document.getTarget() == null || StringUtils.isBlank(document.getTargetId())) {
            log.warn("Skipping verification queue dispatch for document {} because target metadata is incomplete",
                    document.getId());
            return;
        }

        switch (document.getTarget()) {
            case KYC_RECORD:
                KycRecord record = kycRecordRepository.findById(UUID.fromString(document.getTargetId())).orElse(null);
                if (record == null) {
                    log.warn("KYC Record with ID {} not found for document {}. Skipping verification queue dispatch.",
                            document.getTargetId(), document.getId());
                    return;
                }
                rabbitTemplate.convertAndSend(
                        rabbitProperties.getKycVerificationQueueExchange(),
                        rabbitProperties.getKycVerificationQueueRoutingKey(),
                        new QueueObject(record.getId().toString(), record.getTarget(), record.getTargetId()));
                break;
            case ORGANISATION:
                rabbitTemplate.convertAndSend(
                        rabbitProperties.getOrganisationVerificationQueueExchange(),
                        rabbitProperties.getOrganisationVerificationQueueRoutingKey(),
                        new QueueObject(document.getTargetId(), document.getTarget(), document.getTargetId()));
                break;
            case INDIVIDUAL:
                rabbitTemplate.convertAndSend(
                        rabbitProperties.getIndividualVerificationQueueExchange(),
                        rabbitProperties.getIndividualVerificationQueueRoutingKey(),
                        new QueueObject(document.getTargetId(), document.getTarget(), document.getTargetId()));
                break;
            default:
                log.debug("No verification queue configured for target {} on document {}",
                        document.getTarget().name().toLowerCase(Locale.ROOT), document.getId());
        }
    }

    /**
     * Updates the file content of a document based on the response from the LLM.
     *
     * @param response The response from the LLM.
     * @param document The document to update.
     * @return
     */
    @Async("virtualThreadExecutor")
    public CompletableFuture<Boolean> updateFileContent(Object response, DocumentDTO document) {
        log.info("Updating file content for document ID: {}", document.getId());

        if (llmId == null) {
            log.warn("LLM ID is not configured. Skipping file content update for document ID: {}",
                    document.getId());
            return CompletableFuture.completedFuture(false);
        }

        if (response instanceof LmStudioResponse) {
            document = updateFileContent((LmStudioResponse) response, document);
        } else if (response instanceof OllamaResponse) {
            document = updateFileContent((OllamaResponse) response, document);

        } else if (response instanceof ChatResponse) {
            document = updateFileContent((ChatResponse) response, document);
        } else {
            log.warn("Unknown LLM ID: {}. Skipping file content update for document ID: {}",
                    llmId, document.getId());
        }

        if (document.getAnalyticsStatus() == DocumentAnalyticsStatus.TEXT_CLEANUP_COMPLETE) {

            return CompletableFuture.completedFuture(true);
        }

        return CompletableFuture.completedFuture(false);
    }

    private DocumentDTO updateFileContent(ChatResponse response, DocumentDTO document) {
        log.info("Updating file content for document ID: {}", document.getId());

        if (response.getResult() != null && response.getResult().getOutput() != null
                && StringUtils.isNotBlank(response.getResult().getOutput().getText())) {
            document = this.handleContentUpdate(response.getResult().getOutput().getText(), document);
        }

        return document;
    }

    private DocumentDTO updateFileContent(LmStudioResponse response, DocumentDTO document) {
        log.info("Updating file content for document ID: {}", document.getId());

        for (LmStudioResponseChoice choice : response.getChoices()) {
            if (choice.getMessage() != null && choice.getMessage().getContent() != null) {
                document = this.handleContentUpdate(choice.getMessage().getContent(), document);
            }
        }

        return document;
    }

    private DocumentDTO updateFileContent(OllamaResponse response, DocumentDTO document) {
        log.info("Updating file content for document ID: {}", document.getId());

        String content = response.getMessage().getContent();

        if (content != null) {
            document = this.handleContentUpdate(content, document);
        }

        return document;
    }

    private DocumentDTO handleContentUpdate(String content, DocumentDTO document) {
        if (content != null) {
            String updatedContent = removeThinkBlocks(content);
            document.setFileContent(updatedContent);
            document.setAnalyticsStatus(DocumentAnalyticsStatus.TEXT_CLEANUP_COMPLETE);
            document = documentService.save(document);
            log.info("Updated file content for document ID: {}", document.getId());
        }

        return document;
    }

    String removeThinkBlocks(String input) {
        if (input == null)
            return null;

        return input.replaceAll("(?s)<think>.*?</think>", "").trim();
    }
}
