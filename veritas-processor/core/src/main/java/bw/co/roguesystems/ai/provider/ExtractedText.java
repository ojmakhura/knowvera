package bw.co.roguesystems.ai.provider;

/**
 * A file's text, with the model that read it (null for providers that are not language models).
 */
public record ExtractedText(String text, String model) {

    public static ExtractedText of(String text) {
        return new ExtractedText(text, null);
    }
}
