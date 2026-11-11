package bw.co.knowvera.suspicion;

import jakarta.validation.Valid;
import java.util.List;

/**
 * TODO: Model Documentation for bw.co.knowvera.suspicion.SuspicionCaseService
 */
public interface SuspicionCaseService
{
    /**
     * TODO: Model Documentation for bw.co.knowvera.suspicion.SuspicionCaseService.raise
     * @param suspicionCase TODO: Model Documentation for bw.co.knowvera.suspicion.SuspicionCaseService.raise(suspicionCase)
     * @param user TODO: Model Documentation for bw.co.knowvera.suspicion.SuspicionCaseService.raise(user)
     * @return SuspicionCaseDTO
     */
    public SuspicionCaseDTO raise(@Valid SuspicionCaseDTO suspicionCase, String user) ;

    /**
     * TODO: Model Documentation for bw.co.knowvera.suspicion.SuspicionCaseService.findById
     * @param id TODO: Model Documentation for bw.co.knowvera.suspicion.SuspicionCaseService.findById(id)
     * @return SuspicionCaseDTO
     */
    public SuspicionCaseDTO findById(String id) ;

    /**
     * TODO: Model Documentation for bw.co.knowvera.suspicion.SuspicionCaseService.findActive
     * @return List<SuspicionCaseDTO>
     */
    public List<SuspicionCaseDTO> findActive() ;

    /**
     * TODO: Model Documentation for bw.co.knowvera.suspicion.SuspicionCaseService.findByRecord
     * @param kycRecordId TODO: Model Documentation for
bw.co.knowvera.suspicion.SuspicionCaseService.findByRecord(kycRecordId)
     * @return List<SuspicionCaseDTO>
     */
    public List<SuspicionCaseDTO> findByRecord(String kycRecordId) ;

    /**
     * TODO: Model Documentation for bw.co.knowvera.suspicion.SuspicionCaseService.addNote
     * @param id TODO: Model Documentation for bw.co.knowvera.suspicion.SuspicionCaseService.addNote(id)
     * @param note TODO: Model Documentation for bw.co.knowvera.suspicion.SuspicionCaseService.addNote(note)
     * @param user TODO: Model Documentation for bw.co.knowvera.suspicion.SuspicionCaseService.addNote(user)
     * @return SuspicionCaseDTO
     */
    public SuspicionCaseDTO addNote(String id, String note, String user) ;

    /**
     * TODO: Model Documentation for bw.co.knowvera.suspicion.SuspicionCaseService.escalate
     * @param id TODO: Model Documentation for bw.co.knowvera.suspicion.SuspicionCaseService.escalate(id)
     * @param user TODO: Model Documentation for bw.co.knowvera.suspicion.SuspicionCaseService.escalate(user)
     * @return SuspicionCaseDTO
     */
    public SuspicionCaseDTO escalate(String id, String user) ;

    /**
     * TODO: Model Documentation for bw.co.knowvera.suspicion.SuspicionCaseService.recordReport
     * @param id TODO: Model Documentation for bw.co.knowvera.suspicion.SuspicionCaseService.recordReport(id)
     * @param fiaReference TODO: Model Documentation for
bw.co.knowvera.suspicion.SuspicionCaseService.recordReport(fiaReference)
     * @param user TODO: Model Documentation for bw.co.knowvera.suspicion.SuspicionCaseService.recordReport(user)
     * @return SuspicionCaseDTO
     */
    public SuspicionCaseDTO recordReport(String id, String fiaReference, String user) ;

    /**
     * TODO: Model Documentation for bw.co.knowvera.suspicion.SuspicionCaseService.close
     * @param id TODO: Model Documentation for bw.co.knowvera.suspicion.SuspicionCaseService.close(id)
     * @param outcome TODO: Model Documentation for bw.co.knowvera.suspicion.SuspicionCaseService.close(outcome)
     * @param user TODO: Model Documentation for bw.co.knowvera.suspicion.SuspicionCaseService.close(user)
     * @return SuspicionCaseDTO
     */
    public SuspicionCaseDTO close(String id, String outcome, String user) ;

    /**
     * TODO: Model Documentation for bw.co.knowvera.suspicion.SuspicionCaseService.draftReport
     * @param id TODO: Model Documentation for bw.co.knowvera.suspicion.SuspicionCaseService.draftReport(id)
     * @return String
     */
    public String draftReport(String id) ;

}