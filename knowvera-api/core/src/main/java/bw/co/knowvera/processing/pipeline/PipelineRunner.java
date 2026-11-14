package bw.co.knowvera.processing.pipeline;

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

import bw.co.knowvera.processing.ProcessingProperties;
import bw.co.knowvera.processing.job.JobStatus;
import bw.co.knowvera.processing.job.ProcessingJobRequest;
import bw.co.knowvera.processing.job.ProcessingJobResult;
import bw.co.knowvera.processing.job.ProcessingJobService;
import bw.co.knowvera.processing.job.StepResult;
import bw.co.knowvera.processing.job.StepStatus;
import bw.co.knowvera.processing.profile.DefaultPrompts;
import bw.co.knowvera.processing.profile.ProcessingProfile;
import bw.co.knowvera.processing.profile.ProcessingProfileService;
import bw.co.knowvera.processing.schema.DocumentTypeSpec;
import bw.co.knowvera.processing.schema.FieldSpec;
import bw.co.knowvera.processing.schema.FieldType;
import bw.co.knowvera.processing.llm.PromptMessage;
import bw.co.knowvera.document.type.DocumentType;
import bw.co.knowvera.document.type.DocumentTypeRepository;
import bw.co.knowvera.document.type.field.ExpectedField;
import bw.co.knowvera.processing.profile.ProcessingStep;
import bw.co.knowvera.processing.profile.ProviderType;
import bw.co.knowvera.processing.profile.StepType;
import bw.co.knowvera.processing.provider.ExtractedText;
import bw.co.knowvera.processing.provider.LlmCall;
import bw.co.knowvera.processing.provider.LlmProvider;
import bw.co.knowvera.processing.provider.LlmResponse;
import bw.co.knowvera.processing.provider.ProviderException;
import bw.co.knowvera.processing.provider.ProviderRegistry;
import bw.co.knowvera.processing.provider.TextExtractionProvider;
import bw.co.knowvera.processing.storage.DocumentStore;
import io.micrometer.core.instrument.Metrics;
import tools.jackson.databind.json.JsonMapper;

/**
 * Runs a job: the latest version of its profile, from the requested step on. Each step tries its
 * providers in order and keeps the first result. When none can and one of them was unreachable,
 * timed out or failed itself, the step is tried again after a wait (app.processing.jobs.attempts and
 * retry-delay); a step that still cannot be completed fails the job, keeping what the steps before
 * it produced.
 * <p>
 * Metrics: processing.step (timer; step, provider, status) and processing.jobs (counter; status). The
 * job id is in the logging context (jobId) while the job runs.
 */
@Service
public class PipelineRunner {

    private static final Logger log = LoggerFactory.getLogger(PipelineRunner.class);

    private final ProcessingProfileService profiles;
    private final DocumentTypeRepository documentTypes;
    private final ProcessingJobService jobs;
    private final ProviderRegistry providers;
    private final DocumentStore documentStore;
    private final PromptRenderer prompts;
    private final JsonMapper jsonMapper;
    private final ProcessingProperties.Jobs settings;

    public PipelineRunner(ProcessingProfileService profiles, DocumentTypeRepository documentTypes, ProcessingJobService jobs,
            ProviderRegistry providers, DocumentStore documentStore, PromptRenderer prompts, JsonMapper jsonMapper,
            ProcessingProperties properties) {
        this.settings = properties.getJobs();
        this.profiles = profiles;
        this.documentTypes = documentTypes;
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
            Metrics.counter("processing.jobs", "status", result.status().name()).increment();
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
            DocumentType type = documentType(submitted.documentTypeId());
            request = withTypes(submitted, type);
            steps = steps(profile, type);
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

    /** The document type a job names by its id (null: it names none). */
    private DocumentType documentType(String id) {
        if (!hasText(id)) {
            return null;
        }
        return find(id).orElseThrow(() -> new IllegalArgumentException("No document type with id '" + id + "'"));
    }

    private Optional<DocumentType> find(String id) {
        try {
            return documentTypes.findById(UUID.fromString(id.trim()));
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    /**
     * The job with what its document types hold: the expected fields of its own type as the fields to
     * extract (fields the job gives itself win), and those of every candidate type that names a
     * document type. A candidate whose document type does not exist is still a candidate, without
     * fields.
     */
    private ProcessingJobRequest withTypes(ProcessingJobRequest request, DocumentType type) {
        List<FieldSpec> fields = request.fields();
        if (type != null && (fields == null || fields.isEmpty())) {
            fields = fields(type);
        }
        List<DocumentTypeSpec> candidateTypes = request.candidateTypes();
        if (candidateTypes != null && candidateTypes.stream().anyMatch(candidate -> hasText(candidate.documentTypeId()))) {
            candidateTypes = candidateTypes.stream().map(candidate -> withType(candidate, request.jobId())).toList();
        }
        return fields == request.fields() && candidateTypes == request.candidateTypes() ? request
                : request.withTypeFields(fields, candidateTypes);
    }

    private DocumentTypeSpec withType(DocumentTypeSpec type, String jobId) {
        if (!hasText(type.documentTypeId()) || (type.fields() != null && !type.fields().isEmpty())) {
            return type;
        }
        return find(type.documentTypeId())
                .map(found -> new DocumentTypeSpec(type.name(), fields(found), type.documentTypeId()))
                .orElseGet(() -> {
                    // One unknown type must not stop every job: classification still has the name
                    log.warn("Job {} names the document type '{}' for the candidate type {}, which does not exist",
                            jobId, type.documentTypeId(), type.name());
                    return type;
                });
    }

    /** A document type's expected fields, as the steps use them. */
    static List<FieldSpec> fields(DocumentType type) {
        List<FieldSpec> fields = new ArrayList<>();
        for (ExpectedField field : type.getExpectedFields()) {
            fields.add(new FieldSpec(field.getField(), field.getFieldLabel(),
                    field.getFieldType() == null ? FieldType.STRING : FieldType.valueOf(field.getFieldType().name()),
                    field.getFormat(), field.getMany(), field.getMandatory()));
        }
        return fields;
    }

    /**
     * The profile's steps for this job. A document type's own prompts replace the profile's: its
     * validation prompts (a system and a user message) those of the classification step, its text
     * extraction prompts those of the text extraction step. The providers, model and options stay
     * the profile's.
     */
    static List<ProcessingStep> steps(ProcessingProfile profile, DocumentType type) {
        if (type == null) {
            return profile.getSteps();
        }
        String validationSystem = prompt(type.getValidationPrompts(), "system");
        String validationUser = prompt(type.getValidationPrompts(), "user");
        String extractionSystem = prompt(type.getTextExtractionPrompts(), "system");
        String extractionUser = prompt(type.getTextExtractionPrompts(), "user");
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
        Metrics.timer("processing.step", "step", step.getType().name(),
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
                    failures.add(type + ": not allowed (app.processing.local-only)");
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
