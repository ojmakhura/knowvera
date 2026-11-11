package bw.co.roguesystems.ai.provider.gemini;

import java.util.ArrayList;
import java.util.List;

import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.google.genai.GoogleGenAiChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.content.Media;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Component;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;

import bw.co.roguesystems.ai.profile.DefaultPrompts;
import bw.co.roguesystems.ai.profile.ProcessingStep;
import bw.co.roguesystems.ai.profile.ProviderType;
import bw.co.roguesystems.ai.provider.DocumentFile;
import bw.co.roguesystems.ai.provider.ExtractedText;
import bw.co.roguesystems.ai.provider.LlmCall;
import bw.co.roguesystems.ai.provider.LlmProvider;
import bw.co.roguesystems.ai.provider.LlmResponse;
import bw.co.roguesystems.ai.provider.ProviderException;
import bw.co.roguesystems.ai.provider.TextExtractionProvider;

/**
 * Gemini on Vertex AI (spring.ai.google.genai.*): chat, and OCR with the file attached to the
 * prompt. Step options: temperature, topP, topK, maxTokens.
 */
@Component
public class GeminiProvider implements LlmProvider, TextExtractionProvider {

    private final ObjectProvider<ChatModel> chatModel;

    public GeminiProvider(ObjectProvider<ChatModel> chatModel) {
        this.chatModel = chatModel;
    }

    @Override
    public ProviderType type() {
        return ProviderType.GEMINI;
    }

    @Override
    public LlmResponse chat(LlmCall call) {
        List<Message> messages = new ArrayList<>();
        if (call.systemPrompt() != null && !call.systemPrompt().isBlank()) {
            messages.add(new SystemMessage(call.systemPrompt()));
        }
        messages.add(new UserMessage(call.userPrompt()));
        return call(new Prompt(messages, options(call)));
    }

    @Override
    public ExtractedText extract(DocumentFile file, ProcessingStep step) {
        MimeType mimeType = file.isPdf() ? MimeTypeUtils.parseMimeType("application/pdf")
                : file.contentType() == null ? MimeTypeUtils.APPLICATION_OCTET_STREAM
                        : MimeTypeUtils.parseMimeType(file.contentType());
        String system = step.getSystemPrompt() == null ? DefaultPrompts.OCR_SYSTEM : step.getSystemPrompt();
        String user = step.getUserPrompt() == null ? DefaultPrompts.OCR_USER : step.getUserPrompt();

        UserMessage message = UserMessage.builder()
                .text(user)
                .media(new Media(mimeType, new ByteArrayResource(file.content())))
                .build();
        LlmCall call = new LlmCall(system, user, step.getModel(), step.getOptions());
        LlmResponse response = call(new Prompt(List.of(new SystemMessage(system), message), options(call)));
        return new ExtractedText(response.content(), response.model() == null ? step.getModel() : response.model());
    }

    private LlmResponse call(Prompt prompt) {
        ChatModel model = chatModel.getIfAvailable();
        if (model == null) {
            throw new ProviderException("Gemini is not configured (spring.ai.model.chat=google-genai, "
                    + "spring.ai.google.genai.project-id and location, and Google Cloud credentials)");
        }
        ChatResponse response;
        try {
            response = model.call(prompt);
        } catch (RuntimeException e) {
            // Mostly quota, availability and network failures: worth another try
            throw new ProviderException("Gemini call failed: " + e.getMessage(), e, true);
        }
        if (response == null || response.getResult() == null || response.getResult().getOutput() == null
                || response.getResult().getOutput().getText() == null
                || response.getResult().getOutput().getText().isBlank()) {
            throw new ProviderException("Gemini returned no text");
        }
        String usedModel = response.getMetadata() == null ? null : response.getMetadata().getModel();
        return new LlmResponse(response.getResult().getOutput().getText(), usedModel);
    }

    private static ChatOptions options(LlmCall call) {
        GoogleGenAiChatOptions.Builder options = GoogleGenAiChatOptions.builder()
                .model(call.model())
                .temperature(call.temperature())
                .topP(call.doubleOption("topP"))
                .topK(call.intOption("topK"));
        if (call.intOption("maxTokens") != null) {
            options.maxOutputTokens(call.intOption("maxTokens"));
        }
        if (call.responseSchema() != null) {
            // Gemini's schema dialect differs from JSON schema: ask for JSON, the prompt gives the shape
            options.responseMimeType("application/json");
        }
        return options.build();
    }
}
