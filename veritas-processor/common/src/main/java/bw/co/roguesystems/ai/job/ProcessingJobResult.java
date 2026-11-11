package bw.co.roguesystems.ai.job;

import java.util.List;
import java.util.Map;

/**
 * The outcome of a job.
 *
 * @param jobId          the request's job id
 * @param correlationId  the request's correlation id
 * @param profileCode    the profile that ran
 * @param profileVersion the version of the profile that ran
 * @param status         COMPLETED or FAILED
 * @param text           the final text (after cleanup when the profile cleans it)
 * @param fields         the extracted fields (null when no field extraction ran)
 * @param classification the classification (null when no classification ran)
 * @param steps          what each step did, in order
 * @param error          why the job failed
 * @param attributes     the request's attributes, unchanged
 */
public record ProcessingJobResult(
        String jobId,
        String correlationId,
        String profileCode,
        Integer profileVersion,
        JobStatus status,
        String text,
        Map<String, Object> fields,
        Classification classification,
        List<StepResult> steps,
        String error,
        Map<String, String> attributes) {
}
