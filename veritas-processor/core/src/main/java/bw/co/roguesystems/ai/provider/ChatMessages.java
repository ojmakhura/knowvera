package bw.co.roguesystems.ai.provider;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * The role/content message list the OpenAI style chat APIs (LM Studio, Ollama) take.
 */
public final class ChatMessages {

    private ChatMessages() {
    }

    public static List<Map<String, String>> of(LlmCall call) {
        List<Map<String, String>> messages = new ArrayList<>();
        if (call.systemPrompt() != null && !call.systemPrompt().isBlank()) {
            messages.add(Map.of("role", "system", "content", call.systemPrompt()));
        }
        messages.add(Map.of("role", "user", "content", call.userPrompt()));
        return messages;
    }
}
