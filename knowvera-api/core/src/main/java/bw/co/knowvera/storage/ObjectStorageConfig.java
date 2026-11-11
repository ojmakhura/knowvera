package bw.co.knowvera.storage;

import java.net.URI;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.awspring.cloud.autoconfigure.s3.S3ClientCustomizer;
import software.amazon.awssdk.core.checksums.RequestChecksumCalculation;
import software.amazon.awssdk.core.checksums.ResponseChecksumValidation;

/**
 * Adjusts the S3Client Spring Cloud AWS builds from spring.cloud.aws.* (region, keys, path-style
 * access) for an S3-compatible server. See docs/storage.md.
 */
@Configuration
public class ObjectStorageConfig {

    /**
     * The server's S3 API (storage.s3.endpoint), applied only when set: Spring Cloud AWS's own
     * spring.cloud.aws.s3.endpoint fails at startup when it is empty, and blank here means AWS S3.
     */
    static URI endpoint(String endpoint) {
        if (StringUtils.isBlank(endpoint)) {
            return null;
        }
        URI uri;
        try {
            uri = URI.create(endpoint.trim());
        } catch (IllegalArgumentException e) {
            uri = null;  // e.g. an unexpanded "${MINIO_URL}" from an env file
        }
        if (uri == null || uri.getScheme() == null || uri.getHost() == null) {
            throw new IllegalStateException("storage.s3.endpoint (S3_ENDPOINT) must be a URL such as "
                    + "https://s3.example.com, not '" + endpoint + "'");
        }
        return uri;
    }

    /**
     * The endpoint, and only the checksums S3 requires: the SDK's newer default checksums (CRC32
     * headers, chunked trailers) are not supported by every S3-compatible server (older Garage and
     * SeaweedFS releases reject such uploads).
     */
    @Bean
    public S3ClientCustomizer s3CompatibleClient(@Value("${storage.s3.endpoint:}") String endpoint) {
        URI uri = endpoint(endpoint);
        return builder -> {
            if (uri != null) {
                builder.endpointOverride(uri);
            }
            builder.requestChecksumCalculation(RequestChecksumCalculation.WHEN_REQUIRED)
                    .responseChecksumValidation(ResponseChecksumValidation.WHEN_REQUIRED);
        };
    }
}
