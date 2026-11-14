package bw.co.knowvera.form.field;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.jspecify.annotations.Nullable;

/**
 * One question of a custom form.
 */
public class FormFieldDTO
    implements Serializable
{
    private static final long serialVersionUID = 4401630276611930011L;

    @Nullable
    protected String id;
    /** Key of the field, unique within its form; captured values refer to it. Derived from the label when empty. */
    @Nullable
    protected String fieldId;
    protected String label;
    @Nullable
    protected String description;
    protected FormFieldType fieldType;
    @Nullable
    protected Boolean required;
    @Nullable
    protected String defaultValue;
    @Nullable
    protected String placeholder;
    /** Lowest value of a NUMBER field. */
    @Nullable
    protected BigDecimal minValue;
    /** Highest value of a NUMBER field. */
    @Nullable
    protected BigDecimal maxValue;
    @Nullable
    protected Integer minLength;
    @Nullable
    protected Integer maxLength;
    /** Regular expression a text value must match in full. */
    @Nullable
    protected String pattern;
    /** The choices of a SELECT, RADIO or MULTI_SELECT field. */
    @Nullable
    protected List<String> options;
    /** Place of the field in its section (read only: the order of the list decides). */
    @Nullable
    protected Integer position;

    public FormFieldDTO()
    {
    }

    public String getId() { return this.id; }
    public void setId(final String value) { this.id = value; }

    public String getFieldId() { return this.fieldId; }
    public void setFieldId(final String value) { this.fieldId = value; }

    public String getLabel() { return this.label; }
    public void setLabel(final String value) { this.label = value; }

    public String getDescription() { return this.description; }
    public void setDescription(final String value) { this.description = value; }

    public FormFieldType getFieldType() { return this.fieldType; }
    public void setFieldType(final FormFieldType value) { this.fieldType = value; }

    public Boolean getRequired() { return this.required; }
    public void setRequired(final Boolean value) { this.required = value; }

    public String getDefaultValue() { return this.defaultValue; }
    public void setDefaultValue(final String value) { this.defaultValue = value; }

    public String getPlaceholder() { return this.placeholder; }
    public void setPlaceholder(final String value) { this.placeholder = value; }

    public BigDecimal getMinValue() { return this.minValue; }
    public void setMinValue(final BigDecimal value) { this.minValue = value; }

    public BigDecimal getMaxValue() { return this.maxValue; }
    public void setMaxValue(final BigDecimal value) { this.maxValue = value; }

    public Integer getMinLength() { return this.minLength; }
    public void setMinLength(final Integer value) { this.minLength = value; }

    public Integer getMaxLength() { return this.maxLength; }
    public void setMaxLength(final Integer value) { this.maxLength = value; }

    public String getPattern() { return this.pattern; }
    public void setPattern(final String value) { this.pattern = value; }

    public List<String> getOptions()
    {
        if (this.options == null)
        {
            this.options = new ArrayList<String>();
        }
        return this.options;
    }
    public void setOptions(final List<String> value) { this.options = value; }

    public Integer getPosition() { return this.position; }
    public void setPosition(final Integer value) { this.position = value; }
}
