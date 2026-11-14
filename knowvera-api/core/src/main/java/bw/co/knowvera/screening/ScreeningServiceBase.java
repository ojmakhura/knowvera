package bw.co.knowvera.screening;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * Spring Service base class for <code>ScreeningService</code>,
 * provides access to all services and entities referenced by this service.
 * </p>
 *
 * @see ScreeningService
 */
public abstract class ScreeningServiceBase
    implements ScreeningService
{
    protected Logger logger = LoggerFactory.getLogger(ScreeningService.class);
    protected final SanctionsListEntryRepository sanctionsListEntryRepository;
    protected final SanctionsListEntryMapper sanctionsListEntryMapper;
    protected final ScreeningHitRepository screeningHitRepository;
    protected final ScreeningHitMapper screeningHitMapper;

    public ScreeningServiceBase(
        SanctionsListEntryRepository sanctionsListEntryRepository,
        SanctionsListEntryMapper sanctionsListEntryMapper,         ScreeningHitRepository screeningHitRepository,
        ScreeningHitMapper screeningHitMapper    ) {
        this.sanctionsListEntryRepository = sanctionsListEntryRepository;
        this.sanctionsListEntryMapper = sanctionsListEntryMapper;
        this.screeningHitRepository = screeningHitRepository;
        this.screeningHitMapper = screeningHitMapper;
    }

    /**
     * Gets the reference to <code>sanctionsListEntry</code>'s repository.
     * @return sanctionsListEntryRepository
     */
    public SanctionsListEntryRepository getSanctionsListEntryRepository() {
        return sanctionsListEntryRepository;
    }

    /**
     * Gets the reference to <code>screeningHit</code>'s repository.
     * @return screeningHitRepository
     */
    public ScreeningHitRepository getScreeningHitRepository() {
        return screeningHitRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ScreeningListStatus updateSanctionsList(final String user)
    {
        if (user == null || user.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.screening.ScreeningService.updateSanctionsList(String user) - 'user' can not be null or empty");
        }
        try
        {
            return this.handleUpdateSanctionsList(user);
        }
        catch (Throwable th)
        {
            throw new ScreeningServiceException(
                "Error performing 'ScreeningService.updateSanctionsList(String user)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #updateSanctionsList(String)}
     * @param user java.lang.String TODO: Model Documentation for ScreeningService.updateSanctionsList(user)
     * @return ScreeningListStatus
     * @throws Exception
     */
    protected abstract ScreeningListStatus handleUpdateSanctionsList(String user)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ScreeningListStatus> getListStatus()
    {
        try
        {
            return this.handleGetListStatus();
        }
        catch (Throwable th)
        {
            throw new ScreeningServiceException(
                "Error performing 'ScreeningService.getListStatus()' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #getListStatus()}
     * @return List<ScreeningListStatus>
     * @throws Exception
     */
    protected abstract List<ScreeningListStatus> handleGetListStatus()
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ScreeningHitDTO> screenRecord(final String kycRecordId)
    {
        if (kycRecordId == null || kycRecordId.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.screening.ScreeningService.screenRecord(String kycRecordId) - 'kycRecordId' can not be null or empty");
        }
        try
        {
            return this.handleScreenRecord(kycRecordId);
        }
        catch (Throwable th)
        {
            throw new ScreeningServiceException(
                "Error performing 'ScreeningService.screenRecord(String kycRecordId)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #screenRecord(String)}
     * @param kycRecordId java.lang.String TODO: Model Documentation for ScreeningService.screenRecord(kycRecordId)
     * @return List<ScreeningHitDTO>
     * @throws Exception
     */
    protected abstract List<ScreeningHitDTO> handleScreenRecord(String kycRecordId)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ScreeningHitDTO> findHitsByRecord(final String kycRecordId)
    {
        if (kycRecordId == null || kycRecordId.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.screening.ScreeningService.findHitsByRecord(String kycRecordId) - 'kycRecordId' can not be null or empty");
        }
        try
        {
            return this.handleFindHitsByRecord(kycRecordId);
        }
        catch (Throwable th)
        {
            throw new ScreeningServiceException(
                "Error performing 'ScreeningService.findHitsByRecord(String kycRecordId)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #findHitsByRecord(String)}
     * @param kycRecordId java.lang.String TODO: Model Documentation for
ScreeningService.findHitsByRecord(kycRecordId)
     * @return List<ScreeningHitDTO>
     * @throws Exception
     */
    protected abstract List<ScreeningHitDTO> handleFindHitsByRecord(String kycRecordId)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ScreeningHitDTO> findPendingHits()
    {
        try
        {
            return this.handleFindPendingHits();
        }
        catch (Throwable th)
        {
            throw new ScreeningServiceException(
                "Error performing 'ScreeningService.findPendingHits()' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #findPendingHits()}
     * @return List<ScreeningHitDTO>
     * @throws Exception
     */
    protected abstract List<ScreeningHitDTO> handleFindPendingHits()
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    public ScreeningHitDTO reviewHit(final String id, final ScreeningHitStatus status, final String notes, final String user)
    {
        if (id == null || id.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.screening.ScreeningService.reviewHit(String id, ScreeningHitStatus status, String notes, String user) - 'id' can not be null or empty");
        }
        if (status == null)
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.screening.ScreeningService.reviewHit(String id, ScreeningHitStatus status, String notes, String user) - 'status' can not be null");
        }
        if (notes == null || notes.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.screening.ScreeningService.reviewHit(String id, ScreeningHitStatus status, String notes, String user) - 'notes' can not be null or empty");
        }
        if (user == null || user.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.screening.ScreeningService.reviewHit(String id, ScreeningHitStatus status, String notes, String user) - 'user' can not be null or empty");
        }
        try
        {
            return this.handleReviewHit(id, status, notes, user);
        }
        catch (Throwable th)
        {
            throw new ScreeningServiceException(
                "Error performing 'ScreeningService.reviewHit(String id, ScreeningHitStatus status, String notes, String user)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #reviewHit(String, ScreeningHitStatus, String, String)}
     * @param id java.lang.String TODO: Model Documentation for ScreeningService.reviewHit(id)
     * @param status ScreeningHitStatus TODO: Model Documentation for ScreeningService.reviewHit(status)
     * @param notes java.lang.String TODO: Model Documentation for ScreeningService.reviewHit(notes)
     * @param user java.lang.String TODO: Model Documentation for ScreeningService.reviewHit(user)
     * @return ScreeningHitDTO
     * @throws Exception
     */
    protected abstract ScreeningHitDTO handleReviewHit(String id, ScreeningHitStatus status, String notes, String user)
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