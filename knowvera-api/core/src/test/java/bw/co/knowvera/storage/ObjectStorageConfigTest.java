package bw.co.knowvera.storage;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import io.awspring.cloud.autoconfigure.core.AwsAutoConfiguration;
import io.awspring.cloud.autoconfigure.core.CredentialsProviderAutoConfiguration;
import io.awspring.cloud.autoconfigure.core.RegionProviderAutoConfiguration;
import io.awspring.cloud.autoconfigure.s3.S3AutoConfiguration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

/** The S3Client Spring Cloud AWS builds from the properties in application.yml. */
class ObjectStorageConfigTest {

    private final ApplicationContextRunner context = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(AwsAutoConfiguration.class, CredentialsProviderAutoConfiguration.class,
                    RegionProviderAutoConfiguration.class, S3AutoConfiguration.class))
            .withUserConfiguration(ObjectStorageConfig.class)
            .withPropertyValues(
                    "spring.cloud.aws.credentials.access-key=test-access",
                    "spring.cloud.aws.credentials.secret-key=test-secret",
                    "spring.cloud.aws.region.static=garage",
                    "storage.s3.endpoint=https://s3.example.com",
                    "spring.cloud.aws.s3.path-style-access-enabled=true");

    @Test
    void clientUsesTheConfiguredServerRegionAndCustomizer() {
        context.run(app -> {
            assertThat(app).hasNotFailed();
            S3Client client = app.getBean(S3Client.class);
            assertThat(client.serviceClientConfiguration().endpointOverride()).contains(URI.create("https://s3.example.com"));
            assertThat(client.serviceClientConfiguration().region()).isEqualTo(Region.of("garage"));
            assertThat(app).hasBean("s3CompatibleClient");
        });
    }

    @Test
    void blankEndpointMeansAwsS3AndStillStarts() {
        context.withPropertyValues("storage.s3.endpoint=").run(app -> {
            assertThat(app).hasNotFailed();
            assertThat(app.getBean(S3Client.class).serviceClientConfiguration().endpointOverride()).isEmpty();
        });
    }

    @Test
    void endpointWithoutSchemeIsAClearError() {
        context.withPropertyValues("storage.s3.endpoint=s3.example.com").run(app -> {
            assertThat(app).hasFailed();
            assertThat(app.getStartupFailure()).rootCause().hasMessageContaining("S3_ENDPOINT");
        });
    }

    @Test
    void unexpandedVariableIsAClearError() {
        org.assertj.core.api.Assertions.assertThatThrownBy(() -> ObjectStorageConfig.endpoint("${MINIO_URL}"))
                .hasMessageContaining("S3_ENDPOINT").hasMessageContaining("${MINIO_URL}");
    }
}
