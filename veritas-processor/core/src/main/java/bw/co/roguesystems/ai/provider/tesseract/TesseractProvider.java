package bw.co.roguesystems.ai.provider.tesseract;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.stereotype.Component;

import bw.co.roguesystems.ai.config.VeritasProperties;
import bw.co.roguesystems.ai.profile.ProcessingStep;
import bw.co.roguesystems.ai.profile.ProviderType;
import bw.co.roguesystems.ai.provider.DocumentFile;
import bw.co.roguesystems.ai.provider.ExtractedText;
import bw.co.roguesystems.ai.provider.ProviderException;
import bw.co.roguesystems.ai.provider.TextExtractionProvider;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

/**
 * Tesseract OCR on each page of a PDF (rendered at the step's ocrDpi), or on an image file.
 * Step options: ocrLanguages (e.g. "eng+tsn"), ocrDpi.
 */
@Component
public class TesseractProvider implements TextExtractionProvider {

    private final VeritasProperties.Tesseract settings;
    private final int maxPages;

    public TesseractProvider(VeritasProperties properties) {
        this.settings = properties.getTesseract();
        this.maxPages = properties.getLimits().getMaxPages();
    }

    @Override
    public ProviderType type() {
        return ProviderType.TESSERACT;
    }

    @Override
    public ExtractedText extract(DocumentFile file, ProcessingStep step) {
        ITesseract tesseract = new Tesseract();
        tesseract.setDatapath(settings.getDatapath());
        String languages = step.option("ocrLanguages");
        tesseract.setLanguage(languages == null || languages.isBlank() ? settings.getLanguages() : languages);

        try {
            String text = file.isPdf() ? ocrPdf(tesseract, file, dpi(step), maxPages) : ocrImage(tesseract, file);
            if (text.isBlank()) {
                throw new ProviderException("OCR found no text");
            }
            return ExtractedText.of(text);
        } catch (IOException | TesseractException e) {
            throw new ProviderException("OCR failed: " + e.getMessage(), e);
        }
    }

    private int dpi(ProcessingStep step) {
        String dpi = step.option("ocrDpi");
        return dpi == null || dpi.isBlank() ? settings.getDpi() : Integer.parseInt(dpi.trim());
    }

    private static String ocrPdf(ITesseract tesseract, DocumentFile file, int dpi, int maxPages)
            throws IOException, TesseractException {
        StringBuilder text = new StringBuilder();
        try (PDDocument document = Loader.loadPDF(file.content())) {
            // Every page is rendered to an image: a long PDF would take the memory of the other jobs
            if (document.getNumberOfPages() > maxPages) {
                throw new ProviderException("the PDF has " + document.getNumberOfPages() + " pages, more than the "
                        + maxPages + " allowed (veritas.limits.max-pages)");
            }
            PDFRenderer renderer = new PDFRenderer(document);
            for (int page = 0; page < document.getNumberOfPages(); page++) {
                text.append(tesseract.doOCR(renderer.renderImageWithDPI(page, dpi))).append('\n');
            }
        }
        return text.toString();
    }

    private static String ocrImage(ITesseract tesseract, DocumentFile file) throws IOException, TesseractException {
        BufferedImage image = ImageIO.read(new ByteArrayInputStream(file.content()));
        if (image == null) {
            throw new ProviderException("neither a PDF nor a readable image");
        }
        return tesseract.doOCR(image);
    }
}
