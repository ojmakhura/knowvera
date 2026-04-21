package bw.co.centralkyc.extractor;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

import bw.co.centralkyc.llm.Prompt;


@HttpExchange("/v1")
public interface LmStudioExtractor {
    
    @PostExchange("/chat/completions")
    String createChatCompletion(@RequestBody Prompt request);
}
