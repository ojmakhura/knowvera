package bw.co.knowvera.veritas;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
import bw.co.roguesystems.ai.profile.ProviderType;
import bw.co.roguesystems.ai.profile.StepType;

@ExtendWith(MockitoExtension.class)
class VeritasResultHandlerTest {

    @Mock
    private DocumentService documentService;
    @Mock
    private DocumentConfirmationService confirmationService;

    private VeritasResultHandler handler;
    private DocumentDTO document;

    @BeforeEach
    void setUp() {
        handler = new VeritasResultHandler(documentService, confirmationService);
        document = new DocumentDTO();
        document.setId("doc-1");
    }

    private static StepResult step(StepType type, StepStatus status) {
        return new StepResult(type, status, status == StepStatus.COMPLETED ? ProviderType.OLLAMA : null, "gemma4", 10,
                List.of());
    }

    private static ProcessingJobResult result(JobStatus status, Classification classification, StepResult... steps) {
        return new ProcessingJobResult("job-1", "doc-1", "default", 2, status, "clean text",
                Map.of("idNumber", "123"), classification, List.of(steps), null, Map.of());
    }

    @Test
    void appliesTextFieldsAndTheConfirmation() {
        when(documentService.findById("doc-1")).thenReturn(document);
        when(documentService.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(confirmationService.applyConfirmation(any(), any())).thenAnswer(i -> i.getArgument(1));
        Classification classification = new Classification("Omang", "Omang", true, 0.9, Map.of("idNumber", 1), null);

        handler.handle(result(JobStatus.COMPLETED, classification,
                step(StepType.TEXT_EXTRACTION, StepStatus.COMPLETED), step(StepType.TEXT_CLEANUP, StepStatus.COMPLETED),
                step(StepType.FIELD_EXTRACTION, StepStatus.COMPLETED), step(StepType.CLASSIFICATION, StepStatus.COMPLETED)));

        assertEquals("clean text", document.getFileContent());
        assertEquals(Map.of("idNumber", "123"), document.getExtractedInformation());
        assertEquals(DocumentAnalyticsStatus.INFORMATION_EXTRACTION_COMPLETE, document.getAnalyticsStatus());
        verify(confirmationService).applyConfirmation(argThat(results -> "Omang".equals(results.getDetectedType())
                && Boolean.TRUE.equals(results.getMatch()) && results.getScore() == 0.9), any());
        verify(confirmationService).dispatchInformationConfirmation(document);
    }

    @Test
    void aFailedJobKeepsWhatItProducedAndDoesNotContinue() {
        when(documentService.findById("doc-1")).thenReturn(document);
        when(documentService.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        handler.handle(result(JobStatus.FAILED, null,
                step(StepType.TEXT_EXTRACTION, StepStatus.COMPLETED), step(StepType.TEXT_CLEANUP, StepStatus.FAILED)));

        assertEquals("clean text", document.getFileContent());
        assertEquals(DocumentAnalyticsStatus.TEXT_EXTRACTION_COMPLETE, document.getAnalyticsStatus());
        assertNull(document.getExtractedInformation());
        verify(confirmationService, never()).applyConfirmation(any(), any());
        verify(confirmationService, never()).dispatchInformationConfirmation(any());
    }

    @Test
    void anAnalysisRunLeavesTheTextAndOnlyConfirms() {
        when(documentService.findById("doc-1")).thenReturn(document);
        when(confirmationService.applyConfirmation(any(), any())).thenAnswer(i -> i.getArgument(1));
        document.setFileContent("stored text");

        handler.handle(result(JobStatus.COMPLETED, new Classification("Omang", "Payslip", false, 0.1, Map.of(), "no"),
                step(StepType.TEXT_EXTRACTION, StepStatus.SKIPPED), step(StepType.CLASSIFICATION, StepStatus.COMPLETED)));

        assertEquals("stored text", document.getFileContent());
        verify(documentService, never()).save(any());
        verify(confirmationService).dispatchInformationConfirmation(document);
    }

    @Test
    void ignoresResultsForUnknownDocuments() {
        handler.handle(result(JobStatus.COMPLETED, null, step(StepType.TEXT_EXTRACTION, StepStatus.COMPLETED)));

        verify(documentService, never()).save(any());
    }

    @Test
    void mapsTheClassificationFieldByField() {
        DocumentValidationResults results = VeritasResultHandler.validationResults(
                new Classification("Omang", "UNKNOWN", false, 0.25, Map.of("idNumber", 0), "no id number"));

        assertEquals("Omang", results.getExpectedType());
        assertEquals("UNKNOWN", results.getDetectedType());
        assertEquals(Boolean.FALSE, results.getMatch());
        assertEquals(0.25, results.getScore());
        assertEquals(Map.of("idNumber", 0), results.getSignalScores());
        assertEquals("no id number", results.getReason());
    }

    @Test
    void everyStepHasItsAnalyticsStatus() {
        for (StepType type : StepType.values()) {
            VeritasResultHandler.analyticsStatus(type);
        }
        assertEquals(DocumentAnalyticsStatus.TEXT_CLEANUP_COMPLETE, VeritasResultHandler.analyticsStatus(StepType.TEXT_CLEANUP));
    }
}
