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
public class ExtractedInformationQueueConfig {
    private final RabbitProperties rabbitProperties;

    public ExtractedInformationQueueConfig(RabbitProperties rabbitProperties) {
        this.rabbitProperties = rabbitProperties;
    }

    @Bean
    public Declarables informationConfirmationQueueSchema() {
        DirectExchange informationConfirmationQueueExchange = new DirectExchange(
                rabbitProperties.getInformationConfirmationQueueExchange());
        Queue informationConfirmationQueue = QueueBuilder
                .durable(rabbitProperties.getInformationConfirmationQueue())
                .build();

        return new Declarables(
                informationConfirmationQueueExchange,
                informationConfirmationQueue,
                BindingBuilder.bind(informationConfirmationQueue).to(informationConfirmationQueueExchange)
                        .with(rabbitProperties.getInformationConfirmationQueueRoutingKey()));
    }
}
