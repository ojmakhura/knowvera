package bw.co.knowvera.form.submission;

import jakarta.validation.Valid;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * Spring Service base class for <code>FormSubmissionService</code>,
 * provides access to all services and entities referenced by this service.
 * </p>
 *
 * @see FormSubmissionService
 */
public abstract class FormSubmissionServiceBase
    implements FormSubmissionService
{
    protected Logger logger = LoggerFactory.getLogger(FormSubmissionService.class);
    protected final FormSubmissionRepository formSubmissionRepository;
    protected final FormSubmissionMapper formSubmissionMapper;

    public FormSubmissionServiceBase(
        FormSubmissionRepository formSubmissionRepository,
        FormSubmissionMapper formSubmissionMapper    ) {
        this.formSubmissionRepository = formSubmissionRepository;
        this.formSubmissionMapper = formSubmissionMapper;
    }

    /**
     * Gets the reference to <code>formSubmission</code>'s repository.
     * @return formSubmissionRepository
     */
    public FormSubmissionRepository getFormSubmissionRepository() {
        return formSubmissionRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FormSubmissionDTO createSubmission(final @Valid FormAssignmentDTO assignment, final String user)
    {
        if (assignment == null)
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.form.submission.FormSubmissionService.createSubmission(@Valid FormAssignmentDTO assignment, String user) - 'assignment' can not be null");
        }
        if (user == null || user.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.form.submission.FormSubmissionService.createSubmission(@Valid FormAssignmentDTO assignment, String user) - 'user' can not be null or empty");
        }
        try
        {
            return this.handleCreateSubmission(assignment, user);
        }
        catch (Throwable th)
        {
            throw failure("FormSubmissionService.createSubmission(@Valid FormAssignmentDTO assignment, String user)", th);
        }
    }

    protected abstract FormSubmissionDTO handleCreateSubmission(@Valid FormAssignmentDTO assignment, String user)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    public FormSubmissionDTO findById(final String id)
    {
        if (id == null || id.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.form.submission.FormSubmissionService.findById(String id) - 'id' can not be null or empty");
        }
        try
        {
            return this.handleFindById(id);
        }
        catch (Throwable th)
        {
            throw failure("FormSubmissionService.findById(String id)", th);
        }
    }

    protected abstract FormSubmissionDTO handleFindById(String id)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<FormSubmissionDTO> findByForm(final String formId)
    {
        if (formId == null || formId.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.form.submission.FormSubmissionService.findByForm(String formId) - 'formId' can not be null or empty");
        }
        try
        {
            return this.handleFindByForm(formId);
        }
        catch (Throwable th)
        {
            throw failure("FormSubmissionService.findByForm(String formId)", th);
        }
    }

    protected abstract List<FormSubmissionDTO> handleFindByForm(String formId)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<FormSubmissionDTO> findByOrganisation(final String organisationId)
    {
        if (organisationId == null || organisationId.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.form.submission.FormSubmissionService.findByOrganisation(String organisationId) - 'organisationId' can not be null or empty");
        }
        try
        {
            return this.handleFindByOrganisation(organisationId);
        }
        catch (Throwable th)
        {
            throw failure("FormSubmissionService.findByOrganisation(String organisationId)", th);
        }
    }

    protected abstract List<FormSubmissionDTO> handleFindByOrganisation(String organisationId)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<FormSubmissionDTO> findByKycRecord(final String kycRecordId)
    {
        if (kycRecordId == null || kycRecordId.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.form.submission.FormSubmissionService.findByKycRecord(String kycRecordId) - 'kycRecordId' can not be null or empty");
        }
        try
        {
            return this.handleFindByKycRecord(kycRecordId);
        }
        catch (Throwable th)
        {
            throw failure("FormSubmissionService.findByKycRecord(String kycRecordId)", th);
        }
    }

    protected abstract List<FormSubmissionDTO> handleFindByKycRecord(String kycRecordId)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<FormSubmissionDTO> search(final FormSubmissionSearchCriteria criteria)
    {
        if (criteria == null)
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.form.submission.FormSubmissionService.search(FormSubmissionSearchCriteria criteria) - 'criteria' can not be null");
        }
        try
        {
            return this.handleSearch(criteria);
        }
        catch (Throwable th)
        {
            throw failure("FormSubmissionService.search(FormSubmissionSearchCriteria criteria)", th);
        }
    }

    protected abstract List<FormSubmissionDTO> handleSearch(FormSubmissionSearchCriteria criteria)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    public FormSubmissionDTO saveValues(final String id, final List<FormFieldValueDTO> values, final String user)
    {
        if (id == null || id.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.form.submission.FormSubmissionService.saveValues(String id, List<FormFieldValueDTO> values, String user) - 'id' can not be null or empty");
        }
        if (values == null)
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.form.submission.FormSubmissionService.saveValues(String id, List<FormFieldValueDTO> values, String user) - 'values' can not be null");
        }
        if (user == null || user.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.form.submission.FormSubmissionService.saveValues(String id, List<FormFieldValueDTO> values, String user) - 'user' can not be null or empty");
        }
        try
        {
            return this.handleSaveValues(id, values, user);
        }
        catch (Throwable th)
        {
            throw failure("FormSubmissionService.saveValues(String id, List<FormFieldValueDTO> values, String user)", th);
        }
    }

    protected abstract FormSubmissionDTO handleSaveValues(String id, List<FormFieldValueDTO> values, String user)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    public FormSubmissionDTO submit(final String id, final String user)
    {
        if (id == null || id.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.form.submission.FormSubmissionService.submit(String id, String user) - 'id' can not be null or empty");
        }
        if (user == null || user.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.form.submission.FormSubmissionService.submit(String id, String user) - 'user' can not be null or empty");
        }
        try
        {
            return this.handleSubmit(id, user);
        }
        catch (Throwable th)
        {
            throw failure("FormSubmissionService.submit(String id, String user)", th);
        }
    }

    protected abstract FormSubmissionDTO handleSubmit(String id, String user)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    public FormSubmissionDTO updateStatus(final String id, final FormSubmissionStatus status, final String note, final String user)
    {
        if (id == null || id.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.form.submission.FormSubmissionService.updateStatus(String id, FormSubmissionStatus status, String note, String user) - 'id' can not be null or empty");
        }
        if (status == null)
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.form.submission.FormSubmissionService.updateStatus(String id, FormSubmissionStatus status, String note, String user) - 'status' can not be null");
        }
        if (user == null || user.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.form.submission.FormSubmissionService.updateStatus(String id, FormSubmissionStatus status, String note, String user) - 'user' can not be null or empty");
        }
        try
        {
            return this.handleUpdateStatus(id, status, note, user);
        }
        catch (Throwable th)
        {
            throw failure("FormSubmissionService.updateStatus(String id, FormSubmissionStatus status, String note, String user)", th);
        }
    }

    protected abstract FormSubmissionDTO handleUpdateStatus(String id, FormSubmissionStatus status, String note, String user)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean remove(final String id)
    {
        if (id == null || id.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.form.submission.FormSubmissionService.remove(String id) - 'id' can not be null or empty");
        }
        try
        {
            return this.handleRemove(id);
        }
        catch (Throwable th)
        {
            throw failure("FormSubmissionService.remove(String id)", th);
        }
    }

    protected abstract boolean handleRemove(String id)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<FormSubmissionDTO> findRequired(final String kycRecordId, final String user)
    {
        if (kycRecordId == null || kycRecordId.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.form.submission.FormSubmissionService.findRequired(String kycRecordId, String user) - 'kycRecordId' can not be null or empty");
        }
        if (user == null || user.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.form.submission.FormSubmissionService.findRequired(String kycRecordId, String user) - 'user' can not be null or empty");
        }
        try
        {
            return this.handleFindRequired(kycRecordId, user);
        }
        catch (Throwable th)
        {
            throw failure("FormSubmissionService.findRequired(String kycRecordId, String user)", th);
        }
    }

    protected abstract List<FormSubmissionDTO> handleFindRequired(String kycRecordId, String user)
        throws Exception;

    /**
     * A rule the caller broke is passed on as it is, so that its message reaches the user;
     * anything else is wrapped.
     */
    private static FormSubmissionServiceException failure(String operation, Throwable th)
    {
        if (th instanceof FormSubmissionServiceException rule)
        {
            return rule;
        }
        return new FormSubmissionServiceException("Error performing '" + operation + "' --> " + th, th);
    }
}
