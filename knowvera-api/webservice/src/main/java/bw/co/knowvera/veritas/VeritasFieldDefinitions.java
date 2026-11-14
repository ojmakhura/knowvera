package bw.co.knowvera.veritas;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import bw.co.knowvera.document.type.field.ExpectedFieldType;
import bw.co.knowvera.document.type.field.FieldDefinitionSource;
import bw.co.roguesystems.ai.processor.DataProcessorDTO;
import bw.co.roguesystems.ai.processor.field.ExpectedFieldDTO;

/**
 * The definitions of the expected fields, read from the document types' data processors in
 * veritas-processor, where they are kept. A data processor is read once and reused for a short
 * while (app.veritas.processor-cache); when veritas-processor cannot be reached the last one read
 * is used, and without one the fields have no definition until it can.
 */
@Component
public class VeritasFieldDefinitions implements FieldDefinitionSource {

    private static final Logger log = LoggerFactory.getLogger(VeritasFieldDefinitions.class);

    private record Cached(Map<String, Definition> definitions, Instant readAt) {
    }

    private final VeritasProfilesClient client;
    private final JdbcTemplate jdbc;
    private final Duration cacheFor;
    private final Map<UUID, Cached> cache = new ConcurrentHashMap<>();

    public VeritasFieldDefinitions(VeritasProfilesClient client, JdbcTemplate jdbc,
            @Value("${app.veritas.processor-cache:60s}") Duration cacheFor) {
        this.client = client;
        this.jdbc = jdbc;
        this.cacheFor = cacheFor;
    }

    @Override
    public @Nullable Definition definition(UUID documentTypeId, String field) {
        Cached cached = cache.get(documentTypeId);
        if (cached == null || cached.readAt().plus(cacheFor).isBefore(Instant.now())) {
            try {
                cached = new Cached(read(documentTypeId), Instant.now());
                cache.put(documentTypeId, cached);
            } catch (RuntimeException e) {
                log.warn("The data processor of document type {} could not be read from veritas-processor{}: {}",
                        documentTypeId, cached == null ? "" : " (using the last one read)", e.getMessage());
                if (cached == null) {
                    return null;
                }
                // Do not ask again for every field of every document type while it is down
                cache.put(documentTypeId, new Cached(cached.definitions(), Instant.now()));
            }
        }
        return cached.definitions().get(key(field));
    }

    /** Records the document type's link to its data processor (it was just made or found). */
    public void link(String documentTypeId, String processorId) {
        jdbc.update("UPDATE DOCUMENT_TYPE SET DATA_PROCESSOR_ID = ? WHERE ID = ?", processorId,
                UUID.fromString(documentTypeId));
        invalidate();
    }

    /** Forget what was read: a data processor has just been saved. */
    public void invalidate() {
        cache.clear();
    }

    private Map<String, Definition> read(UUID documentTypeId) {
        // Plain SQL: this runs while Hibernate loads an expected field
        List<String> links = jdbc.queryForList("SELECT DATA_PROCESSOR_ID FROM DOCUMENT_TYPE WHERE ID = ?",
                String.class, documentTypeId);
        String processorId = links.isEmpty() || links.getFirst() == null || links.getFirst().isBlank() ? null
                : links.getFirst().trim();
        DataProcessorDTO processor = processorId == null ? null : client.processor(processorId);
        Map<String, Definition> definitions = new HashMap<>();
        if (processor != null && processor.getExpectedFields() != null) {
            for (ExpectedFieldDTO field : processor.getExpectedFields()) {
                definitions.put(key(field.getField()), new Definition(field.getFieldLabel(),
                        field.getFieldType() == null ? ExpectedFieldType.STRING
                                : ExpectedFieldType.valueOf(field.getFieldType().name()),
                        field.getFormat(), Boolean.TRUE.equals(field.getMany()),
                        Boolean.TRUE.equals(field.getMandatory())));
            }
        }
        return definitions;
    }

    static String key(String field) {
        return field.trim().toLowerCase(Locale.ROOT);
    }
}
