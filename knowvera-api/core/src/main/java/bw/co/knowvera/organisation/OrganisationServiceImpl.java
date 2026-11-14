package bw.co.knowvera.organisation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
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

import bw.co.knowvera.GeneralStatus;
import bw.co.knowvera.PropertySearchOrder;
import bw.co.knowvera.SearchObject;
import bw.co.knowvera.TargetEntity;
import bw.co.knowvera.kyc.KycComplianceStatus;
import bw.co.knowvera.organisation.client.ClientRequestRepository;
import bw.co.knowvera.retention.RecordRetention;

/**
 * @see bw.co.knowvera.organisation.OrganisationService
 */
@Service("organisationService")
public class OrganisationServiceImpl
        extends OrganisationServiceBase {

    private final OrganisationRepository organisationRepository;
    private final ClientRequestRepository clientRequestRepository;
    private final PasswordEncoder passwordEncoder;

    /** How long the identity confirmation token stays valid; as for individuals. */
    @org.springframework.beans.factory.annotation.Value("${app.request-token-session-ttl:24h}")
    private java.time.Duration requestSessionTtl;
    private final OrganisationListQueries organisationListQueries;
    private final RecordRetention recordRetention;


    public OrganisationServiceImpl(OrganisationRepository organisationRepository, OrganisationMapper organisationMapper,
            ClientRequestRepository clientRequestRepository, PasswordEncoder passwordEncoder,
            OrganisationListQueries organisationListQueries, RecordRetention recordRetention) {
        super( organisationRepository, organisationMapper);
        this.organisationRepository = organisationRepository;
        // TODO Auto-generated constructor stub

        this.clientRequestRepository = clientRequestRepository;
        this.passwordEncoder = passwordEncoder;
        this.organisationListQueries = organisationListQueries;
        this.recordRetention = recordRetention;
    }

    /**
     * @see bw.co.knowvera.organisation.OrganisationService#findById(String)
     */
    @Override
    protected OrganisationDTO handleFindById(String id)
            throws Exception {

        Organisation org = organisationRepository.findById(UUID.fromString(id)).orElseThrow(
                () -> new OrganisationServiceException("Could not find organisation: " + id)
        );

        return organisationMapper.toOrganisationDTO(org);
    }

    /**
     * @see bw.co.knowvera.organisation.OrganisationService#getAll()
     */
    @Override
    protected List<OrganisationListDTO> handleGetAll()
            throws Exception {

        return organisationListQueries.search(null, Sort.unsorted());

    }

    /**
     * @see bw.co.knowvera.organisation.OrganisationService#getAll(Integer,
     *      Integer)
     */
    @Override
    protected Page<OrganisationListDTO> handleGetAll(Integer pageNumber, Integer pageSize)
            throws Exception {

        return organisationListQueries.search(null, PageRequest.of(pageNumber, pageSize));
    }

    /**
     * @see bw.co.knowvera.organisation.OrganisationService#remove(String)
     */
    @Override
    protected boolean handleRemove(String id)
            throws Exception {

        Organisation organisation = organisationRepository.findById(UUID.fromString(id)).orElse(null);

        if (organisation == null) {
            return false;
        }

        // Removing ends the relationship; the records are kept for the retention period
        if (organisation.getClosedAt() == null) {
            LocalDateTime closedAt = LocalDateTime.now();
            organisation.setClosedAt(closedAt);
            organisation.setRetainUntil(recordRetention.retainUntil(closedAt));
            organisationRepository.save(organisation);
            recordRetention.closeKycRecords(TargetEntity.ORGANISATION, id, closedAt);
            return true;
        }

        if (!recordRetention.mayDelete(organisation.getRetainUntil(), LocalDate.now())) {
            throw new OrganisationServiceException(
                    recordRetention.retainedMessage("Organisation " + id, organisation.getRetainUntil()));
        }

        organisationRepository.delete(organisation);
        return true;
    }

    /**
     * @see bw.co.knowvera.organisation.OrganisationService#save(OrganisationDTO)
     */
    @Override
    protected OrganisationDTO handleSave(OrganisationDTO organisation)
            throws Exception {

        Organisation orgEntity = organisationMapper.organisationDTOToEntity(organisation);

        // Closing is not editable through save: keep what is stored
        if (orgEntity.getId() != null) {
            organisationRepository.findById(orgEntity.getId()).ifPresent(existing -> {
                orgEntity.setClosedAt(existing.getClosedAt());
                orgEntity.setRetainUntil(existing.getRetainUntil());
            });
        }

        Organisation saved = organisationRepository.save(orgEntity);

        return organisationMapper.toOrganisationDTO(saved);
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

        // The same text as name and registration number is a quick search: either may match
        boolean quickSearch = StringUtils.isNotBlank(criteria.getName())
                && StringUtils.equals(criteria.getName(), criteria.getRegistrationNo());
        if (quickSearch) {
            String text = criteria.getName().trim();
            Specification<Organisation> tmp = ((root, query, builder) -> builder.or(
                    builder.like(builder.upper(root.get("name")), "%" + text.toUpperCase() + "%"),
                    builder.equal(root.get("registrationNo"), text)));
            spec = spec == null ? tmp : spec.and(tmp);
        }

        if (!quickSearch && StringUtils.isNotBlank(criteria.getName())) {
            Specification<Organisation> tmp = ((root, query, builder) -> builder.like(builder.upper(root.get("name")),
                    "%" + criteria.getName().toUpperCase() + "%"));
            spec = spec == null ? tmp : spec.and(tmp);
        }

        if (!quickSearch && StringUtils.isNotBlank(criteria.getRegistrationNo())) {
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
     * @see bw.co.knowvera.organisation.OrganisationService#search(String)
     */
    @Override
    protected List<OrganisationListDTO> handleSearch(OrganisationSearchCriteria criteria,
            Set<PropertySearchOrder> orderings)
            throws Exception {

        Specification<Organisation> spec = createSpecification(criteria);

        return organisationListQueries.search(spec, Sort.unsorted());

    }

    /**
     * @see bw.co.knowvera.organisation.OrganisationService#search(Integer,
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

        return organisationListQueries.search(spec, pageable);
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

        bw.co.knowvera.organisation.client.ClientRequest clientRequest = clientRequestRepository
                .findById(UUID.fromString(requestId))
                .orElseThrow(() -> new OrganisationServiceException("ClientRequest not found"));

        if (StringUtils.isBlank(identityConfirmationToken)) {
            // The portal only has a token after confirm-token succeeded
            throw new OrganisationServiceException(
                    "No identity confirmation token: the registration link was not confirmed");
        }

        String token = clientRequest.getIdentityConfirmationToken();
        boolean matches = token != null && passwordEncoder.matches(identityConfirmationToken, token);
        if (!matches || bw.co.knowvera.utils.RequestTokens.isExpired(identityConfirmationToken, requestSessionTtl,
                clientRequest.getCreatedAt())) {
            throw new OrganisationServiceException("Invalid or expired confirmation token");
        }

        if (clientRequest.getTarget() != TargetEntity.ORGANISATION) {
            throw new OrganisationServiceException("The client request is not for an organisation");
        }

        // The registration number is what the client proves they know: it must be the request's organisation
        Organisation organisation = StringUtils.isBlank(registrationNo) ? null
                : organisationRepository.findByRegistrationNo(registrationNo.trim()).orElse(null);
        if (organisation == null || !organisation.getId().equals(UUID.fromString(clientRequest.getTargetId()))) {
            throw new OrganisationServiceException("The registration number does not match this request");
        }

        return organisationMapper.toOrganisationDTO(organisation);
    }

    @Override
    protected OrganisationDTO handleFindByCode(String code) throws Exception {

        Organisation org = organisationRepository.findByCode(code).orElse(null);

        if(org == null) {
            return null;
        }

        return organisationMapper.toOrganisationDTO(org);
    }

    @Override
    protected OrganisationDTO handleFindByName(String name) throws Exception {
        Organisation org = organisationRepository.findByName(name).orElse(null);

        if(org == null) {
            return null;
        }

        return organisationMapper.toOrganisationDTO(org);
    }

    @Override
    protected OrganisationDTO handleFindByRegistrationNo(String registrationNo) throws Exception {
        
        Organisation org = organisationRepository.findByRegistrationNo(registrationNo).orElse(null);

        if(org == null) {
            return null;
        }

        return organisationMapper.toOrganisationDTO(org);

    }

    @Override
    protected OrganisationDTO handleVerifyOrganisation(String id, String user) throws Exception {
        
        Organisation org = organisationRepository.findById(UUID.fromString(id)).orElse(null);

        if(org == null) {
            return null;
        }

        org = organisationRepository.save(org);

        return organisationMapper.toOrganisationDTO(org);

    }

    @Override
    protected OrganisationListDTO handleFindListOrganisationById(String id) throws Exception {
        
        return organisationRepository.findListOrganisationById(UUID.fromString(id)).get();
    }

    @Override
    protected OrganisationListDTO handleFindListOrganisationByKeycloakId(String keycloakId) throws Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'handleFindListOrganisationByKeycloakId'");
    }

}
