package bw.co.centralkyc.invoice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
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
import bw.co.centralkyc.TimePeriod;
import bw.co.centralkyc.sequence.SequenceGenerator;
import bw.co.centralkyc.sequence.SequenceGeneratorRepository;
import bw.co.centralkyc.sequence.SequenceGeneratorService;
import bw.co.centralkyc.settings.SettingsDTO;
import bw.co.centralkyc.settings.SettingsService;
import bw.co.centralkyc.subscription.KycSubsciptionStatus;
import bw.co.centralkyc.subscription.KycSubscription;
import bw.co.centralkyc.subscription.KycSubscriptionMapper;
import bw.co.centralkyc.subscription.KycSubscriptionRepository;
import bw.co.centralkyc.organisation.Organisation;

@ExtendWith(MockitoExtension.class)
class KycInvoiceServiceImplTest {

    @Mock
    private KycInvoiceRepository kycInvoiceRepository;
    @Mock
    private KycInvoiceMapper kycInvoiceMapper;
    @Mock
    private SequenceGeneratorService sequenceGeneratorService;
    @Mock
    private SequenceGeneratorRepository sequenceGeneratorRepository;
    @Mock
    private KycSubscriptionRepository kycSubscriptionRepository;
    @Mock
    private KycSubscriptionMapper kycSubscriptionMapper;
    @Mock
    private SettingsService settingsService;
    @Mock
    private MessageSource messageSource;

    private KycInvoiceServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new KycInvoiceServiceImpl(
                kycInvoiceRepository,
                kycInvoiceMapper,
                sequenceGeneratorService,
                sequenceGeneratorRepository,
                kycSubscriptionRepository,
                kycSubscriptionMapper,
                settingsService,
                messageSource);
    }

    @Test
    void handleSaveCreatesInvoiceSequenceWhenRefIsBlank() throws Exception {
        KycInvoiceDTO input = new KycInvoiceDTO();
        input.setRef("PLACEHOLDER");
        input.setIssueDate(new java.util.Date());
        input.setOrganisationId(UUID.randomUUID().toString());
        input.setOrganisationCode("ORG");
        input.setOrganisationName("Organisation");
        input.setOrganisationRegistrationNo("REG-1");
        input.setSubscriptionRef("SUB-1");
        input.setSubscriptionId(UUID.randomUUID().toString());
        input.setSubscriptionPeriod(TimePeriod.MONTH);
        KycInvoice entity = KycInvoice.Factory.newInstance();
        KycInvoiceDTO expected = new KycInvoiceDTO();

        when(kycInvoiceMapper.kycInvoiceDTOToEntity(input)).thenReturn(entity);
        when(sequenceGeneratorRepository.findByName("KYC_INVOICE_REF")).thenReturn(Optional.empty());
        when(sequenceGeneratorRepository.save(any(SequenceGenerator.class))).thenAnswer(inv -> inv.getArgument(0));
        when(sequenceGeneratorService.generateNextSequenceValue("KYC_INVOICE_REF", true)).thenReturn("INV-000001");
        when(kycInvoiceRepository.save(entity)).thenReturn(entity);
        when(kycInvoiceMapper.toKycInvoiceDTO(entity)).thenReturn(expected);

        KycInvoiceDTO actual = service.save(input);

        ArgumentCaptor<SequenceGenerator> captor = ArgumentCaptor.forClass(SequenceGenerator.class);
        verify(sequenceGeneratorRepository).save(captor.capture());
        assertEquals(TargetEntity.INVOICE, captor.getValue().getTargetEntity());
        assertEquals("INV-000001", entity.getRef());
        assertSame(expected, actual);
    }

    @Test
    void handleGenerateInvoiceSetsVatAndReturnsRepositoryResult() throws Exception {
        UUID subscriptionId = UUID.randomUUID();
        KycSubscription subscription = KycSubscription.Factory.newInstance();
        subscription.setId(subscriptionId);
        subscription.setStatus(KycSubsciptionStatus.ACTIVE);
        subscription.setPeriod(TimePeriod.MONTH);
        subscription.setAmount(100.0);
        subscription.setRef("SUB-1");
        subscription.setOrganisation(Organisation.Factory.newInstance());

        SettingsDTO settings = new SettingsDTO();
        settings.setVat(14.0);
        List<KycInvoiceDTO> expected = List.of(new KycInvoiceDTO());

        when(kycSubscriptionRepository.findById(subscriptionId)).thenReturn(Optional.of(subscription));
        when(sequenceGeneratorRepository.findByName("KYC_INVOICE_REF")).thenReturn(Optional.of(SequenceGenerator.Factory.newInstance()));
        when(sequenceGeneratorService.generateNextSequenceValue("KYC_INVOICE_REF", true)).thenReturn("INV-000777");
        when(settingsService.getAll()).thenReturn(List.of(settings));
        when(kycInvoiceRepository.save(any(KycInvoice.class))).thenAnswer(inv -> inv.getArgument(0));
        when(kycInvoiceRepository.findInvoicesBySubscriptionId(subscriptionId)).thenReturn(expected);

        List<KycInvoiceDTO> actual = service.generateInvoice(subscriptionId.toString(), "tester");

        ArgumentCaptor<KycInvoice> captor = ArgumentCaptor.forClass(KycInvoice.class);
        verify(kycInvoiceRepository).save(captor.capture());
        KycInvoice saved = captor.getValue();
        assertEquals("INV-000777", saved.getRef());
        assertEquals(14.0, saved.getVat());
        assertEquals(114.0, saved.getTotalAmount());
        assertEquals(LocalDate.now().withDayOfMonth(1), saved.getStartDate());
        assertTrue(saved.getEndDate().isAfter(saved.getStartDate()));
        assertSame(expected, actual);
    }

    @Test
    void handleFindByIdReturnsMappedInvoice() throws Exception {
        UUID id = UUID.randomUUID();
        KycInvoice entity = KycInvoice.Factory.newInstance();
        KycInvoiceDTO expected = new KycInvoiceDTO();

        when(kycInvoiceRepository.findById(id)).thenReturn(Optional.of(entity));
        when(kycInvoiceMapper.toKycInvoiceDTO(entity)).thenReturn(expected);

        KycInvoiceDTO actual = service.findById(id.toString());

        assertSame(expected, actual);
    }

    @Test
    void handleSaveSkipsSequenceWhenRefAlreadyPresent() throws Exception {
        KycInvoiceDTO input = new KycInvoiceDTO();
        KycInvoice entity = KycInvoice.Factory.newInstance();
        input.setRef("INV-CUSTOM");
        input.setIssueDate(new java.util.Date());
        input.setOrganisationId(UUID.randomUUID().toString());
        input.setOrganisationCode("ORG");
        input.setOrganisationName("Organisation");
        input.setOrganisationRegistrationNo("REG-1");
        input.setSubscriptionRef("SUB-1");
        input.setSubscriptionId(UUID.randomUUID().toString());
        input.setSubscriptionPeriod(TimePeriod.MONTH);
        entity.setRef("INV-CUSTOM");
        KycInvoiceDTO expected = new KycInvoiceDTO();

        when(kycInvoiceMapper.kycInvoiceDTOToEntity(input)).thenReturn(entity);
        when(kycInvoiceRepository.save(entity)).thenReturn(entity);
        when(kycInvoiceMapper.toKycInvoiceDTO(entity)).thenReturn(expected);

        KycInvoiceDTO actual = service.save(input);

        assertSame(expected, actual);
        verify(sequenceGeneratorRepository, never()).findByName("KYC_INVOICE_REF");
    }

    @Test
    void handleRemoveDeletesById() throws Exception {
        UUID id = UUID.randomUUID();

        boolean removed = service.remove(id.toString());

        assertTrue(removed);
        verify(kycInvoiceRepository).deleteById(id);
    }

    @Test
    void handleGetAllMapsRepositoryResults() throws Exception {
        List<KycInvoice> entities = List.of(KycInvoice.Factory.newInstance());
        List<KycInvoiceDTO> expected = List.of(new KycInvoiceDTO());

        when(kycInvoiceRepository.findAll()).thenReturn(entities);
        when(kycInvoiceMapper.toKycInvoiceDTOCollection(entities)).thenReturn(expected);

        List<KycInvoiceDTO> actual = service.getAll();

        assertSame(expected, actual);
    }

    @Test
    void handleSearchUsesDefaultSortWhenMissingSortOrders() throws Exception {
        InvoiceSearchCriteria criteria = new InvoiceSearchCriteria();
        criteria.setPaid(Boolean.TRUE);

        List<KycInvoice> entities = List.of(KycInvoice.Factory.newInstance());
        List<KycInvoiceDTO> expected = List.of(new KycInvoiceDTO());

        when(kycInvoiceRepository.findAll(org.mockito.ArgumentMatchers.<Specification<KycInvoice>>any(), any(Sort.class)))
                .thenReturn(entities);
        when(kycInvoiceMapper.toKycInvoiceDTOCollection(entities)).thenReturn(expected);

        List<KycInvoiceDTO> actual = service.search(criteria, null);

        assertSame(expected, actual);
    }

    @Test
    void handleSearchEvaluatesAllSpecificationPredicates() throws Exception {
        InvoiceSearchCriteria criteria = new InvoiceSearchCriteria();
        criteria.setPaid(Boolean.TRUE);
        criteria.setRef("INV");
        criteria.setOrganisationName("Acme");
        criteria.setOrganisationRegistrationNo("REG-1");
        criteria.setOrganisatonCode("ORG");
        criteria.setOrganisatonId(UUID.randomUUID().toString());

        List<KycInvoice> entities = List.of(KycInvoice.Factory.newInstance());
        when(kycInvoiceRepository.findAll(org.mockito.ArgumentMatchers.<Specification<KycInvoice>>any(), any(Sort.class)))
                .thenAnswer(invocation -> {
                    evaluateSpecification(invocation.getArgument(0));
                    return entities;
                });
        when(kycInvoiceMapper.toKycInvoiceDTOCollection(entities)).thenReturn(List.of());

        List<KycInvoiceDTO> actual = service.search(criteria, Set.of());

        assertTrue(actual.isEmpty());
    }

    @Test
    void handleSearchWithPagingMapsResults() throws Exception {
        SearchObject<InvoiceSearchCriteria> criteria = new SearchObject<>();
        criteria.setCriteria(new InvoiceSearchCriteria());
        criteria.setPageNumber(0);
        criteria.setPageSize(10);
        criteria.setSortings(List.of(new PropertySearchOrder("createdAt", SortOrder.DESC)));

        KycInvoice entity = KycInvoice.Factory.newInstance();
        KycInvoiceDTO dto = new KycInvoiceDTO();
        Page<KycInvoice> page = new PageImpl<>(List.of(entity));

        when(kycInvoiceRepository.findAll(org.mockito.ArgumentMatchers.<Specification<KycInvoice>>any(), any(PageRequest.class)))
                .thenReturn(page);
        when(kycInvoiceMapper.toKycInvoiceDTO(entity)).thenReturn(dto);

        Page<KycInvoiceDTO> actual = service.search(criteria);

        assertEquals(1, actual.getContent().size());
        assertSame(dto, actual.getContent().get(0));
    }

    @Test
    void handleGetAllWithPagingMapsResults() throws Exception {
        KycInvoice entity = KycInvoice.Factory.newInstance();
        KycInvoiceDTO dto = new KycInvoiceDTO();
        Page<KycInvoice> page = new PageImpl<>(List.of(entity));

        when(kycInvoiceRepository.findAll(any(PageRequest.class))).thenReturn(page);
        when(kycInvoiceMapper.toKycInvoiceDTO(entity)).thenReturn(dto);

        Page<KycInvoiceDTO> actual = service.getAll(0, 5);

        assertEquals(1, actual.getContent().size());
        assertSame(dto, actual.getContent().get(0));
    }

    @Test
    void handleUploadThrowsUnsupportedOperationException() {
        assertThrows(KycInvoiceServiceException.class,
                () -> service.upload(UUID.randomUUID().toString(), "url", UploadPurpose.INVOICE, "tester"));
    }

    @Test
    void handleGenerateInvoiceThrowsWhenSubscriptionNotFound() {
        UUID id = UUID.randomUUID();
        when(kycSubscriptionRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(KycInvoiceServiceException.class,
                () -> service.generateInvoice(id.toString(), "tester"));
    }

    @Test
    void handleGenerateInvoiceThrowsWhenSubscriptionInactive() {
        UUID id = UUID.randomUUID();
        KycSubscription subscription = KycSubscription.Factory.newInstance();
        subscription.setId(id);
        subscription.setStatus(KycSubsciptionStatus.INACTIVE);
        subscription.setRef("SUB-2");

        when(kycSubscriptionRepository.findById(id)).thenReturn(Optional.of(subscription));

        assertThrows(KycInvoiceServiceException.class,
                () -> service.generateInvoice(id.toString(), "tester"));
    }

    @Test
    void handleGenerateInvoiceThrowsForWeeklyPeriod() {
        UUID id = UUID.randomUUID();
        KycSubscription subscription = KycSubscription.Factory.newInstance();
        subscription.setId(id);
        subscription.setStatus(KycSubsciptionStatus.ACTIVE);
        subscription.setPeriod(TimePeriod.WEEK);
        subscription.setAmount(100.0);
        subscription.setRef("SUB-WEEK");
        subscription.setOrganisation(Organisation.Factory.newInstance());

        when(kycSubscriptionRepository.findById(id)).thenReturn(Optional.of(subscription));
        when(sequenceGeneratorRepository.findByName("KYC_INVOICE_REF")).thenReturn(Optional.of(SequenceGenerator.Factory.newInstance()));
        when(sequenceGeneratorService.generateNextSequenceValue("KYC_INVOICE_REF", true)).thenReturn("INV-991");

        assertThrows(KycInvoiceServiceException.class,
                () -> service.generateInvoice(id.toString(), "tester"));
    }

    @Test
    void handleGenerateInvoiceSetsYearRangeAndNoVatWhenSettingsMissing() throws Exception {
        UUID subscriptionId = UUID.randomUUID();
        KycSubscription subscription = KycSubscription.Factory.newInstance();
        subscription.setId(subscriptionId);
        subscription.setStatus(KycSubsciptionStatus.ACTIVE);
        subscription.setPeriod(TimePeriod.YEAR);
        subscription.setAmount(200.0);
        subscription.setRef("SUB-Y");
        subscription.setOrganisation(Organisation.Factory.newInstance());

        List<KycInvoiceDTO> expected = List.of(new KycInvoiceDTO());

        when(kycSubscriptionRepository.findById(subscriptionId)).thenReturn(Optional.of(subscription));
        when(sequenceGeneratorRepository.findByName("KYC_INVOICE_REF")).thenReturn(Optional.of(SequenceGenerator.Factory.newInstance()));
        when(sequenceGeneratorService.generateNextSequenceValue("KYC_INVOICE_REF", true)).thenReturn("INV-888");
        when(settingsService.getAll()).thenReturn(List.of());
        when(kycInvoiceRepository.save(any(KycInvoice.class))).thenAnswer(inv -> inv.getArgument(0));
        when(kycInvoiceRepository.findInvoicesBySubscriptionId(subscriptionId)).thenReturn(expected);

        List<KycInvoiceDTO> actual = service.generateInvoice(subscriptionId.toString(), "tester");

        ArgumentCaptor<KycInvoice> captor = ArgumentCaptor.forClass(KycInvoice.class);
        verify(kycInvoiceRepository).save(captor.capture());
        KycInvoice saved = captor.getValue();
        assertEquals(LocalDate.now().withDayOfYear(1), saved.getStartDate());
        assertEquals(saved.getAmount(), saved.getTotalAmount());
        assertSame(expected, actual);
    }

    @Test
    void handleFindByOrganisationReturnsMappedCollection() throws Exception {
        UUID orgId = UUID.randomUUID();
        List<KycInvoice> entities = List.of(KycInvoice.Factory.newInstance());
        List<KycInvoiceDTO> expected = List.of(new KycInvoiceDTO());

        when(kycInvoiceRepository.findAll(org.mockito.ArgumentMatchers.<Specification<KycInvoice>>any())).thenAnswer(invocation -> {
            evaluateSpecification(invocation.getArgument(0));
            return entities;
        });
        when(kycInvoiceMapper.toKycInvoiceDTOCollection(entities)).thenReturn(expected);

        List<KycInvoiceDTO> actual = service.findByOrganisation(orgId.toString());

        assertSame(expected, actual);
    }

    @Test
    void handleFindBySubscriptionReturnsMappedCollection() throws Exception {
        UUID subscriptionId = UUID.randomUUID();
        List<KycInvoice> entities = List.of(KycInvoice.Factory.newInstance());
        List<KycInvoiceDTO> expected = List.of(new KycInvoiceDTO());

        when(kycInvoiceRepository.findAll(org.mockito.ArgumentMatchers.<Specification<KycInvoice>>any())).thenAnswer(invocation -> {
            evaluateSpecification(invocation.getArgument(0));
            return entities;
        });
        when(kycInvoiceMapper.toKycInvoiceDTOCollection(entities)).thenReturn(expected);

        List<KycInvoiceDTO> actual = service.findBySubscription(subscriptionId.toString());

        assertSame(expected, actual);
    }

    @Test
    void handleFindByOrganisationPagedDelegatesToRepository() throws Exception {
        UUID orgId = UUID.randomUUID();
        Page<KycInvoiceDTO> expected = Page.empty();

        when(kycInvoiceRepository.findInvoicesByOrganisationId(orgId, PageRequest.of(0, 5))).thenReturn(expected);

        Page<KycInvoiceDTO> actual = service.findByOrganisation(orgId.toString(), 0, 5);

        assertSame(expected, actual);
    }

    @Test
    void handleFindBySubscriptionPagedDelegatesToRepository() throws Exception {
        UUID subscriptionId = UUID.randomUUID();
        Page<KycInvoiceDTO> expected = Page.empty();

        when(kycInvoiceRepository.findInvoicesBySubscriptionId(subscriptionId, PageRequest.of(1, 3))).thenReturn(expected);

        Page<KycInvoiceDTO> actual = service.findBySubscription(subscriptionId.toString(), 1, 3);

        assertSame(expected, actual);
    }

    @Test
    void handleCountMethodsReturnRepositoryValuesOrZero() throws Exception {
        UUID orgId = UUID.randomUUID();
        when(kycInvoiceRepository.countInvoicesByOrganisationId(orgId)).thenReturn(Optional.of(2L));
        when(kycInvoiceRepository.countInvoices(true)).thenReturn(Optional.empty());
        when(kycInvoiceRepository.countOrganisationInvoices(false, orgId)).thenReturn(Optional.of(5L));
        when(kycInvoiceRepository.count()).thenReturn(12L);

        assertEquals(2L, service.countInvoicesByOrganisationId(orgId.toString()));
        assertEquals(0L, service.countInvoices(true));
        assertEquals(5L, service.countOrganisationInvoices(false, orgId.toString()));
        assertEquals(12L, service.count());
    }

    @Test
    void serviceBaseGuardsRejectInvalidArguments() {
        assertThrows(IllegalArgumentException.class, () -> service.findById(null));
        assertThrows(IllegalArgumentException.class, () -> service.findById(" "));
        assertThrows(IllegalArgumentException.class, () -> service.save(null));
        assertThrows(IllegalArgumentException.class, () -> service.remove(null));
        assertThrows(IllegalArgumentException.class, () -> service.remove("\t"));
        assertThrows(IllegalArgumentException.class, () -> service.upload(null, "url", UploadPurpose.INVOICE, "tester"));
        assertThrows(IllegalArgumentException.class,
                () -> service.upload(UUID.randomUUID().toString(), null, UploadPurpose.INVOICE, "tester"));
        assertThrows(IllegalArgumentException.class,
                () -> service.upload(UUID.randomUUID().toString(), "url", null, "tester"));
        assertThrows(IllegalArgumentException.class,
                () -> service.upload(UUID.randomUUID().toString(), "url", UploadPurpose.INVOICE, " "));
        assertThrows(IllegalArgumentException.class, () -> service.generateInvoice(null, "tester"));
        assertThrows(IllegalArgumentException.class, () -> service.generateInvoice(" ", "tester"));
        assertThrows(IllegalArgumentException.class,
                () -> service.generateInvoice(UUID.randomUUID().toString(), null));
        assertThrows(IllegalArgumentException.class,
                () -> service.generateInvoice(UUID.randomUUID().toString(), "\t"));
        assertThrows(IllegalArgumentException.class, () -> service.findByOrganisation(null));
        assertThrows(IllegalArgumentException.class, () -> service.findByOrganisation(" "));
        assertThrows(IllegalArgumentException.class, () -> service.findBySubscription(null));
        assertThrows(IllegalArgumentException.class, () -> service.findBySubscription("\n"));
        assertThrows(IllegalArgumentException.class, () -> service.findByOrganisation(null, 0, 10));
        assertThrows(IllegalArgumentException.class, () -> service.findBySubscription("", 0, 10));
        assertThrows(IllegalArgumentException.class, () -> service.countInvoicesByOrganisationId(null));
        assertThrows(IllegalArgumentException.class, () -> service.countInvoicesByOrganisationId(" "));
        assertThrows(IllegalArgumentException.class, () -> service.countOrganisationInvoices(Boolean.TRUE, null));
        assertThrows(IllegalArgumentException.class, () -> service.countOrganisationInvoices(Boolean.FALSE, "\t"));
    }

    @Test
    void serviceBaseSaveGuardsRejectMissingRequiredFields() {
        KycInvoiceDTO missingRef = validInvoice();
        missingRef.setRef(" ");
        assertThrows(IllegalArgumentException.class, () -> service.save(missingRef));

        KycInvoiceDTO missingIssueDate = validInvoice();
        missingIssueDate.setIssueDate(null);
        assertThrows(IllegalArgumentException.class, () -> service.save(missingIssueDate));

        KycInvoiceDTO missingOrganisationId = validInvoice();
        missingOrganisationId.setOrganisationId(null);
        assertThrows(IllegalArgumentException.class, () -> service.save(missingOrganisationId));

        KycInvoiceDTO missingOrganisationCode = validInvoice();
        missingOrganisationCode.setOrganisationCode("\t");
        assertThrows(IllegalArgumentException.class, () -> service.save(missingOrganisationCode));

        KycInvoiceDTO missingOrganisationName = validInvoice();
        missingOrganisationName.setOrganisationName("");
        assertThrows(IllegalArgumentException.class, () -> service.save(missingOrganisationName));

        KycInvoiceDTO missingRegistrationNo = validInvoice();
        missingRegistrationNo.setOrganisationRegistrationNo(null);
        assertThrows(IllegalArgumentException.class, () -> service.save(missingRegistrationNo));

        KycInvoiceDTO missingSubscriptionRef = validInvoice();
        missingSubscriptionRef.setSubscriptionRef(" ");
        assertThrows(IllegalArgumentException.class, () -> service.save(missingSubscriptionRef));

        KycInvoiceDTO missingSubscriptionId = validInvoice();
        missingSubscriptionId.setSubscriptionId(null);
        assertThrows(IllegalArgumentException.class, () -> service.save(missingSubscriptionId));

        KycInvoiceDTO missingSubscriptionPeriod = validInvoice();
        missingSubscriptionPeriod.setSubscriptionPeriod(null);
        assertThrows(IllegalArgumentException.class, () -> service.save(missingSubscriptionPeriod));
    }

    private static KycInvoiceDTO validInvoice() {
        KycInvoiceDTO dto = new KycInvoiceDTO();
        dto.setRef("INV-1");
        dto.setIssueDate(new java.util.Date());
        dto.setOrganisationId(UUID.randomUUID().toString());
        dto.setOrganisationCode("ORG");
        dto.setOrganisationName("Organisation");
        dto.setOrganisationRegistrationNo("REG-1");
        dto.setSubscriptionRef("SUB-1");
        dto.setSubscriptionId(UUID.randomUUID().toString());
        dto.setSubscriptionPeriod(TimePeriod.MONTH);
        return dto;
    }

    @SuppressWarnings("unchecked")
    private void evaluateSpecification(Specification<KycInvoice> specification) {
        Root<KycInvoice> root = (Root<KycInvoice>) org.mockito.Mockito.mock(Root.class);
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