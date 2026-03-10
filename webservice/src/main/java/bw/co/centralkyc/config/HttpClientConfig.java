package bw.co.centralkyc.config;

 import org.springframework.beans.factory.annotation.Value;
 import org.springframework.context.annotation.Bean;
 import org.springframework.context.annotation.Configuration;
 import org.springframework.security.core.Authentication;
 import org.springframework.security.core.context.SecurityContextHolder;
 import org.springframework.security.oauth2.jwt.Jwt;
 import org.springframework.web.client.support.RestClientHttpServiceGroupConfigurer;
import bw.co.centralkyc.extractor.LmStudioExtractor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.service.registry.ImportHttpServices;

@Configuration
@ImportHttpServices({
        LmStudioExtractor.class,
})
public class HttpClientConfig {

     @Bean
     RestClientHttpServiceGroupConfigurer groupConfigurer() {
         return groups -> {
             groups.forEachClient((group, builder) -> builder
//                     .baseUrl(url)
                     .defaultHeader("Authorization", getCurrentBearerToken())
                     .build());
         };
     }


     private String getCurrentBearerToken() {
         Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
         if (authentication != null && authentication.getPrincipal() instanceof Jwt jwt) {
             return "Bearer " + jwt.getTokenValue();
         }
         return null;
     }
}
