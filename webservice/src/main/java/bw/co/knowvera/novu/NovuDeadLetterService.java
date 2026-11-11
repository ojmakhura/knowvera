package bw.co.knowvera.novu;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.core.QueueInformation;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.DefaultMessagePropertiesConverter;
import org.springframework.amqp.rabbit.support.MessagePropertiesConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.rabbitmq.client.GetResponse;

import bw.co.knowvera.messaging.ConfirmedPublisher;

/**
 * The Novu dead-letter queues ({@code <queue>.dlq}, filled by {@link NovuQueueConfig} after the
 * retries): how many messages wait there, and moving them back to their queue once the cause
 * (Novu outage, wrong key, missing template) is fixed.
 *
 * <p>Re-driving is at-least-once: a message is taken with a manual acknowledgement and only
 * acknowledged (removed) after its copy was confirmed and routed. The copy keeps its message id,
 * which is Novu's transactionId, so a message Novu had in fact accepted is not delivered twice.
 */
@Service
public class NovuDeadLetterService {

    private static final Logger LOGGER = LoggerFactory.getLogger(NovuDeadLetterService.class);

    /** Most messages one call moves; callers repeat for more. */
    static final int MAX_BATCH = 1000;

    /** Headers RepublishMessageRecoverer adds; removed so a re-driven message starts clean. */
    static final List<String> FAILURE_HEADERS = List.of("x-exception-message", "x-exception-stacktrace",
            "x-original-exchange", "x-original-routingKey");

    private static final Duration CONFIRM_TIMEOUT = Duration.ofSeconds(10);

    /** A Novu queue and where its messages are published. */
    record Route(String queue, String exchange, String routingKey) {
        String deadLetterQueue() {
            return queue + NovuQueueConfig.DLQ_SUFFIX;
        }
    }

    private final RabbitTemplate rabbitTemplate;
    private final AmqpAdmin amqpAdmin;
    private final ConfirmedPublisher publisher;
    private final Map<String, Route> routes = new LinkedHashMap<>();
    private final MessagePropertiesConverter propertiesConverter = new DefaultMessagePropertiesConverter();

    public NovuDeadLetterService(RabbitTemplate rabbitTemplate, AmqpAdmin amqpAdmin, ConfirmedPublisher publisher,
            @Value("${app.novu.queue.newUserQueue}") String newUserQueue,
            @Value("${app.novu.queue.newUserQueueExchange}") String newUserExchange,
            @Value("${app.novu.queue.newUserQueueRoutingKey}") String newUserRoutingKey,
            @Value("${app.novu.queue.newOrgUserQueue}") String newOrgUserQueue,
            @Value("${app.novu.queue.newOrgUserQueueExchange}") String newOrgUserExchange,
            @Value("${app.novu.queue.newOrgUserQueueRoutingKey}") String newOrgUserRoutingKey,
            @Value("${app.novu.queue.newOrgClientRequestQueue}") String newOrgClientRequestQueue,
            @Value("${app.novu.queue.newOrgClientRequestQueueExchange}") String newOrgClientRequestExchange,
            @Value("${app.novu.queue.newOrgClientRequestQueueRoutingKey}") String newOrgClientRequestRoutingKey,
            @Value("${app.novu.queue.newKycRecordQueue}") String newKycRecordQueue,
            @Value("${app.novu.queue.newKycRecordQueueExchange}") String newKycRecordExchange,
            @Value("${app.novu.queue.newKycRecordQueueRoutingKey}") String newKycRecordRoutingKey) {

        this.rabbitTemplate = rabbitTemplate;
        this.amqpAdmin = amqpAdmin;
        this.publisher = publisher;
        for (Route route : List.of(
                new Route(newUserQueue, newUserExchange, newUserRoutingKey),
                new Route(newOrgUserQueue, newOrgUserExchange, newOrgUserRoutingKey),
                new Route(newOrgClientRequestQueue, newOrgClientRequestExchange, newOrgClientRequestRoutingKey),
                new Route(newKycRecordQueue, newKycRecordExchange, newKycRecordRoutingKey))) {
            routes.put(route.queue(), route);
        }
    }

    /** Messages waiting in each dead-letter queue, by Novu queue name. */
    public Map<String, Long> counts() {

        Map<String, Long> counts = new LinkedHashMap<>();
        routes.values().forEach(route -> {
            QueueInformation info = amqpAdmin.getQueueInfo(route.deadLetterQueue());
            counts.put(route.queue(), info == null ? 0L : info.getMessageCount());
        });
        return counts;
    }

    /**
     * Moves up to {@code max} messages from {@code queue}'s dead-letter queue back to {@code queue}.
     * Stops at the first message the broker does not take back; that message stays dead-lettered.
     *
     * @return how many messages were moved
     */
    public int redrive(String queue, int max) {

        Route route = routes.get(queue);
        if (route == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unknown Novu queue " + queue);
        }
        if (max < 1 || max > MAX_BATCH) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "max must be between 1 and " + MAX_BATCH);
        }

        Integer moved = rabbitTemplate.execute(channel -> {
            int count = 0;
            while (count < max) {
                GetResponse response = channel.basicGet(route.deadLetterQueue(), false);
                if (response == null) {
                    break;  // empty
                }
                long tag = response.getEnvelope().getDeliveryTag();
                Message copy = withoutFailureHeaders(response);
                if (publisher.publishAndWait(route.exchange(), route.routingKey(), copy, CONFIRM_TIMEOUT)) {
                    channel.basicAck(tag, false);
                    count++;
                } else {
                    requeue(channel, tag);
                    LOGGER.error("Re-driving {} stopped after {} message(s): RabbitMQ did not take message {} back",
                            route.deadLetterQueue(), count, copy.getMessageProperties().getMessageId());
                    break;
                }
            }
            return count;
        });

        int result = moved == null ? 0 : moved;
        LOGGER.info("Re-drove {} message(s) from {} to {}", result, route.deadLetterQueue(), route.queue());
        return result;
    }

    private Message withoutFailureHeaders(GetResponse response) {

        MessageProperties properties = propertiesConverter.toMessageProperties(response.getProps(),
                response.getEnvelope(), StandardCharsets.UTF_8.name());
        FAILURE_HEADERS.forEach(properties.getHeaders()::remove);
        return new Message(response.getBody(), properties);
    }

    private static void requeue(com.rabbitmq.client.Channel channel, long tag) {
        try {
            channel.basicNack(tag, false, true);
        } catch (IOException e) {
            // The channel closes; the broker returns the unacknowledged message to the queue anyway
            LOGGER.warn("Could not return a dead letter to its queue: {}", e.getMessage());
        }
    }
}
