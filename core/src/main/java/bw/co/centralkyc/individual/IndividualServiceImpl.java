// license-header java merge-point
/**
 * This is only generated once! It will never be overwritten.
 * You can (and have to!) safely modify it by hand.
 * TEMPLATE:    SpringServiceImpl.vsl in andromda-spring cartridge
 * MODEL CLASS: AndroMDAModel::backend::bw.co.centralkyc::individual::IndividualService
 * STEREOTYPE:  Service
 */
package bw.co.centralkyc.individual;

import java.util.Collection;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import bw.co.centralkyc.PropertySearchOrder;
import bw.co.centralkyc.SearchObject;
import bw.co.centralkyc.kyc.KycComplianceStatus;
import bw.co.centralkyc.organisation.client.ClientRequest;
import bw.co.centralkyc.organisation.client.ClientRequestRepository;

/**
 * @see bw.co.centralkyc.individual.IndividualService
 */
@Service("individualService")
public class IndividualServiceImpl
        extends IndividualServiceBase {

    private final IndividualMapper individualMapper;
    private final ClientRequestRepository clientRequestRepository;
    private final PasswordEncoder passwordEncoder;

    public IndividualServiceImpl(
            IndividualDao individualDao, IndividualMapper individualMapper,
            IndividualRepository individualRepository, ClientRequestRepository clientRequestRepository,
            PasswordEncoder passwordEncoder, MessageSource messageSource) {

        super(
                individualDao,
                individualRepository,
                messageSource);
        
        this.individualMapper = individualMapper;
        this.clientRequestRepository = clientRequestRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * @see bw.co.centralkyc.individual.IndividualService#findById(String)
     */
    @Override
    protected IndividualDTO handleFindById(String id)
            throws Exception {

        Individual individual = individualRepository.getReferenceById(UUID.fromString(id));

        System.out.println("Fetched Individual: " + individual);
        return individualMapper.toIndividualDTO(individual);
    }

    /**
     * @see bw.co.centralkyc.individual.IndividualService#getAll()
     */
    @Override
    protected Collection<IndividualListDTO> handleGetAll()
            throws Exception {

        return individualMapper.toIndividualListDTOCollection(individualRepository.findAll());
    }

    /**
     * @see bw.co.centralkyc.individual.IndividualService#getAll(Integer, Integer)
     */
    @Override
    protected Page<IndividualListDTO> handleGetAll(Integer pageNumber, Integer pageSize)
            throws Exception {

        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        Page<Individual> individuals = individualRepository.findAll(pageRequest);

        return individuals.map(individual -> individualMapper.toIndividualListDTO(individual));
    }

    /**
     * @see bw.co.centralkyc.individual.IndividualService#remove(String)
     */
    @Override
    protected boolean handleRemove(String id)
            throws Exception {

        Individual individual = individualRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new IndividualServiceException("Individual not found with id: " + id));

        individualRepository.delete(individual);
        return true;
    }

    /**
     * @see bw.co.centralkyc.individual.IndividualService#save(IndividualDTO)
     */
    @Override
    protected IndividualDTO handleSave(IndividualDTO individual)
            throws Exception {

        Individual entity = individualDao.individualDTOToEntity(individual);
        entity = individualRepository.save(entity);

        return individualDao.toIndividualDTO(entity);
    }

    private Specification<Individual> createSpecification(IndividualSearchCriteria criteria) {

        Specification<Individual> spec = ((root, query, builder) -> builder.conjunction());

        if(StringUtils.isNotBlank(criteria.getEmailAddress())) {

            Specification<Individual>  tmp = ((root, query, builder) ->
                    builder.like(builder.lower(root.get("email")), "%" + criteria.getEmailAddress().toLowerCase() + "%"));
            spec = spec == null ? tmp : spec.and(tmp);

        }

        if(StringUtils.isNotBlank(criteria.getFirstName())) {

            Specification<Individual>  tmp = ((root, query, builder) ->
                    builder.like(builder.lower(root.get("firstName")), "%" + criteria.getFirstName().toLowerCase() + "%"));
            spec = spec == null ? tmp : spec.and(tmp);

        }

        if(StringUtils.isNotBlank(criteria.getSurname())) {

            Specification<Individual>  tmp = ((root, query, builder) ->
                    builder.like(builder.lower(root.get("surname")), "%" + criteria.getSurname().toLowerCase() + "%"));
            spec = spec == null ? tmp : spec.and(tmp);

        }

        if(StringUtils.isNotBlank(criteria.getMiddleName())) {

            Specification<Individual>  tmp = ((root, query, builder) ->
                    builder.like(builder.lower(root.get("middleName")), "%" + criteria.getMiddleName().toLowerCase() + "%"));
            spec = spec == null ? tmp : spec.and(tmp);

        }

        if(StringUtils.isNotBlank(criteria.getIdentityNo())) {

            Specification<Individual>  tmp = ((root, query, builder) ->
                    builder.equal(builder.lower(root.get("identityNo")), criteria.getIdentityNo().toLowerCase()));
            spec = spec == null ? tmp : spec.and(tmp);

        }

        return spec;
    }

    /**
     * @see bw.co.centralkyc.individual.IndividualService#search(String)
     */
    @Override
    protected Collection<IndividualListDTO> handleSearch(IndividualSearchCriteria criteria, Set<PropertySearchOrder> orderings)
            throws Exception {

        Specification<Individual> spec = createSpecification(criteria);

        return individualMapper.toIndividualListDTOCollection(
            spec == null ? 
            individualRepository.findAll(Sort.by(Sort.Direction.ASC, "surname")) :
            individualRepository.findAll(spec, Sort.by(Sort.Direction.ASC, "surname"))
        );
    }

    /**
     * @see bw.co.centralkyc.individual.IndividualService#search(Integer, Integer,
     *      String)
     */
    @Override
    protected Page<IndividualListDTO> handleSearch(SearchObject<IndividualSearchCriteria> criteria)
            throws Exception {

        Specification<Individual> spec = createSpecification(criteria.getCriteria());

        PageRequest pageRequest = PageRequest.of(criteria.getPageNumber(), criteria.getPageSize());
        Page<Individual> individuals = spec == null ? individualRepository.findAll(pageRequest) : individualRepository.findAll(spec, pageRequest);

        return individuals.map(individual -> individualMapper.toIndividualListDTO(individual));
    }

    @Override
    protected Collection<IndividualListDTO> handleGetOrganisationClients(String organisationId) throws Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'handleGetOrganisationClients'");
    }

    @Override
    protected Page<IndividualListDTO> handleGetOrganisationClients(String organisationId, Integer pageNumber,
            Integer pageSize) throws Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'handleGetOrganisationClients'");
    }

    @Override
    protected IndividualDTO handleFindByIdentityNoAndIdentityType(String identityNo, IndividualIdentityType identityType)
            throws Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'handleFindByIdentityNoAndIdentityType'");
    }

    @Override
    protected Long handleCountByPepStatus(PepStatus pepStatus) throws Exception {
        
        return this.individualRepository.countByPepStatus(pepStatus).orElse(0L);
    }

    @Override
    protected Long handleCount() throws Exception {
        // TODO Auto-generated method stub
        return this.individualRepository.count();
    }

    @Override
    protected Long handleCountByKycStatus(KycComplianceStatus kycStatus) throws Exception {
        
        return this.individualRepository.countByKycStatus(kycStatus).orElse(0L);
    }

    @Override
    protected Long handleCountByEmploymentStatus(EmploymentStatus employmentStatus) throws Exception {
        
        return this.individualRepository.countByEmploymentStatus(employmentStatus).orElse(0L);
    }

    @Override
    protected Long handleCountBySex(Sex sex) throws Exception {
        
        return this.individualRepository.countBySex(sex).orElse(0L);
    }

    @Override
    protected IndividualDTO handleLoadRequestIndividual(String requestId, String identityConfirmationToken,
            String identityNo) throws Exception {

        ClientRequest clientRequest = clientRequestRepository.findById(UUID.fromString(requestId))
                .orElseThrow(() -> new IndividualServiceException("ClientRequest not found"));

        String token = clientRequest.getIdentityConfirmationToken();
        
        boolean matches = passwordEncoder.matches(identityConfirmationToken, token);

        if (!matches) {
            throw new IndividualServiceException("Invalid confirmation token");
        }

        Individual individual = individualRepository.findByIdentityNo(identityNo)
                .orElseThrow(() -> new Exception("Individual not found for identityNo: " + identityNo));

        if (individual == null) {
            throw new IndividualServiceException("Individual not found with identityNo: " + identityNo);
        }

        if(!individual.getId().equals(UUID.fromString(clientRequest.getTargetId()))) {

            throw new IndividualServiceException("Individual does not match ClientRequest target");
        }

        return individualDao.toIndividualDTO(individual);
    }

    @Override
    protected IndividualDTO handleFindByUserId(String userId) throws Exception {
        
        Individual individual = individualRepository.findByUserId(userId)
                    .orElseThrow(() -> new IndividualServiceException("Individual not found for userId: " + userId));
        return individualDao.toIndividualDTO(individual);
    }

}