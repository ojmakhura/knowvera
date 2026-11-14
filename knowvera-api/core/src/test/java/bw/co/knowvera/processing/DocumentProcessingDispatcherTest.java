package bw.co.knowvera.processing;

import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import bw.co.knowvera.document.DocumentDTO;
import bw.co.knowvera.processing.job.ProcessingJobRequest;
import bw.co.knowvera.processing.messaging.ProcessingMessaging;
import bw.co.knowvera.processing.profile.StepType;

@ExtendWith(MockitoExtension.class)
class DocumentProcessingDispatcherTest {

    @Mock
    private RabbitTemplate rabbitTemplate;
    @Mock
    private ProcessingJobFactory jobFactory;

    private final DocumentDTO document = new DocumentDTO();
    private final ProcessingJobRequest job = new ProcessingJobRequest("job-1", "doc-1", "default", null, null,
            List.of(), List.of(), null, null, Map.of());

    @Test
    void processingSendsAJobForTheWholeProfile() {
        when(jobFactory.create(same(document), isNull())).thenReturn(job);

        new DocumentProcessingDispatcher(rabbitTemplate, jobFactory).process(document);

        verify(rabbitTemplate).convertAndSend(ProcessingMessaging.EXCHANGE, ProcessingMessaging.JOBS_ROUTING_KEY, job);
    }

    @Test
    void analysisStartsAtFieldExtraction() {
        when(jobFactory.create(document, StepType.FIELD_EXTRACTION)).thenReturn(job);

        new DocumentProcessingDispatcher(rabbitTemplate, jobFactory).analyse(document);

        verify(rabbitTemplate).convertAndSend(ProcessingMessaging.EXCHANGE, ProcessingMessaging.JOBS_ROUTING_KEY, job);
    }
}
