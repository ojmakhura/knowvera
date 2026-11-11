package bw.co.knowvera.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * RabbitMQ connection and the queues of the document stages that follow veritas-processor's
 * results: information confirmation, and the verification of KYC records, organisations and
 * individuals (prefix app.rabbitmq).
 */
@ConfigurationProperties(prefix = "app.rabbitmq")
public class RabbitProperties {

    private final String host;
    private final int port;
    private final String username;
    private final String password;

    private final String informationConfirmationQueueExchange;
    private final String informationConfirmationQueue;
    private final String informationConfirmationQueueRoutingKey;

    private final String kycVerificationQueueExchange;
    private final String kycVerificationQueue;
    private final String kycVerificationQueueRoutingKey;

    private final String organisationVerificationQueueExchange;
    private final String organisationVerificationQueue;
    private final String organisationVerificationQueueRoutingKey;

    private final String individualVerificationQueueExchange;
    private final String individualVerificationQueue;
    private final String individualVerificationQueueRoutingKey;

    public RabbitProperties(
            String host,
            int port,
            String username,
            String password,
            String informationConfirmationQueueExchange,
            String informationConfirmationQueue,
            String informationConfirmationQueueRoutingKey,
            String kycVerificationQueueExchange,
            String kycVerificationQueue,
            String kycVerificationQueueRoutingKey,
            String organisationVerificationQueueExchange,
            String organisationVerificationQueue,
            String organisationVerificationQueueRoutingKey,
            String individualVerificationQueueExchange,
            String individualVerificationQueue,
            String individualVerificationQueueRoutingKey) {
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
        this.informationConfirmationQueueExchange = informationConfirmationQueueExchange;
        this.informationConfirmationQueue = informationConfirmationQueue;
        this.informationConfirmationQueueRoutingKey = informationConfirmationQueueRoutingKey;
        this.kycVerificationQueueExchange = kycVerificationQueueExchange;
        this.kycVerificationQueue = kycVerificationQueue;
        this.kycVerificationQueueRoutingKey = kycVerificationQueueRoutingKey;
        this.organisationVerificationQueueExchange = organisationVerificationQueueExchange;
        this.organisationVerificationQueue = organisationVerificationQueue;
        this.organisationVerificationQueueRoutingKey = organisationVerificationQueueRoutingKey;
        this.individualVerificationQueueExchange = individualVerificationQueueExchange;
        this.individualVerificationQueue = individualVerificationQueue;
        this.individualVerificationQueueRoutingKey = individualVerificationQueueRoutingKey;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getInformationConfirmationQueueExchange() {
        return informationConfirmationQueueExchange;
    }

    public String getInformationConfirmationQueue() {
        return informationConfirmationQueue;
    }

    public String getInformationConfirmationQueueRoutingKey() {
        return informationConfirmationQueueRoutingKey;
    }

    public String getKycVerificationQueueExchange() {
        return kycVerificationQueueExchange;
    }

    public String getKycVerificationQueue() {
        return kycVerificationQueue;
    }

    public String getKycVerificationQueueRoutingKey() {
        return kycVerificationQueueRoutingKey;
    }

    public String getOrganisationVerificationQueueExchange() {
        return organisationVerificationQueueExchange;
    }

    public String getOrganisationVerificationQueue() {
        return organisationVerificationQueue;
    }

    public String getOrganisationVerificationQueueRoutingKey() {
        return organisationVerificationQueueRoutingKey;
    }

    public String getIndividualVerificationQueueExchange() {
        return individualVerificationQueueExchange;
    }

    public String getIndividualVerificationQueue() {
        return individualVerificationQueue;
    }

    public String getIndividualVerificationQueueRoutingKey() {
        return individualVerificationQueueRoutingKey;
    }
}
