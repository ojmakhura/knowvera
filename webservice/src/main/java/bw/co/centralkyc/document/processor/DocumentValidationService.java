package bw.co.centralkyc.document.processor;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import bw.co.centralkyc.QueueObject;
import bw.co.centralkyc.document.DocumentDTO;
import bw.co.centralkyc.document.DocumentService;
import bw.co.centralkyc.document.type.DocumentTypeDTO;
import bw.co.centralkyc.document.type.DocumentTypeService;
import bw.co.centralkyc.extractor.LmStudioExtractorService;
import bw.co.centralkyc.lmstudio.CompletionRequest;
import bw.co.centralkyc.lmstudio.CompletionRequestMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import tools.jackson.databind.json.JsonMapper;

@Service
@Slf4j
@RequiredArgsConstructor
public class DocumentValidationService {

    private final DocumentService documentService;
    private final DocumentTypeService documentTypeService;
    private final LmStudioExtractorService lmStudioExtractorService;
    private final DocumentProcessorService documentProcessorService;

    @RabbitListener(queues = "${app.rabbitmq.documentConfirmationQueue}")
    public void handleDocumentConfirmation(QueueObject queueObject) {
        // Implement your logic to process the extracted information here
        // For example, you could perform validation, enrichment, or trigger further
        // workflows based on the extracted data

        log.info("Processing extracted information for document ID: {}", queueObject.documentId());
        DocumentDTO document = documentService.findById(queueObject.documentId());

        boolean hasCustomPrompts = document.getValidationPrompts() != null
                && !document.getValidationPrompts().isEmpty();

        CompletionRequestMessage systemPrompt = hasCustomPrompts ? buildCustomSystemPrompt(document)
                : buildSystemPrompt();

        CompletionRequestMessage userPrompt = hasCustomPrompts ? buildCustomUserPrompt(document)
                : buildUserPrompt(document);

        if (document != null) {
            // Example: Log the extracted information
            log.info("Extracted Information for Document ID {}: {}", queueObject.documentId(),
                    document.getExtractedInformation());
        }

        CompletionRequest completionRequest = new CompletionRequest();
        completionRequest.setStream(false);
        completionRequest.setModel("local-model");
        completionRequest.setMessages(List.of(systemPrompt, userPrompt));
        lmStudioExtractorService.extractInformation(completionRequest)
                .thenAccept(response -> {
                    System.out.println("✅ Got response");
                    documentProcessorService.processDocumentConfirmation(response, document);
                })
                .exceptionally(ex -> {
                    System.err.println("❌ ERROR:");
                    ex.printStackTrace();
                    return null;
                });
    }

    private CompletionRequestMessage buildCustomSystemPrompt(DocumentDTO document) {

        Collection<DocumentTypeDTO> documentTypes = documentTypeService.getAll();
        StringBuilder systemPromptBuilder = new StringBuilder();

        for (DocumentTypeDTO type : documentTypes) {
            systemPromptBuilder.append("\t").append(type.getName()).append('\n');

            type.getExpectedFields().forEach(field -> {
                systemPromptBuilder.append("\t\t-").append(field.getField()).append('\n');
            });
            systemPromptBuilder.append('\n');
        }

        String validationSystemPrompt = document.getValidationPrompts().stream()
                .filter(prompt -> prompt.getRole().equals("system"))
                .findFirst()
                .map(p -> p.getContent())
                .orElse("");

        CompletionRequestMessage system = new CompletionRequestMessage();
        system.setRole("system");
        system.setContent(String.format(validationSystemPrompt, systemPromptBuilder.toString()));

        return system;
    }

    private CompletionRequestMessage buildCustomUserPrompt(DocumentDTO document) {
        String userPromptTemplate = document.getValidationPrompts().stream()
                .filter(prompt -> prompt.getRole().equals("user"))
                .findFirst()
                .map(p -> p.getContent())
                .orElse("");

        String userPromptContent = String.format(userPromptTemplate, document.getDocumentType(),
                document.getFileContent());

        CompletionRequestMessage user = new CompletionRequestMessage();
        user.setRole("user");
        user.setContent(userPromptContent);

        return user;
    }

    private CompletionRequestMessage buildSystemPrompt() {

        Collection<DocumentTypeDTO> documentTypes = documentTypeService.getAll();

        StringBuilder systemPromptBuilder = new StringBuilder();
        systemPromptBuilder
                .append("""
                        You are a strict KYC document verification assistant.

                        Your task is to verify whether the OCR document content matches the expected document type using signal scoring.

                        Expected document types and their signals:
                        """);

        for (DocumentTypeDTO type : documentTypes) {
            systemPromptBuilder.append("\t").append(type.getName()).append('\n');

            type.getExpectedFields().forEach(field -> {
                systemPromptBuilder.append("\t\t-").append(field.getField()).append('\n');
            });
            systemPromptBuilder.append('\n');
        }

        systemPromptBuilder.append("""
                Instructions:
                1. For each signal, indicate if it is present (1) or absent (0) in the OCR text.
                2. Compute the verification score: sum of signal values / total number of signals for the expected type and output only the result of this calculation.
                3. Compare the verification score to a threshold of 0.6:
                   - >=0.6 → match=true
                   - <0.6 → match=false
                4. Return ONLY valid JSON.

                Output format:

                {
                  "expectedType": "<TYPE>",
                  "detectedType": "<TYPE or UNKNOWN>",
                  "match": true | false,
                  "score": 0.0-1.0,
                  "signalScores": {
                     "signal1": 0|1,
                     "signal2": 0|1
                  },
                  "reason": "<short explanation>"
                }
                """);

        CompletionRequestMessage system = new CompletionRequestMessage();
        system.setRole("system");
        system.setContent(systemPromptBuilder.toString());

        return system;
    }

    private CompletionRequestMessage buildUserPrompt(DocumentDTO document) {
        String userPrompt = String.format("""
                Validate the document using signal scoring.

                Expected document type:
                %s

                OCR text:
                --------------------
                %s
                --------------------

                Return JSON only.
                """, document.getDocumentType(), document.getFileContent());

        CompletionRequestMessage user = new CompletionRequestMessage();
        user.setRole("user");
        user.setContent(userPrompt);

        return user;
    }
}
