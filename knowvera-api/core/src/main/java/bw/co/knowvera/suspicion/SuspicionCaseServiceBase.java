package bw.co.knowvera.suspicion;

import jakarta.validation.Valid;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * Spring Service base class for <code>SuspicionCaseService</code>,
 * provides access to all services and entities referenced by this service.
 * </p>
 *
 * @see SuspicionCaseService
 */
public abstract class SuspicionCaseServiceBase
    implements SuspicionCaseService
{
    protected Logger logger = LoggerFactory.getLogger(SuspicionCaseService.class);
    protected final SuspicionCaseRepository suspicionCaseRepository;
    protected final SuspicionCaseMapper suspicionCaseMapper;

    public SuspicionCaseServiceBase(
        SuspicionCaseRepository suspicionCaseRepository,
        SuspicionCaseMapper suspicionCaseMapper    ) {
        this.suspicionCaseRepository = suspicionCaseRepository;
        this.suspicionCaseMapper = suspicionCaseMapper;
    }

    /**
     * Gets the reference to <code>suspicionCase</code>'s repository.
     * @return suspicionCaseRepository
     */
    public SuspicionCaseRepository getSuspicionCaseRepository() {
        return suspicionCaseRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SuspicionCaseDTO raise(final @Valid SuspicionCaseDTO suspicionCase, final String user)
    {
        if (suspicionCase == null)
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.suspicion.SuspicionCaseService.raise(@Valid SuspicionCaseDTO suspicionCase, String user) - 'suspicionCase' can not be null");
        }
        if (user == null || user.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.suspicion.SuspicionCaseService.raise(@Valid SuspicionCaseDTO suspicionCase, String user) - 'user' can not be null or empty");
        }
        try
        {
            return this.handleRaise(suspicionCase, user);
        }
        catch (Throwable th)
        {
            throw new SuspicionCaseServiceException(
                "Error performing 'SuspicionCaseService.raise(@Valid SuspicionCaseDTO suspicionCase, String user)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #raise(@Valid SuspicionCaseDTO, String)}
     * @param suspicionCase SuspicionCaseDTO TODO: Model Documentation for SuspicionCaseService.raise(suspicionCase)
     * @param user String TODO: Model Documentation for SuspicionCaseService.raise(user)
     * @return SuspicionCaseDTO
     * @throws Exception
     */
    protected abstract SuspicionCaseDTO handleRaise(@Valid SuspicionCaseDTO suspicionCase, String user)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    public SuspicionCaseDTO findById(final String id)
    {
        if (id == null || id.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.suspicion.SuspicionCaseService.findById(String id) - 'id' can not be null or empty");
        }
        try
        {
            return this.handleFindById(id);
        }
        catch (Throwable th)
        {
            throw new SuspicionCaseServiceException(
                "Error performing 'SuspicionCaseService.findById(String id)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #findById(String)}
     * @param id String TODO: Model Documentation for SuspicionCaseService.findById(id)
     * @return SuspicionCaseDTO
     * @throws Exception
     */
    protected abstract SuspicionCaseDTO handleFindById(String id)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<SuspicionCaseDTO> findActive()
    {
        try
        {
            return this.handleFindActive();
        }
        catch (Throwable th)
        {
            throw new SuspicionCaseServiceException(
                "Error performing 'SuspicionCaseService.findActive()' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #findActive()}
     * @return List<SuspicionCaseDTO>
     * @throws Exception
     */
    protected abstract List<SuspicionCaseDTO> handleFindActive()
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<SuspicionCaseDTO> findByRecord(final String kycRecordId)
    {
        if (kycRecordId == null || kycRecordId.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.suspicion.SuspicionCaseService.findByRecord(String kycRecordId) - 'kycRecordId' can not be null or empty");
        }
        try
        {
            return this.handleFindByRecord(kycRecordId);
        }
        catch (Throwable th)
        {
            throw new SuspicionCaseServiceException(
                "Error performing 'SuspicionCaseService.findByRecord(String kycRecordId)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #findByRecord(String)}
     * @param kycRecordId String TODO: Model Documentation for
SuspicionCaseService.findByRecord(kycRecordId)
     * @return List<SuspicionCaseDTO>
     * @throws Exception
     */
    protected abstract List<SuspicionCaseDTO> handleFindByRecord(String kycRecordId)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    public SuspicionCaseDTO addNote(final String id, final String note, final String user)
    {
        if (id == null || id.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.suspicion.SuspicionCaseService.addNote(String id, String note, String user) - 'id' can not be null or empty");
        }
        if (note == null || note.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.suspicion.SuspicionCaseService.addNote(String id, String note, String user) - 'note' can not be null or empty");
        }
        if (user == null || user.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.suspicion.SuspicionCaseService.addNote(String id, String note, String user) - 'user' can not be null or empty");
        }
        try
        {
            return this.handleAddNote(id, note, user);
        }
        catch (Throwable th)
        {
            throw new SuspicionCaseServiceException(
                "Error performing 'SuspicionCaseService.addNote(String id, String note, String user)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #addNote(String, String, String)}
     * @param id String TODO: Model Documentation for SuspicionCaseService.addNote(id)
     * @param note String TODO: Model Documentation for SuspicionCaseService.addNote(note)
     * @param user String TODO: Model Documentation for SuspicionCaseService.addNote(user)
     * @return SuspicionCaseDTO
     * @throws Exception
     */
    protected abstract SuspicionCaseDTO handleAddNote(String id, String note, String user)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    public SuspicionCaseDTO escalate(final String id, final String user)
    {
        if (id == null || id.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.suspicion.SuspicionCaseService.escalate(String id, String user) - 'id' can not be null or empty");
        }
        if (user == null || user.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.suspicion.SuspicionCaseService.escalate(String id, String user) - 'user' can not be null or empty");
        }
        try
        {
            return this.handleEscalate(id, user);
        }
        catch (Throwable th)
        {
            throw new SuspicionCaseServiceException(
                "Error performing 'SuspicionCaseService.escalate(String id, String user)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #escalate(String, String)}
     * @param id String TODO: Model Documentation for SuspicionCaseService.escalate(id)
     * @param user String TODO: Model Documentation for SuspicionCaseService.escalate(user)
     * @return SuspicionCaseDTO
     * @throws Exception
     */
    protected abstract SuspicionCaseDTO handleEscalate(String id, String user)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    public SuspicionCaseDTO recordReport(final String id, final String fiaReference, final String user)
    {
        if (id == null || id.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.suspicion.SuspicionCaseService.recordReport(String id, String fiaReference, String user) - 'id' can not be null or empty");
        }
        if (fiaReference == null || fiaReference.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.suspicion.SuspicionCaseService.recordReport(String id, String fiaReference, String user) - 'fiaReference' can not be null or empty");
        }
        if (user == null || user.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.suspicion.SuspicionCaseService.recordReport(String id, String fiaReference, String user) - 'user' can not be null or empty");
        }
        try
        {
            return this.handleRecordReport(id, fiaReference, user);
        }
        catch (Throwable th)
        {
            throw new SuspicionCaseServiceException(
                "Error performing 'SuspicionCaseService.recordReport(String id, String fiaReference, String user)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #recordReport(String, String, String)}
     * @param id String TODO: Model Documentation for SuspicionCaseService.recordReport(id)
     * @param fiaReference String TODO: Model Documentation for
SuspicionCaseService.recordReport(fiaReference)
     * @param user String TODO: Model Documentation for SuspicionCaseService.recordReport(user)
     * @return SuspicionCaseDTO
     * @throws Exception
     */
    protected abstract SuspicionCaseDTO handleRecordReport(String id, String fiaReference, String user)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    public SuspicionCaseDTO close(final String id, final String outcome, final String user)
    {
        if (id == null || id.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.suspicion.SuspicionCaseService.close(String id, String outcome, String user) - 'id' can not be null or empty");
        }
        if (outcome == null || outcome.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.suspicion.SuspicionCaseService.close(String id, String outcome, String user) - 'outcome' can not be null or empty");
        }
        if (user == null || user.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.suspicion.SuspicionCaseService.close(String id, String outcome, String user) - 'user' can not be null or empty");
        }
        try
        {
            return this.handleClose(id, outcome, user);
        }
        catch (Throwable th)
        {
            throw new SuspicionCaseServiceException(
                "Error performing 'SuspicionCaseService.close(String id, String outcome, String user)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #close(String, String, String)}
     * @param id String TODO: Model Documentation for SuspicionCaseService.close(id)
     * @param outcome String TODO: Model Documentation for SuspicionCaseService.close(outcome)
     * @param user String TODO: Model Documentation for SuspicionCaseService.close(user)
     * @return SuspicionCaseDTO
     * @throws Exception
     */
    protected abstract SuspicionCaseDTO handleClose(String id, String outcome, String user)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    public String draftReport(final String id)
    {
        if (id == null || id.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.suspicion.SuspicionCaseService.draftReport(String id) - 'id' can not be null or empty");
        }
        try
        {
            return this.handleDraftReport(id);
        }
        catch (Throwable th)
        {
            throw new SuspicionCaseServiceException(
                "Error performing 'SuspicionCaseService.draftReport(String id)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #draftReport(String)}
     * @param id String TODO: Model Documentation for SuspicionCaseService.draftReport(id)
     * @return String
     * @throws Exception
     */
    protected abstract String handleDraftReport(String id)
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