package bw.co.roguesystems.ai.schema;

import java.util.List;

/**
 * The latest version of a schema, for lists.
 */
public record SchemaSummary(String code, int version, String name, String description, List<String> fields) {
}
