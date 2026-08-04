package bw.co.kyvera.config;

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
