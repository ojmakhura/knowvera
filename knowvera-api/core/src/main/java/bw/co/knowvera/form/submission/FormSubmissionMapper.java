package bw.co.knowvera.form.submission;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(
    componentModel = "spring"
)
public abstract class FormSubmissionMapper {

    /**
     * A submission with its answers. The form's definition is left out: the service adds it.
     */
    @Mapping(target = "formId", source = "form.id")
    @Mapping(target = "formCode", source = "form.code")
    @Mapping(target = "formName", source = "form.name")
    @Mapping(target = "organisationId", source = "form.organisation.id")
    @Mapping(target = "organisation", source = "form.organisation.name")
    @Mapping(target = "kycRecordId", source = "kycRecord.id")
    @Mapping(target = "kycRecordRef", source = "kycRecord.ref")
    @Mapping(target = "target", source = "kycRecord.target")
    @Mapping(target = "targetId", source = "kycRecord.targetId")
    @Mapping(target = "clientName", source = "kycRecord.ownerDetails.name")
    @Mapping(target = "form", ignore = true)
    public abstract FormSubmissionDTO toFormSubmissionDTO(FormSubmission entity);

    /**
     * A submission without its answers, for lists.
     */
    @Named("summary")
    @Mapping(target = "formId", source = "form.id")
    @Mapping(target = "formCode", source = "form.code")
    @Mapping(target = "formName", source = "form.name")
    @Mapping(target = "organisationId", source = "form.organisation.id")
    @Mapping(target = "organisation", source = "form.organisation.name")
    @Mapping(target = "kycRecordId", source = "kycRecord.id")
    @Mapping(target = "kycRecordRef", source = "kycRecord.ref")
    @Mapping(target = "target", source = "kycRecord.target")
    @Mapping(target = "targetId", source = "kycRecord.targetId")
    @Mapping(target = "clientName", source = "kycRecord.ownerDetails.name")
    @Mapping(target = "form", ignore = true)
    @Mapping(target = "values", ignore = true)
    public abstract FormSubmissionDTO toFormSubmissionSummary(FormSubmission entity);

    public List<FormSubmissionDTO> toFormSubmissionSummaries(Collection<FormSubmission> entities)
    {
        List<FormSubmissionDTO> summaries = new ArrayList<FormSubmissionDTO>();
        if (entities != null)
        {
            entities.forEach(entity -> summaries.add(toFormSubmissionSummary(entity)));
        }
        return summaries;
    }

    @Mapping(target = "fieldId", source = "field.fieldId")
    @Mapping(target = "label", source = "field.label")
    public abstract FormFieldValueDTO toFormFieldValueDTO(FormFieldValue entity);
}
