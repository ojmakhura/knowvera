package bw.co.roguesystems.ai.schema;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

/**
 * A field of a schema.
 */
@Embeddable
public class SchemaField {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "label")
    private String label;

    @Enumerated(EnumType.STRING)
    @Column(name = "field_type", nullable = false, length = 20)
    private FieldType type = FieldType.STRING;

    @Column(name = "format")
    private String format;

    @Column(name = "many", nullable = false)
    private boolean many;

    @Column(name = "mandatory", nullable = false)
    private boolean mandatory;

    protected SchemaField() {
    }

    public SchemaField(FieldSpec field) {
        this.name = field.name();
        this.label = field.label();
        this.type = field.typeOrDefault();
        this.format = field.format();
        this.many = field.many();
        this.mandatory = field.mandatory();
    }

    public FieldSpec toSpec() {
        return new FieldSpec(name, label, type, format, many, mandatory);
    }

    public String getName() {
        return name;
    }
}
