package bw.co.knowvera.processing;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.test.util.ReflectionTestUtils;

class ProcessingQueueConfigTest {

    private static SimpleRabbitListenerContainerFactory factory(boolean worker) {
        ProcessingProperties properties = new ProcessingProperties();
        properties.getWorker().setEnabled(worker);
        return new ProcessingQueueConfig().processingJobContainerFactory(mock(ConnectionFactory.class),
                mock(JacksonJsonMessageConverter.class), properties);
    }

    @Test
    void theJobConsumersStartOnlyOnWorkerInstances() {
        assertThat(ReflectionTestUtils.getField(factory(true), "autoStartup")).isEqualTo(true);
        assertThat(ReflectionTestUtils.getField(factory(false), "autoStartup")).isEqualTo(false);
    }

    @Test
    void eachConsumerTakesOneJobAtATime() {
        assertThat(ReflectionTestUtils.getField(factory(true), "prefetchCount")).isEqualTo(1);
        assertThat(ReflectionTestUtils.getField(factory(true), "defaultRequeueRejected")).isEqualTo(false);
    }

    @Test
    void theWorkerIsOnUnlessTurnedOff() {
        assertThat(new ProcessingProperties().getWorker().isEnabled()).isTrue();
    }
}
