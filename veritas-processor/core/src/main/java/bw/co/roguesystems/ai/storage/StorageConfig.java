package bw.co.roguesystems.ai.storage;

import java.net.URI;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.awspring.cloud.autoconfigure.s3.S3ClientCustomizer;
import software.amazon.awssdk.core.checksums.RequestChecksumCalculation;
import software.amazon.awssdk.core.checksums.ResponseChecksumValidation;

/**
 * Adjusts the S3Client Spring Cloud AWS builds from spring.cloud.aws.* for an S3-compatible server
 * (the same settings as knowvera-api, which uploads the documents).
 */
@Configuration
public class StorageConfig {

    /**
     * The server's S3 API (veritas.storage.endpoint), applied only when set: blank means AWS S3.
     */
    static URI endpoint(String endpoint) {
        if (StringUtils.isBlank(endpoint)) {
            return null;
        }
        URI uri;
        try {
            uri = URI.create(endpoint.trim());
        } catch (IllegalArgumentException e) {
            uri = null;
        }
        if (uri == null || uri.getScheme() == null || uri.getHost() == null) {
            throw new IllegalStateException("veritas.storage.endpoint (S3_ENDPOINT) must be a URL such as "
                    + "https://s3.example.com, not '" + endpoint + "'");
        }
        return uri;
    }

    /** The endpoint, and only the checksums S3 requires (not every S3-compatible server supports the rest). */
    @Bean
    public S3ClientCustomizer s3CompatibleClient(@Value("${veritas.storage.endpoint:}") String endpoint) {
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
