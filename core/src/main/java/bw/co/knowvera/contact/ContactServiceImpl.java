// license-header java merge-point
/**
 * This is only generated once! It will never be overwritten.
 * You can (and have to!) safely modify it by hand.
 * TEMPLATE:    SpringServiceImpl.vsl in andromda-spring cartridge
 * MODEL CLASS: AndroMDAModel::backend::bw.co.knowvera::contact::ContactService
 * STEREOTYPE:  Service
 */
package bw.co.knowvera.contact;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import bw.co.knowvera.TargetEntity;
import bw.co.knowvera.contact.bw;
import bw.co.knowvera.sequence.SequenceGenerator;
import bw.co.knowvera.sequence.SequenceGeneratorRepository;
import bw.co.knowvera.sequence.SequenceGeneratorService;
import bw.co.knowvera.sequence.SequencePart;
import bw.co.knowvera.sequence.SequencePartType;
import bw.co.knowvera.contact.Contact;
import bw.co.knowvera.contact.ContactDTO;
import bw.co.knowvera.contact.ContactRepository;
import bw.co.knowvera.contact.ContactServiceBase;
import bw.co.knowvera.contact.ContactType;

/**
 * @see bw.co.knowvera.contact.ContactService
 */
@Service("contactService")
public class ContactServiceImpl
        extends ContactServiceBase {
    private final SequenceGeneratorService sequenceGeneratorService;
    private final SequenceGeneratorRepository sequenceGeneratorRepository;
    private static final String SEQUENCE_NAME = "CONTACT_REF";

    public ContactServiceImpl(
            ContactRepository contactRepository,
            ContactMapper contactMapper,
            SequenceGeneratorService sequenceGeneratorService,
            SequenceGeneratorRepository sequenceGeneratorRepository,
            MessageSource messageSource) {
        super(
                contactRepository,
                contactMapper,
                messageSource);
        this.sequenceGeneratorService = sequenceGeneratorService;
        this.sequenceGeneratorRepository = sequenceGeneratorRepository;
    }

    /**
     * @see bw.co.knowvera.contact.ContactService#findById(String)
     */
    @Override
    protected ContactDTO handleFindById(String id)
            throws Exception {
        // TODO implement protected ContactDTO handleFindById(String id)
        throw new UnsupportedOperationException(
                "bw.co.knowvera.contact.ContactService.handleFindById(String id) Not implemented!");
    }

    /**
     * @see bw.co.knowvera.contact.ContactService#save(ContactDTO)
     */
    @Override
    protected ContactDTO handleSave(ContactDTO document)
            throws Exception {

        Contact contact = this.contactMapper.contactDTOToEntity(document);
        if (contact.getId() == null) {
            SequenceGenerator sequenceGenerator = this.sequenceGeneratorRepository.findByName(SEQUENCE_NAME)
                    .orElse(null);

            if (sequenceGenerator == null) {
                sequenceGenerator = new SequenceGenerator();
                sequenceGenerator.setName(SEQUENCE_NAME);
                sequenceGenerator.setTargetEntity(TargetEntity.CONTACT);

                List<SequencePart> sequenceParts = new ArrayList<>();

                SequencePart counterPart = new SequencePart();
                counterPart.setPosition(0);
                counterPart.setType(SequencePartType.STATIC);
                counterPart.setInitialValue("CT/");
                counterPart.setName(SEQUENCE_NAME + "_PREFIX");
                counterPart.setSequenceGenerator(sequenceGenerator);
                sequenceParts.add(counterPart);

                counterPart = new SequencePart();
                counterPart.setPosition(1);
                counterPart.setType(SequencePartType.YEAR);
                counterPart.setName(SEQUENCE_NAME + "_YEAR");
                counterPart.setInitialValue("0000000");
                counterPart.setSequenceGenerator(sequenceGenerator);
                sequenceParts.add(counterPart);

                counterPart = new SequencePart();
                counterPart.setPosition(2);
                counterPart.setType(SequencePartType.STATIC);
                counterPart.setInitialValue("/");
                counterPart.setName(SEQUENCE_NAME + "_YEAR_SLASH");
                counterPart.setSequenceGenerator(sequenceGenerator);
                sequenceParts.add(counterPart);

                counterPart = new SequencePart();
                counterPart.setPosition(3);
                counterPart.setType(SequencePartType.COUNTER);
                counterPart.setName(SEQUENCE_NAME + "_COUNTER");
                counterPart.setInitialValue("0000000");
                counterPart.setSequenceGenerator(sequenceGenerator);
                sequenceParts.add(counterPart);

                sequenceGenerator.setSequenceParts(sequenceParts);
                sequenceGenerator = sequenceGeneratorRepository.save(sequenceGenerator);
            }

            String nextRef = sequenceGeneratorService.generateNextSequenceValue(SEQUENCE_NAME, true);
            contact.setRef(nextRef);
        }

        contact = this.contactRepository.save(contact);

        return this.contactMapper.toContactDTO(contact);
    }

    /**
     * @see bw.co.knowvera.contact.ContactService#remove(String)
     */
    @Override
    protected boolean handleRemove(String id)
            throws Exception {
        // TODO implement protected boolean handleRemove(String id)
        throw new UnsupportedOperationException(
                "bw.co.knowvera.contact.ContactService.handleRemove(String id) Not implemented!");
    }

    /**
     * @see bw.co.knowvera.contact.ContactService#getAll()
     */
    @Override
    protected List<ContactDTO> handleGetAll()
            throws Exception {
        // TODO implement protected List<ContactDTO> handleGetAll()
        throw new UnsupportedOperationException(
                "bw.co.knowvera.contact.ContactService.handleGetAll() Not implemented!");
    }

    /**
     * @see bw.co.knowvera.contact.ContactService#search(String)
     */
    @Override
    protected List<ContactDTO> handleSearch(String criteria)
            throws Exception {
        // TODO implement protected List<ContactDTO> handleSearch(String criteria)
        throw new UnsupportedOperationException(
                "bw.co.knowvera.contact.ContactService.handleSearch(String criteria) Not implemented!");
    }

    /**
     * @see bw.co.knowvera.contact.ContactService#getAll(Integer, Integer)
     */
    @Override
    protected Page<ContactDTO> handleGetAll(Integer pageNumber, Integer pageSize)
            throws Exception {
        // TODO implement protected Page<ContactDTO> handleGetAll(Integer pageNumber,
        // Integer pageSize)
        throw new UnsupportedOperationException(
                "bw.co.knowvera.contact.ContactService.handleGetAll(Integer pageNumber, Integer pageSize) Not implemented!");
    }

    /**
     * @see bw.co.knowvera.contact.ContactService#findByType(ContactType)
     */
    @Override
    protected List<ContactDTO> handleFindByType(ContactType type)
            throws Exception {
        // TODO implement protected List<ContactDTO> handleFindByType(ContactType type)
        throw new UnsupportedOperationException(
                "bw.co.knowvera.contact.ContactService.handleFindByType(ContactType type) Not implemented!");
    }

    /**
     * @see bw.co.knowvera.contact.ContactService#findByType(ContactType, Integer,
     *      Integer)
     */
    @Override
    protected Page<ContactDTO> handleFindByType(ContactType type, Integer pageNumber, Integer pageSize)
            throws Exception {
        // TODO implement protected Page<ContactDTO> handleFindByType(ContactType type,
        // Integer pageNumber, Integer pageSize)
        throw new UnsupportedOperationException(
                "bw.co.knowvera.contact.ContactService.handleFindByType(ContactType type, Integer pageNumber, Integer pageSize) Not implemented!");
    }

}