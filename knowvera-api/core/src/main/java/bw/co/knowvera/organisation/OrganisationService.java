package bw.co.knowvera.organisation;

import bw.co.knowvera.GeneralStatus;
import bw.co.knowvera.PropertySearchOrder;
import bw.co.knowvera.SearchObject;
import bw.co.knowvera.kyc.KycComplianceStatus;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Set;
import org.springframework.data.domain.Page;

/**
 * TODO: Model Documentation for bw.co.knowvera.organisation.OrganisationService
 */
public interface OrganisationService
{
    /**
     * TODO: Model Documentation for bw.co.knowvera.organisation.OrganisationService.findById
     * @param id TODO: Model Documentation for bw.co.knowvera.organisation.OrganisationService.findById(id)
     * @return OrganisationDTO
     */
    public OrganisationDTO findById(String id) ;

    /**
     * TODO: Model Documentation for bw.co.knowvera.organisation.OrganisationService.getAll
     * @return List<OrganisationListDTO>
     */
    public List<OrganisationListDTO> getAll() ;

    /**
     * TODO: Model Documentation for bw.co.knowvera.organisation.OrganisationService.getAll
     * @param pageNumber TODO: Model Documentation for bw.co.knowvera.organisation.OrganisationService.getAll(pageNumber)
     * @param pageSize TODO: Model Documentation for bw.co.knowvera.organisation.OrganisationService.getAll(pageSize)
     * @return Page<OrganisationListDTO>
     */
    public Page<OrganisationListDTO> getAll(Integer pageNumber, Integer pageSize) ;

    /**
     * TODO: Model Documentation for bw.co.knowvera.organisation.OrganisationService.remove
     * @param id TODO: Model Documentation for bw.co.knowvera.organisation.OrganisationService.remove(id)
     * @return boolean
     */
    public boolean remove(String id) ;

    /**
     * TODO: Model Documentation for bw.co.knowvera.organisation.OrganisationService.save
     * @param organisation TODO: Model Documentation for bw.co.knowvera.organisation.OrganisationService.save(organisation)
     * @return OrganisationDTO
     */
    public OrganisationDTO save(@Valid OrganisationDTO organisation) ;

    /**
     * TODO: Model Documentation for bw.co.knowvera.organisation.OrganisationService.search
     * @param criteria TODO: Model Documentation for bw.co.knowvera.organisation.OrganisationService.search(criteria)
     * @param orderings TODO: Model Documentation for bw.co.knowvera.organisation.OrganisationService.search(orderings)
     * @return List<OrganisationListDTO>
     */
    public List<OrganisationListDTO> search(@Valid OrganisationSearchCriteria criteria, @Valid Set<PropertySearchOrder> orderings) ;

    /**
     * TODO: Model Documentation for bw.co.knowvera.organisation.OrganisationService.search
     * @param criteria TODO: Model Documentation for bw.co.knowvera.organisation.OrganisationService.search(criteria)
     * @return Page<OrganisationListDTO>
     */
    public Page<OrganisationListDTO> search(SearchObject<OrganisationSearchCriteria> criteria) ;

    /**
     * TODO: Model Documentation for
     * bw.co.knowvera.organisation.OrganisationService.countByKycStatus
     * @param kycStatus TODO: Model Documentation for
bw.co.knowvera.organisation.OrganisationService.countByKycStatus(kycStatus)
     * @return Long
     */
    public Long countByKycStatus(KycComplianceStatus kycStatus) ;

    /**
     * TODO: Model Documentation for bw.co.knowvera.organisation.OrganisationService.countByStatus
     * @param status TODO: Model Documentation for bw.co.knowvera.organisation.OrganisationService.countByStatus(status)
     * @return Long
     */
    public Long countByStatus(GeneralStatus status) ;

    /**
     * TODO: Model Documentation for
     * bw.co.knowvera.organisation.OrganisationService.countByIsClientFalse
     * @return Long
     */
    public Long countByIsClientFalse() ;

    /**
     * TODO: Model Documentation for
     * bw.co.knowvera.organisation.OrganisationService.countByIsClientTrue
     * @return Long
     */
    public Long countByIsClientTrue() ;

    /**
     * TODO: Model Documentation for bw.co.knowvera.organisation.OrganisationService.count
     * @return Long
     */
    public Long count() ;

    /**
     * TODO: Model Documentation for
     * bw.co.knowvera.organisation.OrganisationService.loadRequestOrganisation
     * @param requestId TODO: Model Documentation for
bw.co.knowvera.organisation.OrganisationService.loadRequestOrganisation(requestId)
     * @param identityConfirmationToken TODO: Model Documentation for
bw.co.knowvera.organisation.OrganisationService.loadRequestOrganisation(identityConfirmationToken)
     * @param registrationNo TODO: Model Documentation for
bw.co.knowvera.organisation.OrganisationService.loadRequestOrganisation(registrationNo)
     * @return OrganisationDTO
     */
    public OrganisationDTO loadRequestOrganisation(String requestId, String identityConfirmationToken, String registrationNo) ;

    /**
     * TODO: Model Documentation for bw.co.knowvera.organisation.OrganisationService.findByCode
     * @param code TODO: Model Documentation for bw.co.knowvera.organisation.OrganisationService.findByCode(code)
     * @return OrganisationDTO
     */
    public OrganisationDTO findByCode(String code) ;

    /**
     * TODO: Model Documentation for bw.co.knowvera.organisation.OrganisationService.findByName
     * @param name TODO: Model Documentation for bw.co.knowvera.organisation.OrganisationService.findByName(name)
     * @return OrganisationDTO
     */
    public OrganisationDTO findByName(String name) ;

    /**
     * TODO: Model Documentation for
     * bw.co.knowvera.organisation.OrganisationService.findByRegistrationNo
     * @param registrationNo TODO: Model Documentation for
bw.co.knowvera.organisation.OrganisationService.findByRegistrationNo(registrationNo)
     * @return OrganisationDTO
     */
    public OrganisationDTO findByRegistrationNo(String registrationNo) ;

    /**
     * TODO: Model Documentation for
     * bw.co.knowvera.organisation.OrganisationService.verifyOrganisation
     * @param id TODO: Model Documentation for bw.co.knowvera.organisation.OrganisationService.verifyOrganisation(id)
     * @param user TODO: Model Documentation for
bw.co.knowvera.organisation.OrganisationService.verifyOrganisation(user)
     * @return OrganisationDTO
     */
    public OrganisationDTO verifyOrganisation(String id, String user) ;

    /**
     * TODO: Model Documentation for
     * bw.co.knowvera.organisation.OrganisationService.findListOrganisationById
     * @param id TODO: Model Documentation for
bw.co.knowvera.organisation.OrganisationService.findListOrganisationById(id)
     * @return OrganisationListDTO
     */
    public OrganisationListDTO findListOrganisationById(String id) ;

    /**
     * TODO: Model Documentation for
     * bw.co.knowvera.organisation.OrganisationService.findListOrganisationByKeycloakId
     * @param keycloakId TODO: Model Documentation for
bw.co.knowvera.organisation.OrganisationService.findListOrganisationByKeycloakId(keycloakId)
     * @return OrganisationListDTO
     */
    public OrganisationListDTO findListOrganisationByKeycloakId(String keycloakId) ;

}