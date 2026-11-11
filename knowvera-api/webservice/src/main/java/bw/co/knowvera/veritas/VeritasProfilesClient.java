package bw.co.knowvera.veritas;

import java.time.Instant;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import bw.co.roguesystems.ai.profile.ProcessingProfileDTO;
import bw.co.roguesystems.ai.profile.ProfileSummary;

/**
 * veritas-processor's profile API, called with this API's own client credentials (the
 * service account of app.authz.client needs the veritas-processor client role profiles-admin, and
 * veritas-processor in its token audience).
 */
@Component
public class VeritasProfilesClient {

    private final RestClient api;
    private final RestClient keycloak;
    private final String clientId;
    private final String clientSecret;

    private String token;
    private Instant tokenExpiry = Instant.EPOCH;

    public VeritasProfilesClient(
            @Value("${app.veritas.base-url}") String baseUrl,
            @Value("${app.keycloak.authz.server-url}") String authServerUrl,
            @Value("${app.keycloak.authz.realm}") String realm,
            @Value("${app.veritas.client-id:${app.authz.client}}") String clientId,
            @Value("${app.veritas.client-secret:${app.authz.client-secret}}") String clientSecret) {
        this.api = RestClient.builder().baseUrl(baseUrl).build();
        this.keycloak = RestClient.builder()
                .baseUrl(authServerUrl + "/realms/" + realm + "/protocol/openid-connect/token").build();
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    public List<ProfileSummary> profiles() {
        return api.get().uri("/profiles").headers(h -> h.setBearerAuth(token())).retrieve()
                .body(new ParameterizedTypeReference<List<ProfileSummary>>() {
                });
    }

    /** Saves the profile as the next version of {@code code} (unchanged profiles keep their version). */
    public ProcessingProfileDTO save(String code, ProcessingProfileDTO profile) {
        return api.put().uri("/profiles/{code}", code).headers(h -> h.setBearerAuth(token()))
                .contentType(MediaType.APPLICATION_JSON).body(profile).retrieve().body(ProcessingProfileDTO.class);
    }

    private synchronized String token() {
        if (token == null || Instant.now().isAfter(tokenExpiry)) {
            MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
            form.add("grant_type", "client_credentials");
            form.add("client_id", clientId);
            form.add("client_secret", clientSecret);
            TokenResponse response = keycloak.post().contentType(MediaType.APPLICATION_FORM_URLENCODED).body(form)
                    .retrieve().body(TokenResponse.class);
            if (response == null || response.access_token() == null) {
                throw new IllegalStateException("Keycloak returned no token for client " + clientId);
            }
            token = response.access_token();
            tokenExpiry = Instant.now().plusSeconds(Math.max(0, response.expires_in() - 30));
        }
        return token;
    }

    record TokenResponse(String access_token, long expires_in) {
    }
}
