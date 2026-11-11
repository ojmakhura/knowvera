package bw.co.roguesystems.ai.messaging;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;

import bw.co.roguesystems.ai.job.JobStatus;
import bw.co.roguesystems.ai.job.ProcessingJobRequest;
import bw.co.roguesystems.ai.job.ProcessingJobResult;
import bw.co.roguesystems.ai.pipeline.PipelineRunner;

class JobListenerTest {

    private final PipelineRunner runner = mock(PipelineRunner.class);
    private final ResultPublisher publisher = mock(ResultPublisher.class);
    private final JobListener listener = new JobListener(runner, publisher);

    private static ProcessingJobRequest job(String jobId) {
        return new ProcessingJobRequest(jobId, "doc-1", "default", null, "National ID", List.of(), List.of(), null,
                "text", Map.of("k", "v"));
    }

    @Test
    void publishesTheResultOfAJob() {
        ProcessingJobResult result = new ProcessingJobResult("job-1", "doc-1", "default", 1, JobStatus.COMPLETED,
                "text", null, null, List.of(), null, Map.of());
        when(runner.run(job("job-1"), "portal")).thenReturn(result);

        listener.handle(job("job-1"), "portal");

        verify(publisher).publish(result);
    }

    @Test
    void aJobTheProcessorCannotAcceptIsAnsweredAsFailed() {
        when(runner.run(any(), any())).thenThrow(new IllegalArgumentException("A job needs a profileCode"));

        listener.handle(job("job-1"), null);

        ArgumentCaptor<ProcessingJobResult> published = ArgumentCaptor.forClass(ProcessingJobResult.class);
        verify(publisher).publish(published.capture());
        assertThat(published.getValue().status()).isEqualTo(JobStatus.FAILED);
        assertThat(published.getValue().error()).isEqualTo("A job needs a profileCode");
        assertThat(published.getValue().attributes()).containsEntry("k", "v");
    }

    @Test
    void jobsThatCannotBeAnsweredOrHitAnInfrastructureFailureAreDeadLettered() {
        assertThatThrownBy(() -> listener.handle(job(" "), null)).isInstanceOf(AmqpRejectAndDontRequeueException.class);

        when(runner.run(any(), any())).thenThrow(new IllegalStateException("database down"));
        assertThatThrownBy(() -> listener.handle(job("job-1"), null))
                .isInstanceOf(AmqpRejectAndDontRequeueException.class);
        verify(publisher, never()).publish(any());
    }
}
