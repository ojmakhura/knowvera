package bw.co.roguesystems.ai.profile;

import java.util.List;
import java.util.Map;

/**
 * One step of a processing profile.
 *
 * @param type          what the step does
 * @param providers     the engines to try, in order
 * @param model         the model for language model providers (null: the provider's default)
 * @param systemPrompt  system prompt template (language model providers)
 * @param userPrompt    user prompt template; placeholders: {{documentType}}, {{fields}},
 *                      {{candidateTypes}} and {{text}}
 * @param options       provider options, e.g. temperature, topP, topK, ocrLanguages, ocrDpi
 */
public record ProcessingStepDTO(
        StepType type,
        List<ProviderType> providers,
        String model,
        String systemPrompt,
        String userPrompt,
        Map<String, String> options) {
}
