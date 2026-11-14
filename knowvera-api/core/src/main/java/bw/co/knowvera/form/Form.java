package bw.co.knowvera.form;

import bw.co.knowvera.Auditable;
import bw.co.knowvera.TargetEntity;
import bw.co.knowvera.form.section.FormSection;
import bw.co.knowvera.organisation.Organisation;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.hibernate.annotations.UuidGenerator;

/**
 * A custom form an organisation designs for its clients to fill: sections of fields.
 */
@Entity
@Table(
    name = "FORM",
    uniqueConstraints = {
        @UniqueConstraint(name = "UK_FORM_ORGANISATION_CODE", columnNames = {"ORGANISATION_FK", "CODE"})
    })
public class Form
    extends Auditable
{
    private static final long serialVersionUID = 8832104476120095561L;

    @Id
    @UuidGenerator(style = UuidGenerator.Style.VERSION_7)
    @Column(name = "ID", unique = true, nullable = false, insertable = true, updatable = true)
    private UUID id;

    @Column(name = "CODE", nullable = false, length = 50)
    private String code;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "DESCRIPTION", nullable = true, length = 2000)
    private String description;

    @Column(name = "TARGET_TYPE", nullable = false)
    @Enumerated(EnumType.STRING)
    private TargetEntity targetType;

    @Column(name = "STATUS", nullable = false)
    @Enumerated(EnumType.STRING)
    private FormStatus status = FormStatus.DRAFT;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "ORGANISATION_FK")
    private Organisation organisation;

    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "form", fetch = FetchType.LAZY, orphanRemoval = true)
    @OrderBy("position ASC")
    private List<FormSection> sections = new ArrayList<FormSection>();

    public UUID getId() { return this.id; }
    public void setId(UUID idIn) { this.id = idIn; }

    public String getCode() { return this.code; }
    public void setCode(String codeIn) { this.code = codeIn; }

    public String getName() { return this.name; }
    public void setName(String nameIn) { this.name = nameIn; }

    public String getDescription() { return this.description; }
    public void setDescription(String descriptionIn) { this.description = descriptionIn; }

    public TargetEntity getTargetType() { return this.targetType; }
    public void setTargetType(TargetEntity targetTypeIn) { this.targetType = targetTypeIn; }

    public FormStatus getStatus() { return this.status; }
    public void setStatus(FormStatus statusIn) { this.status = statusIn; }

    public Organisation getOrganisation() { return this.organisation; }
    public void setOrganisation(Organisation organisationIn) { this.organisation = organisationIn; }

    public List<FormSection> getSections() { return this.sections; }
    public void setSections(List<FormSection> sectionsIn) { this.sections = sectionsIn; }

    @Override
    public boolean equals(Object object)
    {
        if (this == object)
        {
            return true;
        }
        if (!(object instanceof Form))
        {
            return false;
        }
        final Form that = (Form)object;
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
        return "Form( code=" + this.code + " name=" + this.name + " status=" + this.status + " id=" + this.id + ")";
    }

    /**
     * Constructs new instances of {@link Form}.
     */
    public static final class Factory
    {
        public static Form newInstance()
        {
            return new Form();
        }
    }
}
