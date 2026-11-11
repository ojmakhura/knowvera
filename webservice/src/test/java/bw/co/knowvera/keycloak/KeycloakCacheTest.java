package bw.co.knowvera.keycloak;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Duration;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import bw.co.knowvera.security.AuthzCache;
import bw.co.knowvera.user.UserDTO;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.json.JsonMapper;

@ExtendWith(MockitoExtension.class)
class KeycloakCacheTest {

    private static final TypeReference<UserDTO> USER = new TypeReference<>() { };

    @Mock
    private AuthzCache authzCache;

    private KeycloakCache cache;

    @BeforeEach
    void setUp() {
        cache = new KeycloakCache(authzCache, JsonMapper.builder().build());
    }

    private static UserDTO user() {
        UserDTO user = new UserDTO();
        user.setUserId("u1");
        user.setUsername("ann");
        user.setOrganisationId("org-1");
        user.setRoles(new LinkedHashSet<>(Set.of("APPLICANT")));
        return user;
    }

    @Test
    void aMissLoadsAndStoresTheValueAsJsonUnderTheKnowveraPrefix() {
        UserDTO loaded = cache.get("user:u1", Duration.ofMinutes(1), USER, KeycloakCacheTest::user);

        assertEquals("ann", loaded.getUsername());
        ArgumentCaptor<String> json = ArgumentCaptor.forClass(String.class);
        verify(authzCache).put(eq("knowvera:keycloak:user:u1"), json.capture(), eq(Duration.ofMinutes(1)));
        assertEquals(true, json.getValue().contains("\"organisationId\":\"org-1\""));
    }

    @Test
    void aHitReturnsAFreshCopyWithoutCallingTheLoader() throws Exception {
        String json = JsonMapper.builder().build().writeValueAsString(user());
        when(authzCache.get("knowvera:keycloak:user:u1")).thenReturn(json);
        AtomicInteger loads = new AtomicInteger();

        UserDTO first = cache.get("user:u1", Duration.ofMinutes(1), USER, () -> { loads.incrementAndGet(); return null; });
        UserDTO second = cache.get("user:u1", Duration.ofMinutes(1), USER, () -> { loads.incrementAndGet(); return null; });

        assertEquals(0, loads.get());
        assertEquals(Set.of("APPLICANT"), first.getRoles());
        assertNotSame(first, second);
        first.getRoles().add("SUPER_ADMIN");
        assertEquals(Set.of("APPLICANT"), second.getRoles(), "callers cannot change what is cached");
    }

    @Test
    void nullResultsAndZeroTtlAreNotCached() {
        assertNull(cache.get("organisation:X", Duration.ofMinutes(10), new TypeReference<String>() { }, () -> null));
        cache.get("user:u1", Duration.ZERO, USER, KeycloakCacheTest::user);

        verify(authzCache, never()).put(anyString(), anyString(), any());
        verify(authzCache, never()).get("knowvera:keycloak:user:u1");
    }

    @Test
    void loaderFailuresPropagateAndAreNotCached() {
        assertThrows(IllegalStateException.class, () -> cache.get("user:u1", Duration.ofMinutes(1), USER,
                () -> { throw new IllegalStateException("Keycloak down"); }));
        verify(authzCache, never()).put(anyString(), anyString(), any());
    }

    @Test
    void unreadableEntriesAreTreatedAsMisses() {
        when(authzCache.get("knowvera:keycloak:user:u1")).thenReturn("{not json");

        assertEquals("ann", cache.get("user:u1", Duration.ofMinutes(1), USER, KeycloakCacheTest::user).getUsername());
    }

    @Test
    void evictRemovesEveryKey() {
        cache.evict("user:u1", "user:u1:effective-roles");
        verify(authzCache).evict("knowvera:keycloak:user:u1");
        verify(authzCache).evict("knowvera:keycloak:user:u1:effective-roles");
    }
}
