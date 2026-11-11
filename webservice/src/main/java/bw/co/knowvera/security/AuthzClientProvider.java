package bw.co.knowvera.security;

import java.util.Map;

import org.keycloak.authorization.client.AuthzClient;
import org.keycloak.authorization.client.Configuration;
import org.springframework.stereotype.Component;

/**
 * Lazily creates the {@link AuthzClient} for the API client (app.keycloak.authz.*): creating it fetches
 * Keycloak's UMA discovery document, and the application should still start when Keycloak is briefly
 * unavailable.
 */
@Component
public class AuthzClientProvider {

    private final KeycloakAuthzProperties properties;

    private volatile AuthzClient client;

    public AuthzClientProvider(KeycloakAuthzProperties properties) {
        this.properties = properties;
    }

    public AuthzClient get() {
        AuthzClient current = client;
        if (current == null) {
            synchronized (this) {
                current = client;
                if (current == null) {
                    Configuration configuration = new Configuration(
                            properties.serverUrl(),
                            properties.realm(),
                            properties.clientId(),
                            Map.of("secret", properties.clientSecret()),
                            null);
                    current = AuthzClient.create(configuration);
                    client = current;
                }
            }
        }
        return current;
    }
}
