package bw.co.centralkyc.document.processor;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import bw.co.centralkyc.document.DocumentDTO;
import bw.co.centralkyc.document.DocumentService;
import bw.co.centralkyc.extractor.LmStudioExtractor;
import bw.co.centralkyc.lmstudio.CompletionRequest;
import bw.co.centralkyc.lmstudio.CompletionRequestMessage;
import bw.co.centralkyc.lmstudio.CompletionResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import tools.jackson.databind.json.JsonMapper;
import org.springframework.ai.chat.client.ChatClient;

@Service
@Slf4j
@RequiredArgsConstructor
public class TextProcessingService {

    private final DocumentService documentService;
    private final LmStudioExtractor lmStudioExtractor;
    private final ChatClient chatClient;
    private final JsonMapper jsonMapper;

    @RabbitListener(queues = "${app.rabbitmq.textProcessingQueue}")
    public void processExtractedText(String documentId) {
        log.info("Processing extracted text for document ID: {}", documentId);
        // Implement your text processing logic here
        // For example, you could perform keyword extraction, sentiment analysis, etc.

        try {
            DocumentDTO document = documentService.findById(documentId); // Replace with actual retrieval logic

            if (document == null) {
                log.warn("Document not found for ID: {}", documentId);
                return;
            }

            String extractedText = document.getFileContent(); // Assuming this contains the extracted text

            if(StringUtils.isBlank(extractedText)) {
                log.warn("Extracted text is empty for document ID: {}", documentId);
                return;
            }

            // Call LmStudioExtractor to process the extracted text
            CompletionRequest request = new CompletionRequest();
            request.setModel("local-model"); // Specify the model you want to use
            request.setStream(false);
            
            CompletionRequestMessage message = new CompletionRequestMessage();
            message.setRole("user");

            StringBuilder contentBuilder = new StringBuilder();
            contentBuilder.append("In the following text: ")
                          .append(extractedText)
                          .append(", extract a json object containing the following fields: ")
                          .append(jsonMapper.writeValueAsString(document.getExpectedInformation()));

            message.setContent(contentBuilder.toString());
            request.setMessages(List.of(message));

            CompletionResponse response = lmStudioExtractor.createChatCompletion(request);

            response.getChoices().forEach(choice -> {
                log.info("Received response from LmStudioExtractor: {}", choice.getMessage().getContent());
                // Here you can implement logic to update the document with the extracted information
                // For example, you could parse the JSON response and update the document in the database
                if(choice.getMessage() != null && StringUtils.isNotBlank(choice.getMessage().getContent())) {
                    try {
                        // Assuming the response content is a JSON string representing the extracted information
                        Map extractedInfo = jsonMapper.readValue(choice.getMessage().getContent(), Map.class);
                        document.setExtractedInformation(extractedInfo);
                        documentService.save(document);
                    } catch (Exception e) {
                        log.error("Failed to parse LmStudioExtractor response for document ID: {}", documentId, e);
                    }
                } else {
                    log.warn("LmStudioExtractor response is empty for document ID: {}", documentId);

                }
            });

            log.info("Completed text processing for document ID: {}", documentId);
        } catch (Exception e) {
            log.error("Text processing interrupted for document ID: {}", documentId, e);
        }
    }

}
