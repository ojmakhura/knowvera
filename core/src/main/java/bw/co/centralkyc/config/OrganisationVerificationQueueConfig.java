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
public class OrganisationVerificationQueueConfig {

    private final RabbitProperties rabbitProperties;

    public OrganisationVerificationQueueConfig(RabbitProperties rabbitProperties) {
        this.rabbitProperties = rabbitProperties;
    }

    @Bean
    public Declarables organisationVerificationQueueSchema() {
        DirectExchange organisationVerificationQueueExchange = new DirectExchange(
                rabbitProperties.getOrganisationVerificationQueueExchange());
        Queue organisationVerificationQueue = QueueBuilder
                .durable(rabbitProperties.getOrganisationVerificationQueue())
                .build();

        return new Declarables(
                organisationVerificationQueueExchange,
                organisationVerificationQueue,
                BindingBuilder.bind(organisationVerificationQueue).to(organisationVerificationQueueExchange)
                        .with(rabbitProperties.getOrganisationVerificationQueueRoutingKey()));
    }
}