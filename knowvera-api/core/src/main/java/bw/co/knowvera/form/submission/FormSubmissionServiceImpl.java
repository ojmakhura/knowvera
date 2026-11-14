package bw.co.knowvera.form.submission;

import bw.co.knowvera.form.Form;
import bw.co.knowvera.form.FormMapper;
import bw.co.knowvera.form.FormRepository;
import bw.co.knowvera.form.FormStatus;
import bw.co.knowvera.form.field.FormField;
import bw.co.knowvera.form.field.FormFieldType;
import bw.co.knowvera.kyc.KycRecord;
import bw.co.knowvera.kyc.KycRecordRepository;
import jakarta.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @see bw.co.knowvera.form.submission.FormSubmissionService
 */
@Service("formSubmissionService")
@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
public class FormSubmissionServiceImpl
    extends FormSubmissionServiceBase
{
    private static final Sort NEWEST_FIRST = Sort.by(Sort.Direction.DESC, "createdAt");

    private final FormRepository formRepository;
    private final FormMapper formMapper;
    private final KycRecordRepository kycRecordRepository;
    private final FormRequirements formRequirements;

    public FormSubmissionServiceImpl(
        FormSubmissionRepository formSubmissionRepository,
        FormSubmissionMapper formSubmissionMapper,
        FormRepository formRepository,
        FormMapper formMapper,
        KycRecordRepository kycRecordRepository,
        FormRequirements formRequirements) {
        super(
            formSubmissionRepository,
            formSubmissionMapper        );
        this.formRepository = formRepository;
        this.formMapper = formMapper;
        this.kycRecordRepository = kycRecordRepository;
        this.formRequirements = formRequirements;
    }

    /**
     * @see bw.co.knowvera.form.submission.FormSubmissionService#createSubmission(FormAssignmentDTO, String)
     */
    @Override
    protected FormSubmissionDTO handleCreateSubmission(FormAssignmentDTO assignment, String user)
        throws Exception
    {
        if (StringUtils.isBlank(assignment.getFormId()) || StringUtils.isBlank(assignment.getKycRecordId()))
        {
            throw new FormSubmissionServiceException("Say which form is to be filled, and on which KYC record");
        }
        Form form = formRepository.findById(uuid(assignment.getFormId(), "form"))
                .orElseThrow(() -> FormSubmissionServiceException.notFound("The form does not exist"));
        if (form.getStatus() != FormStatus.PUBLISHED)
        {
            throw new FormSubmissionServiceException("Only a published form can be given to clients to fill");
        }
        KycRecord record = record(assignment.getKycRecordId());
        if (record.getTarget() != form.getTargetType())
        {
            throw new FormSubmissionServiceException(
                    "This form is filled by " + form.getTargetType() + " clients, not " + record.getTarget());
        }
        if (formSubmissionRepository.existsByFormIdAndKycRecordIdAndStatusNot(
                form.getId(), record.getId(), FormSubmissionStatus.ACCEPTED))
        {
            throw new FormSubmissionServiceException("The KYC record already has this form to fill");
        }

        FormSubmission entity = FormSubmission.Factory.newInstance();
        entity.setForm(form);
        entity.setKycRecord(record);
        entity.setStatus(FormSubmissionStatus.NEW);
        entity.setDueDate(assignment.getDueDate());
        entity.setCreatedBy(user);
        entity.setCreatedAt(LocalDateTime.now());
        return formSubmissionMapper.toFormSubmissionDTO(formSubmissionRepository.save(entity));
    }

    /**
     * @see bw.co.knowvera.form.submission.FormSubmissionService#findById(String)
     */
    @Override
    protected FormSubmissionDTO handleFindById(String id)
        throws Exception
    {
        return withForm(load(id));
    }

    /**
     * @see bw.co.knowvera.form.submission.FormSubmissionService#findByForm(String)
     */
    @Override
    protected List<FormSubmissionDTO> handleFindByForm(String formId)
        throws Exception
    {
        FormSubmissionSearchCriteria criteria = new FormSubmissionSearchCriteria();
        criteria.setFormId(formId);
        return handleSearch(criteria);
    }

    /**
     * @see bw.co.knowvera.form.submission.FormSubmissionService#findByOrganisation(String)
     */
    @Override
    protected List<FormSubmissionDTO> handleFindByOrganisation(String organisationId)
        throws Exception
    {
        FormSubmissionSearchCriteria criteria = new FormSubmissionSearchCriteria();
        criteria.setOrganisationId(organisationId);
        return handleSearch(criteria);
    }

    /**
     * @see bw.co.knowvera.form.submission.FormSubmissionService#findByKycRecord(String)
     */
    @Override
    protected List<FormSubmissionDTO> handleFindByKycRecord(String kycRecordId)
        throws Exception
    {
        FormSubmissionSearchCriteria criteria = new FormSubmissionSearchCriteria();
        criteria.setKycRecordId(kycRecordId);
        return handleSearch(criteria);
    }

    /**
     * @see bw.co.knowvera.form.submission.FormSubmissionService#findRequired(String, String)
     */
    @Override
    protected List<FormSubmissionDTO> handleFindRequired(String kycRecordId, String user)
        throws Exception
    {
        formRequirements.requireFor(record(kycRecordId), user);
        return handleFindByKycRecord(kycRecordId);
    }

    /**
     * @see bw.co.knowvera.form.submission.FormSubmissionService#search(FormSubmissionSearchCriteria)
     */
    @Override
    protected List<FormSubmissionDTO> handleSearch(FormSubmissionSearchCriteria criteria)
        throws Exception
    {
        Specification<FormSubmission> specification = (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<Predicate>();
            if (StringUtils.isNotBlank(criteria.getFormId()))
            {
                predicates.add(builder.equal(root.get("form").get("id"), uuid(criteria.getFormId(), "form")));
            }
            if (StringUtils.isNotBlank(criteria.getOrganisationId()))
            {
                predicates.add(builder.equal(root.get("form").get("organisation").get("id"),
                        uuid(criteria.getOrganisationId(), "organisation")));
            }
            if (StringUtils.isNotBlank(criteria.getKycRecordId()))
            {
                predicates.add(builder.equal(root.get("kycRecord").get("id"),
                        uuid(criteria.getKycRecordId(), "KYC record")));
            }
            if (criteria.getStatus() != null)
            {
                predicates.add(builder.equal(root.get("status"), criteria.getStatus()));
            }
            return builder.and(predicates.toArray(new Predicate[0]));
        };
        return formSubmissionMapper.toFormSubmissionSummaries(
                formSubmissionRepository.findAll(specification, NEWEST_FIRST));
    }

    /**
     * @see bw.co.knowvera.form.submission.FormSubmissionService#saveValues(String, List, String)
     */
    @Override
    protected FormSubmissionDTO handleSaveValues(String id, List<FormFieldValueDTO> values, String user)
        throws Exception
    {
        FormSubmission entity = load(id);
        if (!entity.getStatus().isEditable())
        {
            throw new FormSubmissionServiceException(entity.getStatus() == FormSubmissionStatus.SUBMITTED
                    ? "The form has been submitted and can no longer be changed"
                    : "The form has been accepted and can no longer be changed");
        }

        Map<String, FormField> fields = fieldsOf(entity.getForm());
        Map<UUID, FormFieldValue> stored = new LinkedHashMap<UUID, FormFieldValue>();
        entity.getValues().forEach(value -> stored.put(value.getField().getId(), value));

        List<String> errors = new ArrayList<String>();
        for (FormFieldValueDTO answer : values)
        {
            FormField field = answer == null || answer.getFieldId() == null ? null : fields.get(answer.getFieldId().trim());
            if (field == null)
            {
                errors.add("'" + (answer == null ? null : answer.getFieldId()) + "' is not a field of this form");
                continue;
            }
            String text = normalise(field, answer.getValue());
            List<String> choices = field.getFieldType() == FormFieldType.MULTI_SELECT ? clean(answer.getValues()) : null;
            Optional<String> error = FormValueValidator.validate(field, text, choices, false);
            if (error.isPresent())
            {
                errors.add(field.getLabel() + " " + error.get());
                continue;
            }

            FormFieldValue value = stored.get(field.getId());
            boolean empty = text == null && (choices == null || choices.isEmpty());
            if (empty)
            {
                if (value != null)
                {
                    entity.getValues().remove(value);
                }
                continue;
            }
            if (value == null)
            {
                value = new FormFieldValue();
                value.setSubmission(entity);
                value.setField(field);
                entity.getValues().add(value);
                stored.put(field.getId(), value);
            }
            value.setValue(text);
            value.setValues(choices);
        }
        if (!errors.isEmpty())
        {
            throw new FormSubmissionServiceException(String.join("; ", errors));
        }

        if (entity.getStatus() == FormSubmissionStatus.NEW)
        {
            entity.setStatus(FormSubmissionStatus.DRAFT);
        }
        touch(entity, user);
        return withForm(formSubmissionRepository.save(entity));
    }

    /**
     * @see bw.co.knowvera.form.submission.FormSubmissionService#submit(String, String)
     */
    @Override
    protected FormSubmissionDTO handleSubmit(String id, String user)
        throws Exception
    {
        FormSubmission entity = load(id);
        if (!entity.getStatus().isEditable())
        {
            throw new FormSubmissionServiceException("The form has already been submitted");
        }

        Map<UUID, FormFieldValue> stored = new LinkedHashMap<UUID, FormFieldValue>();
        entity.getValues().forEach(value -> stored.put(value.getField().getId(), value));
        List<String> errors = new ArrayList<String>();
        for (FormField field : fieldsOf(entity.getForm()).values())
        {
            FormFieldValue value = stored.get(field.getId());
            FormValueValidator.validate(field, value == null ? null : value.getValue(),
                    value == null ? null : value.getValues(), true)
                    .ifPresent(error -> errors.add(field.getLabel() + " " + error));
        }
        if (!errors.isEmpty())
        {
            throw new FormSubmissionServiceException("The form cannot be submitted yet: " + String.join("; ", errors));
        }

        entity.setStatus(FormSubmissionStatus.SUBMITTED);
        entity.setSubmittedBy(user);
        entity.setSubmittedAt(LocalDateTime.now());
        touch(entity, user);
        return withForm(formSubmissionRepository.save(entity));
    }

    /**
     * @see bw.co.knowvera.form.submission.FormSubmissionService#updateStatus(String, FormSubmissionStatus, String, String)
     */
    @Override
    protected FormSubmissionDTO handleUpdateStatus(String id, FormSubmissionStatus status, String note, String user)
        throws Exception
    {
        if (status != FormSubmissionStatus.ACCEPTED && status != FormSubmissionStatus.RETURNED)
        {
            throw new FormSubmissionServiceException("A submitted form is either ACCEPTED or RETURNED");
        }
        if (status == FormSubmissionStatus.RETURNED && StringUtils.isBlank(note))
        {
            throw new FormSubmissionServiceException("Tell the client what to correct");
        }
        FormSubmission entity = load(id);
        if (entity.getStatus() != FormSubmissionStatus.SUBMITTED)
        {
            throw new FormSubmissionServiceException("Only a submitted form can be accepted or returned");
        }
        entity.setStatus(status);
        entity.setReviewNote(StringUtils.trimToNull(note));
        entity.setReviewedBy(user);
        entity.setReviewedAt(LocalDateTime.now());
        touch(entity, user);
        return withForm(formSubmissionRepository.save(entity));
    }

    /**
     * @see bw.co.knowvera.form.submission.FormSubmissionService#remove(String)
     */
    @Override
    protected boolean handleRemove(String id)
        throws Exception
    {
        formSubmissionRepository.delete(load(id));
        return true;
    }

    private FormSubmission load(String id)
    {
        return formSubmissionRepository.findById(uuid(id, "form submission"))
                .orElseThrow(() -> FormSubmissionServiceException.notFound("The form submission does not exist"));
    }

    private KycRecord record(String id)
    {
        return kycRecordRepository.findById(uuid(id, "KYC record"))
                .orElseThrow(() -> FormSubmissionServiceException.notFound("The KYC record does not exist"));
    }

    private FormSubmissionDTO withForm(FormSubmission entity)
    {
        FormSubmissionDTO submission = formSubmissionMapper.toFormSubmissionDTO(entity);
        submission.setForm(formMapper.toFormDTO(entity.getForm()));
        return submission;
    }

    /** The fields of the form by their key, in the order of the form. */
    private static Map<String, FormField> fieldsOf(Form form)
    {
        Map<String, FormField> fields = new LinkedHashMap<String, FormField>();
        form.getSections().forEach(section ->
                section.getFields().forEach(field -> fields.put(field.getFieldId(), field)));
        return fields;
    }

    private static String normalise(FormField field, String value)
    {
        String text = StringUtils.trimToNull(value);
        return text != null && field.getFieldType() == FormFieldType.CHECKBOX ? text.toLowerCase() : text;
    }

    private static List<String> clean(List<String> choices)
    {
        return choices == null ? List.of() : choices.stream()
                .filter(StringUtils::isNotBlank)
                .map(String::trim)
                .distinct()
                .toList();
    }

    private static void touch(FormSubmission entity, String user)
    {
        entity.setModifiedBy(user);
        entity.setModifiedAt(LocalDateTime.now());
    }

    private static UUID uuid(String id, String what)
    {
        try
        {
            return UUID.fromString(id.trim());
        }
        catch (IllegalArgumentException e)
        {
            throw FormSubmissionServiceException.notFound("'" + id + "' is not the id of a " + what);
        }
    }
}
