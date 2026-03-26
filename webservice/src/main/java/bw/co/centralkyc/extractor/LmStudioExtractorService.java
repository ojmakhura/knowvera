package bw.co.centralkyc.extractor;

import java.util.concurrent.CompletableFuture;

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
    private final JsonMapper jsonMapper;

    @Async("virtualThreadExecutor")
    public CompletableFuture<CompletionResponse> extractInformation(CompletionRequest request) {
        try {
            String response = lmStudioExtractor.createChatCompletion(request);
            return CompletableFuture.completedFuture(jsonMapper.readValue(response, CompletionResponse.class));
        } catch (Exception ex) {
            CompletableFuture<CompletionResponse> future = new CompletableFuture<>();
            future.completeExceptionally(ex);
            return future;
        }
    }
}
