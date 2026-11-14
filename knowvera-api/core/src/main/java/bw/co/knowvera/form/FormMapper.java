package bw.co.knowvera.form;

import bw.co.knowvera.form.field.FormField;
import bw.co.knowvera.form.field.FormFieldDTO;
import bw.co.knowvera.form.section.FormSection;
import bw.co.knowvera.form.section.FormSectionDTO;
import java.util.Collection;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Forms to their DTOs. Saving goes the other way by hand in {@link FormServiceImpl}, which merges
 * the sections and fields into the stored form.
 */
@Mapper(
    componentModel = "spring"
)
public abstract class FormMapper {

    @Mapping(target = "organisation", source = "organisation.name")
    @Mapping(target = "organisationId", source = "organisation.id")
    public abstract FormDTO toFormDTO(Form entity);

    public abstract List<FormDTO> toFormDTOCollection(Collection<Form> entities);

    public abstract FormSectionDTO toFormSectionDTO(FormSection entity);

    public abstract FormFieldDTO toFormFieldDTO(FormField entity);
}
