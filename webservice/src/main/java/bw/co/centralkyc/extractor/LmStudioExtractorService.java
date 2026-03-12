package bw.co.centralkyc.extractor;

import java.util.concurrent.CompletableFuture;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import bw.co.centralkyc.lmstudio.CompletionRequest;
import bw.co.centralkyc.lmstudio.CompletionResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class LmStudioExtractorService {
    
    private final LmStudioExtractor lmStudioExtractor;

    @Async("virtualThreadExecutor")
    public CompletableFuture<CompletionResponse> extractInformation(CompletionRequest request) {
        return CompletableFuture.supplyAsync(() -> lmStudioExtractor.createChatCompletion(request));
    }
}
