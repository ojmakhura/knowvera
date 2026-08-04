package bw.co.kyvera.subscription;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;
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
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import bw.co.kyvera.PropertySearchOrder;
import bw.co.kyvera.SearchObject;
import bw.co.kyvera.SortOrder;
import bw.co.kyvera.TargetEntity;
import bw.co.kyvera.invoice.KycInvoiceMapper;
import bw.co.kyvera.invoice.KycInvoiceRepository;
import bw.co.kyvera.sequence.SequenceGenerator;
import bw.co.kyvera.sequence.SequenceGeneratorRepository;
import bw.co.kyvera.sequence.SequenceGeneratorService;

@ExtendWith(MockitoExtension.class)
class KycSubscriptionServiceImplTest {

    @Mock
    private KycSubscriptionRepository kycSubscriptionRepository;
    @Mock
    private SequenceGeneratorService sequenceGeneratorService;
    @Mock
    private SequenceGeneratorRepository sequenceGeneratorRepository;
    @Mock
    private KycSubscriptionMapper kycSubscriptionMapper;
    @Mock
    private KycInvoiceRepository kycInvoiceRepository;
    @Mock
    private KycInvoiceMapper kycInvoiceMapper;
    @Mock
    private MessageSource messageSource;

    private KycSubscriptionServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new KycSubscriptionServiceImpl(
                kycSubscriptionRepository,
                sequenceGeneratorService,
                sequenceGeneratorRepository,
                kycSubscriptionMapper,
                kycInvoiceRepository,
                kycInvoiceMapper,
                messageSource);
    }

    @Test
    void saveCreatesSequenceDefinitionWhenRefIsBlank() throws Exception {
        KycSubscriptionDTO input = new KycSubscriptionDTO();
        input.setStartDate(new java.util.Date());
        input.setPeriod(bw.co.kyvera.TimePeriod.MONTH);
        input.setOrganisationCode("ORG");
        input.setOrganisationName("Organisation");
        input.setOrganisationRegistrationNo("REG-1");
        KycSubscription entity = KycSubscription.Factory.newInstance();
        KycSubscriptionDTO expected = new KycSubscriptionDTO();

        when(kycSubscriptionMapper.kycSubscriptionDTOToEntity(input)).thenReturn(entity);
        when(sequenceGeneratorRepository.findByName("KYC_SUBSCRIPTION_REF")).thenReturn(Optional.empty());
        when(sequenceGeneratorRepository.save(any(SequenceGenerator.class))).thenAnswer(inv -> inv.getArgument(0));
        when(sequenceGeneratorService.generateNextSequenceValue("KYC_SUBSCRIPTION_REF", true)).thenReturn("SUB-000001");
        when(kycSubscriptionRepository.save(entity)).thenReturn(entity);
        when(kycSubscriptionMapper.toKycSubscriptionDTO(entity)).thenReturn(expected);

        KycSubscriptionDTO actual = service.save(input);

        ArgumentCaptor<SequenceGenerator> captor = ArgumentCaptor.forClass(SequenceGenerator.class);
        verify(sequenceGeneratorRepository).save(captor.capture());
        assertEquals(TargetEntity.SUBSCRIPTION, captor.getValue().getTargetEntity());
        assertEquals("SUB-000001", entity.getRef());
        assertSame(expected, actual);
    }

    @Test
    void findByIdMapsLoadedSubscription() throws Exception {
        UUID id = UUID.randomUUID();
        KycSubscription entity = KycSubscription.Factory.newInstance();
        KycSubscriptionDTO expected = new KycSubscriptionDTO();

        when(kycSubscriptionRepository.findById(id)).thenReturn(Optional.of(entity));
        when(kycSubscriptionMapper.toKycSubscriptionDTO(entity)).thenReturn(expected);

        KycSubscriptionDTO actual = service.findById(id.toString());

        assertSame(expected, actual);
    }

    @Test
    void countByStatusReturnsZeroWhenRepositoryHasNoValue() throws Exception {
        when(kycSubscriptionRepository.countByStatus(KycSubsciptionStatus.ACTIVE)).thenReturn(Optional.empty());

        long actual = service.countByStatus(KycSubsciptionStatus.ACTIVE);

        assertEquals(0L, actual);
    }

    @Test
    void removeDeletesById() throws Exception {
        UUID id = UUID.randomUUID();

        boolean removed = service.remove(id.toString());

        assertTrue(removed);
        verify(kycSubscriptionRepository).deleteById(id);
    }

    @Test
    void saveUsesExistingSequenceGeneratorWhenAlreadyConfigured() throws Exception {
        KycSubscriptionDTO input = new KycSubscriptionDTO();
        input.setStartDate(new java.util.Date());
        input.setPeriod(bw.co.kyvera.TimePeriod.MONTH);
        input.setOrganisationCode("ORG");
        input.setOrganisationName("Organisation");
        input.setOrganisationRegistrationNo("REG-1");
        KycSubscription entity = KycSubscription.Factory.newInstance();
        entity.setRef(null);
        KycSubscriptionDTO expected = new KycSubscriptionDTO();

        when(kycSubscriptionMapper.kycSubscriptionDTOToEntity(input)).thenReturn(entity);
        when(sequenceGeneratorRepository.findByName("KYC_SUBSCRIPTION_REF")).thenReturn(Optional.of(SequenceGenerator.Factory.newInstance()));
        when(sequenceGeneratorService.generateNextSequenceValue("KYC_SUBSCRIPTION_REF", true)).thenReturn("SUB-000222");
        when(kycSubscriptionRepository.save(entity)).thenReturn(entity);
        when(kycSubscriptionMapper.toKycSubscriptionDTO(entity)).thenReturn(expected);

        KycSubscriptionDTO actual = service.save(input);

        assertSame(expected, actual);
        assertEquals("SUB-000222", entity.getRef());
        verify(sequenceGeneratorRepository, never()).save(any(SequenceGenerator.class));
    }

    @Test
    void saveSkipsSequenceGenerationWhenRefAlreadyProvided() throws Exception {
        KycSubscriptionDTO input = new KycSubscriptionDTO();
        input.setStartDate(new java.util.Date());
        input.setPeriod(bw.co.kyvera.TimePeriod.MONTH);
        input.setOrganisationCode("ORG");
        input.setOrganisationName("Organisation");
        input.setOrganisationRegistrationNo("REG-1");
        input.setRef("SUB-CUSTOM");
        KycSubscription entity = KycSubscription.Factory.newInstance();
        entity.setRef("SUB-CUSTOM");
        KycSubscriptionDTO expected = new KycSubscriptionDTO();

        when(kycSubscriptionMapper.kycSubscriptionDTOToEntity(input)).thenReturn(entity);
        when(kycSubscriptionRepository.save(entity)).thenReturn(entity);
        when(kycSubscriptionMapper.toKycSubscriptionDTO(entity)).thenReturn(expected);

        KycSubscriptionDTO actual = service.save(input);

        assertSame(expected, actual);
        verify(sequenceGeneratorRepository, never()).findByName("KYC_SUBSCRIPTION_REF");
        verify(sequenceGeneratorService, never()).generateNextSequenceValue(any(), any(Boolean.class));
    }

    @Test
    void getAllMapsRepositoryResults() throws Exception {
        List<KycSubscription> entities = List.of(KycSubscription.Factory.newInstance());
        List<KycSubscriptionDTO> expected = List.of(new KycSubscriptionDTO());

        when(kycSubscriptionRepository.findAll()).thenReturn(entities);
        when(kycSubscriptionMapper.toKycSubscriptionDTOCollection(entities)).thenReturn(expected);

        List<KycSubscriptionDTO> actual = service.getAll();

        assertSame(expected, actual);
    }

    @Test
    void searchWithDefaultSortReturnsMappedCollection() throws Exception {
        SubscriptionSearchCriteria criteria = new SubscriptionSearchCriteria();
        criteria.setRef("SUB");

        List<KycSubscription> entities = List.of(KycSubscription.Factory.newInstance());
        List<KycSubscriptionDTO> expected = List.of(new KycSubscriptionDTO());

        when(kycSubscriptionRepository.findAll(org.mockito.ArgumentMatchers.<Specification<KycSubscription>>any(), any(Sort.class)))
                .thenReturn(entities);
        when(kycSubscriptionMapper.toKycSubscriptionDTOCollection(entities)).thenReturn(expected);

        List<KycSubscriptionDTO> actual = service.search(criteria, null);

        assertSame(expected, actual);
    }

    @Test
    void searchWithProvidedSortReturnsMappedCollection() throws Exception {
        SubscriptionSearchCriteria criteria = new SubscriptionSearchCriteria();
        Set<PropertySearchOrder> sort = Set.of(new PropertySearchOrder("createdAt", SortOrder.DESC));
        List<KycSubscription> entities = List.of(KycSubscription.Factory.newInstance());
        List<KycSubscriptionDTO> expected = List.of(new KycSubscriptionDTO());

        when(kycSubscriptionRepository.findAll(org.mockito.ArgumentMatchers.<Specification<KycSubscription>>any(), any(Sort.class)))
                .thenReturn(entities);
        when(kycSubscriptionMapper.toKycSubscriptionDTOCollection(entities)).thenReturn(expected);

        List<KycSubscriptionDTO> actual = service.search(criteria, sort);

        assertSame(expected, actual);
    }

    @Test
    void searchEvaluatesAllSpecificationPredicates() throws Exception {
        SubscriptionSearchCriteria criteria = new SubscriptionSearchCriteria();
        criteria.setRef("SUB");
        criteria.setOrganisationName("Acme");
        criteria.setOrganisationRegistrationNo("REG-1");
        criteria.setOrganisatonCode("ORG");
        criteria.setOrganisatonId(UUID.randomUUID().toString());
        criteria.setPeriod(bw.co.kyvera.TimePeriod.MONTH);
        criteria.setStatus(KycSubsciptionStatus.ACTIVE);

        List<KycSubscription> entities = List.of(KycSubscription.Factory.newInstance());
        when(kycSubscriptionRepository.findAll(org.mockito.ArgumentMatchers.<Specification<KycSubscription>>any(), any(Sort.class)))
                .thenAnswer(invocation -> {
                    evaluateSpecification(invocation.getArgument(0));
                    return entities;
                });
        when(kycSubscriptionMapper.toKycSubscriptionDTOCollection(entities)).thenReturn(List.of());

        List<KycSubscriptionDTO> actual = service.search(criteria, Set.of());

        assertTrue(actual.isEmpty());
    }

    @Test
    void getAllWithPagingMapsEachEntity() throws Exception {
        KycSubscription entity = KycSubscription.Factory.newInstance();
        KycSubscriptionDTO dto = new KycSubscriptionDTO();
        Page<KycSubscription> page = new PageImpl<>(List.of(entity));

        when(kycSubscriptionRepository.findAll(PageRequest.of(0, 10))).thenReturn(page);
        when(kycSubscriptionMapper.toKycSubscriptionDTO(entity)).thenReturn(dto);

        Page<KycSubscriptionDTO> actual = service.getAll(0, 10);

        assertEquals(1, actual.getContent().size());
        assertSame(dto, actual.getContent().get(0));
    }

    @Test
    void searchWithPagingUsesProvidedSortings() throws Exception {
        SearchObject<SubscriptionSearchCriteria> criteria = new SearchObject<>();
        criteria.setCriteria(new SubscriptionSearchCriteria());
        criteria.setPageNumber(0);
        criteria.setPageSize(10);
        criteria.setSortings(List.of(new PropertySearchOrder("createdAt", SortOrder.DESC)));

        KycSubscription entity = KycSubscription.Factory.newInstance();
        KycSubscriptionDTO dto = new KycSubscriptionDTO();
        Page<KycSubscription> page = new PageImpl<>(List.of(entity));

        when(kycSubscriptionRepository.findAll(org.mockito.ArgumentMatchers.<Specification<KycSubscription>>any(), any(PageRequest.class)))
                .thenReturn(page);
        when(kycSubscriptionMapper.toKycSubscriptionDTO(entity)).thenReturn(dto);

        Page<KycSubscriptionDTO> actual = service.search(criteria);

        assertEquals(1, actual.getContent().size());
        assertSame(dto, actual.getContent().get(0));
    }

    @Test
    void searchWithPagingUsesDefaultSortWhenSortingsMissing() throws Exception {
        SearchObject<SubscriptionSearchCriteria> criteria = new SearchObject<>();
        criteria.setCriteria(new SubscriptionSearchCriteria());
        criteria.setPageNumber(0);
        criteria.setPageSize(10);
        criteria.setSortings(List.of());

        when(kycSubscriptionRepository.findAll(org.mockito.ArgumentMatchers.<Specification<KycSubscription>>any(), any(PageRequest.class)))
                .thenReturn(Page.empty());

        Page<KycSubscriptionDTO> actual = service.search(criteria);

        assertTrue(actual.isEmpty());
    }

    @Test
    void findByOrganisationReturnsMappedCollection() throws Exception {
        UUID orgId = UUID.randomUUID();
        List<KycSubscription> entities = List.of(KycSubscription.Factory.newInstance());
        List<KycSubscriptionDTO> expected = List.of(new KycSubscriptionDTO());

        when(kycSubscriptionRepository.findAll(org.mockito.ArgumentMatchers.<Specification<KycSubscription>>any()))
                .thenAnswer(invocation -> {
                    evaluateSpecification(invocation.getArgument(0));
                    return entities;
                });
        when(kycSubscriptionMapper.toKycSubscriptionDTOCollection(entities)).thenReturn(expected);

        List<KycSubscriptionDTO> actual = service.findByOrganisation(orgId.toString(), "tester");

        assertSame(expected, actual);
    }

    @Test
    void countByStatusReturnsRepositoryValue() throws Exception {
        when(kycSubscriptionRepository.countByStatus(KycSubsciptionStatus.ACTIVE)).thenReturn(Optional.of(12L));

        long actual = service.countByStatus(KycSubsciptionStatus.ACTIVE);

        assertEquals(12L, actual);
    }

    @Test
    void countReturnsRepositoryCount() throws Exception {
        when(kycSubscriptionRepository.count()).thenReturn(20L);

        long actual = service.count();

        assertEquals(20L, actual);
    }

    @Test
    void serviceBaseGuardsRejectInvalidArguments() {
        assertThrows(IllegalArgumentException.class, () -> service.findById(null));
        assertThrows(IllegalArgumentException.class, () -> service.findById(" "));
        assertThrows(IllegalArgumentException.class, () -> service.save(null));
        assertThrows(IllegalArgumentException.class, () -> service.remove(null));
        assertThrows(IllegalArgumentException.class, () -> service.remove("\n"));
        assertThrows(KycSubscriptionServiceException.class, () -> service.search(null, Set.of()));
        assertThrows(KycSubscriptionServiceException.class, () -> service.search((SearchObject<SubscriptionSearchCriteria>) null));
        assertThrows(IllegalArgumentException.class, () -> service.findByOrganisation(null, "tester"));
        assertThrows(IllegalArgumentException.class, () -> service.findByOrganisation("\t", "tester"));
        assertThrows(IllegalArgumentException.class, () -> service.findByOrganisation(UUID.randomUUID().toString(), null));
        assertThrows(IllegalArgumentException.class, () -> service.findByOrganisation(UUID.randomUUID().toString(), " "));
        assertThrows(IllegalArgumentException.class, () -> service.countByStatus(null));
    }

    @Test
    void serviceBaseSaveGuardRejectsMissingFields() {
        KycSubscriptionDTO missingStartDate = validSubscription();
        missingStartDate.setStartDate(null);
        assertThrows(IllegalArgumentException.class, () -> service.save(missingStartDate));

        KycSubscriptionDTO missingPeriod = validSubscription();
        missingPeriod.setPeriod(null);
        assertThrows(IllegalArgumentException.class, () -> service.save(missingPeriod));

        KycSubscriptionDTO missingOrganisationCode = validSubscription();
        missingOrganisationCode.setOrganisationCode(" ");
        assertThrows(IllegalArgumentException.class, () -> service.save(missingOrganisationCode));

        KycSubscriptionDTO missingOrganisationName = validSubscription();
        missingOrganisationName.setOrganisationName(null);
        assertThrows(IllegalArgumentException.class, () -> service.save(missingOrganisationName));

        KycSubscriptionDTO missingOrganisationRegNo = validSubscription();
        missingOrganisationRegNo.setOrganisationRegistrationNo("\t");
        assertThrows(IllegalArgumentException.class, () -> service.save(missingOrganisationRegNo));
    }

    private static KycSubscriptionDTO validSubscription() {
        KycSubscriptionDTO dto = new KycSubscriptionDTO();
        dto.setStartDate(new java.util.Date());
        dto.setPeriod(bw.co.kyvera.TimePeriod.MONTH);
        dto.setOrganisationCode("ORG");
        dto.setOrganisationName("Organisation");
        dto.setOrganisationRegistrationNo("REG-1");
        return dto;
    }

    @SuppressWarnings("unchecked")
    private void evaluateSpecification(Specification<KycSubscription> specification) {
        Root<KycSubscription> root = (Root<KycSubscription>) org.mockito.Mockito.mock(Root.class);
        Path<Object> path = (Path<Object>) org.mockito.Mockito.mock(Path.class);
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