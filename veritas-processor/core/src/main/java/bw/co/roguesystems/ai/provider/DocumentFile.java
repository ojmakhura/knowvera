package bw.co.roguesystems.ai.provider;

/**
 * A document's bytes, as read from storage.
 */
public record DocumentFile(byte[] content, String contentType, String fileName) {

    public boolean isPdf() {
        return content != null && content.length >= 5 && content[0] == '%' && content[1] == 'P' && content[2] == 'D'
                && content[3] == 'F' && content[4] == '-';
    }
}
