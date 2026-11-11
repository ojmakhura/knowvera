package bw.co.roguesystems.ai.messaging;

import org.springframework.amqp.core.Declarables;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import tools.jackson.databind.json.JsonMapper;

/**
 * The job and result queues (callers declare the same ones, so either side can start first) and
 * JSON messages. Messages that cannot be handled go to the dead-letter queues.
 */
@Configuration
public class MessagingConfig {

    @Bean
    public Declarables veritasQueues() {
        return VeritasTopology.declarables();
    }

    /** Used by the RabbitTemplate and the listeners Spring Boot configures. */
    @Bean
    public JacksonJsonMessageConverter messageConverter(JsonMapper jsonMapper) {
        JacksonJsonMessageConverter converter = new JacksonJsonMessageConverter(jsonMapper);
        converter.setCreateMessageIds(true);
        return converter;
    }
}
