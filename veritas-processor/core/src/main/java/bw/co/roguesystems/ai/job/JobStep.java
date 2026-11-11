package bw.co.roguesystems.ai.job;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import bw.co.roguesystems.ai.profile.ProviderType;
import bw.co.roguesystems.ai.profile.StepType;

/**
 * What a step of a job did (a {@link StepResult} as stored).
 */
@Embeddable
public class JobStep {

    @Enumerated(EnumType.STRING)
    @Column(name = "step_type", nullable = false, length = 40)
    private StepType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private StepStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "provider", length = 40)
    private ProviderType provider;

    @Column(name = "model")
    private String model;

    @Column(name = "duration_ms", nullable = false)
    private long durationMs;

    /** One line per failed provider attempt. */
    @Column(name = "failures", columnDefinition = "TEXT")
    private String failures;

    protected JobStep() {
    }

    public JobStep(StepResult result) {
        this.type = result.type();
        this.status = result.status();
        this.provider = result.provider();
        this.model = result.model();
        this.durationMs = result.durationMs();
        this.failures = result.failures() == null || result.failures().isEmpty() ? null
                : result.failures().stream().map(JobStep::oneLine).collect(java.util.stream.Collectors.joining("\n"));
    }

    /** Failures are stored one per line, and exception messages can span several. */
    private static String oneLine(String failure) {
        return failure == null ? "" : failure.replaceAll("\\s*\\R\\s*", " ");
    }

    public StepResult toResult() {
        return new StepResult(type, status, provider, model, durationMs,
                failures == null ? java.util.List.of() : java.util.List.of(failures.split("\n")));
    }
}
