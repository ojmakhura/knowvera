package bw.co.knowvera.document.type.field;

import java.util.UUID;

import org.jspecify.annotations.Nullable;

/**
 * Where an expected field's definition is kept. The definition (label, type, format, many,
 * mandatory) says what to extract from a document, which is veritas-processor's: it is stored in
 * the document type's schema there and nowhere here. An expected field here keeps the field's
 * name and what it is matched against.
 */
public interface FieldDefinitionSource {

    record Definition(@Nullable String label, ExpectedFieldType type, @Nullable String format, boolean many,
            boolean mandatory) {
    }

    /** The definition of a document type's field, or null when it is not known (yet, or right now). */
    @Nullable
    Definition definition(UUID documentTypeId, String field);
}
