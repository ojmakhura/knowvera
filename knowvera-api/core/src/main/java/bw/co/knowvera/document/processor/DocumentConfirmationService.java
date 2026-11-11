package bw.co.knowvera.document.processor;

import java.util.Locale;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import bw.co.knowvera.QueueObject;
import bw.co.knowvera.document.DocumentAnalyticsStatus;
import bw.co.knowvera.document.DocumentDTO;
import bw.co.knowvera.document.DocumentService;
import bw.co.knowvera.document.DocumentValidationResults;
import bw.co.knowvera.document.DocumentVerificationStatus;
import bw.co.knowvera.kyc.KycRecord;
import bw.co.knowvera.kyc.KycRecordRepository;
import bw.co.knowvera.matcher.UniversalStringMatcher;
import bw.co.knowvera.properties.RabbitProperties;

/**
 * The KYC rules for a document type confirmation (the processor's classification), and the hand-off
 * to the next stages: information confirmation, and the verification of the document's target.
 */
@Service
public class DocumentConfirmationService {

    private static final Logger log = LoggerFactory.getLogger(DocumentConfirmationService.class);

    /** Detected and expected type names at least this similar are the same type. */
    static final double TYPE_SIMILARITY = 0.8;
    /** A mismatching document scoring below this is rejected; above it a reviewer decides. */
    static final double REJECT_SCORE = 0.3;

    private final DocumentService documentService;
    private final KycRecordRepository kycRecordRepository;
    private final UniversalStringMatcher stringMatcher;
    private final RabbitTemplate rabbitTemplate;
    private final RabbitProperties rabbitProperties;

    public DocumentConfirmationService(DocumentService documentService, KycRecordRepository kycRecordRepository,
            UniversalStringMatcher stringMatcher, RabbitTemplate rabbitTemplate, RabbitProperties rabbitProperties) {
        this.documentService = documentService;
        this.kycRecordRepository = kycRecordRepository;
        this.stringMatcher = stringMatcher;
        this.rabbitTemplate = rabbitTemplate;
        this.rabbitProperties = rabbitProperties;
    }

    /**
     * Applies a type confirmation to the document and saves it: a type mismatch rejects it (and
     * its target is verified again), a match goes to manual review (a model's verdict never
     * verifies a document on its own), a mismatch with a low score is rejected.
     */
    public DocumentDTO applyConfirmation(DocumentValidationResults results, DocumentDTO document) {
        log.info("Document confirmation for document {}: {}", document.getId(), results);
        double typeSimilarity = stringMatcher.calculateSimilarity(results.getDetectedType(), results.getExpectedType());
        results.setTypeMatch(typeSimilarity >= TYPE_SIMILARITY);
        document.setValidationResults(results);
        document.setAnalyticsStatus(DocumentAnalyticsStatus.TYPE_CONFIRMATION_COMPLETE);

        if (!results.getTypeMatch()) {
            log.info("Document {} rejected: type mismatch (similarity {})", document.getId(), typeSimilarity);
            document.setVerificationStatus(DocumentVerificationStatus.REJECTED);
            document = documentService.save(document);
            dispatchVerification(document);
            return document;
        }
        if (Boolean.TRUE.equals(results.getMatch())) {
            // A reviewer confirms it, or (with auto-verify) the independent data verification decides
            document.setVerificationStatus(DocumentVerificationStatus.MANUAL_REVIEW);
        } else if (results.getScore() != null && results.getScore() < REJECT_SCORE) {
            document.setVerificationStatus(DocumentVerificationStatus.REJECTED);
        } else {
            document.setVerificationStatus(DocumentVerificationStatus.MANUAL_REVIEW);
        }
        return documentService.save(document);
    }

    /** Information confirmation runs whatever the verification status, so its workflows always run. */
    public void dispatchInformationConfirmation(DocumentDTO document) {
        rabbitTemplate.convertAndSend(
                rabbitProperties.getInformationConfirmationQueueExchange(),
                rabbitProperties.getInformationConfirmationQueueRoutingKey(),
                new QueueObject(document.getId(), document.getTarget(), document.getTargetId()));
    }

    /** Verifies the document's target (KYC record, organisation or individual) again. */
    void dispatchVerification(DocumentDTO document) {
        if (document.getTarget() == null || StringUtils.isBlank(document.getTargetId())) {
            log.warn("Document {} has no target to verify", document.getId());
            return;
        }
        switch (document.getTarget()) {
            case KYC_RECORD -> {
                KycRecord record = kycRecordRepository.findById(UUID.fromString(document.getTargetId())).orElse(null);
                if (record == null) {
                    log.warn("KYC record {} of document {} not found", document.getTargetId(), document.getId());
                    return;
                }
                rabbitTemplate.convertAndSend(rabbitProperties.getKycVerificationQueueExchange(),
                        rabbitProperties.getKycVerificationQueueRoutingKey(),
                        new QueueObject(record.getId().toString(), record.getTarget(), record.getTargetId()));
            }
            case ORGANISATION -> rabbitTemplate.convertAndSend(
                    rabbitProperties.getOrganisationVerificationQueueExchange(),
                    rabbitProperties.getOrganisationVerificationQueueRoutingKey(),
                    new QueueObject(document.getTargetId(), document.getTarget(), document.getTargetId()));
            case INDIVIDUAL -> rabbitTemplate.convertAndSend(
                    rabbitProperties.getIndividualVerificationQueueExchange(),
                    rabbitProperties.getIndividualVerificationQueueRoutingKey(),
                    new QueueObject(document.getTargetId(), document.getTarget(), document.getTargetId()));
            default -> log.debug("No verification for target {} of document {}",
                    document.getTarget().name().toLowerCase(Locale.ROOT), document.getId());
        }
    }
}
