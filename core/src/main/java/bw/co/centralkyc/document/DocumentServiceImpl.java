// license-header java merge-point
/**
 * This is only generated once! It will never be overwritten.
 * You can (and have to!) safely modify it by hand.
 * TEMPLATE:    SpringServiceImpl.vsl in andromda-spring cartridge
 * MODEL CLASS: AndroMDAModel::backend::bw.co.centralkyc::document::DocumentService
 * STEREOTYPE:  Service
 */
package bw.co.centralkyc.document;

import bw.co.centralkyc.TargetEntity;
import java.io.File;
import java.util.Collection;
import java.util.UUID;

import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @see bw.co.centralkyc.document.DocumentService
 */
@Service("documentService")
@Transactional(propagation = Propagation.REQUIRED, readOnly=false)
public class DocumentServiceImpl
    extends DocumentServiceBase
{
    

    public DocumentServiceImpl(DocumentDao documentDao, DocumentRepository documentRepository,
            DocumentMapper documentMapper, MessageSource messageSource) {
        super(documentDao, documentRepository, documentMapper, messageSource);
        //TODO Auto-generated constructor stub
    }

    /**
     * @see bw.co.centralkyc.document.DocumentService#findById(String)
     */
    @Override
    protected DocumentDTO handleFindById(String id)
        throws Exception
    {

        Document doc = documentRepository.findById(UUID.fromString(id)).orElseThrow(() -> new Exception("Document not found"));
        return documentMapper.toDocumentDTO(doc);
    }

    /**
     * @see bw.co.centralkyc.document.DocumentService#save(DocumentDTO)
     */
    @Override
    protected DocumentDTO handleSave(DocumentDTO document)
        throws Exception
    {

        Document entity = documentDao.documentDTOToEntity(document);
        entity = documentRepository.save(entity);

        return documentMapper.toDocumentDTO(entity);
    }

    /**
     * @see bw.co.centralkyc.document.DocumentService#remove(String)
     */
    @Override
    protected boolean handleRemove(String id)
        throws Exception
    {

        Document doc = documentRepository.getReferenceById(UUID.fromString(id));
        documentRepository.delete(doc);
        return true;
    }

    /**
     * @see bw.co.centralkyc.document.DocumentService#getAll()
     */
    @Override
    protected Collection<DocumentDTO> handleGetAll()
        throws Exception
    {
        Collection<Document> all = documentRepository.findAll();
        return documentMapper.toDocumentDTOCollection(all);
    }

    /**
     * @see bw.co.centralkyc.document.DocumentService#search(String)
     */
    @Override
    protected Collection<DocumentDTO> handleSearch(String criteria)
        throws Exception
    {
        // TODO implement protected  Collection<DocumentDTO> handleSearch(String criteria)
        throw new UnsupportedOperationException("bw.co.centralkyc.document.DocumentService.handleSearch(String criteria) Not implemented!");
    }

    /**
     * @see bw.co.centralkyc.document.DocumentService#getAll(Integer, Integer)
     */
    @Override
    protected Page<DocumentDTO> handleGetAll(Integer pageNumber, Integer pageSize)
        throws Exception
    {

        PageRequest request = PageRequest.of(pageNumber, pageSize);
        Page<Document> page = documentRepository.findAll(request);

        return page.map(doc -> documentMapper.toDocumentDTO(doc));
    }

    /**
     * @see bw.co.centralkyc.document.DocumentService#findByDocumentType(String)
     */
    @Override
    protected Collection<DocumentDTO> handleFindByDocumentType(String documentTypeId)
        throws Exception
    {

        Specification<Document> spec = (root, cq, cb) -> {
            return cb.equal(root.get("documentType").get("id"), documentTypeId);
        };

        Collection<Document> docs = documentRepository.findAll(spec, Sort.by(Direction.ASC, "fileName"));

        return documentMapper.toDocumentDTOCollection(docs);

    }

    /**
     * @see bw.co.centralkyc.document.DocumentService#upload(TargetEntity, String, String, File)
     */
    @Override
    protected DocumentDTO handleUpload(TargetEntity target, String targetId, String documentTypeId, String url)
        throws Exception
    {
        // TODO implement protected  DocumentDTO handleUpload(TargetEntity target, String targetId, String documentTypeId, File url)
        throw new UnsupportedOperationException("bw.co.centralkyc.document.DocumentService.handleUpload(TargetEntity target, String targetId, String documentTypeId, File url) Not implemented!");
    }

    /**
     * @see bw.co.centralkyc.document.DocumentService#findByTarget(TargetEntity, String)
     */
    @Override
    protected Collection<DocumentDTO> handleFindByTarget(TargetEntity target, String targetId)
        throws Exception
    {
        Specification<Document> spec = (root, cq, cb) -> {
            return cb.and(
                cb.equal(root.get("target"), target), 
                cb.equal(root.get("targetId"), targetId)
            );
        };

        Collection<Document> docs = documentRepository.findAll(spec, Sort.by(Direction.ASC, "fileName"));

        return documentDao.toDocumentDTOCollection(docs);
        
    }

}