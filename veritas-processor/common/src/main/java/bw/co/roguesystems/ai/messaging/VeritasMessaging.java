package bw.co.roguesystems.ai.messaging;

/**
 * RabbitMQ names shared by the processor and its callers. Callers publish
 * {@link bw.co.roguesystems.ai.job.ProcessingJobRequest}s with {@link #JOBS_ROUTING_KEY} and
 * receive {@link bw.co.roguesystems.ai.job.ProcessingJobResult}s on {@link #RESULTS_QUEUE}.
 */
public final class VeritasMessaging {

    /** Direct exchange for jobs and results. */
    public static final String EXCHANGE = "x.veritas";

    public static final String JOBS_QUEUE = "q.veritas.jobs";
    public static final String JOBS_ROUTING_KEY = "veritas.jobs";

    public static final String RESULTS_QUEUE = "q.veritas.results";
    public static final String RESULTS_ROUTING_KEY = "veritas.results";

    /** Jobs that could not be read or processed at all. */
    public static final String DEAD_LETTER_EXCHANGE = "x.veritas.dead-letter";
    public static final String JOBS_DEAD_LETTER_QUEUE = "q.veritas.jobs.dead-letter";
    public static final String RESULTS_DEAD_LETTER_QUEUE = "q.veritas.results.dead-letter";

    private VeritasMessaging() {
    }
}
