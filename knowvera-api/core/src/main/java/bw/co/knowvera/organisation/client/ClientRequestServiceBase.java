package bw.co.knowvera.organisation.client;

import bw.co.knowvera.PropertySearchOrder;
import bw.co.knowvera.SearchObject;
import bw.co.knowvera.TargetEntity;
import bw.co.knowvera.document.DocumentDTO;
import bw.co.knowvera.document.DocumentMapper;
import bw.co.knowvera.document.DocumentRepository;
import bw.co.knowvera.individual.IndividualMapper;
import bw.co.knowvera.individual.IndividualRepository;
import bw.co.knowvera.settings.SettingsMapper;
import bw.co.knowvera.settings.SettingsRepository;
import jakarta.validation.Valid;
import java.io.InputStream;
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
 * Spring Service base class for <code>ClientRequestService</code>,
 * provides access to all services and entities referenced by this service.
 * </p>
 *
 * @see ClientRequestService
 */
@CacheConfig(cacheNames = "clientRequests")
public abstract class ClientRequestServiceBase
    implements ClientRequestService
{
    protected Logger logger = LoggerFactory.getLogger(ClientRequestService.class);
    protected final ClientRequestRepository clientRequestRepository;
    protected final ClientRequestMapper clientRequestMapper;
    protected final IndividualRepository individualRepository;
    protected final IndividualMapper individualMapper;
    protected final DocumentRepository documentRepository;
    protected final DocumentMapper documentMapper;
    protected final SettingsRepository settingsRepository;
    protected final SettingsMapper settingsMapper;

    public ClientRequestServiceBase(
        ClientRequestRepository clientRequestRepository,
        ClientRequestMapper clientRequestMapper,         IndividualRepository individualRepository,
        IndividualMapper individualMapper,         DocumentRepository documentRepository,
        DocumentMapper documentMapper,         SettingsRepository settingsRepository,
        SettingsMapper settingsMapper    ) {
        this.clientRequestRepository = clientRequestRepository;
        this.clientRequestMapper = clientRequestMapper;
        this.individualRepository = individualRepository;
        this.individualMapper = individualMapper;
        this.documentRepository = documentRepository;
        this.documentMapper = documentMapper;
        this.settingsRepository = settingsRepository;
        this.settingsMapper = settingsMapper;
    }

    /**
     * Gets the reference to <code>clientRequest</code>'s repository.
     * @return clientRequestRepository
     */
    public ClientRequestRepository getClientRequestRepository() {
        return clientRequestRepository;
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
     * Gets the reference to <code>settings</code>'s repository.
     * @return settingsRepository
     */
    public SettingsRepository getSettingsRepository() {
        return settingsRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Cacheable(key = "#id")
    public ClientRequestDTO findById(final String id)
    {
        if (id == null || id.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.organisation.client.ClientRequestService.findById(String id) - 'id' can not be null or empty");
        }
        try
        {
            return this.handleFindById(id);
        }
        catch (Throwable th)
        {
            throw new ClientRequestServiceException(
                "Error performing 'ClientRequestService.findById(String id)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #findById(String)}
     * @param id String TODO: Model Documentation for ClientRequestService.findById(id)
     * @return ClientRequestDTO
     * @throws Exception
     */
    protected abstract ClientRequestDTO handleFindById(String id)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    @CacheEvict(cacheNames = {"branches", "clientRequests", "documents", "employmentRecords", "individuals", "invoices", "kycRecords", "organisations", "settings", "subscriptions"}, allEntries = true)
    public ClientRequestDTO save(final @Valid ClientRequestDTO clientRequest)
    {
        if (clientRequest == null)
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.organisation.client.ClientRequestService.save(@Valid ClientRequestDTO clientRequest) - 'clientRequest' can not be null");
        }
        if (clientRequest.getName() == null || clientRequest.getName().trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.organisation.client.ClientRequestService.save(@Valid ClientRequestDTO clientRequest) - 'clientRequest.name' can not be null or empty");
        }
        if (clientRequest.getRegistration() == null || clientRequest.getRegistration().trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.organisation.client.ClientRequestService.save(@Valid ClientRequestDTO clientRequest) - 'clientRequest.registration' can not be null or empty");
        }
        if (clientRequest.getStatus() == null)
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.organisation.client.ClientRequestService.save(@Valid ClientRequestDTO clientRequest) - 'clientRequest.status' can not be null");
        }
        if (clientRequest.getOrganisationId() == null || clientRequest.getOrganisationId().trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.organisation.client.ClientRequestService.save(@Valid ClientRequestDTO clientRequest) - 'clientRequest.organisationId' can not be null or empty");
        }
        if (clientRequest.getOrganisation() == null || clientRequest.getOrganisation().trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.organisation.client.ClientRequestService.save(@Valid ClientRequestDTO clientRequest) - 'clientRequest.organisation' can not be null or empty");
        }
        if (clientRequest.getOrganisationRegistrationNo() == null || clientRequest.getOrganisationRegistrationNo().trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.organisation.client.ClientRequestService.save(@Valid ClientRequestDTO clientRequest) - 'clientRequest.organisationRegistrationNo' can not be null or empty");
        }
        if (clientRequest.getTarget() == null)
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.organisation.client.ClientRequestService.save(@Valid ClientRequestDTO clientRequest) - 'clientRequest.target' can not be null");
        }
        if (clientRequest.getTargetId() == null || clientRequest.getTargetId().trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.organisation.client.ClientRequestService.save(@Valid ClientRequestDTO clientRequest) - 'clientRequest.targetId' can not be null or empty");
        }
        if (clientRequest.getOrganisationCode() == null || clientRequest.getOrganisationCode().trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.organisation.client.ClientRequestService.save(@Valid ClientRequestDTO clientRequest) - 'clientRequest.organisationCode' can not be null or empty");
        }
        try
        {
            return this.handleSave(clientRequest);
        }
        catch (Throwable th)
        {
            throw new ClientRequestServiceException(
                "Error performing 'ClientRequestService.save(@Valid ClientRequestDTO clientRequest)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #save(@Valid ClientRequestDTO)}
     * @param clientRequest ClientRequestDTO TODO: Model Documentation for
ClientRequestService.save(clientRequest)
     * @return ClientRequestDTO
     * @throws Exception
     */
    protected abstract ClientRequestDTO handleSave(@Valid ClientRequestDTO clientRequest)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    @CacheEvict(cacheNames = {"branches", "clientRequests", "documents", "employmentRecords", "individuals", "invoices", "kycRecords", "organisations", "settings", "subscriptions"}, allEntries = true)
    public boolean remove(final String id)
    {
        if (id == null || id.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.organisation.client.ClientRequestService.remove(String id) - 'id' can not be null or empty");
        }
        try
        {
            return this.handleRemove(id);
        }
        catch (Throwable th)
        {
            throw new ClientRequestServiceException(
                "Error performing 'ClientRequestService.remove(String id)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #remove(String)}
     * @param id String TODO: Model Documentation for ClientRequestService.remove(id)
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
    public List<ClientRequestDTO> getAll()
    {
        try
        {
            return this.handleGetAll();
        }
        catch (Throwable th)
        {
            throw new ClientRequestServiceException(
                "Error performing 'ClientRequestService.getAll()' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #getAll()}
     * @return List<ClientRequestDTO>
     * @throws Exception
     */
    protected abstract List<ClientRequestDTO> handleGetAll()
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ClientRequestDTO> search(final @Valid ClientRequestSearchCriteria criteria, final @Valid Set<PropertySearchOrder> sortProperties)
    {
        if (criteria == null)
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.organisation.client.ClientRequestService.search(@Valid ClientRequestSearchCriteria criteria, @Valid Set<PropertySearchOrder> sortProperties) - 'criteria' can not be null");
        }
        try
        {
            return this.handleSearch(criteria, sortProperties);
        }
        catch (Throwable th)
        {
            throw new ClientRequestServiceException(
                "Error performing 'ClientRequestService.search(@Valid ClientRequestSearchCriteria criteria, @Valid Set<PropertySearchOrder> sortProperties)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #search(@Valid ClientRequestSearchCriteria, @Valid Set<PropertySearchOrder>)}
     * @param criteria ClientRequestSearchCriteria TODO: Model Documentation for
ClientRequestService.search(criteria)
     * @param sortProperties PropertySearchOrder TODO: Model Documentation for
ClientRequestService.search(sortProperties)
     * @return List<ClientRequestDTO>
     * @throws Exception
     */
    protected abstract List<ClientRequestDTO> handleSearch(@Valid ClientRequestSearchCriteria criteria, @Valid Set<PropertySearchOrder> sortProperties)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<ClientRequestDTO> getAll(final Integer pageNumber, final Integer pageSize)
    {
        try
        {
            return this.handleGetAll(pageNumber, pageSize);
        }
        catch (Throwable th)
        {
            throw new ClientRequestServiceException(
                "Error performing 'ClientRequestService.getAll(Integer pageNumber, Integer pageSize)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #getAll(Integer, Integer)}
     * @param pageNumber java.lang.Integer TODO: Model Documentation for
ClientRequestService.getAll(pageNumber)
     * @param pageSize java.lang.Integer TODO: Model Documentation for
ClientRequestService.getAll(pageSize)
     * @return Page<ClientRequestDTO>
     * @throws Exception
     */
    protected abstract Page<ClientRequestDTO> handleGetAll(Integer pageNumber, Integer pageSize)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<ClientRequestDTO> search(final SearchObject<ClientRequestSearchCriteria> criteria)
    {
        if (criteria == null)
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.organisation.client.ClientRequestService.search(SearchObject<ClientRequestSearchCriteria> criteria) - 'criteria' can not be null");
        }
        try
        {
            return this.handleSearch(criteria);
        }
        catch (Throwable th)
        {
            throw new ClientRequestServiceException(
                "Error performing 'ClientRequestService.search(SearchObject<ClientRequestSearchCriteria> criteria)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #search(SearchObject<ClientRequestSearchCriteria>)}
     * @param criteria SearchObject<ClientRequestSearchCriteria> TODO: Model Documentation for
ClientRequestService.search(criteria)
     * @return Page<ClientRequestDTO>
     * @throws Exception
     */
    protected abstract Page<ClientRequestDTO> handleSearch(SearchObject<ClientRequestSearchCriteria> criteria)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ClientRequestDTO> findByOrganisation(final String organisationId)
    {
        if (organisationId == null || organisationId.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.organisation.client.ClientRequestService.findByOrganisation(String organisationId) - 'organisationId' can not be null or empty");
        }
        try
        {
            return this.handleFindByOrganisation(organisationId);
        }
        catch (Throwable th)
        {
            throw new ClientRequestServiceException(
                "Error performing 'ClientRequestService.findByOrganisation(String organisationId)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #findByOrganisation(String)}
     * @param organisationId String TODO: Model Documentation for
ClientRequestService.findByOrganisation(organisationId)
     * @return List<ClientRequestDTO>
     * @throws Exception
     */
    protected abstract List<ClientRequestDTO> handleFindByOrganisation(String organisationId)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<ClientRequestDTO> findByOrganisation(final String organisationId, final Integer pageNumber, final Integer pageSize)
    {
        if (organisationId == null || organisationId.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.organisation.client.ClientRequestService.findByOrganisation(String organisationId, Integer pageNumber, Integer pageSize) - 'organisationId' can not be null or empty");
        }
        try
        {
            return this.handleFindByOrganisation(organisationId, pageNumber, pageSize);
        }
        catch (Throwable th)
        {
            throw new ClientRequestServiceException(
                "Error performing 'ClientRequestService.findByOrganisation(String organisationId, Integer pageNumber, Integer pageSize)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #findByOrganisation(String, Integer, Integer)}
     * @param organisationId String TODO: Model Documentation for
ClientRequestService.findByOrganisation(organisationId)
     * @param pageNumber java.lang.Integer TODO: Model Documentation for
ClientRequestService.findByOrganisation(pageNumber)
     * @param pageSize java.lang.Integer TODO: Model Documentation for
ClientRequestService.findByOrganisation(pageSize)
     * @return Page<ClientRequestDTO>
     * @throws Exception
     */
    protected abstract Page<ClientRequestDTO> handleFindByOrganisation(String organisationId, Integer pageNumber, Integer pageSize)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ClientRequestDTO> findByStatus(final ClientRequestStatus status)
    {
        if (status == null)
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.organisation.client.ClientRequestService.findByStatus(ClientRequestStatus status) - 'status' can not be null");
        }
        try
        {
            return this.handleFindByStatus(status);
        }
        catch (Throwable th)
        {
            throw new ClientRequestServiceException(
                "Error performing 'ClientRequestService.findByStatus(ClientRequestStatus status)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #findByStatus(ClientRequestStatus)}
     * @param status ClientRequestStatus TODO: Model Documentation for
ClientRequestService.findByStatus(status)
     * @return List<ClientRequestDTO>
     * @throws Exception
     */
    protected abstract List<ClientRequestDTO> handleFindByStatus(ClientRequestStatus status)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    @CacheEvict(cacheNames = {"branches", "clientRequests", "documents", "employmentRecords", "individuals", "invoices", "kycRecords", "organisations", "settings", "subscriptions"}, allEntries = true)
    public Page<ClientRequestDTO> uploadRequests(final InputStream inputStream, final String user, final String organisationId, final @Valid DocumentDTO document, final TargetEntity target, final String organisation)
    {
        if (inputStream == null)
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.organisation.client.ClientRequestService.uploadRequests(InputStream inputStream, String user, String organisationId, @Valid DocumentDTO document, TargetEntity target, String organisation) - 'inputStream' can not be null");
        }
        if (user == null || user.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.organisation.client.ClientRequestService.uploadRequests(InputStream inputStream, String user, String organisationId, @Valid DocumentDTO document, TargetEntity target, String organisation) - 'user' can not be null or empty");
        }
        if (organisationId == null || organisationId.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.organisation.client.ClientRequestService.uploadRequests(InputStream inputStream, String user, String organisationId, @Valid DocumentDTO document, TargetEntity target, String organisation) - 'organisationId' can not be null or empty");
        }
        if (document == null)
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.organisation.client.ClientRequestService.uploadRequests(InputStream inputStream, String user, String organisationId, @Valid DocumentDTO document, TargetEntity target, String organisation) - 'document' can not be null");
        }
        if (document.getTargetId() == null || document.getTargetId().trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.organisation.client.ClientRequestService.uploadRequests(InputStream inputStream, String user, String organisationId, @Valid DocumentDTO document, TargetEntity target, String organisation) - 'document.targetId' can not be null or empty");
        }
        if (document.getFileName() == null || document.getFileName().trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.organisation.client.ClientRequestService.uploadRequests(InputStream inputStream, String user, String organisationId, @Valid DocumentDTO document, TargetEntity target, String organisation) - 'document.fileName' can not be null or empty");
        }
        if (target == null)
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.organisation.client.ClientRequestService.uploadRequests(InputStream inputStream, String user, String organisationId, @Valid DocumentDTO document, TargetEntity target, String organisation) - 'target' can not be null");
        }
        if (organisation == null || organisation.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.organisation.client.ClientRequestService.uploadRequests(InputStream inputStream, String user, String organisationId, @Valid DocumentDTO document, TargetEntity target, String organisation) - 'organisation' can not be null or empty");
        }
        try
        {
            return this.handleUploadRequests(inputStream, user, organisationId, document, target, organisation);
        }
        catch (Throwable th)
        {
            throw new ClientRequestServiceException(
                "Error performing 'ClientRequestService.uploadRequests(InputStream inputStream, String user, String organisationId, @Valid DocumentDTO document, TargetEntity target, String organisation)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #uploadRequests(InputStream, String, String, @Valid DocumentDTO, TargetEntity, String)}
     * @param inputStream InputStream TODO: Model Documentation for
ClientRequestService.uploadRequests(inputStream)
     * @param user String TODO: Model Documentation for
ClientRequestService.uploadRequests(user)
     * @param organisationId String TODO: Model Documentation for
ClientRequestService.uploadRequests(organisationId)
     * @param document DocumentDTO TODO: Model Documentation for
ClientRequestService.uploadRequests(document)
     * @param target TargetEntity TODO: Model Documentation for
ClientRequestService.uploadRequests(target)
     * @param organisation String TODO: Model Documentation for
ClientRequestService.uploadRequests(organisation)
     * @return Page<ClientRequestDTO>
     * @throws Exception
     */
    protected abstract Page<ClientRequestDTO> handleUploadRequests(InputStream inputStream, String user, String organisationId, @Valid DocumentDTO document, TargetEntity target, String organisation)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ClientRequestDTO> findByIndividual(final String individualId)
    {
        if (individualId == null || individualId.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.organisation.client.ClientRequestService.findByIndividual(String individualId) - 'individualId' can not be null or empty");
        }
        try
        {
            return this.handleFindByIndividual(individualId);
        }
        catch (Throwable th)
        {
            throw new ClientRequestServiceException(
                "Error performing 'ClientRequestService.findByIndividual(String individualId)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #findByIndividual(String)}
     * @param individualId String TODO: Model Documentation for
ClientRequestService.findByIndividual(individualId)
     * @return List<ClientRequestDTO>
     * @throws Exception
     */
    protected abstract List<ClientRequestDTO> handleFindByIndividual(String individualId)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<ClientRequestDTO> findByIndividual(final String individualId, final Integer pageNumber, final Integer pageSize)
    {
        if (individualId == null || individualId.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.organisation.client.ClientRequestService.findByIndividual(String individualId, Integer pageNumber, Integer pageSize) - 'individualId' can not be null or empty");
        }
        try
        {
            return this.handleFindByIndividual(individualId, pageNumber, pageSize);
        }
        catch (Throwable th)
        {
            throw new ClientRequestServiceException(
                "Error performing 'ClientRequestService.findByIndividual(String individualId, Integer pageNumber, Integer pageSize)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #findByIndividual(String, Integer, Integer)}
     * @param individualId String TODO: Model Documentation for
ClientRequestService.findByIndividual(individualId)
     * @param pageNumber java.lang.Integer TODO: Model Documentation for
ClientRequestService.findByIndividual(pageNumber)
     * @param pageSize java.lang.Integer TODO: Model Documentation for
ClientRequestService.findByIndividual(pageSize)
     * @return Page<ClientRequestDTO>
     * @throws Exception
     */
    protected abstract Page<ClientRequestDTO> handleFindByIndividual(String individualId, Integer pageNumber, Integer pageSize)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ClientRequestDTO> findByDocument(final String documentId)
    {
        if (documentId == null || documentId.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.organisation.client.ClientRequestService.findByDocument(String documentId) - 'documentId' can not be null or empty");
        }
        try
        {
            return this.handleFindByDocument(documentId);
        }
        catch (Throwable th)
        {
            throw new ClientRequestServiceException(
                "Error performing 'ClientRequestService.findByDocument(String documentId)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #findByDocument(String)}
     * @param documentId String TODO: Model Documentation for
ClientRequestService.findByDocument(documentId)
     * @return List<ClientRequestDTO>
     * @throws Exception
     */
    protected abstract List<ClientRequestDTO> handleFindByDocument(String documentId)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<ClientRequestDTO> findByDocument(final String documentId, final Integer pageNumber, final Integer pageSize)
    {
        if (documentId == null || documentId.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.organisation.client.ClientRequestService.findByDocument(String documentId, Integer pageNumber, Integer pageSize) - 'documentId' can not be null or empty");
        }
        try
        {
            return this.handleFindByDocument(documentId, pageNumber, pageSize);
        }
        catch (Throwable th)
        {
            throw new ClientRequestServiceException(
                "Error performing 'ClientRequestService.findByDocument(String documentId, Integer pageNumber, Integer pageSize)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #findByDocument(String, Integer, Integer)}
     * @param documentId String TODO: Model Documentation for
ClientRequestService.findByDocument(documentId)
     * @param pageNumber java.lang.Integer TODO: Model Documentation for
ClientRequestService.findByDocument(pageNumber)
     * @param pageSize java.lang.Integer TODO: Model Documentation for
ClientRequestService.findByDocument(pageSize)
     * @return Page<ClientRequestDTO>
     * @throws Exception
     */
    protected abstract Page<ClientRequestDTO> handleFindByDocument(String documentId, Integer pageNumber, Integer pageSize)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<ClientRequestDTO> findByStatus(final ClientRequestStatus status, final Integer pageNumber, final Integer pageSize)
    {
        if (status == null)
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.organisation.client.ClientRequestService.findByStatus(ClientRequestStatus status, Integer pageNumber, Integer pageSize) - 'status' can not be null");
        }
        try
        {
            return this.handleFindByStatus(status, pageNumber, pageSize);
        }
        catch (Throwable th)
        {
            throw new ClientRequestServiceException(
                "Error performing 'ClientRequestService.findByStatus(ClientRequestStatus status, Integer pageNumber, Integer pageSize)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #findByStatus(ClientRequestStatus, Integer, Integer)}
     * @param status ClientRequestStatus TODO: Model Documentation for
ClientRequestService.findByStatus(status)
     * @param pageNumber java.lang.Integer TODO: Model Documentation for
ClientRequestService.findByStatus(pageNumber)
     * @param pageSize java.lang.Integer TODO: Model Documentation for
ClientRequestService.findByStatus(pageSize)
     * @return Page<ClientRequestDTO>
     * @throws Exception
     */
    protected abstract Page<ClientRequestDTO> handleFindByStatus(ClientRequestStatus status, Integer pageNumber, Integer pageSize)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ClientRequestDTO> findByTarget(final TargetEntity target, final String targetId)
    {
        if (target == null)
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.organisation.client.ClientRequestService.findByTarget(TargetEntity target, String targetId) - 'target' can not be null");
        }
        if (targetId == null || targetId.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.organisation.client.ClientRequestService.findByTarget(TargetEntity target, String targetId) - 'targetId' can not be null or empty");
        }
        try
        {
            return this.handleFindByTarget(target, targetId);
        }
        catch (Throwable th)
        {
            throw new ClientRequestServiceException(
                "Error performing 'ClientRequestService.findByTarget(TargetEntity target, String targetId)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #findByTarget(TargetEntity, String)}
     * @param target TargetEntity TODO: Model Documentation for
ClientRequestService.findByTarget(target)
     * @param targetId String TODO: Model Documentation for
ClientRequestService.findByTarget(targetId)
     * @return List<ClientRequestDTO>
     * @throws Exception
     */
    protected abstract List<ClientRequestDTO> handleFindByTarget(TargetEntity target, String targetId)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<ClientRequestDTO> findByTarget(final TargetEntity target, final String targetId, final Integer pageNumber, final Integer pageSize)
    {
        if (target == null)
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.organisation.client.ClientRequestService.findByTarget(TargetEntity target, String targetId, Integer pageNumber, Integer pageSize) - 'target' can not be null");
        }
        if (targetId == null || targetId.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.organisation.client.ClientRequestService.findByTarget(TargetEntity target, String targetId, Integer pageNumber, Integer pageSize) - 'targetId' can not be null or empty");
        }
        try
        {
            return this.handleFindByTarget(target, targetId, pageNumber, pageSize);
        }
        catch (Throwable th)
        {
            throw new ClientRequestServiceException(
                "Error performing 'ClientRequestService.findByTarget(TargetEntity target, String targetId, Integer pageNumber, Integer pageSize)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #findByTarget(TargetEntity, String, Integer, Integer)}
     * @param target TargetEntity TODO: Model Documentation for
ClientRequestService.findByTarget(target)
     * @param targetId String TODO: Model Documentation for
ClientRequestService.findByTarget(targetId)
     * @param pageNumber java.lang.Integer TODO: Model Documentation for
ClientRequestService.findByTarget(pageNumber)
     * @param pageSize java.lang.Integer TODO: Model Documentation for
ClientRequestService.findByTarget(pageSize)
     * @return Page<ClientRequestDTO>
     * @throws Exception
     */
    protected abstract Page<ClientRequestDTO> handleFindByTarget(TargetEntity target, String targetId, Integer pageNumber, Integer pageSize)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ClientRequestDTO> findByTargetAndOrganisation(final TargetEntity target, final String targetId, final String organisationId)
    {
        if (target == null)
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.organisation.client.ClientRequestService.findByTargetAndOrganisation(TargetEntity target, String targetId, String organisationId) - 'target' can not be null");
        }
        try
        {
            return this.handleFindByTargetAndOrganisation(target, targetId, organisationId);
        }
        catch (Throwable th)
        {
            throw new ClientRequestServiceException(
                "Error performing 'ClientRequestService.findByTargetAndOrganisation(TargetEntity target, String targetId, String organisationId)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #findByTargetAndOrganisation(TargetEntity, String, String)}
     * @param target TargetEntity TODO: Model Documentation for
ClientRequestService.findByTargetAndOrganisation(target)
     * @param targetId String TODO: Model Documentation for
ClientRequestService.findByTargetAndOrganisation(targetId)
     * @param organisationId String TODO: Model Documentation for
ClientRequestService.findByTargetAndOrganisation(organisationId)
     * @return List<ClientRequestDTO>
     * @throws Exception
     */
    protected abstract List<ClientRequestDTO> handleFindByTargetAndOrganisation(TargetEntity target, String targetId, String organisationId)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<ClientRequestDTO> findByTargetAndOrganisation(final TargetEntity target, final String targetId, final String organisationId, final Integer pageNumber, final Integer pageSize)
    {
        if (target == null)
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.organisation.client.ClientRequestService.findByTargetAndOrganisation(TargetEntity target, String targetId, String organisationId, Integer pageNumber, Integer pageSize) - 'target' can not be null");
        }
        try
        {
            return this.handleFindByTargetAndOrganisation(target, targetId, organisationId, pageNumber, pageSize);
        }
        catch (Throwable th)
        {
            throw new ClientRequestServiceException(
                "Error performing 'ClientRequestService.findByTargetAndOrganisation(TargetEntity target, String targetId, String organisationId, Integer pageNumber, Integer pageSize)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #findByTargetAndOrganisation(TargetEntity, String, String, Integer, Integer)}
     * @param target TargetEntity TODO: Model Documentation for
ClientRequestService.findByTargetAndOrganisation(target)
     * @param targetId String TODO: Model Documentation for
ClientRequestService.findByTargetAndOrganisation(targetId)
     * @param organisationId String TODO: Model Documentation for
ClientRequestService.findByTargetAndOrganisation(organisationId)
     * @param pageNumber java.lang.Integer TODO: Model Documentation for
ClientRequestService.findByTargetAndOrganisation(pageNumber)
     * @param pageSize java.lang.Integer TODO: Model Documentation for
ClientRequestService.findByTargetAndOrganisation(pageSize)
     * @return Page<ClientRequestDTO>
     * @throws Exception
     */
    protected abstract Page<ClientRequestDTO> handleFindByTargetAndOrganisation(TargetEntity target, String targetId, String organisationId, Integer pageNumber, Integer pageSize)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    @CacheEvict(cacheNames = {"branches", "clientRequests", "documents", "employmentRecords", "individuals", "invoices", "kycRecords", "organisations", "settings", "subscriptions"}, allEntries = true)
    public ClientRequestDTO updateStatus(final String id, final ClientRequestStatus status)
    {
        if (id == null || id.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.organisation.client.ClientRequestService.updateStatus(String id, ClientRequestStatus status) - 'id' can not be null or empty");
        }
        if (status == null)
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.organisation.client.ClientRequestService.updateStatus(String id, ClientRequestStatus status) - 'status' can not be null");
        }
        try
        {
            return this.handleUpdateStatus(id, status);
        }
        catch (Throwable th)
        {
            throw new ClientRequestServiceException(
                "Error performing 'ClientRequestService.updateStatus(String id, ClientRequestStatus status)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #updateStatus(String, ClientRequestStatus)}
     * @param id String TODO: Model Documentation for
ClientRequestService.updateStatus(id)
     * @param status ClientRequestStatus TODO: Model Documentation for
ClientRequestService.updateStatus(status)
     * @return ClientRequestDTO
     * @throws Exception
     */
    protected abstract ClientRequestDTO handleUpdateStatus(String id, ClientRequestStatus status)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    @CacheEvict(cacheNames = {"branches", "clientRequests", "documents", "employmentRecords", "individuals", "invoices", "kycRecords", "organisations", "settings", "subscriptions"}, allEntries = true)
    public String confirmToken(final String requestId, final String token)
    {
        if (requestId == null || requestId.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.organisation.client.ClientRequestService.confirmToken(String requestId, String token) - 'requestId' can not be null or empty");
        }
        if (token == null || token.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.organisation.client.ClientRequestService.confirmToken(String requestId, String token) - 'token' can not be null or empty");
        }
        try
        {
            return this.handleConfirmToken(requestId, token);
        }
        catch (Throwable th)
        {
            throw new ClientRequestServiceException(
                "Error performing 'ClientRequestService.confirmToken(String requestId, String token)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #confirmToken(String, String)}
     * @param requestId String TODO: Model Documentation for
ClientRequestService.confirmToken(requestId)
     * @param token String TODO: Model Documentation for
ClientRequestService.confirmToken(token)
     * @return String
     * @throws Exception
     */
    protected abstract String handleConfirmToken(String requestId, String token)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    public Long countByStatus(final ClientRequestStatus status)
    {
        if (status == null)
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.organisation.client.ClientRequestService.countByStatus(ClientRequestStatus status) - 'status' can not be null");
        }
        try
        {
            return this.handleCountByStatus(status);
        }
        catch (Throwable th)
        {
            throw new ClientRequestServiceException(
                "Error performing 'ClientRequestService.countByStatus(ClientRequestStatus status)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #countByStatus(ClientRequestStatus)}
     * @param status ClientRequestStatus TODO: Model Documentation for
ClientRequestService.countByStatus(status)
     * @return Long
     * @throws Exception
     */
    protected abstract Long handleCountByStatus(ClientRequestStatus status)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    public Long countByStatusAndOrganisationId(final ClientRequestStatus status, final String organisationId)
    {
        if (status == null)
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.organisation.client.ClientRequestService.countByStatusAndOrganisationId(ClientRequestStatus status, String organisationId) - 'status' can not be null");
        }
        if (organisationId == null || organisationId.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.organisation.client.ClientRequestService.countByStatusAndOrganisationId(ClientRequestStatus status, String organisationId) - 'organisationId' can not be null or empty");
        }
        try
        {
            return this.handleCountByStatusAndOrganisationId(status, organisationId);
        }
        catch (Throwable th)
        {
            throw new ClientRequestServiceException(
                "Error performing 'ClientRequestService.countByStatusAndOrganisationId(ClientRequestStatus status, String organisationId)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #countByStatusAndOrganisationId(ClientRequestStatus, String)}
     * @param status ClientRequestStatus TODO: Model Documentation for
ClientRequestService.countByStatusAndOrganisationId(status)
     * @param organisationId String TODO: Model Documentation for
ClientRequestService.countByStatusAndOrganisationId(organisationId)
     * @return Long
     * @throws Exception
     */
    protected abstract Long handleCountByStatusAndOrganisationId(ClientRequestStatus status, String organisationId)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    public Long count()
    {
        try
        {
            return this.handleCount();
        }
        catch (Throwable th)
        {
            throw new ClientRequestServiceException(
                "Error performing 'ClientRequestService.count()' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #count()}
     * @return Long
     * @throws Exception
     */
    protected abstract Long handleCount()
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    @CacheEvict(cacheNames = {"branches", "clientRequests", "documents", "employmentRecords", "individuals", "invoices", "kycRecords", "organisations", "settings", "subscriptions"}, allEntries = true)
    public boolean confirmRegistration(final String id, final Boolean confirm, final String registrationToken)
    {
        if (id == null || id.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.organisation.client.ClientRequestService.confirmRegistration(String id, Boolean confirm, String registrationToken) - 'id' can not be null or empty");
        }
        if (registrationToken == null || registrationToken.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.organisation.client.ClientRequestService.confirmRegistration(String id, Boolean confirm, String registrationToken) - 'registrationToken' can not be null or empty");
        }
        try
        {
            return this.handleConfirmRegistration(id, confirm, registrationToken);
        }
        catch (Throwable th)
        {
            throw new ClientRequestServiceException(
                "Error performing 'ClientRequestService.confirmRegistration(String id, Boolean confirm, String registrationToken)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #confirmRegistration(String, Boolean, String)}
     * @param id String TODO: Model Documentation for
ClientRequestService.confirmRegistration(id)
     * @param confirm java.lang.Boolean TODO: Model Documentation for
ClientRequestService.confirmRegistration(confirm)
     * @param registrationToken String TODO: Model Documentation for
ClientRequestService.confirmRegistration(registrationToken)
     * @return boolean
     * @throws Exception
     */
    protected abstract boolean handleConfirmRegistration(String id, Boolean confirm, String registrationToken)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    public ClientRequestDTO findUserReadyRequests()
    {
        try
        {
            return this.handleFindUserReadyRequests();
        }
        catch (Throwable th)
        {
            throw new ClientRequestServiceException(
                "Error performing 'ClientRequestService.findUserReadyRequests()' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #findUserReadyRequests()}
     * @return ClientRequestDTO
     * @throws Exception
     */
    protected abstract ClientRequestDTO handleFindUserReadyRequests()
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    public ClientRequestDTO findUserReadyRequests(final Integer pageNumber, final Integer pageSize)
    {
        try
        {
            return this.handleFindUserReadyRequests(pageNumber, pageSize);
        }
        catch (Throwable th)
        {
            throw new ClientRequestServiceException(
                "Error performing 'ClientRequestService.findUserReadyRequests(Integer pageNumber, Integer pageSize)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #findUserReadyRequests(Integer, Integer)}
     * @param pageNumber java.lang.Integer TODO: Model Documentation for
ClientRequestService.findUserReadyRequests(pageNumber)
     * @param pageSize java.lang.Integer TODO: Model Documentation for
ClientRequestService.findUserReadyRequests(pageSize)
     * @return ClientRequestDTO
     * @throws Exception
     */
    protected abstract ClientRequestDTO handleFindUserReadyRequests(Integer pageNumber, Integer pageSize)
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