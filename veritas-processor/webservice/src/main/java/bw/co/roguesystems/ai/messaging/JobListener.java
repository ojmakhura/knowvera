package bw.co.roguesystems.ai.messaging;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import bw.co.roguesystems.ai.job.JobStatus;
import bw.co.roguesystems.ai.job.ProcessingJobRequest;
import bw.co.roguesystems.ai.job.ProcessingJobResult;
import bw.co.roguesystems.ai.pipeline.PipelineRunner;

/**
 * Runs the queued jobs and publishes their results. A job the processor cannot accept (e.g. one
 * without a document) is answered with a FAILED result; one without a job id, or one that hits an
 * infrastructure failure, goes to the dead-letter queue.
 */
@Component
public class JobListener {

    private static final Logger log = LoggerFactory.getLogger(JobListener.class);

    private final PipelineRunner runner;
    private final ResultPublisher publisher;

    public JobListener(PipelineRunner runner, ResultPublisher publisher) {
        this.runner = runner;
        this.publisher = publisher;
    }

    @RabbitListener(queues = VeritasMessaging.JOBS_QUEUE, concurrency = "${veritas.jobs.concurrency:1-4}")
    public void handle(@Payload ProcessingJobRequest request,
            @Header(name = ResultPublisher.SUBMITTED_BY_HEADER, required = false) String submittedBy) {
        if (request == null || request.jobId() == null || request.jobId().isBlank()) {
            throw new AmqpRejectAndDontRequeueException("A job without a jobId cannot be answered");
        }
        ProcessingJobResult result;
        try {
            result = runner.run(request, submittedBy);
        } catch (IllegalArgumentException e) {
            log.warn("Rejected job {}: {}", request.jobId(), e.getMessage());
            result = new ProcessingJobResult(request.jobId(), request.correlationId(), request.profileCode(), null,
                    JobStatus.FAILED, request.text(), null, null, List.of(), e.getMessage(), request.attributes());
        } catch (RuntimeException e) {
            throw new AmqpRejectAndDontRequeueException("Job " + request.jobId() + " failed: " + e.getMessage(), e);
        }
        publisher.publish(result);
    }
}
