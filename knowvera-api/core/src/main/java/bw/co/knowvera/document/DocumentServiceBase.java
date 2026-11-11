package bw.co.knowvera.document;

import bw.co.knowvera.PropertySearchOrder;
import bw.co.knowvera.SearchObject;
import bw.co.knowvera.TargetEntity;
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
 * Spring Service base class for <code>DocumentService</code>,
 * provides access to all services and entities referenced by this service.
 * </p>
 *
 * @see DocumentService
 */
@CacheConfig(cacheNames = "documents")
public abstract class DocumentServiceBase
    implements DocumentService
{
    protected Logger logger = LoggerFactory.getLogger(DocumentService.class);
    protected final DocumentRepository documentRepository;
    protected final DocumentMapper documentMapper;

    public DocumentServiceBase(
        DocumentRepository documentRepository,
        DocumentMapper documentMapper    ) {
        this.documentRepository = documentRepository;
        this.documentMapper = documentMapper;
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
    public DocumentDTO findById(final String id)
    {
        if (id == null || id.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.document.DocumentService.findById(String id) - 'id' can not be null or empty");
        }
        try
        {
            return this.handleFindById(id);
        }
        catch (Throwable th)
        {
            throw new DocumentServiceException(
                "Error performing 'DocumentService.findById(String id)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #findById(String)}
     * @param id java.lang.String TODO: Model Documentation for DocumentService.findById(id)
     * @return DocumentDTO
     * @throws Exception
     */
    protected abstract DocumentDTO handleFindById(String id)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    @CacheEvict(cacheNames = {"branches", "clientRequests", "documents", "employmentRecords", "individuals", "invoices", "kycRecords", "organisations", "settings", "subscriptions"}, allEntries = true)
    public DocumentDTO save(final @Valid DocumentDTO document)
    {
        if (document == null)
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.document.DocumentService.save(@Valid DocumentDTO document) - 'document' can not be null");
        }
        if (document.getTargetId() == null || document.getTargetId().trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.document.DocumentService.save(@Valid DocumentDTO document) - 'document.targetId' can not be null or empty");
        }
        if (document.getFileName() == null || document.getFileName().trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.document.DocumentService.save(@Valid DocumentDTO document) - 'document.fileName' can not be null or empty");
        }
        try
        {
            return this.handleSave(document);
        }
        catch (Throwable th)
        {
            throw new DocumentServiceException(
                "Error performing 'DocumentService.save(@Valid DocumentDTO document)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #save(@Valid DocumentDTO)}
     * @param document DocumentDTO TODO: Model Documentation for DocumentService.save(document)
     * @return DocumentDTO
     * @throws Exception
     */
    protected abstract DocumentDTO handleSave(@Valid DocumentDTO document)
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
                "bw.co.knowvera.document.DocumentService.remove(String id) - 'id' can not be null or empty");
        }
        try
        {
            return this.handleRemove(id);
        }
        catch (Throwable th)
        {
            throw new DocumentServiceException(
                "Error performing 'DocumentService.remove(String id)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #remove(String)}
     * @param id java.lang.String TODO: Model Documentation for DocumentService.remove(id)
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
    public List<DocumentListDTO> getAll()
    {
        try
        {
            return this.handleGetAll();
        }
        catch (Throwable th)
        {
            throw new DocumentServiceException(
                "Error performing 'DocumentService.getAll()' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #getAll()}
     * @return List<DocumentListDTO>
     * @throws Exception
     */
    protected abstract List<DocumentListDTO> handleGetAll()
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<DocumentListDTO> search(final @Valid DocumentSearchCriteria criteria, final @Valid Set<PropertySearchOrder> orderings)
    {
        if (orderings == null)
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.document.DocumentService.search(@Valid DocumentSearchCriteria criteria, @Valid Set<PropertySearchOrder> orderings) - 'orderings' can not be null");
        }
        try
        {
            return this.handleSearch(criteria, orderings);
        }
        catch (Throwable th)
        {
            throw new DocumentServiceException(
                "Error performing 'DocumentService.search(@Valid DocumentSearchCriteria criteria, @Valid Set<PropertySearchOrder> orderings)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #search(@Valid DocumentSearchCriteria, @Valid Set<PropertySearchOrder>)}
     * @param criteria DocumentSearchCriteria TODO: Model Documentation for DocumentService.search(criteria)
     * @param orderings PropertySearchOrder TODO: Model Documentation for DocumentService.search(orderings)
     * @return List<DocumentListDTO>
     * @throws Exception
     */
    protected abstract List<DocumentListDTO> handleSearch(@Valid DocumentSearchCriteria criteria, @Valid Set<PropertySearchOrder> orderings)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<DocumentListDTO> getAll(final Integer pageNumber, final Integer pageSize)
    {
        try
        {
            return this.handleGetAll(pageNumber, pageSize);
        }
        catch (Throwable th)
        {
            throw new DocumentServiceException(
                "Error performing 'DocumentService.getAll(Integer pageNumber, Integer pageSize)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #getAll(Integer, Integer)}
     * @param pageNumber java.lang.Integer TODO: Model Documentation for DocumentService.getAll(pageNumber)
     * @param pageSize java.lang.Integer TODO: Model Documentation for DocumentService.getAll(pageSize)
     * @return Page<DocumentListDTO>
     * @throws Exception
     */
    protected abstract Page<DocumentListDTO> handleGetAll(Integer pageNumber, Integer pageSize)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<DocumentListDTO> findByDocumentType(final String documentTypeId)
    {
        if (documentTypeId == null || documentTypeId.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.document.DocumentService.findByDocumentType(String documentTypeId) - 'documentTypeId' can not be null or empty");
        }
        try
        {
            return this.handleFindByDocumentType(documentTypeId);
        }
        catch (Throwable th)
        {
            throw new DocumentServiceException(
                "Error performing 'DocumentService.findByDocumentType(String documentTypeId)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #findByDocumentType(String)}
     * @param documentTypeId java.lang.String TODO: Model Documentation for
DocumentService.findByDocumentType(documentTypeId)
     * @return List<DocumentListDTO>
     * @throws Exception
     */
    protected abstract List<DocumentListDTO> handleFindByDocumentType(String documentTypeId)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    @CacheEvict(cacheNames = {"branches", "clientRequests", "documents", "employmentRecords", "individuals", "invoices", "kycRecords", "organisations", "settings", "subscriptions"}, allEntries = true)
    public DocumentDTO upload(final TargetEntity target, final String targetId, final String documentTypeId, final String url)
    {
        if (target == null)
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.document.DocumentService.upload(TargetEntity target, String targetId, String documentTypeId, String url) - 'target' can not be null");
        }
        if (targetId == null || targetId.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.document.DocumentService.upload(TargetEntity target, String targetId, String documentTypeId, String url) - 'targetId' can not be null or empty");
        }
        if (documentTypeId == null || documentTypeId.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.document.DocumentService.upload(TargetEntity target, String targetId, String documentTypeId, String url) - 'documentTypeId' can not be null or empty");
        }
        if (url == null || url.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.document.DocumentService.upload(TargetEntity target, String targetId, String documentTypeId, String url) - 'url' can not be null or empty");
        }
        try
        {
            return this.handleUpload(target, targetId, documentTypeId, url);
        }
        catch (Throwable th)
        {
            throw new DocumentServiceException(
                "Error performing 'DocumentService.upload(TargetEntity target, String targetId, String documentTypeId, String url)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #upload(TargetEntity, String, String, String)}
     * @param target TargetEntity TODO: Model Documentation for DocumentService.upload(target)
     * @param targetId java.lang.String TODO: Model Documentation for DocumentService.upload(targetId)
     * @param documentTypeId java.lang.String TODO: Model Documentation for DocumentService.upload(documentTypeId)
     * @param url java.lang.String TODO: Model Documentation for DocumentService.upload(url)
     * @return DocumentDTO
     * @throws Exception
     */
    protected abstract DocumentDTO handleUpload(TargetEntity target, String targetId, String documentTypeId, String url)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<DocumentListDTO> findByTarget(final TargetEntity target, final String targetId)
    {
        if (target == null)
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.document.DocumentService.findByTarget(TargetEntity target, String targetId) - 'target' can not be null");
        }
        if (targetId == null || targetId.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.document.DocumentService.findByTarget(TargetEntity target, String targetId) - 'targetId' can not be null or empty");
        }
        try
        {
            return this.handleFindByTarget(target, targetId);
        }
        catch (Throwable th)
        {
            throw new DocumentServiceException(
                "Error performing 'DocumentService.findByTarget(TargetEntity target, String targetId)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #findByTarget(TargetEntity, String)}
     * @param target TargetEntity TODO: Model Documentation for DocumentService.findByTarget(target)
     * @param targetId java.lang.String TODO: Model Documentation for DocumentService.findByTarget(targetId)
     * @return List<DocumentListDTO>
     * @throws Exception
     */
    protected abstract List<DocumentListDTO> handleFindByTarget(TargetEntity target, String targetId)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<DocumentListDTO> search(final SearchObject<DocumentSearchCriteria> criteria)
    {
        try
        {
            return this.handleSearch(criteria);
        }
        catch (Throwable th)
        {
            throw new DocumentServiceException(
                "Error performing 'DocumentService.search(SearchObject<DocumentSearchCriteria> criteria)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #search(SearchObject<DocumentSearchCriteria>)}
     * @param criteria SearchObject<DocumentSearchCriteria> TODO: Model Documentation for DocumentService.search(criteria)
     * @return Page<DocumentListDTO>
     * @throws Exception
     */
    protected abstract Page<DocumentListDTO> handleSearch(SearchObject<DocumentSearchCriteria> criteria)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    @CacheEvict(cacheNames = {"branches", "clientRequests", "documents", "employmentRecords", "individuals", "invoices", "kycRecords", "organisations", "settings", "subscriptions"}, allEntries = true)
    public DocumentDTO updateFileContent(final String id, final String content)
    {
        if (id == null || id.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.document.DocumentService.updateFileContent(String id, String content) - 'id' can not be null or empty");
        }
        if (content == null || content.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.document.DocumentService.updateFileContent(String id, String content) - 'content' can not be null or empty");
        }
        try
        {
            return this.handleUpdateFileContent(id, content);
        }
        catch (Throwable th)
        {
            throw new DocumentServiceException(
                "Error performing 'DocumentService.updateFileContent(String id, String content)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #updateFileContent(String, String)}
     * @param id java.lang.String TODO: Model Documentation for DocumentService.updateFileContent(id)
     * @param content java.lang.String TODO: Model Documentation for DocumentService.updateFileContent(content)
     * @return DocumentDTO
     * @throws Exception
     */
    protected abstract DocumentDTO handleUpdateFileContent(String id, String content)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    @CacheEvict(cacheNames = {"branches", "clientRequests", "documents", "employmentRecords", "individuals", "invoices", "kycRecords", "organisations", "settings", "subscriptions"}, allEntries = true)
    public DocumentDTO verifyData(final String id, final String user)
    {
        if (id == null || id.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.document.DocumentService.verifyData(String id, String user) - 'id' can not be null or empty");
        }
        if (user == null || user.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.document.DocumentService.verifyData(String id, String user) - 'user' can not be null or empty");
        }
        try
        {
            return this.handleVerifyData(id, user);
        }
        catch (Throwable th)
        {
            throw new DocumentServiceException(
                "Error performing 'DocumentService.verifyData(String id, String user)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #verifyData(String, String)}
     * @param id java.lang.String TODO: Model Documentation for DocumentService.verifyData(id)
     * @param user java.lang.String TODO: Model Documentation for DocumentService.verifyData(user)
     * @return DocumentDTO
     * @throws Exception
     */
    protected abstract DocumentDTO handleVerifyData(String id, String user)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    @CacheEvict(cacheNames = {"branches", "clientRequests", "documents", "employmentRecords", "individuals", "invoices", "kycRecords", "organisations", "settings", "subscriptions"}, allEntries = true)
    public DocumentDTO updateVerificationStatus(final String id, final DocumentVerificationStatus verificationStatus, final String user)
    {
        if (id == null || id.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.document.DocumentService.updateVerificationStatus(String id, DocumentVerificationStatus verificationStatus, String user) - 'id' can not be null or empty");
        }
        if (verificationStatus == null)
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.document.DocumentService.updateVerificationStatus(String id, DocumentVerificationStatus verificationStatus, String user) - 'verificationStatus' can not be null");
        }
        if (user == null || user.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.document.DocumentService.updateVerificationStatus(String id, DocumentVerificationStatus verificationStatus, String user) - 'user' can not be null or empty");
        }
        try
        {
            return this.handleUpdateVerificationStatus(id, verificationStatus, user);
        }
        catch (Throwable th)
        {
            throw new DocumentServiceException(
                "Error performing 'DocumentService.updateVerificationStatus(String id, DocumentVerificationStatus verificationStatus, String user)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #updateVerificationStatus(String, DocumentVerificationStatus, String)}
     * @param id java.lang.String TODO: Model Documentation for DocumentService.updateVerificationStatus(id)
     * @param verificationStatus DocumentVerificationStatus TODO: Model Documentation for
DocumentService.updateVerificationStatus(verificationStatus)
     * @param user java.lang.String TODO: Model Documentation for DocumentService.updateVerificationStatus(user)
     * @return DocumentDTO
     * @throws Exception
     */
    protected abstract DocumentDTO handleUpdateVerificationStatus(String id, DocumentVerificationStatus verificationStatus, String user)
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