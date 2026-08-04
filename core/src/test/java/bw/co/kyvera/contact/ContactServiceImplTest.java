package bw.co.kyvera.contact;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

import bw.co.kyvera.TargetEntity;
import bw.co.kyvera.sequence.SequenceGenerator;
import bw.co.kyvera.sequence.SequenceGeneratorRepository;
import bw.co.kyvera.sequence.SequenceGeneratorService;

@ExtendWith(MockitoExtension.class)
class ContactServiceImplTest {

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
                contactRepository,
                contactMapper,
                sequenceGeneratorService,
                sequenceGeneratorRepository,
                messageSource);
    }

    @Test
    void saveCreatesSequenceDefinitionForNewContacts() throws Exception {
        ContactDTO input = new ContactDTO();
        input.setMessage("Need help");
        input.setEmail("user@example.com");
        Contact contact = Contact.Factory.newInstance();
        ContactDTO expected = new ContactDTO();

        when(contactMapper.contactDTOToEntity(input)).thenReturn(contact);
        when(sequenceGeneratorRepository.findByName("CONTACT_REF")).thenReturn(Optional.empty());
        when(sequenceGeneratorRepository.save(any(SequenceGenerator.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        when(sequenceGeneratorService.generateNextSequenceValue("CONTACT_REF", true)).thenReturn("CT/2026/0000001");
        when(contactRepository.save(contact)).thenReturn(contact);
        when(contactMapper.toContactDTO(contact)).thenReturn(expected);

        ContactDTO actual = service.save(input);

        ArgumentCaptor<SequenceGenerator> captor = ArgumentCaptor.forClass(SequenceGenerator.class);
        verify(sequenceGeneratorRepository).save(captor.capture());
        assertEquals(TargetEntity.CONTACT, captor.getValue().getTargetEntity());
        assertEquals(4, captor.getValue().getSequenceParts().size());
        assertEquals("CT/2026/0000001", contact.getRef());
        assertSame(expected, actual);
    }

    @Test
    void saveSkipsSequenceGenerationForExistingContacts() throws Exception {
        ContactDTO input = new ContactDTO();
        input.setMessage("Need help");
        input.setEmail("user@example.com");
        Contact contact = Contact.Factory.newInstance();
        contact.setId(UUID.randomUUID());
        ContactDTO expected = new ContactDTO();

        when(contactMapper.contactDTOToEntity(input)).thenReturn(contact);
        when(contactRepository.save(contact)).thenReturn(contact);
        when(contactMapper.toContactDTO(contact)).thenReturn(expected);

        ContactDTO actual = service.save(input);

        assertSame(expected, actual);
        verify(sequenceGeneratorRepository, never()).findByName(any());
        verify(sequenceGeneratorService, never()).generateNextSequenceValue(eq("CONTACT_REF"), eq(true));
    }

    @Test
    void findByIdThrowsUnsupportedOperationException() {
        String id = UUID.randomUUID().toString();

        org.junit.jupiter.api.Assertions.assertThrows(
            ContactServiceException.class,
                () -> service.findById(id),
                "bw.co.kyvera.contact.ContactService.findById(String id) Not implemented!");
    }

    @Test
    void removeThrowsUnsupportedOperationException() {
        String id = UUID.randomUUID().toString();

        org.junit.jupiter.api.Assertions.assertThrows(
            ContactServiceException.class,
                () -> service.remove(id),
                "bw.co.kyvera.contact.ContactService.remove(String id) Not implemented!");
    }

    @Test
    void getAllNoArgsThrowsUnsupportedOperationException() {
        org.junit.jupiter.api.Assertions.assertThrows(
            ContactServiceException.class,
                () -> service.getAll(),
                "bw.co.kyvera.contact.ContactService.getAll() Not implemented!");
    }

    @Test
    void searchThrowsUnsupportedOperationException() {
        String criteria = "test criteria";

        org.junit.jupiter.api.Assertions.assertThrows(
            ContactServiceException.class,
                () -> service.search(criteria),
                "bw.co.kyvera.contact.ContactService.search(String criteria) Not implemented!");
    }

    @Test
    void getAllWithPaginationThrowsUnsupportedOperationException() {
        Integer pageNumber = 1;
        Integer pageSize = 10;

        org.junit.jupiter.api.Assertions.assertThrows(
            ContactServiceException.class,
                () -> service.getAll(pageNumber, pageSize),
                "bw.co.kyvera.contact.ContactService.getAll(Integer pageNumber, Integer pageSize) Not implemented!");
    }

    @Test
    void findByTypeNoArgsThrowsUnsupportedOperationException() {
        ContactType type = ContactType.ENQUIRY;

        org.junit.jupiter.api.Assertions.assertThrows(
            ContactServiceException.class,
                () -> service.findByType(type),
                "bw.co.kyvera.contact.ContactService.findByType(ContactType type) Not implemented!");
    }

    @Test
    void findByTypeWithPaginationThrowsUnsupportedOperationException() {
        ContactType type = ContactType.ENQUIRY;
        Integer pageNumber = 1;
        Integer pageSize = 10;

        org.junit.jupiter.api.Assertions.assertThrows(
            ContactServiceException.class,
                () -> service.findByType(type, pageNumber, pageSize),
                "bw.co.kyvera.contact.ContactService.findByType(ContactType type, Integer pageNumber, Integer pageSize) Not implemented!");
    }

    @Test
    void serviceBaseFindByIdRejectsNullId() {
        assertThrows(IllegalArgumentException.class, () -> service.findById(null));
    }

    @Test
    void serviceBaseFindByIdRejectsBlankId() {
        assertThrows(IllegalArgumentException.class, () -> service.findById("   "));
    }

    @Test
    void serviceBaseSaveRejectsNullDocument() {
        assertThrows(IllegalArgumentException.class, () -> service.save(null));
    }

    @Test
    void serviceBaseSaveRejectsMissingMessage() {
        ContactDTO input = new ContactDTO();
        input.setEmail("user@example.com");

        assertThrows(IllegalArgumentException.class, () -> service.save(input));
    }

    @Test
    void serviceBaseSaveRejectsMissingEmail() {
        ContactDTO input = new ContactDTO();
        input.setMessage("Need help");

        assertThrows(IllegalArgumentException.class, () -> service.save(input));
    }

    @Test
    void serviceBaseRemoveRejectsNullId() {
        assertThrows(IllegalArgumentException.class, () -> service.remove(null));
    }

    @Test
    void serviceBaseRemoveRejectsBlankId() {
        assertThrows(IllegalArgumentException.class, () -> service.remove("\t\n"));
    }

    @Test
    void serviceBaseFindByTypeRejectsNullType() {
        assertThrows(IllegalArgumentException.class, () -> service.findByType(null));
    }

    @Test
    void serviceBaseFindByTypeWithPagingRejectsNullType() {
        assertThrows(IllegalArgumentException.class, () -> service.findByType(null, 0, 10));
    }
}