package bw.co.kyvera.organisation.client;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.security.crypto.password.PasswordEncoder;

import bw.co.kyvera.TargetEntity;
import bw.co.kyvera.SearchObject;
import bw.co.kyvera.document.DocumentMapper;
import bw.co.kyvera.document.DocumentDTO;
import bw.co.kyvera.document.DocumentRepository;
import bw.co.kyvera.individual.IndividualMapper;
import bw.co.kyvera.individual.IndividualRepository;
import bw.co.kyvera.messaging.ClientRequestNotification;
import bw.co.kyvera.organisation.Organisation;
import bw.co.kyvera.organisation.OrganisationRepository;
import bw.co.kyvera.sequence.SequenceGenerator;
import bw.co.kyvera.sequence.SequenceGeneratorRepository;
import bw.co.kyvera.sequence.SequenceGeneratorService;
import bw.co.kyvera.settings.SettingsMapper;
import bw.co.kyvera.settings.SettingsRepository;

@ExtendWith(MockitoExtension.class)
class ClientRequestServiceImplTest {

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
    @Mock
    private MessageSource messageSource;

    private ClientRequestServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new ClientRequestServiceImpl(
                clientRequestRepository,
                clientRequestMapper,
                passwordEncoder,
                clientRequestNotification,
                individualRepository,
                individualMapper,
                organisationRepository,
                sequenceGeneratorRepository,
                sequenceGeneratorService,
                documentRepository,
                documentMapper,
                settingsRepository,
                settingsMapper,
                messageSource);
    }

    @Test
    void findByIdReturnsMappedRequest() throws Exception {
        UUID id = UUID.randomUUID();
        ClientRequest request = ClientRequest.Factory.newInstance();
        ClientRequestDTO expected = new ClientRequestDTO();

        when(clientRequestRepository.findById(id)).thenReturn(Optional.of(request));
        when(clientRequestMapper.toClientRequestDTO(request)).thenReturn(expected);

        ClientRequestDTO actual = service.findById(id.toString());

        assertSame(expected, actual);
    }

    @Test
    void saveGeneratesRefWithoutQueuingNotificationForExistingRequest() throws Exception {
        ClientRequestDTO input = new ClientRequestDTO();
        input.setId(UUID.randomUUID().toString());
        input.setRef(null);
        input.setName("Client A");
        input.setRegistration("REG-100");
        input.setStatus(ClientRequestStatus.PENDING);
        input.setOrganisationId(UUID.randomUUID().toString());
        input.setOrganisation("ORG");
        input.setOrganisationCode("ORG1");
        input.setOrganisationRegistrationNo("ORG-REG-1");
        input.setTarget(TargetEntity.INDIVIDUAL);
        input.setTargetId(UUID.randomUUID().toString());

        ClientRequest entity = ClientRequest.Factory.newInstance();
        Organisation organisation = Organisation.Factory.newInstance();
        organisation.setCode("ORG");
        entity.setOrganisation(organisation);
        ClientRequestDTO expected = new ClientRequestDTO();

        when(clientRequestMapper.clientRequestDTOToEntity(input)).thenReturn(entity);
        when(sequenceGeneratorRepository.findByName("ORG_CLIENT_REQUEST_REF")).thenReturn(Optional.empty());
        when(sequenceGeneratorRepository.save(any(SequenceGenerator.class))).thenAnswer(inv -> inv.getArgument(0));
        when(sequenceGeneratorService.generateNextSequenceValue("ORG_CLIENT_REQUEST_REF", true)).thenReturn("ORG_KR-00000001");
        when(clientRequestRepository.save(entity)).thenReturn(entity);
        when(clientRequestMapper.toClientRequestDTO(entity)).thenReturn(expected);

        ClientRequestDTO actual = service.save(input);

        ArgumentCaptor<SequenceGenerator> captor = ArgumentCaptor.forClass(SequenceGenerator.class);
        verify(sequenceGeneratorRepository).save(captor.capture());
        assertSame(expected, actual);
        verify(clientRequestNotification, never()).queueEmailNotificationsForRequests(any(), any(), any());
    }

    @Test
    void removeThrowsWhenRequestDoesNotExist() {
        UUID id = UUID.randomUUID();
        when(clientRequestRepository.existsById(id)).thenReturn(false);

        assertThrows(ClientRequestServiceException.class, () -> service.remove(id.toString()));
    }

    @Test
    void serviceBaseFindByIdRejectsNullAndBlank() {
        assertThrows(IllegalArgumentException.class, () -> service.findById(null));
        assertThrows(IllegalArgumentException.class, () -> service.findById("  "));
    }

    @Test
    void serviceBaseSaveRejectsNullRequest() {
        assertThrows(IllegalArgumentException.class, () -> service.save(null));
    }

    @Test
    void serviceBaseSaveRejectsMissingRequiredFields() {
        ClientRequestDTO missingName = validClientRequest();
        missingName.setName("  ");

        ClientRequestDTO missingRegistration = validClientRequest();
        missingRegistration.setRegistration(null);

        ClientRequestDTO missingStatus = validClientRequest();
        missingStatus.setStatus(null);

        ClientRequestDTO missingOrganisationId = validClientRequest();
        missingOrganisationId.setOrganisationId("\t");

        ClientRequestDTO missingOrganisation = validClientRequest();
        missingOrganisation.setOrganisation("");

        ClientRequestDTO missingOrganisationRegistrationNo = validClientRequest();
        missingOrganisationRegistrationNo.setOrganisationRegistrationNo("  ");

        ClientRequestDTO missingTarget = validClientRequest();
        missingTarget.setTarget(null);

        ClientRequestDTO missingTargetId = validClientRequest();
        missingTargetId.setTargetId("");

        assertThrows(IllegalArgumentException.class, () -> service.save(missingName));
        assertThrows(IllegalArgumentException.class, () -> service.save(missingRegistration));
        assertThrows(IllegalArgumentException.class, () -> service.save(missingStatus));
        assertThrows(IllegalArgumentException.class, () -> service.save(missingOrganisationId));
        assertThrows(IllegalArgumentException.class, () -> service.save(missingOrganisation));
        assertThrows(IllegalArgumentException.class, () -> service.save(missingOrganisationRegistrationNo));
        assertThrows(IllegalArgumentException.class, () -> service.save(missingTarget));
        assertThrows(IllegalArgumentException.class, () -> service.save(missingTargetId));
    }

    @Test
    void serviceBaseRemoveRejectsNullAndBlank() {
        assertThrows(IllegalArgumentException.class, () -> service.remove(null));
        assertThrows(IllegalArgumentException.class, () -> service.remove("\n\t"));
    }

    @Test
    void serviceBaseSearchRejectsNullCriteriaForListAndPaged() {
        assertThrows(IllegalArgumentException.class, () -> service.search((ClientRequestSearchCriteria) null, Set.of()));
        assertThrows(IllegalArgumentException.class, () -> service.search((SearchObject<ClientRequestSearchCriteria>) null));
    }

    @Test
    void serviceBaseFindByOrganisationRejectsNullAndBlank() {
        assertThrows(IllegalArgumentException.class, () -> service.findByOrganisation(null));
        assertThrows(IllegalArgumentException.class, () -> service.findByOrganisation(" "));
        assertThrows(IllegalArgumentException.class, () -> service.findByOrganisation(null, 0, 10));
        assertThrows(IllegalArgumentException.class, () -> service.findByOrganisation("\t", 0, 10));
    }

    @Test
    void serviceBaseFindByStatusRejectsNull() {
        assertThrows(IllegalArgumentException.class, () -> service.findByStatus(null));
        assertThrows(IllegalArgumentException.class, () -> service.findByStatus(null, 0, 10));
    }

    @Test
    void serviceBaseUploadRequestsRejectsInvalidInput() {
        DocumentDTO validDocument = validUploadDocument();

        assertThrows(IllegalArgumentException.class,
                () -> service.uploadRequests(null, "user", UUID.randomUUID().toString(), validDocument, TargetEntity.INDIVIDUAL, "ORG"));
        assertThrows(IllegalArgumentException.class,
                () -> service.uploadRequests(new ByteArrayInputStream(new byte[0]), " ", UUID.randomUUID().toString(), validDocument, TargetEntity.INDIVIDUAL, "ORG"));
        assertThrows(IllegalArgumentException.class,
                () -> service.uploadRequests(new ByteArrayInputStream(new byte[0]), "user", "\t", validDocument, TargetEntity.INDIVIDUAL, "ORG"));
        assertThrows(IllegalArgumentException.class,
                () -> service.uploadRequests(new ByteArrayInputStream(new byte[0]), "user", UUID.randomUUID().toString(), null, TargetEntity.INDIVIDUAL, "ORG"));

        DocumentDTO missingTargetId = validUploadDocument();
        missingTargetId.setTargetId(" ");
        assertThrows(IllegalArgumentException.class,
                () -> service.uploadRequests(new ByteArrayInputStream(new byte[0]), "user", UUID.randomUUID().toString(), missingTargetId, TargetEntity.INDIVIDUAL, "ORG"));

        DocumentDTO missingFileName = validUploadDocument();
        missingFileName.setFileName(null);
        assertThrows(IllegalArgumentException.class,
                () -> service.uploadRequests(new ByteArrayInputStream(new byte[0]), "user", UUID.randomUUID().toString(), missingFileName, TargetEntity.INDIVIDUAL, "ORG"));

        assertThrows(IllegalArgumentException.class,
                () -> service.uploadRequests(new ByteArrayInputStream(new byte[0]), "user", UUID.randomUUID().toString(), validDocument, null, "ORG"));
        assertThrows(IllegalArgumentException.class,
                () -> service.uploadRequests(new ByteArrayInputStream(new byte[0]), "user", UUID.randomUUID().toString(), validDocument, TargetEntity.INDIVIDUAL, " "));
    }

    @Test
    void serviceBaseFindByIndividualRejectsNullAndBlank() {
        assertThrows(IllegalArgumentException.class, () -> service.findByIndividual(null));
        assertThrows(IllegalArgumentException.class, () -> service.findByIndividual(" "));
        assertThrows(IllegalArgumentException.class, () -> service.findByIndividual(null, 0, 10));
        assertThrows(IllegalArgumentException.class, () -> service.findByIndividual("\n", 0, 10));
    }

    @Test
    void serviceBaseFindByDocumentRejectsNullAndBlank() {
        assertThrows(IllegalArgumentException.class, () -> service.findByDocument(null));
        assertThrows(IllegalArgumentException.class, () -> service.findByDocument(" "));
        assertThrows(IllegalArgumentException.class, () -> service.findByDocument(null, 0, 10));
        assertThrows(IllegalArgumentException.class, () -> service.findByDocument("\n", 0, 10));
    }

    @Test
    void serviceBaseFindByTargetRejectsNullTargetAndBlankTargetId() {
        assertThrows(IllegalArgumentException.class,
                () -> service.findByTarget(null, UUID.randomUUID().toString()));
        assertThrows(IllegalArgumentException.class,
                () -> service.findByTarget(TargetEntity.INDIVIDUAL, " "));

        assertThrows(IllegalArgumentException.class,
                () -> service.findByTarget(null, UUID.randomUUID().toString(), 0, 10));
        assertThrows(IllegalArgumentException.class,
                () -> service.findByTarget(TargetEntity.INDIVIDUAL, "\t", 0, 10));
    }

    @Test
    void serviceBaseFindByTargetAndOrganisationRejectsNullTarget() {
        assertThrows(IllegalArgumentException.class,
                () -> service.findByTargetAndOrganisation(null, UUID.randomUUID().toString(), UUID.randomUUID().toString()));
        assertThrows(IllegalArgumentException.class,
                () -> service.findByTargetAndOrganisation(null, UUID.randomUUID().toString(), UUID.randomUUID().toString(), 0, 10));
    }

    @Test
    void serviceBaseUpdateStatusRejectsNullAndBlankIdAndNullStatus() {
        assertThrows(IllegalArgumentException.class, () -> service.updateStatus(null, ClientRequestStatus.PENDING));
        assertThrows(IllegalArgumentException.class, () -> service.updateStatus(" ", ClientRequestStatus.PENDING));
        assertThrows(IllegalArgumentException.class, () -> service.updateStatus(UUID.randomUUID().toString(), null));
    }

    @Test
    void serviceBaseConfirmTokenRejectsInvalidArguments() {
        assertThrows(IllegalArgumentException.class, () -> service.confirmToken(null, "token"));
        assertThrows(IllegalArgumentException.class, () -> service.confirmToken(" ", "token"));
        assertThrows(IllegalArgumentException.class, () -> service.confirmToken(UUID.randomUUID().toString(), null));
        assertThrows(IllegalArgumentException.class, () -> service.confirmToken(UUID.randomUUID().toString(), "  "));
    }

    @Test
    void serviceBaseCountByStatusRejectsNull() {
        assertThrows(IllegalArgumentException.class, () -> service.countByStatus(null));
    }

    @Test
    void serviceBaseCountByStatusAndOrganisationRejectsNullOrBlankArguments() {
        assertThrows(IllegalArgumentException.class, () -> service.countByStatusAndOrganisationId(null, UUID.randomUUID().toString()));
        assertThrows(IllegalArgumentException.class,
                () -> service.countByStatusAndOrganisationId(ClientRequestStatus.PENDING, "\t"));
    }

    @Test
    void serviceBaseConfirmRegistrationRejectsInvalidArguments() {
        assertThrows(IllegalArgumentException.class,
                () -> service.confirmRegistration(null, Boolean.TRUE, "registration-token"));
        assertThrows(IllegalArgumentException.class,
                () -> service.confirmRegistration("\n", Boolean.FALSE, "registration-token"));
        assertThrows(IllegalArgumentException.class,
                () -> service.confirmRegistration(UUID.randomUUID().toString(), Boolean.TRUE, null));
        assertThrows(IllegalArgumentException.class,
                () -> service.confirmRegistration(UUID.randomUUID().toString(), Boolean.TRUE, "\t"));
    }

    @Test
    void serviceBaseFindUserReadyRequestsRoutesUnsupportedOperations() {
        assertThrows(ClientRequestServiceException.class, () -> service.findUserReadyRequests());
        assertThrows(ClientRequestServiceException.class, () -> service.findUserReadyRequests(0, 10));
    }

    private static ClientRequestDTO validClientRequest() {
        ClientRequestDTO input = new ClientRequestDTO();
        input.setName("Client A");
        input.setRegistration("REG-100");
        input.setStatus(ClientRequestStatus.PENDING);
        input.setOrganisationId(UUID.randomUUID().toString());
        input.setOrganisation("ORG");
        input.setOrganisationRegistrationNo("ORG-REG-1");
        input.setTarget(TargetEntity.INDIVIDUAL);
        input.setTargetId(UUID.randomUUID().toString());
        return input;
    }

    private static DocumentDTO validUploadDocument() {
        DocumentDTO document = new DocumentDTO();
        document.setTargetId(UUID.randomUUID().toString());
        document.setFileName("requests.csv");
        return document;
    }
}