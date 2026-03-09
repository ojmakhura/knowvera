package bw.co.centralkyc.config;

import org.springframework.amqp.core.*;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import bw.co.centralkyc.properties.RabbitProperties;

@Configuration
@EnableConfigurationProperties(RabbitProperties.class)
public class DocumentQueueConfig {
    private final RabbitProperties rabbitProperties;

    public DocumentQueueConfig(RabbitProperties rabbitProperties) {
        this.rabbitProperties = rabbitProperties;
    }

    @Bean
    public Queue documentHandlerQueue() {
        return QueueBuilder.durable(rabbitProperties.getDocumentHandler())
                .withArgument("x-dead-letter-exchange", rabbitProperties.getDocumentHandlerDeadLetterExchange())
                .withArgument("x-dead-letter-routing-key", rabbitProperties.getDocumentHandlerDeadLetterRoutingKey())
                .build();
    }

    @Bean
    public Declarables documentDispatchSchema() {
        FanoutExchange documentDispatchExchange = new FanoutExchange(rabbitProperties.getDocumentDispatchExchange());
        Queue documentDispatchQueue = QueueBuilder.durable(rabbitProperties.getDocumentDispatchQueue()).build();

        return new Declarables(
                documentDispatchExchange,
                documentDispatchQueue,
                BindingBuilder.bind(documentDispatchQueue).to(documentDispatchExchange));
    }

    @Bean
    public Declarables deadLetterSchema() {
        DirectExchange deadLetterExchange = new DirectExchange(rabbitProperties.getDocumentHandlerDeadLetterExchange());
        Queue deadLetterQueue = QueueBuilder.durable(rabbitProperties.getDocumentHandlerDeadLetterQueue()).build();

        return new Declarables(
                deadLetterExchange,
                deadLetterQueue,
                BindingBuilder.bind(deadLetterQueue).to(deadLetterExchange)
                        .with(rabbitProperties.getDocumentHandlerDeadLetterRoutingKey()));
    }

    @Bean
    public Declarables documentQueueSchema() {
        DirectExchange documentQueueExchange = new DirectExchange(rabbitProperties.getDocumentQueueExchange());
        Queue documentQueue = QueueBuilder.durable(rabbitProperties.getDocumentQueue()).build();

        return new Declarables(
                documentQueueExchange,
                documentQueue,
                BindingBuilder.bind(documentQueue).to(documentQueueExchange)
                        .with(rabbitProperties.getDocumentQueueRoutingKey()));
    }
}
