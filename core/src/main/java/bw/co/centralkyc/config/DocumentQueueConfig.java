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
    public Declarables textExtractionQueueSchema() {
        DirectExchange textExtractionQueueExchange = new DirectExchange(rabbitProperties.getTextExtractionQueueExchange());
        Queue textExtractionQueue = QueueBuilder.durable(rabbitProperties.getTextExtractionQueue()).build();

        return new Declarables(
                textExtractionQueueExchange,
                textExtractionQueue,
                BindingBuilder.bind(textExtractionQueue).to(textExtractionQueueExchange)
                        .with(rabbitProperties.getTextExtractionQueueRoutingKey()));
    }
}
