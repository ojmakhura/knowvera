package bw.co.knowvera.veritas;

import java.time.Instant;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.jspecify.annotations.Nullable;
import org.springframework.web.client.RestClient;

import bw.co.roguesystems.ai.profile.ProcessingProfileDTO;
import bw.co.roguesystems.ai.profile.ProfileSummary;
import bw.co.roguesystems.ai.processor.DataProcessorDTO;
import bw.co.roguesystems.ai.processor.field.ExpectedFieldDTO;

/**
 * veritas-processor's profile and data processor API, called with this API's own client credentials (the
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

    /** Every data processor (an empty list when there is none). */
    public List<DataProcessorDTO> processors() {
        List<DataProcessorDTO> found = api.get().uri("/data-processors").headers(h -> h.setBearerAuth(token()))
                .retrieve().body(new ParameterizedTypeReference<List<DataProcessorDTO>>() {
                });
        return found == null ? List.of() : found;
    }

    /** The data processor with this id, or null when there is none. */
    public @Nullable DataProcessorDTO processor(String id) {
        return processors().stream().filter(processor -> id.equalsIgnoreCase(processor.getId())).findFirst()
                .orElse(null);
    }

    /** The data processor with this code, or null when there is none. */
    public @Nullable DataProcessorDTO processorByCode(String code) {
        return processors().stream().filter(processor -> code.equalsIgnoreCase(processor.getCode())).findFirst()
                .orElse(null);
    }

    /** Saves a data processor (a new one when it has no id). */
    public DataProcessorDTO saveProcessor(DataProcessorDTO processor) {
        return api.post().uri("/data-processors").headers(h -> h.setBearerAuth(token()))
                .contentType(MediaType.APPLICATION_JSON).body(processor).retrieve().body(DataProcessorDTO.class);
    }

    /** Adds the expected fields to the data processor, or updates those that carry an id. */
    public DataProcessorDTO saveExpectedFields(String processorId, Collection<ExpectedFieldDTO> fields) {
        return api.post().uri("/data-processors/{id}/expected-field", processorId)
                .headers(h -> h.setBearerAuth(token())).contentType(MediaType.APPLICATION_JSON).body(fields)
                .retrieve().body(DataProcessorDTO.class);
    }

    public void removeExpectedField(String fieldId) {
        api.delete().uri("/expected-fields/{id}", fieldId).headers(h -> h.setBearerAuth(token())).retrieve()
                .toBodilessEntity();
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
