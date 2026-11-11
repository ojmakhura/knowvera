package bw.co.roguesystems.ai.schema;

import java.util.List;

/**
 * A document type the classification step can choose, with the fields that identify it.
 */
public record DocumentTypeSpec(String name, List<FieldSpec> fields) {
}
