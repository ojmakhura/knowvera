package bw.co.knowvera.gemini;

import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;

public interface GeminiClient {
    ChatResponse generate(Prompt prompt);
}
