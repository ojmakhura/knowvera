package bw.co.knowvera.processing.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import bw.co.knowvera.processing.messaging.ProcessingMessaging;

@Component
public class ResultPublisher {

    private static final Logger log = LoggerFactory.getLogger(ResultPublisher.class);
    private static final int ATTEMPTS = 3;

    private final RabbitTemplate rabbitTemplate;

    public ResultPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    /**
     * Publishes a result, trying again when the broker does not take it: the job is done, and its
     * caller would otherwise never hear of it.
     */
    public void publish(ProcessingJobResult result) {
        for (int attempt = 1;; attempt++) {
            try {
                rabbitTemplate.convertAndSend(ProcessingMessaging.EXCHANGE, ProcessingMessaging.RESULTS_ROUTING_KEY, result);
                return;
            } catch (AmqpException e) {
                if (attempt == ATTEMPTS || !pause(attempt)) {
                    throw e;
                }
                log.warn("Publishing the result of job {} failed, trying again: {}", result.jobId(), e.getMessage());
            }
        }
    }

    private static boolean pause(int attempt) {
        try {
            Thread.sleep(1000L * attempt);
            return true;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }
    }

}
