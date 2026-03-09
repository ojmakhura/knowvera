package bw.co.centralkyc.config;

import org.springframework.amqp.core.*;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import bw.co.centralkyc.properties.RabbitProperties;

@Configuration
@EnableConfigurationProperties(RabbitProperties.class)
public class TextProcessingQueueConfig {
    private final RabbitProperties rabbitProperties;

    public TextProcessingQueueConfig(RabbitProperties rabbitProperties) {
        this.rabbitProperties = rabbitProperties;
    }

    @Bean
    public Queue textProcessingHandlerQueue() {
        return QueueBuilder.durable(rabbitProperties.getTextProcessingHandler())
                .withArgument("x-dead-letter-exchange", rabbitProperties.getTextProcessingHandlerDeadLetterExchange())
                .withArgument("x-dead-letter-routing-key", rabbitProperties.getTextProcessingHandlerDeadLetterRoutingKey())
                .build();
    }

    @Bean
    public Declarables textProcessingDispatchSchema() {
        FanoutExchange textProcessingDispatchExchange = new FanoutExchange(
                rabbitProperties.getTextProcessingDispatchExchange());
        Queue textProcessingDispatchQueue = QueueBuilder.durable(rabbitProperties.getTextProcessingDispatchQueue()).build();

        return new Declarables(
                textProcessingDispatchExchange,
                textProcessingDispatchQueue,
                BindingBuilder.bind(textProcessingDispatchQueue).to(textProcessingDispatchExchange));
    }

    @Bean
    public Declarables textProcessingQueueSchema() {
        DirectExchange textProcessingQueueExchange = new DirectExchange(rabbitProperties.getTextProcessingQueueExchange());
        Queue textProcessingQueue = QueueBuilder.durable(rabbitProperties.getTextProcessingQueue()).build();

        return new Declarables(
                textProcessingQueueExchange,
                textProcessingQueue,
                BindingBuilder.bind(textProcessingQueue).to(textProcessingQueueExchange)
                        .with(rabbitProperties.getTextProcessingQueueRoutingKey()));
    }
}
