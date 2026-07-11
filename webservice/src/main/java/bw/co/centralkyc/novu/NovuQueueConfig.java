package bw.co.centralkyc.novu;

import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Declarables;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NovuQueueConfig {

    @Value("${app.novu.queue.newUserQueueExchange}")
    private String newUserQueueExchange;

    @Value("${app.novu.queue.newUserQueue}")
    private String newUserQueue;

    @Value("${app.novu.queue.newUserQueueRoutingKey}")
    private String newUserQueueRoutingKey;

    @Value("${app.novu.queue.newOrgUserQueueExchange}")
    private String newOrgUserQueueExchange;

    @Value("${app.novu.queue.newOrgUserQueue}")
    private String newOrgUserQueue;

    @Value("${app.novu.queue.newOrgUserQueueRoutingKey}")
    private String newOrgUserQueueRoutingKey;

    @Value("${app.novu.queue.newOrgClientRequestQueueExchange}")
    private String newOrgClientRequestQueueExchange;

    @Value("${app.novu.queue.newOrgClientRequestQueue}")
    private String newOrgClientRequestQueue;

    @Value("${app.novu.queue.newOrgClientRequestQueueRoutingKey}")
    private String newOrgClientRequestQueueRoutingKey;

    @Value("${app.novu.queue.newKycRecordQueueExchange}")
    private String newKycRecordQueueExchange;

    @Value("${app.novu.queue.newKycRecordQueue}")
    private String newKycRecordQueue;

    @Value("${app.novu.queue.newKycRecordQueueRoutingKey}")
    private String newKycRecordQueueRoutingKey;

    @Bean
    public Declarables newUserQueueSchema() {
        DirectExchange newUserExchange = new DirectExchange(newUserQueueExchange);
        Queue newUserQ = QueueBuilder.durable(newUserQueue).build();

        return new Declarables(
                newUserExchange,
                newUserQ,
                BindingBuilder.bind(newUserQ).to(newUserExchange).with(newUserQueueRoutingKey));
    }

    @Bean
    public Declarables newOrgUserQueueSchema() {
        DirectExchange newOrgUserExchange = new DirectExchange(newOrgUserQueueExchange);
        Queue newOrgUserQ = QueueBuilder.durable(newOrgUserQueue).build();

        return new Declarables(
                newOrgUserExchange,
                newOrgUserQ,
                BindingBuilder.bind(newOrgUserQ).to(newOrgUserExchange).with(newOrgUserQueueRoutingKey));
    }

    @Bean
    public Declarables newOrgClientRequestQueueSchema() {
        DirectExchange newOrgClientRequestExchange = new DirectExchange(newOrgClientRequestQueueExchange);
        Queue newOrgClientRequestQ = QueueBuilder.durable(newOrgClientRequestQueue).build();

        return new Declarables(
                newOrgClientRequestExchange,
                newOrgClientRequestQ,
                BindingBuilder.bind(newOrgClientRequestQ).to(newOrgClientRequestExchange).with(newOrgClientRequestQueueRoutingKey));
    }

    @Bean
    public Declarables newKycRecordQueueSchema() {
        DirectExchange newKycRecordExchange = new DirectExchange(newKycRecordQueueExchange);
        Queue newKycRecordQ = QueueBuilder.durable(newKycRecordQueue).build();
        return new Declarables(
                newKycRecordExchange,
                newKycRecordQ,
                BindingBuilder.bind(newKycRecordQ).to(newKycRecordExchange).with(newKycRecordQueueRoutingKey));
    }
}
