package bw.co.kyvera.extractor;

import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Value;
// import org.springframework.ai.chat.client.ChatClient;
// import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import bw.co.kyvera.llm.LmStudioResponse;
import bw.co.kyvera.llm.OllamaResponse;
import bw.co.kyvera.llm.Prompt;
import lombok.RequiredArgsConstructor;
import tools.jackson.databind.json.JsonMapper;

@Service
public class LmStudioExtractorService {

    private final LmStudioExtractor lmStudioExtractor;
    private final OllamaIntegration ollamaIntegration;
    // private final ChatClient geminiClient;
    private final JsonMapper jsonMapper;

    public LmStudioExtractorService(LmStudioExtractor lmStudioExtractor, OllamaIntegration ollamaIntegration,
            JsonMapper jsonMapper) {
        this.lmStudioExtractor = lmStudioExtractor;
        this.ollamaIntegration = ollamaIntegration;
        this.jsonMapper = jsonMapper;
    }

    @Value("${app.llm.id}")
    private String llmId;

    @Async("virtualThreadExecutor")
    public CompletableFuture<Object> extractInformation(Prompt request) {
        try {

            if (llmId.equals("ollama")) {
                String response = ollamaIntegration.getOllamaResponse(request);

                return CompletableFuture.completedFuture(jsonMapper.readValue(response, OllamaResponse.class));
            } else {

                String response = lmStudioExtractor.createChatCompletion(request);
                return CompletableFuture.completedFuture(jsonMapper.readValue(response, LmStudioResponse.class));

            }

        } catch (Exception ex) {
            CompletableFuture<Object> future = new CompletableFuture<>();
            future.completeExceptionally(ex);

            // CompletionRequestMessage systemPrompt =
            // request.getMessages().stream().filter(m ->
            // "system".equals(m.getRole())).findFirst().orElse(null);

            // CompletionRequestMessage userPrompt = request.getMessages().stream().filter(m
            // -> "user".equals(m.getRole())).findFirst().orElse(null);

            return future;
        }
    }
}
