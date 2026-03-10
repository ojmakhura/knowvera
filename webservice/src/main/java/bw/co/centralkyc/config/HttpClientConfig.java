package bw.co.centralkyc.config;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.support.RestClientHttpServiceGroupConfigurer;
import bw.co.centralkyc.extractor.LmStudioExtractor;
import org.springframework.web.service.registry.ImportHttpServices;

@Configuration
@ImportHttpServices(group = "lmstudio", types = {
        LmStudioExtractor.class,
})
public class HttpClientConfig {

    @Value("${app.lmstudio.base-url}")
    private String url;

    @Bean
    RestClientHttpServiceGroupConfigurer groupConfigurer() {
        return groups -> {
            
            SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
            factory.setConnectTimeout((int) Duration.ofSeconds(5).toMillis());
            factory.setReadTimeout((int) Duration.ofSeconds(60).toMillis());

            groups.filterByName("lmstudio")
                    .forEachClient((group, builder) -> builder
                            .baseUrl(url)
                            .requestFactory(factory)
                            .build());
        };
    }

    // private String getCurrentBearerToken() {
    //     Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    //     if (authentication != null && authentication.getPrincipal() instanceof Jwt jwt) {
    //         return "Bearer " + jwt.getTokenValue();
    //     }
    //     return null;
    // }
}
