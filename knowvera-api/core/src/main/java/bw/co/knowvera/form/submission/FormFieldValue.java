package bw.co.knowvera.form.submission;

import bw.co.knowvera.form.field.FormField;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;

/**
 * A client's answer to one field of a form.
 */
@Entity
@Table(
    name = "FORM_FIELD_VALUE",
    uniqueConstraints = {
        @UniqueConstraint(name = "UK_FORM_FIELD_VALUE_FIELD", columnNames = {"FORM_SUBMISSION_FK", "FORM_FIELD_FK"})
    })
public class FormFieldValue
    implements Serializable
{
    private static final long serialVersionUID = -7741229035618850046L;

    @Id
    @UuidGenerator(style = UuidGenerator.Style.VERSION_7)
    @Column(name = "ID", unique = true, nullable = false, insertable = true, updatable = true)
    private UUID id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "FORM_SUBMISSION_FK")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private FormSubmission submission;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "FORM_FIELD_FK")
    private FormField field;

    @Column(name = "FIELD_VALUE", nullable = true, columnDefinition = "text")
    private String value;

    /** The chosen options of a MULTI_SELECT field. */
    @Column(name = "FIELD_VALUES", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private List<String> values;

    public UUID getId() { return this.id; }
    public void setId(UUID idIn) { this.id = idIn; }

    public FormSubmission getSubmission() { return this.submission; }
    public void setSubmission(FormSubmission submissionIn) { this.submission = submissionIn; }

    public FormField getField() { return this.field; }
    public void setField(FormField fieldIn) { this.field = fieldIn; }

    public String getValue() { return this.value; }
    public void setValue(String valueIn) { this.value = valueIn; }

    public List<String> getValues() { return this.values; }
    public void setValues(List<String> valuesIn) { this.values = valuesIn; }

    @Override
    public boolean equals(Object object)
    {
        if (this == object)
        {
            return true;
        }
        if (!(object instanceof FormFieldValue))
        {
            return false;
        }
        final FormFieldValue that = (FormFieldValue)object;
        return this.id != null && this.id.equals(that.getId());
    }

    @Override
    public int hashCode()
    {
        return 29 * (this.id == null ? 0 : this.id.hashCode());
    }
}
