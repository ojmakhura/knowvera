package bw.co.knowvera.document;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

/**
 * Accepts only the file types the platform handles, checking the file's content (its leading
 * "magic" bytes) against its extension, and makes names safe for use in storage paths.
 * Size limits are set by spring.servlet.multipart.
 */
public final class UploadValidator {

    /** Identity documents, proofs and generated-document templates. */
    public static final Set<String> DOCUMENT_TYPES = Set.of("pdf", "png", "jpg", "jpeg", "docx", "xlsx", "xls", "csv");

    /** Bulk imports (client requests). */
    public static final Set<String> SPREADSHEET_TYPES = Set.of("xlsx", "xls", "csv");

    private static final byte[] PDF = { '%', 'P', 'D', 'F', '-' };
    private static final byte[] PNG = { (byte) 0x89, 'P', 'N', 'G' };
    private static final byte[] JPEG = { (byte) 0xFF, (byte) 0xD8, (byte) 0xFF };
    private static final byte[] ZIP = { 'P', 'K', 3, 4 };  // xlsx, docx (Office Open XML)
    private static final byte[] OLE2 = { (byte) 0xD0, (byte) 0xCF, 0x11, (byte) 0xE0 };  // xls

    private static final Map<String, byte[]> SIGNATURES = Map.of(
            "pdf", PDF, "png", PNG, "jpg", JPEG, "jpeg", JPEG, "docx", ZIP, "xlsx", ZIP, "xls", OLE2);

    private static final int MAX_NAME_LENGTH = 120;

    private UploadValidator() {
    }

    /** Rejects (415) a file whose extension is not allowed or whose content does not match it. */
    public static void validate(MultipartFile file, Set<String> allowedTypes) {

        if (file == null || file.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No file uploaded");
        }

        String extension = extension(file.getOriginalFilename());
        if (!allowedTypes.contains(extension)) {
            throw new ResponseStatusException(HttpStatus.UNSUPPORTED_MEDIA_TYPE,
                    "File type not allowed; allowed: " + allowedTypes);
        }

        byte[] head = readHead(file);
        boolean matches = "csv".equals(extension)
                ? isText(head)
                : startsWith(head, SIGNATURES.get(extension));
        if (!matches) {
            throw new ResponseStatusException(HttpStatus.UNSUPPORTED_MEDIA_TYPE,
                    "File content does not match its ." + extension + " extension");
        }
    }

    /**
     * A name safe for a storage path segment: no directories, only letters, digits, '.', '_' and
     * '-', no leading dots, at most 120 characters.
     */
    public static String safeName(String name) {

        String base = name == null ? "" : name.replace('\\', '/');
        base = base.substring(base.lastIndexOf('/') + 1);
        base = base.replaceAll("[^A-Za-z0-9._-]", "_").replaceAll("^\\.+", "");
        if (base.length() > MAX_NAME_LENGTH) {
            String ext = extension(base);
            base = base.substring(0, MAX_NAME_LENGTH - ext.length() - 1) + "." + ext;
        }
        return base.isBlank() ? "file" : base;
    }

    private static String extension(String name) {
        int dot = name == null ? -1 : name.lastIndexOf('.');
        return dot < 0 ? "" : name.substring(dot + 1).toLowerCase(Locale.ROOT);
    }

    private static byte[] readHead(MultipartFile file) {
        try (InputStream in = file.getInputStream()) {
            return in.readNBytes(8192);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unreadable file");
        }
    }

    private static boolean startsWith(byte[] data, byte[] prefix) {
        return prefix != null && data.length >= prefix.length
                && Arrays.equals(Arrays.copyOf(data, prefix.length), prefix);
    }

    private static boolean isText(byte[] data) {
        for (byte b : data) {
            if (b == 0) {
                return false;
            }
        }
        return true;
    }
}
