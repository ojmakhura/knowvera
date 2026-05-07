package bw.co.centralkyc.settings.kyc;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import bw.co.centralkyc.TargetEntity;
import bw.co.centralkyc.settings.Settings;
import bw.co.centralkyc.settings.SettingsRepository;

@ExtendWith(MockitoExtension.class)
class KycFieldGroupServiceImplTest {

    @Mock
    private KycFieldGroupDao kycFieldGroupDao;
    @Mock
    private KycFieldGroupRepository kycFieldGroupRepository;
    @Mock
    private KycFieldGroupMapper kycFieldGroupMapper;
    @Mock
    private GroupFieldRepository groupFieldRepository;
    @Mock
    private SettingsRepository settingsRepository;
    @Mock
    private MessageSource messageSource;

    private KycFieldGroupServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new KycFieldGroupServiceImpl(
                kycFieldGroupDao,
                kycFieldGroupRepository,
                kycFieldGroupMapper,
                groupFieldRepository,
                settingsRepository,
                messageSource);
    }

    @Test
    void handleSaveNewIndividualGroupAttachesSettingsAndSetsFieldBackRef() throws Exception {
        KycFieldGroupDTO input = new KycFieldGroupDTO();
        KycFieldGroup entity = KycFieldGroup.Factory.newInstance();
        entity.setTargetType(TargetEntity.INDIVIDUAL);
        GroupField field = GroupField.Factory.newInstance();
        entity.setGroupFields(List.of(field));

        Settings settings = Settings.Factory.newInstance();
        KycFieldGroupDTO expected = new KycFieldGroupDTO();

        when(kycFieldGroupMapper.kycFieldGroupDTOToEntity(input)).thenReturn(entity);
        when(settingsRepository.findAll()).thenReturn(List.of(settings));
        when(kycFieldGroupRepository.save(entity)).thenReturn(entity);
        when(kycFieldGroupMapper.toKycFieldGroupDTO(entity)).thenReturn(expected);

        KycFieldGroupDTO actual = service.handleSave(input);

        assertSame(expected, actual);
        assertSame(entity, field.getKycFieldGroup());
        assertSame(settings, entity.getIndividualSettings());
        assertTrue(settings.getIndividualKycFieldGroups().contains(entity));
    }

    @Test
    void handleRemoveThrowsWhenGroupDoesNotExist() {
        UUID id = UUID.randomUUID();
        when(messageSource.getMessage("kycFieldGroup.notFound", new Object[] { id.toString() }, null))
                .thenReturn("not found");
        when(kycFieldGroupRepository.existsById(id)).thenReturn(false);

        assertThrows(KycFieldGroupServiceException.class, () -> service.handleRemove(id.toString()));
    }

    @Test
    void handleRemoveFieldDeletesFieldAndReturnsMappedGroup() throws Exception {
        UUID groupId = UUID.randomUUID();
        UUID fieldId = UUID.randomUUID();
        KycFieldGroup entity = KycFieldGroup.Factory.newInstance();
        KycFieldGroupDTO expected = new KycFieldGroupDTO();

        when(kycFieldGroupRepository.findById(groupId)).thenReturn(Optional.of(entity));
        when(kycFieldGroupMapper.toKycFieldGroupDTO(entity)).thenReturn(expected);

        KycFieldGroupDTO actual = service.handleRemoveField(groupId.toString(), fieldId.toString());

        assertSame(expected, actual);
        verify(groupFieldRepository).deleteById(fieldId);
    }
}