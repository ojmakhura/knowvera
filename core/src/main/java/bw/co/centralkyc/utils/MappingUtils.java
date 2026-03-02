package bw.co.centralkyc.utils;

import java.util.UUID;

import org.springframework.stereotype.Component;

@Component
public class MappingUtils {
    public String uuidToString(UUID value) {
        return value != null ? value.toString() : null;
    }

    public java.util.UUID stringToUuid(String value) {
        return (value != null && !value.isEmpty()) ? UUID.fromString(value) : null;
    }
}
