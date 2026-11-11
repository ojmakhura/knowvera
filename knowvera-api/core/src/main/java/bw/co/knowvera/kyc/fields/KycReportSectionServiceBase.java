package bw.co.knowvera.kyc.fields;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;

/**
 * <p>
 * Spring Service base class for <code>KycReportSectionService</code>,
 * provides access to all services and entities referenced by this service.
 * </p>
 *
 * @see KycReportSectionService
 */
@CacheConfig(cacheNames = "kycReportSections")
public abstract class KycReportSectionServiceBase
    implements KycReportSectionService
{
    protected Logger logger = LoggerFactory.getLogger(KycReportSectionService.class);
    protected final KycReportSectionRepository kycReportSectionRepository;
    protected final KycReportSectionMapper kycReportSectionMapper;
    protected final GroupFieldValueRepository groupFieldValueRepository;
    protected final GroupFieldValueMapper groupFieldValueMapper;

    public KycReportSectionServiceBase(
        KycReportSectionRepository kycReportSectionRepository,
        KycReportSectionMapper kycReportSectionMapper,         GroupFieldValueRepository groupFieldValueRepository,
        GroupFieldValueMapper groupFieldValueMapper    ) {
        this.kycReportSectionRepository = kycReportSectionRepository;
        this.kycReportSectionMapper = kycReportSectionMapper;
        this.groupFieldValueRepository = groupFieldValueRepository;
        this.groupFieldValueMapper = groupFieldValueMapper;
    }

    /**
     * Gets the reference to <code>kycReportSection</code>'s repository.
     * @return kycReportSectionRepository
     */
    public KycReportSectionRepository getKycReportSectionRepository() {
        return kycReportSectionRepository;
    }

    /**
     * Gets the reference to <code>groupFieldValue</code>'s repository.
     * @return groupFieldValueRepository
     */
    public GroupFieldValueRepository getGroupFieldValueRepository() {
        return groupFieldValueRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Cacheable(key = "#id")
    public KycReportSectionDTO findById(final String id)
    {
        if (id == null || id.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.kyc.fields.KycReportSectionService.findById(String id) - 'id' can not be null or empty");
        }
        try
        {
            return this.handleFindById(id);
        }
        catch (Throwable th)
        {
            throw new KycReportSectionServiceException(
                "Error performing 'KycReportSectionService.findById(String id)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #findById(String)}
     * @param id java.lang.String TODO: Model Documentation for KycReportSectionService.findById(id)
     * @return KycReportSectionDTO
     * @throws Exception
     */
    protected abstract KycReportSectionDTO handleFindById(String id)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    @Caching(evict = { @CacheEvict(cacheNames = {"branches", "clientRequests", "documents", "employmentRecords", "individuals", "invoices", "kycRecords", "kycReportSections", "organisations", "settings", "subscriptions"}, allEntries = true, beforeInvocation = true) }, put = { @CachePut(key = "#result.id", unless = "#result == null") })
    public KycReportSectionDTO save(final @Valid KycReportSectionDTO kycReportSection)
    {
        if (kycReportSection == null)
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.kyc.fields.KycReportSectionService.save(@Valid KycReportSectionDTO kycReportSection) - 'kycReportSection' can not be null");
        }
        if (kycReportSection.getLabel() == null || kycReportSection.getLabel().trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.kyc.fields.KycReportSectionService.save(@Valid KycReportSectionDTO kycReportSection) - 'kycReportSection.label' can not be null or empty");
        }
        if (kycReportSection.getId() == null || kycReportSection.getId().trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.kyc.fields.KycReportSectionService.save(@Valid KycReportSectionDTO kycReportSection) - 'kycReportSection.id' can not be null or empty");
        }
        if (kycReportSection.getKycRecordId() == null || kycReportSection.getKycRecordId().trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.kyc.fields.KycReportSectionService.save(@Valid KycReportSectionDTO kycReportSection) - 'kycReportSection.kycRecordId' can not be null or empty");
        }
        if (kycReportSection.getKycRecordRef() == null || kycReportSection.getKycRecordRef().trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.kyc.fields.KycReportSectionService.save(@Valid KycReportSectionDTO kycReportSection) - 'kycReportSection.kycRecordRef' can not be null or empty");
        }
        try
        {
            return this.handleSave(kycReportSection);
        }
        catch (Throwable th)
        {
            throw new KycReportSectionServiceException(
                "Error performing 'KycReportSectionService.save(@Valid KycReportSectionDTO kycReportSection)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #save(@Valid KycReportSectionDTO)}
     * @param kycReportSection KycReportSectionDTO TODO: Model Documentation for
KycReportSectionService.save(kycReportSection)
     * @return KycReportSectionDTO
     * @throws Exception
     */
    protected abstract KycReportSectionDTO handleSave(@Valid KycReportSectionDTO kycReportSection)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    @CacheEvict(cacheNames = {"branches", "clientRequests", "documents", "employmentRecords", "individuals", "invoices", "kycRecords", "kycReportSections", "organisations", "settings", "subscriptions"}, allEntries = true)
    public boolean remove(final String id)
    {
        if (id == null || id.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.kyc.fields.KycReportSectionService.remove(String id) - 'id' can not be null or empty");
        }
        try
        {
            return this.handleRemove(id);
        }
        catch (Throwable th)
        {
            throw new KycReportSectionServiceException(
                "Error performing 'KycReportSectionService.remove(String id)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #remove(String)}
     * @param id java.lang.String TODO: Model Documentation for KycReportSectionService.remove(id)
     * @return boolean
     * @throws Exception
     */
    protected abstract boolean handleRemove(String id)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    @CacheEvict(cacheNames = {"branches", "clientRequests", "documents", "employmentRecords", "individuals", "invoices", "kycRecords", "kycReportSections", "organisations", "settings", "subscriptions"}, allEntries = true)
    public KycReportSectionDTO addFieldValue(final @Valid GroupFieldValueDTO fieldValue)
    {
        if (fieldValue == null)
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.kyc.fields.KycReportSectionService.addFieldValue(@Valid GroupFieldValueDTO fieldValue) - 'fieldValue' can not be null");
        }
        if (fieldValue.getFieldId() == null || fieldValue.getFieldId().trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.kyc.fields.KycReportSectionService.addFieldValue(@Valid GroupFieldValueDTO fieldValue) - 'fieldValue.fieldId' can not be null or empty");
        }
        if (fieldValue.getField() == null || fieldValue.getField().trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.kyc.fields.KycReportSectionService.addFieldValue(@Valid GroupFieldValueDTO fieldValue) - 'fieldValue.field' can not be null or empty");
        }
        if (fieldValue.getId() == null || fieldValue.getId().trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.kyc.fields.KycReportSectionService.addFieldValue(@Valid GroupFieldValueDTO fieldValue) - 'fieldValue.id' can not be null or empty");
        }
        if (fieldValue.getData() == null)
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.kyc.fields.KycReportSectionService.addFieldValue(@Valid GroupFieldValueDTO fieldValue) - 'fieldValue.data' can not be null");
        }
        if (fieldValue.getReportSectionId() == null || fieldValue.getReportSectionId().trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.kyc.fields.KycReportSectionService.addFieldValue(@Valid GroupFieldValueDTO fieldValue) - 'fieldValue.reportSectionId' can not be null or empty");
        }
        if (fieldValue.getFieldLabel() == null || fieldValue.getFieldLabel().trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.kyc.fields.KycReportSectionService.addFieldValue(@Valid GroupFieldValueDTO fieldValue) - 'fieldValue.fieldLabel' can not be null or empty");
        }
        try
        {
            return this.handleAddFieldValue(fieldValue);
        }
        catch (Throwable th)
        {
            throw new KycReportSectionServiceException(
                "Error performing 'KycReportSectionService.addFieldValue(@Valid GroupFieldValueDTO fieldValue)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #addFieldValue(@Valid GroupFieldValueDTO)}
     * @param fieldValue GroupFieldValueDTO TODO: Model Documentation for
KycReportSectionService.addFieldValue(fieldValue)
     * @return KycReportSectionDTO
     * @throws Exception
     */
    protected abstract KycReportSectionDTO handleAddFieldValue(@Valid GroupFieldValueDTO fieldValue)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    @CacheEvict(cacheNames = {"branches", "clientRequests", "documents", "employmentRecords", "individuals", "invoices", "kycRecords", "kycReportSections", "organisations", "settings", "subscriptions"}, allEntries = true)
    public KycReportSectionDTO removeFieldValue(final String id, final String fieldValueId)
    {
        if (id == null || id.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.kyc.fields.KycReportSectionService.removeFieldValue(String id, String fieldValueId) - 'id' can not be null or empty");
        }
        if (fieldValueId == null || fieldValueId.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.kyc.fields.KycReportSectionService.removeFieldValue(String id, String fieldValueId) - 'fieldValueId' can not be null or empty");
        }
        try
        {
            return this.handleRemoveFieldValue(id, fieldValueId);
        }
        catch (Throwable th)
        {
            throw new KycReportSectionServiceException(
                "Error performing 'KycReportSectionService.removeFieldValue(String id, String fieldValueId)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #removeFieldValue(String, String)}
     * @param id java.lang.String TODO: Model Documentation for KycReportSectionService.removeFieldValue(id)
     * @param fieldValueId java.lang.String TODO: Model Documentation for
KycReportSectionService.removeFieldValue(fieldValueId)
     * @return KycReportSectionDTO
     * @throws Exception
     */
    protected abstract KycReportSectionDTO handleRemoveFieldValue(String id, String fieldValueId)
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