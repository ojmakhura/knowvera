package bw.co.knowvera.processing.job;

import java.util.List;

import bw.co.knowvera.processing.profile.ProviderType;
import bw.co.knowvera.processing.profile.StepType;

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
