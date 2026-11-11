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
import bw.co.roguesystems.ai.schema.ExtractionSchemaDTO;
import bw.co.roguesystems.ai.schema.FieldSpec;

/**
 * The definitions of the expected fields, read from the document types' schemas in
 * veritas-processor, where they are kept. A schema is read once and reused for a short while
 * (app.veritas.schema-cache); when veritas-processor cannot be reached the last schema read is
 * used, and without one the fields have no definition until it can.
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
            @Value("${app.veritas.schema-cache:60s}") Duration cacheFor) {
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
                log.warn("The schema of document type {} could not be read from veritas-processor{}: {}",
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

    /** Forget what was read: a schema has just been saved. */
    public void invalidate() {
        cache.clear();
    }

    private Map<String, Definition> read(UUID documentTypeId) {
        // Plain SQL: this runs while Hibernate loads an expected field
        List<String> codes = jdbc.queryForList("SELECT CODE FROM DOCUMENT_TYPE WHERE ID = ?", String.class,
                documentTypeId);
        String code = VeritasJobFactory.schemaCode(codes.isEmpty() ? null : codes.getFirst(),
                documentTypeId.toString());
        ExtractionSchemaDTO schema = code == null ? null : client.schema(code);
        Map<String, Definition> definitions = new HashMap<>();
        if (schema != null && schema.fields() != null) {
            for (FieldSpec field : schema.fields()) {
                definitions.put(key(field.name()), new Definition(field.label(),
                        ExpectedFieldType.valueOf(field.typeOrDefault().name()), field.format(), field.many(),
                        field.mandatory()));
            }
        }
        return definitions;
    }

    static String key(String field) {
        return field.trim().toLowerCase(Locale.ROOT);
    }
}
