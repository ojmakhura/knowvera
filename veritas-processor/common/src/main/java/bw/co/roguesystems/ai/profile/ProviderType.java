package bw.co.roguesystems.ai.profile;

/**
 * The engines a step can use. A step lists them in order: the next one is the fallback.
 */
public enum ProviderType {
    /** The PDF's own text layer (PDFBox); fails when the PDF has none. */
    PDF_TEXT(false, false),
    /** Tesseract OCR on the rendered pages. */
    TESSERACT(false, false),
    /** Gemini on Vertex AI: OCR (the file as input) and chat. */
    GEMINI(true, true),
    /** An OpenAI compatible LM Studio server. */
    LM_STUDIO(false, true),
    /** An Ollama server. */
    OLLAMA(false, true);

    private final boolean cloud;
    private final boolean llm;

    ProviderType(boolean cloud, boolean llm) {
        this.cloud = cloud;
        this.llm = llm;
    }

    /** Whether the provider sends the document out of the deployment. */
    public boolean isCloud() {
        return cloud;
    }

    /** Whether the provider is a language model (usable for prompts). */
    public boolean isLlm() {
        return llm;
    }
}
