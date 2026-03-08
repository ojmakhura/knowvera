package bw.co.centralkyc.config;

// import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import bw.co.centralkyc.properties.RabbitProperties;
import tools.jackson.databind.json.JsonMapper;

@Configuration
@EnableConfigurationProperties(RabbitProperties.class)
public class RabbitMQConfig {
    private final CachingConnectionFactory cachingConnectionFactory;
    private final RabbitProperties rabbitProperties;

    public RabbitMQConfig(CachingConnectionFactory cachingConnectionFactory, RabbitProperties rabbitProperties) {
        this.cachingConnectionFactory = cachingConnectionFactory;
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
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            JacksonJsonMessageConverter converter) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(cachingConnectionFactory);
        factory.setAcknowledgeMode(AcknowledgeMode.AUTO);
        factory.setDefaultRequeueRejected(false);
        factory.setMessageConverter(converter); // important!
        // factory.setBatchListener(true);
        return factory;
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

    @Bean
    public JacksonJsonMessageConverter converter(JsonMapper mapper) {
        return new JacksonJsonMessageConverter(mapper);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(JacksonJsonMessageConverter converter) {
        RabbitTemplate template = new RabbitTemplate(cachingConnectionFactory);

        // mapper.setDate;
        template.setMessageConverter(converter);
        return template;
    }
}
