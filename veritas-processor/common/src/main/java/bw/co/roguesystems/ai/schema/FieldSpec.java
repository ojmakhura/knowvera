package bw.co.roguesystems.ai.schema;

/**
 * A field to extract from a document.
 *
 * @param name      the JSON key in the result
 * @param label     what the field is, for the model (null: the name says it)
 * @param type      the kind of value (null: STRING)
 * @param format    expected format, e.g. "YYYY-MM-DD" or "9 digits" (null: the type's own)
 * @param many      whether the field holds a list of values (absent: false)
 * @param mandatory whether the text is expected to hold it (absent: false)
 */
public record FieldSpec(String name, String label, FieldType type, String format, Boolean many, Boolean mandatory) {

    /** Senders may leave out the flags. */
    public FieldSpec {
        many = Boolean.TRUE.equals(many);
        mandatory = Boolean.TRUE.equals(mandatory);
    }

    /** A single, optional text field. */
    public static FieldSpec text(String name) {
        return new FieldSpec(name, null, FieldType.STRING, null, false, false);
    }

    public FieldType typeOrDefault() {
        return type == null ? FieldType.STRING : type;
    }
}
