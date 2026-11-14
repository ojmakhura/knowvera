package bw.co.knowvera.document.type;

import bw.co.knowvera.document.type.field.ExpectedFieldDTO;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Set;
import org.springframework.data.domain.Page;

/**
 * TODO: Model Documentation for bw.co.knowvera.document.type.DocumentTypeService
 */
public interface DocumentTypeService
{
    /**
     * TODO: Model Documentation for bw.co.knowvera.document.type.DocumentTypeService.findById
     * @param id TODO: Model Documentation for bw.co.knowvera.document.type.DocumentTypeService.findById(id)
     * @return DocumentTypeDTO
     */
    public DocumentTypeDTO findById(String id) ;

    /**
     * TODO: Model Documentation for bw.co.knowvera.document.type.DocumentTypeService.save
     * @param documentType TODO: Model Documentation for bw.co.knowvera.document.type.DocumentTypeService.save(documentType)
     * @return DocumentTypeDTO
     */
    public DocumentTypeDTO save(@Valid DocumentTypeDTO documentType) ;

    /**
     * TODO: Model Documentation for bw.co.knowvera.document.type.DocumentTypeService.remove
     * @param id TODO: Model Documentation for bw.co.knowvera.document.type.DocumentTypeService.remove(id)
     * @return boolean
     */
    public boolean remove(String id) ;

    /**
     * TODO: Model Documentation for bw.co.knowvera.document.type.DocumentTypeService.getAll
     * @return List<DocumentTypeDTO>
     */
    public List<DocumentTypeDTO> getAll() ;

    /**
     * TODO: Model Documentation for bw.co.knowvera.document.type.DocumentTypeService.search
     * @param criteria TODO: Model Documentation for bw.co.knowvera.document.type.DocumentTypeService.search(criteria)
     * @return List<DocumentTypeDTO>
     */
    public List<DocumentTypeDTO> search(String criteria) ;

    /**
     * TODO: Model Documentation for bw.co.knowvera.document.type.DocumentTypeService.getAll
     * @param pageNumber TODO: Model Documentation for bw.co.knowvera.document.type.DocumentTypeService.getAll(pageNumber)
     * @param pageSize TODO: Model Documentation for bw.co.knowvera.document.type.DocumentTypeService.getAll(pageSize)
     * @return Page<DocumentTypeDTO>
     */
    public Page<DocumentTypeDTO> getAll(Integer pageNumber, Integer pageSize) ;

    /**
     * TODO: Model Documentation for bw.co.knowvera.document.type.DocumentTypeService.search
     * @param criteria TODO: Model Documentation for bw.co.knowvera.document.type.DocumentTypeService.search(criteria)
     * @param pageNumber TODO: Model Documentation for bw.co.knowvera.document.type.DocumentTypeService.search(pageNumber)
     * @param pageSize TODO: Model Documentation for bw.co.knowvera.document.type.DocumentTypeService.search(pageSize)
     * @return Page<DocumentTypeDTO>
     */
    public Page<DocumentTypeDTO> search(String criteria, Integer pageNumber, Integer pageSize) ;

    /**
     * TODO: Model Documentation for
     * bw.co.knowvera.document.type.DocumentTypeService.addExpectedField
     * @param id TODO: Model Documentation for bw.co.knowvera.document.type.DocumentTypeService.addExpectedField(id)
     * @param expectedFields TODO: Model Documentation for
bw.co.knowvera.document.type.DocumentTypeService.addExpectedField(expectedFields)
     * @return DocumentTypeDTO
     */
    public DocumentTypeDTO addExpectedField(String id, @Valid Set<ExpectedFieldDTO> expectedFields) ;

}