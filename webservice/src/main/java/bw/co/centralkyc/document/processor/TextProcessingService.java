package bw.co.centralkyc.document.processor;

import java.util.List;

import bw.co.centralkyc.properties.RabbitProperties;
import bw.co.centralkyc.settings.SettingsDTO;
import bw.co.centralkyc.settings.SettingsService;
import bw.co.centralkyc.settings.Tool;
import bw.co.centralkyc.settings.ToolSelectorDTO;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import bw.co.centralkyc.QueueObject;
import bw.co.centralkyc.document.DocumentDTO;
import bw.co.centralkyc.document.DocumentService;
import bw.co.centralkyc.extractor.LmStudioExtractorService;
import bw.co.centralkyc.gemini.GeminiService;
import bw.co.centralkyc.llm.Prompt;
import bw.co.centralkyc.llm.PromptMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import tools.jackson.databind.json.JsonMapper;

@Service
@Slf4j
public class TextProcessingService {

  private static final Logger log = LoggerFactory.getLogger(TextProcessingService.class);

  @Value("${app.llm.model}")
  private String llmModel;

  private final DocumentService documentService;
  private final LmStudioExtractorService lmStudioExtractorService;
  private final JsonMapper jsonMapper;
  private final DocumentProcessorService documentProcessorService;
  private final RabbitTemplate rabbitTemplate;
  private final RabbitProperties rabbitProperties;
  private final GeminiService geminiService;
  private final SettingsService settingsService;

  public TextProcessingService(DocumentService documentService, LmStudioExtractorService lmStudioExtractorService,
      JsonMapper jsonMapper, DocumentProcessorService documentProcessorService, RabbitTemplate rabbitTemplate,
      RabbitProperties rabbitProperties, GeminiService geminiService, SettingsService settingsService) {
    this.documentService = documentService;
    this.lmStudioExtractorService = lmStudioExtractorService;
    this.jsonMapper = jsonMapper;
    this.documentProcessorService = documentProcessorService;
    this.rabbitTemplate = rabbitTemplate;
    this.rabbitProperties = rabbitProperties;
    this.geminiService = geminiService;
    this.settingsService = settingsService;
  }

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

      SettingsDTO settings = settingsService.getAll().stream().findFirst().orElse(null);
      if (settings == null) {
        log.warn("No settings found.");
        return;
      }
      List<ToolSelectorDTO> textProcessingTools = settings.getTextProcessingTools();

      String extractedText = document.getFileContent(); // Assuming this contains the extracted text

      if (StringUtils.isBlank(extractedText)) {
        log.warn("Extracted text is empty for document ID: {}", queueObject.objectId());
        return;
      }

      if (textProcessingTools.get(0).getTool() == Tool.LM_STUDIO) {

        lmStudioProcessor(document, extractedText);

      } else if (textProcessingTools.get(0).getTool() == Tool.GEMINI) {

        geminiProcessor(document, extractedText);

      } else {
        log.warn("No valid text processing tool configured for document ID: {}", queueObject.objectId());
      }

      log.info("Completed text processing for document ID: {}", queueObject.objectId());

    } catch (Exception e) {
      log.error("Text processing interrupted for document ID: {}", queueObject.objectId(), e);
    }
  }

  private void geminiProcessor(DocumentDTO document, String extractedText) {
    // Call GeminiService to process the extracted text

    Message systemMessage = new SystemMessage(
        "You are a data extraction assistant. You **MUST ONLY output valid JSON**. Do not include explanations, notes, reasoning, or any extra text. Follow the instructions carefully.");

    StringBuilder contentBuilder = new StringBuilder();
    contentBuilder.append(initialPrompt)
        .append('\n')
        .append(jsonMapper.writeValueAsString(document.getExpectedFields()))
        .append('\n')
        .append("Text to process: ")
        .append(extractedText);

    Message userMessage = new UserMessage(contentBuilder.toString());

    org.springframework.ai.chat.prompt.Prompt request = new org.springframework.ai.chat.prompt.Prompt(
        List.of(systemMessage, userMessage));

    ChatResponse response = geminiService.generate(request);
    documentProcessorService.processExtractedData(response, document)
        .thenAccept(continueProcessing -> {
          if (continueProcessing) {
            QueueObject queueItem = new QueueObject(
                document.getId(),
                document.getTarget(),
                document.getTargetId());

            rabbitTemplate.convertAndSend(
                rabbitProperties.getDocumentConfirmationQueueExchange(),
                rabbitProperties.getDocumentConfirmationQueueRoutingKey(),
                queueItem);
          }
        });
  }

  private void lmStudioProcessor(DocumentDTO document, String extractedText) throws Exception {
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
          documentProcessorService.processExtractedData(response, document)
              .thenAccept(continueProcessing -> {
                if (continueProcessing) {
                  QueueObject queueItem = new QueueObject(
                      document.getId(),
                      document.getTarget(),
                      document.getTargetId());

                  rabbitTemplate.convertAndSend(
                      rabbitProperties.getDocumentConfirmationQueueExchange(),
                      rabbitProperties.getDocumentConfirmationQueueRoutingKey(),
                      queueItem);
                }
              });
        })
        .exceptionally(ex -> {
          System.err.println("❌ ERROR:");
          ex.printStackTrace();
          return null;
        });
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

      SettingsDTO settings = settingsService.loadSettings();
      List<ToolSelectorDTO> textCleanupTools = settings.getTextCleanupTools();

      if (textCleanupTools.get(0).getTool() == Tool.LM_STUDIO) {
        lmStudioProcessorForCleanup(document, finalPrompt);
      } else if (textCleanupTools.get(0).getTool() == Tool.GEMINI) {
        geminiProcessorForCleanup(document, finalPrompt);
      } else {
        log.warn("No valid text cleanup tool configured for document ID: {}", queueObject.objectId());
      }

    } catch (Exception e) {
      log.error("Error retrieving document for ID: {}", queueObject.objectId(), e);
      return;
    }

  }

  private void geminiProcessorForCleanup(DocumentDTO document, String finalPrompt) {
    // Call GeminiService to process the extracted text
    Message systemMessage = new AssistantMessage(systemCleanUpPrompt);

    Message userMessage = new UserMessage(finalPrompt);

    org.springframework.ai.chat.prompt.Prompt request = new org.springframework.ai.chat.prompt.Prompt(
        List.of(systemMessage, userMessage));

    ChatResponse response = geminiService.generate(request);
    documentProcessorService.updateFileContent(response, document)
        .thenAccept(continueProcessing -> {
          if (continueProcessing) {
            QueueObject queueItem = new QueueObject(
                document.getId(),
                document.getTarget(),
                document.getTargetId());

            rabbitTemplate.convertAndSend(
                rabbitProperties.getTextProcessingQueueExchange(),
                rabbitProperties.getTextProcessingQueueRoutingKey(),
                queueItem);
          }
        });
  }

  private void lmStudioProcessorForCleanup(DocumentDTO document, String finalPrompt) throws Exception {
    // Call LmStudioExtractor to process the extracted text
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
          documentProcessorService.updateFileContent(response, document)
              .thenAccept(continueProcessing -> {
                if (continueProcessing) {
                  QueueObject queueItem = new QueueObject(
                      document.getId(),
                      document.getTarget(),
                      document.getTargetId());

                  rabbitTemplate.convertAndSend(
                      rabbitProperties.getTextProcessingQueueExchange(),
                      rabbitProperties.getTextProcessingQueueRoutingKey(),
                      queueItem);
                }
              });
        })
        .exceptionally(ex -> {
          log.error("Error during text cleanup for document ID: {}", document.getId(), ex);
          return null;
        });
  }
}
