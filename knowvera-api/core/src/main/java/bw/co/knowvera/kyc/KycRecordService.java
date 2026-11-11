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
import org.springframework.data.domain.Page;

/**
 * TODO: Model Documentation for bw.co.knowvera.kyc.KycRecordService
 */
public interface KycRecordService
{
    /**
     * TODO: Model Documentation for bw.co.knowvera.kyc.KycRecordService.findById
     * @param id TODO: Model Documentation for bw.co.knowvera.kyc.KycRecordService.findById(id)
     * @return KycRecordDTO
     */
    public KycRecordDTO findById(String id) ;

    /**
     * TODO: Model Documentation for bw.co.knowvera.kyc.KycRecordService.save
     * @param kycRecord TODO: Model Documentation for bw.co.knowvera.kyc.KycRecordService.save(kycRecord)
     * @return KycRecordDTO
     */
    public KycRecordDTO save(@Valid KycRecordDTO kycRecord) ;

    /**
     * TODO: Model Documentation for bw.co.knowvera.kyc.KycRecordService.remove
     * @param id TODO: Model Documentation for bw.co.knowvera.kyc.KycRecordService.remove(id)
     * @return boolean
     */
    public boolean remove(String id) ;

    /**
     * TODO: Model Documentation for bw.co.knowvera.kyc.KycRecordService.getAll
     * @return List<KycRecordListDTO>
     */
    public List<KycRecordListDTO> getAll() ;

    /**
     * TODO: Model Documentation for bw.co.knowvera.kyc.KycRecordService.search
     * @param criteria TODO: Model Documentation for bw.co.knowvera.kyc.KycRecordService.search(criteria)
     * @param orderings TODO: Model Documentation for bw.co.knowvera.kyc.KycRecordService.search(orderings)
     * @return List<KycRecordListDTO>
     */
    public List<KycRecordListDTO> search(@Valid KycRecordSearchCriteria criteria, @Valid Set<PropertySearchOrder> orderings) ;

    /**
     * TODO: Model Documentation for bw.co.knowvera.kyc.KycRecordService.getAll
     * @param pageNumber TODO: Model Documentation for bw.co.knowvera.kyc.KycRecordService.getAll(pageNumber)
     * @param pageSize TODO: Model Documentation for bw.co.knowvera.kyc.KycRecordService.getAll(pageSize)
     * @return Page<KycRecordListDTO>
     */
    public Page<KycRecordListDTO> getAll(Integer pageNumber, Integer pageSize) ;

    /**
     * TODO: Model Documentation for bw.co.knowvera.kyc.KycRecordService.search
     * @param criteria TODO: Model Documentation for bw.co.knowvera.kyc.KycRecordService.search(criteria)
     * @return Page<KycRecordListDTO>
     */
    public Page<KycRecordListDTO> search(SearchObject<KycRecordSearchCriteria> criteria) ;

    /**
     * TODO: Model Documentation for bw.co.knowvera.kyc.KycRecordService.findByIndividual
     * @param individualId TODO: Model Documentation for bw.co.knowvera.kyc.KycRecordService.findByIndividual(individualId)
     * @return List<KycRecordListDTO>
     */
    public List<KycRecordListDTO> findByIndividual(String individualId) ;

    /**
     * TODO: Model Documentation for bw.co.knowvera.kyc.KycRecordService.findByIdentityNo
     * @param identityNo TODO: Model Documentation for bw.co.knowvera.kyc.KycRecordService.findByIdentityNo(identityNo)
     * @return List<KycRecordListDTO>
     */
    public List<KycRecordListDTO> findByIdentityNo(String identityNo) ;

    /**
     * TODO: Model Documentation for bw.co.knowvera.kyc.KycRecordService.findByOrganisation
     * @param organisationId TODO: Model Documentation for bw.co.knowvera.kyc.KycRecordService.findByOrganisation(organisationId)
     * @return List<KycRecordListDTO>
     */
    public List<KycRecordListDTO> findByOrganisation(String organisationId) ;

    /**
     * TODO: Model Documentation for bw.co.knowvera.kyc.KycRecordService.findByIndividual
     * @param individualId TODO: Model Documentation for bw.co.knowvera.kyc.KycRecordService.findByIndividual(individualId)
     * @param pageNumber TODO: Model Documentation for bw.co.knowvera.kyc.KycRecordService.findByIndividual(pageNumber)
     * @param pageSize TODO: Model Documentation for bw.co.knowvera.kyc.KycRecordService.findByIndividual(pageSize)
     * @return Page<KycRecordListDTO>
     */
    public Page<KycRecordListDTO> findByIndividual(String individualId, Integer pageNumber, Integer pageSize) ;

    /**
     * TODO: Model Documentation for bw.co.knowvera.kyc.KycRecordService.findByIdentityNo
     * @param identityNo TODO: Model Documentation for bw.co.knowvera.kyc.KycRecordService.findByIdentityNo(identityNo)
     * @param pageNumber TODO: Model Documentation for bw.co.knowvera.kyc.KycRecordService.findByIdentityNo(pageNumber)
     * @param pageSize TODO: Model Documentation for bw.co.knowvera.kyc.KycRecordService.findByIdentityNo(pageSize)
     * @return Page<KycRecordListDTO>
     */
    public Page<KycRecordListDTO> findByIdentityNo(String identityNo, Integer pageNumber, Integer pageSize) ;

    /**
     * TODO: Model Documentation for bw.co.knowvera.kyc.KycRecordService.findByOrganisation
     * @param organisationId TODO: Model Documentation for bw.co.knowvera.kyc.KycRecordService.findByOrganisation(organisationId)
     * @param pageNumber TODO: Model Documentation for bw.co.knowvera.kyc.KycRecordService.findByOrganisation(pageNumber)
     * @param pageSize TODO: Model Documentation for bw.co.knowvera.kyc.KycRecordService.findByOrganisation(pageSize)
     * @return Page<KycRecordListDTO>
     */
    public Page<KycRecordListDTO> findByOrganisation(String organisationId, Integer pageNumber, Integer pageSize) ;

    /**
     * TODO: Model Documentation for bw.co.knowvera.kyc.KycRecordService.createTargetRecord
     * @param targetId TODO: Model Documentation for bw.co.knowvera.kyc.KycRecordService.createTargetRecord(targetId)
     * @param target TODO: Model Documentation for bw.co.knowvera.kyc.KycRecordService.createTargetRecord(target)
     * @param user TODO: Model Documentation for bw.co.knowvera.kyc.KycRecordService.createTargetRecord(user)
     * @return KycRecordDTO
     */
    public KycRecordDTO createTargetRecord(String targetId, TargetEntity target, String user) ;

    /**
     * TODO: Model Documentation for bw.co.knowvera.kyc.KycRecordService.confirmOwnership
     * @param kycRecordId TODO: Model Documentation for bw.co.knowvera.kyc.KycRecordService.confirmOwnership(kycRecordId)
     * @param user TODO: Model Documentation for bw.co.knowvera.kyc.KycRecordService.confirmOwnership(user)
     * @return boolean
     */
    public boolean confirmOwnership(String kycRecordId, @Valid UserDTO user) ;

    /**
     * TODO: Model Documentation for bw.co.knowvera.kyc.KycRecordService.findLatestValidForOwner
     * @param ownerId TODO: Model Documentation for bw.co.knowvera.kyc.KycRecordService.findLatestValidForOwner(ownerId)
     * @param ownerType TODO: Model Documentation for bw.co.knowvera.kyc.KycRecordService.findLatestValidForOwner(ownerType)
     * @param today TODO: Model Documentation for bw.co.knowvera.kyc.KycRecordService.findLatestValidForOwner(today)
     * @return KycRecordDTO
     */
    public KycRecordDTO findLatestValidForOwner(String ownerId, TargetEntity ownerType, LocalDate today) ;

    /**
     * TODO: Model Documentation for bw.co.knowvera.kyc.KycRecordService.createNew
     * @param record TODO: Model Documentation for bw.co.knowvera.kyc.KycRecordService.createNew(record)
     * @param user TODO: Model Documentation for bw.co.knowvera.kyc.KycRecordService.createNew(user)
     * @return KycRecordDTO
     */
    public KycRecordDTO createNew(@Valid KycRecordDTO record, String user) ;

    /**
     * TODO: Model Documentation for bw.co.knowvera.kyc.KycRecordService.removeRecordFile
     * @param id TODO: Model Documentation for bw.co.knowvera.kyc.KycRecordService.removeRecordFile(id)
     * @param documentId TODO: Model Documentation for bw.co.knowvera.kyc.KycRecordService.removeRecordFile(documentId)
     * @return KycRecordDTO
     */
    public KycRecordDTO removeRecordFile(String id, String documentId) ;

    /**
     * TODO: Model Documentation for bw.co.knowvera.kyc.KycRecordService.updateRecordFiles
     * @param id TODO: Model Documentation for bw.co.knowvera.kyc.KycRecordService.updateRecordFiles(id)
     * @param documents TODO: Model Documentation for bw.co.knowvera.kyc.KycRecordService.updateRecordFiles(documents)
     * @return KycRecordDTO
     */
    public KycRecordDTO updateRecordFiles(String id, @Valid List<DocumentDTO> documents) ;

    /**
     * TODO: Model Documentation for bw.co.knowvera.kyc.KycRecordService.findSummaryById
     * @param id TODO: Model Documentation for bw.co.knowvera.kyc.KycRecordService.findSummaryById(id)
     * @param organisationId TODO: Model Documentation for bw.co.knowvera.kyc.KycRecordService.findSummaryById(organisationId)
     * @return KycRecordSummary
     */
    public KycRecordSummary findSummaryById(String id, String organisationId) ;

    /**
     * TODO: Model Documentation for bw.co.knowvera.kyc.KycRecordService.runVerification
     * @param id TODO: Model Documentation for bw.co.knowvera.kyc.KycRecordService.runVerification(id)
     * @param user TODO: Model Documentation for bw.co.knowvera.kyc.KycRecordService.runVerification(user)
     * @return KycRecordDTO
     */
    public KycRecordDTO runVerification(String id, String user) ;

    /**
     * This method generates a KYC report for the given KYC record ID. It retrieves the KYC record,
     * checks the verification status of the associated documents, and creates report sections
     * accordingly. The report sections are then saved along with the updated KYC record status.
     * @param id TODO: Model Documentation for bw.co.knowvera.kyc.KycRecordService.generateKycReport(id)
     * @param user TODO: Model Documentation for bw.co.knowvera.kyc.KycRecordService.generateKycReport(user)
     * @return KycRecordDTO
     */
    public KycRecordDTO generateKycReport(String id, String user) ;

    /**
     * TODO: Model Documentation for bw.co.knowvera.kyc.KycRecordService.updateStatus
     * @param id TODO: Model Documentation for bw.co.knowvera.kyc.KycRecordService.updateStatus(id)
     * @param kycStatus TODO: Model Documentation for bw.co.knowvera.kyc.KycRecordService.updateStatus(kycStatus)
     * @param user TODO: Model Documentation for bw.co.knowvera.kyc.KycRecordService.updateStatus(user)
     * @return KycRecordDTO
     */
    public KycRecordDTO updateStatus(String id, KycComplianceStatus kycStatus, String user) ;

    /**
     * TODO: Model Documentation for bw.co.knowvera.kyc.KycRecordService.findOrganisationSummaries
     * @param organisationId TODO: Model Documentation for
bw.co.knowvera.kyc.KycRecordService.findOrganisationSummaries(organisationId)
     * @param forIndividual TODO: Model Documentation for
bw.co.knowvera.kyc.KycRecordService.findOrganisationSummaries(forIndividual)
     * @return List<KycRecordSummary>
     */
    public List<KycRecordSummary> findOrganisationSummaries(String organisationId, Boolean forIndividual) ;

    /**
     * TODO: Model Documentation for bw.co.knowvera.kyc.KycRecordService.findOrganisationSummaries
     * @param organisationId TODO: Model Documentation for
bw.co.knowvera.kyc.KycRecordService.findOrganisationSummaries(organisationId)
     * @param forIndividual TODO: Model Documentation for
bw.co.knowvera.kyc.KycRecordService.findOrganisationSummaries(forIndividual)
     * @param pageSize TODO: Model Documentation for
bw.co.knowvera.kyc.KycRecordService.findOrganisationSummaries(pageSize)
     * @param pageNumber TODO: Model Documentation for
bw.co.knowvera.kyc.KycRecordService.findOrganisationSummaries(pageNumber)
     * @return Page<KycRecordSummary>
     */
    public Page<KycRecordSummary> findOrganisationSummaries(String organisationId, Boolean forIndividual, Integer pageSize, Integer pageNumber) ;

    /**
     * TODO: Model Documentation for bw.co.knowvera.kyc.KycRecordService.searchSummaries
     * @param criteria TODO: Model Documentation for bw.co.knowvera.kyc.KycRecordService.searchSummaries(criteria)
     * @return Page<KycRecordSummary>
     */
    public Page<KycRecordSummary> searchSummaries(SearchObject<KycRecordSearchCriteria> criteria) ;

    /**
     * TODO: Model Documentation for bw.co.knowvera.kyc.KycRecordService.searchSummaries
     * @param criteria TODO: Model Documentation for bw.co.knowvera.kyc.KycRecordService.searchSummaries(criteria)
     * @param orderings TODO: Model Documentation for bw.co.knowvera.kyc.KycRecordService.searchSummaries(orderings)
     * @return List<KycRecordSummary>
     */
    public List<KycRecordSummary> searchSummaries(@Valid KycRecordSearchCriteria criteria, @Valid Set<PropertySearchOrder> orderings) ;

}