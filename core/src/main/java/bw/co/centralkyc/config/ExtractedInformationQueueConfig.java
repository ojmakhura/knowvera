package bw.co.centralkyc.config;

import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Declarables;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import bw.co.centralkyc.properties.RabbitProperties;

@Configuration
@EnableConfigurationProperties(RabbitProperties.class)
public class ExtractedInformationQueueConfig {
    private final RabbitProperties rabbitProperties;

    public ExtractedInformationQueueConfig(RabbitProperties rabbitProperties) {
        this.rabbitProperties = rabbitProperties;
    }

    @Bean
    public Queue extractedInformationHandlerQueue() {
        return QueueBuilder.durable(rabbitProperties.getExtractedInformationHandler())
                .withArgument("x-dead-letter-exchange", rabbitProperties.getExtractedInformationHandlerDeadLetterExchange())
                .withArgument("x-dead-letter-routing-key", rabbitProperties.getExtractedInformationHandlerDeadLetterRoutingKey())
                .build();
    }

    @Bean
    public Declarables extractedInformationDispatchSchema() {
        FanoutExchange extractedInformationDispatchExchange = new FanoutExchange(
                rabbitProperties.getExtractedInformationDispatchExchange());
        Queue extractedInformationDispatchQueue = QueueBuilder
                .durable(rabbitProperties.getExtractedInformationDispatchQueue())
                .build();

        return new Declarables(
                extractedInformationDispatchExchange,
                extractedInformationDispatchQueue,
                BindingBuilder.bind(extractedInformationDispatchQueue).to(extractedInformationDispatchExchange));
    }

    @Bean
    public Declarables extractedInformationDeadLetterSchema() {
        DirectExchange deadLetterExchange = new DirectExchange(
                rabbitProperties.getExtractedInformationHandlerDeadLetterExchange());
        Queue deadLetterQueue = QueueBuilder
                .durable(rabbitProperties.getExtractedInformationHandlerDeadLetterQueue())
                .build();

        return new Declarables(
                deadLetterExchange,
                deadLetterQueue,
                BindingBuilder.bind(deadLetterQueue).to(deadLetterExchange)
                        .with(rabbitProperties.getExtractedInformationHandlerDeadLetterRoutingKey()));
    }

    @Bean
    public Declarables extractedInformationQueueSchema() {
        DirectExchange extractedInformationQueueExchange = new DirectExchange(
                rabbitProperties.getExtractedInformationQueueExchange());
        Queue extractedInformationQueue = QueueBuilder.durable(rabbitProperties.getExtractedInformationQueue()).build();

        return new Declarables(
                extractedInformationQueueExchange,
                extractedInformationQueue,
                BindingBuilder.bind(extractedInformationQueue).to(extractedInformationQueueExchange)
                        .with(rabbitProperties.getExtractedInformationQueueRoutingKey()));
    }
}
