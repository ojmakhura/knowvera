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
public class KycVerificationQueueConfig {

    private final RabbitProperties rabbitProperties;

    public KycVerificationQueueConfig(RabbitProperties rabbitProperties) {
        this.rabbitProperties = rabbitProperties;
    }

    @Bean
    public Declarables kycVerificationQueueSchema() {
        DirectExchange kycVerificationQueueExchange = new DirectExchange(
                rabbitProperties.getKycVerificationQueueExchange());
        Queue kycVerificationQueue = QueueBuilder
                .durable(rabbitProperties.getKycVerificationQueue())
                .build();

        return new Declarables(
                kycVerificationQueueExchange,
                kycVerificationQueue,
                BindingBuilder.bind(kycVerificationQueue).to(kycVerificationQueueExchange)
                        .with(rabbitProperties.getKycVerificationQueueRoutingKey()));
    }
}
