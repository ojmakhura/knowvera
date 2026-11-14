package bw.co.knowvera.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class AuthzCacheTest {

    @Mock
    private StringRedisTemplate redis;
    @Mock
    private ValueOperations<String, String> values;

    private AuthzCache cache;

    @BeforeEach
    void setUp() {
        when(redis.opsForValue()).thenReturn(values);
        cache = new AuthzCache(redis, new KeycloakAuthzProperties(null, null, null, null, null,
                Duration.ofMinutes(1), 100L));
    }

    @Test
    void putWritesBothLevelsAndTheLocalCopyAnswersRepeatReads() {
        cache.put("k", "v", Duration.ofMinutes(5));

        assertEquals("v", cache.get("k"));
        assertEquals("v", cache.get("k"));
        verify(values).set("k", "v", Duration.ofMinutes(5));
        verify(values, never()).get(anyString());
    }

    @Test
    void aMissLocallyIsAnsweredFromRedisAndKeptLocally() {
        when(values.get("k")).thenReturn("from-redis");
        when(redis.getExpire("k", TimeUnit.MILLISECONDS)).thenReturn(30_000L);

        assertEquals("from-redis", cache.get("k"));
        assertEquals("from-redis", cache.get("k"));
        verify(values, times(1)).get("k");
    }

    @Test
    void redisFailuresAreMissesNotErrors() {
        when(values.get("k")).thenThrow(new RedisConnectionFailureException("down"));
        org.mockito.Mockito.doThrow(new RedisConnectionFailureException("down")).when(values)
                .set(anyString(), anyString(), any(Duration.class));

        assertNull(cache.get("k"));
        cache.put("other", "v", Duration.ofMinutes(1));
        assertEquals("v", cache.get("other"), "still cached in-process");
    }

    @Test
    void evictRemovesBothLevels() {
        cache.put("k", "v", Duration.ofMinutes(5));
        cache.evict("k");

        verify(redis).delete("k");
        assertNull(cache.get("k"));
    }

    @Test
    void zeroOrNegativeTtlIsNotCached() {
        cache.put("k", "v", Duration.ZERO);
        cache.put("k", "v", Duration.ofSeconds(-1));
        verify(values, never()).set(eq("k"), anyString(), any(Duration.class));
    }
}
