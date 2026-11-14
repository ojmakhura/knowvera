package bw.co.knowvera.processing.provider;

import bw.co.knowvera.processing.profile.ProcessingStep;
import bw.co.knowvera.processing.profile.ProviderType;

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
