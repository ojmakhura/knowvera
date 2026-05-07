package bw.co.centralkyc.organisation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
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
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;

import bw.co.centralkyc.GeneralStatus;
import bw.co.centralkyc.PropertySearchOrder;
import bw.co.centralkyc.SearchObject;
import bw.co.centralkyc.SortOrder;
import bw.co.centralkyc.kyc.KycComplianceStatus;
import bw.co.centralkyc.organisation.client.ClientRequestRepository;

@ExtendWith(MockitoExtension.class)
class OrganisationServiceImplTest {

    @Mock
    private OrganisationDao organisationDao;
    @Mock
    private OrganisationRepository organisationRepository;
    @Mock
    private OrganisationMapper organisationMapper;
    @Mock
    private ClientRequestRepository clientRequestRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private MessageSource messageSource;

    private OrganisationServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new OrganisationServiceImpl(
                organisationDao,
                organisationRepository,
                organisationMapper,
                clientRequestRepository,
                passwordEncoder,
                messageSource);
    }

    @Test
    void handleFindByIdReloadsAndMapsOrganisation() throws Exception {
        UUID id = UUID.randomUUID();
        Organisation organisation = Organisation.Factory.newInstance();
        OrganisationDTO expected = new OrganisationDTO();

        when(organisationRepository.getReferenceById(id)).thenReturn(organisation);
        when(organisationRepository.save(organisation)).thenReturn(organisation);
        when(organisationMapper.toOrganisationDTO(organisation)).thenReturn(expected);

        OrganisationDTO actual = service.handleFindById(id.toString());

        assertSame(expected, actual);
        verify(organisationRepository).save(organisation);
    }

    @Test
    void handleRemoveReturnsFalseWhenOrganisationDoesNotExist() throws Exception {
        UUID id = UUID.randomUUID();
        when(organisationRepository.existsById(id)).thenReturn(false);

        boolean removed = service.handleRemove(id.toString());

        assertFalse(removed);
    }

    @Test
    void handleRemoveDeletesWhenOrganisationExists() throws Exception {
        UUID id = UUID.randomUUID();
        when(organisationRepository.existsById(id)).thenReturn(true);

        boolean removed = service.handleRemove(id.toString());

        assertTrue(removed);
        verify(organisationRepository).deleteById(id);
    }

    @Test
    void handleFindByCodeReturnsNullWhenCodeIsMissing() throws Exception {
        when(organisationRepository.findByCode("ORG-1")).thenReturn(Optional.empty());

        OrganisationDTO actual = service.handleFindByCode("ORG-1");

        assertNull(actual);
    }

    @Test
    void handleCountByStatusReturnsZeroWhenRepositoryHasNoValue() throws Exception {
        when(organisationRepository.countByStatus(GeneralStatus.ACTIVE)).thenReturn(Optional.empty());

        long actual = service.handleCountByStatus(GeneralStatus.ACTIVE);

        assertEquals(0L, actual);
    }

    @Test
    void handleGetAllMapsAllEntities() throws Exception {
        List<Organisation> entities = List.of(Organisation.Factory.newInstance());
        List<OrganisationListDTO> expected = List.of(new OrganisationListDTO("ORG", "Acme", "REG-1", GeneralStatus.ACTIVE,
            bw.co.centralkyc.kyc.KycComplianceStatus.CURRENT));

        when(organisationRepository.findAll()).thenReturn(entities);
        when(organisationMapper.toOrganisationListDTOCollection(entities)).thenReturn(expected);

        List<OrganisationListDTO> actual = service.handleGetAll();

        assertSame(expected, actual);
    }

    @Test
    void handleGetAllWithPagingCurrentlyReturnsNull() throws Exception {
        assertNull(service.handleGetAll(0, 10));
    }

    @Test
    void handleSaveMapsPersistsAndMapsBack() throws Exception {
        OrganisationDTO input = new OrganisationDTO();
        Organisation entity = Organisation.Factory.newInstance();
        OrganisationDTO expected = new OrganisationDTO();

        when(organisationMapper.organisationDTOToEntity(input)).thenReturn(entity);
        when(organisationRepository.save(entity)).thenReturn(entity);
        when(organisationMapper.toOrganisationDTO(entity)).thenReturn(expected);

        OrganisationDTO actual = service.handleSave(input);

        assertSame(expected, actual);
    }

    @Test
    void handleSearchDelegatesToRepositoryAndMapper() throws Exception {
        OrganisationSearchCriteria criteria = new OrganisationSearchCriteria();
        criteria.setName("Acme");

        List<Organisation> entities = List.of(Organisation.Factory.newInstance());
        List<OrganisationListDTO> expected = List.of(new OrganisationListDTO("ORG", "Acme", "REG-1", GeneralStatus.ACTIVE,
            bw.co.centralkyc.kyc.KycComplianceStatus.CURRENT));

        when(organisationRepository.findAll(org.mockito.ArgumentMatchers.<Specification<Organisation>>any())).thenReturn(entities);
        when(organisationMapper.toOrganisationListDTOCollection(entities)).thenReturn(expected);

        List<OrganisationListDTO> actual = service.handleSearch(criteria, Set.of(new PropertySearchOrder("name", SortOrder.ASC)));

        assertSame(expected, actual);
    }

    @Test
    void handleSearchWithPagingMapsResults() throws Exception {
        OrganisationSearchCriteria orgCriteria = new OrganisationSearchCriteria();
        orgCriteria.setRegistrationNo("BW-1");
        SearchObject<OrganisationSearchCriteria> criteria = new SearchObject<>();
        criteria.setCriteria(orgCriteria);
        criteria.setPageNumber(0);
        criteria.setPageSize(5);

        Organisation entity = Organisation.Factory.newInstance();
        Page<Organisation> page = new PageImpl<>(List.of(entity));
        OrganisationListDTO mapped = new OrganisationListDTO("ORG", "Acme", "REG-1", GeneralStatus.ACTIVE,
            bw.co.centralkyc.kyc.KycComplianceStatus.CURRENT);

        when(organisationRepository.findAll(org.mockito.ArgumentMatchers.<Specification<Organisation>>any(), any(org.springframework.data.domain.Pageable.class))).thenReturn(page);
        when(organisationMapper.toOrganisationListDTO(entity)).thenReturn(mapped);

        Page<OrganisationListDTO> actual = service.handleSearch(criteria);

        assertEquals(1, actual.getContent().size());
        assertSame(mapped, actual.getContent().get(0));
    }

    @Test
    void handleSearchWithPagingUsesDefaultPagingWhenInvalidInput() throws Exception {
        SearchObject<OrganisationSearchCriteria> criteria = new SearchObject<>();
        criteria.setCriteria(new OrganisationSearchCriteria());
        criteria.setPageNumber(-1);
        criteria.setPageSize(0);

        when(organisationRepository.findAll(org.mockito.ArgumentMatchers.<Specification<Organisation>>any(), any(org.springframework.data.domain.Pageable.class)))
                .thenReturn(Page.empty());

        Page<OrganisationListDTO> actual = service.handleSearch(criteria);

        assertTrue(actual.isEmpty());
    }

    @Test
    void handleSearchEvaluatesAllSpecificationPredicates() throws Exception {
        OrganisationSearchCriteria criteria = new OrganisationSearchCriteria();
        criteria.setId(UUID.randomUUID().toString());
        criteria.setName("Acme");
        criteria.setRegistrationNo("REG-9");
        criteria.setContactEmailAddress("mail@acme.com");
        criteria.setStatus(GeneralStatus.ACTIVE);
        criteria.setIsClient(Boolean.TRUE);

        List<Organisation> entities = List.of(Organisation.Factory.newInstance());
        when(organisationRepository.findAll(org.mockito.ArgumentMatchers.<Specification<Organisation>>any()))
                .thenAnswer(invocation -> {
                    evaluateSpecification(invocation.getArgument(0));
                    return entities;
                });
        when(organisationMapper.toOrganisationListDTOCollection(entities)).thenReturn(List.of());

        List<OrganisationListDTO> actual = service.handleSearch(criteria, Set.of());

        assertTrue(actual.isEmpty());
    }

    @Test
    void handleCountByKycStatusReturnsRepositoryValueOrZero() throws Exception {
        when(organisationRepository.countByKycStatus(KycComplianceStatus.CURRENT)).thenReturn(Optional.of(6L));
        when(organisationRepository.countByKycStatus(KycComplianceStatus.EXPIRED)).thenReturn(Optional.empty());

        assertEquals(6L, service.handleCountByKycStatus(KycComplianceStatus.CURRENT));
        assertEquals(0L, service.handleCountByKycStatus(KycComplianceStatus.EXPIRED));
    }

    @Test
    void handleCountByStatusReturnsRepositoryValue() throws Exception {
        when(organisationRepository.countByStatus(GeneralStatus.ACTIVE)).thenReturn(Optional.of(8L));

        assertEquals(8L, service.handleCountByStatus(GeneralStatus.ACTIVE));
    }

    @Test
    void handleCountByIsClientFalseReturnsRepositoryValueOrZero() throws Exception {
        when(organisationRepository.countByIsClientFalse()).thenReturn(Optional.of(5L));
        assertEquals(5L, service.handleCountByIsClientFalse());

        when(organisationRepository.countByIsClientFalse()).thenReturn(Optional.empty());
        assertEquals(0L, service.handleCountByIsClientFalse());
    }

    @Test
    void handleCountByIsClientTrueReturnsRepositoryValueOrZero() throws Exception {
        when(organisationRepository.countByIsClientTrue()).thenReturn(Optional.of(4L));
        assertEquals(4L, service.handleCountByIsClientTrue());

        when(organisationRepository.countByIsClientTrue()).thenReturn(Optional.empty());
        assertEquals(0L, service.handleCountByIsClientTrue());
    }

    @Test
    void handleCountReturnsRepositoryCount() throws Exception {
        when(organisationRepository.count()).thenReturn(11L);

        assertEquals(11L, service.handleCount());
    }

    @Test
    void handleLoadRequestOrganisationThrowsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class,
                () -> service.handleLoadRequestOrganisation(UUID.randomUUID().toString(), "token", "reg"));
    }

    @Test
    void handleFindByCodeReturnsMappedWhenFound() throws Exception {
        Organisation entity = Organisation.Factory.newInstance();
        OrganisationDTO expected = new OrganisationDTO();

        when(organisationRepository.findByCode("ORG-1")).thenReturn(Optional.of(entity));
        when(organisationMapper.toOrganisationDTO(entity)).thenReturn(expected);

        OrganisationDTO actual = service.handleFindByCode("ORG-1");

        assertSame(expected, actual);
    }

    @Test
    void handleFindByNameReturnsMappedOrNull() throws Exception {
        Organisation entity = Organisation.Factory.newInstance();
        OrganisationDTO expected = new OrganisationDTO();

        when(organisationRepository.findByName("Acme")).thenReturn(Optional.of(entity));
        when(organisationMapper.toOrganisationDTO(entity)).thenReturn(expected);
        when(organisationRepository.findByName("Missing")).thenReturn(Optional.empty());

        assertSame(expected, service.handleFindByName("Acme"));
        assertNull(service.handleFindByName("Missing"));
    }

    @Test
    void handleFindByRegistrationNoReturnsMappedOrNull() throws Exception {
        Organisation entity = Organisation.Factory.newInstance();
        OrganisationDTO expected = new OrganisationDTO();

        when(organisationRepository.findByRegistrationNo("REG-1")).thenReturn(Optional.of(entity));
        when(organisationMapper.toOrganisationDTO(entity)).thenReturn(expected);
        when(organisationRepository.findByRegistrationNo("MISSING")).thenReturn(Optional.empty());

        assertSame(expected, service.handleFindByRegistrationNo("REG-1"));
        assertNull(service.handleFindByRegistrationNo("MISSING"));
    }

    private void evaluateSpecification(Specification<Organisation> specification) {
        Root<Organisation> root = org.mockito.Mockito.mock(Root.class);
        @SuppressWarnings("unchecked")
        Path<Object> path = org.mockito.Mockito.mock(Path.class);
        CriteriaQuery<Object> query = org.mockito.Mockito.mock(CriteriaQuery.class);
        CriteriaBuilder builder = org.mockito.Mockito.mock(CriteriaBuilder.class);
        Predicate predicate = org.mockito.Mockito.mock(Predicate.class);

        org.mockito.Mockito.lenient().when(root.get(any(String.class))).thenReturn(path);
        org.mockito.Mockito.lenient().when(path.get(any(String.class))).thenReturn(path);
        org.mockito.Mockito.lenient().when(builder.conjunction()).thenReturn(predicate);
        org.mockito.Mockito.lenient().when(builder.upper(any())).thenAnswer(invocation -> invocation.getArgument(0));
        org.mockito.Mockito.lenient().when(builder.like(any(), any(String.class))).thenReturn(predicate);
        org.mockito.Mockito.lenient().when(builder.equal(any(), any())).thenReturn(predicate);
        org.mockito.Mockito.lenient().when(builder.and(any(Predicate.class), any(Predicate.class))).thenReturn(predicate);

        specification.toPredicate(root, query, builder);
    }
}