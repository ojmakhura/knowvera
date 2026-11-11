package bw.co.knowvera.veritas;

import org.springframework.amqp.core.Declarables;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import bw.co.roguesystems.ai.messaging.VeritasTopology;

/**
 * veritas-processor's job and result queues, declared as the processor declares them so either
 * side can start first.
 */
@Configuration
public class VeritasQueueConfig {

    @Bean
    public Declarables veritasQueues() {
        return VeritasTopology.declarables();
    }
}
