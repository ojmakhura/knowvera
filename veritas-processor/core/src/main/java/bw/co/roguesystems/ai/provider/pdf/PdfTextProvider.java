package bw.co.roguesystems.ai.provider.pdf;

import java.io.IOException;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Component;

import bw.co.roguesystems.ai.config.VeritasProperties;
import bw.co.roguesystems.ai.profile.ProcessingStep;
import bw.co.roguesystems.ai.profile.ProviderType;
import bw.co.roguesystems.ai.provider.DocumentFile;
import bw.co.roguesystems.ai.provider.ExtractedText;
import bw.co.roguesystems.ai.provider.ProviderException;
import bw.co.roguesystems.ai.provider.TextExtractionProvider;

/**
 * The PDF's own text layer. Scanned PDFs have none, or only a stamp or header, so this is usually
 * followed by OCR.
 */
@Component
public class PdfTextProvider implements TextExtractionProvider {

    private final VeritasProperties.Limits limits;

    public PdfTextProvider(VeritasProperties properties) {
        this.limits = properties.getLimits();
    }

    @Override
    public ProviderType type() {
        return ProviderType.PDF_TEXT;
    }

    @Override
    public ExtractedText extract(DocumentFile file, ProcessingStep step) {
        if (!file.isPdf()) {
            throw new ProviderException("not a PDF");
        }
        try (PDDocument document = Loader.loadPDF(file.content())) {
            int pages = document.getNumberOfPages();
            if (pages > limits.getMaxPages()) {
                throw new ProviderException("the PDF has " + pages + " pages, more than the " + limits.getMaxPages()
                        + " allowed (veritas.limits.max-pages)");
            }
            String text = new PDFTextStripper().getText(document);
            if (text == null || text.isBlank()) {
                throw new ProviderException("the PDF has no text layer");
            }
            long characters = text.chars().filter(character -> !Character.isWhitespace(character)).count();
            if (characters < (long) limits.getMinTextCharsPerPage() * Math.max(pages, 1)) {
                throw new ProviderException("the PDF's text layer is too thin to be the document's text ("
                        + characters + " characters on " + pages + " pages)");
            }
            return ExtractedText.of(text);
        } catch (IOException e) {
            throw new ProviderException("unreadable PDF: " + e.getMessage(), e);
        }
    }
}
