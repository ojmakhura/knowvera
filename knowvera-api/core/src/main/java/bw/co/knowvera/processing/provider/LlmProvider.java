package bw.co.knowvera.processing.provider;

import bw.co.knowvera.processing.profile.ProviderType;

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
