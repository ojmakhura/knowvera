package bw.co.knowvera.form;

import bw.co.knowvera.AuditableDTO;
import bw.co.knowvera.TargetEntity;
import bw.co.knowvera.form.section.FormSectionDTO;
import java.util.ArrayList;
import java.util.List;
import org.jspecify.annotations.Nullable;

/**
 * A custom form an organisation designs for its clients to fill: sections of fields.
 */
public class FormDTO
    extends AuditableDTO
{
    private static final long serialVersionUID = 7715520083916240213L;

    /** Short code of the form, unique within the organisation. */
    protected String code;
    protected String name;
    @Nullable
    protected String description;
    /** Who fills the form: INDIVIDUAL or ORGANISATION clients. */
    protected TargetEntity targetType;
    @Nullable
    protected FormStatus status;
    /** The organisation the form belongs to. */
    protected String organisationId;
    @Nullable
    protected String organisation;
    @Nullable
    protected List<FormSectionDTO> sections;

    public FormDTO()
    {
        super();
    }

    public String getCode() { return this.code; }
    public void setCode(final String value) { this.code = value; }

    public String getName() { return this.name; }
    public void setName(final String value) { this.name = value; }

    public String getDescription() { return this.description; }
    public void setDescription(final String value) { this.description = value; }

    public TargetEntity getTargetType() { return this.targetType; }
    public void setTargetType(final TargetEntity value) { this.targetType = value; }

    public FormStatus getStatus() { return this.status; }
    public void setStatus(final FormStatus value) { this.status = value; }

    public String getOrganisationId() { return this.organisationId; }
    public void setOrganisationId(final String value) { this.organisationId = value; }

    public String getOrganisation() { return this.organisation; }
    public void setOrganisation(final String value) { this.organisation = value; }

    public List<FormSectionDTO> getSections()
    {
        if (this.sections == null)
        {
            this.sections = new ArrayList<FormSectionDTO>();
        }
        return this.sections;
    }
    public void setSections(final List<FormSectionDTO> value) { this.sections = value; }
}
