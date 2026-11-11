package bw.co.roguesystems.ai.storage;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.stereotype.Component;

import bw.co.roguesystems.ai.config.VeritasProperties;
import bw.co.roguesystems.ai.provider.DocumentFile;
import bw.co.roguesystems.ai.provider.ProviderException;
import bw.co.roguesystems.ai.schema.DocumentRef;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.exception.SdkException;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;

/**
 * Reads documents from the bucket the callers upload to (read-only access is enough).
 */
@Component
public class DocumentStore {

    private final S3Client s3Client;
    private final String bucket;
    private final long maxFileSize;

    public DocumentStore(S3Client s3Client, VeritasProperties properties) {
        this.s3Client = s3Client;
        this.bucket = properties.getStorage().getBucket();
        this.maxFileSize = properties.getLimits().getMaxFileSize().toBytes();
    }

    public DocumentFile read(DocumentRef ref) {
        if (ref == null) {
            throw new IllegalArgumentException("The job has no document");
        }
        String key = objectKey(ref.objectKey());
        try (ResponseInputStream<GetObjectResponse> object = s3Client
                .getObject(request -> request.bucket(bucket).key(key))) {
            Long size = object.response().contentLength();
            // One byte more than allowed is enough to know, whatever the size the server announced
            byte[] content = size != null && size > maxFileSize ? null : object.readNBytes(maxBytesRead());
            if (content == null || content.length > maxFileSize) {
                object.abort();
                throw new ProviderException("document '" + key + "' is larger than the " + maxFileSize
                        + " bytes allowed (veritas.limits.max-file-size)");
            }
            return new DocumentFile(content, ref.contentType(), ref.fileName());
        } catch (NoSuchKeyException e) {
            throw new ProviderException("document '" + key + "' is not in bucket '" + bucket + "'", e);
        } catch (SdkException | IOException e) {
            // Storage that cannot be reached now may be back for the step's next try
            throw new ProviderException("cannot read document '" + key + "' from bucket '" + bucket + "': "
                    + e.getMessage(), e, true);
        }
    }

    private int maxBytesRead() {
        return (int) Math.min(maxFileSize + 1, Integer.MAX_VALUE - 8);
    }

    /** The object key of a stored key or of a full URL to the object (path style or not). */
    static String objectKey(String keyOrUrl) {
        if (keyOrUrl == null || keyOrUrl.isBlank()) {
            throw new IllegalArgumentException("The document reference has no object key");
        }
        String candidate = keyOrUrl.trim();
        if (candidate.startsWith("http://") || candidate.startsWith("https://")) {
            try {
                String path = new URI(candidate).getPath();
                if (path == null || path.isBlank()) {
                    throw new IllegalArgumentException("The document URL has no object path: " + candidate);
                }
                candidate = path;
            } catch (URISyntaxException e) {
                throw new IllegalArgumentException("Invalid document URL: " + candidate, e);
            }
        }
        while (candidate.startsWith("/")) {
            candidate = candidate.substring(1);
        }
        if (candidate.isBlank()) {
            throw new IllegalArgumentException("The document reference has no object key");
        }
        return candidate;
    }
}
