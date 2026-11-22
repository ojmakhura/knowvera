package bw.co.knowvera.organisation.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.support.TransactionOperations;

import bw.co.knowvera.PhoneNumber;
import bw.co.knowvera.TargetEntity;
import bw.co.knowvera.document.Document;
import bw.co.knowvera.document.DocumentDTO;
import bw.co.knowvera.document.DocumentMapper;
import bw.co.knowvera.document.DocumentRepository;
import bw.co.knowvera.individual.EmploymentStatus;
import bw.co.knowvera.individual.Individual;
import bw.co.knowvera.individual.IndividualIdentityType;
import bw.co.knowvera.individual.IndividualMapper;
import bw.co.knowvera.individual.IndividualRepository;
import bw.co.knowvera.individual.MaritalStatus;
import bw.co.knowvera.individual.Sex;
import bw.co.knowvera.kyc.KycComplianceStatus;
import bw.co.knowvera.kyc.KycRecordRepository;
import bw.co.knowvera.messaging.ClientRequestNotification;
import bw.co.knowvera.organisation.Organisation;
import bw.co.knowvera.organisation.OrganisationRepository;
import bw.co.knowvera.retention.RecordRetention;
import bw.co.knowvera.sequence.SequenceGeneratorRepository;
import bw.co.knowvera.sequence.SequenceGeneratorService;
import bw.co.knowvera.settings.SettingsMapper;
import bw.co.knowvera.settings.SettingsRepository;

/** The client request spreadsheet import ({@code ClientRequestService.uploadRequests}). */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ClientRequestImportTest {

    private static final String HEADER = "First Name,Middle Name,Surname,Identity Type,Identity No,Email Address,"
            + "Phone Numbers,Physical Address,Postal Address,Sex,Date of Birth,Nationality,Marital Status,Employment Status\n";

    private static final String NEW_ROW = "Neo,T.,Kgosi,Passport,BN0012345,omakhura+neo@gmail.com,"
            + "\"71234567; 3912345\",\"Plot 12, Gaborone\",P O Box 1,F,1990-04-12,Botswana,Single,Self Employed\n";

    private static final String STORED_ROW = "Thabo,,Moeng,National ID,123456789,omakhura+thabo@gmail.com,,,,Male,,,,\n";

    @Mock
    private KycRecordRepository kycRecordRepository;
    @Mock
    private ClientRequestRepository clientRequestRepository;
    @Mock
    private ClientRequestMapper clientRequestMapper;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private ClientRequestNotification clientRequestNotification;
    @Mock
    private IndividualRepository individualRepository;
    @Mock
    private IndividualMapper individualMapper;
    @Mock
    private OrganisationRepository organisationRepository;
    @Mock
    private SequenceGeneratorRepository sequenceGeneratorRepository;
    @Mock
    private SequenceGeneratorService sequenceGeneratorService;
    @Mock
    private DocumentRepository documentRepository;
    @Mock
    private DocumentMapper documentMapper;
    @Mock
    private SettingsRepository settingsRepository;
    @Mock
    private SettingsMapper settingsMapper;

    private ClientRequestServiceImpl service;

    private final UUID organisationId = UUID.randomUUID();
    private Organisation organisation;
    private Individual stored;
    private Document file;

    @BeforeEach
    @SuppressWarnings("unchecked")
    void setUp() throws Exception {

        service = new ClientRequestServiceImpl(clientRequestRepository, clientRequestMapper, passwordEncoder,
                clientRequestNotification, individualRepository, individualMapper, organisationRepository,
                sequenceGeneratorRepository, sequenceGeneratorService, documentRepository, documentMapper,
                settingsRepository, settingsMapper, new RecordRetention(20, kycRecordRepository),
                TransactionOperations.withoutTransaction());

        organisation = Organisation.Factory.newInstance();
        organisation.setId(organisationId);
        organisation.setCode("ACME");
        when(organisationRepository.findById(organisationId)).thenReturn(Optional.of(organisation));

        stored = new Individual();
        stored.setId(UUID.randomUUID());
        stored.setFirstName("Thabo");
        stored.setSurname("Moeng");
        stored.setIdentityType(IndividualIdentityType.OMANG);
        stored.setIdentityNo("123456789");
        stored.setEmailAddress("omakhura+thabo@gmail.com");
        when(individualRepository.findByIdentityNoAndIdentityType("123456789", IndividualIdentityType.OMANG))
                .thenReturn(Optional.of(stored));
        when(individualRepository.findByIdentityNo("123456789")).thenReturn(Optional.of(stored));

        when(individualRepository.save(any(Individual.class))).thenAnswer(invocation -> {
            Individual individual = invocation.getArgument(0);
            if (individual.getId() == null) {
                individual.setId(UUID.randomUUID());
            }
            return individual;
        });

        file = new Document();
        when(documentMapper.documentDTOToEntity(any())).thenReturn(file);
        when(documentRepository.save(any(Document.class))).thenAnswer(invocation -> invocation.getArgument(0));

        AtomicInteger counter = new AtomicInteger();
        when(sequenceGeneratorService.generateNextSequenceValue("ACME_CLIENT_REQUEST_REF", true))
                .thenAnswer(invocation -> String.format("ACME_KR-%08d", counter.incrementAndGet()));
        when(passwordEncoder.encode(any())).thenAnswer(invocation -> "hash:" + invocation.getArgument(0));

        when(clientRequestRepository.saveAll(any(Iterable.class))).thenAnswer(invocation -> {
            List<ClientRequest> requests = new ArrayList<>();
            for (ClientRequest request : (Iterable<ClientRequest>) invocation.getArgument(0)) {
                request.setId(UUID.randomUUID());
                requests.add(request);
            }
            return requests;
        });
        when(clientRequestMapper.toClientRequestDTOCollection(any())).thenAnswer(invocation -> {
            List<ClientRequestDTO> dtos = new ArrayList<>();
            for (ClientRequest request : (Iterable<ClientRequest>) invocation.getArgument(0)) {
                ClientRequestDTO dto = new ClientRequestDTO();
                dto.setRef(request.getRef());
                dto.setTarget(request.getTarget());
                dto.setTargetId(request.getTargetId());
                dtos.add(dto);
            }
            return dtos;
        });
    }

    @Test
    @SuppressWarnings("unchecked")
    void csvCreatesANewIndividualAndReusesAStoredOne() {

        Page<ClientRequestDTO> page = upload(csv(HEADER + NEW_ROW + STORED_ROW), TargetEntity.INDIVIDUAL);

        // Only the individual who was not stored is saved, with everything the row says
        ArgumentCaptor<Individual> savedIndividual = ArgumentCaptor.forClass(Individual.class);
        verify(individualRepository, times(1)).save(savedIndividual.capture());
        Individual created = savedIndividual.getValue();
        assertEquals("Neo", created.getFirstName());
        assertEquals("T.", created.getMiddleName());
        assertEquals("Kgosi", created.getSurname());
        assertEquals(IndividualIdentityType.PASSPORT, created.getIdentityType());
        assertEquals("BN0012345", created.getIdentityNo());
        assertEquals("omakhura+neo@gmail.com", created.getEmailAddress());
        assertEquals(List.of("71234567", "3912345"),
                created.getPhoneNumbers().stream().map(PhoneNumber::getPhoneNumber).toList());
        assertEquals("Plot 12, Gaborone", created.getPhysicalAddress());
        assertEquals(Sex.FEMALE, created.getSex());
        assertEquals(LocalDate.of(1990, 4, 12), created.getDateOfBirth());
        assertEquals("Botswana", created.getNationality());
        assertEquals(MaritalStatus.SINGLE, created.getMaritalStatus());
        assertEquals(EmploymentStatus.SELF_EMPLOYED, created.getEmploymentStatus());
        assertEquals(KycComplianceStatus.ABSENT, created.getKycStatus());
        assertEquals("importer", created.getCreatedBy());

        // One pending request per row, as a single saved request would be
        ArgumentCaptor<List<ClientRequest>> savedRequests = ArgumentCaptor.forClass(List.class);
        verify(clientRequestRepository).saveAll(savedRequests.capture());
        List<ClientRequest> requests = savedRequests.getValue();
        assertEquals(2, requests.size());
        assertEquals(List.of(created.getId().toString(), stored.getId().toString()),
                requests.stream().map(ClientRequest::getTargetId).toList());
        assertEquals(List.of("ACME_KR-00000001", "ACME_KR-00000002"),
                requests.stream().map(ClientRequest::getRef).toList());
        for (ClientRequest request : requests) {
            assertSame(organisation, request.getOrganisation());
            assertEquals(TargetEntity.INDIVIDUAL, request.getTarget());
            assertEquals(ClientRequestStatus.PENDING, request.getStatus());
            assertSame(file, request.getDocument());
            assertTrue(request.getAccountRequestToken().startsWith("hash:"));
        }

        // The file's record is saved against client requests, and the invitations carry the plain tokens
        verify(documentRepository).save(file);
        assertEquals(TargetEntity.CLIENT_REQUEST, file.getTarget());

        ArgumentCaptor<Map<String, String>> tokens = ArgumentCaptor.forClass(Map.class);
        verify(clientRequestNotification).queueEmailNotificationsForRequests(eq(requests), tokens.capture(),
                eq("Acme Ltd"));
        for (ClientRequest request : requests) {
            assertEquals(request.getAccountRequestToken(), "hash:" + tokens.getValue().get(request.getTargetId()));
        }

        assertEquals(List.of("ACME_KR-00000001", "ACME_KR-00000002"),
                page.getContent().stream().map(ClientRequestDTO::getRef).toList());
    }

    @Test
    @SuppressWarnings("unchecked")
    void xlsxReadsNumbersAndDatesAsTheSpreadsheetShowsThem() throws Exception {

        byte[] content;
        try (XSSFWorkbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Clients");
            Row header = sheet.createRow(0);
            String[] names = HEADER.trim().split(",");
            for (int i = 0; i < names.length; i++) {
                header.createCell(i).setCellValue(names[i]);
            }
            CellStyle dateStyle = workbook.createCellStyle();
            dateStyle.setDataFormat(workbook.getCreationHelper().createDataFormat().getFormat("dd/mm/yyyy"));

            Row created = sheet.createRow(1);
            created.createCell(0).setCellValue("Neo");
            created.createCell(2).setCellValue("Kgosi");
            created.createCell(3).setCellValue("Driver License");
            created.createCell(4).setCellValue(9264080123d); // typed as a number
            created.createCell(5).setCellValue("omakhura+neo@gmail.com");
            created.createCell(6).setCellValue(71234567d);
            Cell dateOfBirth = created.createCell(10);
            dateOfBirth.setCellValue(LocalDate.of(1985, 11, 3));
            dateOfBirth.setCellStyle(dateStyle);
            created.createCell(11).setCellValue("Botswana");
            created.createCell(12).setCellValue("married");
            created.createCell(13).setCellValue("EMPLOYED");

            sheet.createRow(2); // a blank row is skipped

            Row reused = sheet.createRow(3);
            reused.createCell(0).setCellValue("Thabo");
            reused.createCell(2).setCellValue("Moeng");
            reused.createCell(3).setCellValue("Omang");
            reused.createCell(4).setCellValue(123456789d);
            reused.createCell(5).setCellValue("omakhura+thabo@gmail.com");

            workbook.write(out);
            content = out.toByteArray();
        }

        Page<ClientRequestDTO> page = upload(content, TargetEntity.INDIVIDUAL);

        ArgumentCaptor<Individual> savedIndividual = ArgumentCaptor.forClass(Individual.class);
        verify(individualRepository, times(1)).save(savedIndividual.capture());
        Individual created = savedIndividual.getValue();
        assertEquals(IndividualIdentityType.DRIVERS_LICENCE, created.getIdentityType());
        assertEquals("9264080123", created.getIdentityNo());
        assertEquals(LocalDate.of(1985, 11, 3), created.getDateOfBirth());
        assertEquals("71234567", created.getPhoneNumbers().get(0).getPhoneNumber());
        assertEquals(MaritalStatus.MARRIED, created.getMaritalStatus());
        assertNull(created.getMiddleName());

        ArgumentCaptor<List<ClientRequest>> savedRequests = ArgumentCaptor.forClass(List.class);
        verify(clientRequestRepository).saveAll(savedRequests.capture());
        assertEquals(List.of(created.getId().toString(), stored.getId().toString()),
                savedRequests.getValue().stream().map(ClientRequest::getTargetId).toList());
        assertEquals(2, page.getContent().size());
    }

    @Test
    void aStoredIndividualKeepsTheirDetailsAndAClosedOneIsReopened() {

        stored.setClosedAt(LocalDateTime.now().minusYears(1));
        stored.setRetainUntil(LocalDate.now().plusYears(19));

        upload(csv(HEADER + "Someone,,Else,Omang,123456789,omakhura+other@gmail.com,,,,,,,,\n"),
                TargetEntity.INDIVIDUAL);

        verify(individualRepository).save(stored);
        assertEquals("Thabo", stored.getFirstName());
        assertEquals("omakhura+thabo@gmail.com", stored.getEmailAddress());
        assertNull(stored.getClosedAt());
        assertNull(stored.getRetainUntil());
        assertEquals("importer", stored.getModifiedBy());
    }

    @Test
    @SuppressWarnings("unchecked")
    void badRowsAreAllReportedAndNothingIsStored() {

        // Thabo already has a request from this organisation
        when(clientRequestRepository.count(any(Specification.class))).thenReturn(1L);

        String file = HEADER
                + NEW_ROW                                                                              // row 2: fine
                + "Lorato,,,Passport,P111,omakhura+lorato@gmail.com,,,,,1991-01-01,Botswana,Single,Employed\n" // row 3
                + "Kabo,,Sebina,Library Card,L222,omakhura+kabo@gmail.com,,,,,1991-01-01,Botswana,Single,Employed\n" // row 4
                + STORED_ROW                                                                           // row 5
                + "Neo,,Kgosi,Passport,bn0012345,omakhura+neo2@gmail.com,,,,,1990-04-12,Botswana,Single,Employed\n" // row 6
                + "Pako,,Tau,Passport,P333,not-an-email,,,,X,31/02/1990,,Single,Employed\n";               // row 7

        ClientRequestImportException refused = refused(csv(file), TargetEntity.INDIVIDUAL);

        assertTrue(refused.getMessage().startsWith("5 of 6 rows could not be imported. Nothing was imported"),
                refused.getMessage());
        List<String> errors = refused.getRowErrors();
        assertEquals(5, errors.size(), errors.toString());
        assertEquals("Row 3: Surname is required", errors.get(0));
        assertTrue(errors.get(1).startsWith("Row 4: Identity Type 'Library Card' is not recognised"), errors.get(1));
        assertEquals("Row 5: there is already a client request for OMANG 123456789 from this organisation",
                errors.get(2));
        assertEquals("Row 6: Identity No bn0012345 is already on row 2", errors.get(3));
        assertTrue(errors.get(4).startsWith("Row 7: "), errors.get(4));
        assertTrue(errors.get(4).contains("Email Address 'not-an-email' is not a valid email address"), errors.get(4));
        assertTrue(errors.get(4).contains("Sex 'X' is not recognised"), errors.get(4));
        assertTrue(errors.get(4).contains("Date of Birth '31/02/1990' is not a date"), errors.get(4));
        assertTrue(errors.get(4).contains("Nationality is required for a client who is not registered yet"),
                errors.get(4));

        verify(individualRepository, never()).save(any(Individual.class));
        verify(documentRepository, never()).save(any(Document.class));
        verify(clientRequestRepository, never()).saveAll(any(Iterable.class));
        verify(sequenceGeneratorService, never()).generateNextSequenceValue(anyString(), anyBoolean());
        verify(clientRequestNotification, never()).queueEmailNotificationsForRequests(any(), any(), any());
    }

    @Test
    void anIdentityNumberStoredUnderAnotherTypeIsReported() {

        ClientRequestImportException refused = refused(
                csv(HEADER + "Thabo,,Moeng,Passport,123456789,omakhura+thabo@gmail.com,,,,,1980-01-01,Botswana,Single,Employed\n"),
                TargetEntity.INDIVIDUAL);

        assertEquals(List.of("Row 2: Identity No 123456789 is already registered with identity type OMANG"),
                refused.getRowErrors());
    }

    @Test
    void aFileWithoutTheMandatoryColumnsOrWithoutRowsIsRefused() {

        ClientRequestImportException noColumns = refused(csv("Name,Email\nNeo Kgosi,omakhura+neo@gmail.com\n"),
                TargetEntity.INDIVIDUAL);
        assertTrue(noColumns.getMessage().contains("First Name, Surname, Identity Type, Identity No"),
                noColumns.getMessage());

        ClientRequestImportException noRows = refused(csv(HEADER + "\n,,,\n"), TargetEntity.INDIVIDUAL);
        assertEquals("The file has no client rows below its header row.", noRows.getMessage());
    }

    @Test
    void organisationClientsAreNotImported() {

        ClientRequestImportException refused = refused(csv(HEADER + NEW_ROW), TargetEntity.ORGANISATION);

        assertTrue(refused.getMessage().startsWith("Only individual clients can be imported"), refused.getMessage());
        verify(clientRequestRepository, never()).saveAll(any());
    }

    @Test
    void semicolonSeparatedCsvWithABomIsRead() {

        String file = "﻿" + HEADER.replace(',', ';')
                + "Neo;;Kgosi;Passport;BN0012345;omakhura+neo@gmail.com;;\"Plot 12; Gaborone\";;;12/04/1990;Botswana;Single;Employed\n";

        upload(csv(file), TargetEntity.INDIVIDUAL);

        ArgumentCaptor<Individual> savedIndividual = ArgumentCaptor.forClass(Individual.class);
        verify(individualRepository).save(savedIndividual.capture());
        assertEquals("Neo", savedIndividual.getValue().getFirstName());
        assertEquals("Plot 12; Gaborone", savedIndividual.getValue().getPhysicalAddress());
        assertEquals(LocalDate.of(1990, 4, 12), savedIndividual.getValue().getDateOfBirth());
    }

    private static byte[] csv(String text) {
        return text.getBytes(StandardCharsets.UTF_8);
    }

    private Page<ClientRequestDTO> upload(byte[] content, TargetEntity target) {
        DocumentDTO document = new DocumentDTO();
        document.setTargetId(organisationId.toString());
        document.setFileName("clients.csv");
        Page<ClientRequestDTO> page = service.uploadRequests(new ByteArrayInputStream(content), "importer",
                organisationId.toString(), document, target, "Acme Ltd");
        assertNotNull(page);
        return page;
    }

    /** The service wraps what it throws; the refusal is the cause. */
    private ClientRequestImportException refused(byte[] content, TargetEntity target) {
        ClientRequestServiceException thrown = assertThrows(ClientRequestServiceException.class,
                () -> upload(content, target));
        return assertInstanceOf(ClientRequestImportException.class, thrown.getCause());
    }
}
