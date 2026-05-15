package bw.co.centralkyc.settings.kyc;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
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
                kycFieldGroupRepository,
                kycFieldGroupMapper,
                groupFieldRepository,
                settingsRepository,
                messageSource);
    }

    @Test
    void saveNewIndividualGroupAttachesSettingsAndSetsFieldBackRef() throws Exception {
        KycFieldGroupDTO input = new KycFieldGroupDTO();
        input.setLabel("Identity");
        input.setDescription("Identity fields");
        input.setTargetType(TargetEntity.INDIVIDUAL);
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

        KycFieldGroupDTO actual = service.save(input);

        assertSame(expected, actual);
        assertSame(entity, field.getKycFieldGroup());
        assertSame(settings, entity.getIndividualSettings());
        assertTrue(settings.getIndividualKycFieldGroups().contains(entity));
    }

    @Test
    void removeThrowsWhenGroupDoesNotExist() {
        UUID id = UUID.randomUUID();
        when(messageSource.getMessage("kycFieldGroup.notFound", new Object[] { id.toString() }, null))
                .thenReturn("not found");
        when(kycFieldGroupRepository.existsById(id)).thenReturn(false);

        assertThrows(KycFieldGroupServiceException.class, () -> service.remove(id.toString()));
    }

    @Test
    void removeFieldDeletesFieldAndReturnsMappedGroup() throws Exception {
        UUID groupId = UUID.randomUUID();
        UUID fieldId = UUID.randomUUID();
        KycFieldGroup entity = KycFieldGroup.Factory.newInstance();
        KycFieldGroupDTO expected = new KycFieldGroupDTO();

        when(kycFieldGroupRepository.findById(groupId)).thenReturn(Optional.of(entity));
        when(kycFieldGroupMapper.toKycFieldGroupDTO(entity)).thenReturn(expected);

        KycFieldGroupDTO actual = service.removeField(groupId.toString(), fieldId.toString());

        assertSame(expected, actual);
        verify(groupFieldRepository).deleteById(fieldId);
    }

    @Test
    void findByIdReturnsMappedGroup() throws Exception {
        UUID id = UUID.randomUUID();
        KycFieldGroup entity = KycFieldGroup.Factory.newInstance();
        KycFieldGroupDTO expected = new KycFieldGroupDTO();

        when(kycFieldGroupRepository.findById(id)).thenReturn(Optional.of(entity));
        when(kycFieldGroupMapper.toKycFieldGroupDTO(entity)).thenReturn(expected);

        KycFieldGroupDTO actual = service.findById(id.toString());

        assertSame(expected, actual);
    }

    @Test
    void findByTargetReturnsMappedCollection() throws Exception {
        List<KycFieldGroup> entities = List.of(KycFieldGroup.Factory.newInstance());
        List<KycFieldGroupDTO> expected = List.of(new KycFieldGroupDTO());

        when(kycFieldGroupRepository.findAll(org.mockito.ArgumentMatchers.<org.springframework.data.jpa.domain.Specification<KycFieldGroup>>any()))
                .thenReturn(entities);
        when(kycFieldGroupMapper.toKycFieldGroupDTOCollection(entities)).thenReturn(expected);

        List<KycFieldGroupDTO> actual = service.findByTarget(TargetEntity.INDIVIDUAL);

        assertEquals(1, actual.size());
        assertSame(expected.get(0), actual.get(0));
    }

    @Test
    void removeDeletesWhenGroupExists() throws Exception {
        UUID id = UUID.randomUUID();
        when(kycFieldGroupRepository.existsById(id)).thenReturn(true);

        boolean removed = service.remove(id.toString());

        assertTrue(removed);
        verify(kycFieldGroupRepository).deleteById(id);
    }

    @Test
    void serviceBaseGuardsRejectInvalidArguments() {
        assertThrows(IllegalArgumentException.class, () -> service.findById(null));
        assertThrows(IllegalArgumentException.class, () -> service.findById(" "));
        assertThrows(IllegalArgumentException.class, () -> service.save(null));
        assertThrows(IllegalArgumentException.class, () -> service.remove(null));
        assertThrows(IllegalArgumentException.class, () -> service.remove("\n"));
        assertThrows(IllegalArgumentException.class, () -> service.findByTarget(null));
        assertThrows(IllegalArgumentException.class, () -> service.removeField(null, UUID.randomUUID().toString()));
        assertThrows(IllegalArgumentException.class, () -> service.removeField(" ", UUID.randomUUID().toString()));
        assertThrows(IllegalArgumentException.class, () -> service.removeField(UUID.randomUUID().toString(), null));
        assertThrows(IllegalArgumentException.class, () -> service.removeField(UUID.randomUUID().toString(), "\t"));

        KycFieldGroupDTO missingLabel = validFieldGroup();
        missingLabel.setLabel(" ");
        assertThrows(IllegalArgumentException.class, () -> service.save(missingLabel));

        KycFieldGroupDTO missingDescription = validFieldGroup();
        missingDescription.setDescription(null);
        assertThrows(IllegalArgumentException.class, () -> service.save(missingDescription));

        KycFieldGroupDTO missingTargetType = validFieldGroup();
        missingTargetType.setTargetType(null);
        assertThrows(IllegalArgumentException.class, () -> service.save(missingTargetType));
    }

    private static KycFieldGroupDTO validFieldGroup() {
        KycFieldGroupDTO input = new KycFieldGroupDTO();
        input.setLabel("Identity");
        input.setDescription("Identity fields");
        input.setTargetType(TargetEntity.INDIVIDUAL);
        return input;
    }
}