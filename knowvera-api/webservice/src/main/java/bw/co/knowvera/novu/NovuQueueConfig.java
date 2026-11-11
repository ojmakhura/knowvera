package bw.co.knowvera.novu;

import java.time.Duration;
import java.util.concurrent.Executors;

import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Declarables;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.config.RetryInterceptorBuilder;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.retry.RepublishMessageRecoverer;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NovuQueueConfig {

    /** Listener container factory of the Novu queues: bounded retries, then the dead-letter queue. */
    public static final String CONTAINER_FACTORY = "novuListenerContainerFactory";

    /** Suffix of each queue's dead-letter queue. */
    static final String DLQ_SUFFIX = ".dlq";

    @Value("${app.novu.queue.deadLetterExchange:x.novu-dead-letter}")
    private String deadLetterExchange;

    @Value("${app.novu.retry.max-retries:3}")
    private int maxRetries;

    @Value("${app.novu.retry.initial-interval:2s}")
    private Duration initialInterval;

    @Value("${app.novu.retry.multiplier:3}")
    private double multiplier;

    @Value("${app.novu.retry.max-interval:30s}")
    private Duration maxInterval;

    /** Consumers per Novu queue; a message waiting between retries holds only its own consumer. */
    @Value("${app.novu.consumers.concurrency:3}")
    private int concurrentConsumers;

    @Value("${app.novu.consumers.max-concurrency:10}")
    private int maxConcurrentConsumers;

    @Value("${app.novu.queue.newUserQueueExchange}")
    private String newUserQueueExchange;

    @Value("${app.novu.queue.newUserQueue}")
    private String newUserQueue;

    @Value("${app.novu.queue.newUserQueueRoutingKey}")
    private String newUserQueueRoutingKey;

    @Value("${app.novu.queue.newOrgUserQueueExchange}")
    private String newOrgUserQueueExchange;

    @Value("${app.novu.queue.newOrgUserQueue}")
    private String newOrgUserQueue;

    @Value("${app.novu.queue.newOrgUserQueueRoutingKey}")
    private String newOrgUserQueueRoutingKey;

    @Value("${app.novu.queue.newOrgClientRequestQueueExchange}")
    private String newOrgClientRequestQueueExchange;

    @Value("${app.novu.queue.newOrgClientRequestQueue}")
    private String newOrgClientRequestQueue;

    @Value("${app.novu.queue.newOrgClientRequestQueueRoutingKey}")
    private String newOrgClientRequestQueueRoutingKey;

    @Value("${app.novu.queue.newKycRecordQueueExchange}")
    private String newKycRecordQueueExchange;

    @Value("${app.novu.queue.newKycRecordQueue}")
    private String newKycRecordQueue;

    @Value("${app.novu.queue.newKycRecordQueueRoutingKey}")
    private String newKycRecordQueueRoutingKey;

    /**
     * Retries a failed trigger with back-off, then republishes it, with the exception in its
     * headers, to the dead-letter exchange under its original routing key. The main queues keep
     * their arguments, so existing brokers need no queue changes.
     */
    @Bean(CONTAINER_FACTORY)
    public SimpleRabbitListenerContainerFactory novuListenerContainerFactory(ConnectionFactory connectionFactory,
            JacksonJsonMessageConverter converter, RabbitTemplate rabbitTemplate) {

        RepublishMessageRecoverer recoverer = new RepublishMessageRecoverer(rabbitTemplate, deadLetterExchange)
                .errorRoutingKeyPrefix("");

        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setAcknowledgeMode(AcknowledgeMode.AUTO);
        factory.setDefaultRequeueRejected(false);
        factory.setMessageConverter(converter);
        factory.setTaskExecutor(Executors.newVirtualThreadPerTaskExecutor());
        // Several consumers, each holding one unacknowledged message: while one message waits
        // between retries, the others keep the queue moving
        factory.setConcurrentConsumers(concurrentConsumers);
        factory.setMaxConcurrentConsumers(Math.max(concurrentConsumers, maxConcurrentConsumers));
        factory.setPrefetchCount(1);
        factory.setAdviceChain(RetryInterceptorBuilder.stateless()
                .maxRetries(maxRetries)
                .backOffOptions(initialInterval.toMillis(), multiplier, maxInterval.toMillis())
                .recoverer(recoverer)
                .build());
        return factory;
    }

    @Bean
    public Declarables novuDeadLetterSchema() {

        DirectExchange exchange = new DirectExchange(deadLetterExchange);
        Queue newUserDlq = QueueBuilder.durable(newUserQueue + DLQ_SUFFIX).build();
        Queue newOrgUserDlq = QueueBuilder.durable(newOrgUserQueue + DLQ_SUFFIX).build();
        Queue newOrgClientRequestDlq = QueueBuilder.durable(newOrgClientRequestQueue + DLQ_SUFFIX).build();
        Queue newKycRecordDlq = QueueBuilder.durable(newKycRecordQueue + DLQ_SUFFIX).build();

        return new Declarables(
                exchange,
                newUserDlq,
                newOrgUserDlq,
                newOrgClientRequestDlq,
                newKycRecordDlq,
                BindingBuilder.bind(newUserDlq).to(exchange).with(newUserQueueRoutingKey),
                BindingBuilder.bind(newOrgUserDlq).to(exchange).with(newOrgUserQueueRoutingKey),
                BindingBuilder.bind(newOrgClientRequestDlq).to(exchange).with(newOrgClientRequestQueueRoutingKey),
                BindingBuilder.bind(newKycRecordDlq).to(exchange).with(newKycRecordQueueRoutingKey));
    }

    @Bean
    public Declarables newUserQueueSchema() {
        DirectExchange newUserExchange = new DirectExchange(newUserQueueExchange);
        Queue newUserQ = QueueBuilder.durable(newUserQueue).build();

        return new Declarables(
                newUserExchange,
                newUserQ,
                BindingBuilder.bind(newUserQ).to(newUserExchange).with(newUserQueueRoutingKey));
    }

    @Bean
    public Declarables newOrgUserQueueSchema() {
        DirectExchange newOrgUserExchange = new DirectExchange(newOrgUserQueueExchange);
        Queue newOrgUserQ = QueueBuilder.durable(newOrgUserQueue).build();

        return new Declarables(
                newOrgUserExchange,
                newOrgUserQ,
                BindingBuilder.bind(newOrgUserQ).to(newOrgUserExchange).with(newOrgUserQueueRoutingKey));
    }

    @Bean
    public Declarables newOrgClientRequestQueueSchema() {
        DirectExchange newOrgClientRequestExchange = new DirectExchange(newOrgClientRequestQueueExchange);
        Queue newOrgClientRequestQ = QueueBuilder.durable(newOrgClientRequestQueue).build();

        return new Declarables(
                newOrgClientRequestExchange,
                newOrgClientRequestQ,
                BindingBuilder.bind(newOrgClientRequestQ).to(newOrgClientRequestExchange).with(newOrgClientRequestQueueRoutingKey));
    }

    @Bean
    public Declarables newKycRecordQueueSchema() {
        DirectExchange newKycRecordExchange = new DirectExchange(newKycRecordQueueExchange);
        Queue newKycRecordQ = QueueBuilder.durable(newKycRecordQueue).build();
        return new Declarables(
                newKycRecordExchange,
                newKycRecordQ,
                BindingBuilder.bind(newKycRecordQ).to(newKycRecordExchange).with(newKycRecordQueueRoutingKey));
    }
}
