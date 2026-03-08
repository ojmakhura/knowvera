package bw.co.centralkyc.document.processor;

import java.io.IOException;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import java.awt.image.BufferedImage;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
@RequiredArgsConstructor
public class DocumentProcessorService {

    @Value("${app.tessdata-prefix}")
    private String tessdataPrefix;

    @Value("${app.tessdata-langs}")
    private String tessdataLangs;

    @Async
    public CompletableFuture<String> extractText(byte[] pdfBytes) {
        return CompletableFuture.supplyAsync(() -> {
            validatePdfBytes(pdfBytes);

            // Use a single try-with-resources for the PDDocument
            try (PDDocument document = Loader.loadPDF(pdfBytes)) {

                // 1. Try standard extraction
                PDFTextStripper stripper = new PDFTextStripper();
                String extractedText = stripper.getText(document);

                // 2. Fallback to OCR if text is empty
                if (extractedText == null || extractedText.trim().isEmpty()) {
                    log.info("No text found; starting OCR process.");
                    return performOcr(document);
                }

                return extractedText;
            } catch (IOException e) {
                log.warn("Invalid or corrupted PDF payload", e);
                throw new IllegalArgumentException("Invalid PDF payload", e);
            } catch (Exception e) {
                log.error("PDF Processing failed", e);
                throw new RuntimeException("Failed to parse PDF", e);
            }
        });
    }

    private void validatePdfBytes(byte[] pdfBytes) {
        if (pdfBytes == null || pdfBytes.length < 5) {
            throw new IllegalArgumentException("PDF payload is empty or too small");
        }

        // Fast signature check to avoid sending non-PDF content into PDFBox.
        if (!(pdfBytes[0] == '%' && pdfBytes[1] == 'P' && pdfBytes[2] == 'D' && pdfBytes[3] == 'F' && pdfBytes[4] == '-')) {
            throw new IllegalArgumentException("File is not a valid PDF (missing %PDF- header)");
        }
    }

    private String performOcr(PDDocument document) throws IOException, TesseractException {
        StringBuilder sb = new StringBuilder();
        PDFRenderer renderer = new PDFRenderer(document);
        ITesseract tesseract = new Tesseract();
        tesseract.setDatapath(tessdataPrefix);
        tesseract.setLanguage(tessdataLangs);

        for (int page = 0; page < document.getNumberOfPages(); page++) {
            BufferedImage image = renderer.renderImageWithDPI(page, 300);
            sb.append(tesseract.doOCR(image)).append("\n");
        }
        return sb.toString();
    }
}
