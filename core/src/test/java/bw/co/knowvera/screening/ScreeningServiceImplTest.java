package bw.co.knowvera.screening;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyIterable;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentCaptor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.jpa.domain.Specification;

import bw.co.knowvera.TargetEntity;
import bw.co.knowvera.individual.Individual;
import bw.co.knowvera.individual.IndividualRepository;
import bw.co.knowvera.kyc.KycRecord;
import bw.co.knowvera.kyc.KycRecordRepository;
import bw.co.knowvera.organisation.OrganisationRepository;
import bw.co.knowvera.properties.RabbitProperties;
import bw.co.knowvera.suspicion.SuspicionCaseDTO;
import bw.co.knowvera.suspicion.SuspicionCaseService;
import bw.co.knowvera.suspicion.SuspicionTrigger;

class ScreeningServiceImplTest {

    private final SanctionsListEntryRepository entries = mock(SanctionsListEntryRepository.class);
    private final ScreeningHitRepository hits = mock(ScreeningHitRepository.class);
    private final KycRecordRepository records = mock(KycRecordRepository.class);
    private final IndividualRepository individuals = mock(IndividualRepository.class);
    private final SuspicionCaseService cases = mock(SuspicionCaseService.class);
    private ScreeningServiceImpl service;

    private final UUID recordId = UUID.randomUUID();
    private final UUID individualId = UUID.randomUUID();
    private KycRecord record;

    @BeforeEach
    void setUp() {
        service = new ScreeningServiceImpl(entries, Mappers.getMapper(SanctionsListEntryMapper.class), hits,
                Mappers.getMapper(ScreeningHitMapper.class), records, individuals, mock(OrganisationRepository.class), cases,
                mock(RabbitTemplate.class), mock(RabbitProperties.class), new SanctionsMatcher(0.85), new UnSanctionsListParser());

        record = KycRecord.Factory.newInstance();
        record.setId(recordId);
        record.setTarget(TargetEntity.INDIVIDUAL);
        record.setTargetId(individualId.toString());
        record.setRelatedParties(new ArrayList<>());

        Individual individual = Individual.Factory.newInstance();
        individual.setFirstName("Abdul");
        individual.setSurname("Rahman Yasin");
        when(individuals.findById(individualId)).thenReturn(Optional.of(individual));

        SanctionsListEntry listed = SanctionsListEntry.Factory.newInstance();
        listed.setSource(ScreeningListSource.UN_SECURITY_COUNCIL);
        listed.setSubjectType(ScreeningSubjectType.INDIVIDUAL);
        listed.setReference("QDi.123");
        listed.setName("ABDUL RAHMAN YASIN");
        listed.setListVersion("2026-11-02");
        when(entries.findAll()).thenReturn(List.of(listed));
    }

    @Test
    @SuppressWarnings("unchecked")
    void aListedCustomerBecomesAPendingHitOnce() {
        when(hits.findAll(any(Specification.class))).thenReturn(List.of());

        List<ScreeningHit> found = service.screen(record);

        assertEquals(1, found.size());
        assertEquals(ScreeningHitStatus.PENDING, found.get(0).getStatus());
        assertEquals("QDi.123", found.get(0).getEntryReference());
        assertEquals("Customer", found.get(0).getSubjectRole());

        // Screening again finds nothing new: the match is already recorded
        when(hits.findAll(any(Specification.class))).thenReturn(found);
        assertTrue(service.screen(record).isEmpty());
    }

    @Test
    void anEmptyListIsRefused() {
        assertThrows(ScreeningServiceException.class,
                () -> service.importList(new UnSanctionsListParser.ParsedList("v", List.of()), "admin"));
        verify(entries, never()).deleteAll(anyIterable());
    }

    @Test
    void aDecisionNeedsReasons() {
        // Blank reasons are refused by the service's own argument checks
        assertThrows(Exception.class,
                () -> service.reviewHit(UUID.randomUUID().toString(), ScreeningHitStatus.CONFIRMED, " ", "analyst"));
        assertThrows(ScreeningServiceException.class,
                () -> service.reviewHit(UUID.randomUUID().toString(), ScreeningHitStatus.PENDING, "why", "analyst"));
    }

    @Test
    void aConfirmedMatchRaisesASuspicionCase() throws Exception {
        UUID hitId = UUID.randomUUID();
        ScreeningHit hit = ScreeningHit.Factory.newInstance();
        hit.setId(hitId);
        hit.setKycRecordId(recordId.toString());
        hit.setSubjectName("Abdul Rahman Yasin");
        hit.setSubjectRole("Customer");
        hit.setSource(ScreeningListSource.UN_SECURITY_COUNCIL);
        hit.setEntryReference("QDi.123");
        hit.setEntryName("ABDUL RAHMAN YASIN");
        hit.setScore(0.97);
        hit.setStatus(ScreeningHitStatus.PENDING);
        when(hits.findById(hitId)).thenReturn(Optional.of(hit));
        when(hits.save(any(ScreeningHit.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(records.findById(recordId)).thenReturn(Optional.of(record));
        when(cases.findByRecord(recordId.toString())).thenReturn(List.of());

        service.reviewHit(hitId.toString(), ScreeningHitStatus.CONFIRMED, "Same name, date of birth and nationality", "screening.analyst");

        assertEquals(ScreeningHitStatus.CONFIRMED, hit.getStatus());
        assertEquals("screening.analyst", hit.getReviewedBy());
        ArgumentCaptor<SuspicionCaseDTO> raised = ArgumentCaptor.forClass(SuspicionCaseDTO.class);
        verify(cases).raise(raised.capture(), eq("screening.analyst"));
        assertEquals(SuspicionTrigger.SANCTIONS_MATCH, raised.getValue().getCause());
        assertEquals(recordId.toString(), raised.getValue().getKycRecordId());
    }
}
