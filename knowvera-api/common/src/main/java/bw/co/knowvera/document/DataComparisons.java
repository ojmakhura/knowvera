package bw.co.knowvera.document;

import org.jspecify.annotations.Nullable;

import java.io.Serializable;


//DataComparisons
public record DataComparisons (
    // Record attributes
    /**
     * TODO: Model Documentation for bw.co.knowvera.document.DataComparisons.field
     */
    java.lang.String field,    /**
     * TODO: Model Documentation for bw.co.knowvera.document.DataComparisons.expected
     */
    java.lang.String expected,    /**
     * TODO: Model Documentation for bw.co.knowvera.document.DataComparisons.extracted
     */
    java.lang.String extracted,    /**
     * TODO: Model Documentation for bw.co.knowvera.document.DataComparisons.matches
     */
    java.lang.Boolean matches,    /**
     * TODO: Model Documentation for bw.co.knowvera.document.DataComparisons.fieldType
     */
    bw.co.knowvera.document.type.field.ExpectedFieldType fieldType
) implements Serializable {
    /**
     * Constructor taking only required properties
     * @param fieldIn java.lang.String TODO: Model Documentation for bw.co.knowvera.document.DataComparisons.field
     * @param expectedIn java.lang.String TODO: Model Documentation for bw.co.knowvera.document.DataComparisons.expected
     * @param extractedIn java.lang.String TODO: Model Documentation for bw.co.knowvera.document.DataComparisons.extracted
     * @param fieldTypeIn bw.co.knowvera.document.type.field.ExpectedFieldType TODO: Model Documentation for bw.co.knowvera.document.DataComparisons.fieldType
     */
    public DataComparisons(final java.lang.String fieldIn, final java.lang.String expectedIn, final java.lang.String extractedIn, final bw.co.knowvera.document.type.field.ExpectedFieldType fieldTypeIn)
    {
        this(
            fieldIn,  // field: required property - use constructor argument
            expectedIn,  // expected: required property - use constructor argument
            extractedIn,  // extracted: required property - use constructor argument
            null,  // matches: optional non-primitive property - use null
            fieldTypeIn // fieldType: required property - use constructor argument
        );
    }

}
