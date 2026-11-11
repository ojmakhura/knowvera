package bw.co.roguesystems.ai.job;

public enum JobStatus {
    RUNNING,
    /** Every step produced a result. */
    COMPLETED,
    /** A step failed with all its providers; the results of the steps before it are kept. */
    FAILED
}
