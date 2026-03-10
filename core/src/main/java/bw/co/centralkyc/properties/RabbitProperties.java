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
    // Document properties
    private final String documentHandler;
    private final String documentDispatchExchange;
    private final String documentDispatchQueue;
    private final String documentDispatchRoutingKey;
    private final String documentHandlerDeadLetterExchange;
    private final String documentHandlerDeadLetterQueue;
    private final String documentHandlerDeadLetterRoutingKey;
    private final String documentQueueExchange;
    private final String documentQueue;
    private final String documentQueueRoutingKey;
    private final String textProcessingHandler;
    private final String textProcessingDispatchExchange;
    private final String textProcessingDispatchQueue;
    private final String textProcessingDispatchRoutingKey;
    private final String textProcessingHandlerDeadLetterExchange;
    private final String textProcessingHandlerDeadLetterQueue;
    private final String textProcessingHandlerDeadLetterRoutingKey;
    private final String textProcessingQueueExchange;
    private final String textProcessingQueue;
    private final String textProcessingQueueRoutingKey;
    private final String extractedInformationHandler;
    private final String extractedInformationDispatchExchange;
    private final String extractedInformationDispatchQueue;
    private final String extractedInformationDispatchRoutingKey;
    private final String extractedInformationHandlerDeadLetterExchange;
    private final String extractedInformationHandlerDeadLetterQueue;
    private final String extractedInformationHandlerDeadLetterRoutingKey;
    private final String extractedInformationQueueExchange;
    private final String extractedInformationQueue;
    private final String extractedInformationQueueRoutingKey;
    

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

    public String getDocumentDispatchQueue() {
        return documentDispatchQueue;
    }

    public String getDocumentDispatchRoutingKey() {
        return documentDispatchRoutingKey;
    }

    public String getDocumentQueue() {
        return documentQueue;
    }

    public String getDocumentQueueRoutingKey() {
        return documentQueueRoutingKey;
    }

    public String getDocumentDispatchExchange() {
        return documentDispatchExchange;
    }

    public String getDocumentQueueExchange() {
        return documentQueueExchange;
    }

    public String getDocumentHandler() {
        return documentHandler;
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

    public String getTextProcessingHandler() {
        return textProcessingHandler;
    }

    public String getTextProcessingDispatchExchange() {
        return textProcessingDispatchExchange;
    }

    public String getTextProcessingDispatchQueue() {
        return textProcessingDispatchQueue;
    }

    public String getTextProcessingDispatchRoutingKey() {
        return textProcessingDispatchRoutingKey;
    }

    public String getTextProcessingHandlerDeadLetterExchange() {
        return textProcessingHandlerDeadLetterExchange;
    }

    public String getTextProcessingHandlerDeadLetterQueue() {
        return textProcessingHandlerDeadLetterQueue;
    }

    public String getTextProcessingHandlerDeadLetterRoutingKey() {
        return textProcessingHandlerDeadLetterRoutingKey;
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

    public String getExtractedInformationHandler() {
        return extractedInformationHandler;
    }

    public String getExtractedInformationDispatchExchange() {
        return extractedInformationDispatchExchange;
    }

    public String getExtractedInformationDispatchQueue() {
        return extractedInformationDispatchQueue;
    }

    public String getExtractedInformationDispatchRoutingKey() {
        return extractedInformationDispatchRoutingKey;
    }

    public String getExtractedInformationHandlerDeadLetterExchange() {
        return extractedInformationHandlerDeadLetterExchange;
    }

    public String getExtractedInformationHandlerDeadLetterQueue() {
        return extractedInformationHandlerDeadLetterQueue;
    }

    public String getExtractedInformationHandlerDeadLetterRoutingKey() {
        return extractedInformationHandlerDeadLetterRoutingKey;
    }

    public String getExtractedInformationQueueExchange() {
        return extractedInformationQueueExchange;
    }

    public String getExtractedInformationQueue() {
        return extractedInformationQueue;
    }

    public String getExtractedInformationQueueRoutingKey() {
        return extractedInformationQueueRoutingKey;
    }
}
