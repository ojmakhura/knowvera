package bw.co.knowvera.gemini;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Service;

@Service
public class VertexAiGeminiClient implements GeminiClient {
    private final ObjectProvider<ChatModel> chatModelProvider;

    public VertexAiGeminiClient(ObjectProvider<ChatModel> chatModelProvider) {
        this.chatModelProvider = chatModelProvider;
    }

    @Override
    public ChatResponse generate(Prompt prompt) {
        if (prompt == null) {
            throw new IllegalArgumentException("Prompt must not be blank.");
        }

        ChatModel chatModel = chatModelProvider.getIfAvailable();
        if (chatModel == null) {
            throw new IllegalStateException(
                    "Vertex AI Gemini chat model is not configured. Set spring.ai.model.chat=vertexai, "
                            + "spring.ai.vertex.ai.gemini.project-id, spring.ai.vertex.ai.gemini.location, "
                            + "and Google Cloud credentials (GOOGLE_APPLICATION_CREDENTIALS).");
        }

        return chatModel.call(prompt);
    }
}
