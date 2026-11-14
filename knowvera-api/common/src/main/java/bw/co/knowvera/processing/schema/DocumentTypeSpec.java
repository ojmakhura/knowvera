package bw.co.knowvera.processing.schema;

import java.util.List;

/**
 * A type the classification step can choose for a text, with the fields that identify it: given
 * here, or taken from the document type.
 *
 * @param name          the type's name, as the prompts show it
 * @param fields          the fields that identify the type (null or empty: the expected fields of
 *                        {@code documentTypeId})
 * @param documentTypeId  the id of the document type that holds the fields; null when {@code fields}
 *                        are given
 */
public record DocumentTypeSpec(String name, List<FieldSpec> fields, String documentTypeId) {

    /** A type that carries its fields itself. */
    public DocumentTypeSpec(String name, List<FieldSpec> fields) {
        this(name, fields, null);
    }
}
