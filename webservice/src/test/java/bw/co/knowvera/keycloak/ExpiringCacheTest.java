package bw.co.knowvera.keycloak;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Test;

class ExpiringCacheTest {

    private static class MutableClock extends Clock {
        Instant now = Instant.parse("2026-10-12T10:00:00Z");
        @Override public ZoneOffset getZone() { return ZoneOffset.UTC; }
        @Override public Clock withZone(java.time.ZoneId zone) { return this; }
        @Override public Instant instant() { return now; }
    }

    private final MutableClock clock = new MutableClock();
    private final AtomicInteger loads = new AtomicInteger();
    private final ExpiringCache<String, List<String>> cache =
            new ExpiringCache<>(Duration.ofSeconds(60), ArrayList::new, clock);

    private List<String> load(String key) {
        loads.incrementAndGet();
        return new ArrayList<>(List.of(key));
    }

    @Test
    void reusesValuesUntilTheyExpire() {
        cache.get("u1", this::load);
        cache.get("u1", this::load);
        assertEquals(1, loads.get());

        clock.now = clock.now.plusSeconds(61);
        cache.get("u1", this::load);
        assertEquals(2, loads.get());
    }

    @Test
    void evictionForcesAReload() {
        cache.get("u1", this::load);
        cache.evict("u1");
        cache.get("u1", this::load);
        assertEquals(2, loads.get());
    }

    @Test
    void callersGetCopies() {
        List<String> first = cache.get("u1", this::load);
        first.add("changed by a caller");

        List<String> second = cache.get("u1", this::load);
        assertEquals(List.of("u1"), second);
        assertNotSame(second, cache.get("u1", this::load));
    }

    @Test
    void failuresAndMissingValuesAreNotCached() {
        assertThrows(IllegalStateException.class, () -> cache.get("u1", k -> { throw new IllegalStateException(); }));
        cache.get("u2", k -> null);
        cache.get("u1", this::load);
        cache.get("u2", this::load);
        assertEquals(2, loads.get());
    }

    @Test
    void zeroLifetimeDisablesCaching() {
        ExpiringCache<String, List<String>> off = new ExpiringCache<>(Duration.ZERO, ArrayList::new, clock);
        off.get("u1", this::load);
        off.get("u1", this::load);
        assertEquals(2, loads.get());
    }
}
