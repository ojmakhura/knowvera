package bw.co.knowvera.form;

import bw.co.knowvera.TargetEntity;
import bw.co.knowvera.form.field.FormField;
import bw.co.knowvera.form.field.FormFieldDTO;
import bw.co.knowvera.form.field.FormFieldRepository;
import bw.co.knowvera.form.section.FormSection;
import bw.co.knowvera.form.section.FormSectionDTO;
import bw.co.knowvera.form.submission.FormRequirements;
import bw.co.knowvera.form.submission.FormSubmissionRepository;
import bw.co.knowvera.organisation.Organisation;
import bw.co.knowvera.organisation.OrganisationRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @see bw.co.knowvera.form.FormService
 */
@Service("formService")
@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
public class FormServiceImpl
    extends FormServiceBase
{
    private static final Pattern CODE = Pattern.compile("^[A-Za-z0-9][A-Za-z0-9_-]{0,49}$");
    private static final int MAX_KEY_LENGTH = 100;

    private final OrganisationRepository organisationRepository;
    private final FormSubmissionRepository formSubmissionRepository;
    private final FormRequirements formRequirements;

    public FormServiceImpl(
        FormRepository formRepository,
        FormMapper formMapper,
        FormFieldRepository formFieldRepository,
        OrganisationRepository organisationRepository,
        FormSubmissionRepository formSubmissionRepository,
        FormRequirements formRequirements) {
        super(
            formRepository,
            formMapper,
            formFieldRepository        );
        this.organisationRepository = organisationRepository;
        this.formSubmissionRepository = formSubmissionRepository;
        this.formRequirements = formRequirements;
    }

    /**
     * @see bw.co.knowvera.form.FormService#findById(String)
     */
    @Override
    protected FormDTO handleFindById(String id)
        throws Exception
    {
        return formMapper.toFormDTO(load(id));
    }

    /**
     * @see bw.co.knowvera.form.FormService#save(FormDTO)
     */
    @Override
    protected FormDTO handleSave(FormDTO form)
        throws Exception
    {
        checkDefinition(form);

        boolean creating = StringUtils.isBlank(form.getId());
        Form entity;
        if (creating)
        {
            if (StringUtils.isBlank(form.getOrganisationId()))
            {
                throw new FormServiceException("Say which organisation the form belongs to");
            }
            Organisation organisation = organisationRepository.findById(uuid(form.getOrganisationId(), "organisation"))
                    .orElseThrow(() -> FormServiceException.notFound("The organisation of the form does not exist"));
            entity = Form.Factory.newInstance();
            entity.setOrganisation(organisation);
            entity.setStatus(FormStatus.DRAFT);
            entity.setCreatedBy(StringUtils.defaultIfBlank(form.getCreatedBy(), "system"));
            entity.setCreatedAt(form.getCreatedAt() == null ? LocalDateTime.now() : form.getCreatedAt());
        }
        else
        {
            entity = load(form.getId());
            if (StringUtils.isNotBlank(form.getOrganisationId())
                    && !entity.getOrganisation().getId().toString().equalsIgnoreCase(form.getOrganisationId().trim()))
            {
                throw new FormServiceException("A form cannot be moved to another organisation");
            }
            entity.setModifiedBy(StringUtils.defaultIfBlank(form.getModifiedBy(), "system"));
            entity.setModifiedAt(form.getModifiedAt() == null ? LocalDateTime.now() : form.getModifiedAt());
        }

        String code = form.getCode().trim();
        final Form current = entity;
        formRepository.findByOrganisationIdAndCodeIgnoreCase(entity.getOrganisation().getId(), code)
                .filter(other -> !other.equals(current))
                .ifPresent(other -> {
                    throw new FormServiceException("The organisation already has a form with the code " + code);
                });

        entity.setCode(code);
        entity.setName(form.getName().trim());
        entity.setDescription(StringUtils.trimToNull(form.getDescription()));
        entity.setTargetType(form.getTargetType());
        if (form.getStatus() != null)
        {
            entity.setStatus(form.getStatus());
        }

        mergeSections(entity, form.getSections());

        if (entity.getStatus() == FormStatus.PUBLISHED
                && entity.getSections().stream().allMatch(section -> section.getFields().isEmpty()))
        {
            throw new FormServiceException("A form without fields cannot be published");
        }

        Form saved = formRepository.save(entity);
        // A published form is required from the organisation's clients: those that already have a
        // KYC record are asked now, the others when they start their KYC
        if (saved.getStatus() == FormStatus.PUBLISHED)
        {
            formRequirements.requireFrom(saved,
                    creating ? saved.getCreatedBy() : saved.getModifiedBy());
        }
        return formMapper.toFormDTO(saved);
    }

    /**
     * @see bw.co.knowvera.form.FormService#remove(String)
     */
    @Override
    protected boolean handleRemove(String id)
        throws Exception
    {
        Form entity = load(id);
        if (formSubmissionRepository.existsByFormId(entity.getId()))
        {
            throw new FormServiceException(
                    "Clients have been asked to fill this form: archive it instead of removing it");
        }
        formRepository.delete(entity);
        return true;
    }

    /**
     * @see bw.co.knowvera.form.FormService#getAll()
     */
    @Override
    protected List<FormDTO> handleGetAll()
        throws Exception
    {
        return formMapper.toFormDTOCollection(formRepository.findAll(Sort.by(Sort.Direction.ASC, "name")));
    }

    /**
     * @see bw.co.knowvera.form.FormService#findByOrganisation(String)
     */
    @Override
    protected List<FormDTO> handleFindByOrganisation(String organisationId)
        throws Exception
    {
        return formMapper.toFormDTOCollection(
                formRepository.findByOrganisationIdOrderByNameAsc(uuid(organisationId, "organisation")));
    }

    private Form load(String id)
    {
        return formRepository.findById(uuid(id, "form"))
                .orElseThrow(() -> FormServiceException.notFound("The form does not exist"));
    }

    private static UUID uuid(String id, String what)
    {
        try
        {
            return UUID.fromString(id.trim());
        }
        catch (IllegalArgumentException e)
        {
            throw FormServiceException.notFound("'" + id + "' is not the id of a " + what);
        }
    }

    /**
     * Everything that can be said about the definition without the stored form.
     */
    private static void checkDefinition(FormDTO form)
    {
        List<String> errors = new ArrayList<String>();
        if (StringUtils.isBlank(form.getCode()) || !CODE.matcher(form.getCode().trim()).matches())
        {
            errors.add("The code is required: up to 50 letters, digits, '-' or '_'");
        }
        if (StringUtils.isBlank(form.getName()))
        {
            errors.add("The name is required");
        }
        if (form.getTargetType() != TargetEntity.INDIVIDUAL && form.getTargetType() != TargetEntity.ORGANISATION)
        {
            errors.add("Say who fills the form: INDIVIDUAL or ORGANISATION clients");
        }

        Set<String> sectionKeys = new HashSet<String>();
        Set<String> fieldKeys = new HashSet<String>();
        for (FormSectionDTO section : form.getSections())
        {
            if (StringUtils.isBlank(section.getLabel()))
            {
                errors.add("Every section needs a label");
                continue;
            }
            if (StringUtils.isNotBlank(section.getSectionId()) && !sectionKeys.add(key(section.getSectionId())))
            {
                errors.add("More than one section has the id " + key(section.getSectionId()));
            }
            for (FormFieldDTO field : section.getFields())
            {
                if (StringUtils.isBlank(field.getLabel()))
                {
                    errors.add("Every field of the section '" + section.getLabel() + "' needs a label");
                    continue;
                }
                if (StringUtils.isNotBlank(field.getFieldId()) && !fieldKeys.add(key(field.getFieldId())))
                {
                    errors.add("More than one field has the id " + key(field.getFieldId()));
                }
                checkField(field, errors);
            }
        }
        if (!errors.isEmpty())
        {
            throw new FormServiceException(String.join("; ", errors));
        }
    }

    private static void checkField(FormFieldDTO field, List<String> errors)
    {
        String name = "'" + field.getLabel().trim() + "'";
        if (field.getFieldType() == null)
        {
            errors.add(name + " needs a field type");
            return;
        }
        if (field.getFieldType().hasOptions())
        {
            List<String> options = cleanOptions(field.getOptions());
            if (options.isEmpty())
            {
                errors.add(name + " needs options to choose from");
            }
        }
        if (field.getMinValue() != null && field.getMaxValue() != null
                && field.getMinValue().compareTo(field.getMaxValue()) > 0)
        {
            errors.add(name + ": the minimum value is above the maximum");
        }
        if ((field.getMinLength() != null && field.getMinLength() < 0)
                || (field.getMaxLength() != null && field.getMaxLength() < 1)
                || (field.getMinLength() != null && field.getMaxLength() != null
                        && field.getMinLength() > field.getMaxLength()))
        {
            errors.add(name + ": the length limits are not valid");
        }
        if (StringUtils.isNotBlank(field.getPattern()))
        {
            try
            {
                Pattern.compile(field.getPattern());
            }
            catch (PatternSyntaxException e)
            {
                errors.add(name + ": the pattern is not a valid regular expression");
            }
        }
    }

    /**
     * Makes the sections and fields of the stored form those of the definition: updates the ones
     * that carry an id, adds the ones that do not and removes the rest. The order in which they
     * are given is the order of the form: it sets their positions.
     */
    private void mergeSections(Form entity, List<FormSectionDTO> definition)
    {
        Map<UUID, FormSection> storedSections = new HashMap<UUID, FormSection>();
        Map<UUID, FormField> storedFields = new LinkedHashMap<UUID, FormField>();
        for (FormSection section : entity.getSections())
        {
            storedSections.put(section.getId(), section);
            section.getFields().forEach(field -> storedFields.put(field.getId(), field));
        }

        // Decided before anything changes: the query below flushes the session, and by then no
        // stored field may point to a section that is not saved yet
        Set<UUID> statedFields = new HashSet<UUID>();
        for (FormSectionDTO section : definition)
        {
            section.getFields().stream()
                    .filter(field -> StringUtils.isNotBlank(field.getId()))
                    .forEach(field -> statedFields.add(uuid(field.getId(), "field")));
        }
        List<FormField> removedFields = storedFields.values().stream()
                .filter(field -> !statedFields.contains(field.getId()))
                .toList();
        if (!removedFields.isEmpty()
                && formSubmissionRepository.countValuesOfFields(removedFields.stream().map(FormField::getId).toList()) > 0)
        {
            throw new FormServiceException("Clients have already answered "
                    + removedFields.stream().map(field -> "'" + field.getLabel() + "'").distinct()
                            .reduce((left, right) -> left + ", " + right).orElse("")
                    + ": a field with answers cannot be removed");
        }

        // Ids the definition states are kept; the ones derived from a label make way for them
        Set<String> sectionKeys = new HashSet<String>();
        Set<String> fieldKeys = new HashSet<String>();
        for (FormSectionDTO section : definition)
        {
            if (StringUtils.isNotBlank(section.getSectionId()))
            {
                sectionKeys.add(key(section.getSectionId()));
            }
            section.getFields().stream()
                    .filter(field -> StringUtils.isNotBlank(field.getFieldId()))
                    .forEach(field -> fieldKeys.add(key(field.getFieldId())));
        }

        List<FormSection> sections = new ArrayList<FormSection>();
        Map<FormSection, List<FormField>> fieldsOfSection = new LinkedHashMap<FormSection, List<FormField>>();
        Set<UUID> keptFields = new HashSet<UUID>();

        for (FormSectionDTO sectionDTO : definition)
        {
            FormSection section;
            if (StringUtils.isBlank(sectionDTO.getId()))
            {
                section = new FormSection();
                section.setForm(entity);
            }
            else
            {
                section = storedSections.get(uuid(sectionDTO.getId(), "section"));
                if (section == null || sections.contains(section))
                {
                    throw new FormServiceException(
                            "The section '" + sectionDTO.getLabel() + "' is not a section of this form");
                }
            }
            section.setLabel(sectionDTO.getLabel().trim());
            section.setDescription(StringUtils.trimToNull(sectionDTO.getDescription()));
            section.setSectionId(StringUtils.isNotBlank(sectionDTO.getSectionId())
                    ? key(sectionDTO.getSectionId()) : freeKey(sectionDTO.getLabel(), sectionKeys));
            section.setPosition(sections.size());
            sections.add(section);

            List<FormField> fields = new ArrayList<FormField>();
            for (FormFieldDTO fieldDTO : sectionDTO.getFields())
            {
                FormField field;
                if (StringUtils.isBlank(fieldDTO.getId()))
                {
                    field = new FormField();
                }
                else
                {
                    // A stored field may come back under another section: it moves with its answers
                    field = storedFields.get(uuid(fieldDTO.getId(), "field"));
                    if (field == null || !keptFields.add(field.getId()))
                    {
                        throw new FormServiceException(
                                "The field '" + fieldDTO.getLabel() + "' is not a field of this form");
                    }
                }
                apply(fieldDTO, field);
                field.setFieldId(StringUtils.isNotBlank(fieldDTO.getFieldId())
                        ? key(fieldDTO.getFieldId()) : freeKey(fieldDTO.getLabel(), fieldKeys));
                field.setPosition(fields.size());
                field.setSection(section);
                fields.add(field);
            }
            fieldsOfSection.put(section, fields);
        }

        // The stored collections are changed in place: Hibernate tracks them
        entity.getSections().forEach(section -> section.getFields().clear());
        entity.getSections().clear();
        entity.getSections().addAll(sections);
        fieldsOfSection.forEach((section, fields) -> section.getFields().addAll(fields));
        if (!removedFields.isEmpty())
        {
            formFieldRepository.deleteAll(removedFields);
        }
    }

    private static void apply(FormFieldDTO source, FormField target)
    {
        target.setLabel(source.getLabel().trim());
        target.setDescription(StringUtils.trimToNull(source.getDescription()));
        target.setFieldType(source.getFieldType());
        target.setRequired(Boolean.TRUE.equals(source.getRequired()));
        target.setDefaultValue(StringUtils.trimToNull(source.getDefaultValue()));
        target.setPlaceholder(StringUtils.trimToNull(source.getPlaceholder()));
        target.setMinValue(source.getMinValue());
        target.setMaxValue(source.getMaxValue());
        target.setMinLength(source.getMinLength());
        target.setMaxLength(source.getMaxLength());
        target.setPattern(StringUtils.trimToNull(source.getPattern()));
        target.setOptions(source.getFieldType().hasOptions() ? cleanOptions(source.getOptions()) : null);
    }

    private static List<String> cleanOptions(List<String> options)
    {
        return options == null ? List.of() : options.stream()
                .filter(StringUtils::isNotBlank)
                .map(String::trim)
                .distinct()
                .toList();
    }

    /** An id as it is stored: lower case letters, digits and underscores. */
    static String key(String text)
    {
        String key = StringUtils.strip(
                StringUtils.stripAccents(text).toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9]+", "_"), "_");
        return StringUtils.left(StringUtils.defaultIfBlank(key, "field"), MAX_KEY_LENGTH);
    }

    /** The key of the label, numbered when another section or field already has it. */
    private static String freeKey(String label, Set<String> taken)
    {
        String base = key(label);
        String candidate = base;
        for (int number = 2; !taken.add(candidate); number++)
        {
            String suffix = "_" + number;
            candidate = StringUtils.left(base, MAX_KEY_LENGTH - suffix.length()) + suffix;
        }
        return candidate;
    }
}
