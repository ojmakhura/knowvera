package bw.co.knowvera.keycloak;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.UnaryOperator;

/**
 * A small in-memory cache whose entries expire after a fixed time. Values are copied on the way
 * in and out, so callers cannot change what is cached. A zero time-to-live disables caching.
 */
public class ExpiringCache<K, V> {

    private static final int SWEEP_THRESHOLD = 10_000;

    private record Entry<V>(V value, Instant expiresAt) {
    }

    private final Duration timeToLive;
    private final UnaryOperator<V> copy;
    private final Clock clock;
    private final Map<K, Entry<V>> entries = new ConcurrentHashMap<>();

    public ExpiringCache(Duration timeToLive, UnaryOperator<V> copy) {
        this(timeToLive, copy, Clock.systemUTC());
    }

    ExpiringCache(Duration timeToLive, UnaryOperator<V> copy, Clock clock) {
        this.timeToLive = timeToLive;
        this.copy = copy;
        this.clock = clock;
    }

    /** The cached value, or {@code loader}'s result (cached unless null). Loader exceptions are not cached. */
    public V get(K key, Function<K, V> loader) {

        Instant now = clock.instant();
        Entry<V> entry = entries.get(key);
        if (entry != null && entry.expiresAt().isAfter(now)) {
            return copy.apply(entry.value());
        }

        V value = loader.apply(key);
        if (value != null && !timeToLive.isZero()) {
            if (entries.size() > SWEEP_THRESHOLD) {
                entries.values().removeIf(e -> !e.expiresAt().isAfter(now));
            }
            entries.put(key, new Entry<>(copy.apply(value), now.plus(timeToLive)));
        }
        return value;
    }

    /** Forget the key, e.g. after the underlying record changed. */
    public void evict(K key) {
        if (key != null) {
            entries.remove(key);
        }
    }
}
