package bw.co.centralkyc.subscription;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
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

import bw.co.centralkyc.PropertySearchOrder;
import bw.co.centralkyc.SearchObject;
import bw.co.centralkyc.SortOrder;
import bw.co.centralkyc.TargetEntity;
import bw.co.centralkyc.invoice.KycInvoiceDao;
import bw.co.centralkyc.invoice.KycInvoiceMapper;
import bw.co.centralkyc.invoice.KycInvoiceRepository;
import bw.co.centralkyc.sequence.SequenceGenerator;
import bw.co.centralkyc.sequence.SequenceGeneratorRepository;
import bw.co.centralkyc.sequence.SequenceGeneratorService;

@ExtendWith(MockitoExtension.class)
class KycSubscriptionServiceImplTest {

    @Mock
    private KycSubscriptionDao kycSubscriptionDao;
    @Mock
    private KycSubscriptionRepository kycSubscriptionRepository;
    @Mock
    private KycInvoiceDao kycInvoiceDao;
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
                kycSubscriptionDao,
                kycSubscriptionRepository,
                kycInvoiceDao,
                sequenceGeneratorService,
                sequenceGeneratorRepository,
                kycSubscriptionMapper,
                kycInvoiceRepository,
                kycInvoiceMapper,
                messageSource);
    }

    @Test
    void handleSaveCreatesSequenceDefinitionWhenRefIsBlank() throws Exception {
        KycSubscriptionDTO input = new KycSubscriptionDTO();
        KycSubscription entity = KycSubscription.Factory.newInstance();
        KycSubscriptionDTO expected = new KycSubscriptionDTO();

        when(kycSubscriptionDao.kycSubscriptionDTOToEntity(input)).thenReturn(entity);
        when(sequenceGeneratorRepository.findByName("KYC_SUBSCRIPTION_REF")).thenReturn(Optional.empty());
        when(sequenceGeneratorRepository.save(any(SequenceGenerator.class))).thenAnswer(inv -> inv.getArgument(0));
        when(sequenceGeneratorService.generateNextSequenceValue("KYC_SUBSCRIPTION_REF", true)).thenReturn("SUB-000001");
        when(kycSubscriptionRepository.save(entity)).thenReturn(entity);
        when(kycSubscriptionDao.toKycSubscriptionDTO(entity)).thenReturn(expected);

        KycSubscriptionDTO actual = service.handleSave(input);

        ArgumentCaptor<SequenceGenerator> captor = ArgumentCaptor.forClass(SequenceGenerator.class);
        verify(sequenceGeneratorRepository).save(captor.capture());
        assertEquals(TargetEntity.SUBSCRIPTION, captor.getValue().getTargetEntity());
        assertEquals("SUB-000001", entity.getRef());
        assertSame(expected, actual);
    }

    @Test
    void handleFindByIdMapsLoadedSubscription() throws Exception {
        UUID id = UUID.randomUUID();
        KycSubscription entity = KycSubscription.Factory.newInstance();
        KycSubscriptionDTO expected = new KycSubscriptionDTO();

        when(kycSubscriptionRepository.findById(id)).thenReturn(Optional.of(entity));
        when(kycSubscriptionDao.toKycSubscriptionDTO(entity)).thenReturn(expected);

        KycSubscriptionDTO actual = service.handleFindById(id.toString());

        assertSame(expected, actual);
    }

    @Test
    void handleCountByStatusReturnsZeroWhenRepositoryHasNoValue() throws Exception {
        when(kycSubscriptionRepository.countByStatus(KycSubsciptionStatus.ACTIVE)).thenReturn(Optional.empty());

        long actual = service.handleCountByStatus(KycSubsciptionStatus.ACTIVE);

        assertEquals(0L, actual);
    }

    @Test
    void handleRemoveDeletesById() throws Exception {
        UUID id = UUID.randomUUID();

        boolean removed = service.handleRemove(id.toString());

        assertTrue(removed);
        verify(kycSubscriptionRepository).deleteById(id);
    }

    @Test
    void handleSaveUsesExistingSequenceGeneratorWhenAlreadyConfigured() throws Exception {
        KycSubscriptionDTO input = new KycSubscriptionDTO();
        KycSubscription entity = KycSubscription.Factory.newInstance();
        entity.setRef(null);
        KycSubscriptionDTO expected = new KycSubscriptionDTO();

        when(kycSubscriptionDao.kycSubscriptionDTOToEntity(input)).thenReturn(entity);
        when(sequenceGeneratorRepository.findByName("KYC_SUBSCRIPTION_REF")).thenReturn(Optional.of(SequenceGenerator.Factory.newInstance()));
        when(sequenceGeneratorService.generateNextSequenceValue("KYC_SUBSCRIPTION_REF", true)).thenReturn("SUB-000222");
        when(kycSubscriptionRepository.save(entity)).thenReturn(entity);
        when(kycSubscriptionDao.toKycSubscriptionDTO(entity)).thenReturn(expected);

        KycSubscriptionDTO actual = service.handleSave(input);

        assertSame(expected, actual);
        assertEquals("SUB-000222", entity.getRef());
        verify(sequenceGeneratorRepository, never()).save(any(SequenceGenerator.class));
    }

    @Test
    void handleSaveSkipsSequenceGenerationWhenRefAlreadyProvided() throws Exception {
        KycSubscriptionDTO input = new KycSubscriptionDTO();
        input.setRef("SUB-CUSTOM");
        KycSubscription entity = KycSubscription.Factory.newInstance();
        entity.setRef("SUB-CUSTOM");
        KycSubscriptionDTO expected = new KycSubscriptionDTO();

        when(kycSubscriptionDao.kycSubscriptionDTOToEntity(input)).thenReturn(entity);
        when(kycSubscriptionRepository.save(entity)).thenReturn(entity);
        when(kycSubscriptionDao.toKycSubscriptionDTO(entity)).thenReturn(expected);

        KycSubscriptionDTO actual = service.handleSave(input);

        assertSame(expected, actual);
        verify(sequenceGeneratorRepository, never()).findByName("KYC_SUBSCRIPTION_REF");
        verify(sequenceGeneratorService, never()).generateNextSequenceValue(any(), any(Boolean.class));
    }

    @Test
    void handleGetAllMapsRepositoryResults() throws Exception {
        List<KycSubscription> entities = List.of(KycSubscription.Factory.newInstance());
        List<KycSubscriptionDTO> expected = List.of(new KycSubscriptionDTO());

        when(kycSubscriptionRepository.findAll()).thenReturn(entities);
        when(kycSubscriptionMapper.toKycSubscriptionDTOCollection(entities)).thenReturn(expected);

        List<KycSubscriptionDTO> actual = service.handleGetAll();

        assertSame(expected, actual);
    }

    @Test
    void handleSearchWithDefaultSortReturnsMappedCollection() throws Exception {
        SubscriptionSearchCriteria criteria = new SubscriptionSearchCriteria();
        criteria.setRef("SUB");

        List<KycSubscription> entities = List.of(KycSubscription.Factory.newInstance());
        List<KycSubscriptionDTO> expected = List.of(new KycSubscriptionDTO());

        when(kycSubscriptionRepository.findAll(org.mockito.ArgumentMatchers.<Specification<KycSubscription>>any(), any(Sort.class)))
                .thenReturn(entities);
        when(kycSubscriptionMapper.toKycSubscriptionDTOCollection(entities)).thenReturn(expected);

        List<KycSubscriptionDTO> actual = service.handleSearch(criteria, null);

        assertSame(expected, actual);
    }

    @Test
    void handleSearchWithProvidedSortReturnsMappedCollection() throws Exception {
        SubscriptionSearchCriteria criteria = new SubscriptionSearchCriteria();
        Set<PropertySearchOrder> sort = Set.of(new PropertySearchOrder("createdAt", SortOrder.DESC));
        List<KycSubscription> entities = List.of(KycSubscription.Factory.newInstance());
        List<KycSubscriptionDTO> expected = List.of(new KycSubscriptionDTO());

        when(kycSubscriptionRepository.findAll(org.mockito.ArgumentMatchers.<Specification<KycSubscription>>any(), any(Sort.class)))
                .thenReturn(entities);
        when(kycSubscriptionMapper.toKycSubscriptionDTOCollection(entities)).thenReturn(expected);

        List<KycSubscriptionDTO> actual = service.handleSearch(criteria, sort);

        assertSame(expected, actual);
    }

    @Test
    void handleSearchEvaluatesAllSpecificationPredicates() throws Exception {
        SubscriptionSearchCriteria criteria = new SubscriptionSearchCriteria();
        criteria.setRef("SUB");
        criteria.setOrganisationName("Acme");
        criteria.setOrganisationRegistrationNo("REG-1");
        criteria.setOrganisatonCode("ORG");
        criteria.setOrganisatonId(UUID.randomUUID().toString());
        criteria.setPeriod(bw.co.centralkyc.TimePeriod.MONTH);
        criteria.setStatus(KycSubsciptionStatus.ACTIVE);

        List<KycSubscription> entities = List.of(KycSubscription.Factory.newInstance());
        when(kycSubscriptionRepository.findAll(org.mockito.ArgumentMatchers.<Specification<KycSubscription>>any(), any(Sort.class)))
                .thenAnswer(invocation -> {
                    evaluateSpecification(invocation.getArgument(0));
                    return entities;
                });
        when(kycSubscriptionMapper.toKycSubscriptionDTOCollection(entities)).thenReturn(List.of());

        List<KycSubscriptionDTO> actual = service.handleSearch(criteria, Set.of());

        assertTrue(actual.isEmpty());
    }

    @Test
    void handleGetAllWithPagingMapsEachEntity() throws Exception {
        KycSubscription entity = KycSubscription.Factory.newInstance();
        KycSubscriptionDTO dto = new KycSubscriptionDTO();
        Page<KycSubscription> page = new PageImpl<>(List.of(entity));

        when(kycSubscriptionRepository.findAll(PageRequest.of(0, 10))).thenReturn(page);
        when(kycSubscriptionDao.toKycSubscriptionDTO(entity)).thenReturn(dto);

        Page<KycSubscriptionDTO> actual = service.handleGetAll(0, 10);

        assertEquals(1, actual.getContent().size());
        assertSame(dto, actual.getContent().get(0));
    }

    @Test
    void handleSearchWithPagingUsesProvidedSortings() throws Exception {
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
        when(kycSubscriptionDao.toKycSubscriptionDTO(entity)).thenReturn(dto);

        Page<KycSubscriptionDTO> actual = service.handleSearch(criteria);

        assertEquals(1, actual.getContent().size());
        assertSame(dto, actual.getContent().get(0));
    }

    @Test
    void handleSearchWithPagingUsesDefaultSortWhenSortingsMissing() throws Exception {
        SearchObject<SubscriptionSearchCriteria> criteria = new SearchObject<>();
        criteria.setCriteria(new SubscriptionSearchCriteria());
        criteria.setPageNumber(0);
        criteria.setPageSize(10);
        criteria.setSortings(List.of());

        when(kycSubscriptionRepository.findAll(org.mockito.ArgumentMatchers.<Specification<KycSubscription>>any(), any(PageRequest.class)))
                .thenReturn(Page.empty());

        Page<KycSubscriptionDTO> actual = service.handleSearch(criteria);

        assertTrue(actual.isEmpty());
    }

    @Test
    void handleFindByOrganisationReturnsMappedCollection() throws Exception {
        UUID orgId = UUID.randomUUID();
        List<KycSubscription> entities = List.of(KycSubscription.Factory.newInstance());
        List<KycSubscriptionDTO> expected = List.of(new KycSubscriptionDTO());

        when(kycSubscriptionRepository.findAll(org.mockito.ArgumentMatchers.<Specification<KycSubscription>>any()))
                .thenAnswer(invocation -> {
                    evaluateSpecification(invocation.getArgument(0));
                    return entities;
                });
        when(kycSubscriptionMapper.toKycSubscriptionDTOCollection(entities)).thenReturn(expected);

        List<KycSubscriptionDTO> actual = service.handleFindByOrganisation(orgId.toString(), "tester");

        assertSame(expected, actual);
    }

    @Test
    void handleCountByStatusReturnsRepositoryValue() throws Exception {
        when(kycSubscriptionRepository.countByStatus(KycSubsciptionStatus.ACTIVE)).thenReturn(Optional.of(12L));

        long actual = service.handleCountByStatus(KycSubsciptionStatus.ACTIVE);

        assertEquals(12L, actual);
    }

    @Test
    void handleCountReturnsRepositoryCount() throws Exception {
        when(kycSubscriptionRepository.count()).thenReturn(20L);

        long actual = service.handleCount();

        assertEquals(20L, actual);
    }

    private void evaluateSpecification(Specification<KycSubscription> specification) {
        Root<KycSubscription> root = org.mockito.Mockito.mock(Root.class);
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