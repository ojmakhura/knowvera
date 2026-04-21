// license-header java merge-point
/**
 * This is only generated once! It will never be overwritten.
 * You can (and have to!) safely modify it by hand.
 * TEMPLATE:    SpringServiceImpl.vsl in andromda-spring cartridge
 * MODEL CLASS: AndroMDAModel::backend::bw.co.centralkyc::organisation::document::OrganisationDocumentService
 * STEREOTYPE:  Service
 */
package bw.co.centralkyc.organisation.document;

import bw.co.centralkyc.PropertySearchOrder;
import bw.co.centralkyc.SearchObject;
import bw.co.centralkyc.document.DocumentDao;
import bw.co.centralkyc.document.DocumentMapper;
import bw.co.centralkyc.document.DocumentRepository;
import bw.co.centralkyc.individual.IndividualDao;
import bw.co.centralkyc.individual.IndividualMapper;
import bw.co.centralkyc.individual.IndividualRepository;
import java.util.Collection;
import java.util.Set;
import java.util.UUID;

import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @see bw.co.centralkyc.organisation.document.OrganisationDocumentService
 */
@Service("organisationDocumentService")
@Transactional(propagation = Propagation.REQUIRED, readOnly=false)
public class OrganisationDocumentServiceImpl
    extends OrganisationDocumentServiceBase
{
    

    public OrganisationDocumentServiceImpl(OrganisationDocumentDao organisationDocumentDao,
            OrganisationDocumentRepository organisationDocumentRepository,
            OrganisationDocumentMapper organisationDocumentMapper, IndividualDao individualDao,
            IndividualRepository individualRepository, IndividualMapper individualMapper, DocumentDao documentDao,
            DocumentRepository documentRepository, DocumentMapper documentMapper, MessageSource messageSource) {
        super(organisationDocumentDao, organisationDocumentRepository, organisationDocumentMapper, individualDao,
                individualRepository, individualMapper, documentDao, documentRepository, documentMapper, messageSource);
        //TODO Auto-generated constructor stub
    }

    /**
     * @see bw.co.centralkyc.organisation.document.OrganisationDocumentService#findById(String)
     */
    @Override
    protected OrganisationDocumentDTO handleFindById(String id)
        throws Exception
    {

        OrganisationDocument entity = organisationDocumentRepository.getReferenceById(UUID.fromString(id));
        return organisationDocumentDao.toOrganisationDocumentDTO(entity);
    }

    /**
     * @see bw.co.centralkyc.organisation.document.OrganisationDocumentService#save(OrganisationDocumentDTO)
     */
    @Override
    protected OrganisationDocumentDTO handleSave(OrganisationDocumentDTO clientRequest)
        throws Exception
    {

        OrganisationDocument organisationDocument = organisationDocumentDao.organisationDocumentDTOToEntity(clientRequest);
        organisationDocument = organisationDocumentRepository.save(organisationDocument);
        return organisationDocumentDao.toOrganisationDocumentDTO(organisationDocument);
    }

    /**
     * @see bw.co.centralkyc.organisation.document.OrganisationDocumentService#remove(String)
     */
    @Override
    protected boolean handleRemove(String id)
        throws Exception
    {

        organisationDocumentRepository.deleteById(UUID.fromString(id));
        return true;

    }

    /**
     * @see bw.co.centralkyc.organisation.document.OrganisationDocumentService#getAll()
     */
    @Override
    protected Collection<OrganisationDocumentDTO> handleGetAll()
        throws Exception
    {

        Collection<OrganisationDocument> entities = organisationDocumentRepository.findAll();
        return organisationDocumentDao.toOrganisationDocumentDTOCollection(entities);

    }

    /**
     * @see bw.co.centralkyc.organisation.document.OrganisationDocumentService#search(OrganisationDocumentSearchCriteria, Set<PropertySearchOrder>)
     */
    @Override
    protected Collection<OrganisationDocumentDTO> handleSearch(OrganisationDocumentSearchCriteria criteria, Set<PropertySearchOrder> sortProperties)
        throws Exception
    {
        // TODO implement protected  Collection<OrganisationDocumentDTO> handleSearch(OrganisationDocumentSearchCriteria criteria, Set<PropertySearchOrder> sortProperties)
        throw new UnsupportedOperationException("bw.co.centralkyc.organisation.document.OrganisationDocumentService.handleSearch(OrganisationDocumentSearchCriteria criteria, Set<PropertySearchOrder> sortProperties) Not implemented!");
    }

    /**
     * @see bw.co.centralkyc.organisation.document.OrganisationDocumentService#getAll(Integer, Integer)
     */
    @Override
    protected Page<OrganisationDocumentDTO> handleGetAll(Integer pageNumber, Integer pageSize)
        throws Exception
    {

        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        Page<OrganisationDocument> entities = organisationDocumentRepository.findAll(pageRequest);
        return entities.map(entity -> organisationDocumentDao.toOrganisationDocumentDTO(entity));

    }

    /**
     * @see bw.co.centralkyc.organisation.document.OrganisationDocumentService#search(SearchObject<OrganisationDocumentSearchCriteria>)
     */
    @Override
    protected Page<OrganisationDocumentDTO> handleSearch(SearchObject<OrganisationDocumentSearchCriteria> criteria)
        throws Exception
    {
        // TODO implement protected  Page<OrganisationDocumentDTO> handleSearch(SearchObject<OrganisationDocumentSearchCriteria> criteria)
        throw new UnsupportedOperationException("bw.co.centralkyc.organisation.document.OrganisationDocumentService.handleSearch(SearchObject<OrganisationDocumentSearchCriteria> criteria) Not implemented!");
    }

    /**
     * @see bw.co.centralkyc.organisation.document.OrganisationDocumentService#findByOrganisation(String)
     */
    @Override
    protected Collection<OrganisationDocumentDTO> handleFindByOrganisation(String organisationId)
        throws Exception
    {

        Specification<OrganisationDocument> specification = (root, query, criteriaBuilder) -> 
            criteriaBuilder.equal(root.get("organisation").get("id"), organisationId);

        // TODO implement protected  Collection<OrganisationDocumentDTO> handleFindByOrganisation(String organisationId)
        throw new UnsupportedOperationException("bw.co.centralkyc.organisation.document.OrganisationDocumentService.handleFindByOrganisation(String organisationId) Not implemented!");
    }

    /**
     * @see bw.co.centralkyc.organisation.document.OrganisationDocumentService#findByOrganisation(String, Integer, Integer)
     */
    @Override
    protected Page<OrganisationDocumentDTO> handleFindByOrganisation(String organisationId, Integer pageNumber, Integer pageSize)
        throws Exception
    {
        // TODO implement protected  Page<OrganisationDocumentDTO> handleFindByOrganisation(String organisationId, Integer pageNumber, Integer pageSize)
        throw new UnsupportedOperationException("bw.co.centralkyc.organisation.document.OrganisationDocumentService.handleFindByOrganisation(String organisationId, Integer pageNumber, Integer pageSize) Not implemented!");
    }

    /**
     * @see bw.co.centralkyc.organisation.document.OrganisationDocumentService#findByStatus(OrganisationDocumentStatus)
     */
    @Override
    protected Collection<OrganisationDocumentDTO> handleFindByStatus(OrganisationDocumentStatus status)
        throws Exception
    {
        // TODO implement protected  Collection<OrganisationDocumentDTO> handleFindByStatus(OrganisationDocumentStatus status)
        throw new UnsupportedOperationException("bw.co.centralkyc.organisation.document.OrganisationDocumentService.handleFindByStatus(OrganisationDocumentStatus status) Not implemented!");
    }

    /**
     * @see bw.co.centralkyc.organisation.document.OrganisationDocumentService#findByStatus(OrganisationDocumentStatus, Integer, Integer)
     */
    @Override
    protected Page<OrganisationDocumentDTO> handleFindByStatus(OrganisationDocumentStatus status, Integer pageNumber, Integer pageSize)
        throws Exception
    {
        // TODO implement protected  Page<OrganisationDocumentDTO> handleFindByStatus(OrganisationDocumentStatus status, Integer pageNumber, Integer pageSize)
        throw new UnsupportedOperationException("bw.co.centralkyc.organisation.document.OrganisationDocumentService.handleFindByStatus(OrganisationDocumentStatus status, Integer pageNumber, Integer pageSize) Not implemented!");
    }

}