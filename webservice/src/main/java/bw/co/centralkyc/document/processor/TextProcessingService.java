package bw.co.centralkyc.document.processor;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import bw.co.centralkyc.QueueObject;
import bw.co.centralkyc.document.DocumentDTO;
import bw.co.centralkyc.document.DocumentService;
import bw.co.centralkyc.extractor.LmStudioExtractorService;
import bw.co.centralkyc.llm.Prompt;
import bw.co.centralkyc.llm.PromptMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import tools.jackson.databind.json.JsonMapper;

@Service
@Slf4j
@RequiredArgsConstructor
public class TextProcessingService {

    @Value("${app.llm.model}")
    private String llmModel;

    private final DocumentService documentService;
    private final LmStudioExtractorService lmStudioExtractorService;
    private final JsonMapper jsonMapper;
    private final DocumentProcessorService documentProcessorService;

    private final String initialPrompt = """
                Extract all required information from the text and return it strictly in JSON format.

                Instructions:
                1. Only return a single valid JSON object. Do NOT add any explanations, notes, or extra fields.
                2. Use the JSON keys below as the exact structure and only populate these keys.
                3. Use ISO 8601 date format (YYYY-MM-DD) for any date fields.
                4. If the information is missing or unclear in the text, set the value to null.
                5. Do NOT infer or guess data that is not explicitly present.

                JSON structure to populate:
            """;

    /**
     * Extracts data from the provided queue object. The method retrieves the
     * document based on the ID from the queue object, processes the extracted text
     * using LmStudioExtractor, and then handles the response to update the document
     * accordingly.
     * 
     * @param queueObject the queue object containing the document ID
     */
    @RabbitListener(queues = "${app.rabbitmq.textProcessingQueue}")
    public void extractData(QueueObject queueObject) {

        log.info("Processing extracted text for document ID: {}", queueObject.objectId());
        try {
            DocumentDTO document = documentService.findById(queueObject.objectId()); // Replace with actual retrieval
                                                                                       // logic

            if (document == null) {
                log.warn("Document not found for ID: {}", queueObject.objectId());
                return;
            }

            String extractedText = document.getFileContent(); // Assuming this contains the extracted text

            if (StringUtils.isBlank(extractedText)) {
                log.warn("Extracted text is empty for document ID: {}", queueObject.objectId());
                return;
            }

            // Call LmStudioExtractor to process the extracted text
            Prompt request = new Prompt();
            request.setModel(llmModel); // Specify the model you want to use
            request.setStream(false);

            PromptMessage system = new PromptMessage();
            system.setRole("system");
            system.setContent(
                    "You are a data extraction assistant. You **MUST ONLY output valid JSON**. Do not include explanations, notes, reasoning, or any extra text. Follow the instructions carefully.");

            PromptMessage message = new PromptMessage();
            message.setRole("user");

            StringBuilder contentBuilder = new StringBuilder();
            contentBuilder.append(initialPrompt)
                    .append('\n')
                    .append(jsonMapper.writeValueAsString(document.getExpectedFields()))
                    .append('\n')
                    .append("Text to process: ")
                    .append(extractedText);

            message.setContent(contentBuilder.toString());
            System.out.println(contentBuilder.toString());
            request.setMessages(List.of(system, message));

            lmStudioExtractorService.extractInformation(request)
                    .thenAccept(response -> {
                        System.out.println("✅ Got response");
                        documentProcessorService.processLmCompletionResponse(response, document);
                    })
                    .exceptionally(ex -> {
                        System.err.println("❌ ERROR:");
                        ex.printStackTrace();
                        return null;
                    });

            log.info("Completed text processing for document ID: {}", queueObject.objectId());
        } catch (Exception e) {
            log.error("Text processing interrupted for document ID: {}", queueObject.objectId(), e);
        }
    }

    private String getExtractionSystemPrompt(DocumentDTO document) {
        StringBuilder systemPromptBuilder = new StringBuilder();

        return systemPromptBuilder.toString();
    }

    private final String systemCleanUpPrompt = """
                You are a text normalization and cleaning engine. Your task is to clean and structure raw OCR-extracted text from documents while preserving meaning. Do not add new information or hallucinate content.
            """;

    private final String userCleanUpPromptTemplate = """
            I will provide you with raw text extracted from a document using OCR. Your job is to clean and normalize it.

            Goals:
            1. Fix OCR errors (misread characters, broken words, incorrect spacing).
            2. Remove noise (headers, footers, page numbers, watermarks, repeated artifacts).
            3. Reconstruct broken sentences where obvious.
            4. Normalize spacing and punctuation.
            5. Preserve original meaning exactly — do NOT rewrite or summarize.
            6. Keep the structure of the document where possible (paragraphs, sections, bullet points).
            7. If the structure is unclear, infer minimal logical formatting.
            8. Do NOT invent missing text.

            Output rules:
            - Return only the cleaned text.
            - Do not explain your changes.
            - Do not add commentary.
            - Maintain original language.

            Input text:
            %s
            """;

    @RabbitListener(queues = "${app.rabbitmq.textCleanupQueue}")
    public void cleanExtractedText(QueueObject queueObject) {

        try {
            DocumentDTO document = documentService.findById(queueObject.objectId());

            String finalPrompt = String.format(userCleanUpPromptTemplate, document.getFileContent());

            Prompt request = new Prompt();
            request.setModel(llmModel); // Specify the model you want to use
            request.setStream(false);

            PromptMessage system = new PromptMessage();
            system.setRole("system");
            system.setContent(systemCleanUpPrompt);

            PromptMessage message = new PromptMessage();
            message.setRole("user");
            message.setContent(finalPrompt);

            request.setMessages(List.of(system, message));

            lmStudioExtractorService.extractInformation(request)
                    .thenAccept(response -> {
                        documentProcessorService.updateFileContent(response, document);

                    })
                    .exceptionally(ex -> {
                        log.error("Error during text cleanup for document ID: {}", queueObject.objectId(), ex);
                        return null;
                    });

        } catch (Exception e) {
            log.error("Error retrieving document for ID: {}", queueObject.objectId(), e);
            return;
        }

    }
}
