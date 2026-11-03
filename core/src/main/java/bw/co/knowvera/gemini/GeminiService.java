package bw.co.knowvera.gemini;

import bw.co.knowvera.settings.ProcessingTools;

import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Service;

@Service
public class GeminiService {
    private final GeminiClient geminiClient;
    private final ProcessingTools processingTools;

    public GeminiService(GeminiClient geminiClient, ProcessingTools processingTools) {
        this.geminiClient = geminiClient;
        this.processingTools = processingTools;
    }

    public ChatResponse generate(Prompt prompt) {
        // Never send a document out of the deployment when processing must stay local
        if (processingTools.isLocalOnly()) {
            throw new IllegalStateException("Gemini is disabled: app.processing.local-only is on");
        }
        return geminiClient.generate(prompt);
    }
}
