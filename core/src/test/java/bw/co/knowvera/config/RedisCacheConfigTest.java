package bw.co.knowvera.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.data.redis.serializer.RedisSerializer;

import bw.co.knowvera.organisation.OrganisationListDTO;
import bw.co.knowvera.settings.SettingsDTO;

/** What the cached service methods return must come back from Redis as the same types. */
class RedisCacheConfigTest {

    @SuppressWarnings("unchecked")
    private final RedisSerializer<Object> serializer = (RedisSerializer<Object>) (RedisSerializer<?>) RedisCacheConfig.cacheValueSerializer();

    private Object roundTrip(Object value) {
        return serializer.deserialize(serializer.serialize(value));
    }

    private static OrganisationListDTO organisation(String id) {
        return new OrganisationListDTO(id, "CODE", "Name", "REG-1", null, null, null, true, "kc-1", "", "");
    }

    @Test
    void aRecordComesBackAsTheRecord() {
        assertEquals(organisation("o1"), roundTrip(organisation("o1")));
    }

    @Test
    void aListOfRecordsComesBackAsRecords() {
        Object back = roundTrip(List.of(organisation("o1"), organisation("o2")));
        assertInstanceOf(OrganisationListDTO.class, ((List<?>) back).get(0));
        assertEquals(List.of(organisation("o1"), organisation("o2")), back);
    }

    @Test
    void aDtoComesBackAsTheDto() {
        SettingsDTO settings = new SettingsDTO();
        settings.setPlatformName("Knowvera");
        Object back = roundTrip(settings);
        assertInstanceOf(SettingsDTO.class, back);
        assertEquals("Knowvera", ((SettingsDTO) back).getPlatformName());
    }

    @Test
    void aSetOfStringsComesBackAsStrings() {
        assertEquals(Set.of("a", "b"), Set.copyOf((java.util.Collection<?>) roundTrip(Set.of("a", "b"))));
    }
}
