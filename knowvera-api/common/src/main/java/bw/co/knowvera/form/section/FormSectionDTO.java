package bw.co.knowvera.form.section;

import bw.co.knowvera.form.field.FormFieldDTO;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.jspecify.annotations.Nullable;

/**
 * A group of fields of a custom form, shown together under one heading.
 */
public class FormSectionDTO
    implements Serializable
{
    private static final long serialVersionUID = -1173368209145587230L;

    @Nullable
    protected String id;
    /** Key of the section, unique within its form. Derived from the label when empty. */
    @Nullable
    protected String sectionId;
    protected String label;
    @Nullable
    protected String description;
    /** Place of the section in its form (read only: the order of the list decides). */
    @Nullable
    protected Integer position;
    @Nullable
    protected List<FormFieldDTO> fields;

    public FormSectionDTO()
    {
    }

    public String getId() { return this.id; }
    public void setId(final String value) { this.id = value; }

    public String getSectionId() { return this.sectionId; }
    public void setSectionId(final String value) { this.sectionId = value; }

    public String getLabel() { return this.label; }
    public void setLabel(final String value) { this.label = value; }

    public String getDescription() { return this.description; }
    public void setDescription(final String value) { this.description = value; }

    public Integer getPosition() { return this.position; }
    public void setPosition(final Integer value) { this.position = value; }

    public List<FormFieldDTO> getFields()
    {
        if (this.fields == null)
        {
            this.fields = new ArrayList<FormFieldDTO>();
        }
        return this.fields;
    }
    public void setFields(final List<FormFieldDTO> value) { this.fields = value; }
}
