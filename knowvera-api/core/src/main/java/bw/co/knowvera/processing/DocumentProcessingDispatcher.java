package bw.co.knowvera.processing;

import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import bw.co.knowvera.document.DocumentDTO;
import bw.co.knowvera.processing.job.ProcessingJobRequest;
import bw.co.knowvera.processing.messaging.ProcessingMessaging;
import bw.co.knowvera.processing.profile.StepType;

/**
 * Sends documents to the processing; {@link ProcessingResultHandler} applies the results.
 */
@Service
public class DocumentProcessingDispatcher {

    private static final Logger log = LoggerFactory.getLogger(DocumentProcessingDispatcher.class);

    private final RabbitTemplate rabbitTemplate;
    private final ProcessingJobFactory jobFactory;

    public DocumentProcessingDispatcher(RabbitTemplate rabbitTemplate, ProcessingJobFactory jobFactory) {
        this.rabbitTemplate = rabbitTemplate;
        this.jobFactory = jobFactory;
    }

    /** Processes a newly uploaded (or replaced) document from its file. */
    public void process(DocumentDTO document) {
        submit(document, null);
    }

    /** Extracts the fields from, and confirms the type of, the document's stored text. */
    public void analyse(DocumentDTO document) {
        submit(document, StepType.FIELD_EXTRACTION);
    }

    private void submit(DocumentDTO document, @Nullable StepType fromStep) {
        ProcessingJobRequest job = jobFactory.create(document, fromStep);
        rabbitTemplate.convertAndSend(ProcessingMessaging.EXCHANGE, ProcessingMessaging.JOBS_ROUTING_KEY, job);
        log.info("Document {} sent to the processing as job {} (profile {})", document.getId(), job.jobId(),
                job.profileCode());
    }
}
