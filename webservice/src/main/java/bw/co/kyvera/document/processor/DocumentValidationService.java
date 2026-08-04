package bw.co.kyvera.document.processor;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import bw.co.kyvera.QueueObject;
import bw.co.kyvera.TargetEntity;
import bw.co.kyvera.document.DocumentDTO;
import bw.co.kyvera.document.DocumentService;
import bw.co.kyvera.document.DocumentVerificationStatus;
import bw.co.kyvera.document.type.DocumentTypeDTO;
import bw.co.kyvera.document.type.DocumentTypeService;
import bw.co.kyvera.extractor.LmStudioExtractorService;
import bw.co.kyvera.gemini.GeminiService;
import bw.co.kyvera.individual.IndividualDTO;
import bw.co.kyvera.individual.IndividualService;
import bw.co.kyvera.kyc.KycRecordDTO;
import bw.co.kyvera.kyc.KycRecordService;
import bw.co.kyvera.llm.PromptMessage;
import bw.co.kyvera.organisation.OrganisationDTO;
import bw.co.kyvera.organisation.OrganisationService;
import bw.co.kyvera.properties.RabbitProperties;
import bw.co.kyvera.settings.SettingsDTO;
import bw.co.kyvera.settings.SettingsService;
import bw.co.kyvera.settings.Tool;
import bw.co.kyvera.settings.ToolSelectorDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@Slf4j
public class DocumentValidationService {

    private static final Logger log = LoggerFactory.getLogger(DocumentValidationService.class);

    @Value("${app.llm.model}")
    private String llmModel;

    private final OrganisationService organisationService;
    private final IndividualService individualService;
    private final DocumentService documentService;
    private final DocumentTypeService documentTypeService;
    private final LmStudioExtractorService lmStudioExtractorService;
    private final DocumentProcessorService documentProcessorService;
    private final KycRecordService kycRecordService;
    private final RabbitTemplate rabbitTemplate;
    private final RabbitProperties rabbitProperties;
    private final SettingsService settingsService;
    private final GeminiService geminiService;

    public DocumentValidationService(OrganisationService organisationService, IndividualService individualService,
            DocumentService documentService, DocumentTypeService documentTypeService,
            LmStudioExtractorService lmStudioExtractorService, DocumentProcessorService documentProcessorService,
            KycRecordService kycRecordService, RabbitTemplate rabbitTemplate, RabbitProperties rabbitProperties,
            SettingsService settingsService, GeminiService geminiService) {

        this.organisationService = organisationService;
        this.individualService = individualService;
        this.documentService = documentService;
        this.documentTypeService = documentTypeService;
        this.lmStudioExtractorService = lmStudioExtractorService;
        this.documentProcessorService = documentProcessorService;
        this.kycRecordService = kycRecordService;
        this.rabbitTemplate = rabbitTemplate;
        this.rabbitProperties = rabbitProperties;
        this.settingsService = settingsService;
        this.geminiService = geminiService;
    }

    @RabbitListener(queues = "${app.rabbitmq.documentConfirmationQueue}")
    public void handleDocumentConfirmation(QueueObject queueObject) {
        // Implement your logic to process the extracted information here
        // For example, you could perform validation, enrichment, or trigger further
        // workflows based on the extracted data

        log.info("Processing extracted information for document ID: {}", queueObject.objectId());
        DocumentDTO document = documentService.findById(queueObject.objectId());
        SettingsDTO settings = settingsService.loadSettings();
        List<ToolSelectorDTO> tools = settings.getDocumentConfirmationTools();
        
        if(CollectionUtils.isEmpty(tools)) {
            log.warn("No tools configured for document confirmation. Skipping processing for document ID: {}", queueObject.objectId());
            return;
        }

        if(tools.get(0).getTool() == Tool.GEMINI) {
            log.info("Using Gemini for document confirmation for document ID: {}", queueObject.objectId());
            // Implement Gemini processing logic here
            geminiConfirmation(document);
        } else if(tools.get(0).getTool() == Tool.LM_STUDIO) {
            log.info("Using LM Studio for document confirmation for document ID: {}", queueObject.objectId());
            lmStudioConfirmation(document);
        } else {
            log.warn("Unknown tool configured for document confirmation. Skipping processing for document ID: {}", queueObject.objectId());
        }
    }

    private void geminiConfirmation(DocumentDTO document) {
        
        boolean hasCustomPrompts = document.getValidationPrompts() != null
                && !document.getValidationPrompts().isEmpty();

        PromptMessage systemPrompt = hasCustomPrompts ? buildCustomSystemPrompt(document)
                : buildSystemPrompt();

        PromptMessage userPrompt = hasCustomPrompts ? buildCustomUserPrompt(document)
                : buildUserPrompt(document);

        Message systemMessage = new SystemMessage(systemPrompt.getContent());
        Message userMessage = new UserMessage(userPrompt.getContent());

        Prompt request = new Prompt(List.of(systemMessage, userMessage));

        ChatResponse response = geminiService.generate(request);
        CompletableFuture<Boolean> result = documentProcessorService.processDocumentConfirmation(response, document);

        result.thenAccept(continueProcessing -> {
            // Information confirmation is already dispatched in
            // DocumentProcessorService.processDocumentConfirmation.
        }).exceptionally(ex -> {
            System.err.println("❌ ERROR during Gemini document processing:");
            ex.printStackTrace();
            return null;
        });
    }

    private void lmStudioConfirmation(DocumentDTO document) {
        // DocumentDTO document = documentService.findById(queueObject.objectId());

        boolean hasCustomPrompts = document.getValidationPrompts() != null
                && !document.getValidationPrompts().isEmpty();

        PromptMessage systemPrompt = hasCustomPrompts ? buildCustomSystemPrompt(document)
                : buildSystemPrompt();

        PromptMessage userPrompt = hasCustomPrompts ? buildCustomUserPrompt(document)
                : buildUserPrompt(document);

        if (document != null) {
            // Example: Log the extracted information
            log.info("Extracted Information for Document ID {}: {}", document.getId(),
                    document.getExtractedInformation());
        }

        bw.co.kyvera.llm.Prompt completionRequest = new bw.co.kyvera.llm.Prompt();
        completionRequest.setStream(false);
        completionRequest.setModel(llmModel);
        completionRequest.setMessages(List.of(systemPrompt, userPrompt));

        lmStudioExtractorService.extractInformation(completionRequest)
                .thenAccept(response -> {
                    System.out.println("✅ Got response");
                    CompletableFuture<Boolean> result = documentProcessorService.processDocumentConfirmation(response,
                            document);

                    result.thenAccept(continueProcessing -> {
                        // Information confirmation is already dispatched in
                        // DocumentProcessorService.processDocumentConfirmation.
                    }).exceptionally(ex -> {
                        System.err.println("❌ ERROR during document processing:");
                        ex.printStackTrace();
                        return null;
                    });
                }).exceptionally(ex ->

                {
                    System.err.println("❌ ERROR:");
                    ex.printStackTrace();
                    return null;
                });
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
                    new QueueObject(document.getId(), document.getTarget(), document.getTargetId()));
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

    private PromptMessage buildCustomSystemPrompt(DocumentDTO document) {

        Collection<DocumentTypeDTO> documentTypes = documentTypeService.getAll();
        StringBuilder systemPromptBuilder = new StringBuilder();

        for (DocumentTypeDTO type : documentTypes) {
            systemPromptBuilder.append("\t").append(type.getName()).append('\n');

            type.getExpectedFields().forEach(field -> {
                systemPromptBuilder
                        .append("\t\t-")
                        .append(field.getField());

                if (field.getMandatory() != null && field.getMandatory()) {
                    systemPromptBuilder.append(" (mandatory)");
                }

                systemPromptBuilder.append('\n');
            });
            systemPromptBuilder.append('\n');
        }

        String validationSystemPrompt = document.getValidationPrompts().stream()
                .filter(prompt -> prompt.getRole().equals("system"))
                .findFirst()
                .map(p -> p.getContent())
                .orElse("");

        PromptMessage system = new PromptMessage();
        system.setRole("system");
        system.setContent(String.format(validationSystemPrompt, systemPromptBuilder.toString()));

        return system;
    }

    private PromptMessage buildCustomUserPrompt(DocumentDTO document) {
        String userPromptTemplate = document.getValidationPrompts().stream()
                .filter(prompt -> prompt.getRole().equals("user"))
                .findFirst()
                .map(p -> p.getContent())
                .orElse("");

        String userPromptContent = String.format(userPromptTemplate, document.getDocumentType(),
                document.getFileContent());

        PromptMessage user = new PromptMessage();
        user.setRole("user");
        user.setContent(userPromptContent);

        return user;
    }

    private PromptMessage buildSystemPrompt() {

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

                systemPromptBuilder
                        .append("\t\t")
                        .append(field.getField());

                if (field.getMandatory() != null && field.getMandatory()) {
                    systemPromptBuilder.append(" (mandatory)");

                }

                systemPromptBuilder.append('\n');
            });
            systemPromptBuilder.append('\n');
        }

        systemPromptBuilder
                .append("""
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

        PromptMessage system = new PromptMessage();
        system.setRole("system");
        system.setContent(systemPromptBuilder.toString());

        return system;
    }

    private PromptMessage buildUserPrompt(DocumentDTO document) {
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

        PromptMessage user = new PromptMessage();
        user.setRole("user");
        user.setContent(userPrompt);

        return user;
    }
}
