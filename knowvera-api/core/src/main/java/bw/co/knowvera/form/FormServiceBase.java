package bw.co.knowvera.form;

import bw.co.knowvera.form.field.FormFieldRepository;
import jakarta.validation.Valid;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * Spring Service base class for <code>FormService</code>,
 * provides access to all services and entities referenced by this service.
 * </p>
 *
 * @see FormService
 */
public abstract class FormServiceBase
    implements FormService
{
    protected Logger logger = LoggerFactory.getLogger(FormService.class);
    protected final FormRepository formRepository;
    protected final FormMapper formMapper;
    protected final FormFieldRepository formFieldRepository;

    public FormServiceBase(
        FormRepository formRepository,
        FormMapper formMapper,
        FormFieldRepository formFieldRepository    ) {
        this.formRepository = formRepository;
        this.formMapper = formMapper;
        this.formFieldRepository = formFieldRepository;
    }

    /**
     * Gets the reference to <code>form</code>'s repository.
     * @return formRepository
     */
    public FormRepository getFormRepository() {
        return formRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FormDTO findById(final String id)
    {
        if (id == null || id.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.form.FormService.findById(String id) - 'id' can not be null or empty");
        }
        try
        {
            return this.handleFindById(id);
        }
        catch (Throwable th)
        {
            throw failure("FormService.findById(String id)", th);
        }
    }

    protected abstract FormDTO handleFindById(String id)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    public FormDTO save(final @Valid FormDTO form)
    {
        if (form == null)
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.form.FormService.save(@Valid FormDTO form) - 'form' can not be null");
        }
        try
        {
            return this.handleSave(form);
        }
        catch (Throwable th)
        {
            throw failure("FormService.save(@Valid FormDTO form)", th);
        }
    }

    protected abstract FormDTO handleSave(@Valid FormDTO form)
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
                "bw.co.knowvera.form.FormService.remove(String id) - 'id' can not be null or empty");
        }
        try
        {
            return this.handleRemove(id);
        }
        catch (Throwable th)
        {
            throw failure("FormService.remove(String id)", th);
        }
    }

    protected abstract boolean handleRemove(String id)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<FormDTO> getAll()
    {
        try
        {
            return this.handleGetAll();
        }
        catch (Throwable th)
        {
            throw failure("FormService.getAll()", th);
        }
    }

    protected abstract List<FormDTO> handleGetAll()
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<FormDTO> findByOrganisation(final String organisationId)
    {
        if (organisationId == null || organisationId.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.form.FormService.findByOrganisation(String organisationId) - 'organisationId' can not be null or empty");
        }
        try
        {
            return this.handleFindByOrganisation(organisationId);
        }
        catch (Throwable th)
        {
            throw failure("FormService.findByOrganisation(String organisationId)", th);
        }
    }

    protected abstract List<FormDTO> handleFindByOrganisation(String organisationId)
        throws Exception;

    /**
     * A rule the caller broke is passed on as it is, so that its message reaches the user;
     * anything else is wrapped.
     */
    private static FormServiceException failure(String operation, Throwable th)
    {
        if (th instanceof FormServiceException rule)
        {
            return rule;
        }
        return new FormServiceException("Error performing '" + operation + "' --> " + th, th);
    }
}
