package bw.co.knowvera.document.type.field;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.data.domain.Page;

/**
 * TODO: Model Documentation for bw.co.knowvera.document.type.field.ExpectedFieldService
 */
public interface ExpectedFieldService
{
    /**
     * TODO: Model Documentation for
     * bw.co.knowvera.document.type.field.ExpectedFieldService.findById
     * @param id TODO: Model Documentation for bw.co.knowvera.document.type.field.ExpectedFieldService.findById(id)
     * @return ExpectedFieldDTO
     */
    public ExpectedFieldDTO findById(String id) ;

    /**
     * TODO: Model Documentation for bw.co.knowvera.document.type.field.ExpectedFieldService.save
     * @param expectedField TODO: Model Documentation for
bw.co.knowvera.document.type.field.ExpectedFieldService.save(expectedField)
     * @return ExpectedFieldDTO
     */
    public ExpectedFieldDTO save(@Valid ExpectedFieldDTO expectedField) ;

    /**
     * TODO: Model Documentation for bw.co.knowvera.document.type.field.ExpectedFieldService.remove
     * @param id TODO: Model Documentation for bw.co.knowvera.document.type.field.ExpectedFieldService.remove(id)
     * @return boolean
     */
    public boolean remove(String id) ;

    /**
     * TODO: Model Documentation for
     * bw.co.knowvera.document.type.field.ExpectedFieldService.findByDocumentType
     * @param documentTypeIds TODO: Model Documentation for
bw.co.knowvera.document.type.field.ExpectedFieldService.findByDocumentType(documentTypeIds)
     * @param pageNumber TODO: Model Documentation for
bw.co.knowvera.document.type.field.ExpectedFieldService.findByDocumentType(pageNumber)
     * @param pageSize TODO: Model Documentation for
bw.co.knowvera.document.type.field.ExpectedFieldService.findByDocumentType(pageSize)
     * @return Page<ExpectedFieldDTO>
     */
    public Page<ExpectedFieldDTO> findByDocumentType(List<String> documentTypeIds, Integer pageNumber, Integer pageSize) ;

    /**
     * TODO: Model Documentation for
     * bw.co.knowvera.document.type.field.ExpectedFieldService.findByDocumentType
     * @param documentTypeIds TODO: Model Documentation for
bw.co.knowvera.document.type.field.ExpectedFieldService.findByDocumentType(documentTypeIds)
     * @return List<ExpectedFieldDTO>
     */
    public List<ExpectedFieldDTO> findByDocumentType(List<String> documentTypeIds) ;

}