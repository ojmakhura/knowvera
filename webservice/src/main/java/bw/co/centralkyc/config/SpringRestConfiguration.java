package bw.co.centralkyc.config;

import org.springframework.boot.jackson.autoconfigure.JsonMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.client.RestTemplate;

import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.SerializationFeature;

@Configuration
@EnableAsync
public class SpringRestConfiguration {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public JsonMapperBuilderCustomizer jacksonCustomizer() {
        return builder -> builder
            .enable(SerializationFeature.INDENT_OUTPUT) // Pretty print JSON
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES); // Ignore unknown fields
    }

}
