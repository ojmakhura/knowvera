package bw.co.roguesystems.ai.pipeline;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import bw.co.roguesystems.ai.config.VeritasProperties;
import bw.co.roguesystems.ai.job.JobStatus;
import bw.co.roguesystems.ai.job.ProcessingJobRequest;
import bw.co.roguesystems.ai.job.ProcessingJobResult;
import bw.co.roguesystems.ai.job.ProcessingJobService;
import bw.co.roguesystems.ai.job.StepStatus;
import bw.co.roguesystems.ai.profile.ProcessingProfile;
import bw.co.roguesystems.ai.profile.ProcessingProfileService;
import bw.co.roguesystems.ai.profile.ProcessingStep;
import bw.co.roguesystems.ai.profile.ProfileNotFoundException;
import bw.co.roguesystems.ai.profile.ProviderType;
import bw.co.roguesystems.ai.profile.StepType;
import bw.co.roguesystems.ai.provider.DocumentFile;
import bw.co.roguesystems.ai.provider.ExtractedText;
import bw.co.roguesystems.ai.provider.LlmCall;
import bw.co.roguesystems.ai.provider.LlmProvider;
import bw.co.roguesystems.ai.provider.LlmResponse;
import bw.co.roguesystems.ai.provider.ProviderException;
import bw.co.roguesystems.ai.provider.ProviderRegistry;
import bw.co.roguesystems.ai.provider.TextExtractionProvider;
import bw.co.roguesystems.ai.schema.DocumentRef;
import bw.co.roguesystems.ai.schema.DocumentTypeSpec;
import bw.co.roguesystems.ai.schema.ExtractionSchema;
import bw.co.roguesystems.ai.schema.ExtractionSchemaService;
import bw.co.roguesystems.ai.schema.FieldSpec;
import bw.co.roguesystems.ai.schema.SchemaField;
import bw.co.roguesystems.ai.schema.SchemaNotFoundException;
import bw.co.roguesystems.ai.schema.FieldType;
import bw.co.roguesystems.ai.storage.DocumentStore;
import tools.jackson.databind.json.JsonMapper;

class PipelineRunnerTest {

    private final ProcessingProfileService profiles = mock(ProcessingProfileService.class);
    private final ExtractionSchemaService schemas = mock(ExtractionSchemaService.class);
    private final ProcessingJobService jobs = mock(ProcessingJobService.class);
    private final DocumentStore store = mock(DocumentStore.class);
    private final VeritasProperties properties = new VeritasProperties();
    private final List<String> calls = new ArrayList<>();
    private final JsonMapper jsonMapper = JsonMapper.builder().build();

    private String gemini = "{}";
    private boolean ollamaFails;
    /** Times Ollama is unreachable before it answers. */
    private int ollamaOutages;
    private final List<LlmCall> llmCalls = new ArrayList<>();

    private PipelineRunner runner;

    @BeforeEach
    void setUp() {
        properties.getJobs().setRetryDelay(Duration.ZERO);
        TextExtractionProvider pdfText = new FakeExtractor(ProviderType.PDF_TEXT, null);
        TextExtractionProvider tesseract = new FakeExtractor(ProviderType.TESSERACT, "OCR TEXT");
        LlmProvider ollama = new FakeLlm(ProviderType.OLLAMA);
        LlmProvider geminiLlm = new FakeLlm(ProviderType.GEMINI);
        ProviderRegistry registry = new ProviderRegistry(List.of(pdfText, tesseract), List.of(ollama, geminiLlm),
                properties);
        runner = new PipelineRunner(profiles, schemas, jobs, registry, store, new PromptRenderer(jsonMapper), jsonMapper, properties);
        when(store.read(any())).thenReturn(new DocumentFile("%PDF-1.7".getBytes(), "application/pdf", "id.pdf"));
    }

    private ProcessingProfile profile(ProcessingStep... steps) {
        ProcessingProfile profile = new ProcessingProfile();
        profile.setCode("default");
        profile.setVersion(3);
        profile.setName("Default");
        profile.getSteps().addAll(List.of(steps));
        when(profiles.latest("default")).thenReturn(profile);
        return profile;
    }

    private static ProcessingStep step(StepType type, ProviderType... providers) {
        ProcessingStep step = new ProcessingStep();
        step.setType(type);
        step.setProviders(new ArrayList<>(List.of(providers)));
        return step;
    }

    private static ProcessingJobRequest request(StepType fromStep, String text) {
        return new ProcessingJobRequest("job-1", "doc-1", "default",
                new DocumentRef("KYC_RECORD/1/id.pdf", "application/pdf", "id.pdf"), "National ID",
                List.of(new FieldSpec("idNumber", null, FieldType.STRING, null, false, true),
                        new FieldSpec("dateOfBirth", null, FieldType.DATE, null, false, false)), List.of(), fromStep, text, Map.of("k", "v"));
    }

    @Test
    void runsEveryStepFallingBackToTheNextProvider() {
        profile(step(StepType.TEXT_EXTRACTION, ProviderType.PDF_TEXT, ProviderType.TESSERACT),
                step(StepType.TEXT_CLEANUP, ProviderType.OLLAMA),
                step(StepType.FIELD_EXTRACTION, ProviderType.OLLAMA),
                step(StepType.CLASSIFICATION, ProviderType.GEMINI));
        gemini = "{\"expectedType\":\"National ID\",\"detectedType\":\"National ID\",\"match\":true,"
                + "\"signalScores\":{\"idNumber\":1}}";

        ProcessingJobResult result = runner.run(request(null, null));

        assertThat(result.status()).isEqualTo(JobStatus.COMPLETED);
        assertThat(result.profileVersion()).isEqualTo(3);
        assertThat(result.text()).isEqualTo("cleaned: OCR TEXT");
        assertThat(result.fields()).containsEntry("idNumber", "123").containsEntry("dateOfBirth", null);
        assertThat(result.classification().score()).isEqualTo(1.0);
        assertThat(result.attributes()).containsEntry("k", "v");
        assertThat(result.steps().getFirst().provider()).isEqualTo(ProviderType.TESSERACT);
        assertThat(result.steps().getFirst().failures()).singleElement().asString().startsWith("PDF_TEXT:");
        verify(jobs).finish(result);
    }

    private void schema(String code, String name, String... fieldNames) {
        ExtractionSchema schema = new ExtractionSchema();
        schema.setCode(code);
        schema.setVersion(1);
        schema.setName(name);
        for (String fieldName : fieldNames) {
            schema.getFields().add(new SchemaField(FieldSpec.text(fieldName)));
        }
        when(schemas.latest(code)).thenReturn(schema);
    }

    @Test
    void aJobThatNamesASchemaExtractsTheSchemasFields() {
        profile(step(StepType.FIELD_EXTRACTION, ProviderType.OLLAMA), step(StepType.CLASSIFICATION, ProviderType.GEMINI));
        schema("NID", "National ID", "idNumber", "surname");
        schema("PSPT", "Any label", "passportNumber");
        when(schemas.latest("MISSING")).thenThrow(new SchemaNotFoundException("MISSING"));
        gemini = "{\"detectedType\":\"National ID\",\"signalScores\":{\"idNumber\":1,\"surname\":1}}";

        ProcessingJobResult result = runner.run(new ProcessingJobRequest("job-1", "doc-1", "default", null,
                "National ID", null,
                List.of(new DocumentTypeSpec("National ID", null, "NID"), new DocumentTypeSpec("Passport", null, "PSPT"),
                        new DocumentTypeSpec("Permit", null, "MISSING")),
                StepType.FIELD_EXTRACTION, "ID 123", Map.of(), "NID"));

        assertThat(result.status()).isEqualTo(JobStatus.COMPLETED);
        assertThat(result.fields()).containsOnlyKeys("idNumber", "surname");
        assertThat(result.classification().match()).isTrue();
        assertThat(llmCalls.getLast().systemPrompt() + llmCalls.getLast().userPrompt()).contains("National ID")
                .contains("Passport").contains("Permit").doesNotContain("Any label")
                .contains("passportNumber");
    }

    @Test
    void aJobWhoseSchemaIsMissingFails() {
        profile(step(StepType.FIELD_EXTRACTION, ProviderType.OLLAMA));
        when(schemas.latest("NOPE")).thenThrow(new SchemaNotFoundException("NOPE"));

        ProcessingJobResult result = runner.run(new ProcessingJobRequest("job-1", "doc-1", "default", null, null, null,
                null, StepType.FIELD_EXTRACTION, "ID 123", Map.of(), "NOPE"));

        assertThat(result.status()).isEqualTo(JobStatus.FAILED);
        assertThat(result.error()).contains("No schema with code 'NOPE'");
    }

    @Test
    void localOnlyDeploymentsSkipCloudProviders() {
        properties.setLocalOnly(true);
        profile(step(StepType.TEXT_CLEANUP, ProviderType.GEMINI, ProviderType.OLLAMA));

        ProcessingJobResult result = runner.run(request(StepType.TEXT_CLEANUP, "raw"));

        assertThat(result.status()).isEqualTo(JobStatus.COMPLETED);
        assertThat(result.steps().getFirst().provider()).isEqualTo(ProviderType.OLLAMA);
        assertThat(result.steps().getFirst().failures()).containsExactly("GEMINI: not allowed (veritas.local-only)");
        assertThat(calls).noneMatch(call -> call.startsWith("GEMINI"));
    }

    @Test
    void startsAtTheRequestedStepWithTheGivenText() {
        profile(step(StepType.TEXT_EXTRACTION, ProviderType.TESSERACT),
                step(StepType.TEXT_CLEANUP, ProviderType.OLLAMA),
                step(StepType.FIELD_EXTRACTION, ProviderType.OLLAMA));

        ProcessingJobResult result = runner.run(request(StepType.FIELD_EXTRACTION, "stored text"));

        assertThat(result.status()).isEqualTo(JobStatus.COMPLETED);
        assertThat(result.steps()).extracting(s -> s.status())
                .containsExactly(StepStatus.SKIPPED, StepStatus.SKIPPED, StepStatus.COMPLETED);
        assertThat(result.text()).isEqualTo("stored text");
        verify(store, never()).read(any());
    }

    @Test
    void aLaterStepWithoutTextRunsTheWholeProfileOnTheDocument() {
        profile(step(StepType.TEXT_EXTRACTION, ProviderType.TESSERACT),
                step(StepType.FIELD_EXTRACTION, ProviderType.OLLAMA));

        ProcessingJobResult result = runner.run(request(StepType.FIELD_EXTRACTION, " "));

        assertThat(result.status()).isEqualTo(JobStatus.COMPLETED);
        assertThat(result.steps()).extracting(s -> s.status())
                .containsExactly(StepStatus.COMPLETED, StepStatus.COMPLETED);
        assertThat(result.text()).isEqualTo("OCR TEXT");
    }

    @Test
    void aStepNoProviderCompletesFailsTheJobAndKeepsEarlierResults() {
        ollamaFails = true;
        profile(step(StepType.TEXT_EXTRACTION, ProviderType.TESSERACT),
                step(StepType.TEXT_CLEANUP, ProviderType.OLLAMA),
                step(StepType.FIELD_EXTRACTION, ProviderType.OLLAMA));

        ProcessingJobResult result = runner.run(request(null, null));

        assertThat(result.status()).isEqualTo(JobStatus.FAILED);
        assertThat(result.text()).isEqualTo("OCR TEXT");
        assertThat(result.steps()).hasSize(2);
        assertThat(result.error()).startsWith("TEXT_CLEANUP failed: OLLAMA: server down");
    }

    @Test
    void anUnknownProfileFailsTheJob() {
        when(profiles.latest("default")).thenThrow(new ProfileNotFoundException("default"));

        ProcessingJobResult result = runner.run(request(null, null));

        assertThat(result.status()).isEqualTo(JobStatus.FAILED);
        assertThat(result.error()).contains("default");
    }

    @Test
    void anAnswerWithoutJsonFallsBackToTheNextModel() {
        gemini = "Sorry, I can't";
        profile(step(StepType.FIELD_EXTRACTION, ProviderType.GEMINI, ProviderType.OLLAMA));

        ProcessingJobResult result = runner.run(request(StepType.FIELD_EXTRACTION, "text"));

        assertThat(result.steps().getFirst().provider()).isEqualTo(ProviderType.OLLAMA);
        assertThat(result.fields()).containsEntry("idNumber", "123");
    }

    @Test
    void aStepWhoseProviderWasUnreachableIsTriedAgain() {
        ollamaOutages = 2;
        profile(step(StepType.FIELD_EXTRACTION, ProviderType.OLLAMA));

        ProcessingJobResult result = runner.run(request(StepType.FIELD_EXTRACTION, "text"));

        assertThat(result.status()).isEqualTo(JobStatus.COMPLETED);
        assertThat(result.steps().getFirst().failures()).containsExactly("OLLAMA: connection refused",
                "try 2 of 3", "OLLAMA: connection refused", "try 3 of 3");
    }

    @Test
    void givesUpWhenTheProviderStaysUnreachable() {
        ollamaOutages = 5;
        profile(step(StepType.FIELD_EXTRACTION, ProviderType.OLLAMA));

        ProcessingJobResult result = runner.run(request(StepType.FIELD_EXTRACTION, "text"));

        assertThat(result.status()).isEqualTo(JobStatus.FAILED);
        assertThat(llmCalls).hasSize(3);
    }

    @Test
    void aFailureThatWouldRepeatIsNotTriedAgain() {
        ollamaFails = true;
        profile(step(StepType.FIELD_EXTRACTION, ProviderType.OLLAMA));

        runner.run(request(StepType.FIELD_EXTRACTION, "text"));

        assertThat(llmCalls).hasSize(1);
    }

    @Test
    @SuppressWarnings("unchecked")
    void fieldExtractionHoldsTheAnswerToTheRequestedFields() {
        ProcessingStep cleanup = step(StepType.TEXT_CLEANUP, ProviderType.OLLAMA);
        ProcessingStep fields = step(StepType.FIELD_EXTRACTION, ProviderType.OLLAMA);
        profile(cleanup, fields);

        runner.run(request(StepType.TEXT_CLEANUP, "text"));

        assertThat(llmCalls.get(0).responseSchema()).isNull();
        Map<String, Object> schema = llmCalls.get(1).responseSchema();
        assertThat((Map<String, Object>) schema.get("properties")).containsOnlyKeys("idNumber", "dateOfBirth");
        assertThat((List<String>) schema.get("required")).containsExactly("idNumber", "dateOfBirth");

        llmCalls.clear();
        fields.setOptions(Map.of("structuredOutput", "false"));
        runner.run(request(StepType.FIELD_EXTRACTION, "text"));
        assertThat(llmCalls.getFirst().responseSchema()).isNull();
    }

    private record FakeExtractor(ProviderType type, String text) implements TextExtractionProvider {
        @Override
        public ExtractedText extract(DocumentFile file, ProcessingStep step) {
            if (text == null) {
                throw new ProviderException("the PDF has no text layer");
            }
            return ExtractedText.of(text);
        }
    }

    private final class FakeLlm implements LlmProvider {
        private final ProviderType type;

        FakeLlm(ProviderType type) {
            this.type = type;
        }

        @Override
        public ProviderType type() {
            return type;
        }

        @Override
        public LlmResponse chat(LlmCall call) {
            calls.add(type + ": " + call.userPrompt());
            llmCalls.add(call);
            if (type == ProviderType.GEMINI) {
                return new LlmResponse(gemini, "gemini-2.5-flash");
            }
            if (ollamaFails) {
                throw new ProviderException("server down");
            }
            if (ollamaOutages-- > 0) {
                throw new ProviderException("connection refused", null, true);
            }
            if (call.systemPrompt().startsWith("You are a text normalization")) {
                String text = call.userPrompt().substring(call.userPrompt().indexOf("<document>\n") + 11,
                        call.userPrompt().indexOf("\n</document>"));
                return new LlmResponse("<think>...</think>cleaned: " + text, "gemma4");
            }
            return new LlmResponse("{\"idNumber\": \"123\"}", "gemma4");
        }
    }
}
