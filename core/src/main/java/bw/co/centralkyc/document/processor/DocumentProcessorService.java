package bw.co.centralkyc.document.processor;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import bw.co.centralkyc.document.DocumentDTO;
import bw.co.centralkyc.document.DocumentService;
import bw.co.centralkyc.minio.MinioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import java.awt.image.BufferedImage;

@Service
@RequiredArgsConstructor
public class DocumentProcessorService {
    

    @Value("${app.tessdata-prefix}")
    private String tessdataPrefix;

    @Value("${app.tessdata-langs}")
    private String tessdataLangs;
    
    private String extractTextFromScannedPdf(byte[] pdfBytes) throws IOException, TesseractException {
        StringBuilder text = new StringBuilder();

        try (PDDocument document = Loader.loadPDF(pdfBytes)) {

            PDFRenderer renderer = new PDFRenderer(document);

            ITesseract tesseract = new Tesseract();
            tesseract.setLanguage(tessdataLangs);

            // ⚠️ IMPORTANT: set this correctly
            tesseract.setDatapath(tessdataPrefix);

            for (int page = 0; page < document.getNumberOfPages(); page++) {
                BufferedImage image =
                        renderer.renderImageWithDPI(page, 300);

                String pageText = tesseract.doOCR(image);
                text.append(pageText).append("\n");
            }
        }

        return text.toString();
    }

    private String extractTextFromPdf(byte[] pdfBytes) throws IOException {
        StringBuilder text = new StringBuilder();

        try (PDDocument document = Loader.loadPDF(pdfBytes)) {
            PDFTextStripper stripper = new PDFTextStripper();
            text.append(stripper.getText(document));
        }

        return text.toString();
    }

    @Async
    public String extractText(byte[] pdfBytes) throws IOException, TesseractException {
        String extractedText = extractTextFromPdf(pdfBytes);

        // If no text found, try OCR
        if (extractedText.trim().isEmpty()) {
            extractedText = extractTextFromScannedPdf(pdfBytes);
        }

        return extractedText;
    }
}
