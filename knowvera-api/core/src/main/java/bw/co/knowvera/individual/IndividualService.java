package bw.co.knowvera.individual;

import bw.co.knowvera.PropertySearchOrder;
import bw.co.knowvera.SearchObject;
import bw.co.knowvera.kyc.KycComplianceStatus;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Set;
import org.springframework.data.domain.Page;

/**
 * TODO: Model Documentation for bw.co.knowvera.individual.IndividualService
 */
public interface IndividualService
{
    /**
     * TODO: Model Documentation for bw.co.knowvera.individual.IndividualService.findById
     * @param id TODO: Model Documentation for bw.co.knowvera.individual.IndividualService.findById(id)
     * @return IndividualDTO
     */
    public IndividualDTO findById(String id) ;

    /**
     * TODO: Model Documentation for bw.co.knowvera.individual.IndividualService.getAll
     * @return List<IndividualListDTO>
     */
    public List<IndividualListDTO> getAll() ;

    /**
     * TODO: Model Documentation for bw.co.knowvera.individual.IndividualService.getAll
     * @param pageNumber TODO: Model Documentation for bw.co.knowvera.individual.IndividualService.getAll(pageNumber)
     * @param pageSize TODO: Model Documentation for bw.co.knowvera.individual.IndividualService.getAll(pageSize)
     * @return Page<IndividualListDTO>
     */
    public Page<IndividualListDTO> getAll(Integer pageNumber, Integer pageSize) ;

    /**
     * TODO: Model Documentation for bw.co.knowvera.individual.IndividualService.remove
     * @param id TODO: Model Documentation for bw.co.knowvera.individual.IndividualService.remove(id)
     * @return boolean
     */
    public boolean remove(String id) ;

    /**
     * TODO: Model Documentation for bw.co.knowvera.individual.IndividualService.save
     * @param individual TODO: Model Documentation for bw.co.knowvera.individual.IndividualService.save(individual)
     * @return IndividualDTO
     */
    public IndividualDTO save(@Valid IndividualDTO individual) ;

    /**
     * TODO: Model Documentation for bw.co.knowvera.individual.IndividualService.search
     * @param criteria TODO: Model Documentation for bw.co.knowvera.individual.IndividualService.search(criteria)
     * @param orderings TODO: Model Documentation for bw.co.knowvera.individual.IndividualService.search(orderings)
     * @return List<IndividualListDTO>
     */
    public List<IndividualListDTO> search(@Valid IndividualSearchCriteria criteria, @Valid Set<PropertySearchOrder> orderings) ;

    /**
     * TODO: Model Documentation for bw.co.knowvera.individual.IndividualService.search
     * @param criteria TODO: Model Documentation for bw.co.knowvera.individual.IndividualService.search(criteria)
     * @return Page<IndividualListDTO>
     */
    public Page<IndividualListDTO> search(SearchObject<IndividualSearchCriteria> criteria) ;

    /**
     * TODO: Model Documentation for
     * bw.co.knowvera.individual.IndividualService.getOrganisationClients
     * @param organisationId TODO: Model Documentation for
bw.co.knowvera.individual.IndividualService.getOrganisationClients(organisationId)
     * @return List<IndividualListDTO>
     */
    public List<IndividualListDTO> getOrganisationClients(String organisationId) ;

    /**
     * TODO: Model Documentation for
     * bw.co.knowvera.individual.IndividualService.getOrganisationClients
     * @param organisationId TODO: Model Documentation for
bw.co.knowvera.individual.IndividualService.getOrganisationClients(organisationId)
     * @param pageNumber TODO: Model Documentation for
bw.co.knowvera.individual.IndividualService.getOrganisationClients(pageNumber)
     * @param pageSize TODO: Model Documentation for
bw.co.knowvera.individual.IndividualService.getOrganisationClients(pageSize)
     * @return Page<IndividualListDTO>
     */
    public Page<IndividualListDTO> getOrganisationClients(String organisationId, Integer pageNumber, Integer pageSize) ;

    /**
     * TODO: Model Documentation for
     * bw.co.knowvera.individual.IndividualService.findByIdentityNoAndIdentityType
     * @param identityNo TODO: Model Documentation for
bw.co.knowvera.individual.IndividualService.findByIdentityNoAndIdentityType(identityNo)
     * @param identityType TODO: Model Documentation for
bw.co.knowvera.individual.IndividualService.findByIdentityNoAndIdentityType(identityType)
     * @return IndividualDTO
     */
    public IndividualDTO findByIdentityNoAndIdentityType(String identityNo, IndividualIdentityType identityType) ;

    /**
     * TODO: Model Documentation for bw.co.knowvera.individual.IndividualService.countByPepStatus
     * @param pepStatus TODO: Model Documentation for
bw.co.knowvera.individual.IndividualService.countByPepStatus(pepStatus)
     * @return Long
     */
    public Long countByPepStatus(PepStatus pepStatus) ;

    /**
     * TODO: Model Documentation for bw.co.knowvera.individual.IndividualService.count
     * @return Long
     */
    public Long count() ;

    /**
     * TODO: Model Documentation for bw.co.knowvera.individual.IndividualService.countByKycStatus
     * @param kycStatus TODO: Model Documentation for
bw.co.knowvera.individual.IndividualService.countByKycStatus(kycStatus)
     * @return Long
     */
    public Long countByKycStatus(KycComplianceStatus kycStatus) ;

    /**
     * TODO: Model Documentation for
     * bw.co.knowvera.individual.IndividualService.countByEmploymentStatus
     * @param employmentStatus TODO: Model Documentation for
bw.co.knowvera.individual.IndividualService.countByEmploymentStatus(employmentStatus)
     * @return Long
     */
    public Long countByEmploymentStatus(EmploymentStatus employmentStatus) ;

    /**
     * TODO: Model Documentation for bw.co.knowvera.individual.IndividualService.countBySex
     * @param sex TODO: Model Documentation for bw.co.knowvera.individual.IndividualService.countBySex(sex)
     * @return Long
     */
    public Long countBySex(Sex sex) ;

    /**
     * TODO: Model Documentation for
     * bw.co.knowvera.individual.IndividualService.loadRequestIndividual
     * @param requestId TODO: Model Documentation for
bw.co.knowvera.individual.IndividualService.loadRequestIndividual(requestId)
     * @param identityConfirmationToken TODO: Model Documentation for
bw.co.knowvera.individual.IndividualService.loadRequestIndividual(identityConfirmationToken)
     * @param identityNo TODO: Model Documentation for
bw.co.knowvera.individual.IndividualService.loadRequestIndividual(identityNo)
     * @return IndividualDTO
     */
    public IndividualDTO loadRequestIndividual(String requestId, String identityConfirmationToken, String identityNo) ;

    /**
     * TODO: Model Documentation for bw.co.knowvera.individual.IndividualService.findByUserId
     * @param userId TODO: Model Documentation for bw.co.knowvera.individual.IndividualService.findByUserId(userId)
     * @return IndividualDTO
     */
    public IndividualDTO findByUserId(String userId) ;

    /**
     * TODO: Model Documentation for bw.co.knowvera.individual.IndividualService.verifyIndividual
     * @param id TODO: Model Documentation for bw.co.knowvera.individual.IndividualService.verifyIndividual(id)
     * @param user TODO: Model Documentation for bw.co.knowvera.individual.IndividualService.verifyIndividual(user)
     * @return IndividualDTO
     */
    public IndividualDTO verifyIndividual(String id, String user) ;

}