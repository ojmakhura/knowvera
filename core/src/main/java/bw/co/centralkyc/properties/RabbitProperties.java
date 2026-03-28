package bw.co.centralkyc.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.AllArgsConstructor;

@ConfigurationProperties(prefix = "app.rabbitmq")
@AllArgsConstructor
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
}
