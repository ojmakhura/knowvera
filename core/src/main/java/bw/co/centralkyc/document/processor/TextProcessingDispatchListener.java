package bw.co.centralkyc.document.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import bw.co.centralkyc.QueueObject;
import bw.co.centralkyc.properties.RabbitProperties;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TextProcessingDispatchListener {

    private static final Logger log = LoggerFactory.getLogger(TextProcessingDispatchListener.class);

    private final RabbitTemplate rabbitTemplate;
    private final RabbitProperties rabbitProperties;

    @RabbitListener(queues = "${app.rabbitmq.textProcessingDispatchQueue}")
    public void handleTextProcessingDispatch(QueueObject queueObject) {
        log.info("Received text-processing dispatch for document ID: {}", queueObject.documentId());

        rabbitTemplate.convertAndSend(
                rabbitProperties.getTextProcessingQueueExchange(),
                rabbitProperties.getTextProcessingQueueRoutingKey(),
                queueObject);
    }
}
