package bw.co.centralkyc.organisation.document;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import bw.co.centralkyc.document.DocumentDao;
import bw.co.centralkyc.document.DocumentMapper;
import bw.co.centralkyc.document.DocumentRepository;
import bw.co.centralkyc.individual.IndividualDao;
import bw.co.centralkyc.individual.IndividualMapper;
import bw.co.centralkyc.individual.IndividualRepository;

@ExtendWith(MockitoExtension.class)
class OrganisationDocumentServiceImplTest {

    @Mock
    private OrganisationDocumentDao organisationDocumentDao;
    @Mock
    private OrganisationDocumentRepository organisationDocumentRepository;
    @Mock
    private OrganisationDocumentMapper organisationDocumentMapper;
    @Mock
    private IndividualDao individualDao;
    @Mock
    private IndividualRepository individualRepository;
    @Mock
    private IndividualMapper individualMapper;
    @Mock
    private DocumentDao documentDao;
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
                organisationDocumentDao,
                organisationDocumentRepository,
                organisationDocumentMapper,
                individualDao,
                individualRepository,
                individualMapper,
                documentDao,
                documentRepository,
                documentMapper,
                messageSource);
    }

    @Test
    void handleFindByIdLoadsAndMapsOrganisationDocument() throws Exception {
        UUID id = UUID.randomUUID();
        OrganisationDocument entity = OrganisationDocument.Factory.newInstance();
        OrganisationDocumentDTO expected = new OrganisationDocumentDTO();

        when(organisationDocumentRepository.getReferenceById(id)).thenReturn(entity);
        when(organisationDocumentDao.toOrganisationDocumentDTO(entity)).thenReturn(expected);

        OrganisationDocumentDTO actual = service.handleFindById(id.toString());

        assertSame(expected, actual);
    }

    @Test
    void handleSaveMapsPersistsAndMapsBack() throws Exception {
        OrganisationDocumentDTO input = new OrganisationDocumentDTO();
        OrganisationDocument entity = OrganisationDocument.Factory.newInstance();
        OrganisationDocumentDTO expected = new OrganisationDocumentDTO();

        when(organisationDocumentDao.organisationDocumentDTOToEntity(input)).thenReturn(entity);
        when(organisationDocumentRepository.save(entity)).thenReturn(entity);
        when(organisationDocumentDao.toOrganisationDocumentDTO(entity)).thenReturn(expected);

        OrganisationDocumentDTO actual = service.handleSave(input);

        assertSame(expected, actual);
    }

    @Test
    void handleGetAllDelegatesToMapper() throws Exception {
        List<OrganisationDocument> entities = List.of(OrganisationDocument.Factory.newInstance());
        List<OrganisationDocumentDTO> expected = List.of(new OrganisationDocumentDTO());

        when(organisationDocumentRepository.findAll()).thenReturn(entities);
        when(organisationDocumentMapper.toOrganisationDocumentDTOCollection(entities)).thenReturn(expected);

        List<OrganisationDocumentDTO> actual = service.handleGetAll();

        assertSame(expected, actual);
    }

    @Test
    void handleRemoveDeletesById() throws Exception {
        UUID id = UUID.randomUUID();

        boolean removed = service.handleRemove(id.toString());

        assertTrue(removed);
        verify(organisationDocumentRepository).deleteById(id);
    }
}