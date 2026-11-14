package bw.co.knowvera.processing.provider;

/**
 * A language model's answer, with the model that gave it.
 */
public record LlmResponse(String content, String model) {
}
