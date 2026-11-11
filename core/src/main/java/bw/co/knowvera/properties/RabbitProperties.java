package bw.co.knowvera.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.rabbitmq")
public class RabbitProperties {

    private final String host;
    private final int port;
    private final String username;
    private final String password;
    private final String documentHandlerDeadLetterExchange;
    private final String documentHandlerDeadLetterQueue;
    private final String documentHandlerDeadLetterRoutingKey;

    private final String textExtractionQueueExchange;
    private final String textExtractionQueue;
    private final String textExtractionQueueRoutingKey;

    private final String geminiTextExtractionQueueExchange;
    private final String geminiTextExtractionQueue;
    private final String geminiTextExtractionQueueRoutingKey;

    private final String documentConfirmationQueueExchange;
    private final String documentConfirmationQueue;
    private final String documentConfirmationQueueRoutingKey;

    private final String textProcessingQueueExchange;
    private final String textProcessingQueue;
    private final String textProcessingQueueRoutingKey;

    private final String informationConfirmationQueueExchange;
    private final String informationConfirmationQueue;
    private final String informationConfirmationQueueRoutingKey;

    private final String textCleanupQueueExchange;
    private final String textCleanupQueue;
    private final String textCleanupQueueRoutingKey;

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
            String documentHandlerDeadLetterExchange,
            String documentHandlerDeadLetterQueue,
            String documentHandlerDeadLetterRoutingKey,
            String textExtractionQueueExchange,
            String textExtractionQueue,
            String textExtractionQueueRoutingKey,
            String geminiTextExtractionQueueExchange,
            String geminiTextExtractionQueue,
            String geminiTextExtractionQueueRoutingKey,
            String documentConfirmationQueueExchange,
            String documentConfirmationQueue,
            String documentConfirmationQueueRoutingKey,
            String textProcessingQueueExchange,
            String textProcessingQueue,
            String textProcessingQueueRoutingKey,
            String informationConfirmationQueueExchange,
            String informationConfirmationQueue,
            String informationConfirmationQueueRoutingKey,
            String textCleanupQueueExchange,
            String textCleanupQueue,
            String textCleanupQueueRoutingKey,
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
        this.documentHandlerDeadLetterExchange = documentHandlerDeadLetterExchange;
        this.documentHandlerDeadLetterQueue = documentHandlerDeadLetterQueue;
        this.documentHandlerDeadLetterRoutingKey = documentHandlerDeadLetterRoutingKey;
        this.textExtractionQueueExchange = textExtractionQueueExchange;
        this.textExtractionQueue = textExtractionQueue;
        this.textExtractionQueueRoutingKey = textExtractionQueueRoutingKey;
        this.geminiTextExtractionQueueExchange = geminiTextExtractionQueueExchange;
        this.geminiTextExtractionQueue = geminiTextExtractionQueue;
        this.geminiTextExtractionQueueRoutingKey = geminiTextExtractionQueueRoutingKey;
        this.documentConfirmationQueueExchange = documentConfirmationQueueExchange;
        this.documentConfirmationQueue = documentConfirmationQueue;
        this.documentConfirmationQueueRoutingKey = documentConfirmationQueueRoutingKey;
        this.textProcessingQueueExchange = textProcessingQueueExchange;
        this.textProcessingQueue = textProcessingQueue;
        this.textProcessingQueueRoutingKey = textProcessingQueueRoutingKey;
        this.informationConfirmationQueueExchange = informationConfirmationQueueExchange;
        this.informationConfirmationQueue = informationConfirmationQueue;
        this.informationConfirmationQueueRoutingKey = informationConfirmationQueueRoutingKey;
        this.textCleanupQueueExchange = textCleanupQueueExchange;
        this.textCleanupQueue = textCleanupQueue;
        this.textCleanupQueueRoutingKey = textCleanupQueueRoutingKey;
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

    public String getDocumentHandlerDeadLetterExchange() {
        return documentHandlerDeadLetterExchange;
    }

    public String getDocumentHandlerDeadLetterQueue() {
        return documentHandlerDeadLetterQueue;
    }

    public String getDocumentHandlerDeadLetterRoutingKey() {
        return documentHandlerDeadLetterRoutingKey;
    }

    public String getTextExtractionQueueExchange() {
        return textExtractionQueueExchange;
    }

    public String getTextExtractionQueue() {
        return textExtractionQueue;
    }

    public String getTextExtractionQueueRoutingKey() {
        return textExtractionQueueRoutingKey;
    }

    public String getGeminiTextExtractionQueueExchange() {
        return geminiTextExtractionQueueExchange;
    }

    public String getGeminiTextExtractionQueue() {
        return geminiTextExtractionQueue;
    }

    public String getGeminiTextExtractionQueueRoutingKey() {
        return geminiTextExtractionQueueRoutingKey;
    }

    public String getDocumentConfirmationQueueExchange() {
        return documentConfirmationQueueExchange;
    }

    public String getDocumentConfirmationQueue() {
        return documentConfirmationQueue;
    }

    public String getDocumentConfirmationQueueRoutingKey() {
        return documentConfirmationQueueRoutingKey;
    }

    public String getTextProcessingQueueExchange() {
        return textProcessingQueueExchange;
    }

    public String getTextProcessingQueue() {
        return textProcessingQueue;
    }

    public String getTextProcessingQueueRoutingKey() {
        return textProcessingQueueRoutingKey;
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

    public String getTextCleanupQueueExchange() {
        return textCleanupQueueExchange;
    }

    public String getTextCleanupQueue() {
        return textCleanupQueue;
    }

    public String getTextCleanupQueueRoutingKey() {
        return textCleanupQueueRoutingKey;
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
