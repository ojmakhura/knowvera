package bw.co.roguesystems.ai.provider;

import bw.co.roguesystems.ai.profile.ProviderType;

/**
 * A language model.
 */
public interface LlmProvider {

    ProviderType type();

    /**
     * @throws ProviderException when the model cannot be reached or answers with nothing
     */
    LlmResponse chat(LlmCall call);
}
