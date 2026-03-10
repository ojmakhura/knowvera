package bw.co.centralkyc.extractor;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

import bw.co.centralkyc.lmstudio.CompletionRequest;
import bw.co.centralkyc.lmstudio.CompletionResponse;

@HttpExchange(url = "${app.lmstudio.base-url}")
public interface LmStudioExtractor {
    
    @PostExchange("/v1/chat/completions")
    CompletionResponse createChatCompletion(@RequestBody CompletionRequest request);

    
}
