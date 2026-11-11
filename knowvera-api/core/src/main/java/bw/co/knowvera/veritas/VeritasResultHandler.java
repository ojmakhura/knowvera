package bw.co.knowvera.veritas;

import java.util.LinkedHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import bw.co.knowvera.document.DocumentAnalyticsStatus;
import bw.co.knowvera.document.DocumentDTO;
import bw.co.knowvera.document.DocumentService;
import bw.co.knowvera.document.DocumentValidationResults;
import bw.co.knowvera.document.processor.DocumentConfirmationService;
import bw.co.roguesystems.ai.job.Classification;
import bw.co.roguesystems.ai.job.JobStatus;
import bw.co.roguesystems.ai.job.ProcessingJobResult;
import bw.co.roguesystems.ai.job.StepResult;
import bw.co.roguesystems.ai.job.StepStatus;
import bw.co.roguesystems.ai.messaging.VeritasMessaging;
import bw.co.roguesystems.ai.profile.StepType;

/**
 * Applies veritas-processor results to the documents (the job's correlation id is the document
 * id): the text, the extracted fields, then the type confirmation, after which the document
 * continues to information confirmation. A failed job keeps what its completed steps produced.
 */
@Service
public class VeritasResultHandler {

    private static final Logger log = LoggerFactory.getLogger(VeritasResultHandler.class);

    private final DocumentService documentService;
    private final DocumentConfirmationService confirmationService;

    public VeritasResultHandler(DocumentService documentService, DocumentConfirmationService confirmationService) {
        this.documentService = documentService;
        this.confirmationService = confirmationService;
    }

    @RabbitListener(queues = VeritasMessaging.RESULTS_QUEUE)
    public void handle(ProcessingJobResult result) {
        DocumentDTO document = result.correlationId() == null ? null : documentService.findById(result.correlationId());
        if (document == null) {
            log.warn("veritas-processor job {} is for document {}, which does not exist", result.jobId(),
                    result.correlationId());
            return;
        }
        if (result.status() == JobStatus.FAILED) {
            log.warn("veritas-processor job {} for document {} failed: {}", result.jobId(), document.getId(),
                    result.error());
        }

        DocumentAnalyticsStatus status = null;
        for (StepResult step : result.steps()) {
            if (step.status() == StepStatus.COMPLETED && step.type() != StepType.CLASSIFICATION) {
                status = analyticsStatus(step.type());
            }
        }
        if (status != null) {
            document.setFileContent(result.text());
            if (completed(result, StepType.FIELD_EXTRACTION) && result.fields() != null) {
                document.setExtractedInformation(new LinkedHashMap<>(result.fields()));
            }
            document.setAnalyticsStatus(status);
            document = documentService.save(document);
        }

        if (completed(result, StepType.CLASSIFICATION) && result.classification() != null) {
            document = confirmationService.applyConfirmation(validationResults(result.classification()), document);
            confirmationService.dispatchInformationConfirmation(document);
        }
        log.info("Applied veritas-processor job {} ({} v{}, {}) to document {}", result.jobId(), result.profileCode(),
                result.profileVersion(), result.status(), document.getId());
    }

    /** The document's analytics status once a processing step has completed. */
    static DocumentAnalyticsStatus analyticsStatus(StepType step) {
        return switch (step) {
            case TEXT_EXTRACTION -> DocumentAnalyticsStatus.TEXT_EXTRACTION_COMPLETE;
            case TEXT_CLEANUP -> DocumentAnalyticsStatus.TEXT_CLEANUP_COMPLETE;
            case FIELD_EXTRACTION -> DocumentAnalyticsStatus.INFORMATION_EXTRACTION_COMPLETE;
            case CLASSIFICATION -> DocumentAnalyticsStatus.TYPE_CONFIRMATION_COMPLETE;
        };
    }

    /** The processor's classification as the document's validation results. */
    static DocumentValidationResults validationResults(Classification classification) {
        DocumentValidationResults results = new DocumentValidationResults();
        results.setExpectedType(classification.expectedType());
        results.setDetectedType(classification.detectedType());
        results.setMatch(classification.match());
        results.setScore(classification.score());
        results.setSignalScores(classification.signalScores() == null ? null
                : new LinkedHashMap<>(classification.signalScores()));
        results.setReason(classification.reason());
        return results;
    }

    private static boolean completed(ProcessingJobResult result, StepType type) {
        return result.steps().stream().anyMatch(step -> step.type() == type && step.status() == StepStatus.COMPLETED);
    }

}
