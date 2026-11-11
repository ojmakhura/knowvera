package bw.co.roguesystems.ai.job;

public enum StepStatus {
    COMPLETED,
    FAILED,
    /** Before the requested first step, or not in the profile. */
    SKIPPED
}
