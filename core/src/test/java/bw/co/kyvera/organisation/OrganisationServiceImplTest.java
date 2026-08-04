package bw.co.kyvera.organisation;

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

import bw.co.kyvera.GeneralStatus;
import bw.co.kyvera.PropertySearchOrder;
import bw.co.kyvera.SearchObject;
import bw.co.kyvera.SortOrder;
import bw.co.kyvera.kyc.KycComplianceStatus;
import bw.co.kyvera.organisation.client.ClientRequestRepository;

@ExtendWith(MockitoExtension.class)
class OrganisationServiceImplTest {

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
                organisationRepository,
                organisationMapper,
                clientRequestRepository,
                passwordEncoder,
                messageSource);
    }

    @Test
    void findByIdReloadsAndMapsOrganisation() throws Exception {
        UUID id = UUID.randomUUID();
        Organisation organisation = Organisation.Factory.newInstance();
        OrganisationDTO expected = new OrganisationDTO();

        when(organisationRepository.getReferenceById(id)).thenReturn(organisation);
        when(organisationRepository.save(organisation)).thenReturn(organisation);
        when(organisationMapper.toOrganisationDTO(organisation)).thenReturn(expected);

        OrganisationDTO actual = service.findById(id.toString());

        assertSame(expected, actual);
        verify(organisationRepository).save(organisation);
    }

    @Test
    void removeReturnsFalseWhenOrganisationDoesNotExist() throws Exception {
        UUID id = UUID.randomUUID();
        when(organisationRepository.existsById(id)).thenReturn(false);

        boolean removed = service.remove(id.toString());

        assertFalse(removed);
    }

    @Test
    void removeDeletesWhenOrganisationExists() throws Exception {
        UUID id = UUID.randomUUID();
        when(organisationRepository.existsById(id)).thenReturn(true);

        boolean removed = service.remove(id.toString());

        assertTrue(removed);
        verify(organisationRepository).deleteById(id);
    }

    @Test
    void findByCodeReturnsNullWhenCodeIsMissing() throws Exception {
        when(organisationRepository.findByCode("ORG-1")).thenReturn(Optional.empty());

        OrganisationDTO actual = service.findByCode("ORG-1");

        assertNull(actual);
    }

    @Test
    void countByStatusReturnsZeroWhenRepositoryHasNoValue() throws Exception {
        when(organisationRepository.countByStatus(GeneralStatus.ACTIVE)).thenReturn(Optional.empty());

        long actual = service.countByStatus(GeneralStatus.ACTIVE);

        assertEquals(0L, actual);
    }

    @Test
    void getAllMapsAllEntities() throws Exception {
        List<Organisation> entities = List.of(Organisation.Factory.newInstance());
        List<OrganisationListDTO> expected = List.of(new OrganisationListDTO("ORG", "Acme", "REG-1", GeneralStatus.ACTIVE,
            bw.co.kyvera.kyc.KycComplianceStatus.CURRENT));

        when(organisationRepository.findAll()).thenReturn(entities);
        when(organisationMapper.toOrganisationListDTOCollection(entities)).thenReturn(expected);

        List<OrganisationListDTO> actual = service.getAll();

        assertSame(expected, actual);
    }

    @Test
    void getAllWithPagingCurrentlyReturnsNull() throws Exception {
        assertNull(service.getAll(0, 10));
    }

    @Test
    void saveMapsPersistsAndMapsBack() throws Exception {
        OrganisationDTO input = new OrganisationDTO();
        input.setCode("ORG-1");
        input.setName("Organisation");
        input.setRegistrationNo("REG-1");
        input.setStatus(GeneralStatus.ACTIVE);
        input.setDomains(new java.util.ArrayList<>());
        input.setKycStatus(KycComplianceStatus.CURRENT);
        input.setCountryOfRegistration("BW");
        Organisation entity = Organisation.Factory.newInstance();
        OrganisationDTO expected = new OrganisationDTO();

        when(organisationMapper.organisationDTOToEntity(input)).thenReturn(entity);
        when(organisationRepository.save(entity)).thenReturn(entity);
        when(organisationMapper.toOrganisationDTO(entity)).thenReturn(expected);

        OrganisationDTO actual = service.save(input);

        assertSame(expected, actual);
    }

    @Test
    void searchDelegatesToRepositoryAndMapper() throws Exception {
        OrganisationSearchCriteria criteria = new OrganisationSearchCriteria();
        criteria.setName("Acme");

        List<Organisation> entities = List.of(Organisation.Factory.newInstance());
        List<OrganisationListDTO> expected = List.of(new OrganisationListDTO("ORG", "Acme", "REG-1", GeneralStatus.ACTIVE,
            bw.co.kyvera.kyc.KycComplianceStatus.CURRENT));

        when(organisationRepository.findAll(org.mockito.ArgumentMatchers.<Specification<Organisation>>any())).thenReturn(entities);
        when(organisationMapper.toOrganisationListDTOCollection(entities)).thenReturn(expected);

        List<OrganisationListDTO> actual = service.search(criteria, Set.of(new PropertySearchOrder("name", SortOrder.ASC)));

        assertSame(expected, actual);
    }

    @Test
    void searchWithPagingMapsResults() throws Exception {
        OrganisationSearchCriteria orgCriteria = new OrganisationSearchCriteria();
        orgCriteria.setRegistrationNo("BW-1");
        SearchObject<OrganisationSearchCriteria> criteria = new SearchObject<>();
        criteria.setCriteria(orgCriteria);
        criteria.setPageNumber(0);
        criteria.setPageSize(5);

        Organisation entity = Organisation.Factory.newInstance();
        Page<Organisation> page = new PageImpl<>(List.of(entity));
        OrganisationListDTO mapped = new OrganisationListDTO("ORG", "Acme", "REG-1", GeneralStatus.ACTIVE,
            bw.co.kyvera.kyc.KycComplianceStatus.CURRENT);

        when(organisationRepository.findAll(org.mockito.ArgumentMatchers.<Specification<Organisation>>any(), any(org.springframework.data.domain.Pageable.class))).thenReturn(page);
        when(organisationMapper.toOrganisationListDTO(entity)).thenReturn(mapped);

        Page<OrganisationListDTO> actual = service.search(criteria);

        assertEquals(1, actual.getContent().size());
        assertSame(mapped, actual.getContent().get(0));
    }

    @Test
    void searchWithPagingUsesDefaultPagingWhenInvalidInput() throws Exception {
        SearchObject<OrganisationSearchCriteria> criteria = new SearchObject<>();
        criteria.setCriteria(new OrganisationSearchCriteria());
        criteria.setPageNumber(-1);
        criteria.setPageSize(0);

        when(organisationRepository.findAll(org.mockito.ArgumentMatchers.<Specification<Organisation>>any(), any(org.springframework.data.domain.Pageable.class)))
                .thenReturn(Page.empty());

        Page<OrganisationListDTO> actual = service.search(criteria);

        assertTrue(actual.isEmpty());
    }

    @Test
    void searchEvaluatesAllSpecificationPredicates() throws Exception {
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

        List<OrganisationListDTO> actual = service.search(criteria, Set.of());

        assertTrue(actual.isEmpty());
    }

    @Test
    void countByKycStatusReturnsRepositoryValueOrZero() throws Exception {
        when(organisationRepository.countByKycStatus(KycComplianceStatus.CURRENT)).thenReturn(Optional.of(6L));
        when(organisationRepository.countByKycStatus(KycComplianceStatus.EXPIRED)).thenReturn(Optional.empty());

        assertEquals(6L, service.countByKycStatus(KycComplianceStatus.CURRENT));
        assertEquals(0L, service.countByKycStatus(KycComplianceStatus.EXPIRED));
    }

    @Test
    void countByStatusReturnsRepositoryValue() throws Exception {
        when(organisationRepository.countByStatus(GeneralStatus.ACTIVE)).thenReturn(Optional.of(8L));

        assertEquals(8L, service.countByStatus(GeneralStatus.ACTIVE));
    }

    @Test
    void countByIsClientFalseReturnsRepositoryValueOrZero() throws Exception {
        when(organisationRepository.countByIsClientFalse()).thenReturn(Optional.of(5L));
        assertEquals(5L, service.countByIsClientFalse());

        when(organisationRepository.countByIsClientFalse()).thenReturn(Optional.empty());
        assertEquals(0L, service.countByIsClientFalse());
    }

    @Test
    void countByIsClientTrueReturnsRepositoryValueOrZero() throws Exception {
        when(organisationRepository.countByIsClientTrue()).thenReturn(Optional.of(4L));
        assertEquals(4L, service.countByIsClientTrue());

        when(organisationRepository.countByIsClientTrue()).thenReturn(Optional.empty());
        assertEquals(0L, service.countByIsClientTrue());
    }

    @Test
    void countReturnsRepositoryCount() throws Exception {
        when(organisationRepository.count()).thenReturn(11L);

        assertEquals(11L, service.count());
    }

    @Test
    void loadRequestOrganisationThrowsUnsupportedOperationException() {
        assertThrows(OrganisationServiceException.class,
                () -> service.loadRequestOrganisation(UUID.randomUUID().toString(), "token", "reg"));
    }

    @Test
    void findByCodeReturnsMappedWhenFound() throws Exception {
        Organisation entity = Organisation.Factory.newInstance();
        OrganisationDTO expected = new OrganisationDTO();

        when(organisationRepository.findByCode("ORG-1")).thenReturn(Optional.of(entity));
        when(organisationMapper.toOrganisationDTO(entity)).thenReturn(expected);

        OrganisationDTO actual = service.findByCode("ORG-1");

        assertSame(expected, actual);
    }

    @Test
    void findByNameReturnsMappedOrNull() throws Exception {
        Organisation entity = Organisation.Factory.newInstance();
        OrganisationDTO expected = new OrganisationDTO();

        when(organisationRepository.findByName("Acme")).thenReturn(Optional.of(entity));
        when(organisationMapper.toOrganisationDTO(entity)).thenReturn(expected);
        when(organisationRepository.findByName("Missing")).thenReturn(Optional.empty());

        assertSame(expected, service.findByName("Acme"));
        assertNull(service.findByName("Missing"));
    }

    @Test
    void findByRegistrationNoReturnsMappedOrNull() throws Exception {
        Organisation entity = Organisation.Factory.newInstance();
        OrganisationDTO expected = new OrganisationDTO();

        when(organisationRepository.findByRegistrationNo("REG-1")).thenReturn(Optional.of(entity));
        when(organisationMapper.toOrganisationDTO(entity)).thenReturn(expected);
        when(organisationRepository.findByRegistrationNo("MISSING")).thenReturn(Optional.empty());

        assertSame(expected, service.findByRegistrationNo("REG-1"));
        assertNull(service.findByRegistrationNo("MISSING"));
    }

        @Test
        void serviceBaseGuardsRejectInvalidArguments() {
        assertThrows(IllegalArgumentException.class, () -> service.findById(null));
        assertThrows(IllegalArgumentException.class, () -> service.findById(" "));
        assertThrows(IllegalArgumentException.class, () -> service.remove(null));
        assertThrows(IllegalArgumentException.class, () -> service.remove("\t"));
        assertThrows(IllegalArgumentException.class, () -> service.save(null));
        assertThrows(OrganisationServiceException.class, () -> service.search((SearchObject<OrganisationSearchCriteria>) null));
        assertThrows(IllegalArgumentException.class, () -> service.countByKycStatus(null));
        assertThrows(IllegalArgumentException.class, () -> service.countByStatus(null));
        assertThrows(IllegalArgumentException.class,
            () -> service.loadRequestOrganisation(null, "token", "REG-1"));
        assertThrows(IllegalArgumentException.class,
            () -> service.loadRequestOrganisation(" ", "token", "REG-1"));
        assertThrows(IllegalArgumentException.class,
            () -> service.loadRequestOrganisation(UUID.randomUUID().toString(), null, "REG-1"));
        assertThrows(IllegalArgumentException.class,
            () -> service.loadRequestOrganisation(UUID.randomUUID().toString(), " ", "REG-1"));
        assertThrows(IllegalArgumentException.class,
            () -> service.loadRequestOrganisation(UUID.randomUUID().toString(), "token", null));
        assertThrows(IllegalArgumentException.class,
            () -> service.loadRequestOrganisation(UUID.randomUUID().toString(), "token", "\t"));
        assertThrows(IllegalArgumentException.class, () -> service.findByCode(null));
        assertThrows(IllegalArgumentException.class, () -> service.findByCode(""));
        assertThrows(IllegalArgumentException.class, () -> service.findByName(null));
        assertThrows(IllegalArgumentException.class, () -> service.findByName("\n"));
        assertThrows(IllegalArgumentException.class, () -> service.findByRegistrationNo(null));
        assertThrows(IllegalArgumentException.class, () -> service.findByRegistrationNo(" "));
        }

        @Test
        void serviceBaseSaveGuardRejectsMissingRequiredFields() {
        OrganisationDTO missingCode = validOrganisation();
        missingCode.setCode(" ");
        assertThrows(IllegalArgumentException.class, () -> service.save(missingCode));

        OrganisationDTO missingName = validOrganisation();
        missingName.setName(null);
        assertThrows(IllegalArgumentException.class, () -> service.save(missingName));

        OrganisationDTO missingRegistration = validOrganisation();
        missingRegistration.setRegistrationNo("\t");
        assertThrows(IllegalArgumentException.class, () -> service.save(missingRegistration));

        OrganisationDTO missingStatus = validOrganisation();
        missingStatus.setStatus(null);
        assertThrows(IllegalArgumentException.class, () -> service.save(missingStatus));

        OrganisationDTO missingKycStatus = validOrganisation();
        missingKycStatus.setKycStatus(null);
        assertThrows(IllegalArgumentException.class, () -> service.save(missingKycStatus));

        OrganisationDTO missingCountry = validOrganisation();
        missingCountry.setCountryOfRegistration("");
        assertThrows(IllegalArgumentException.class, () -> service.save(missingCountry));
        }

        private static OrganisationDTO validOrganisation() {
        OrganisationDTO dto = new OrganisationDTO();
        dto.setCode("ORG-1");
        dto.setName("Organisation");
        dto.setRegistrationNo("REG-1");
        dto.setStatus(GeneralStatus.ACTIVE);
        dto.setDomains(new java.util.ArrayList<>());
        dto.setKycStatus(KycComplianceStatus.CURRENT);
        dto.setCountryOfRegistration("BW");
        return dto;
        }

    @SuppressWarnings("unchecked")
    private void evaluateSpecification(Specification<Organisation> specification) {
        Root<Organisation> root = (Root<Organisation>) org.mockito.Mockito.mock(Root.class);
        Path<Object> path = (Path<Object>) org.mockito.Mockito.mock(Path.class);
        CriteriaQuery<Object> query = (CriteriaQuery<Object>) org.mockito.Mockito.mock(CriteriaQuery.class);
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