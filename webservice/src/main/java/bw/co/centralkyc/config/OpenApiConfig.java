package bw.co.centralkyc.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.OAuthFlow;
import io.swagger.v3.oas.models.security.OAuthFlows;
import io.swagger.v3.oas.models.security.Scopes;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class OpenApiConfig {

    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    private String issuerUri;
    
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
            .components(
                new Components()
                    .addSecuritySchemes("keycloak",
                        new SecurityScheme()
                            .type(SecurityScheme.Type.OAUTH2)
                            .scheme("bearer")
                            .bearerFormat("JWT")
                            .flows(
                                new OAuthFlows()
                                .authorizationCode(
                                    new OAuthFlow()
                                        .authorizationUrl(issuerUri + "/protocol/openid-connect/auth")
                                        .tokenUrl(issuerUri + "/protocol/openid-connect/token")
                                        .scopes(new Scopes()
                                            .addString("openid", "OpenID Connect scope")
                                            .addString("profile", "Profile scope")
                                            .addString("email", "Email scope")
                                        )
                                )
                            )
                    )
            )
            .addSecurityItem(new SecurityRequirement().addList("keycloak"));
    }
}
