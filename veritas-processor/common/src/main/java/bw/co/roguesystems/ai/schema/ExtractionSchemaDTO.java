package bw.co.roguesystems.ai.schema;

import java.time.Instant;
import java.util.List;

/**
 * A named set of fields to extract from text. A schema is not tied to what the text is: any job
 * can name it, for a document or for text it brings itself. Saving a schema adds its next version;
 * jobs name a schema by its code and use its latest version.
 *
 * @param code        the schema's code
 * @param version     set by the processor
 * @param name        a label for the schema
 * @param description free text
 * @param fields      the fields to extract
 */
public record ExtractionSchemaDTO(
        String code,
        Integer version,
        String name,
        String description,
        List<FieldSpec> fields,
        Instant createdAt,
        String createdBy) {
}
