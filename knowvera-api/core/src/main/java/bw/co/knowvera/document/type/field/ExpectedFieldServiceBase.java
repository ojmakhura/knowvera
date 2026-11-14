package bw.co.knowvera.document.type.field;

import jakarta.validation.Valid;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;

/**
 * <p>
 * Spring Service base class for <code>ExpectedFieldService</code>,
 * provides access to all services and entities referenced by this service.
 * </p>
 *
 * @see ExpectedFieldService
 */
@CacheConfig(cacheNames = "expectedFields")
public abstract class ExpectedFieldServiceBase
    implements ExpectedFieldService
{
    protected Logger logger = LoggerFactory.getLogger(ExpectedFieldService.class);
    protected final ExpectedFieldRepository expectedFieldRepository;
    protected final ExpectedFieldMapper expectedFieldMapper;

    public ExpectedFieldServiceBase(
        ExpectedFieldRepository expectedFieldRepository,
        ExpectedFieldMapper expectedFieldMapper    ) {
        this.expectedFieldRepository = expectedFieldRepository;
        this.expectedFieldMapper = expectedFieldMapper;
    }

    /**
     * Gets the reference to <code>expectedField</code>'s repository.
     * @return expectedFieldRepository
     */
    public ExpectedFieldRepository getExpectedFieldRepository() {
        return expectedFieldRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Cacheable(key = "#id")
    public ExpectedFieldDTO findById(final String id)
    {
        if (id == null || id.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.document.type.field.ExpectedFieldService.findById(String id) - 'id' can not be null or empty");
        }
        try
        {
            return this.handleFindById(id);
        }
        catch (Throwable th)
        {
            throw new ExpectedFieldServiceException(
                "Error performing 'ExpectedFieldService.findById(String id)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #findById(String)}
     * @param id java.lang.String TODO: Model Documentation for ExpectedFieldService.findById(id)
     * @return ExpectedFieldDTO
     * @throws Exception
     */
    protected abstract ExpectedFieldDTO handleFindById(String id)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    @Caching(evict = { @CacheEvict(cacheNames = {"branches", "clientRequests", "documentTypes", "documents", "employmentRecords", "expectedFields", "individuals", "invoices", "kycRecords", "organisations", "settings", "subscriptions", "verificationDataConfigurations"}, allEntries = true, beforeInvocation = true) }, put = { @CachePut(key = "#result.id", unless = "#result == null") })
    public ExpectedFieldDTO save(final @Valid ExpectedFieldDTO expectedField)
    {
        if (expectedField == null)
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.document.type.field.ExpectedFieldService.save(@Valid ExpectedFieldDTO expectedField) - 'expectedField' can not be null");
        }
        if (expectedField.getField() == null || expectedField.getField().trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.document.type.field.ExpectedFieldService.save(@Valid ExpectedFieldDTO expectedField) - 'expectedField.field' can not be null or empty");
        }
        if (expectedField.getTargetType() == null)
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.document.type.field.ExpectedFieldService.save(@Valid ExpectedFieldDTO expectedField) - 'expectedField.targetType' can not be null");
        }
        if (expectedField.getFieldType() == null)
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.document.type.field.ExpectedFieldService.save(@Valid ExpectedFieldDTO expectedField) - 'expectedField.fieldType' can not be null");
        }
        try
        {
            return this.handleSave(expectedField);
        }
        catch (Throwable th)
        {
            throw new ExpectedFieldServiceException(
                "Error performing 'ExpectedFieldService.save(@Valid ExpectedFieldDTO expectedField)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #save(@Valid ExpectedFieldDTO)}
     * @param expectedField ExpectedFieldDTO TODO: Model Documentation for
ExpectedFieldService.save(expectedField)
     * @return ExpectedFieldDTO
     * @throws Exception
     */
    protected abstract ExpectedFieldDTO handleSave(@Valid ExpectedFieldDTO expectedField)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    @CacheEvict(cacheNames = {"branches", "clientRequests", "documentTypes", "documents", "employmentRecords", "expectedFields", "individuals", "invoices", "kycRecords", "organisations", "settings", "subscriptions", "verificationDataConfigurations"}, allEntries = true)
    public boolean remove(final String id)
    {
        if (id == null || id.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.document.type.field.ExpectedFieldService.remove(String id) - 'id' can not be null or empty");
        }
        try
        {
            return this.handleRemove(id);
        }
        catch (Throwable th)
        {
            throw new ExpectedFieldServiceException(
                "Error performing 'ExpectedFieldService.remove(String id)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #remove(String)}
     * @param id java.lang.String TODO: Model Documentation for ExpectedFieldService.remove(id)
     * @return boolean
     * @throws Exception
     */
    protected abstract boolean handleRemove(String id)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<ExpectedFieldDTO> findByDocumentType(final List<String> documentTypeIds, final Integer pageNumber, final Integer pageSize)
    {
        try
        {
            return this.handleFindByDocumentType(documentTypeIds, pageNumber, pageSize);
        }
        catch (Throwable th)
        {
            throw new ExpectedFieldServiceException(
                "Error performing 'ExpectedFieldService.findByDocumentType(List<String> documentTypeIds, Integer pageNumber, Integer pageSize)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #findByDocumentType(List<String>, Integer, Integer)}
     * @param documentTypeIds java.lang.String TODO: Model Documentation for
ExpectedFieldService.findByDocumentType(documentTypeIds)
     * @param pageNumber java.lang.Integer TODO: Model Documentation for
ExpectedFieldService.findByDocumentType(pageNumber)
     * @param pageSize java.lang.Integer TODO: Model Documentation for
ExpectedFieldService.findByDocumentType(pageSize)
     * @return Page<ExpectedFieldDTO>
     * @throws Exception
     */
    protected abstract Page<ExpectedFieldDTO> handleFindByDocumentType(List<String> documentTypeIds, Integer pageNumber, Integer pageSize)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ExpectedFieldDTO> findByDocumentType(final List<String> documentTypeIds)
    {
        try
        {
            return this.handleFindByDocumentType(documentTypeIds);
        }
        catch (Throwable th)
        {
            throw new ExpectedFieldServiceException(
                "Error performing 'ExpectedFieldService.findByDocumentType(List<String> documentTypeIds)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #findByDocumentType(List<String>)}
     * @param documentTypeIds java.lang.String TODO: Model Documentation for
ExpectedFieldService.findByDocumentType(documentTypeIds)
     * @return List<ExpectedFieldDTO>
     * @throws Exception
     */
    protected abstract List<ExpectedFieldDTO> handleFindByDocumentType(List<String> documentTypeIds)
        throws Exception;

    /**
     * Gets the current <code>principal</code> if one has been set,
     * otherwise returns <code>null</code>.
     *
     * @return the current principal
     */
    /**protected java.security.Principal getPrincipal()
    {
        return bw.co.knowvera.PrincipalStore.get();
    }*/


}