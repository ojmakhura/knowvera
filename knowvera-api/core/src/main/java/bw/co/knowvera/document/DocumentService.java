package bw.co.knowvera.document;

import bw.co.knowvera.PropertySearchOrder;
import bw.co.knowvera.SearchObject;
import bw.co.knowvera.TargetEntity;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Set;
import org.springframework.data.domain.Page;

/**
 * TODO: Model Documentation for bw.co.knowvera.document.DocumentService
 */
public interface DocumentService
{
    /**
     * TODO: Model Documentation for bw.co.knowvera.document.DocumentService.findById
     * @param id TODO: Model Documentation for bw.co.knowvera.document.DocumentService.findById(id)
     * @return DocumentDTO
     */
    public DocumentDTO findById(String id) ;

    /**
     * TODO: Model Documentation for bw.co.knowvera.document.DocumentService.save
     * @param document TODO: Model Documentation for bw.co.knowvera.document.DocumentService.save(document)
     * @return DocumentDTO
     */
    public DocumentDTO save(@Valid DocumentDTO document) ;

    /**
     * TODO: Model Documentation for bw.co.knowvera.document.DocumentService.remove
     * @param id TODO: Model Documentation for bw.co.knowvera.document.DocumentService.remove(id)
     * @return boolean
     */
    public boolean remove(String id) ;

    /**
     * TODO: Model Documentation for bw.co.knowvera.document.DocumentService.getAll
     * @return List<DocumentListDTO>
     */
    public List<DocumentListDTO> getAll() ;

    /**
     * TODO: Model Documentation for bw.co.knowvera.document.DocumentService.search
     * @param criteria TODO: Model Documentation for bw.co.knowvera.document.DocumentService.search(criteria)
     * @param orderings TODO: Model Documentation for bw.co.knowvera.document.DocumentService.search(orderings)
     * @return List<DocumentListDTO>
     */
    public List<DocumentListDTO> search(@Valid DocumentSearchCriteria criteria, @Valid Set<PropertySearchOrder> orderings) ;

    /**
     * TODO: Model Documentation for bw.co.knowvera.document.DocumentService.getAll
     * @param pageNumber TODO: Model Documentation for bw.co.knowvera.document.DocumentService.getAll(pageNumber)
     * @param pageSize TODO: Model Documentation for bw.co.knowvera.document.DocumentService.getAll(pageSize)
     * @return Page<DocumentListDTO>
     */
    public Page<DocumentListDTO> getAll(Integer pageNumber, Integer pageSize) ;

    /**
     * TODO: Model Documentation for bw.co.knowvera.document.DocumentService.findByDocumentType
     * @param documentTypeId TODO: Model Documentation for
bw.co.knowvera.document.DocumentService.findByDocumentType(documentTypeId)
     * @return List<DocumentListDTO>
     */
    public List<DocumentListDTO> findByDocumentType(String documentTypeId) ;

    /**
     * TODO: Model Documentation for bw.co.knowvera.document.DocumentService.upload
     * @param target TODO: Model Documentation for bw.co.knowvera.document.DocumentService.upload(target)
     * @param targetId TODO: Model Documentation for bw.co.knowvera.document.DocumentService.upload(targetId)
     * @param documentTypeId TODO: Model Documentation for bw.co.knowvera.document.DocumentService.upload(documentTypeId)
     * @param url TODO: Model Documentation for bw.co.knowvera.document.DocumentService.upload(url)
     * @return DocumentDTO
     */
    public DocumentDTO upload(TargetEntity target, String targetId, String documentTypeId, String url) ;

    /**
     * TODO: Model Documentation for bw.co.knowvera.document.DocumentService.findByTarget
     * @param target TODO: Model Documentation for bw.co.knowvera.document.DocumentService.findByTarget(target)
     * @param targetId TODO: Model Documentation for bw.co.knowvera.document.DocumentService.findByTarget(targetId)
     * @return List<DocumentListDTO>
     */
    public List<DocumentListDTO> findByTarget(TargetEntity target, String targetId) ;

    /**
     * TODO: Model Documentation for bw.co.knowvera.document.DocumentService.search
     * @param criteria TODO: Model Documentation for bw.co.knowvera.document.DocumentService.search(criteria)
     * @return Page<DocumentListDTO>
     */
    public Page<DocumentListDTO> search(SearchObject<DocumentSearchCriteria> criteria) ;

    /**
     * TODO: Model Documentation for bw.co.knowvera.document.DocumentService.updateFileContent
     * @param id TODO: Model Documentation for bw.co.knowvera.document.DocumentService.updateFileContent(id)
     * @param content TODO: Model Documentation for bw.co.knowvera.document.DocumentService.updateFileContent(content)
     * @return DocumentDTO
     */
    public DocumentDTO updateFileContent(String id, String content) ;

    /**
     * TODO: Model Documentation for bw.co.knowvera.document.DocumentService.verifyData
     * @param id TODO: Model Documentation for bw.co.knowvera.document.DocumentService.verifyData(id)
     * @param user TODO: Model Documentation for bw.co.knowvera.document.DocumentService.verifyData(user)
     * @return DocumentDTO
     */
    public DocumentDTO verifyData(String id, String user) ;

    /**
     * TODO: Model Documentation for
     * bw.co.knowvera.document.DocumentService.updateVerificationStatus
     * @param id TODO: Model Documentation for bw.co.knowvera.document.DocumentService.updateVerificationStatus(id)
     * @param verificationStatus TODO: Model Documentation for
bw.co.knowvera.document.DocumentService.updateVerificationStatus(verificationStatus)
     * @param user TODO: Model Documentation for bw.co.knowvera.document.DocumentService.updateVerificationStatus(user)
     * @return DocumentDTO
     */
    public DocumentDTO updateVerificationStatus(String id, DocumentVerificationStatus verificationStatus, String user) ;

}