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

//     @Value("${app.rabbitmq.newRenewalApplicationQueue}")
//     private String newRenewalApplicationQueue;

//     @Value("${app.rabbitmq.newRenewalApplicationExchange}")
//     private String newRenewalApplicationExchange;

//     @Value("${app.rabbitmq.newRenewalApplicationRoutingKey}")
//     private String newRenewalApplicationRoutingKey;

//     @Bean
//     public Declarables renewalApplicationQueueSchema() {
//         DirectExchange renewalApplicationQueueExchange = new DirectExchange(
//                 newRenewalApplicationExchange);
//         Queue renewalApplicationQueue = QueueBuilder.durable(newRenewalApplicationQueue).build();

//         return new Declarables(
//                 renewalApplicationQueueExchange,
//                 renewalApplicationQueue,
//                 BindingBuilder.bind(renewalApplicationQueue).to(renewalApplicationQueueExchange)
//                         .with(newRenewalApplicationRoutingKey));
//     }

//     @Value("${app.rabbitmq.newPractitionerQueue}")
//     private String newPractitionerQueue;

//     @Value("${app.rabbitmq.newPractitionerExchange}")
//     private String newPractitionerExchange;

//     @Value("${app.rabbitmq.newPractitionerRoutingKey}")
//     private String newPractitionerRoutingKey;

//     @Bean
//     public Declarables practitionerQueueSchema() {
//         DirectExchange practitionerQueueExchange = new DirectExchange(
//                 newPractitionerExchange);
//         Queue practitionerQueue = QueueBuilder.durable(newPractitionerQueue).build();

//         return new Declarables(
//                 practitionerQueueExchange,
//                 practitionerQueue,
//                 BindingBuilder.bind(practitionerQueue).to(practitionerQueueExchange)
//                         .with(newPractitionerRoutingKey));
//     }

//     @Value("${app.rabbitmq.practitionerLicenceToExpireQueue}")
//     private String practitionerLicenceToExpireQueue;

//     @Value("${app.rabbitmq.practitionerLicenceToExpireExchange}")
//     private String practitionerLicenceToExpireExchange;

//     @Value("${app.rabbitmq.practitionerLicenceToExpireRoutingKey}")
//     private String practitionerLicenceToExpireRoutingKey;

//     @Bean
//     public Declarables practitionerLicenceToExpireQueueSchema() {
//         DirectExchange licenceToExpireQueueExchange = new DirectExchange(
//                 practitionerLicenceToExpireExchange);
//         Queue licenceToExpireQueue = QueueBuilder.durable(practitionerLicenceToExpireQueue).build();

//         return new Declarables(
//                 licenceToExpireQueueExchange,
//                 licenceToExpireQueue,
//                 BindingBuilder.bind(licenceToExpireQueue).to(licenceToExpireQueueExchange)
//                         .with(practitionerLicenceToExpireRoutingKey));
//     }

//     @Value("${app.rabbitmq.practitionerAccountExpiredQueue}")
//     private String practitionerAccountExpiredQueue;

//     @Value("${app.rabbitmq.practitionerAccountExpiredExchange}")
//     private String practitionerAccountExpiredExchange;

//     @Value("${app.rabbitmq.practitionerAccountExpiredRoutingKey}")
//     private String practitionerAccountExpiredRoutingKey;

//     @Bean
//     public Declarables practitionerAccountExpiredQueueSchema() {
//         DirectExchange accountExpiredQueueExchange = new DirectExchange(
//                 practitionerAccountExpiredExchange);
//         Queue accountExpiredQueue = QueueBuilder.durable(practitionerAccountExpiredQueue).build();

//         return new Declarables(
//                 accountExpiredQueueExchange,
//                 accountExpiredQueue,
//                 BindingBuilder.bind(accountExpiredQueue).to(accountExpiredQueueExchange)
//                         .with(practitionerAccountExpiredRoutingKey));
//     }

//     @Value("${app.rabbitmq.applicationSubmissionQueue}")
//     private String applicationSubmissionQueue;

//     @Value("${app.rabbitmq.applicationSubmissionExchange}")
//     private String applicationSubmissionExchange;

//     @Value("${app.rabbitmq.applicationSubmissionRoutingKey}")
//     private String applicationSubmissionRoutingKey;

//     @Bean
//     public Declarables applicationSubmissionQueueSchema() {
//         DirectExchange submissionQueueExchange = new DirectExchange(
//                 applicationSubmissionExchange);
//         Queue submissionQueue = QueueBuilder.durable(applicationSubmissionQueue).build();

//         return new Declarables(
//                 submissionQueueExchange,
//                 submissionQueue,
//                 BindingBuilder.bind(submissionQueue).to(submissionQueueExchange)
//                         .with(applicationSubmissionRoutingKey));
//     }

//     @Value("${app.rabbitmq.applicationVerifiedQueue}")
//     private String applicationVerifiedQueue;

//     @Value("${app.rabbitmq.applicationVerifiedExchange}")
//     private String applicationVerifiedExchange;

//     @Value("${app.rabbitmq.applicationVerifiedRoutingKey}")
//     private String applicationVerifiedRoutingKey;

//     @Bean
//     public Declarables applicationVerifiedQueueSchema() {
//         DirectExchange verifiedQueueExchange = new DirectExchange(
//                 applicationVerifiedExchange);
//         Queue verifiedQueue = QueueBuilder.durable(applicationVerifiedQueue).build();

//         return new Declarables(
//                 verifiedQueueExchange,
//                 verifiedQueue,
//                 BindingBuilder.bind(verifiedQueue).to(verifiedQueueExchange)
//                         .with(applicationVerifiedRoutingKey));
//     }
}
