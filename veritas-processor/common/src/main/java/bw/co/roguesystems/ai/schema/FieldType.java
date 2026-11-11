package bw.co.roguesystems.ai.schema;

/**
 * The kind of value a field holds; extracted values are normalised to it.
 */
public enum FieldType {
    /** Free text. */
    STRING,
    /** A JSON number ("1 250,00" and "P1,250.00" become 1250.00). */
    NUMBER,
    /** An ISO 8601 date (YYYY-MM-DD) unless the field has its own format. */
    DATE,
    /** true or false ("yes"/"no" are accepted). */
    BOOLEAN,
    /** A JSON object, kept as the model returned it. */
    OBJECT
}
