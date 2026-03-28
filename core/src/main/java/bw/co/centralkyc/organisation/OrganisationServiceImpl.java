// license-header java merge-point
/**
 * This is only generated once! It will never be overwritten.
 * You can (and have to!) safely modify it by hand.
 * TEMPLATE:    SpringServiceImpl.vsl in andromda-spring cartridge
 * MODEL CLASS: AndroMDAModel::backend::bw.co.centralkyc::organisation::OrganisationService
 * STEREOTYPE:  Service
 */
package bw.co.centralkyc.organisation;

import java.util.Collection;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import bw.co.centralkyc.GeneralStatus;
import bw.co.centralkyc.PropertySearchOrder;
import bw.co.centralkyc.SearchObject;
import bw.co.centralkyc.individual.IndividualDTO;
import bw.co.centralkyc.kyc.KycComplianceStatus;
import bw.co.centralkyc.organisation.client.ClientRequestRepository;

/**
 * @see bw.co.centralkyc.organisation.OrganisationService
 */
@Service("organisationService")
public class OrganisationServiceImpl
        extends OrganisationServiceBase {

    private final OrganisationRepository organisationRepository;
    private final ClientRequestRepository clientRequestRepository;
    private final PasswordEncoder passwordEncoder;


    public OrganisationServiceImpl(OrganisationDao organisationDao, OrganisationRepository organisationRepository, OrganisationMapper organisationMapper,
            ClientRequestRepository clientRequestRepository, PasswordEncoder passwordEncoder,
            MessageSource messageSource) {
        super(organisationDao, organisationRepository, organisationMapper, messageSource);
        this.organisationRepository = organisationRepository;
        // TODO Auto-generated constructor stub

        this.clientRequestRepository = clientRequestRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * @see bw.co.centralkyc.organisation.OrganisationService#findById(String)
     */
    @Override
    protected OrganisationDTO handleFindById(String id)
            throws Exception {

        Organisation org = organisationRepository.getReferenceById(UUID.fromString(id));
        org = organisationRepository.save(org);

        return organisationDao.toOrganisationDTO(org);
    }

    /**
     * @see bw.co.centralkyc.organisation.OrganisationService#getAll()
     */
    @Override
    protected Collection<OrganisationListDTO> handleGetAll()
            throws Exception {

        Collection<Organisation> orgs = organisationRepository.findAll();
        return organisationDao.toOrganisationListDTOCollection(orgs);

    }

    /**
     * @see bw.co.centralkyc.organisation.OrganisationService#getAll(Integer,
     *      Integer)
     */
    @Override
    protected Page<OrganisationListDTO> handleGetAll(Integer pageNumber, Integer pageSize)
            throws Exception {

        return null;
    }

    /**
     * @see bw.co.centralkyc.organisation.OrganisationService#remove(String)
     */
    @Override
    protected boolean handleRemove(String id)
            throws Exception {

        if(organisationRepository.existsById(UUID.fromString(id))) {
            organisationRepository.deleteById(UUID.fromString(id));
            return true;
        }   

        return false;
    }

    /**
     * @see bw.co.centralkyc.organisation.OrganisationService#save(OrganisationDTO)
     */
    @Override
    protected OrganisationDTO handleSave(OrganisationDTO organisation)
            throws Exception {

        Organisation orgEntity = organisationDao.organisationDTOToEntity(organisation);
        orgEntity = organisationRepository.save(orgEntity); 

        return organisationDao.toOrganisationDTO(orgEntity);
    }

    private Specification<Organisation> createSpecification(OrganisationSearchCriteria criteria) {

        Specification<Organisation> spec = ((root, query, builder) -> builder.conjunction());

        if (criteria == null) {
            return spec;
        }

        if (StringUtils.isNotBlank(criteria.getId())) {

            Specification<Organisation> tmp = ((root, query, builder) -> builder.equal(root.get("id"),
                    criteria.getId()));
            spec = spec == null ? tmp : spec.and(tmp);

        }

        if (StringUtils.isNotBlank(criteria.getName())) {
            Specification<Organisation> tmp = ((root, query, builder) -> builder.like(builder.upper(root.get("name")),
                    "%" + criteria.getName().toUpperCase() + "%"));
            spec = spec == null ? tmp : spec.and(tmp);
        }

        if (StringUtils.isNotBlank(criteria.getRegistrationNo())) {
            Specification<Organisation> tmp = ((root, query, builder) -> builder.equal(root.get("registrationNo"),
                    criteria.getRegistrationNo()));
            spec = spec == null ? tmp : spec.and(tmp);
        }

        if (StringUtils.isNotBlank(criteria.getContactEmailAddress())) {
            Specification<Organisation> tmp = ((root, query, builder) -> builder.like(
                    builder.upper(root.get("contactEmailAddress")),
                    "%" + criteria.getContactEmailAddress().toUpperCase() + "%"));
            spec = spec == null ? tmp : spec.and(tmp);
        }

        if (criteria.getStatus() != null) {
            Specification<Organisation> tmp = ((root, query, builder) -> builder.equal(root.get("status"),
                    criteria.getStatus()));
            spec = spec == null ? tmp : spec.and(tmp);
        }

        if(criteria.getIsClient() != null) {

            spec = spec.and(
                    (root, query, builder) -> builder.equal(root.get("isClient"), criteria.getIsClient())
            );
        }

        return spec;
    }

    /**
     * @see bw.co.centralkyc.organisation.OrganisationService#search(String)
     */
    @Override
    protected Collection<OrganisationListDTO> handleSearch(OrganisationSearchCriteria criteria,
            Set<PropertySearchOrder> orderings)
            throws Exception {

        Specification<Organisation> spec = createSpecification(criteria);

        Collection<Organisation> orgs = organisationRepository.findAll(spec);

        return organisationDao.toOrganisationListDTOCollection(orgs);

    }

    /**
     * @see bw.co.centralkyc.organisation.OrganisationService#search(Integer,
     *      Integer, String)
     */
    @Override
    protected Page<OrganisationListDTO> handleSearch(SearchObject<OrganisationSearchCriteria> criteria)
            throws Exception {

        Specification<Organisation> spec = createSpecification(criteria.getCriteria());

        Pageable pageable = PageRequest.of(
                criteria.getPageNumber() != null && criteria.getPageNumber() >= 0 ? criteria.getPageNumber() : 0,
                criteria.getPageSize() != null && criteria.getPageSize() > 0 ? criteria.getPageSize()
                        : Integer.MAX_VALUE,
                Sort.by(Direction.ASC, "name") // Default sorting
        );

        Page<Organisation> orgs = organisationRepository.findAll(spec, pageable);

        return orgs.map(organisation -> organisationDao.toOrganisationListDTO(organisation));
    }

    @Override
    protected Long handleCountByKycStatus(KycComplianceStatus kycStatus) throws Exception {
        
        return organisationRepository.countByKycStatus(kycStatus).orElse(0L);
    }

    @Override
    protected Long handleCountByStatus(GeneralStatus status) throws Exception {
        
        return organisationRepository.countByStatus(status).orElse(0L);
    }

    @Override
    protected Long handleCountByIsClientFalse() throws Exception {
        
        return organisationRepository.countByIsClientFalse().orElse(0L);
    }

    @Override
    protected Long handleCountByIsClientTrue() throws Exception {
        
        return organisationRepository.countByIsClientTrue().orElse(0L);
    }

    @Override
    protected Long handleCount() throws Exception {
        
        return organisationRepository.count();
    }

    @Override
    protected OrganisationDTO handleLoadRequestOrganisation(String requestId, String identityConfirmationToken,
            String registrationNo) throws Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'handleLoadRequestOrganisation'");
    }

    @Override
    protected OrganisationDTO handleFindByCode(String code) throws Exception {

        Organisation org = organisationRepository.findByCode(code).orElse(null);

        return organisationMapper.toOrganisationDTO(org);
    }

    @Override
    protected OrganisationDTO handleFindByName(String name) throws Exception {
        Organisation org = organisationRepository.findByName(name).orElse(null);
        return organisationMapper.toOrganisationDTO(org);
    }

}