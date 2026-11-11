package bw.co.roguesystems.ai.provider;

/**
 * A language model's answer, with the model that gave it.
 */
public record LlmResponse(String content, String model) {
}
