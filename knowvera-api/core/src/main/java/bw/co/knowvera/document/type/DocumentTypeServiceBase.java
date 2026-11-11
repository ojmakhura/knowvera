package bw.co.knowvera.document.type;

import bw.co.knowvera.document.type.field.ExpectedFieldDTO;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;

/**
 * <p>
 * Spring Service base class for <code>DocumentTypeService</code>,
 * provides access to all services and entities referenced by this service.
 * </p>
 *
 * @see DocumentTypeService
 */
@CacheConfig(cacheNames = "documentTypes")
public abstract class DocumentTypeServiceBase
    implements DocumentTypeService
{
    protected Logger logger = LoggerFactory.getLogger(DocumentTypeService.class);
    protected final DocumentTypeRepository documentTypeRepository;
    protected final DocumentTypeMapper documentTypeMapper;

    public DocumentTypeServiceBase(
        DocumentTypeRepository documentTypeRepository,
        DocumentTypeMapper documentTypeMapper    ) {
        this.documentTypeRepository = documentTypeRepository;
        this.documentTypeMapper = documentTypeMapper;
    }

    /**
     * Gets the reference to <code>documentType</code>'s repository.
     * @return documentTypeRepository
     */
    public DocumentTypeRepository getDocumentTypeRepository() {
        return documentTypeRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Cacheable(key = "#id")
    public DocumentTypeDTO findById(final String id)
    {
        if (id == null || id.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.document.type.DocumentTypeService.findById(String id) - 'id' can not be null or empty");
        }
        try
        {
            return this.handleFindById(id);
        }
        catch (Throwable th)
        {
            throw new DocumentTypeServiceException(
                "Error performing 'DocumentTypeService.findById(String id)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #findById(String)}
     * @param id java.lang.String TODO: Model Documentation for DocumentTypeService.findById(id)
     * @return DocumentTypeDTO
     * @throws Exception
     */
    protected abstract DocumentTypeDTO handleFindById(String id)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    @CacheEvict(cacheNames = {"branches", "clientRequests", "documentTypes", "documents", "employmentRecords", "expectedFields", "individuals", "invoices", "kycRecords", "organisations", "settings", "subscriptions", "verificationDataConfigurations"}, allEntries = true)
    public DocumentTypeDTO save(final @Valid DocumentTypeDTO documentType)
    {
        if (documentType == null)
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.document.type.DocumentTypeService.save(@Valid DocumentTypeDTO documentType) - 'documentType' can not be null");
        }
        if (documentType.getCode() == null || documentType.getCode().trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.document.type.DocumentTypeService.save(@Valid DocumentTypeDTO documentType) - 'documentType.code' can not be null or empty");
        }
        if (documentType.getName() == null || documentType.getName().trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.document.type.DocumentTypeService.save(@Valid DocumentTypeDTO documentType) - 'documentType.name' can not be null or empty");
        }
        try
        {
            return this.handleSave(documentType);
        }
        catch (Throwable th)
        {
            throw new DocumentTypeServiceException(
                "Error performing 'DocumentTypeService.save(@Valid DocumentTypeDTO documentType)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #save(@Valid DocumentTypeDTO)}
     * @param documentType DocumentTypeDTO TODO: Model Documentation for DocumentTypeService.save(documentType)
     * @return DocumentTypeDTO
     * @throws Exception
     */
    protected abstract DocumentTypeDTO handleSave(@Valid DocumentTypeDTO documentType)
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
                "bw.co.knowvera.document.type.DocumentTypeService.remove(String id) - 'id' can not be null or empty");
        }
        try
        {
            return this.handleRemove(id);
        }
        catch (Throwable th)
        {
            throw new DocumentTypeServiceException(
                "Error performing 'DocumentTypeService.remove(String id)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #remove(String)}
     * @param id java.lang.String TODO: Model Documentation for DocumentTypeService.remove(id)
     * @return boolean
     * @throws Exception
     */
    protected abstract boolean handleRemove(String id)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    @Cacheable(key = "'all'")
    public List<DocumentTypeDTO> getAll()
    {
        try
        {
            return this.handleGetAll();
        }
        catch (Throwable th)
        {
            throw new DocumentTypeServiceException(
                "Error performing 'DocumentTypeService.getAll()' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #getAll()}
     * @return List<DocumentTypeDTO>
     * @throws Exception
     */
    protected abstract List<DocumentTypeDTO> handleGetAll()
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<DocumentTypeDTO> search(final String criteria)
    {
        try
        {
            return this.handleSearch(criteria);
        }
        catch (Throwable th)
        {
            throw new DocumentTypeServiceException(
                "Error performing 'DocumentTypeService.search(String criteria)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #search(String)}
     * @param criteria java.lang.String TODO: Model Documentation for DocumentTypeService.search(criteria)
     * @return List<DocumentTypeDTO>
     * @throws Exception
     */
    protected abstract List<DocumentTypeDTO> handleSearch(String criteria)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<DocumentTypeDTO> getAll(final Integer pageNumber, final Integer pageSize)
    {
        try
        {
            return this.handleGetAll(pageNumber, pageSize);
        }
        catch (Throwable th)
        {
            throw new DocumentTypeServiceException(
                "Error performing 'DocumentTypeService.getAll(Integer pageNumber, Integer pageSize)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #getAll(Integer, Integer)}
     * @param pageNumber java.lang.Integer TODO: Model Documentation for DocumentTypeService.getAll(pageNumber)
     * @param pageSize java.lang.Integer TODO: Model Documentation for DocumentTypeService.getAll(pageSize)
     * @return Page<DocumentTypeDTO>
     * @throws Exception
     */
    protected abstract Page<DocumentTypeDTO> handleGetAll(Integer pageNumber, Integer pageSize)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<DocumentTypeDTO> search(final String criteria, final Integer pageNumber, final Integer pageSize)
    {
        try
        {
            return this.handleSearch(criteria, pageNumber, pageSize);
        }
        catch (Throwable th)
        {
            throw new DocumentTypeServiceException(
                "Error performing 'DocumentTypeService.search(String criteria, Integer pageNumber, Integer pageSize)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #search(String, Integer, Integer)}
     * @param criteria java.lang.String TODO: Model Documentation for DocumentTypeService.search(criteria)
     * @param pageNumber java.lang.Integer TODO: Model Documentation for DocumentTypeService.search(pageNumber)
     * @param pageSize java.lang.Integer TODO: Model Documentation for DocumentTypeService.search(pageSize)
     * @return Page<DocumentTypeDTO>
     * @throws Exception
     */
    protected abstract Page<DocumentTypeDTO> handleSearch(String criteria, Integer pageNumber, Integer pageSize)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    @CacheEvict(cacheNames = {"branches", "clientRequests", "documentTypes", "documents", "employmentRecords", "expectedFields", "individuals", "invoices", "kycRecords", "organisations", "settings", "subscriptions", "verificationDataConfigurations"}, allEntries = true)
    public DocumentTypeDTO addExpectedField(final String id, final @Valid Set<ExpectedFieldDTO> expectedFields)
    {
        if (id == null || id.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.document.type.DocumentTypeService.addExpectedField(String id, @Valid Set<ExpectedFieldDTO> expectedFields) - 'id' can not be null or empty");
        }
        if (expectedFields == null)
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.document.type.DocumentTypeService.addExpectedField(String id, @Valid Set<ExpectedFieldDTO> expectedFields) - 'expectedFields' can not be null");
        }
        try
        {
            return this.handleAddExpectedField(id, expectedFields);
        }
        catch (Throwable th)
        {
            throw new DocumentTypeServiceException(
                "Error performing 'DocumentTypeService.addExpectedField(String id, @Valid Set<ExpectedFieldDTO> expectedFields)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #addExpectedField(String, @Valid Set<ExpectedFieldDTO>)}
     * @param id java.lang.String TODO: Model Documentation for DocumentTypeService.addExpectedField(id)
     * @param expectedFields ExpectedFieldDTO TODO: Model Documentation for
DocumentTypeService.addExpectedField(expectedFields)
     * @return DocumentTypeDTO
     * @throws Exception
     */
    protected abstract DocumentTypeDTO handleAddExpectedField(String id, @Valid Set<ExpectedFieldDTO> expectedFields)
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