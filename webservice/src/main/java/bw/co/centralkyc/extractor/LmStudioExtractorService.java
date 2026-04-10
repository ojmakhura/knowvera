package bw.co.centralkyc.extractor;

import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Value;
// import org.springframework.ai.chat.client.ChatClient;
// import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import bw.co.centralkyc.llm.LmStudioResponse;
import bw.co.centralkyc.llm.OllamaResponse;
import bw.co.centralkyc.llm.Prompt;
import lombok.RequiredArgsConstructor;
import tools.jackson.databind.json.JsonMapper;

@RequiredArgsConstructor
@Service
public class LmStudioExtractorService {
    
    private final LmStudioExtractor lmStudioExtractor;
    // private final ChatClient geminiClient;
    private final JsonMapper jsonMapper;

    @Value("${app.llm.id}")
    private String llmId;

    @Async("virtualThreadExecutor")
    public CompletableFuture<Object> extractInformation(Prompt request) {
        try {
            String response = lmStudioExtractor.createChatCompletion(request);

            Object parsedResponse = jsonMapper.readValue(response, Object.class);

            if (llmId.equals("ollama")) {
                return CompletableFuture.completedFuture(jsonMapper.readValue(response, OllamaResponse.class));
            } else {
                return CompletableFuture.completedFuture(jsonMapper.readValue(response, LmStudioResponse.class));
                
            }

        } catch (Exception ex) {
            CompletableFuture<Object> future = new CompletableFuture<>();
            future.completeExceptionally(ex);

            // CompletionRequestMessage systemPrompt = request.getMessages().stream().filter(m -> "system".equals(m.getRole())).findFirst().orElse(null);

            // CompletionRequestMessage userPrompt = request.getMessages().stream().filter(m -> "user".equals(m.getRole())).findFirst().orElse(null);

            return future;
        }
    }
}
