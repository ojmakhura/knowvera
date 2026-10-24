package bw.co.knowvera.security;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Expiry;

/**
 * Two-level cache for authorization data: an in-process Caffeine cache (L1) in front of Redis (L2).
 * <p>
 * L1 answers repeat lookups without a network hop; Redis shares results between instances. Every entry
 * carries its own TTL (e.g. never past the token's expiry). L1 keeps an entry for at most
 * {@code app.keycloak.authz.local-cache-ttl} so a change seen through Redis reaches every instance
 * quickly. Redis failures are logged and treated as cache misses, so authorization still works (by asking
 * Keycloak) when Redis is down.
 */
@Component
public class AuthzCache {

    private static final Logger logger = LoggerFactory.getLogger(AuthzCache.class);

    private record Entry(String value, long expiresAtNanos) {
    }

    private final StringRedisTemplate redis;
    private final Duration localTtl;
    private final Cache<String, Entry> local;

    public AuthzCache(StringRedisTemplate redis, KeycloakAuthzProperties properties) {
        this.redis = redis;
        this.localTtl = properties.localCacheTtl();
        this.local = Caffeine.newBuilder()
                .maximumSize(properties.localCacheMaxSize())
                .expireAfter(new Expiry<String, Entry>() {
                    @Override
                    public long expireAfterCreate(String key, Entry entry, long currentTime) {
                        return Math.max(0, entry.expiresAtNanos() - currentTime);
                    }

                    @Override
                    public long expireAfterUpdate(String key, Entry entry, long currentTime, long currentDuration) {
                        return expireAfterCreate(key, entry, currentTime);
                    }

                    @Override
                    public long expireAfterRead(String key, Entry entry, long currentTime, long currentDuration) {
                        return currentDuration;
                    }
                })
                .build();
    }

    /** The cached value, or null on a miss in both levels. */
    public String get(String key) {

        Entry entry = local.getIfPresent(key);
        if (entry != null) {
            return entry.value();
        }

        try {
            String value = redis.opsForValue().get(key);
            if (value != null) {
                Long remainingMs = redis.getExpire(key, TimeUnit.MILLISECONDS);
                Duration remaining = remainingMs != null && remainingMs > 0 ? Duration.ofMillis(remainingMs) : localTtl;
                putLocal(key, value, remaining);
            }
            return value;
        } catch (RuntimeException e) {
            logger.warn("Authorization cache read from Redis failed for {}", key, e);
            return null;
        }
    }

    public void put(String key, String value, Duration ttl) {

        if (ttl == null || ttl.isNegative() || ttl.isZero()) {
            return;
        }

        putLocal(key, value, ttl);

        try {
            redis.opsForValue().set(key, value, ttl);
        } catch (RuntimeException e) {
            logger.warn("Authorization cache write to Redis failed for {}", key, e);
        }
    }

    /**
     * Removes the entry from Redis and this instance's L1. Other instances drop their L1 copy within
     * {@code app.keycloak.authz.local-cache-ttl}.
     */
    public void evict(String key) {

        local.invalidate(key);
        try {
            redis.delete(key);
        } catch (RuntimeException e) {
            logger.warn("Authorization cache eviction from Redis failed for {}", key, e);
        }
    }

    private void putLocal(String key, String value, Duration ttl) {
        Duration effective = ttl.compareTo(localTtl) < 0 ? ttl : localTtl;
        local.put(key, new Entry(value, System.nanoTime() + effective.toNanos()));
    }

    /** For tests and diagnostics. */
    long localSize() {
        local.cleanUp();
        return local.estimatedSize();
    }

    /** Remaining TTL from an absolute expiry, for callers that know when a token expires. */
    public static Duration until(Instant expiresAt, Duration max) {
        if (expiresAt == null) {
            return max;
        }
        Duration remaining = Duration.between(Instant.now(), expiresAt);
        return remaining.compareTo(max) < 0 ? remaining : max;
    }
}
