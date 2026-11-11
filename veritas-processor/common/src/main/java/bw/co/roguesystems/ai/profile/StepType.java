package bw.co.roguesystems.ai.profile;

/**
 * The kinds of step a processing profile can run, in pipeline order.
 */
public enum StepType {
    /** File to text: PDF text layer, OCR or a multimodal model. */
    TEXT_EXTRACTION,
    /** OCR noise removal and normalisation of the extracted text. */
    TEXT_CLEANUP,
    /** Text to the requested fields, as JSON. */
    FIELD_EXTRACTION,
    /** Which of the candidate types the text is, scored against their fields. */
    CLASSIFICATION
}
