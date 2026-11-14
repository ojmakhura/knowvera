package bw.co.roguesystems.ai.utils;

import java.util.UUID;

import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Component;

import bw.co.roguesystems.ai.processor.DataProcessor;

@Component
public class MappingUtils {
    public String uuidToString(UUID value) {
        return value != null ? value.toString() : null;
    }

    @Nullable
    public UUID stringToUuid(String value) {
        return (value != null && !value.isEmpty()) ? UUID.fromString(value) : null;
    }

    String map(DataProcessor dataProcessor) {
        return dataProcessor != null ? dataProcessor.getName() : null;
    }
}
