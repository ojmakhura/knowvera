package bw.co.knowvera.extractor;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

import bw.co.knowvera.llm.Prompt;

@HttpExchange("/api")
public interface OllamaIntegration {
    
    @PostExchange("/chat")
    String getOllamaResponse(@RequestBody Prompt request);
}
