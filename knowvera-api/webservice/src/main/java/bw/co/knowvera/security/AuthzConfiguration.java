package bw.co.knowvera.security;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import bw.co.knowvera.keycloak.KeycloakCacheProperties;

/** Binds the authorization and Keycloak cache settings (app.keycloak.authz.*, app.keycloak.cache.*). */
@Configuration
@EnableConfigurationProperties({ KeycloakAuthzProperties.class, KeycloakCacheProperties.class })
public class AuthzConfiguration {
}
