package bw.co.roguesystems.ai.job;

import java.util.List;

import bw.co.roguesystems.ai.profile.ProviderType;
import bw.co.roguesystems.ai.profile.StepType;

/**
 * What a step did.
 *
 * @param type       the step
 * @param status     outcome
 * @param provider   the provider that produced the result (null when none did)
 * @param model      the model it used, for language model providers
 * @param durationMs time spent on the step, all attempts included
 * @param failures   one message per provider that failed before the result, in order
 */
public record StepResult(
        StepType type,
        StepStatus status,
        ProviderType provider,
        String model,
        long durationMs,
        List<String> failures) {
}
