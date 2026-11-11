package bw.co.knowvera.config;

import java.time.Duration;
import java.util.concurrent.ThreadLocalRandom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair;

import tools.jackson.databind.DefaultTyping;
import tools.jackson.databind.JavaType;
import tools.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import tools.jackson.databind.jsontype.PolymorphicTypeValidator;
import tools.jackson.databind.jsontype.impl.DefaultTypeResolverBuilder;

@Configuration
@EnableCaching
public class RedisCacheConfig implements CachingConfigurer {

	private static final Logger LOGGER = LoggerFactory.getLogger(RedisCacheConfig.class);

	/**
	 * A cache failure (Redis down, an entry that cannot be read back) is logged and treated as a miss, so the
	 * service method still runs against the database instead of failing the call. A failed eviction is logged
	 * louder: the entry may stay stale until its TTL.
	 */
	@Override
	public CacheErrorHandler errorHandler() {
		return new CacheErrorHandler() {
			@Override
			public void handleCacheGetError(RuntimeException e, Cache cache, Object key) {
				LOGGER.warn("Cache {} read of {} failed, using the database: {}", cache.getName(), key, e.toString());
			}

			@Override
			public void handleCachePutError(RuntimeException e, Cache cache, Object key, Object value) {
				LOGGER.warn("Cache {} write of {} failed: {}", cache.getName(), key, e.toString());
			}

			@Override
			public void handleCacheEvictError(RuntimeException e, Cache cache, Object key) {
				LOGGER.error("Cache {} eviction of {} failed; it may be stale until its TTL: {}", cache.getName(), key,
						e.toString());
			}

			@Override
			public void handleCacheClearError(RuntimeException e, Cache cache) {
				LOGGER.error("Cache {} clear failed; it may be stale until its TTL: {}", cache.getName(), e.toString());
			}
		};
	}

	/**
	 * Prefix of every cache key. Bump the version whenever the value format changes, so entries written
	 * in an older format (e.g. JDK serialization) are never read back; they expire on their own TTL.
	 */
	static final String KEY_PREFIX = "knowvera:";

	/** Type hints are written as this property (and as a wrapper array for collections). */
	static final String TYPE_PROPERTY = "@class";

	@Bean
	public RedisCacheManager cacheManager(RedisConnectionFactory factory,
			@Value("${spring.cache.redis.time-to-live:PT1H}") Duration timeToLive) {

		RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
				.computePrefixWith(cacheName -> KEY_PREFIX + cacheName + "::")
				.entryTtl((key, value) -> {
					long baseSeconds = timeToLive.toSeconds(); // 10 minutes
					long jitter = ThreadLocalRandom.current().nextLong(0, 120); // up to +2 minutes
					return Duration.ofSeconds(baseSeconds + jitter);
				})
				.serializeValuesWith(SerializationPair.fromSerializer(cacheValueSerializer()));

		return RedisCacheManager.builder(factory)
				.cacheDefaults(config)
				.build();
	}

	/**
	 * JSON with type hints so cached DTOs come back as DTOs rather than maps.
	 * <p>
	 * Jackson's NON_FINAL typing skips final classes, which includes the JDK's immutable lists returned by
	 * {@code List.of(..)} and {@code Stream.toList()}: those would be written without a type hint and could
	 * not be read back ("need String ... that contains type id"). Collections and maps therefore always get
	 * a type hint, and so do records (final too, e.g. OrganisationListDTO), which services return directly.
	 */
	static GenericJacksonJsonRedisSerializer cacheValueSerializer() {

		PolymorphicTypeValidator validator = BasicPolymorphicTypeValidator.builder()
				.allowIfBaseType(Object.class)
				.build();

		DefaultTypeResolverBuilder typing = new DefaultTypeResolverBuilder(validator, DefaultTyping.NON_FINAL,
				TYPE_PROPERTY) {
			@Override
			public boolean useForType(JavaType type) {
				return !type.isPrimitive()
						&& (super.useForType(type) || type.isCollectionLikeType() || type.isMapLikeType()
								|| type.getRawClass().isRecord());
			}
		};

		return GenericJacksonJsonRedisSerializer.builder()
				.typePropertyName(TYPE_PROPERTY)
				.enableSpringCacheNullValueSupport(TYPE_PROPERTY) // handles Spring Cache's NullValue when nulls are cached
				.customize(mapper -> mapper.setDefaultTyping(typing))
				.build();
	}
}
