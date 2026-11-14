package bw.co.knowvera.kyc;

import bw.co.knowvera.PropertySearchOrder;
import bw.co.knowvera.SearchObject;
import bw.co.knowvera.TargetEntity;
import bw.co.knowvera.document.DocumentDTO;
import bw.co.knowvera.user.UserDTO;
import jakarta.validation.Valid;
import java.time.LocalDate;
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
 * Spring Service base class for <code>KycRecordService</code>,
 * provides access to all services and entities referenced by this service.
 * </p>
 *
 * @see KycRecordService
 */
@CacheConfig(cacheNames = "kycRecords")
public abstract class KycRecordServiceBase
    implements KycRecordService
{
    protected Logger logger = LoggerFactory.getLogger(KycRecordService.class);
    protected final KycRecordRepository kycRecordRepository;
    protected final KycRecordMapper kycRecordMapper;

    public KycRecordServiceBase(
        KycRecordRepository kycRecordRepository,
        KycRecordMapper kycRecordMapper    ) {
        this.kycRecordRepository = kycRecordRepository;
        this.kycRecordMapper = kycRecordMapper;
    }

    /**
     * Gets the reference to <code>kycRecord</code>'s repository.
     * @return kycRecordRepository
     */
    public KycRecordRepository getKycRecordRepository() {
        return kycRecordRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Cacheable(key = "#id")
    public KycRecordDTO findById(final String id)
    {
        if (id == null || id.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.kyc.KycRecordService.findById(String id) - 'id' can not be null or empty");
        }
        try
        {
            return this.handleFindById(id);
        }
        catch (Throwable th)
        {
            throw new KycRecordServiceException(
                "Error performing 'KycRecordService.findById(String id)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #findById(String)}
     * @param id java.lang.String TODO: Model Documentation for KycRecordService.findById(id)
     * @return KycRecordDTO
     * @throws Exception
     */
    protected abstract KycRecordDTO handleFindById(String id)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    @Caching(evict = { @CacheEvict(cacheNames = {"branches", "clientRequests", "documents", "employmentRecords", "individuals", "invoices", "kycRecords", "organisations", "settings", "subscriptions"}, allEntries = true, beforeInvocation = true) }, put = { @CachePut(key = "#result.id", unless = "#result == null") })
    public KycRecordDTO save(final @Valid KycRecordDTO kycRecord)
    {
        if (kycRecord == null)
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.kyc.KycRecordService.save(@Valid KycRecordDTO kycRecord) - 'kycRecord' can not be null");
        }
        if (kycRecord.getKycStatus() == null)
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.kyc.KycRecordService.save(@Valid KycRecordDTO kycRecord) - 'kycRecord.kycStatus' can not be null");
        }
        if (kycRecord.getTargetId() == null || kycRecord.getTargetId().trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.kyc.KycRecordService.save(@Valid KycRecordDTO kycRecord) - 'kycRecord.targetId' can not be null or empty");
        }
        if (kycRecord.getTarget() == null)
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.kyc.KycRecordService.save(@Valid KycRecordDTO kycRecord) - 'kycRecord.target' can not be null");
        }
        if (kycRecord.getSourceOfFunds() == null)
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.kyc.KycRecordService.save(@Valid KycRecordDTO kycRecord) - 'kycRecord.sourceOfFunds' can not be null");
        }
        if (kycRecord.getOwnerDetails() == null)
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.kyc.KycRecordService.save(@Valid KycRecordDTO kycRecord) - 'kycRecord.ownerDetails' can not be null");
        }
        try
        {
            return this.handleSave(kycRecord);
        }
        catch (Throwable th)
        {
            throw new KycRecordServiceException(
                "Error performing 'KycRecordService.save(@Valid KycRecordDTO kycRecord)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #save(@Valid KycRecordDTO)}
     * @param kycRecord KycRecordDTO TODO: Model Documentation for KycRecordService.save(kycRecord)
     * @return KycRecordDTO
     * @throws Exception
     */
    protected abstract KycRecordDTO handleSave(@Valid KycRecordDTO kycRecord)
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
                "bw.co.knowvera.kyc.KycRecordService.remove(String id) - 'id' can not be null or empty");
        }
        try
        {
            return this.handleRemove(id);
        }
        catch (Throwable th)
        {
            throw new KycRecordServiceException(
                "Error performing 'KycRecordService.remove(String id)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #remove(String)}
     * @param id java.lang.String TODO: Model Documentation for KycRecordService.remove(id)
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
    public List<KycRecordListDTO> getAll()
    {
        try
        {
            return this.handleGetAll();
        }
        catch (Throwable th)
        {
            throw new KycRecordServiceException(
                "Error performing 'KycRecordService.getAll()' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #getAll()}
     * @return List<KycRecordListDTO>
     * @throws Exception
     */
    protected abstract List<KycRecordListDTO> handleGetAll()
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<KycRecordListDTO> search(final @Valid KycRecordSearchCriteria criteria, final @Valid Set<PropertySearchOrder> orderings)
    {
        if (criteria == null)
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.kyc.KycRecordService.search(@Valid KycRecordSearchCriteria criteria, @Valid Set<PropertySearchOrder> orderings) - 'criteria' can not be null");
        }
        if (orderings == null)
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.kyc.KycRecordService.search(@Valid KycRecordSearchCriteria criteria, @Valid Set<PropertySearchOrder> orderings) - 'orderings' can not be null");
        }
        try
        {
            return this.handleSearch(criteria, orderings);
        }
        catch (Throwable th)
        {
            throw new KycRecordServiceException(
                "Error performing 'KycRecordService.search(@Valid KycRecordSearchCriteria criteria, @Valid Set<PropertySearchOrder> orderings)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #search(@Valid KycRecordSearchCriteria, @Valid Set<PropertySearchOrder>)}
     * @param criteria KycRecordSearchCriteria TODO: Model Documentation for KycRecordService.search(criteria)
     * @param orderings PropertySearchOrder TODO: Model Documentation for KycRecordService.search(orderings)
     * @return List<KycRecordListDTO>
     * @throws Exception
     */
    protected abstract List<KycRecordListDTO> handleSearch(@Valid KycRecordSearchCriteria criteria, @Valid Set<PropertySearchOrder> orderings)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<KycRecordListDTO> getAll(final Integer pageNumber, final Integer pageSize)
    {
        try
        {
            return this.handleGetAll(pageNumber, pageSize);
        }
        catch (Throwable th)
        {
            throw new KycRecordServiceException(
                "Error performing 'KycRecordService.getAll(Integer pageNumber, Integer pageSize)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #getAll(Integer, Integer)}
     * @param pageNumber java.lang.Integer TODO: Model Documentation for KycRecordService.getAll(pageNumber)
     * @param pageSize java.lang.Integer TODO: Model Documentation for KycRecordService.getAll(pageSize)
     * @return Page<KycRecordListDTO>
     * @throws Exception
     */
    protected abstract Page<KycRecordListDTO> handleGetAll(Integer pageNumber, Integer pageSize)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<KycRecordListDTO> search(final SearchObject<KycRecordSearchCriteria> criteria)
    {
        try
        {
            return this.handleSearch(criteria);
        }
        catch (Throwable th)
        {
            throw new KycRecordServiceException(
                "Error performing 'KycRecordService.search(SearchObject<KycRecordSearchCriteria> criteria)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #search(SearchObject<KycRecordSearchCriteria>)}
     * @param criteria SearchObject<KycRecordSearchCriteria> TODO: Model Documentation for KycRecordService.search(criteria)
     * @return Page<KycRecordListDTO>
     * @throws Exception
     */
    protected abstract Page<KycRecordListDTO> handleSearch(SearchObject<KycRecordSearchCriteria> criteria)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<KycRecordListDTO> findByIndividual(final String individualId)
    {
        if (individualId == null || individualId.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.kyc.KycRecordService.findByIndividual(String individualId) - 'individualId' can not be null or empty");
        }
        try
        {
            return this.handleFindByIndividual(individualId);
        }
        catch (Throwable th)
        {
            throw new KycRecordServiceException(
                "Error performing 'KycRecordService.findByIndividual(String individualId)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #findByIndividual(String)}
     * @param individualId java.lang.String TODO: Model Documentation for KycRecordService.findByIndividual(individualId)
     * @return List<KycRecordListDTO>
     * @throws Exception
     */
    protected abstract List<KycRecordListDTO> handleFindByIndividual(String individualId)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<KycRecordListDTO> findByIdentityNo(final String identityNo)
    {
        if (identityNo == null || identityNo.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.kyc.KycRecordService.findByIdentityNo(String identityNo) - 'identityNo' can not be null or empty");
        }
        try
        {
            return this.handleFindByIdentityNo(identityNo);
        }
        catch (Throwable th)
        {
            throw new KycRecordServiceException(
                "Error performing 'KycRecordService.findByIdentityNo(String identityNo)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #findByIdentityNo(String)}
     * @param identityNo java.lang.String TODO: Model Documentation for KycRecordService.findByIdentityNo(identityNo)
     * @return List<KycRecordListDTO>
     * @throws Exception
     */
    protected abstract List<KycRecordListDTO> handleFindByIdentityNo(String identityNo)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<KycRecordListDTO> findByOrganisation(final String organisationId)
    {
        if (organisationId == null || organisationId.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.kyc.KycRecordService.findByOrganisation(String organisationId) - 'organisationId' can not be null or empty");
        }
        try
        {
            return this.handleFindByOrganisation(organisationId);
        }
        catch (Throwable th)
        {
            throw new KycRecordServiceException(
                "Error performing 'KycRecordService.findByOrganisation(String organisationId)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #findByOrganisation(String)}
     * @param organisationId java.lang.String TODO: Model Documentation for KycRecordService.findByOrganisation(organisationId)
     * @return List<KycRecordListDTO>
     * @throws Exception
     */
    protected abstract List<KycRecordListDTO> handleFindByOrganisation(String organisationId)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<KycRecordListDTO> findByIndividual(final String individualId, final Integer pageNumber, final Integer pageSize)
    {
        if (individualId == null || individualId.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.kyc.KycRecordService.findByIndividual(String individualId, Integer pageNumber, Integer pageSize) - 'individualId' can not be null or empty");
        }
        try
        {
            return this.handleFindByIndividual(individualId, pageNumber, pageSize);
        }
        catch (Throwable th)
        {
            throw new KycRecordServiceException(
                "Error performing 'KycRecordService.findByIndividual(String individualId, Integer pageNumber, Integer pageSize)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #findByIndividual(String, Integer, Integer)}
     * @param individualId java.lang.String TODO: Model Documentation for KycRecordService.findByIndividual(individualId)
     * @param pageNumber java.lang.Integer TODO: Model Documentation for KycRecordService.findByIndividual(pageNumber)
     * @param pageSize java.lang.Integer TODO: Model Documentation for KycRecordService.findByIndividual(pageSize)
     * @return Page<KycRecordListDTO>
     * @throws Exception
     */
    protected abstract Page<KycRecordListDTO> handleFindByIndividual(String individualId, Integer pageNumber, Integer pageSize)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<KycRecordListDTO> findByIdentityNo(final String identityNo, final Integer pageNumber, final Integer pageSize)
    {
        if (identityNo == null || identityNo.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.kyc.KycRecordService.findByIdentityNo(String identityNo, Integer pageNumber, Integer pageSize) - 'identityNo' can not be null or empty");
        }
        try
        {
            return this.handleFindByIdentityNo(identityNo, pageNumber, pageSize);
        }
        catch (Throwable th)
        {
            throw new KycRecordServiceException(
                "Error performing 'KycRecordService.findByIdentityNo(String identityNo, Integer pageNumber, Integer pageSize)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #findByIdentityNo(String, Integer, Integer)}
     * @param identityNo java.lang.String TODO: Model Documentation for KycRecordService.findByIdentityNo(identityNo)
     * @param pageNumber java.lang.Integer TODO: Model Documentation for KycRecordService.findByIdentityNo(pageNumber)
     * @param pageSize java.lang.Integer TODO: Model Documentation for KycRecordService.findByIdentityNo(pageSize)
     * @return Page<KycRecordListDTO>
     * @throws Exception
     */
    protected abstract Page<KycRecordListDTO> handleFindByIdentityNo(String identityNo, Integer pageNumber, Integer pageSize)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<KycRecordListDTO> findByOrganisation(final String organisationId, final Integer pageNumber, final Integer pageSize)
    {
        if (organisationId == null || organisationId.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.kyc.KycRecordService.findByOrganisation(String organisationId, Integer pageNumber, Integer pageSize) - 'organisationId' can not be null or empty");
        }
        try
        {
            return this.handleFindByOrganisation(organisationId, pageNumber, pageSize);
        }
        catch (Throwable th)
        {
            throw new KycRecordServiceException(
                "Error performing 'KycRecordService.findByOrganisation(String organisationId, Integer pageNumber, Integer pageSize)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #findByOrganisation(String, Integer, Integer)}
     * @param organisationId java.lang.String TODO: Model Documentation for KycRecordService.findByOrganisation(organisationId)
     * @param pageNumber java.lang.Integer TODO: Model Documentation for KycRecordService.findByOrganisation(pageNumber)
     * @param pageSize java.lang.Integer TODO: Model Documentation for KycRecordService.findByOrganisation(pageSize)
     * @return Page<KycRecordListDTO>
     * @throws Exception
     */
    protected abstract Page<KycRecordListDTO> handleFindByOrganisation(String organisationId, Integer pageNumber, Integer pageSize)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    @CacheEvict(cacheNames = {"branches", "clientRequests", "documents", "employmentRecords", "individuals", "invoices", "kycRecords", "organisations", "settings", "subscriptions"}, allEntries = true)
    public KycRecordDTO createTargetRecord(final String targetId, final TargetEntity target, final String user)
    {
        if (targetId == null || targetId.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.kyc.KycRecordService.createTargetRecord(String targetId, TargetEntity target, String user) - 'targetId' can not be null or empty");
        }
        if (target == null)
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.kyc.KycRecordService.createTargetRecord(String targetId, TargetEntity target, String user) - 'target' can not be null");
        }
        if (user == null || user.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.kyc.KycRecordService.createTargetRecord(String targetId, TargetEntity target, String user) - 'user' can not be null or empty");
        }
        try
        {
            return this.handleCreateTargetRecord(targetId, target, user);
        }
        catch (Throwable th)
        {
            throw new KycRecordServiceException(
                "Error performing 'KycRecordService.createTargetRecord(String targetId, TargetEntity target, String user)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #createTargetRecord(String, TargetEntity, String)}
     * @param targetId java.lang.String TODO: Model Documentation for KycRecordService.createTargetRecord(targetId)
     * @param target TargetEntity TODO: Model Documentation for KycRecordService.createTargetRecord(target)
     * @param user java.lang.String TODO: Model Documentation for KycRecordService.createTargetRecord(user)
     * @return KycRecordDTO
     * @throws Exception
     */
    protected abstract KycRecordDTO handleCreateTargetRecord(String targetId, TargetEntity target, String user)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    @CacheEvict(cacheNames = {"branches", "clientRequests", "documents", "employmentRecords", "individuals", "invoices", "kycRecords", "organisations", "settings", "subscriptions"}, allEntries = true)
    public boolean confirmOwnership(final String kycRecordId, final @Valid UserDTO user)
    {
        if (kycRecordId == null || kycRecordId.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.kyc.KycRecordService.confirmOwnership(String kycRecordId, @Valid UserDTO user) - 'kycRecordId' can not be null or empty");
        }
        if (user == null)
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.kyc.KycRecordService.confirmOwnership(String kycRecordId, @Valid UserDTO user) - 'user' can not be null");
        }
        if (user.getUsername() == null || user.getUsername().trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.kyc.KycRecordService.confirmOwnership(String kycRecordId, @Valid UserDTO user) - 'user.username' can not be null or empty");
        }
        if (user.getEmail() == null || user.getEmail().trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.kyc.KycRecordService.confirmOwnership(String kycRecordId, @Valid UserDTO user) - 'user.email' can not be null or empty");
        }
        if (user.getFirstName() == null || user.getFirstName().trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.kyc.KycRecordService.confirmOwnership(String kycRecordId, @Valid UserDTO user) - 'user.firstName' can not be null or empty");
        }
        if (user.getLastName() == null || user.getLastName().trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.kyc.KycRecordService.confirmOwnership(String kycRecordId, @Valid UserDTO user) - 'user.lastName' can not be null or empty");
        }
        try
        {
            return this.handleConfirmOwnership(kycRecordId, user);
        }
        catch (Throwable th)
        {
            throw new KycRecordServiceException(
                "Error performing 'KycRecordService.confirmOwnership(String kycRecordId, @Valid UserDTO user)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #confirmOwnership(String, @Valid UserDTO)}
     * @param kycRecordId java.lang.String TODO: Model Documentation for KycRecordService.confirmOwnership(kycRecordId)
     * @param user UserDTO TODO: Model Documentation for KycRecordService.confirmOwnership(user)
     * @return boolean
     * @throws Exception
     */
    protected abstract boolean handleConfirmOwnership(String kycRecordId, @Valid UserDTO user)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    public KycRecordDTO findLatestValidForOwner(final String ownerId, final TargetEntity ownerType, final LocalDate today)
    {
        if (ownerId == null || ownerId.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.kyc.KycRecordService.findLatestValidForOwner(String ownerId, TargetEntity ownerType, LocalDate today) - 'ownerId' can not be null or empty");
        }
        if (ownerType == null)
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.kyc.KycRecordService.findLatestValidForOwner(String ownerId, TargetEntity ownerType, LocalDate today) - 'ownerType' can not be null");
        }
        if (today == null)
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.kyc.KycRecordService.findLatestValidForOwner(String ownerId, TargetEntity ownerType, LocalDate today) - 'today' can not be null");
        }
        try
        {
            return this.handleFindLatestValidForOwner(ownerId, ownerType, today);
        }
        catch (Throwable th)
        {
            throw new KycRecordServiceException(
                "Error performing 'KycRecordService.findLatestValidForOwner(String ownerId, TargetEntity ownerType, LocalDate today)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #findLatestValidForOwner(String, TargetEntity, LocalDate)}
     * @param ownerId java.lang.String TODO: Model Documentation for KycRecordService.findLatestValidForOwner(ownerId)
     * @param ownerType TargetEntity TODO: Model Documentation for KycRecordService.findLatestValidForOwner(ownerType)
     * @param today LocalDate TODO: Model Documentation for KycRecordService.findLatestValidForOwner(today)
     * @return KycRecordDTO
     * @throws Exception
     */
    protected abstract KycRecordDTO handleFindLatestValidForOwner(String ownerId, TargetEntity ownerType, LocalDate today)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    @CacheEvict(cacheNames = {"branches", "clientRequests", "documents", "employmentRecords", "individuals", "invoices", "kycRecords", "organisations", "settings", "subscriptions"}, allEntries = true)
    public KycRecordDTO createNew(final @Valid KycRecordDTO record, final String user)
    {
        if (record == null)
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.kyc.KycRecordService.createNew(@Valid KycRecordDTO record, String user) - 'record' can not be null");
        }
        if (record.getKycStatus() == null)
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.kyc.KycRecordService.createNew(@Valid KycRecordDTO record, String user) - 'record.kycStatus' can not be null");
        }
        if (record.getTargetId() == null || record.getTargetId().trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.kyc.KycRecordService.createNew(@Valid KycRecordDTO record, String user) - 'record.targetId' can not be null or empty");
        }
        if (record.getTarget() == null)
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.kyc.KycRecordService.createNew(@Valid KycRecordDTO record, String user) - 'record.target' can not be null");
        }
        if (record.getSourceOfFunds() == null)
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.kyc.KycRecordService.createNew(@Valid KycRecordDTO record, String user) - 'record.sourceOfFunds' can not be null");
        }
        if (record.getOwnerDetails() == null)
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.kyc.KycRecordService.createNew(@Valid KycRecordDTO record, String user) - 'record.ownerDetails' can not be null");
        }
        if (user == null || user.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.kyc.KycRecordService.createNew(@Valid KycRecordDTO record, String user) - 'user' can not be null or empty");
        }
        try
        {
            return this.handleCreateNew(record, user);
        }
        catch (Throwable th)
        {
            throw new KycRecordServiceException(
                "Error performing 'KycRecordService.createNew(@Valid KycRecordDTO record, String user)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #createNew(@Valid KycRecordDTO, String)}
     * @param record KycRecordDTO TODO: Model Documentation for KycRecordService.createNew(record)
     * @param user java.lang.String TODO: Model Documentation for KycRecordService.createNew(user)
     * @return KycRecordDTO
     * @throws Exception
     */
    protected abstract KycRecordDTO handleCreateNew(@Valid KycRecordDTO record, String user)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    @CacheEvict(cacheNames = {"branches", "clientRequests", "documents", "employmentRecords", "individuals", "invoices", "kycRecords", "organisations", "settings", "subscriptions"}, allEntries = true)
    public KycRecordDTO removeRecordFile(final String id, final String documentId)
    {
        if (id == null || id.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.kyc.KycRecordService.removeRecordFile(String id, String documentId) - 'id' can not be null or empty");
        }
        if (documentId == null || documentId.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.kyc.KycRecordService.removeRecordFile(String id, String documentId) - 'documentId' can not be null or empty");
        }
        try
        {
            return this.handleRemoveRecordFile(id, documentId);
        }
        catch (Throwable th)
        {
            throw new KycRecordServiceException(
                "Error performing 'KycRecordService.removeRecordFile(String id, String documentId)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #removeRecordFile(String, String)}
     * @param id java.lang.String TODO: Model Documentation for KycRecordService.removeRecordFile(id)
     * @param documentId java.lang.String TODO: Model Documentation for KycRecordService.removeRecordFile(documentId)
     * @return KycRecordDTO
     * @throws Exception
     */
    protected abstract KycRecordDTO handleRemoveRecordFile(String id, String documentId)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    @CacheEvict(cacheNames = {"branches", "clientRequests", "documents", "employmentRecords", "individuals", "invoices", "kycRecords", "organisations", "settings", "subscriptions"}, allEntries = true)
    public KycRecordDTO updateRecordFiles(final String id, final @Valid List<DocumentDTO> documents)
    {
        if (id == null || id.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.kyc.KycRecordService.updateRecordFiles(String id, @Valid List<DocumentDTO> documents) - 'id' can not be null or empty");
        }
        if (documents == null)
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.kyc.KycRecordService.updateRecordFiles(String id, @Valid List<DocumentDTO> documents) - 'documents' can not be null");
        }
        try
        {
            return this.handleUpdateRecordFiles(id, documents);
        }
        catch (Throwable th)
        {
            throw new KycRecordServiceException(
                "Error performing 'KycRecordService.updateRecordFiles(String id, @Valid List<DocumentDTO> documents)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #updateRecordFiles(String, @Valid List<DocumentDTO>)}
     * @param id java.lang.String TODO: Model Documentation for KycRecordService.updateRecordFiles(id)
     * @param documents DocumentDTO TODO: Model Documentation for KycRecordService.updateRecordFiles(documents)
     * @return KycRecordDTO
     * @throws Exception
     */
    protected abstract KycRecordDTO handleUpdateRecordFiles(String id, @Valid List<DocumentDTO> documents)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    public KycRecordSummary findSummaryById(final String id, final String organisationId)
    {
        if (id == null || id.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.kyc.KycRecordService.findSummaryById(String id, String organisationId) - 'id' can not be null or empty");
        }
        try
        {
            return this.handleFindSummaryById(id, organisationId);
        }
        catch (Throwable th)
        {
            throw new KycRecordServiceException(
                "Error performing 'KycRecordService.findSummaryById(String id, String organisationId)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #findSummaryById(String, String)}
     * @param id java.lang.String TODO: Model Documentation for KycRecordService.findSummaryById(id)
     * @param organisationId java.lang.String TODO: Model Documentation for KycRecordService.findSummaryById(organisationId)
     * @return KycRecordSummary
     * @throws Exception
     */
    protected abstract KycRecordSummary handleFindSummaryById(String id, String organisationId)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    @CacheEvict(cacheNames = {"branches", "clientRequests", "documents", "employmentRecords", "individuals", "invoices", "kycRecords", "organisations", "settings", "subscriptions"}, allEntries = true)
    public KycRecordDTO runVerification(final String id, final String user)
    {
        if (id == null || id.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.kyc.KycRecordService.runVerification(String id, String user) - 'id' can not be null or empty");
        }
        if (user == null || user.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.kyc.KycRecordService.runVerification(String id, String user) - 'user' can not be null or empty");
        }
        try
        {
            return this.handleRunVerification(id, user);
        }
        catch (Throwable th)
        {
            throw new KycRecordServiceException(
                "Error performing 'KycRecordService.runVerification(String id, String user)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #runVerification(String, String)}
     * @param id java.lang.String TODO: Model Documentation for KycRecordService.runVerification(id)
     * @param user java.lang.String TODO: Model Documentation for KycRecordService.runVerification(user)
     * @return KycRecordDTO
     * @throws Exception
     */
    protected abstract KycRecordDTO handleRunVerification(String id, String user)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    @CacheEvict(cacheNames = {"branches", "clientRequests", "documents", "employmentRecords", "individuals", "invoices", "kycRecords", "organisations", "settings", "subscriptions"}, allEntries = true)
    public KycRecordDTO generateKycReport(final String id, final String user)
    {
        if (id == null || id.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.kyc.KycRecordService.generateKycReport(String id, String user) - 'id' can not be null or empty");
        }
        if (user == null || user.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.kyc.KycRecordService.generateKycReport(String id, String user) - 'user' can not be null or empty");
        }
        try
        {
            return this.handleGenerateKycReport(id, user);
        }
        catch (Throwable th)
        {
            throw new KycRecordServiceException(
                "Error performing 'KycRecordService.generateKycReport(String id, String user)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #generateKycReport(String, String)}
     * @param id java.lang.String TODO: Model Documentation for KycRecordService.generateKycReport(id)
     * @param user java.lang.String TODO: Model Documentation for KycRecordService.generateKycReport(user)
     * @return KycRecordDTO
     * @throws Exception
     */
    protected abstract KycRecordDTO handleGenerateKycReport(String id, String user)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    @CacheEvict(cacheNames = {"branches", "clientRequests", "documents", "employmentRecords", "individuals", "invoices", "kycRecords", "organisations", "settings", "subscriptions"}, allEntries = true)
    public KycRecordDTO updateStatus(final String id, final KycComplianceStatus kycStatus, final String user)
    {
        if (id == null || id.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.kyc.KycRecordService.updateStatus(String id, KycComplianceStatus kycStatus, String user) - 'id' can not be null or empty");
        }
        if (kycStatus == null)
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.kyc.KycRecordService.updateStatus(String id, KycComplianceStatus kycStatus, String user) - 'kycStatus' can not be null");
        }
        if (user == null || user.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.kyc.KycRecordService.updateStatus(String id, KycComplianceStatus kycStatus, String user) - 'user' can not be null or empty");
        }
        try
        {
            return this.handleUpdateStatus(id, kycStatus, user);
        }
        catch (Throwable th)
        {
            throw new KycRecordServiceException(
                "Error performing 'KycRecordService.updateStatus(String id, KycComplianceStatus kycStatus, String user)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #updateStatus(String, KycComplianceStatus, String)}
     * @param id java.lang.String TODO: Model Documentation for KycRecordService.updateStatus(id)
     * @param kycStatus KycComplianceStatus TODO: Model Documentation for KycRecordService.updateStatus(kycStatus)
     * @param user java.lang.String TODO: Model Documentation for KycRecordService.updateStatus(user)
     * @return KycRecordDTO
     * @throws Exception
     */
    protected abstract KycRecordDTO handleUpdateStatus(String id, KycComplianceStatus kycStatus, String user)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<KycRecordSummary> findOrganisationSummaries(final String organisationId, final Boolean forIndividual)
    {
        try
        {
            return this.handleFindOrganisationSummaries(organisationId, forIndividual);
        }
        catch (Throwable th)
        {
            throw new KycRecordServiceException(
                "Error performing 'KycRecordService.findOrganisationSummaries(String organisationId, Boolean forIndividual)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #findOrganisationSummaries(String, Boolean)}
     * @param organisationId java.lang.String TODO: Model Documentation for
KycRecordService.findOrganisationSummaries(organisationId)
     * @param forIndividual java.lang.Boolean TODO: Model Documentation for
KycRecordService.findOrganisationSummaries(forIndividual)
     * @return List<KycRecordSummary>
     * @throws Exception
     */
    protected abstract List<KycRecordSummary> handleFindOrganisationSummaries(String organisationId, Boolean forIndividual)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<KycRecordSummary> findOrganisationSummaries(final String organisationId, final Boolean forIndividual, final Integer pageSize, final Integer pageNumber)
    {
        try
        {
            return this.handleFindOrganisationSummaries(organisationId, forIndividual, pageSize, pageNumber);
        }
        catch (Throwable th)
        {
            throw new KycRecordServiceException(
                "Error performing 'KycRecordService.findOrganisationSummaries(String organisationId, Boolean forIndividual, Integer pageSize, Integer pageNumber)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #findOrganisationSummaries(String, Boolean, Integer, Integer)}
     * @param organisationId java.lang.String TODO: Model Documentation for
KycRecordService.findOrganisationSummaries(organisationId)
     * @param forIndividual java.lang.Boolean TODO: Model Documentation for
KycRecordService.findOrganisationSummaries(forIndividual)
     * @param pageSize java.lang.Integer TODO: Model Documentation for
KycRecordService.findOrganisationSummaries(pageSize)
     * @param pageNumber java.lang.Integer TODO: Model Documentation for
KycRecordService.findOrganisationSummaries(pageNumber)
     * @return Page<KycRecordSummary>
     * @throws Exception
     */
    protected abstract Page<KycRecordSummary> handleFindOrganisationSummaries(String organisationId, Boolean forIndividual, Integer pageSize, Integer pageNumber)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<KycRecordSummary> searchSummaries(final SearchObject<KycRecordSearchCriteria> criteria)
    {
        try
        {
            return this.handleSearchSummaries(criteria);
        }
        catch (Throwable th)
        {
            throw new KycRecordServiceException(
                "Error performing 'KycRecordService.searchSummaries(SearchObject<KycRecordSearchCriteria> criteria)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #searchSummaries(SearchObject<KycRecordSearchCriteria>)}
     * @param criteria SearchObject<KycRecordSearchCriteria> TODO: Model Documentation for KycRecordService.searchSummaries(criteria)
     * @return Page<KycRecordSummary>
     * @throws Exception
     */
    protected abstract Page<KycRecordSummary> handleSearchSummaries(SearchObject<KycRecordSearchCriteria> criteria)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<KycRecordSummary> searchSummaries(final @Valid KycRecordSearchCriteria criteria, final @Valid Set<PropertySearchOrder> orderings)
    {
        if (criteria == null)
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.kyc.KycRecordService.searchSummaries(@Valid KycRecordSearchCriteria criteria, @Valid Set<PropertySearchOrder> orderings) - 'criteria' can not be null");
        }
        if (orderings == null)
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.kyc.KycRecordService.searchSummaries(@Valid KycRecordSearchCriteria criteria, @Valid Set<PropertySearchOrder> orderings) - 'orderings' can not be null");
        }
        try
        {
            return this.handleSearchSummaries(criteria, orderings);
        }
        catch (Throwable th)
        {
            throw new KycRecordServiceException(
                "Error performing 'KycRecordService.searchSummaries(@Valid KycRecordSearchCriteria criteria, @Valid Set<PropertySearchOrder> orderings)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #searchSummaries(@Valid KycRecordSearchCriteria, @Valid Set<PropertySearchOrder>)}
     * @param criteria KycRecordSearchCriteria TODO: Model Documentation for KycRecordService.searchSummaries(criteria)
     * @param orderings PropertySearchOrder TODO: Model Documentation for KycRecordService.searchSummaries(orderings)
     * @return List<KycRecordSummary>
     * @throws Exception
     */
    protected abstract List<KycRecordSummary> handleSearchSummaries(@Valid KycRecordSearchCriteria criteria, @Valid Set<PropertySearchOrder> orderings)
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