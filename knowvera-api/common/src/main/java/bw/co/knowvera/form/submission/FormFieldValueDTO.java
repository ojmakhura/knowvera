package bw.co.knowvera.form.submission;

import java.io.Serializable;
import java.util.List;
import org.jspecify.annotations.Nullable;

/**
 * A client's answer to one field of a form.
 */
public class FormFieldValueDTO
    implements Serializable
{
    private static final long serialVersionUID = 2890011742963275108L;

    @Nullable
    protected String id;
    /** Key of the answered field ({@code FormFieldDTO.fieldId}). */
    protected String fieldId;
    /** Label of the answered field (read only). */
    @Nullable
    protected String label;
    /** The answer. Dates are ISO-8601, a CHECKBOX is "true" or "false". */
    @Nullable
    protected String value;
    /** The chosen options of a MULTI_SELECT field. */
    @Nullable
    protected List<String> values;

    public FormFieldValueDTO()
    {
    }

    public FormFieldValueDTO(final String fieldIdIn, final String valueIn)
    {
        this.fieldId = fieldIdIn;
        this.value = valueIn;
    }

    public String getId() { return this.id; }
    public void setId(final String value) { this.id = value; }

    public String getFieldId() { return this.fieldId; }
    public void setFieldId(final String value) { this.fieldId = value; }

    public String getLabel() { return this.label; }
    public void setLabel(final String value) { this.label = value; }

    public String getValue() { return this.value; }
    public void setValue(final String value) { this.value = value; }

    public List<String> getValues() { return this.values; }
    public void setValues(final List<String> value) { this.values = value; }
}
