package bw.co.kyvera.config;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.support.RestClientHttpServiceGroupConfigurer;
import bw.co.kyvera.extractor.LmStudioExtractor;
import bw.co.kyvera.extractor.OllamaIntegration;

import org.springframework.web.service.registry.ImportHttpServices;

@Configuration
@ImportHttpServices(group = "lmstudio", types = {
        LmStudioExtractor.class,
})
@ImportHttpServices(group = "ollama", types = {
        OllamaIntegration.class,
})
public class HttpClientConfig {

    @Value("${app.lmstudio.base-url}")
    private String lmstudioUrl;

    @Value("${app.ollama.base-url}")
    private String ollamaUrl;

    @Bean
    RestClientHttpServiceGroupConfigurer groupConfigurer() {
        return groups -> {

            SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
            factory.setConnectTimeout((int) Duration.ofSeconds(5).toMillis());
            factory.setReadTimeout((int) Duration.ofMinutes(10).toMillis());

            groups.filterByName("lmstudio")
                    .forEachClient((group, builder) -> builder
                            .baseUrl(lmstudioUrl)
                            .requestFactory(factory)
                            .build());

            groups.filterByName("ollama")
                    .forEachClient((group, builder) -> builder
                            .baseUrl(ollamaUrl)
                            .requestFactory(factory)
                            .build());
        };
    }

    // private String getCurrentBearerToken() {
    // Authentication authentication =
    // SecurityContextHolder.getContext().getAuthentication();
    // if (authentication != null && authentication.getPrincipal() instanceof Jwt
    // jwt) {
    // return "Bearer " + jwt.getTokenValue();
    // }
    // return null;
    // }
}
