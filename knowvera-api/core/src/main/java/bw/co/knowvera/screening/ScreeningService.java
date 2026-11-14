package bw.co.knowvera.screening;

import java.util.List;

/**
 * TODO: Model Documentation for bw.co.knowvera.screening.ScreeningService
 */
public interface ScreeningService
{
    /**
     * TODO: Model Documentation for bw.co.knowvera.screening.ScreeningService.updateSanctionsList
     * @param user TODO: Model Documentation for bw.co.knowvera.screening.ScreeningService.updateSanctionsList(user)
     * @return ScreeningListStatus
     */
    public ScreeningListStatus updateSanctionsList(String user) ;

    /**
     * TODO: Model Documentation for bw.co.knowvera.screening.ScreeningService.getListStatus
     * @return List<ScreeningListStatus>
     */
    public List<ScreeningListStatus> getListStatus() ;

    /**
     * TODO: Model Documentation for bw.co.knowvera.screening.ScreeningService.screenRecord
     * @param kycRecordId TODO: Model Documentation for bw.co.knowvera.screening.ScreeningService.screenRecord(kycRecordId)
     * @return List<ScreeningHitDTO>
     */
    public List<ScreeningHitDTO> screenRecord(String kycRecordId) ;

    /**
     * TODO: Model Documentation for bw.co.knowvera.screening.ScreeningService.findHitsByRecord
     * @param kycRecordId TODO: Model Documentation for
bw.co.knowvera.screening.ScreeningService.findHitsByRecord(kycRecordId)
     * @return List<ScreeningHitDTO>
     */
    public List<ScreeningHitDTO> findHitsByRecord(String kycRecordId) ;

    /**
     * TODO: Model Documentation for bw.co.knowvera.screening.ScreeningService.findPendingHits
     * @return List<ScreeningHitDTO>
     */
    public List<ScreeningHitDTO> findPendingHits() ;

    /**
     * TODO: Model Documentation for bw.co.knowvera.screening.ScreeningService.reviewHit
     * @param id TODO: Model Documentation for bw.co.knowvera.screening.ScreeningService.reviewHit(id)
     * @param status TODO: Model Documentation for bw.co.knowvera.screening.ScreeningService.reviewHit(status)
     * @param notes TODO: Model Documentation for bw.co.knowvera.screening.ScreeningService.reviewHit(notes)
     * @param user TODO: Model Documentation for bw.co.knowvera.screening.ScreeningService.reviewHit(user)
     * @return ScreeningHitDTO
     */
    public ScreeningHitDTO reviewHit(String id, ScreeningHitStatus status, String notes, String user) ;

}