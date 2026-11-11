package bw.co.knowvera.keycloak;

import java.time.Duration;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import bw.co.knowvera.security.AuthzCache;
import tools.jackson.core.JacksonException;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.json.JsonMapper;

/**
 * Caches Keycloak admin API results in the two-level authorization cache ({@link AuthzCache}: Caffeine in
 * front of Redis), so endpoints that resolve a user, their roles or role lists on every call do not each call
 * Keycloak, and every instance shares (and evicts) the same entries.
 * <p>
 * Values are stored as JSON, so callers always get their own copy. Loader failures are not cached, and
 * neither are null results. A cache read or write that fails is logged and treated as a miss.
 */
@Component
public class KeycloakCache {

    private static final Logger LOGGER = LoggerFactory.getLogger(KeycloakCache.class);

    static final String KEY_PREFIX = "knowvera:keycloak:";

    private final AuthzCache cache;
    private final JsonMapper json;

    public KeycloakCache(AuthzCache cache, JsonMapper json) {
        this.cache = cache;
        this.json = json;
    }

    /** The cached value of {@code key}, else {@code loader}'s result, cached for {@code ttl}. */
    public <T> T get(String key, Duration ttl, TypeReference<T> type, Supplier<T> loader) {

        String cacheKey = KEY_PREFIX + key;
        if (ttl != null && ttl.isPositive()) {
            String cached = cache.get(cacheKey);
            if (cached != null) {
                try {
                    return json.readValue(cached, type);
                } catch (JacksonException e) {
                    LOGGER.warn("Ignoring unreadable cached Keycloak entry {}", cacheKey, e);
                }
            }
        }

        T value = loader.get();
        if (value != null && ttl != null && ttl.isPositive()) {
            try {
                cache.put(cacheKey, json.writeValueAsString(value), ttl);
            } catch (JacksonException e) {
                LOGGER.warn("Could not cache Keycloak entry {}", cacheKey, e);
            }
        }
        return value;
    }

    public void evict(String... keys) {
        for (String key : keys) {
            cache.evict(KEY_PREFIX + key);
        }
    }
}
