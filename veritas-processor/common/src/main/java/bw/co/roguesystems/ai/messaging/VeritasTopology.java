package bw.co.roguesystems.ai.messaging;

import static bw.co.roguesystems.ai.messaging.VeritasMessaging.DEAD_LETTER_EXCHANGE;
import static bw.co.roguesystems.ai.messaging.VeritasMessaging.EXCHANGE;
import static bw.co.roguesystems.ai.messaging.VeritasMessaging.JOBS_DEAD_LETTER_QUEUE;
import static bw.co.roguesystems.ai.messaging.VeritasMessaging.JOBS_QUEUE;
import static bw.co.roguesystems.ai.messaging.VeritasMessaging.JOBS_ROUTING_KEY;
import static bw.co.roguesystems.ai.messaging.VeritasMessaging.RESULTS_DEAD_LETTER_QUEUE;
import static bw.co.roguesystems.ai.messaging.VeritasMessaging.RESULTS_QUEUE;
import static bw.co.roguesystems.ai.messaging.VeritasMessaging.RESULTS_ROUTING_KEY;

import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Declarables;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;

/**
 * The exchanges and queues of the job and result exchange. The processor and its callers declare
 * the same objects (RabbitMQ refuses a redeclaration with other arguments), so either can start
 * first: expose {@link #declarables()} as a bean. Needs spring-amqp on the classpath.
 */
public final class VeritasTopology {

    private VeritasTopology() {
    }

    public static Declarables declarables() {
        DirectExchange exchange = new DirectExchange(EXCHANGE, true, false);
        DirectExchange deadLetterExchange = new DirectExchange(DEAD_LETTER_EXCHANGE, true, false);
        Queue jobs = QueueBuilder.durable(JOBS_QUEUE).deadLetterExchange(DEAD_LETTER_EXCHANGE)
                .deadLetterRoutingKey(JOBS_ROUTING_KEY).build();
        Queue results = QueueBuilder.durable(RESULTS_QUEUE).deadLetterExchange(DEAD_LETTER_EXCHANGE)
                .deadLetterRoutingKey(RESULTS_ROUTING_KEY).build();
        Queue jobsDeadLetter = QueueBuilder.durable(JOBS_DEAD_LETTER_QUEUE).build();
        Queue resultsDeadLetter = QueueBuilder.durable(RESULTS_DEAD_LETTER_QUEUE).build();
        return new Declarables(exchange, deadLetterExchange, jobs, results, jobsDeadLetter, resultsDeadLetter,
                BindingBuilder.bind(jobs).to(exchange).with(JOBS_ROUTING_KEY),
                BindingBuilder.bind(results).to(exchange).with(RESULTS_ROUTING_KEY),
                BindingBuilder.bind(jobsDeadLetter).to(deadLetterExchange).with(JOBS_ROUTING_KEY),
                BindingBuilder.bind(resultsDeadLetter).to(deadLetterExchange).with(RESULTS_ROUTING_KEY));
    }
}
