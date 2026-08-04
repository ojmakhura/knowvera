package bw.co.kyvera.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jackson.autoconfigure.JsonMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;

import co.novu.Novu;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.SerializationFeature;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Configuration
@EnableAsync
public class SpringRestConfiguration {

    @Value("${app.novu.base-url}")
    private String novuApiUrl;

    @Value("${app.novu.api-key}")
    private String novuApiKey;
    
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public RestClient restClientBuilder() {
        return RestClient.builder().build();
    }

    @Bean
    public JsonMapperBuilderCustomizer jacksonCustomizer() {
        return builder -> builder
            .enable(SerializationFeature.INDENT_OUTPUT) // Pretty print JSON
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES); // Ignore unknown fields
    }

    @Bean(name = "virtualThreadExecutor")
    public Executor virtualThreadExecutor() {
        return Executors.newVirtualThreadPerTaskExecutor();
    }

    @Bean
    public Novu novuSdk() {

        Novu sdk = Novu.builder()
                .serverURL(novuApiUrl)
                .secretKey(novuApiKey)
            .build();

        return sdk;
    }
}
