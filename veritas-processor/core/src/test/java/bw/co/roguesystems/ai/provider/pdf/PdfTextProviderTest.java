package bw.co.roguesystems.ai.provider.pdf;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.junit.jupiter.api.Test;

import bw.co.roguesystems.ai.config.VeritasProperties;
import bw.co.roguesystems.ai.provider.DocumentFile;
import bw.co.roguesystems.ai.provider.ProviderException;

class PdfTextProviderTest {

    private final VeritasProperties properties = new VeritasProperties();

    private static DocumentFile pdf(int pages, String textOnEachPage) throws IOException {
        try (PDDocument document = new PDDocument(); ByteArrayOutputStream bytes = new ByteArrayOutputStream()) {
            for (int i = 0; i < pages; i++) {
                PDPage page = new PDPage();
                document.addPage(page);
                try (PDPageContentStream content = new PDPageContentStream(document, page)) {
                    content.beginText();
                    content.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 10);
                    content.newLineAtOffset(40, 700);
                    content.showText(textOnEachPage);
                    content.endText();
                }
            }
            document.save(bytes);
            return new DocumentFile(bytes.toByteArray(), "application/pdf", "test.pdf");
        }
    }

    @Test
    void readsTheTextLayer() throws IOException {
        String text = "REPUBLIC OF BOTSWANA National identity card, surname MOYO, forenames KAGO, born 14/03/1990";

        assertThat(new PdfTextProvider(properties).extract(pdf(1, text), null).text()).contains("surname MOYO");
    }

    @Test
    void aStampOnAScanIsNotTheDocumentsText() throws IOException {
        assertThatThrownBy(() -> new PdfTextProvider(properties).extract(pdf(2, "Scanned by office"), null))
                .isInstanceOf(ProviderException.class).hasMessageContaining("too thin");
    }

    @Test
    void refusesMorePagesThanAllowed() throws IOException {
        properties.getLimits().setMaxPages(2);

        assertThatThrownBy(() -> new PdfTextProvider(properties).extract(pdf(3, "page"), null))
                .isInstanceOf(ProviderException.class).hasMessageContaining("3 pages");
    }
}
