package bw.co.centralkyc.config;

import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Declarables;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import bw.co.centralkyc.properties.RabbitProperties;

@Configuration
@EnableConfigurationProperties(RabbitProperties.class)
public class IndividualVerificationQueueConfig {

    private final RabbitProperties rabbitProperties;

    public IndividualVerificationQueueConfig(RabbitProperties rabbitProperties) {
        this.rabbitProperties = rabbitProperties;
    }

    @Bean
    public Declarables individualVerificationQueueSchema() {
        DirectExchange individualVerificationQueueExchange = new DirectExchange(
                rabbitProperties.getIndividualVerificationQueueExchange());
        Queue individualVerificationQueue = QueueBuilder
                .durable(rabbitProperties.getIndividualVerificationQueue())
                .build();

        return new Declarables(
                individualVerificationQueueExchange,
                individualVerificationQueue,
                BindingBuilder.bind(individualVerificationQueue).to(individualVerificationQueueExchange)
                        .with(rabbitProperties.getIndividualVerificationQueueRoutingKey()));
    }
}