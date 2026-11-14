package bw.co.knowvera.form.field;

import bw.co.knowvera.form.section.FormSection;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;

/**
 * One question of a custom form.
 */
@Entity
@Table(name = "FORM_FIELD")
public class FormField
    implements Serializable
{
    private static final long serialVersionUID = 1907765520318846627L;

    @Id
    @UuidGenerator(style = UuidGenerator.Style.VERSION_7)
    @Column(name = "ID", unique = true, nullable = false, insertable = true, updatable = true)
    private UUID id;

    /** Key of the field, unique within its form. */
    @Column(name = "FIELD_ID", nullable = false, length = 100)
    private String fieldId;

    @Column(name = "LABEL", nullable = false)
    private String label;

    @Column(name = "DESCRIPTION", nullable = true, length = 2000)
    private String description;

    @Column(name = "FIELD_TYPE", nullable = false)
    @Enumerated(EnumType.STRING)
    private FormFieldType fieldType;

    @Column(name = "REQUIRED", nullable = true)
    private Boolean required = Boolean.valueOf(false);

    @Column(name = "DEFAULT_VALUE", nullable = true, length = 2000)
    private String defaultValue;

    @Column(name = "PLACEHOLDER", nullable = true)
    private String placeholder;

    @Column(name = "MIN_VALUE", nullable = true)
    private BigDecimal minValue;

    @Column(name = "MAX_VALUE", nullable = true)
    private BigDecimal maxValue;

    @Column(name = "MIN_LENGTH", nullable = true)
    private Integer minLength;

    @Column(name = "MAX_LENGTH", nullable = true)
    private Integer maxLength;

    @Column(name = "PATTERN", nullable = true, length = 500)
    private String pattern;

    @Column(name = "OPTIONS", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private List<String> options;

    @Column(name = "POSITION", nullable = true)
    private Integer position;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "FORM_SECTION_FK")
    private FormSection section;

    public UUID getId() { return this.id; }
    public void setId(UUID idIn) { this.id = idIn; }

    public String getFieldId() { return this.fieldId; }
    public void setFieldId(String fieldIdIn) { this.fieldId = fieldIdIn; }

    public String getLabel() { return this.label; }
    public void setLabel(String labelIn) { this.label = labelIn; }

    public String getDescription() { return this.description; }
    public void setDescription(String descriptionIn) { this.description = descriptionIn; }

    public FormFieldType getFieldType() { return this.fieldType; }
    public void setFieldType(FormFieldType fieldTypeIn) { this.fieldType = fieldTypeIn; }

    public Boolean getRequired() { return this.required; }
    public void setRequired(Boolean requiredIn) { this.required = requiredIn; }

    public String getDefaultValue() { return this.defaultValue; }
    public void setDefaultValue(String defaultValueIn) { this.defaultValue = defaultValueIn; }

    public String getPlaceholder() { return this.placeholder; }
    public void setPlaceholder(String placeholderIn) { this.placeholder = placeholderIn; }

    public BigDecimal getMinValue() { return this.minValue; }
    public void setMinValue(BigDecimal minValueIn) { this.minValue = minValueIn; }

    public BigDecimal getMaxValue() { return this.maxValue; }
    public void setMaxValue(BigDecimal maxValueIn) { this.maxValue = maxValueIn; }

    public Integer getMinLength() { return this.minLength; }
    public void setMinLength(Integer minLengthIn) { this.minLength = minLengthIn; }

    public Integer getMaxLength() { return this.maxLength; }
    public void setMaxLength(Integer maxLengthIn) { this.maxLength = maxLengthIn; }

    public String getPattern() { return this.pattern; }
    public void setPattern(String patternIn) { this.pattern = patternIn; }

    public List<String> getOptions() { return this.options; }
    public void setOptions(List<String> optionsIn) { this.options = optionsIn; }

    public Integer getPosition() { return this.position; }
    public void setPosition(Integer positionIn) { this.position = positionIn; }

    public FormSection getSection() { return this.section; }
    public void setSection(FormSection sectionIn) { this.section = sectionIn; }

    @Override
    public boolean equals(Object object)
    {
        if (this == object)
        {
            return true;
        }
        if (!(object instanceof FormField))
        {
            return false;
        }
        final FormField that = (FormField)object;
        return this.id != null && this.id.equals(that.getId());
    }

    @Override
    public int hashCode()
    {
        return 29 * (this.id == null ? 0 : this.id.hashCode());
    }

    @Override
    public String toString()
    {
        return "FormField( fieldId=" + this.fieldId + " fieldType=" + this.fieldType + " id=" + this.id + ")";
    }
}
