package bw.co.roguesystems.ai.provider;

import bw.co.roguesystems.ai.profile.ProcessingStep;
import bw.co.roguesystems.ai.profile.ProviderType;

/**
 * Turns a file into text.
 */
public interface TextExtractionProvider {

    ProviderType type();

    /**
     * @return the text, never blank
     * @throws ProviderException when the provider finds no text or fails
     */
    ExtractedText extract(DocumentFile file, ProcessingStep step);
}
