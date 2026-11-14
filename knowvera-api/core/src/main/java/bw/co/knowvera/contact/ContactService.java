package bw.co.knowvera.contact;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.data.domain.Page;

/**
 * TODO: Model Documentation for bw.co.knowvera.contact.ContactService
 */
public interface ContactService
{
    /**
     * TODO: Model Documentation for bw.co.knowvera.contact.ContactService.findById
     * @param id TODO: Model Documentation for bw.co.knowvera.contact.ContactService.findById(id)
     * @return ContactDTO
     */
    public ContactDTO findById(String id) ;

    /**
     * TODO: Model Documentation for bw.co.knowvera.contact.ContactService.save
     * @param document TODO: Model Documentation for bw.co.knowvera.contact.ContactService.save(document)
     * @return ContactDTO
     */
    public ContactDTO save(@Valid ContactDTO document) ;

    /**
     * TODO: Model Documentation for bw.co.knowvera.contact.ContactService.remove
     * @param id TODO: Model Documentation for bw.co.knowvera.contact.ContactService.remove(id)
     * @return boolean
     */
    public boolean remove(String id) ;

    /**
     * TODO: Model Documentation for bw.co.knowvera.contact.ContactService.getAll
     * @return List<ContactDTO>
     */
    public List<ContactDTO> getAll() ;

    /**
     * TODO: Model Documentation for bw.co.knowvera.contact.ContactService.search
     * @param criteria TODO: Model Documentation for bw.co.knowvera.contact.ContactService.search(criteria)
     * @return List<ContactDTO>
     */
    public List<ContactDTO> search(String criteria) ;

    /**
     * TODO: Model Documentation for bw.co.knowvera.contact.ContactService.getAll
     * @param pageNumber TODO: Model Documentation for bw.co.knowvera.contact.ContactService.getAll(pageNumber)
     * @param pageSize TODO: Model Documentation for bw.co.knowvera.contact.ContactService.getAll(pageSize)
     * @return Page<ContactDTO>
     */
    public Page<ContactDTO> getAll(Integer pageNumber, Integer pageSize) ;

    /**
     * TODO: Model Documentation for bw.co.knowvera.contact.ContactService.findByType
     * @param type TODO: Model Documentation for bw.co.knowvera.contact.ContactService.findByType(type)
     * @return List<ContactDTO>
     */
    public List<ContactDTO> findByType(ContactType type) ;

    /**
     * TODO: Model Documentation for bw.co.knowvera.contact.ContactService.findByType
     * @param type TODO: Model Documentation for bw.co.knowvera.contact.ContactService.findByType(type)
     * @param pageNumber TODO: Model Documentation for bw.co.knowvera.contact.ContactService.findByType(pageNumber)
     * @param pageSize TODO: Model Documentation for bw.co.knowvera.contact.ContactService.findByType(pageSize)
     * @return Page<ContactDTO>
     */
    public Page<ContactDTO> findByType(ContactType type, Integer pageNumber, Integer pageSize) ;

}