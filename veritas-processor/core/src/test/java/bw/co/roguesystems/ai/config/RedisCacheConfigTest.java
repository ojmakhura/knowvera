package bw.co.roguesystems.ai.config;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.data.redis.serializer.GenericJacksonJsonRedisSerializer;

import bw.co.roguesystems.ai.llm.PromptMessage;
import bw.co.roguesystems.ai.processor.DataProcessorDTO;
import bw.co.roguesystems.ai.processor.field.ExpectedFieldDTO;
import bw.co.roguesystems.ai.processor.field.ExpectedFieldType;

class RedisCacheConfigTest {

    private final GenericJacksonJsonRedisSerializer serializer = RedisCacheConfig.cacheValueSerializer();

    private static DataProcessorDTO processor() {
        ExpectedFieldDTO field = new ExpectedFieldDTO();
        field.setId("field-1");
        field.setField("idNumber");
        field.setFieldType(ExpectedFieldType.STRING);
        field.setMandatory(true);
        DataProcessorDTO processor = new DataProcessorDTO();
        processor.setId("processor-1");
        processor.setCode("NID");
        processor.setName("National Identity Card");
        processor.setCreatedAt(LocalDateTime.of(2026, 11, 14, 10, 30));
        processor.setExpectedFields(new ArrayList<>(List.of(field)));
        processor.setValidationPrompts(new ArrayList<>(List.of(new PromptMessage("system", "Check it"))));
        return processor;
    }

    @Test
    void aCachedDataProcessorComesBackAsItWas() {
        Object cached = serializer.deserialize(serializer.serialize(processor()));

        assertThat(cached).isInstanceOf(DataProcessorDTO.class);
        DataProcessorDTO processor = (DataProcessorDTO) cached;
        assertThat(processor.getCode()).isEqualTo("NID");
        assertThat(processor.getCreatedAt()).isEqualTo(LocalDateTime.of(2026, 11, 14, 10, 30));
        assertThat(processor.getExpectedFields()).singleElement()
                .satisfies(field -> assertThat(field.getFieldType()).isEqualTo(ExpectedFieldType.STRING));
        assertThat(processor.getValidationPrompts().getFirst().getContent()).isEqualTo("Check it");
    }

    @Test
    void aCachedListOfDataProcessorsComesBackAsAList() {
        Object cached = serializer.deserialize(serializer.serialize(List.of(processor(), processor())));

        assertThat(cached).isInstanceOf(List.class);
        assertThat((List<?>) cached).hasSize(2).allMatch(DataProcessorDTO.class::isInstance);
    }
}
