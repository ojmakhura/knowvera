package bw.co.roguesystems.ai.provider;

import java.util.Map;

/**
 * A chat request to a language model.
 *
 * @param systemPrompt   rendered system prompt
 * @param userPrompt     rendered user prompt
 * @param model          model to use (null: the provider's default)
 * @param options        step options (temperature, topP, topK, numCtx, maxTokens, ...)
 * @param responseSchema JSON schema the answer must follow (null: free text)
 */
public record LlmCall(String systemPrompt, String userPrompt, String model, Map<String, String> options,
        Map<String, Object> responseSchema) {

    /** Extraction must give the same answer for the same document. */
    public static final double DEFAULT_TEMPERATURE = 0.0;

    public LlmCall(String systemPrompt, String userPrompt, String model, Map<String, String> options) {
        this(systemPrompt, userPrompt, model, options, null);
    }

    /** The step's temperature, or {@link #DEFAULT_TEMPERATURE}. */
    public double temperature() {
        Double temperature = doubleOption("temperature");
        return temperature == null ? DEFAULT_TEMPERATURE : temperature;
    }

    public Double doubleOption(String key) {
        String value = options == null ? null : options.get(key);
        return value == null || value.isBlank() ? null : Double.valueOf(value.trim());
    }

    public Integer intOption(String key) {
        String value = options == null ? null : options.get(key);
        return value == null || value.isBlank() ? null : Integer.valueOf(value.trim());
    }

    /** Characters of both prompts. */
    public int promptLength() {
        return (systemPrompt == null ? 0 : systemPrompt.length()) + (userPrompt == null ? 0 : userPrompt.length());
    }
}
