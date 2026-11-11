package bw.co.roguesystems.ai.provider.gemini;

import java.util.List;

import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.content.Media;
import org.springframework.ai.google.genai.GoogleGenAiChatOptions;
import org.springframework.ai.model.tool.StructuredOutputChatOptions;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Component;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;

import bw.co.roguesystems.ai.profile.DefaultPrompts;
import bw.co.roguesystems.ai.profile.ProcessingStep;
import bw.co.roguesystems.ai.profile.ProviderType;
import bw.co.roguesystems.ai.provider.ChatModelProvider;
import bw.co.roguesystems.ai.provider.DocumentFile;
import bw.co.roguesystems.ai.provider.ExtractedText;
import bw.co.roguesystems.ai.provider.LlmCall;
import bw.co.roguesystems.ai.provider.LlmResponse;
import bw.co.roguesystems.ai.provider.ProviderException;
import bw.co.roguesystems.ai.provider.TextExtractionProvider;

/**
 * Gemini on Vertex AI (spring.ai.google.genai.*): chat, and OCR with the file attached to the
 * prompt.
 */
@Component
public class GeminiProvider extends ChatModelProvider implements TextExtractionProvider {

    private final ObjectProvider<ChatModel> chatModel;

    public GeminiProvider(ObjectProvider<ChatModel> chatModel) {
        super(ProviderType.GEMINI, "Gemini");
        this.chatModel = chatModel;
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
        LlmResponse response = complete(List.of(new SystemMessage(system), message),
                new LlmCall(system, user, step.getModel(), step.getOptions()));
        return new ExtractedText(response.content(), response.model());
    }

    /** Created on first use (see {@link GeminiLazyInitConfig}): without credentials only Gemini steps fail. */
    @Override
    protected ChatModel model() {
        ChatModel model = chatModel.getIfAvailable();
        if (model == null) {
            throw new ProviderException("Gemini is not configured (spring.ai.model.chat=google-genai, "
                    + "spring.ai.google.genai.project-id and location, and Google Cloud credentials)");
        }
        return model;
    }

    /** spring.ai.google.genai.chat.model */
    @Override
    protected String defaultModel() {
        return null;
    }

    @Override
    protected StructuredOutputChatOptions.Builder<?> options(LlmCall call) {
        GoogleGenAiChatOptions.Builder options = GoogleGenAiChatOptions.builder();
        if (call.responseSchema() != null) {
            options.responseMimeType("application/json");
        }
        return options;
    }

    /** Gemini takes a subset of JSON schema: it is asked for JSON, the prompt gives the shape. */
    @Override
    protected boolean takesSchema() {
        return false;
    }

    /** Mostly quota, availability and network failures. */
    @Override
    protected boolean retryable(Throwable failure) {
        return true;
    }
}
