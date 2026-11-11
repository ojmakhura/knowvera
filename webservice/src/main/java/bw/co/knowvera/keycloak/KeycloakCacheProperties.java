package bw.co.knowvera.keycloak;

import java.time.Duration;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * How long Keycloak admin API results are cached (see {@link KeycloakCache}). Zero turns a cache off.
 *
 * @param userTtl         a user's account, roles and organisation; changes made through this API evict them at
 *                        once, changes made in the Keycloak console show after this
 * @param roleTtl         the realm's and clients' role names
 * @param organisationTtl which Keycloak Organization an organisation is
 */
@ConfigurationProperties("app.keycloak.cache")
public record KeycloakCacheProperties(Duration userTtl, Duration roleTtl, Duration organisationTtl) {

    public KeycloakCacheProperties {
        userTtl = userTtl == null ? Duration.ofMinutes(1) : userTtl;
        roleTtl = roleTtl == null ? Duration.ofMinutes(10) : roleTtl;
        organisationTtl = organisationTtl == null ? Duration.ofMinutes(10) : organisationTtl;
    }
}
