package bw.co.knowvera.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.keycloak.authorization.client.AuthzClient;
import org.keycloak.authorization.client.resource.AuthorizationResource;
import org.keycloak.representations.idm.authorization.AuthorizationRequest;
import org.keycloak.representations.idm.authorization.Permission;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.jwt.Jwt;

@ExtendWith(MockitoExtension.class)
class PermissionServiceTest {

    @Mock
    private AuthzClientProvider provider;
    @Mock
    private AuthzClient client;
    @Mock
    private AuthorizationResource authorization;
    @Mock
    private AuthzCache cache;

    private PermissionService service;

    @BeforeEach
    void setUp() {
        service = new PermissionService(provider, new KeycloakAuthzProperties("https://kc", "knowvera", "knowvera-api",
                "secret", Duration.ofMinutes(5), null, null), cache);
    }

    private static Jwt token(Map<String, Object> claims) {
        Jwt.Builder builder = Jwt.withTokenValue("token-value").header("alg", "none").subject("user-1")
                .expiresAt(Instant.now().plusSeconds(120));
        claims.forEach(builder::claim);
        return builder.build();
    }

    @Test
    void asksKeycloakOnceAndCachesResourceScopePairsUntilTheTokenExpires() {
        when(provider.get()).thenReturn(client);
        when(client.authorization("token-value")).thenReturn(authorization);
        Permission documents = new Permission("id-1", "documents", Set.of("view", "list"), null);
        Permission keycloak = new Permission("id-2", "keycloak", Set.of(), null);
        when(authorization.getPermissions(any(AuthorizationRequest.class))).thenReturn(List.of(documents, keycloak));

        Set<String> granted = service.grantedPermissions(token(Map.of()));

        assertEquals(Set.of("documents:view", "documents:list", "keycloak"), granted);
        ArgumentCaptor<AuthorizationRequest> request = ArgumentCaptor.forClass(AuthorizationRequest.class);
        verify(authorization).getPermissions(request.capture());
        assertEquals("knowvera-api", request.getValue().getAudience());
        ArgumentCaptor<Duration> ttl = ArgumentCaptor.forClass(Duration.class);
        verify(cache).put(eq(PermissionService.PERMISSIONS_KEY + PermissionService.sha256("token-value")),
                anyString(), ttl.capture());
        assertTrue(ttl.getValue().compareTo(Duration.ofSeconds(120)) <= 0, "never past the token's expiry");
    }

    @Test
    void cachedPermissionsSkipKeycloak() {
        when(cache.get(PermissionService.PERMISSIONS_KEY + PermissionService.sha256("token-value")))
                .thenReturn("documents:view kyc-records:edit-own");

        assertEquals(Set.of("documents:view", "kyc-records:edit-own"), service.grantedPermissions(token(Map.of())));
        verifyNoInteractions(provider);
    }

    @Test
    void anRptCarriesItsPermissions() {
        Jwt rpt = token(Map.of("authorization", Map.of("permissions", List.of(
                Map.of("rsname", "individuals", "scopes", List.of("view-own")),
                Map.of("rsname", "novu")))));

        assertEquals(Set.of("individuals:view-own", "novu"), service.grantedPermissions(rpt));
        verifyNoInteractions(provider, cache);
    }

    @Test
    void keycloakFailuresGrantNothingAndAreNotCached() {
        when(provider.get()).thenThrow(new IllegalStateException("Keycloak down"));

        assertTrue(service.grantedPermissions(token(Map.of())).isEmpty());
        verify(cache, never()).put(anyString(), anyString(), any());
    }
}
