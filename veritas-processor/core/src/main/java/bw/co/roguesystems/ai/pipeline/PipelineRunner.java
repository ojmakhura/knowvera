package bw.co.roguesystems.ai.pipeline;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;

import bw.co.roguesystems.ai.config.VeritasProperties;
import bw.co.roguesystems.ai.job.JobStatus;
import bw.co.roguesystems.ai.job.ProcessingJobRequest;
import bw.co.roguesystems.ai.job.ProcessingJobResult;
import bw.co.roguesystems.ai.job.ProcessingJobService;
import bw.co.roguesystems.ai.job.StepResult;
import bw.co.roguesystems.ai.job.StepStatus;
import bw.co.roguesystems.ai.profile.DefaultPrompts;
import bw.co.roguesystems.ai.profile.ProcessingProfile;
import bw.co.roguesystems.ai.profile.ProcessingProfileService;
import bw.co.roguesystems.ai.schema.DocumentTypeSpec;
import bw.co.roguesystems.ai.schema.FieldSpec;
import bw.co.roguesystems.ai.schema.FieldType;
import bw.co.roguesystems.ai.llm.PromptMessage;
import bw.co.roguesystems.ai.processor.DataProcessor;
import bw.co.roguesystems.ai.processor.DataProcessorRepository;
import bw.co.roguesystems.ai.processor.field.ExpectedField;
import bw.co.roguesystems.ai.profile.ProcessingStep;
import bw.co.roguesystems.ai.profile.ProviderType;
import bw.co.roguesystems.ai.profile.StepType;
import bw.co.roguesystems.ai.provider.ExtractedText;
import bw.co.roguesystems.ai.provider.LlmCall;
import bw.co.roguesystems.ai.provider.LlmProvider;
import bw.co.roguesystems.ai.provider.LlmResponse;
import bw.co.roguesystems.ai.provider.ProviderException;
import bw.co.roguesystems.ai.provider.ProviderRegistry;
import bw.co.roguesystems.ai.provider.TextExtractionProvider;
import bw.co.roguesystems.ai.storage.DocumentStore;
import io.micrometer.core.instrument.Metrics;
import tools.jackson.databind.json.JsonMapper;

/**
 * Runs a job: the latest version of its profile, from the requested step on. Each step tries its
 * providers in order and keeps the first result. When none can and one of them was unreachable,
 * timed out or failed itself, the step is tried again after a wait (veritas.jobs.attempts and
 * retry-delay); a step that still cannot be completed fails the job, keeping what the steps before
 * it produced.
 * <p>
 * Metrics: veritas.step (timer; step, provider, status) and veritas.jobs (counter; status). The
 * job id is in the logging context (jobId) while the job runs.
 */
@Service
public class PipelineRunner {

    private static final Logger log = LoggerFactory.getLogger(PipelineRunner.class);

    private final ProcessingProfileService profiles;
    private final DataProcessorRepository processors;
    private final ProcessingJobService jobs;
    private final ProviderRegistry providers;
    private final DocumentStore documentStore;
    private final PromptRenderer prompts;
    private final JsonMapper jsonMapper;
    private final VeritasProperties.Jobs settings;

    public PipelineRunner(ProcessingProfileService profiles, DataProcessorRepository processors, ProcessingJobService jobs,
            ProviderRegistry providers, DocumentStore documentStore, PromptRenderer prompts, JsonMapper jsonMapper,
            VeritasProperties properties) {
        this.settings = properties.getJobs();
        this.profiles = profiles;
        this.processors = processors;
        this.jobs = jobs;
        this.providers = providers;
        this.documentStore = documentStore;
        this.prompts = prompts;
        this.jsonMapper = jsonMapper;
    }

    public ProcessingJobResult run(ProcessingJobRequest request) {
        return run(request, null);
    }

    /**
     * @param submittedBy who submitted the job through the API (null for jobs from the queue): only
     *                    they can read it or run its id again
     */
    public ProcessingJobResult run(ProcessingJobRequest request, String submittedBy) {
        validate(request);
        try (MDC.MDCCloseable jobId = MDC.putCloseable("jobId", request.jobId())) {
            ProcessingJobResult result = process(request, submittedBy);
            Metrics.counter("veritas.jobs", "status", result.status().name()).increment();
            return result;
        }
    }

    private ProcessingJobResult process(ProcessingJobRequest submitted, String submittedBy) {
        jobs.start(submitted, submittedBy);

        ProcessingProfile profile;
        ProcessingJobRequest request;
        List<ProcessingStep> steps;
        try {
            profile = profiles.latest(submitted.profileCode());
            DataProcessor processor = processor(submitted.processorId());
            request = withProcessors(submitted, processor);
            steps = steps(profile, processor);
        } catch (RuntimeException e) {
            return finish(submitted, null, JobStatus.FAILED, new StepContext(submitted, () -> null), List.of(),
                    e.getMessage());
        }

        StepContext context = new StepContext(request, () -> documentStore.read(request.document()));
        List<StepResult> results = new ArrayList<>();
        StepType fromStep = firstStep(request);
        boolean started = fromStep == null;
        for (ProcessingStep step : steps) {
            if (!started && step.getType().ordinal() >= fromStep.ordinal()) {
                started = true;
            }
            if (!started) {
                results.add(new StepResult(step.getType(), StepStatus.SKIPPED, null, null, 0, List.of()));
                continue;
            }
            StepResult result = runStep(step, context);
            results.add(result);
            if (result.status() == StepStatus.FAILED) {
                return finish(request, profile, JobStatus.FAILED, context, results,
                        step.getType() + " failed: " + String.join("; ", result.failures()));
            }
        }
        return finish(request, profile, JobStatus.COMPLETED, context, results, null);
    }

    /** The data processor a job names by its id (null: it names none). */
    private DataProcessor processor(String id) {
        if (!hasText(id)) {
            return null;
        }
        return find(id).orElseThrow(() -> new IllegalArgumentException("No data processor with id '" + id + "'"));
    }

    private Optional<DataProcessor> find(String id) {
        try {
            return processors.findById(UUID.fromString(id.trim()));
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    /**
     * The job with what its data processors hold: the expected fields of its own processor as the
     * fields to extract (fields the job gives itself win), and those of every candidate type that
     * names a processor. A candidate whose processor does not exist is still a candidate, without
     * fields.
     */
    private ProcessingJobRequest withProcessors(ProcessingJobRequest request, DataProcessor processor) {
        List<FieldSpec> fields = request.fields();
        if (processor != null && (fields == null || fields.isEmpty())) {
            fields = fields(processor);
        }
        List<DocumentTypeSpec> candidateTypes = request.candidateTypes();
        if (candidateTypes != null && candidateTypes.stream().anyMatch(type -> hasText(type.processorId()))) {
            candidateTypes = candidateTypes.stream().map(type -> withProcessor(type, request.jobId())).toList();
        }
        return fields == request.fields() && candidateTypes == request.candidateTypes() ? request
                : request.withProcessorFields(fields, candidateTypes);
    }

    private DocumentTypeSpec withProcessor(DocumentTypeSpec type, String jobId) {
        if (!hasText(type.processorId()) || (type.fields() != null && !type.fields().isEmpty())) {
            return type;
        }
        return find(type.processorId())
                .map(processor -> new DocumentTypeSpec(type.name(), fields(processor), type.processorId()))
                .orElseGet(() -> {
                    // One unknown processor must not stop every job: classification still has the name
                    log.warn("Job {} names the data processor '{}' for the candidate type {}, which does not exist",
                            jobId, type.processorId(), type.name());
                    return type;
                });
    }

    /** A data processor's expected fields, as the steps use them. */
    static List<FieldSpec> fields(DataProcessor processor) {
        List<FieldSpec> fields = new ArrayList<>();
        for (ExpectedField field : processor.getExpectedFields()) {
            fields.add(new FieldSpec(field.getField(), field.getFieldLabel(),
                    field.getFieldType() == null ? FieldType.STRING : FieldType.valueOf(field.getFieldType().name()),
                    field.getFormat(), field.getMany(), field.getMandatory()));
        }
        return fields;
    }

    /**
     * The profile's steps for this job. A data processor's own prompts replace the profile's: its
     * validation prompts (a system and a user message) those of the classification step, its text
     * extraction prompts those of the text extraction step. The providers, model and options stay
     * the profile's.
     */
    static List<ProcessingStep> steps(ProcessingProfile profile, DataProcessor processor) {
        if (processor == null) {
            return profile.getSteps();
        }
        String validationSystem = prompt(processor.getValidationPrompts(), "system");
        String validationUser = prompt(processor.getValidationPrompts(), "user");
        String extractionSystem = prompt(processor.getTextExtractionPrompts(), "system");
        String extractionUser = prompt(processor.getTextExtractionPrompts(), "user");
        List<ProcessingStep> steps = new ArrayList<>();
        for (ProcessingStep step : profile.getSteps()) {
            if (step.getType() == StepType.CLASSIFICATION && validationSystem != null && validationUser != null) {
                steps.add(withPrompts(step, validationSystem, validationUser));
            } else if (step.getType() == StepType.TEXT_EXTRACTION && (extractionSystem != null || extractionUser != null)) {
                steps.add(withPrompts(step, extractionSystem == null ? step.getSystemPrompt() : extractionSystem,
                        extractionUser == null ? step.getUserPrompt() : extractionUser));
            } else {
                steps.add(step);
            }
        }
        return steps;
    }

    private static String prompt(List<PromptMessage> messages, String role) {
        if (messages == null) {
            return null;
        }
        return messages.stream()
                .filter(message -> message != null && role.equalsIgnoreCase(message.getRole())
                        && hasText(message.getContent()))
                .map(PromptMessage::getContent).findFirst().orElse(null);
    }

    /** A copy of the step for this job only: the profile's step is not changed. */
    private static ProcessingStep withPrompts(ProcessingStep step, String systemPrompt, String userPrompt) {
        ProcessingStep copy = new ProcessingStep();
        copy.setType(step.getType());
        copy.setProviders(step.getProviders());
        copy.setModel(step.getModel());
        copy.setOptions(step.getOptions());
        copy.setSystemPrompt(systemPrompt);
        copy.setUserPrompt(userPrompt);
        return copy;
    }

    private static boolean hasText(String value) {
        return value != null && !value.isBlank();
    }

    /**
     * The step the job starts at (null: the profile's first). A job asked to start after text
     * extraction without the text to start from runs the whole profile on its document instead:
     * skipping the steps that produce the text would leave the later ones nothing to work on.
     */
    private static StepType firstStep(ProcessingJobRequest request) {
        StepType fromStep = request.fromStep();
        if (fromStep == null || fromStep == StepType.TEXT_EXTRACTION) {
            return fromStep;
        }
        if (request.text() == null || request.text().isBlank()) {
            log.info("Job {} asked to start at {} without text: running the whole profile on its document",
                    request.jobId(), fromStep);
            return null;
        }
        return fromStep;
    }

    private StepResult runStep(ProcessingStep step, StepContext context) {
        long start = System.nanoTime();
        StepResult result = attemptStep(step, context, start);
        Metrics.timer("veritas.step", "step", step.getType().name(),
                "provider", result.provider() == null ? "none" : result.provider().name(),
                "status", result.status().name()).record(Duration.ofMillis(result.durationMs()));
        return result;
    }

    private StepResult attemptStep(ProcessingStep step, StepContext context, long start) {
        List<String> failures = new ArrayList<>();
        if (step.getType() != StepType.TEXT_EXTRACTION && (context.text() == null || context.text().isBlank())) {
            failures.add("no text to work on");
            return new StepResult(step.getType(), StepStatus.FAILED, null, null, elapsed(start), failures);
        }
        int attempts = Math.max(settings.getAttempts(), 1);
        for (int attempt = 1; attempt <= attempts; attempt++) {
            boolean retryable = false;
            for (ProviderType type : step.getProviders()) {
                if (!providers.allowed(type)) {
                    failures.add(type + ": not allowed (veritas.local-only)");
                    continue;
                }
                try {
                    String model = attempt(step, type, context);
                    return new StepResult(step.getType(), StepStatus.COMPLETED, type, model, elapsed(start), failures);
                } catch (ProviderException | IllegalStateException | IllegalArgumentException e) {
                    log.warn("{} with {} failed for job {}: {}", step.getType(), type, context.request().jobId(),
                            e.getMessage());
                    failures.add(type + ": " + e.getMessage());
                    retryable |= e instanceof ProviderException failure && failure.isRetryable();
                }
            }
            if (!retryable || attempt == attempts || !pause(attempt)) {
                break;
            }
            failures.add("try " + (attempt + 1) + " of " + attempts);
        }
        return new StepResult(step.getType(), StepStatus.FAILED, null, null, elapsed(start), failures);
    }

    /** Waits before the next try (the delay doubles each time); false when interrupted. */
    private boolean pause(int attempt) {
        long millis = settings.getRetryDelay().toMillis() << Math.min(attempt - 1, 10);
        try {
            Thread.sleep(millis);
            return true;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }
    }

    /** Runs the step with one provider and stores its output; returns the model it used. */
    private String attempt(ProcessingStep step, ProviderType type, StepContext context) {
        if (step.getType() == StepType.TEXT_EXTRACTION) {
            TextExtractionProvider extractor = providers.extractor(type)
                    .orElseThrow(() -> new ProviderException("cannot extract text"));
            ExtractedText extracted = extractor.extract(context.file(), step);
            context.text(extracted.text());
            return extracted.model();
        }

        LlmProvider llm = providers.llm(type).orElseThrow(() -> new ProviderException("is not a language model"));
        String userTemplate = step.getUserPrompt() == null ? DefaultPrompts.user(step.getType()) : step.getUserPrompt();
        String systemTemplate = step.getSystemPrompt() == null ? DefaultPrompts.system(step.getType())
                : step.getSystemPrompt();
        LlmResponse response = llm.chat(new LlmCall(
                prompts.renderSystem(systemTemplate, userTemplate, context),
                prompts.renderUser(userTemplate, context),
                step.getModel(),
                step.getOptions(),
                responseSchema(step, context.request())));

        switch (step.getType()) {
            case TEXT_CLEANUP -> {
                String cleaned = ModelOutput.text(response.content());
                if (cleaned.isBlank()) {
                    throw new ProviderException("the cleaned text is empty");
                }
                context.text(cleaned);
            }
            case FIELD_EXTRACTION -> context.fields(
                    FieldValues.normalise(ModelOutput.json(response.content(), jsonMapper), context.request().fields()));
            case CLASSIFICATION -> context.classification(
                    ModelOutput.classification(ModelOutput.json(response.content(), jsonMapper)));
            default -> throw new IllegalStateException("Unhandled step " + step.getType());
        }
        return response.model();
    }

    /**
     * The schema the answer is held to (the step's structuredOutput option turns it on or off).
     * Field extraction always answers with the requested fields; a classification step with its
     * own prompts may ask for another answer than the default one, so it is left free.
     */
    private static Map<String, Object> responseSchema(ProcessingStep step, ProcessingJobRequest request) {
        String option = step.option("structuredOutput");
        boolean asked = option != null && !option.isBlank();
        if (asked && !Boolean.parseBoolean(option.trim())) {
            return null;
        }
        return switch (step.getType()) {
            case FIELD_EXTRACTION -> ResponseSchemas.fields(request.fields());
            case CLASSIFICATION -> asked || (step.getSystemPrompt() == null && step.getUserPrompt() == null)
                    ? ResponseSchemas.classification(request) : null;
            default -> null;
        };
    }

    private ProcessingJobResult finish(ProcessingJobRequest request, ProcessingProfile profile, JobStatus status,
            StepContext context, List<StepResult> steps, String error) {
        Map<String, Object> fields = context.fields();
        ProcessingJobResult result = new ProcessingJobResult(request.jobId(), request.correlationId(),
                request.profileCode(), profile == null ? null : profile.getVersion(), status, context.text(),
                fields, context.classification(), List.copyOf(steps), error, request.attributes());
        jobs.finish(result);
        log.info("Job {} ({} v{}) {}{}", request.jobId(), request.profileCode(),
                profile == null ? "?" : profile.getVersion(), status, error == null ? "" : ": " + error);
        return result;
    }

    private static void validate(ProcessingJobRequest request) {
        if (request == null || request.jobId() == null || request.jobId().isBlank()) {
            throw new IllegalArgumentException("A job needs a jobId");
        }
        if (request.jobId().length() > 64) {
            throw new IllegalArgumentException("A jobId has at most 64 characters");
        }
        if (request.profileCode() == null || request.profileCode().isBlank()) {
            throw new IllegalArgumentException("A job needs a profileCode");
        }
        boolean needsFile = request.fromStep() == null || request.fromStep() == StepType.TEXT_EXTRACTION;
        if (needsFile && request.document() == null) {
            throw new IllegalArgumentException("A job that extracts text needs a document");
        }
        if (!needsFile && request.document() == null && (request.text() == null || request.text().isBlank())) {
            throw new IllegalArgumentException("A job starting at " + request.fromStep() + " needs text or a document");
        }
    }

    private static long elapsed(long start) {
        return (System.nanoTime() - start) / 1_000_000;
    }
}
