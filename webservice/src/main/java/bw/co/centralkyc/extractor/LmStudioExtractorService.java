package bw.co.centralkyc.extractor;

import java.util.concurrent.CompletableFuture;

import bw.co.centralkyc.lmstudio.CompletionRequestMessage;
// import org.springframework.ai.chat.client.ChatClient;
// import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import bw.co.centralkyc.lmstudio.CompletionRequest;
import bw.co.centralkyc.lmstudio.CompletionResponse;
import lombok.RequiredArgsConstructor;
import tools.jackson.databind.json.JsonMapper;

@RequiredArgsConstructor
@Service
public class LmStudioExtractorService {
    
    private final LmStudioExtractor lmStudioExtractor;
    // private final ChatClient geminiClient;
    private final JsonMapper jsonMapper;

    @Async("virtualThreadExecutor")
    public CompletableFuture<CompletionResponse> extractInformation(CompletionRequest request) {
        try {
            String response = lmStudioExtractor.createChatCompletion(request);
            return CompletableFuture.completedFuture(jsonMapper.readValue(response, CompletionResponse.class));
        } catch (Exception ex) {
            CompletableFuture<CompletionResponse> future = new CompletableFuture<>();
            future.completeExceptionally(ex);

            CompletionRequestMessage systemPrompt = request.getMessages().stream().filter(m -> "system".equals(m.getRole())).findFirst().orElse(null);

            CompletionRequestMessage userPrompt = request.getMessages().stream().filter(m -> "user".equals(m.getRole())).findFirst().orElse(null);

            // System.out.println("===============================================");

            // if(systemPrompt != null && userPrompt != null) {

            //     ChatResponse chatResponse = geminiClient.prompt()
            //             .system(systemPrompt.getContent())
            //             .user(userPrompt.getContent())
            //             .call()
            //             .chatResponse();
            // }

            return future;
        }
    }
}
