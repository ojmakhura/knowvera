package bw.co.knowvera.processing;

import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Declarables;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import bw.co.knowvera.processing.messaging.ProcessingTopology;

/**
 * The processing queues (jobs and results, each with its dead-letter queue), and the listener
 * container of the jobs.
 * <p>
 * Jobs are long (OCR, model calls), so each consumer takes one job at a time (prefetch 1), and the
 * consumers only start on instances whose worker is enabled (app.processing.worker.enabled): the
 * others queue jobs and apply results but leave the processing to the workers.
 */
@Configuration
@EnableConfigurationProperties(ProcessingProperties.class)
public class ProcessingQueueConfig {

    public static final String JOB_CONTAINER_FACTORY = "processingJobContainerFactory";

    @Bean
    public Declarables processingQueues() {
        return ProcessingTopology.declarables();
    }

    @Bean(JOB_CONTAINER_FACTORY)
    public SimpleRabbitListenerContainerFactory processingJobContainerFactory(ConnectionFactory connectionFactory,
            JacksonJsonMessageConverter converter, ProcessingProperties properties) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(converter);
        factory.setAcknowledgeMode(AcknowledgeMode.AUTO);
        factory.setDefaultRequeueRejected(false);
        factory.setPrefetchCount(1);
        factory.setObservationEnabled(true);
        factory.setAutoStartup(properties.getWorker().isEnabled());
        return factory;
    }
}
