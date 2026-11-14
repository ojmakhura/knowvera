package bw.co.knowvera.document;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.server.ResponseStatusException;

class UploadValidatorTest {

    private static MockMultipartFile file(String name, byte[] content) {
        return new MockMultipartFile("file", name, "application/octet-stream", content);
    }

    @Test
    void acceptsAllowedTypesWithMatchingContent() {
        assertDoesNotThrow(() -> UploadValidator.validate(file("id.pdf", "%PDF-1.7 ...".getBytes()), UploadValidator.DOCUMENT_TYPES));
        assertDoesNotThrow(() -> UploadValidator.validate(file("ID.JPG", new byte[] { (byte) 0xFF, (byte) 0xD8, (byte) 0xFF, 0 }), UploadValidator.DOCUMENT_TYPES));
        assertDoesNotThrow(() -> UploadValidator.validate(file("clients.csv", "name,id\nA,1\n".getBytes()), UploadValidator.SPREADSHEET_TYPES));
    }

    @Test
    void rejectsDisallowedTypesAndDisguisedContent() {
        ResponseStatusException exe = assertThrows(ResponseStatusException.class,
                () -> UploadValidator.validate(file("run.exe", "MZ".getBytes()), UploadValidator.DOCUMENT_TYPES));
        assertEquals(HttpStatus.UNSUPPORTED_MEDIA_TYPE, exe.getStatusCode());
        // HTML renamed to .pdf
        assertThrows(ResponseStatusException.class,
                () -> UploadValidator.validate(file("id.pdf", "<html><script>".getBytes()), UploadValidator.DOCUMENT_TYPES));
        // Binary content posing as CSV
        assertThrows(ResponseStatusException.class,
                () -> UploadValidator.validate(file("clients.csv", new byte[] { 'a', 0, 'b' }), UploadValidator.SPREADSHEET_TYPES));
        // Documents are not accepted as bulk imports
        assertThrows(ResponseStatusException.class,
                () -> UploadValidator.validate(file("id.pdf", "%PDF-1.7".getBytes()), UploadValidator.SPREADSHEET_TYPES));
        assertThrows(ResponseStatusException.class,
                () -> UploadValidator.validate(file("empty.pdf", new byte[0]), UploadValidator.DOCUMENT_TYPES));
    }

    @Test
    void namesAreSafeForStoragePaths() {
        assertEquals("passwd", UploadValidator.safeName("../../etc/passwd"));
        assertEquals("evil.pdf", UploadValidator.safeName("..\\..\\evil.pdf"));
        assertEquals("my_ID_card__1_.pdf", UploadValidator.safeName("my ID card (1).pdf"));
        assertEquals("hidden", UploadValidator.safeName(".hidden"));
        assertEquals("file", UploadValidator.safeName(null));
        assertEquals(120, UploadValidator.safeName("a".repeat(300) + ".pdf").length());
    }
}
