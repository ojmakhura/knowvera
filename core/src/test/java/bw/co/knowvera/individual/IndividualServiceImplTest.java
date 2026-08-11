package bw.co.knowvera.individual;

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

import bw.co.knowvera.PropertySearchOrder;
import bw.co.knowvera.SearchObject;
import bw.co.knowvera.SortOrder;
import bw.co.knowvera.kyc.KycComplianceStatus;
import bw.co.knowvera.organisation.client.ClientRequest;
import bw.co.knowvera.organisation.client.ClientRequestRepository;
import bw.co.knowvera.settings.SettingsService;

@ExtendWith(MockitoExtension.class)
class IndividualServiceImplTest {

    @Mock
    private IndividualRepository individualRepository;
    @Mock
    private IndividualMapper individualMapper;
    @Mock
    private ClientRequestRepository clientRequestRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private SettingsService settingsService;

    private IndividualService service;

    @BeforeEach
    void setUp() {
        service = new IndividualServiceImpl(
                individualRepository,
            settingsService,
                individualMapper,
                clientRequestRepository,
                passwordEncoder);
    }

    @Test
    void loadRequestIndividualReturnsMappedIndividualWhenTokenMatches() throws Exception {
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

        IndividualDTO actual = service.loadRequestIndividual(requestId.toString(), "plain-token", "OMANG-1");

        assertSame(expected, actual);
    }

    @Test
    void loadRequestIndividualThrowsWhenTokenDoesNotMatch() {
        UUID requestId = UUID.randomUUID();
        ClientRequest clientRequest = ClientRequest.Factory.newInstance();
        clientRequest.setIdentityConfirmationToken("encoded-token");

        when(clientRequestRepository.findById(requestId)).thenReturn(Optional.of(clientRequest));
        when(passwordEncoder.matches("wrong-token", "encoded-token")).thenReturn(false);

        assertThrows(
                IndividualServiceException.class,
                () -> service.loadRequestIndividual(requestId.toString(), "wrong-token", "OMANG-1"));
    }

    @Test
    void removeDeletesFoundIndividual() throws Exception {
        UUID id = UUID.randomUUID();
        Individual individual = Individual.Factory.newInstance();

        when(individualRepository.findById(id)).thenReturn(Optional.of(individual));

        service.remove(id.toString());

        verify(individualRepository).delete(individual);
    }

    @Test
    void findByIdLoadsAndMapsIndividual() throws Exception {
        UUID id = UUID.randomUUID();
        Individual individual = Individual.Factory.newInstance();
        IndividualDTO expected = new IndividualDTO();

        when(individualRepository.getReferenceById(id)).thenReturn(individual);
        when(individualMapper.toIndividualDTO(individual)).thenReturn(expected);

        IndividualDTO actual = service.findById(id.toString());

        assertSame(expected, actual);
    }

    @Test
    void getAllMapsRepositoryResults() throws Exception {
        List<Individual> entities = List.of(Individual.Factory.newInstance());
        List<IndividualListDTO> expected = List.of(new IndividualListDTO("Full Name", "OMANG-1", IndividualIdentityType.OMANG,
            bw.co.knowvera.kyc.KycComplianceStatus.CURRENT, Sex.MALE));

        when(individualRepository.findAll()).thenReturn(entities);
        when(individualMapper.toIndividualListDTOCollection(entities)).thenReturn(expected);

        List<IndividualListDTO> actual = service.getAll();

        assertSame(expected, actual);
    }

    @Test
    void getAllWithPagingMapsPageItems() throws Exception {
        Individual entity = Individual.Factory.newInstance();
        Page<Individual> page = new PageImpl<>(List.of(entity));
        IndividualListDTO mapped = new IndividualListDTO("Full Name", "OMANG-1", IndividualIdentityType.OMANG,
            bw.co.knowvera.kyc.KycComplianceStatus.CURRENT, Sex.MALE);

        when(individualRepository.findAll(PageRequest.of(0, 5))).thenReturn(page);
        when(individualMapper.toIndividualListDTO(entity)).thenReturn(mapped);

        Page<IndividualListDTO> actual = service.getAll(0, 5);

        assertEquals(1, actual.getContent().size());
        assertSame(mapped, actual.getContent().get(0));
    }

    @Test
    void saveMapsPersistsAndMapsBack() throws Exception {
        IndividualDTO input = new IndividualDTO();
        input.setFirstName("John");
        input.setSurname("Doe");
        input.setIdentityNo("OMANG-100");
        input.setIdentityType(IndividualIdentityType.OMANG);
        input.setKycStatus(KycComplianceStatus.CURRENT);
        input.setSex(Sex.MALE);
        input.setNationality("BW");
        input.setMaritalStatus(MaritalStatus.SINGLE);
        input.setEmploymentStatus(EmploymentStatus.EMPLOYED);
        Individual entity = Individual.Factory.newInstance();
        IndividualDTO expected = new IndividualDTO();

        when(individualMapper.individualDTOToEntity(input)).thenReturn(entity);
        when(individualRepository.save(entity)).thenReturn(entity);
        when(individualMapper.toIndividualDTO(entity)).thenReturn(expected);

        IndividualDTO actual = service.save(input);

        assertSame(expected, actual);
    }

    @Test
    void removeThrowsWhenIndividualNotFound() {
        UUID id = UUID.randomUUID();
        when(individualRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(IndividualServiceException.class, () -> service.remove(id.toString()));
    }

    @Test
    void searchReturnsMappedCollection() throws Exception {
        IndividualSearchCriteria criteria = new IndividualSearchCriteria();
        criteria.setFirstName("Jo");

        List<Individual> entities = List.of(Individual.Factory.newInstance());
        List<IndividualListDTO> expected = List.of(new IndividualListDTO("Full Name", "OMANG-2", IndividualIdentityType.OMANG,
            bw.co.knowvera.kyc.KycComplianceStatus.CURRENT, Sex.MALE));

        when(individualRepository.findAll(org.mockito.ArgumentMatchers.<Specification<Individual>>any(), any(Sort.class)))
                .thenReturn(entities);
        when(individualMapper.toIndividualListDTOCollection(entities)).thenReturn(expected);

        List<IndividualListDTO> actual = service.search(criteria, Set.of(new PropertySearchOrder("surname", SortOrder.ASC)));

        assertSame(expected, actual);
    }

    @Test
    void searchWithPagingReturnsMappedPage() throws Exception {
        IndividualSearchCriteria searchCriteria = new IndividualSearchCriteria();
        searchCriteria.setSurname("Do");

        SearchObject<IndividualSearchCriteria> criteria = new SearchObject<>();
        criteria.setCriteria(searchCriteria);
        criteria.setPageNumber(0);
        criteria.setPageSize(10);

        Individual entity = Individual.Factory.newInstance();
        Page<Individual> page = new PageImpl<>(List.of(entity));
        IndividualListDTO mapped = new IndividualListDTO("Full Name", "OMANG-2", IndividualIdentityType.OMANG,
            bw.co.knowvera.kyc.KycComplianceStatus.CURRENT, Sex.MALE);

        when(individualRepository.findAll(org.mockito.ArgumentMatchers.<Specification<Individual>>any(), any(PageRequest.class)))
                .thenReturn(page);
        when(individualMapper.toIndividualListDTO(entity)).thenReturn(mapped);

        Page<IndividualListDTO> actual = service.search(criteria);

        assertEquals(1, actual.getContent().size());
        assertSame(mapped, actual.getContent().get(0));
    }

    @Test
    void searchEvaluatesAllSpecificationPredicates() throws Exception {
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

        List<IndividualListDTO> actual = service.search(criteria, Set.of());

        assertTrue(actual.isEmpty());
    }

    @Test
    void getOrganisationClientsThrowsUnsupportedOperationException() {
        assertThrows(IndividualServiceException.class,
                () -> service.getOrganisationClients(UUID.randomUUID().toString()));
    }

    @Test
    void getOrganisationClientsWithPagingThrowsUnsupportedOperationException() {
        assertThrows(IndividualServiceException.class,
                () -> service.getOrganisationClients(UUID.randomUUID().toString(), 0, 10));
    }

    @Test
    void findByIdentityNoAndIdentityTypeThrowsUnsupportedOperationException() {
        assertThrows(IndividualServiceException.class,
                () -> service.findByIdentityNoAndIdentityType("123", IndividualIdentityType.OMANG));
    }

    @Test
    void countByPepStatusReturnsRepositoryValueOrZero() throws Exception {
        when(individualRepository.countByPepStatus(PepStatus.PEP_SELF)).thenReturn(Optional.of(3L));
        when(individualRepository.countByPepStatus(PepStatus.NOT_PEP)).thenReturn(Optional.empty());

        assertEquals(3L, service.countByPepStatus(PepStatus.PEP_SELF));
        assertEquals(0L, service.countByPepStatus(PepStatus.NOT_PEP));
    }

    @Test
    void countReturnsRepositoryCount() throws Exception {
        when(individualRepository.count()).thenReturn(9L);

        assertEquals(9L, service.count());
    }

    @Test
    void countByKycStatusReturnsRepositoryValueOrZero() throws Exception {
        when(individualRepository.countByKycStatus(KycComplianceStatus.CURRENT)).thenReturn(Optional.of(2L));
        when(individualRepository.countByKycStatus(KycComplianceStatus.EXPIRED)).thenReturn(Optional.empty());

        assertEquals(2L, service.countByKycStatus(KycComplianceStatus.CURRENT));
        assertEquals(0L, service.countByKycStatus(KycComplianceStatus.EXPIRED));
    }

    @Test
    void countByEmploymentStatusReturnsRepositoryValueOrZero() throws Exception {
        when(individualRepository.countByEmploymentStatus(EmploymentStatus.EMPLOYED)).thenReturn(Optional.of(4L));
        when(individualRepository.countByEmploymentStatus(EmploymentStatus.UNEMPLOYED)).thenReturn(Optional.empty());

        assertEquals(4L, service.countByEmploymentStatus(EmploymentStatus.EMPLOYED));
        assertEquals(0L, service.countByEmploymentStatus(EmploymentStatus.UNEMPLOYED));
    }

    @Test
    void countBySexReturnsRepositoryValueOrZero() throws Exception {
        when(individualRepository.countBySex(Sex.MALE)).thenReturn(Optional.of(7L));
        when(individualRepository.countBySex(Sex.FEMALE)).thenReturn(Optional.empty());

        assertEquals(7L, service.countBySex(Sex.MALE));
        assertEquals(0L, service.countBySex(Sex.FEMALE));
    }

    @Test
    void loadRequestIndividualThrowsWhenRequestNotFound() {
        UUID requestId = UUID.randomUUID();
        when(clientRequestRepository.findById(requestId)).thenReturn(Optional.empty());

        assertThrows(IndividualServiceException.class,
                () -> service.loadRequestIndividual(requestId.toString(), "token", "OMANG-1"));
    }

    @Test
    void loadRequestIndividualThrowsWhenIdentityNotFound() {
        UUID requestId = UUID.randomUUID();
        UUID individualId = UUID.randomUUID();
        ClientRequest clientRequest = ClientRequest.Factory.newInstance();
        clientRequest.setIdentityConfirmationToken("encoded-token");
        clientRequest.setTargetId(individualId.toString());

        when(clientRequestRepository.findById(requestId)).thenReturn(Optional.of(clientRequest));
        when(passwordEncoder.matches("plain-token", "encoded-token")).thenReturn(true);
        when(individualRepository.findByIdentityNo("OMANG-1")).thenReturn(Optional.empty());

        assertThrows(Exception.class,
                () -> service.loadRequestIndividual(requestId.toString(), "plain-token", "OMANG-1"));
    }

    @Test
    void loadRequestIndividualThrowsWhenTargetMismatch() {
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
                () -> service.loadRequestIndividual(requestId.toString(), "plain-token", "OMANG-1"));
    }

    @Test
    void findByUserIdReturnsMappedEntity() throws Exception {
        Individual individual = Individual.Factory.newInstance();
        IndividualDTO expected = new IndividualDTO();

        when(individualRepository.findByUserId("user-1")).thenReturn(Optional.of(individual));
        when(individualMapper.toIndividualDTO(individual)).thenReturn(expected);

        IndividualDTO actual = service.findByUserId("user-1");

        assertSame(expected, actual);
    }

    @Test
    void findByUserIdThrowsWhenNotFound() {
        when(individualRepository.findByUserId("user-1")).thenReturn(Optional.empty());

        assertThrows(IndividualServiceException.class, () -> service.findByUserId("user-1"));
    }

        @Test
        void serviceMethodsRejectInvalidArguments() {
        assertThrows(IllegalArgumentException.class, () -> service.findById(null));
        assertThrows(IllegalArgumentException.class, () -> service.findById(" "));
        assertThrows(IllegalArgumentException.class, () -> service.remove(null));
        assertThrows(IllegalArgumentException.class, () -> service.remove("\t"));
        assertThrows(IllegalArgumentException.class, () -> service.save(null));
        assertThrows(IllegalArgumentException.class, () -> service.getOrganisationClients(null));
        assertThrows(IllegalArgumentException.class, () -> service.getOrganisationClients("\n"));
        assertThrows(IllegalArgumentException.class, () -> service.getOrganisationClients(null, 0, 10));
        assertThrows(IllegalArgumentException.class, () -> service.getOrganisationClients("", 0, 10));
        assertThrows(IllegalArgumentException.class, () -> service.findByIdentityNoAndIdentityType(null, IndividualIdentityType.OMANG));
        assertThrows(IllegalArgumentException.class, () -> service.findByIdentityNoAndIdentityType(" ", IndividualIdentityType.OMANG));
        assertThrows(IllegalArgumentException.class, () -> service.findByIdentityNoAndIdentityType("123", null));
        assertThrows(IllegalArgumentException.class, () -> service.countByPepStatus(null));
        assertThrows(IllegalArgumentException.class, () -> service.countByKycStatus(null));
        assertThrows(IllegalArgumentException.class, () -> service.countByEmploymentStatus(null));
        assertThrows(IllegalArgumentException.class, () -> service.countBySex(null));
        assertThrows(IllegalArgumentException.class,
            () -> service.loadRequestIndividual(null, "token", "OMANG-1"));
        assertThrows(IllegalArgumentException.class,
            () -> service.loadRequestIndividual(" ", "token", "OMANG-1"));
        assertThrows(IllegalArgumentException.class,
            () -> service.loadRequestIndividual(UUID.randomUUID().toString(), null, "OMANG-1"));
        assertThrows(IllegalArgumentException.class,
            () -> service.loadRequestIndividual(UUID.randomUUID().toString(), "\t", "OMANG-1"));
        assertThrows(IllegalArgumentException.class,
            () -> service.loadRequestIndividual(UUID.randomUUID().toString(), "token", null));
        assertThrows(IllegalArgumentException.class,
            () -> service.loadRequestIndividual(UUID.randomUUID().toString(), "token", "\n"));
        assertThrows(IllegalArgumentException.class, () -> service.findByUserId(null));
        assertThrows(IllegalArgumentException.class, () -> service.findByUserId(" "));
        }

        @Test
        void saveRejectsMissingRequiredFields() {
        IndividualDTO missingFirstName = validIndividual();
        missingFirstName.setFirstName(" ");
        assertThrows(IllegalArgumentException.class, () -> service.save(missingFirstName));

        IndividualDTO missingSurname = validIndividual();
        missingSurname.setSurname(null);
        assertThrows(IllegalArgumentException.class, () -> service.save(missingSurname));

        IndividualDTO missingIdentityNo = validIndividual();
        missingIdentityNo.setIdentityNo("\t");
        assertThrows(IllegalArgumentException.class, () -> service.save(missingIdentityNo));

        IndividualDTO missingIdentityType = validIndividual();
        missingIdentityType.setIdentityType(null);
        assertThrows(IllegalArgumentException.class, () -> service.save(missingIdentityType));

        IndividualDTO missingKycStatus = validIndividual();
        missingKycStatus.setKycStatus(null);
        assertThrows(IllegalArgumentException.class, () -> service.save(missingKycStatus));

        IndividualDTO missingSex = validIndividual();
        missingSex.setSex(null);
        assertThrows(IllegalArgumentException.class, () -> service.save(missingSex));

        IndividualDTO missingNationality = validIndividual();
        missingNationality.setNationality("");
        assertThrows(IllegalArgumentException.class, () -> service.save(missingNationality));

        IndividualDTO missingMaritalStatus = validIndividual();
        missingMaritalStatus.setMaritalStatus(null);
        assertThrows(IllegalArgumentException.class, () -> service.save(missingMaritalStatus));

        IndividualDTO missingEmploymentStatus = validIndividual();
        missingEmploymentStatus.setEmploymentStatus(null);
        assertThrows(IllegalArgumentException.class, () -> service.save(missingEmploymentStatus));
        }

        private static IndividualDTO validIndividual() {
        IndividualDTO dto = new IndividualDTO();
        dto.setFirstName("John");
        dto.setSurname("Doe");
        dto.setIdentityNo("OMANG-100");
        dto.setIdentityType(IndividualIdentityType.OMANG);
        dto.setKycStatus(KycComplianceStatus.CURRENT);
        dto.setSex(Sex.MALE);
        dto.setNationality("BW");
        dto.setMaritalStatus(MaritalStatus.SINGLE);
        dto.setEmploymentStatus(EmploymentStatus.EMPLOYED);
        return dto;
        }

    @SuppressWarnings("unchecked")
    private void evaluateSpecification(Specification<Individual> specification) {
        Root<Individual> root = (Root<Individual>) org.mockito.Mockito.mock(Root.class);
        Path<Object> path = org.mockito.Mockito.mock(Path.class);
        CriteriaQuery<Object> query = (CriteriaQuery<Object>) org.mockito.Mockito.mock(CriteriaQuery.class);
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