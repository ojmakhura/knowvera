package bw.co.kyvera.organisation.document;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import bw.co.kyvera.document.DocumentMapper;
import bw.co.kyvera.document.DocumentRepository;
import bw.co.kyvera.individual.IndividualMapper;
import bw.co.kyvera.individual.IndividualRepository;
import bw.co.kyvera.SearchObject;

@ExtendWith(MockitoExtension.class)
class OrganisationDocumentServiceImplTest {

    @Mock
    private OrganisationDocumentRepository organisationDocumentRepository;
    @Mock
    private OrganisationDocumentMapper organisationDocumentMapper;
    @Mock
    private IndividualRepository individualRepository;
    @Mock
    private IndividualMapper individualMapper;
    @Mock
    private DocumentRepository documentRepository;
    @Mock
    private DocumentMapper documentMapper;
    @Mock
    private MessageSource messageSource;

    private OrganisationDocumentServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new OrganisationDocumentServiceImpl(
                organisationDocumentRepository,
                organisationDocumentMapper,
                individualRepository,
                individualMapper,
                documentRepository,
                documentMapper,
                messageSource);
    }

    @Test
    void findByIdLoadsAndMapsOrganisationDocument() throws Exception {
        UUID id = UUID.randomUUID();
        OrganisationDocument entity = OrganisationDocument.Factory.newInstance();
        OrganisationDocumentDTO expected = new OrganisationDocumentDTO();

        when(organisationDocumentRepository.getReferenceById(id)).thenReturn(entity);
        when(organisationDocumentMapper.toOrganisationDocumentDTO(entity)).thenReturn(expected);

        OrganisationDocumentDTO actual = service.findById(id.toString());

        assertSame(expected, actual);
    }

    @Test
    void saveMapsPersistsAndMapsBack() throws Exception {
        OrganisationDocumentDTO input = new OrganisationDocumentDTO();
        input.setStatus(OrganisationDocumentStatus.ACTIVE);
        input.setOrganisationId(UUID.randomUUID().toString());
        input.setOrganisation("ORG");
        input.setOrganisationRegistrationNo("REG-1");
        input.setTarget(bw.co.kyvera.TargetEntity.ORGANISATION);
        input.setTargetId(UUID.randomUUID().toString());
        input.setFileName("org-doc.pdf");
        OrganisationDocument entity = OrganisationDocument.Factory.newInstance();
        OrganisationDocumentDTO expected = new OrganisationDocumentDTO();

        when(organisationDocumentMapper.organisationDocumentDTOToEntity(input)).thenReturn(entity);
        when(organisationDocumentRepository.save(entity)).thenReturn(entity);
        when(organisationDocumentMapper.toOrganisationDocumentDTO(entity)).thenReturn(expected);

        OrganisationDocumentDTO actual = service.save(input);

        assertSame(expected, actual);
    }

    @Test
    void getAllDelegatesToMapper() throws Exception {
        List<OrganisationDocument> entities = List.of(OrganisationDocument.Factory.newInstance());
        List<OrganisationDocumentDTO> expected = List.of(new OrganisationDocumentDTO());

        when(organisationDocumentRepository.findAll()).thenReturn(entities);
        when(organisationDocumentMapper.toOrganisationDocumentDTOCollection(entities)).thenReturn(expected);

        List<OrganisationDocumentDTO> actual = service.getAll();

        assertSame(expected, actual);
    }

    @Test
    void removeDeletesById() throws Exception {
        UUID id = UUID.randomUUID();

        boolean removed = service.remove(id.toString());

        assertTrue(removed);
        verify(organisationDocumentRepository).deleteById(id);
    }

    @Test
    void getAllWithPagingMapsPageContent() throws Exception {
        OrganisationDocument entity = OrganisationDocument.Factory.newInstance();
        OrganisationDocumentDTO dto = new OrganisationDocumentDTO();
        Page<OrganisationDocument> page = new PageImpl<>(List.of(entity));

        when(organisationDocumentRepository.findAll(PageRequest.of(0, 5))).thenReturn(page);
        when(organisationDocumentMapper.toOrganisationDocumentDTO(entity)).thenReturn(dto);

        Page<OrganisationDocumentDTO> actual = service.getAll(0, 5);

        assertEquals(1, actual.getContent().size());
        assertSame(dto, actual.getContent().get(0));
    }

    @Test
    void serviceBaseRoutesUnsupportedOperationsToServiceException() {
        assertThrows(OrganisationDocumentServiceException.class,
                () -> service.search(new OrganisationDocumentSearchCriteria(), Set.of()));

        SearchObject<OrganisationDocumentSearchCriteria> search = new SearchObject<>();
        search.setCriteria(new OrganisationDocumentSearchCriteria());
        search.setPageNumber(0);
        search.setPageSize(10);
        assertThrows(OrganisationDocumentServiceException.class, () -> service.search(search));

        assertThrows(OrganisationDocumentServiceException.class,
                () -> service.findByOrganisation(UUID.randomUUID().toString()));
        assertThrows(OrganisationDocumentServiceException.class,
                () -> service.findByOrganisation(UUID.randomUUID().toString(), 0, 10));
        assertThrows(OrganisationDocumentServiceException.class,
                () -> service.findByStatus(OrganisationDocumentStatus.ACTIVE));
        assertThrows(OrganisationDocumentServiceException.class,
                () -> service.findByStatus(OrganisationDocumentStatus.ACTIVE, 0, 10));
    }

    @Test
    void serviceBaseGuardsRejectInvalidArguments() {
        assertThrows(IllegalArgumentException.class, () -> service.findById(null));
        assertThrows(IllegalArgumentException.class, () -> service.findById(" "));
        assertThrows(IllegalArgumentException.class, () -> service.save(null));
        assertThrows(IllegalArgumentException.class, () -> service.remove(null));
        assertThrows(IllegalArgumentException.class, () -> service.remove("\t"));

        OrganisationDocumentDTO missingStatus = validOrganisationDocument();
        missingStatus.setStatus(null);
        assertThrows(IllegalArgumentException.class, () -> service.save(missingStatus));

        OrganisationDocumentDTO missingOrganisationId = validOrganisationDocument();
        missingOrganisationId.setOrganisationId(" ");
        assertThrows(IllegalArgumentException.class, () -> service.save(missingOrganisationId));

        OrganisationDocumentDTO missingOrganisation = validOrganisationDocument();
        missingOrganisation.setOrganisation(null);
        assertThrows(IllegalArgumentException.class, () -> service.save(missingOrganisation));

        OrganisationDocumentDTO missingRegistration = validOrganisationDocument();
        missingRegistration.setOrganisationRegistrationNo("");
        assertThrows(IllegalArgumentException.class, () -> service.save(missingRegistration));

        OrganisationDocumentDTO missingTarget = validOrganisationDocument();
        missingTarget.setTarget(null);
        assertThrows(IllegalArgumentException.class, () -> service.save(missingTarget));

        OrganisationDocumentDTO missingTargetId = validOrganisationDocument();
        missingTargetId.setTargetId("\n");
        assertThrows(IllegalArgumentException.class, () -> service.save(missingTargetId));

        assertThrows(IllegalArgumentException.class, () -> service.search(null, Set.of()));
        assertThrows(IllegalArgumentException.class, () -> service.search((SearchObject<OrganisationDocumentSearchCriteria>) null));
        assertThrows(IllegalArgumentException.class, () -> service.findByOrganisation(null));
        assertThrows(IllegalArgumentException.class, () -> service.findByOrganisation(" "));
        assertThrows(IllegalArgumentException.class, () -> service.findByOrganisation(null, 0, 10));
        assertThrows(IllegalArgumentException.class, () -> service.findByOrganisation("\t", 0, 10));
        assertThrows(IllegalArgumentException.class, () -> service.findByStatus(null));
        assertThrows(IllegalArgumentException.class, () -> service.findByStatus(null, 0, 10));
    }

    private static OrganisationDocumentDTO validOrganisationDocument() {
        OrganisationDocumentDTO input = new OrganisationDocumentDTO();
        input.setStatus(OrganisationDocumentStatus.ACTIVE);
        input.setOrganisationId(UUID.randomUUID().toString());
        input.setOrganisation("ORG");
        input.setOrganisationRegistrationNo("REG-1");
        input.setTarget(bw.co.kyvera.TargetEntity.ORGANISATION);
        input.setTargetId(UUID.randomUUID().toString());
        input.setFileName("org-doc.pdf");
        return input;
    }
}