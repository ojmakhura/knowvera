// license-header java merge-point
/**
 * This is only generated once! It will never be overwritten.
 * You can (and have to!) safely modify it by hand.
 * TEMPLATE:    SpringServiceImpl.vsl in andromda-spring cartridge
 * MODEL CLASS: AndroMDAModel::backend::bw.co.centralkyc::contact::ContactService
 * STEREOTYPE:  Service
 */
package bw.co.centralkyc.contact;

import java.util.Collection;
import java.util.List;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

/**
 * @see bw.co.centralkyc.contact.ContactService
 */
@Service("contactService")
public class ContactServiceImpl
    extends ContactServiceBase
{
    public ContactServiceImpl(
        ContactDao contactDao,
        ContactRepository contactRepository,
        ContactMapper contactMapper,
        MessageSource messageSource
    ) {
        
        super(
            contactDao,
            contactRepository,
            contactMapper,
            messageSource
        );
    }

    /**
     * @see bw.co.centralkyc.contact.ContactService#findById(String)
     */
    @Override
    protected ContactDTO handleFindById(String id)
        throws Exception
    {
        // TODO implement protected  ContactDTO handleFindById(String id)
        throw new UnsupportedOperationException("bw.co.centralkyc.contact.ContactService.handleFindById(String id) Not implemented!");
    }

    /**
     * @see bw.co.centralkyc.contact.ContactService#save(ContactDTO)
     */
    @Override
    protected ContactDTO handleSave(ContactDTO document)
        throws Exception
    {
        // TODO implement protected  ContactDTO handleSave(ContactDTO document)
        throw new UnsupportedOperationException("bw.co.centralkyc.contact.ContactService.handleSave(ContactDTO document) Not implemented!");
    }

    /**
     * @see bw.co.centralkyc.contact.ContactService#remove(String)
     */
    @Override
    protected boolean handleRemove(String id)
        throws Exception
    {
        // TODO implement protected  boolean handleRemove(String id)
        throw new UnsupportedOperationException("bw.co.centralkyc.contact.ContactService.handleRemove(String id) Not implemented!");
    }

    /**
     * @see bw.co.centralkyc.contact.ContactService#getAll()
     */
    @Override
    protected Collection<ContactDTO> handleGetAll()
        throws Exception
    {
        // TODO implement protected  Collection<ContactDTO> handleGetAll()
        throw new UnsupportedOperationException("bw.co.centralkyc.contact.ContactService.handleGetAll() Not implemented!");
    }

    /**
     * @see bw.co.centralkyc.contact.ContactService#search(String)
     */
    @Override
    protected Collection<ContactDTO> handleSearch(String criteria)
        throws Exception
    {
        // TODO implement protected  Collection<ContactDTO> handleSearch(String criteria)
        throw new UnsupportedOperationException("bw.co.centralkyc.contact.ContactService.handleSearch(String criteria) Not implemented!");
    }

    /**
     * @see bw.co.centralkyc.contact.ContactService#getAll(Integer, Integer)
     */
    @Override
    protected Page<ContactDTO> handleGetAll(Integer pageNumber, Integer pageSize)
        throws Exception
    {
        // TODO implement protected  Page<ContactDTO> handleGetAll(Integer pageNumber, Integer pageSize)
        throw new UnsupportedOperationException("bw.co.centralkyc.contact.ContactService.handleGetAll(Integer pageNumber, Integer pageSize) Not implemented!");
    }

    /**
     * @see bw.co.centralkyc.contact.ContactService#findByType(ContactType)
     */
    @Override
    protected List<ContactDTO> handleFindByType(ContactType type)
        throws Exception
    {
        // TODO implement protected  List<ContactDTO> handleFindByType(ContactType type)
        throw new UnsupportedOperationException("bw.co.centralkyc.contact.ContactService.handleFindByType(ContactType type) Not implemented!");
    }

    /**
     * @see bw.co.centralkyc.contact.ContactService#findByType(ContactType, Integer, Integer)
     */
    @Override
    protected Page<ContactDTO> handleFindByType(ContactType type, Integer pageNumber, Integer pageSize)
        throws Exception
    {
        // TODO implement protected  Page<ContactDTO> handleFindByType(ContactType type, Integer pageNumber, Integer pageSize)
        throw new UnsupportedOperationException("bw.co.centralkyc.contact.ContactService.handleFindByType(ContactType type, Integer pageNumber, Integer pageSize) Not implemented!");
    }

}