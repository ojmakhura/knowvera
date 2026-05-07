package bw.co.centralkyc.individual;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;

import bw.co.centralkyc.PropertySearchOrder;
import bw.co.centralkyc.SearchObject;
import bw.co.centralkyc.SortOrder;
import bw.co.centralkyc.kyc.KycComplianceStatus;
import bw.co.centralkyc.organisation.client.ClientRequest;
import bw.co.centralkyc.organisation.client.ClientRequestRepository;

@ExtendWith(MockitoExtension.class)
class IndividualServiceImplTest {

    @Mock
    private IndividualDao individualDao;
    @Mock
    private IndividualRepository individualRepository;
    @Mock
    private IndividualMapper individualMapper;
    @Mock
    private MessageSource messageSource;
    @Mock
    private ClientRequestRepository clientRequestRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    private IndividualServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new IndividualServiceImpl(
                individualDao,
                individualRepository,
                individualMapper,
                messageSource,
                clientRequestRepository,
                passwordEncoder);
    }

    @Test
    void handleLoadRequestIndividualReturnsMappedIndividualWhenTokenMatches() throws Exception {
        UUID requestId = UUID.randomUUID();
        UUID individualId = UUID.randomUUID();
        ClientRequest clientRequest = ClientRequest.Factory.newInstance();
        clientRequest.setIdentityConfirmationToken("encoded-token");
        clientRequest.setTargetId(individualId.toString());

        Individual individual = Individual.Factory.newInstance();
        individual.setId(individualId);
        IndividualDTO expected = new IndividualDTO();

        when(clientRequestRepository.findById(requestId)).thenReturn(Optional.of(clientRequest));
        when(passwordEncoder.matches("plain-token", "encoded-token")).thenReturn(true);
        when(individualRepository.findByIdentityNo("OMANG-1")).thenReturn(Optional.of(individual));
        when(individualMapper.toIndividualDTO(individual)).thenReturn(expected);

        IndividualDTO actual = service.handleLoadRequestIndividual(requestId.toString(), "plain-token", "OMANG-1");

        assertSame(expected, actual);
    }

    @Test
    void handleLoadRequestIndividualThrowsWhenTokenDoesNotMatch() {
        UUID requestId = UUID.randomUUID();
        ClientRequest clientRequest = ClientRequest.Factory.newInstance();
        clientRequest.setIdentityConfirmationToken("encoded-token");

        when(clientRequestRepository.findById(requestId)).thenReturn(Optional.of(clientRequest));
        when(passwordEncoder.matches("wrong-token", "encoded-token")).thenReturn(false);

        assertThrows(
                IndividualServiceException.class,
                () -> service.handleLoadRequestIndividual(requestId.toString(), "wrong-token", "OMANG-1"));
    }

    @Test
    void handleRemoveDeletesFoundIndividual() throws Exception {
        UUID id = UUID.randomUUID();
        Individual individual = Individual.Factory.newInstance();

        when(individualRepository.findById(id)).thenReturn(Optional.of(individual));

        service.handleRemove(id.toString());

        verify(individualRepository).delete(individual);
    }

    @Test
    void handleFindByIdLoadsAndMapsIndividual() throws Exception {
        UUID id = UUID.randomUUID();
        Individual individual = Individual.Factory.newInstance();
        IndividualDTO expected = new IndividualDTO();

        when(individualRepository.getReferenceById(id)).thenReturn(individual);
        when(individualMapper.toIndividualDTO(individual)).thenReturn(expected);

        IndividualDTO actual = service.handleFindById(id.toString());

        assertSame(expected, actual);
    }

    @Test
    void handleGetAllMapsRepositoryResults() throws Exception {
        List<Individual> entities = List.of(Individual.Factory.newInstance());
        List<IndividualListDTO> expected = List.of(new IndividualListDTO("Full Name", "OMANG-1", IndividualIdentityType.OMANG,
            bw.co.centralkyc.kyc.KycComplianceStatus.CURRENT, Sex.MALE));

        when(individualRepository.findAll()).thenReturn(entities);
        when(individualMapper.toIndividualListDTOCollection(entities)).thenReturn(expected);

        List<IndividualListDTO> actual = service.handleGetAll();

        assertSame(expected, actual);
    }

    @Test
    void handleGetAllWithPagingMapsPageItems() throws Exception {
        Individual entity = Individual.Factory.newInstance();
        Page<Individual> page = new PageImpl<>(List.of(entity));
        IndividualListDTO mapped = new IndividualListDTO("Full Name", "OMANG-1", IndividualIdentityType.OMANG,
            bw.co.centralkyc.kyc.KycComplianceStatus.CURRENT, Sex.MALE);

        when(individualRepository.findAll(PageRequest.of(0, 5))).thenReturn(page);
        when(individualMapper.toIndividualListDTO(entity)).thenReturn(mapped);

        Page<IndividualListDTO> actual = service.handleGetAll(0, 5);

        assertEquals(1, actual.getContent().size());
        assertSame(mapped, actual.getContent().get(0));
    }

    @Test
    void handleSaveMapsPersistsAndMapsBack() throws Exception {
        IndividualDTO input = new IndividualDTO();
        Individual entity = Individual.Factory.newInstance();
        IndividualDTO expected = new IndividualDTO();

        when(individualDao.individualDTOToEntity(input)).thenReturn(entity);
        when(individualRepository.save(entity)).thenReturn(entity);
        when(individualMapper.toIndividualDTO(entity)).thenReturn(expected);

        IndividualDTO actual = service.handleSave(input);

        assertSame(expected, actual);
    }

    @Test
    void handleRemoveThrowsWhenIndividualNotFound() {
        UUID id = UUID.randomUUID();
        when(individualRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(IndividualServiceException.class, () -> service.handleRemove(id.toString()));
    }

    @Test
    void handleSearchReturnsMappedCollection() throws Exception {
        IndividualSearchCriteria criteria = new IndividualSearchCriteria();
        criteria.setFirstName("Jo");

        List<Individual> entities = List.of(Individual.Factory.newInstance());
        List<IndividualListDTO> expected = List.of(new IndividualListDTO("Full Name", "OMANG-2", IndividualIdentityType.OMANG,
            bw.co.centralkyc.kyc.KycComplianceStatus.CURRENT, Sex.MALE));

        when(individualRepository.findAll(org.mockito.ArgumentMatchers.<Specification<Individual>>any(), any(Sort.class)))
                .thenReturn(entities);
        when(individualMapper.toIndividualListDTOCollection(entities)).thenReturn(expected);

        List<IndividualListDTO> actual = service.handleSearch(criteria, Set.of(new PropertySearchOrder("surname", SortOrder.ASC)));

        assertSame(expected, actual);
    }

    @Test
    void handleSearchWithPagingReturnsMappedPage() throws Exception {
        IndividualSearchCriteria searchCriteria = new IndividualSearchCriteria();
        searchCriteria.setSurname("Do");

        SearchObject<IndividualSearchCriteria> criteria = new SearchObject<>();
        criteria.setCriteria(searchCriteria);
        criteria.setPageNumber(0);
        criteria.setPageSize(10);

        Individual entity = Individual.Factory.newInstance();
        Page<Individual> page = new PageImpl<>(List.of(entity));
        IndividualListDTO mapped = new IndividualListDTO("Full Name", "OMANG-2", IndividualIdentityType.OMANG,
            bw.co.centralkyc.kyc.KycComplianceStatus.CURRENT, Sex.MALE);

        when(individualRepository.findAll(org.mockito.ArgumentMatchers.<Specification<Individual>>any(), any(PageRequest.class)))
                .thenReturn(page);
        when(individualMapper.toIndividualListDTO(entity)).thenReturn(mapped);

        Page<IndividualListDTO> actual = service.handleSearch(criteria);

        assertEquals(1, actual.getContent().size());
        assertSame(mapped, actual.getContent().get(0));
    }

    @Test
    void handleSearchEvaluatesAllSpecificationPredicates() throws Exception {
        IndividualSearchCriteria criteria = new IndividualSearchCriteria();
        criteria.setEmailAddress("john@example.com");
        criteria.setFirstName("John");
        criteria.setSurname("Doe");
        criteria.setMiddleName("M");
        criteria.setIdentityNo("OMANG-9");

        List<Individual> entities = List.of(Individual.Factory.newInstance());
        when(individualRepository.findAll(org.mockito.ArgumentMatchers.<Specification<Individual>>any(), any(Sort.class)))
                .thenAnswer(invocation -> {
                    evaluateSpecification(invocation.getArgument(0));
                    return entities;
                });
        when(individualMapper.toIndividualListDTOCollection(entities)).thenReturn(List.of());

        List<IndividualListDTO> actual = service.handleSearch(criteria, Set.of());

        assertTrue(actual.isEmpty());
    }

    @Test
    void handleGetOrganisationClientsThrowsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class,
                () -> service.handleGetOrganisationClients(UUID.randomUUID().toString()));
    }

    @Test
    void handleGetOrganisationClientsPagedThrowsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class,
                () -> service.handleGetOrganisationClients(UUID.randomUUID().toString(), 0, 10));
    }

    @Test
    void handleFindByIdentityNoAndIdentityTypeThrowsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class,
                () -> service.handleFindByIdentityNoAndIdentityType("123", IndividualIdentityType.OMANG));
    }

    @Test
    void handleCountByPepStatusReturnsRepositoryValueOrZero() throws Exception {
        when(individualRepository.countByPepStatus(PepStatus.PEP_SELF)).thenReturn(Optional.of(3L));
        when(individualRepository.countByPepStatus(PepStatus.NOT_PEP)).thenReturn(Optional.empty());

        assertEquals(3L, service.handleCountByPepStatus(PepStatus.PEP_SELF));
        assertEquals(0L, service.handleCountByPepStatus(PepStatus.NOT_PEP));
    }

    @Test
    void handleCountReturnsRepositoryCount() throws Exception {
        when(individualRepository.count()).thenReturn(9L);

        assertEquals(9L, service.handleCount());
    }

    @Test
    void handleCountByKycStatusReturnsRepositoryValueOrZero() throws Exception {
        when(individualRepository.countByKycStatus(KycComplianceStatus.CURRENT)).thenReturn(Optional.of(2L));
        when(individualRepository.countByKycStatus(KycComplianceStatus.EXPIRED)).thenReturn(Optional.empty());

        assertEquals(2L, service.handleCountByKycStatus(KycComplianceStatus.CURRENT));
        assertEquals(0L, service.handleCountByKycStatus(KycComplianceStatus.EXPIRED));
    }

    @Test
    void handleCountByEmploymentStatusReturnsRepositoryValueOrZero() throws Exception {
        when(individualRepository.countByEmploymentStatus(EmploymentStatus.EMPLOYED)).thenReturn(Optional.of(4L));
        when(individualRepository.countByEmploymentStatus(EmploymentStatus.UNEMPLOYED)).thenReturn(Optional.empty());

        assertEquals(4L, service.handleCountByEmploymentStatus(EmploymentStatus.EMPLOYED));
        assertEquals(0L, service.handleCountByEmploymentStatus(EmploymentStatus.UNEMPLOYED));
    }

    @Test
    void handleCountBySexReturnsRepositoryValueOrZero() throws Exception {
        when(individualRepository.countBySex(Sex.MALE)).thenReturn(Optional.of(7L));
        when(individualRepository.countBySex(Sex.FEMALE)).thenReturn(Optional.empty());

        assertEquals(7L, service.handleCountBySex(Sex.MALE));
        assertEquals(0L, service.handleCountBySex(Sex.FEMALE));
    }

    @Test
    void handleLoadRequestIndividualThrowsWhenRequestNotFound() {
        UUID requestId = UUID.randomUUID();
        when(clientRequestRepository.findById(requestId)).thenReturn(Optional.empty());

        assertThrows(IndividualServiceException.class,
                () -> service.handleLoadRequestIndividual(requestId.toString(), "token", "OMANG-1"));
    }

    @Test
    void handleLoadRequestIndividualThrowsWhenIdentityNotFound() {
        UUID requestId = UUID.randomUUID();
        UUID individualId = UUID.randomUUID();
        ClientRequest clientRequest = ClientRequest.Factory.newInstance();
        clientRequest.setIdentityConfirmationToken("encoded-token");
        clientRequest.setTargetId(individualId.toString());

        when(clientRequestRepository.findById(requestId)).thenReturn(Optional.of(clientRequest));
        when(passwordEncoder.matches("plain-token", "encoded-token")).thenReturn(true);
        when(individualRepository.findByIdentityNo("OMANG-1")).thenReturn(Optional.empty());

        assertThrows(Exception.class,
                () -> service.handleLoadRequestIndividual(requestId.toString(), "plain-token", "OMANG-1"));
    }

    @Test
    void handleLoadRequestIndividualThrowsWhenTargetMismatch() {
        UUID requestId = UUID.randomUUID();
        UUID requestTargetId = UUID.randomUUID();
        UUID otherIndividualId = UUID.randomUUID();

        ClientRequest clientRequest = ClientRequest.Factory.newInstance();
        clientRequest.setIdentityConfirmationToken("encoded-token");
        clientRequest.setTargetId(requestTargetId.toString());

        Individual individual = Individual.Factory.newInstance();
        individual.setId(otherIndividualId);

        when(clientRequestRepository.findById(requestId)).thenReturn(Optional.of(clientRequest));
        when(passwordEncoder.matches("plain-token", "encoded-token")).thenReturn(true);
        when(individualRepository.findByIdentityNo("OMANG-1")).thenReturn(Optional.of(individual));

        assertThrows(IndividualServiceException.class,
                () -> service.handleLoadRequestIndividual(requestId.toString(), "plain-token", "OMANG-1"));
    }

    @Test
    void handleFindByUserIdReturnsMappedEntity() throws Exception {
        Individual individual = Individual.Factory.newInstance();
        IndividualDTO expected = new IndividualDTO();

        when(individualRepository.findByUserId("user-1")).thenReturn(Optional.of(individual));
        when(individualMapper.toIndividualDTO(individual)).thenReturn(expected);

        IndividualDTO actual = service.handleFindByUserId("user-1");

        assertSame(expected, actual);
    }

    @Test
    void handleFindByUserIdThrowsWhenNotFound() {
        when(individualRepository.findByUserId("user-1")).thenReturn(Optional.empty());

        assertThrows(IndividualServiceException.class, () -> service.handleFindByUserId("user-1"));
    }

    private void evaluateSpecification(Specification<Individual> specification) {
        Root<Individual> root = org.mockito.Mockito.mock(Root.class);
        @SuppressWarnings("unchecked")
        Path<Object> path = org.mockito.Mockito.mock(Path.class);
        CriteriaQuery<Object> query = org.mockito.Mockito.mock(CriteriaQuery.class);
        CriteriaBuilder builder = org.mockito.Mockito.mock(CriteriaBuilder.class);
        Predicate predicate = org.mockito.Mockito.mock(Predicate.class);

        org.mockito.Mockito.lenient().when(root.get(any(String.class))).thenReturn(path);
        org.mockito.Mockito.lenient().when(path.get(any(String.class))).thenReturn(path);
        org.mockito.Mockito.lenient().when(builder.conjunction()).thenReturn(predicate);
        org.mockito.Mockito.lenient().when(builder.lower(any())).thenAnswer(invocation -> invocation.getArgument(0));
        org.mockito.Mockito.lenient().when(builder.like(any(), any(String.class))).thenReturn(predicate);
        org.mockito.Mockito.lenient().when(builder.equal(any(), any())).thenReturn(predicate);
        org.mockito.Mockito.lenient().when(builder.and(any(Predicate.class), any(Predicate.class))).thenReturn(predicate);

        specification.toPredicate(root, query, builder);
    }
}