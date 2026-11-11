package bw.co.knowvera.document.type.field;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Component;

import bw.co.knowvera.document.type.field.FieldDefinitionSource.Definition;
import jakarta.persistence.PostLoad;

/**
 * Gives a loaded expected field its definition (label, type, format, many, mandatory), which is
 * not stored here (see {@link FieldDefinitionSource}). A field whose definition cannot be read is
 * a single, optional text field without a label until it can.
 */
@Component
public class ExpectedFieldDefinitionListener {

    private static final Logger log = LoggerFactory.getLogger(ExpectedFieldDefinitionListener.class);

    private final ObjectProvider<FieldDefinitionSource> source;

    public ExpectedFieldDefinitionListener(ObjectProvider<FieldDefinitionSource> source) {
        this.source = source;
    }

    @PostLoad
    public void loadDefinition(ExpectedField field) {
        field.setFieldType(ExpectedFieldType.STRING);
        field.setMany(false);
        field.setMandatory(false);
        try {
            FieldDefinitionSource definitions = source.getIfAvailable();
            // The id of a lazy document type is read without loading it
            if (definitions == null || field.getDocumentType() == null || field.getField() == null) {
                return;
            }
            Definition definition = definitions.definition(field.getDocumentType().getId(), field.getField());
            if (definition != null) {
                field.setFieldLabel(definition.label());
                field.setFieldType(definition.type());
                field.setFormat(definition.format());
                field.setMany(definition.many());
                field.setMandatory(definition.mandatory());
            }
        } catch (RuntimeException e) {
            log.warn("The definition of expected field {} could not be read: {}", field.getField(), e.getMessage());
        }
    }
}
