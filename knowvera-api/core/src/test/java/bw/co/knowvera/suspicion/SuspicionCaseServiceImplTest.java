package bw.co.knowvera.suspicion;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import bw.co.knowvera.individual.IndividualRepository;
import bw.co.knowvera.kyc.KycRecordRepository;
import bw.co.knowvera.organisation.OrganisationRepository;

class SuspicionCaseServiceImplTest {

    private final SuspicionCaseRepository repository = mock(SuspicionCaseRepository.class);
    private SuspicionCaseServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new SuspicionCaseServiceImpl(repository, Mappers.getMapper(SuspicionCaseMapper.class),
                mock(KycRecordRepository.class), mock(IndividualRepository.class), mock(OrganisationRepository.class));
        when(repository.save(any(SuspicionCase.class))).thenAnswer(invocation -> invocation.getArgument(0));
    }

    @Test
    void theReportIsDueFiveWorkingDaysAfterTheSuspicion() {
        // Wednesday 4 November 2026 -> Wednesday 11 November; Friday 6 November -> Friday 13 November
        assertEquals(LocalDate.of(2026, 11, 11), SuspicionCaseServiceImpl.reportDeadline(LocalDate.of(2026, 11, 4)));
        assertEquals(LocalDate.of(2026, 11, 13), SuspicionCaseServiceImpl.reportDeadline(LocalDate.of(2026, 11, 6)));
        // Raised on a Saturday: counted from the following Monday
        assertEquals(LocalDate.of(2026, 11, 13), SuspicionCaseServiceImpl.reportDeadline(LocalDate.of(2026, 11, 7)));
    }

    @Test
    void raisingACaseNeedsGroundsAndASubject() {
        SuspicionCaseDTO noGrounds = new SuspicionCaseDTO();
        noGrounds.setSubjectName("Kabo Seretse");
        assertThrows(Exception.class, () -> service.raise(noGrounds, "kyc.analyst"));

        SuspicionCaseDTO noSubject = new SuspicionCaseDTO();
        noSubject.setGrounds("Cash deposits just under the reporting threshold");
        assertThrows(Exception.class, () -> service.raise(noSubject, "kyc.analyst"));
    }

    @Test
    void aRaisedCaseIsOpenWithADeadlineAndAHistory() throws Exception {
        SuspicionCaseDTO dto = new SuspicionCaseDTO();
        dto.setSubjectName("Kabo Seretse");
        dto.setGrounds("Cash deposits just under the reporting threshold");
        dto.setSuspicionDate(LocalDate.of(2026, 11, 4));

        SuspicionCaseDTO raised = service.raise(dto, "kyc.analyst");

        assertEquals(SuspicionCaseStatus.OPEN, raised.getStatus());
        assertEquals(SuspicionTrigger.OTHER, raised.getCause());
        assertEquals(LocalDate.of(2026, 11, 11), raised.getReportDeadline());
        assertTrue(raised.getRef().startsWith("STR-2026-"));
        assertEquals(1, raised.getNotes().size());
        assertTrue(raised.getNotes().get(0).contains("kyc.analyst"));
    }

    @Test
    void theMlroReportsAndClosesACase() throws Exception {
        UUID id = UUID.randomUUID();
        SuspicionCase stored = SuspicionCase.Factory.newInstance();
        stored.setId(id);
        stored.setStatus(SuspicionCaseStatus.OPEN);
        stored.setReportDeadline(LocalDate.now().plusDays(3));
        stored.setNotes(new ArrayList<>());
        when(repository.findById(id)).thenReturn(Optional.of(stored));

        service.escalate(id.toString(), "mlro");
        assertEquals(SuspicionCaseStatus.ESCALATED, stored.getStatus());

        assertThrows(Exception.class, () -> service.recordReport(id.toString(), " ", "mlro"));
        service.recordReport(id.toString(), "FIA-2026-0042", "mlro");
        assertEquals(SuspicionCaseStatus.REPORTED, stored.getStatus());
        assertEquals("FIA-2026-0042", stored.getFiaReference());

        assertThrows(Exception.class, () -> service.close(id.toString(), "", "mlro"));
        service.close(id.toString(), "Reported; relationship terminated", "mlro");
        assertEquals(SuspicionCaseStatus.CLOSED, stored.getStatus());
        assertEquals(3, stored.getNotes().size());
    }
}
