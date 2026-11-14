package bw.co.knowvera.form.section;

import bw.co.knowvera.form.Form;
import bw.co.knowvera.form.field.FormField;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.hibernate.annotations.UuidGenerator;

/**
 * A group of fields of a custom form, shown together under one heading.
 */
@Entity
@Table(name = "FORM_SECTION")
public class FormSection
    implements Serializable
{
    private static final long serialVersionUID = -5590217731064428819L;

    @Id
    @UuidGenerator(style = UuidGenerator.Style.VERSION_7)
    @Column(name = "ID", unique = true, nullable = false, insertable = true, updatable = true)
    private UUID id;

    @Column(name = "SECTION_ID", nullable = false, length = 100)
    private String sectionId;

    @Column(name = "LABEL", nullable = false)
    private String label;

    @Column(name = "DESCRIPTION", nullable = true, length = 2000)
    private String description;

    @Column(name = "POSITION", nullable = true)
    private Integer position;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "FORM_FK")
    private Form form;

    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "section", fetch = FetchType.LAZY, orphanRemoval = true)
    @OrderBy("position ASC")
    private List<FormField> fields = new ArrayList<FormField>();

    public UUID getId() { return this.id; }
    public void setId(UUID idIn) { this.id = idIn; }

    public String getSectionId() { return this.sectionId; }
    public void setSectionId(String sectionIdIn) { this.sectionId = sectionIdIn; }

    public String getLabel() { return this.label; }
    public void setLabel(String labelIn) { this.label = labelIn; }

    public String getDescription() { return this.description; }
    public void setDescription(String descriptionIn) { this.description = descriptionIn; }

    public Integer getPosition() { return this.position; }
    public void setPosition(Integer positionIn) { this.position = positionIn; }

    public Form getForm() { return this.form; }
    public void setForm(Form formIn) { this.form = formIn; }

    public List<FormField> getFields() { return this.fields; }
    public void setFields(List<FormField> fieldsIn) { this.fields = fieldsIn; }

    @Override
    public boolean equals(Object object)
    {
        if (this == object)
        {
            return true;
        }
        if (!(object instanceof FormSection))
        {
            return false;
        }
        final FormSection that = (FormSection)object;
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
        return "FormSection( sectionId=" + this.sectionId + " label=" + this.label + " id=" + this.id + ")";
    }
}
