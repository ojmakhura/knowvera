package bw.co.roguesystems.ai.schema;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Saves and reads schemas: named sets of fields to extract from text, whatever the text is. Saving a
 * schema adds its next version; versions are never changed, so the history of a job's fields
 * stays readable.
 */
@Service
@Transactional(readOnly = true)
public class ExtractionSchemaService {

    private final ExtractionSchemaRepository repository;

    public ExtractionSchemaService(ExtractionSchemaRepository repository) {
        this.repository = repository;
    }

    public ExtractionSchema latest(String code) {
        return repository.findFirstByCodeOrderByVersionDesc(code).orElseThrow(() -> new SchemaNotFoundException(code));
    }

    public ExtractionSchemaDTO findLatest(String code) {
        return toDTO(latest(code));
    }

    public ExtractionSchemaDTO findVersion(String code, int version) {
        return toDTO(repository.findByCodeAndVersion(code, version)
                .orElseThrow(() -> new SchemaNotFoundException(code, version)));
    }

    public List<ExtractionSchemaDTO> versions(String code) {
        List<ExtractionSchema> versions = repository.findByCodeOrderByVersionDesc(code);
        if (versions.isEmpty()) {
            throw new SchemaNotFoundException(code);
        }
        return versions.stream().map(ExtractionSchemaService::toDTO).toList();
    }

    public List<SchemaSummary> summaries() {
        return repository.findLatestVersions().stream()
                .map(s -> new SchemaSummary(s.getCode(), s.getVersion(), s.getName(), s.getDescription(),
                        s.getFields().stream().map(SchemaField::getName).toList()))
                .toList();
    }

    /**
     * Saves {@code schema} as the next version of {@code code}. Saving what the latest version
     * already holds changes nothing and returns that version.
     */
    @Transactional
    public ExtractionSchemaDTO save(String code, ExtractionSchemaDTO schema, String user) {
        List<FieldSpec> fields = validate(code, schema);
        String description = StringUtils.trimToNull(schema.description());
        ExtractionSchema latest = repository.findFirstByCodeOrderByVersionDesc(code).orElse(null);
        if (latest != null && latest.getName().equals(schema.name().trim())
                && Objects.equals(latest.getDescription(), description) && latest.fieldSpecs().equals(fields)) {
            return toDTO(latest);
        }

        ExtractionSchema entity = new ExtractionSchema();
        entity.setCode(code);
        entity.setVersion(latest == null ? 1 : latest.getVersion() + 1);
        entity.setName(schema.name().trim());
        entity.setDescription(description);
        entity.setCreatedBy(user);
        fields.forEach(field -> entity.getFields().add(new SchemaField(field)));
        return toDTO(repository.save(entity));
    }

    /** The schema's fields, tidied (trimmed, the type defaulted). */
    static List<FieldSpec> validate(String code, ExtractionSchemaDTO schema) {
        if (StringUtils.isBlank(code) || !code.matches("[A-Za-z0-9][A-Za-z0-9_-]{0,99}")) {
            throw new IllegalArgumentException("A schema code is 1-100 letters, digits, dashes and underscores, "
                    + "starting with a letter or digit");
        }
        if (schema == null || StringUtils.isBlank(schema.name())) {
            throw new IllegalArgumentException("A schema needs a name");
        }
        List<FieldSpec> fields = new ArrayList<>();
        List<String> names = new ArrayList<>();
        for (FieldSpec field : schema.fields() == null ? List.<FieldSpec>of() : schema.fields()) {
            if (field == null || StringUtils.isBlank(field.name())) {
                throw new IllegalArgumentException("Every field needs a name");
            }
            String name = field.name().trim();
            if (name.length() > 255) {
                throw new IllegalArgumentException("A field name has at most 255 characters");
            }
            if (names.contains(name.toLowerCase(Locale.ROOT))) {
                throw new IllegalArgumentException("Field " + name + " appears twice");
            }
            names.add(name.toLowerCase(Locale.ROOT));
            fields.add(new FieldSpec(name, StringUtils.trimToNull(field.label()), field.typeOrDefault(),
                    StringUtils.trimToNull(field.format()), field.many(), field.mandatory()));
        }
        return fields;
    }

    static ExtractionSchemaDTO toDTO(ExtractionSchema entity) {
        return new ExtractionSchemaDTO(entity.getCode(), entity.getVersion(), entity.getName(), entity.getDescription(),
                entity.fieldSpecs(), entity.getCreatedAt(), entity.getCreatedBy());
    }
}
