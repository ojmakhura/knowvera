package bw.co.centralkyc.document.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class DocumentProcessorDispatchListener {

    private final static Logger log = LoggerFactory.getLogger(DocumentProcessorDispatchListener.class);

    private final RabbitTemplate rabbitTemplate;
    private final DocumentProcessorService documentProcessorService;

    public DocumentProcessorDispatchListener(RabbitTemplate rabbitTemplate, DocumentProcessorService documentProcessorService) {
        this.rabbitTemplate = rabbitTemplate;
        this.documentProcessorService = documentProcessorService;
    }

    @RabbitListener(queues = "${app.rabbitmq.document-dispatch-queue}")
    public void handleDocumentDispatch(String documentId) {

        log.info("Received document dispatch message for document ID: {}", documentId);

        try {
            // After processing, you can send the result to another queue or update the document record
            // For example, you could send a message to a "document-processed" queue with the extracted text
            rabbitTemplate.convertAndSend("x.post-document-dispatch", "", documentId);

        } catch (Exception e) {
            log.error("Error processing document with ID {}: {}", documentId, e.getMessage());
            // Handle exceptions (e.g., retry logic, send to a dead-letter queue, etc.)
        }
    }
}