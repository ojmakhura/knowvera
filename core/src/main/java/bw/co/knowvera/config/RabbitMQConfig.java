package bw.co.knowvera.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import tools.jackson.databind.json.JsonMapper;

import java.util.concurrent.Executors;

@Configuration
public class RabbitMQConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMQConfig.class);
    private final CachingConnectionFactory cachingConnectionFactory;

    public RabbitMQConfig(CachingConnectionFactory cachingConnectionFactory) {
        this.cachingConnectionFactory = cachingConnectionFactory;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            JacksonJsonMessageConverter converter) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(cachingConnectionFactory);
        factory.setAcknowledgeMode(AcknowledgeMode.AUTO);
        factory.setDefaultRequeueRejected(false);
        factory.setMessageConverter(converter); // important!
        factory.setTaskExecutor(Executors.newVirtualThreadPerTaskExecutor());
        // factory.setBatchListener(true);
        return factory;
    }

    @Bean
    public JacksonJsonMessageConverter converter(JsonMapper mapper) {
        JacksonJsonMessageConverter converter = new JacksonJsonMessageConverter(mapper);
        // Gives each message a stable id (listeners use it, e.g. as Novu's transactionId)
        converter.setCreateMessageIds(true);
        return converter;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(JacksonJsonMessageConverter converter) {
        RabbitTemplate template = new RabbitTemplate(cachingConnectionFactory);

        // Every publish is confirmed (spring.rabbitmq.publisher-confirm-type: correlated) and an
        // unroutable one comes back (mandatory + publisher-returns) instead of vanishing silently.
        // ConfirmedPublisher logs the failures of the messages it sends; these callbacks cover the rest.
        template.setMandatory(true);
        template.setReturnsCallback(returned -> LOGGER.error(
                "RabbitMQ could not route message {} to exchange '{}' with routing key '{}': {} {}",
                returned.getMessage().getMessageProperties().getMessageId(), returned.getExchange(),
                returned.getRoutingKey(), returned.getReplyCode(), returned.getReplyText()));
        template.setConfirmCallback((correlation, ack, cause) -> {
            if (!ack && correlation == null) {
                LOGGER.error("RabbitMQ did not accept a published message: {}", cause);
            }
        });

        // mapper.setDate;
        template.setMessageConverter(converter);
        return template;
    }
}
