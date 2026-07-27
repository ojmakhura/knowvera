package bw.co.centralkyc.gemini;

import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Service;

@Service
public class GeminiService {
    private final GeminiClient geminiClient;

    public GeminiService(GeminiClient geminiClient) {
        this.geminiClient = geminiClient;
    }

    public ChatResponse generate(Prompt prompt) {
        return geminiClient.generate(prompt);
    }
}
