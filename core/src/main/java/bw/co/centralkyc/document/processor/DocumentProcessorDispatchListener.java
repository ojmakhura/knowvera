package bw.co.centralkyc.document.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import bw.co.centralkyc.QueueObject;
import bw.co.centralkyc.properties.RabbitProperties;

@Service
public class DocumentProcessorDispatchListener {

    private final static Logger log = LoggerFactory.getLogger(DocumentProcessorDispatchListener.class);

    private final RabbitTemplate rabbitTemplate;
    private final DocumentProcessorService documentProcessorService;
    private final RabbitProperties rabbitProperties;

    public DocumentProcessorDispatchListener(
            RabbitTemplate rabbitTemplate,
            DocumentProcessorService documentProcessorService,
            RabbitProperties rabbitProperties) {
        this.rabbitTemplate = rabbitTemplate;
        this.documentProcessorService = documentProcessorService;
        this.rabbitProperties = rabbitProperties;
    }

    @RabbitListener(queues = "${app.rabbitmq.documentDispatchQueue}")
    public void handleDocumentDispatch(QueueObject queueObject) {

        log.info("Received document dispatch message for document ID: {}", queueObject.documentId());

        try {
            rabbitTemplate.convertAndSend(
                    rabbitProperties.getDocumentQueueExchange(),
                    rabbitProperties.getDocumentQueueRoutingKey(),
                    queueObject);

        } catch (Exception e) {
            log.error("Error processing document with ID {}: {}", queueObject.documentId(), e.getMessage());
            // Handle exceptions (e.g., retry logic, send to a dead-letter queue, etc.)
        }
    }
}