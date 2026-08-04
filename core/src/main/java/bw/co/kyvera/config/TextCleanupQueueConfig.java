package bw.co.kyvera.config;

import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Declarables;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import bw.co.kyvera.properties.RabbitProperties;

@Configuration
@EnableConfigurationProperties(RabbitProperties.class)
public class TextCleanupQueueConfig {
    private final RabbitProperties rabbitProperties;

    public TextCleanupQueueConfig(RabbitProperties rabbitProperties) {
        this.rabbitProperties = rabbitProperties;
    }

    @Bean
    public Declarables textCleanupQueueSchema() {
        DirectExchange textCleanupQueueExchange = new DirectExchange(
                rabbitProperties.getTextCleanupQueueExchange());
        Queue textCleanupQueue = QueueBuilder
                .durable(rabbitProperties.getTextCleanupQueue())
                .build();

        return new Declarables(
                textCleanupQueueExchange,
                textCleanupQueue,
                BindingBuilder.bind(textCleanupQueue).to(textCleanupQueueExchange)
                        .with(rabbitProperties.getTextCleanupQueueRoutingKey()));
    }
}
