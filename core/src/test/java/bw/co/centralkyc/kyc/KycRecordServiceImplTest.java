package bw.co.centralkyc.kyc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import bw.co.centralkyc.SourceOfFunds;
import bw.co.centralkyc.TargetEntity;
import bw.co.centralkyc.document.DocumentRepository;
import bw.co.centralkyc.individual.IndividualRepository;
import bw.co.centralkyc.individual.employment.EmploymentRecord;
import bw.co.centralkyc.organisation.OrganisationRepository;
import bw.co.centralkyc.sequence.SequenceGenerator;
import bw.co.centralkyc.sequence.SequenceGeneratorRepository;
import bw.co.centralkyc.sequence.SequenceGeneratorService;
import bw.co.centralkyc.settings.Settings;
import bw.co.centralkyc.settings.SettingsRepository;

@ExtendWith(MockitoExtension.class)
class KycRecordServiceImplTest {

    @Mock
    private KycRecordDao kycRecordDao;
    @Mock
    private KycRecordRepository kycRecordRepository;
    @Mock
    private KycRecordMapper kycRecordMapper;
    @Mock
    private MessageSource messageSource;
    @Mock
    private SettingsRepository settingsRepository;
    @Mock
    private IndividualRepository individualRepository;
    @Mock
    private DocumentRepository documentRepository;
    @Mock
    private SequenceGeneratorRepository sequenceGeneratorRepository;
    @Mock
    private SequenceGeneratorService sequenceGeneratorService;
    @Mock
    private OrganisationRepository organisationRepository;

    private KycRecordServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new KycRecordServiceImpl(
                kycRecordDao,
                kycRecordRepository,
                kycRecordMapper,
                messageSource,
                settingsRepository,
                kycRecordMapper,
                individualRepository,
                documentRepository,
                sequenceGeneratorRepository,
                sequenceGeneratorService,
                organisationRepository);
    }

    @Test
    void handleSaveInitializesDatesClearsEmploymentAndGeneratesRef() throws Exception {
        KycRecordDTO input = new KycRecordDTO();
        KycRecord entity = KycRecord.Factory.newInstance();
        entity.setKycStatus(KycComplianceStatus.INCOMPLETE);
        entity.setTarget(TargetEntity.INDIVIDUAL);
        entity.setTargetId("target-1");
        entity.setSourceOfFunds(List.of(SourceOfFunds.SALARY));
        entity.setEmploymentRecord(EmploymentRecord.Factory.newInstance());

        Settings settings = Settings.Factory.newInstance();
        settings.setKycDuration(null);
        KycRecordDTO expected = new KycRecordDTO();

        when(settingsRepository.findAll()).thenReturn(List.of(settings));
        when(kycRecordMapper.kycRecordDTOToEntity(input)).thenReturn(entity);
        when(sequenceGeneratorRepository.findByName("KYC_RECORD_REF")).thenReturn(Optional.empty());
        when(sequenceGeneratorRepository.save(any(SequenceGenerator.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        when(sequenceGeneratorService.generateNextSequenceValue("KYC_RECORD_REF", true)).thenReturn("KR-2026/0000001");
        when(kycRecordRepository.save(entity)).thenReturn(entity);
        when(kycRecordDao.toKycRecordDTO(entity)).thenReturn(expected);

        KycRecordDTO actual = service.handleSave(input);

        ArgumentCaptor<SequenceGenerator> captor = ArgumentCaptor.forClass(SequenceGenerator.class);
        verify(sequenceGeneratorRepository).save(captor.capture());
        assertEquals(TargetEntity.KYC_RECORD, captor.getValue().getTargetEntity());
        assertNull(entity.getEmploymentRecord());
        assertNotNull(entity.getUploadDate());
        assertEquals(entity.getUploadDate().plusYears(2), entity.getExpiryDate());
        assertEquals("KR-2026/0000001", entity.getRef());
        assertSame(expected, actual);
    }

    @Test
    void handleFindByIdReturnsMappedRecord() throws Exception {
        KycRecord entity = KycRecord.Factory.newInstance();
        entity.setId(java.util.UUID.randomUUID());
        KycRecordDTO expected = new KycRecordDTO();

        when(kycRecordRepository.findById(entity.getId())).thenReturn(Optional.of(entity));
        when(kycRecordMapper.toKycRecordDTO(entity)).thenReturn(expected);

        KycRecordDTO actual = service.handleFindById(entity.getId().toString());

        assertSame(expected, actual);
    }
}