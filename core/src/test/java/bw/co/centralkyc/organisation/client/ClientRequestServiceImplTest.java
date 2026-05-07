package bw.co.centralkyc.organisation.client;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.security.crypto.password.PasswordEncoder;

import bw.co.centralkyc.TargetEntity;
import bw.co.centralkyc.document.DocumentDao;
import bw.co.centralkyc.document.DocumentMapper;
import bw.co.centralkyc.document.DocumentRepository;
import bw.co.centralkyc.individual.IndividualDao;
import bw.co.centralkyc.individual.IndividualMapper;
import bw.co.centralkyc.individual.IndividualRepository;
import bw.co.centralkyc.messaging.ClientRequestNotification;
import bw.co.centralkyc.organisation.Organisation;
import bw.co.centralkyc.sequence.SequenceGenerator;
import bw.co.centralkyc.sequence.SequenceGeneratorRepository;
import bw.co.centralkyc.sequence.SequenceGeneratorService;
import bw.co.centralkyc.settings.SettingsDao;
import bw.co.centralkyc.settings.SettingsMapper;
import bw.co.centralkyc.settings.SettingsRepository;

@ExtendWith(MockitoExtension.class)
class ClientRequestServiceImplTest {

    @Mock
    private ClientRequestDao clientRequestDao;
    @Mock
    private ClientRequestRepository clientRequestRepository;
    @Mock
    private ClientRequestMapper clientRequestMapper;
    @Mock
    private IndividualDao individualDao;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private ClientRequestNotification clientRequestNotification;
    @Mock
    private IndividualRepository individualRepository;
    @Mock
    private IndividualMapper individualMapper;
    @Mock
    private DocumentDao documentDao;
    @Mock
    private SequenceGeneratorRepository sequenceGeneratorRepository;
    @Mock
    private SequenceGeneratorService sequenceGeneratorService;
    @Mock
    private DocumentRepository documentRepository;
    @Mock
    private DocumentMapper documentMapper;
    @Mock
    private SettingsDao settingsDao;
    @Mock
    private SettingsRepository settingsRepository;
    @Mock
    private SettingsMapper settingsMapper;
    @Mock
    private MessageSource messageSource;

    private ClientRequestServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new ClientRequestServiceImpl(
                clientRequestDao,
                clientRequestRepository,
                clientRequestMapper,
                individualDao,
                passwordEncoder,
                clientRequestNotification,
                individualRepository,
                individualMapper,
                documentDao,
                sequenceGeneratorRepository,
                sequenceGeneratorService,
                documentRepository,
                documentMapper,
                settingsDao,
                settingsRepository,
                settingsMapper,
                messageSource);
    }

    @Test
    void handleFindByIdReturnsMappedRequest() throws Exception {
        UUID id = UUID.randomUUID();
        ClientRequest request = ClientRequest.Factory.newInstance();
        ClientRequestDTO expected = new ClientRequestDTO();

        when(clientRequestRepository.findById(id)).thenReturn(Optional.of(request));
        when(clientRequestDao.toClientRequestDTO(request)).thenReturn(expected);

        ClientRequestDTO actual = service.handleFindById(id.toString());

        assertSame(expected, actual);
    }

    @Test
    void handleSaveGeneratesRefWithoutQueuingNotificationForExistingRequest() throws Exception {
        ClientRequestDTO input = new ClientRequestDTO();
        input.setId(UUID.randomUUID().toString());
        input.setRef(null);
        input.setOrganisation("ORG");

        ClientRequest entity = ClientRequest.Factory.newInstance();
        Organisation organisation = Organisation.Factory.newInstance();
        organisation.setCode("ORG");
        entity.setOrganisation(organisation);
        ClientRequestDTO expected = new ClientRequestDTO();

        when(clientRequestDao.clientRequestDTOToEntity(input)).thenReturn(entity);
        when(sequenceGeneratorRepository.findByName("ORG_CLIENT_REQUEST_REF")).thenReturn(Optional.empty());
        when(sequenceGeneratorRepository.save(any(SequenceGenerator.class))).thenAnswer(inv -> inv.getArgument(0));
        when(sequenceGeneratorService.generateNextSequenceValue("ORG_CLIENT_REQUEST_REF", true)).thenReturn("ORG_KR-00000001");
        when(clientRequestRepository.save(entity)).thenReturn(entity);
        when(clientRequestDao.toClientRequestDTO(entity)).thenReturn(expected);

        ClientRequestDTO actual = service.handleSave(input);

        ArgumentCaptor<SequenceGenerator> captor = ArgumentCaptor.forClass(SequenceGenerator.class);
        verify(sequenceGeneratorRepository).save(captor.capture());
        assertSame(expected, actual);
        verify(clientRequestNotification, never()).queueEmailNotificationsForRequests(any(), any(), any());
    }

    @Test
    void handleRemoveThrowsWhenRequestDoesNotExist() {
        UUID id = UUID.randomUUID();
        when(clientRequestRepository.existsById(id)).thenReturn(false);

        assertThrows(ClientRequestServiceException.class, () -> service.handleRemove(id.toString()));
    }
}