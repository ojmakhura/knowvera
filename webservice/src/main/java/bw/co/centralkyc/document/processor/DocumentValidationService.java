package bw.co.centralkyc.document.processor;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import bw.co.centralkyc.QueueObject;
import bw.co.centralkyc.TargetEntity;
import bw.co.centralkyc.document.DocumentDTO;
import bw.co.centralkyc.document.DocumentService;
import bw.co.centralkyc.document.DocumentVerificationStatus;
import bw.co.centralkyc.document.type.DocumentTypeDTO;
import bw.co.centralkyc.document.type.DocumentTypeService;
import bw.co.centralkyc.extractor.LmStudioExtractorService;
import bw.co.centralkyc.individual.IndividualDTO;
import bw.co.centralkyc.individual.IndividualService;
import bw.co.centralkyc.kyc.KycRecordDTO;
import bw.co.centralkyc.kyc.KycRecordService;
import bw.co.centralkyc.llm.Prompt;
import bw.co.centralkyc.llm.PromptMessage;
import bw.co.centralkyc.organisation.OrganisationDTO;
import bw.co.centralkyc.organisation.OrganisationService;
import bw.co.centralkyc.properties.RabbitProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class DocumentValidationService {

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

    @RabbitListener(queues = "${app.rabbitmq.documentConfirmationQueue}")
    public void handleDocumentConfirmation(QueueObject queueObject) {
        // Implement your logic to process the extracted information here
        // For example, you could perform validation, enrichment, or trigger further
        // workflows based on the extracted data

        log.info("Processing extracted information for document ID: {}", queueObject.objectId());
        DocumentDTO document = documentService.findById(queueObject.objectId());

        boolean hasCustomPrompts = document.getValidationPrompts() != null
                && !document.getValidationPrompts().isEmpty();

        PromptMessage systemPrompt = hasCustomPrompts ? buildCustomSystemPrompt(document)
                : buildSystemPrompt();

        PromptMessage userPrompt = hasCustomPrompts ? buildCustomUserPrompt(document)
                : buildUserPrompt(document);

        if (document != null) {
            // Example: Log the extracted information
            log.info("Extracted Information for Document ID {}: {}", queueObject.objectId(),
                    document.getExtractedInformation());
        }

        Prompt completionRequest = new Prompt();
        completionRequest.setStream(false);
        completionRequest.setModel(llmModel);
        completionRequest.setMessages(List.of(systemPrompt, userPrompt));

        lmStudioExtractorService.extractInformation(completionRequest)
                .thenAccept(response -> {
                    System.out.println("✅ Got response");
                    CompletableFuture<Boolean> result = documentProcessorService.processDocumentConfirmation(response,
                            document);

                    result.thenAccept(continueProcessing -> {
                        if (continueProcessing) {

                            // if(document.getTarget() == TargetEntity.KYC_RECORD) {
                            //     triggerKycRecordVerification(document);
                            // }

                            triggerInformationConfirmation(document);
                        }
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
     * Regardless of the verification status, we want to trigger the information
     * confirmation process to ensure that any necessary workflows or notifications
     * that depend on the information confirmation step are executed. This allows
     * the system to maintain a consistent flow of processing and ensures that all
     * necessary steps are completed in a timely manner.
     */
    private void triggerInformationConfirmation(DocumentDTO document) {
        log.info("Triggering information confirmation for document ID: {}", document.getId());

        this.rabbitTemplate.convertAndSend(
                rabbitProperties.getInformationConfirmationQueueExchange(),
                rabbitProperties.getInformationConfirmationQueueRoutingKey(),
                new QueueObject(document.getId(), document.getTarget(), document.getTargetId()));
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

            KycRecordDTO record = kycRecordService.findById(document.getTargetId());

            this.rabbitTemplate.convertAndSend(
                    rabbitProperties.getKycVerificationQueueExchange(),
                    rabbitProperties.getKycVerificationQueueRoutingKey(),
                    new QueueObject(record.getId(), record.getTarget(), record.getTargetId()));
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

            OrganisationDTO org = organisationService.findById(document.getTargetId());

            this.rabbitTemplate.convertAndSend(
                    rabbitProperties.getOrganisationVerificationQueueExchange(),
                    rabbitProperties.getOrganisationVerificationQueueRoutingKey(),
                    new QueueObject(org.getId(), null, null));
        }
    }

    private void triggerIndividualVerification(DocumentDTO document) {

        if (document.getVerificationStatus() == DocumentVerificationStatus.REJECTED
                || document.getVerificationStatus() == DocumentVerificationStatus.VERIFIED) {
            log.info("Triggering individual verification for individual ID: {}", document.getTargetId());

            IndividualDTO individual = individualService.findById(document.getTargetId());

            this.rabbitTemplate.convertAndSend(
                    rabbitProperties.getIndividualVerificationQueueExchange(),
                    rabbitProperties.getIndividualVerificationQueueRoutingKey(),
                    new QueueObject(individual.getId(), null, null));
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
