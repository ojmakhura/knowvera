package bw.co.knowvera.organisation.document;

import bw.co.knowvera.PropertySearchOrder;
import bw.co.knowvera.SearchObject;
import bw.co.knowvera.document.DocumentMapper;
import bw.co.knowvera.document.DocumentRepository;
import bw.co.knowvera.individual.IndividualMapper;
import bw.co.knowvera.individual.IndividualRepository;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Set;
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
 * Spring Service base class for <code>OrganisationDocumentService</code>,
 * provides access to all services and entities referenced by this service.
 * </p>
 *
 * @see OrganisationDocumentService
 */
@CacheConfig(cacheNames = "organisationDocuments")
public abstract class OrganisationDocumentServiceBase
    implements OrganisationDocumentService
{
    protected Logger logger = LoggerFactory.getLogger(OrganisationDocumentService.class);
    protected final OrganisationDocumentRepository organisationDocumentRepository;
    protected final OrganisationDocumentMapper organisationDocumentMapper;
    protected final IndividualRepository individualRepository;
    protected final IndividualMapper individualMapper;
    protected final DocumentRepository documentRepository;
    protected final DocumentMapper documentMapper;

    public OrganisationDocumentServiceBase(
        OrganisationDocumentRepository organisationDocumentRepository,
        OrganisationDocumentMapper organisationDocumentMapper,         IndividualRepository individualRepository,
        IndividualMapper individualMapper,         DocumentRepository documentRepository,
        DocumentMapper documentMapper    ) {
        this.organisationDocumentRepository = organisationDocumentRepository;
        this.organisationDocumentMapper = organisationDocumentMapper;
        this.individualRepository = individualRepository;
        this.individualMapper = individualMapper;
        this.documentRepository = documentRepository;
        this.documentMapper = documentMapper;
    }

    /**
     * Gets the reference to <code>organisationDocument</code>'s repository.
     * @return organisationDocumentRepository
     */
    public OrganisationDocumentRepository getOrganisationDocumentRepository() {
        return organisationDocumentRepository;
    }

    /**
     * Gets the reference to <code>individual</code>'s repository.
     * @return individualRepository
     */
    public IndividualRepository getIndividualRepository() {
        return individualRepository;
    }

    /**
     * Gets the reference to <code>document</code>'s repository.
     * @return documentRepository
     */
    public DocumentRepository getDocumentRepository() {
        return documentRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Cacheable(key = "#id")
    public OrganisationDocumentDTO findById(final String id)
    {
        if (id == null || id.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.organisation.document.OrganisationDocumentService.findById(String id) - 'id' can not be null or empty");
        }
        try
        {
            return this.handleFindById(id);
        }
        catch (Throwable th)
        {
            throw new OrganisationDocumentServiceException(
                "Error performing 'OrganisationDocumentService.findById(String id)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #findById(String)}
     * @param id java.lang.String TODO: Model Documentation for
OrganisationDocumentService.findById(id)
     * @return OrganisationDocumentDTO
     * @throws Exception
     */
    protected abstract OrganisationDocumentDTO handleFindById(String id)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    @Caching(evict = { @CacheEvict(cacheNames = {"organisationDocuments"}, allEntries = true, beforeInvocation = true) }, put = { @CachePut(key = "#result.id", unless = "#result == null") })
    public OrganisationDocumentDTO save(final @Valid OrganisationDocumentDTO clientRequest)
    {
        if (clientRequest == null)
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.organisation.document.OrganisationDocumentService.save(@Valid OrganisationDocumentDTO clientRequest) - 'clientRequest' can not be null");
        }
        if (clientRequest.getStatus() == null)
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.organisation.document.OrganisationDocumentService.save(@Valid OrganisationDocumentDTO clientRequest) - 'clientRequest.status' can not be null");
        }
        if (clientRequest.getOrganisationId() == null || clientRequest.getOrganisationId().trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.organisation.document.OrganisationDocumentService.save(@Valid OrganisationDocumentDTO clientRequest) - 'clientRequest.organisationId' can not be null or empty");
        }
        if (clientRequest.getOrganisation() == null || clientRequest.getOrganisation().trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.organisation.document.OrganisationDocumentService.save(@Valid OrganisationDocumentDTO clientRequest) - 'clientRequest.organisation' can not be null or empty");
        }
        if (clientRequest.getOrganisationRegistrationNo() == null || clientRequest.getOrganisationRegistrationNo().trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.organisation.document.OrganisationDocumentService.save(@Valid OrganisationDocumentDTO clientRequest) - 'clientRequest.organisationRegistrationNo' can not be null or empty");
        }
        if (clientRequest.getTarget() == null)
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.organisation.document.OrganisationDocumentService.save(@Valid OrganisationDocumentDTO clientRequest) - 'clientRequest.target' can not be null");
        }
        if (clientRequest.getTargetId() == null || clientRequest.getTargetId().trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.organisation.document.OrganisationDocumentService.save(@Valid OrganisationDocumentDTO clientRequest) - 'clientRequest.targetId' can not be null or empty");
        }
        if (clientRequest.getFileName() == null || clientRequest.getFileName().trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.organisation.document.OrganisationDocumentService.save(@Valid OrganisationDocumentDTO clientRequest) - 'clientRequest.fileName' can not be null or empty");
        }
        try
        {
            return this.handleSave(clientRequest);
        }
        catch (Throwable th)
        {
            throw new OrganisationDocumentServiceException(
                "Error performing 'OrganisationDocumentService.save(@Valid OrganisationDocumentDTO clientRequest)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #save(@Valid OrganisationDocumentDTO)}
     * @param clientRequest OrganisationDocumentDTO TODO: Model Documentation for
OrganisationDocumentService.save(clientRequest)
     * @return OrganisationDocumentDTO
     * @throws Exception
     */
    protected abstract OrganisationDocumentDTO handleSave(@Valid OrganisationDocumentDTO clientRequest)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    @CacheEvict(cacheNames = {"organisationDocuments"}, allEntries = true)
    public boolean remove(final String id)
    {
        if (id == null || id.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.organisation.document.OrganisationDocumentService.remove(String id) - 'id' can not be null or empty");
        }
        try
        {
            return this.handleRemove(id);
        }
        catch (Throwable th)
        {
            throw new OrganisationDocumentServiceException(
                "Error performing 'OrganisationDocumentService.remove(String id)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #remove(String)}
     * @param id java.lang.String TODO: Model Documentation for
OrganisationDocumentService.remove(id)
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
    public List<OrganisationDocumentDTO> getAll()
    {
        try
        {
            return this.handleGetAll();
        }
        catch (Throwable th)
        {
            throw new OrganisationDocumentServiceException(
                "Error performing 'OrganisationDocumentService.getAll()' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #getAll()}
     * @return List<OrganisationDocumentDTO>
     * @throws Exception
     */
    protected abstract List<OrganisationDocumentDTO> handleGetAll()
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<OrganisationDocumentDTO> search(final @Valid OrganisationDocumentSearchCriteria criteria, final @Valid Set<PropertySearchOrder> sortProperties)
    {
        if (criteria == null)
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.organisation.document.OrganisationDocumentService.search(@Valid OrganisationDocumentSearchCriteria criteria, @Valid Set<PropertySearchOrder> sortProperties) - 'criteria' can not be null");
        }
        try
        {
            return this.handleSearch(criteria, sortProperties);
        }
        catch (Throwable th)
        {
            throw new OrganisationDocumentServiceException(
                "Error performing 'OrganisationDocumentService.search(@Valid OrganisationDocumentSearchCriteria criteria, @Valid Set<PropertySearchOrder> sortProperties)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #search(@Valid OrganisationDocumentSearchCriteria, @Valid Set<PropertySearchOrder>)}
     * @param criteria OrganisationDocumentSearchCriteria TODO: Model Documentation for
OrganisationDocumentService.search(criteria)
     * @param sortProperties PropertySearchOrder TODO: Model Documentation for
OrganisationDocumentService.search(sortProperties)
     * @return List<OrganisationDocumentDTO>
     * @throws Exception
     */
    protected abstract List<OrganisationDocumentDTO> handleSearch(@Valid OrganisationDocumentSearchCriteria criteria, @Valid Set<PropertySearchOrder> sortProperties)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<OrganisationDocumentDTO> getAll(final Integer pageNumber, final Integer pageSize)
    {
        try
        {
            return this.handleGetAll(pageNumber, pageSize);
        }
        catch (Throwable th)
        {
            throw new OrganisationDocumentServiceException(
                "Error performing 'OrganisationDocumentService.getAll(Integer pageNumber, Integer pageSize)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #getAll(Integer, Integer)}
     * @param pageNumber java.lang.Integer TODO: Model Documentation for
OrganisationDocumentService.getAll(pageNumber)
     * @param pageSize java.lang.Integer TODO: Model Documentation for
OrganisationDocumentService.getAll(pageSize)
     * @return Page<OrganisationDocumentDTO>
     * @throws Exception
     */
    protected abstract Page<OrganisationDocumentDTO> handleGetAll(Integer pageNumber, Integer pageSize)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<OrganisationDocumentDTO> search(final SearchObject<OrganisationDocumentSearchCriteria> criteria)
    {
        if (criteria == null)
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.organisation.document.OrganisationDocumentService.search(SearchObject<OrganisationDocumentSearchCriteria> criteria) - 'criteria' can not be null");
        }
        try
        {
            return this.handleSearch(criteria);
        }
        catch (Throwable th)
        {
            throw new OrganisationDocumentServiceException(
                "Error performing 'OrganisationDocumentService.search(SearchObject<OrganisationDocumentSearchCriteria> criteria)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #search(SearchObject<OrganisationDocumentSearchCriteria>)}
     * @param criteria SearchObject<OrganisationDocumentSearchCriteria> TODO: Model Documentation for
OrganisationDocumentService.search(criteria)
     * @return Page<OrganisationDocumentDTO>
     * @throws Exception
     */
    protected abstract Page<OrganisationDocumentDTO> handleSearch(SearchObject<OrganisationDocumentSearchCriteria> criteria)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<OrganisationDocumentDTO> findByOrganisation(final String organisationId)
    {
        if (organisationId == null || organisationId.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.organisation.document.OrganisationDocumentService.findByOrganisation(String organisationId) - 'organisationId' can not be null or empty");
        }
        try
        {
            return this.handleFindByOrganisation(organisationId);
        }
        catch (Throwable th)
        {
            throw new OrganisationDocumentServiceException(
                "Error performing 'OrganisationDocumentService.findByOrganisation(String organisationId)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #findByOrganisation(String)}
     * @param organisationId java.lang.String TODO: Model Documentation for
OrganisationDocumentService.findByOrganisation(organisationId)
     * @return List<OrganisationDocumentDTO>
     * @throws Exception
     */
    protected abstract List<OrganisationDocumentDTO> handleFindByOrganisation(String organisationId)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<OrganisationDocumentDTO> findByOrganisation(final String organisationId, final Integer pageNumber, final Integer pageSize)
    {
        if (organisationId == null || organisationId.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.organisation.document.OrganisationDocumentService.findByOrganisation(String organisationId, Integer pageNumber, Integer pageSize) - 'organisationId' can not be null or empty");
        }
        try
        {
            return this.handleFindByOrganisation(organisationId, pageNumber, pageSize);
        }
        catch (Throwable th)
        {
            throw new OrganisationDocumentServiceException(
                "Error performing 'OrganisationDocumentService.findByOrganisation(String organisationId, Integer pageNumber, Integer pageSize)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #findByOrganisation(String, Integer, Integer)}
     * @param organisationId java.lang.String TODO: Model Documentation for
OrganisationDocumentService.findByOrganisation(organisationId)
     * @param pageNumber java.lang.Integer TODO: Model Documentation for
OrganisationDocumentService.findByOrganisation(pageNumber)
     * @param pageSize java.lang.Integer TODO: Model Documentation for
OrganisationDocumentService.findByOrganisation(pageSize)
     * @return Page<OrganisationDocumentDTO>
     * @throws Exception
     */
    protected abstract Page<OrganisationDocumentDTO> handleFindByOrganisation(String organisationId, Integer pageNumber, Integer pageSize)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<OrganisationDocumentDTO> findByStatus(final OrganisationDocumentStatus status)
    {
        if (status == null)
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.organisation.document.OrganisationDocumentService.findByStatus(OrganisationDocumentStatus status) - 'status' can not be null");
        }
        try
        {
            return this.handleFindByStatus(status);
        }
        catch (Throwable th)
        {
            throw new OrganisationDocumentServiceException(
                "Error performing 'OrganisationDocumentService.findByStatus(OrganisationDocumentStatus status)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #findByStatus(OrganisationDocumentStatus)}
     * @param status OrganisationDocumentStatus TODO: Model Documentation for
OrganisationDocumentService.findByStatus(status)
     * @return List<OrganisationDocumentDTO>
     * @throws Exception
     */
    protected abstract List<OrganisationDocumentDTO> handleFindByStatus(OrganisationDocumentStatus status)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<OrganisationDocumentDTO> findByStatus(final OrganisationDocumentStatus status, final Integer pageNumber, final Integer pageSize)
    {
        if (status == null)
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.organisation.document.OrganisationDocumentService.findByStatus(OrganisationDocumentStatus status, Integer pageNumber, Integer pageSize) - 'status' can not be null");
        }
        try
        {
            return this.handleFindByStatus(status, pageNumber, pageSize);
        }
        catch (Throwable th)
        {
            throw new OrganisationDocumentServiceException(
                "Error performing 'OrganisationDocumentService.findByStatus(OrganisationDocumentStatus status, Integer pageNumber, Integer pageSize)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #findByStatus(OrganisationDocumentStatus, Integer, Integer)}
     * @param status OrganisationDocumentStatus TODO: Model Documentation for
OrganisationDocumentService.findByStatus(status)
     * @param pageNumber java.lang.Integer TODO: Model Documentation for
OrganisationDocumentService.findByStatus(pageNumber)
     * @param pageSize java.lang.Integer TODO: Model Documentation for
OrganisationDocumentService.findByStatus(pageSize)
     * @return Page<OrganisationDocumentDTO>
     * @throws Exception
     */
    protected abstract Page<OrganisationDocumentDTO> handleFindByStatus(OrganisationDocumentStatus status, Integer pageNumber, Integer pageSize)
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