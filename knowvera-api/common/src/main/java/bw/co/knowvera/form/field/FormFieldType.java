package bw.co.knowvera.form.field;

/**
 * The input a form field is captured with. It decides how a captured value is validated.
 */
public enum FormFieldType
{
    TEXT,
    TEXTAREA,
    NUMBER,
    DATE,
    DATETIME,
    TIME,
    EMAIL,
    TEL,
    URL,
    /** A single tick box: the value is "true" or "false". */
    CHECKBOX,
    /** One of the field's options, as a drop-down. */
    SELECT,
    /** One of the field's options, as radio buttons. */
    RADIO,
    /** Any number of the field's options. */
    MULTI_SELECT;

    /** @return true when the value must come from the field's options */
    public boolean hasOptions()
    {
        return this == SELECT || this == RADIO || this == MULTI_SELECT;
    }

    /** @return true for free text, where length limits and a pattern apply */
    public boolean isText()
    {
        return this == TEXT || this == TEXTAREA || this == TEL;
    }
}
