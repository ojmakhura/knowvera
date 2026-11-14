package bw.co.knowvera.messaging;

import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

/**
 * Publishes with a broker confirm. Each message gets an id that is both its AMQP message id (the
 * listeners' Novu transactionId) and its correlation id, so a rejected or unroutable publish is
 * logged with the id that identifies it everywhere else.
 */
@Component
public class ConfirmedPublisher {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfirmedPublisher.class);

    private final RabbitTemplate rabbitTemplate;

    public ConfirmedPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    /**
     * Converts and publishes {@code payload}; returns its message id. Does not wait: a rejected
     * (nack) or unroutable publish is logged when the broker answers.
     */
    public String publish(String exchange, String routingKey, Object payload) {

        String id = UUID.randomUUID().toString();
        CorrelationData correlation = new CorrelationData(id);
        correlation.getFuture().whenComplete((confirm, error) -> {
            if (error != null) {
                LOGGER.error("No confirm for message {} to exchange '{}' ({}): {}", id, exchange, routingKey,
                        error.getMessage());
            } else if (!confirm.ack()) {
                LOGGER.error("RabbitMQ rejected message {} to exchange '{}' ({}): {}", id, exchange, routingKey,
                        confirm.reason());
            }
            // Unroutable messages are logged by RabbitMQConfig's returns callback
        });

        rabbitTemplate.convertAndSend(exchange, routingKey, payload, message -> {
            message.getMessageProperties().setMessageId(id);
            return message;
        }, correlation);
        return id;
    }

    /**
     * Publishes a ready-made message and waits for the broker: true only when it was confirmed and
     * routed to a queue. Used where the caller must not lose the message (re-driving dead letters).
     */
    public boolean publishAndWait(String exchange, String routingKey, Message message, Duration timeout) {

        String id = message.getMessageProperties().getMessageId() != null
                ? message.getMessageProperties().getMessageId() : UUID.randomUUID().toString();
        CorrelationData correlation = new CorrelationData(id);
        rabbitTemplate.send(exchange, routingKey, message, correlation);
        try {
            CorrelationData.Confirm confirm = correlation.getFuture().get(timeout.toMillis(), TimeUnit.MILLISECONDS);
            if (!confirm.ack()) {
                LOGGER.error("RabbitMQ rejected message {} to exchange '{}' ({}): {}", id, exchange, routingKey,
                        confirm.reason());
                return false;
            }
            return correlation.getReturned() == null;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        } catch (ExecutionException | TimeoutException e) {
            LOGGER.error("No confirm for message {} to exchange '{}' ({}) within {}", id, exchange, routingKey, timeout);
            return false;
        }
    }
}
