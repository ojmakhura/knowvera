package bw.co.centralkyc.contact;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import bw.co.centralkyc.TargetEntity;
import bw.co.centralkyc.sequence.SequenceGenerator;
import bw.co.centralkyc.sequence.SequenceGeneratorRepository;
import bw.co.centralkyc.sequence.SequenceGeneratorService;

@ExtendWith(MockitoExtension.class)
class ContactServiceImplTest {

    @Mock
    private ContactDao contactDao;
    @Mock
    private ContactRepository contactRepository;
    @Mock
    private ContactMapper contactMapper;
    @Mock
    private SequenceGeneratorService sequenceGeneratorService;
    @Mock
    private SequenceGeneratorRepository sequenceGeneratorRepository;
    @Mock
    private MessageSource messageSource;

    private ContactServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new ContactServiceImpl(
                contactDao,
                contactRepository,
                contactMapper,
                sequenceGeneratorService,
                sequenceGeneratorRepository,
                messageSource);
    }

    @Test
    void handleSaveCreatesSequenceDefinitionForNewContacts() throws Exception {
        ContactDTO input = new ContactDTO();
        Contact contact = Contact.Factory.newInstance();
        ContactDTO expected = new ContactDTO();

        when(contactMapper.contactDTOToEntity(input)).thenReturn(contact);
        when(sequenceGeneratorRepository.findByName("CONTACT_REF")).thenReturn(Optional.empty());
        when(sequenceGeneratorRepository.save(any(SequenceGenerator.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        when(sequenceGeneratorService.generateNextSequenceValue("CONTACT_REF", true)).thenReturn("CT/2026/0000001");
        when(contactRepository.save(contact)).thenReturn(contact);
        when(contactMapper.toContactDTO(contact)).thenReturn(expected);

        ContactDTO actual = service.handleSave(input);

        ArgumentCaptor<SequenceGenerator> captor = ArgumentCaptor.forClass(SequenceGenerator.class);
        verify(sequenceGeneratorRepository).save(captor.capture());
        assertEquals(TargetEntity.CONTACT, captor.getValue().getTargetEntity());
        assertEquals(4, captor.getValue().getSequenceParts().size());
        assertEquals("CT/2026/0000001", contact.getRef());
        assertSame(expected, actual);
    }

    @Test
    void handleSaveSkipsSequenceGenerationForExistingContacts() throws Exception {
        ContactDTO input = new ContactDTO();
        Contact contact = Contact.Factory.newInstance();
        contact.setId(UUID.randomUUID());
        ContactDTO expected = new ContactDTO();

        when(contactMapper.contactDTOToEntity(input)).thenReturn(contact);
        when(contactRepository.save(contact)).thenReturn(contact);
        when(contactMapper.toContactDTO(contact)).thenReturn(expected);

        ContactDTO actual = service.handleSave(input);

        assertSame(expected, actual);
        verify(sequenceGeneratorRepository, never()).findByName(any());
        verify(sequenceGeneratorService, never()).generateNextSequenceValue(eq("CONTACT_REF"), eq(true));
    }

    @Test
    void handleFindByIdThrowsUnsupportedOperationException() {
        String id = UUID.randomUUID().toString();

        org.junit.jupiter.api.Assertions.assertThrows(
                UnsupportedOperationException.class,
                () -> service.handleFindById(id),
                "bw.co.centralkyc.contact.ContactService.handleFindById(String id) Not implemented!");
    }

    @Test
    void handleRemoveThrowsUnsupportedOperationException() {
        String id = UUID.randomUUID().toString();

        org.junit.jupiter.api.Assertions.assertThrows(
                UnsupportedOperationException.class,
                () -> service.handleRemove(id),
                "bw.co.centralkyc.contact.ContactService.handleRemove(String id) Not implemented!");
    }

    @Test
    void handleGetAllNoArgsThrowsUnsupportedOperationException() {
        org.junit.jupiter.api.Assertions.assertThrows(
                UnsupportedOperationException.class,
                () -> service.handleGetAll(),
                "bw.co.centralkyc.contact.ContactService.handleGetAll() Not implemented!");
    }

    @Test
    void handleSearchThrowsUnsupportedOperationException() {
        String criteria = "test criteria";

        org.junit.jupiter.api.Assertions.assertThrows(
                UnsupportedOperationException.class,
                () -> service.handleSearch(criteria),
                "bw.co.centralkyc.contact.ContactService.handleSearch(String criteria) Not implemented!");
    }

    @Test
    void handleGetAllWithPaginationThrowsUnsupportedOperationException() {
        Integer pageNumber = 1;
        Integer pageSize = 10;

        org.junit.jupiter.api.Assertions.assertThrows(
                UnsupportedOperationException.class,
                () -> service.handleGetAll(pageNumber, pageSize),
                "bw.co.centralkyc.contact.ContactService.handleGetAll(Integer pageNumber, Integer pageSize) Not implemented!");
    }

    @Test
    void handleFindByTypeNoArgsThrowsUnsupportedOperationException() {
        ContactType type = ContactType.ENQUIRY;

        org.junit.jupiter.api.Assertions.assertThrows(
                UnsupportedOperationException.class,
                () -> service.handleFindByType(type),
                "bw.co.centralkyc.contact.ContactService.handleFindByType(ContactType type) Not implemented!");
    }

    @Test
    void handleFindByTypeWithPaginationThrowsUnsupportedOperationException() {
        ContactType type = ContactType.ENQUIRY;
        Integer pageNumber = 1;
        Integer pageSize = 10;

        org.junit.jupiter.api.Assertions.assertThrows(
                UnsupportedOperationException.class,
                () -> service.handleFindByType(type, pageNumber, pageSize),
                "bw.co.centralkyc.contact.ContactService.handleFindByType(ContactType type, Integer pageNumber, Integer pageSize) Not implemented!");
    }
}