package bw.co.centralkyc.document.processor;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import bw.co.centralkyc.QueueObject;
import bw.co.centralkyc.document.DocumentDTO;
import bw.co.centralkyc.document.DocumentService;
import bw.co.centralkyc.extractor.LmStudioExtractor;
import bw.co.centralkyc.lmstudio.CompletionRequest;
import bw.co.centralkyc.lmstudio.CompletionRequestMessage;
import bw.co.centralkyc.lmstudio.CompletionResponse;
import bw.co.centralkyc.properties.RabbitProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import tools.jackson.databind.json.JsonMapper;

@Service
@Slf4j
@RequiredArgsConstructor
public class TextProcessingService {

    private final DocumentService documentService;
    private final LmStudioExtractor lmStudioExtractor;
    private final RabbitTemplate rabbitTemplate;
    private final RabbitProperties rabbitProperties;
    private final JsonMapper jsonMapper;

    private final String initialPrompt = """
                Extract all identity information from the text and return it strictly in JSON format.

                Instructions:
                1. Only return a single valid JSON object. Do NOT add any explanations, notes, or extra fields.
                2. Use the JSON keys below as the exact structure and only populate these keys.
                3. Use ISO 8601 date format (YYYY-MM-DD) for any date fields.
                4. If the information is missing or unclear in the text, set the value to null.
                5. Do NOT infer or guess data that is not explicitly present.

                JSON structure to populate:
            """;

    @RabbitListener(queues = "${app.rabbitmq.textProcessingQueue}")
    public void processExtractedText(QueueObject queueObject) {
        log.info("Processing extracted text for document ID: {}", queueObject.documentId());
        try {
            DocumentDTO document = documentService.findById(queueObject.documentId()); // Replace with actual retrieval logic

            if (document == null) {
                log.warn("Document not found for ID: {}", queueObject.documentId());
                return;
            }

            String extractedText = document.getFileContent(); // Assuming this contains the extracted text

            if (StringUtils.isBlank(extractedText)) {
                log.warn("Extracted text is empty for document ID: {}", queueObject.documentId());
                return;
            }

            // Call LmStudioExtractor to process the extracted text
            CompletionRequest request = new CompletionRequest();
            request.setModel("local-model"); // Specify the model you want to use
            request.setStream(false);

            CompletionRequestMessage system = new CompletionRequestMessage();
            system.setRole("system");
            system.setContent(
                    "You are a data extraction assistant. You **MUST ONLY output valid JSON**. Do not include explanations, notes, reasoning, or any extra text. Follow the instructions carefully.");

            CompletionRequestMessage message = new CompletionRequestMessage();
            message.setRole("user");

            StringBuilder contentBuilder = new StringBuilder();
            contentBuilder.append(initialPrompt)
                    .append('\n')
                    .append(jsonMapper.writeValueAsString(document.getExpectedInformation()))
                    .append('\n')
                    .append("Text to process: ")
                    .append(extractedText);

            message.setContent(contentBuilder.toString());
            System.out.println(contentBuilder.toString());
            request.setMessages(List.of(system, message));

            CompletionResponse response = lmStudioExtractor.createChatCompletion(request);

            response.getChoices().forEach(choice -> {
                log.info("Received response from LmStudioExtractor: {}",
                        choice.getMessage().getContent());
                // Here you can implement logic to update the document with the extracted
                if (StringUtils.isNotBlank(choice.getMessage().getContent())) {
                    try {
                        // Assuming the response content is a JSON string representing the extracted

                        Map extractedInfo = parseLmStudioResponse(choice.getMessage().getContent());
                        document.setExtractedInformation(extractedInfo);
                        documentService.save(document);

                        // Send this to the next queue for further processing
                        rabbitTemplate.convertAndSend(
                                rabbitProperties.getExtractedInformationDispatchExchange(),
                                rabbitProperties.getExtractedInformationDispatchRoutingKey(),
                                queueObject);
                    } catch (Exception e) {
                        log.error("Failed to parse LmStudioExtractor response for document ID: {}",
                                queueObject.documentId(), e);
                    }
                } else {
                    log.warn("LmStudioExtractor response is empty for document ID: {}",
                            queueObject.documentId());

                }
            });

            log.info("Completed text processing for document ID: {}", queueObject.documentId());
        } catch (Exception e) {
            log.error("Text processing interrupted for document ID: {}", queueObject.documentId(), e);
        }
    }

    private Map<String, Object> parseLmStudioResponse(String responseContent) {
        try {
            Pattern pattern = Pattern.compile("\\{.*\\}", Pattern.DOTALL);
            Matcher matcher = pattern.matcher(responseContent);

            if (matcher.find()) {
                String jsonString = matcher.group();
                System.out.println("Extracted JSON String:\n" + jsonString);

                // Optional: parse into a Map
                Map<String, Object> jsonMap = jsonMapper.readValue(jsonString, Map.class);
                System.out.println("\nParsed JSON Map:\n" + jsonMap);

                return jsonMap;
            } else {
                System.out.println("No JSON found in the response.");
                return Map.of();
            }
        } catch (Exception e) {
            log.error("Failed to parse LmStudio response content", e);
            return Map.of(); // Return an empty map on parsing failure
        }
    }
}
