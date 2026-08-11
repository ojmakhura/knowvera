// license-header java merge-point
/**
 * This is only generated once! It will never be overwritten.
 * You can (and have to!) safely modify it by hand.
 * TEMPLATE:    SpringServiceImpl.vsl in andromda-spring cartridge
 * MODEL CLASS: AndroMDAModel::backend::bw.co.knowvera::organisation::document::OrganisationDocumentService
 * STEREOTYPE:  Service
 */
package bw.co.knowvera.organisation.document;

import bw.co.knowvera.PropertySearchOrder;
import bw.co.knowvera.SearchObject;
import bw.co.knowvera.document.DocumentMapper;
import bw.co.knowvera.document.DocumentRepository;
import bw.co.knowvera.individual.IndividualMapper;
import bw.co.knowvera.individual.IndividualRepository;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @see bw.co.knowvera.organisation.document.OrganisationDocumentService
 */
@Service("organisationDocumentService")
@Transactional(propagation = Propagation.REQUIRED, readOnly=false)
public class OrganisationDocumentServiceImpl
    extends OrganisationDocumentServiceBase
{
    

    public OrganisationDocumentServiceImpl(
            OrganisationDocumentRepository organisationDocumentRepository,
            OrganisationDocumentMapper organisationDocumentMapper,
            IndividualRepository individualRepository, IndividualMapper individualMapper, 
            DocumentRepository documentRepository, DocumentMapper documentMapper) {
        super(organisationDocumentRepository, organisationDocumentMapper,
                individualRepository, individualMapper, documentRepository, documentMapper);
        //TODO Auto-generated constructor stub
    }

    /**
     * @see bw.co.knowvera.organisation.document.OrganisationDocumentService#findById(String)
     */
    @Override
    protected OrganisationDocumentDTO handleFindById(String id)
        throws Exception
    {

        OrganisationDocument entity = organisationDocumentRepository.getReferenceById(UUID.fromString(id));
        return organisationDocumentMapper.toOrganisationDocumentDTO(entity);
    }

    /**
     * @see bw.co.knowvera.organisation.document.OrganisationDocumentService#save(OrganisationDocumentDTO)
     */
    @Override
    protected OrganisationDocumentDTO handleSave(OrganisationDocumentDTO clientRequest)
        throws Exception
    {

        OrganisationDocument organisationDocument = organisationDocumentMapper.organisationDocumentDTOToEntity(clientRequest);
        organisationDocument = organisationDocumentRepository.save(organisationDocument);
        return organisationDocumentMapper.toOrganisationDocumentDTO(organisationDocument);
    }

    /**
     * @see bw.co.knowvera.organisation.document.OrganisationDocumentService#remove(String)
     */
    @Override
    protected boolean handleRemove(String id)
        throws Exception
    {

        organisationDocumentRepository.deleteById(UUID.fromString(id));
        return true;

    }

    /**
     * @see bw.co.knowvera.organisation.document.OrganisationDocumentService#getAll()
     */
    @Override
    protected List<OrganisationDocumentDTO> handleGetAll()
        throws Exception
    {

        List<OrganisationDocument> entities = organisationDocumentRepository.findAll();
        return organisationDocumentMapper.toOrganisationDocumentDTOCollection(entities);

    }

    /**
     * @see bw.co.knowvera.organisation.document.OrganisationDocumentService#search(OrganisationDocumentSearchCriteria, Set<PropertySearchOrder>)
     */
    @Override
    protected List<OrganisationDocumentDTO> handleSearch(OrganisationDocumentSearchCriteria criteria, Set<PropertySearchOrder> sortProperties)
        throws Exception
    {
        // TODO implement protected  Collection<OrganisationDocumentDTO> handleSearch(OrganisationDocumentSearchCriteria criteria, Set<PropertySearchOrder> sortProperties)
        throw new UnsupportedOperationException("bw.co.knowvera.organisation.document.OrganisationDocumentService.handleSearch(OrganisationDocumentSearchCriteria criteria, Set<PropertySearchOrder> sortProperties) Not implemented!");
    }

    /**
     * @see bw.co.knowvera.organisation.document.OrganisationDocumentService#getAll(Integer, Integer)
     */
    @Override
    protected Page<OrganisationDocumentDTO> handleGetAll(Integer pageNumber, Integer pageSize)
        throws Exception
    {

        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        Page<OrganisationDocument> entities = organisationDocumentRepository.findAll(pageRequest);
        return entities.map(entity -> organisationDocumentMapper.toOrganisationDocumentDTO(entity));

    }

    /**
     * @see bw.co.knowvera.organisation.document.OrganisationDocumentService#search(SearchObject<OrganisationDocumentSearchCriteria>)
     */
    @Override
    protected Page<OrganisationDocumentDTO> handleSearch(SearchObject<OrganisationDocumentSearchCriteria> criteria)
        throws Exception
    {
        // TODO implement protected  Page<OrganisationDocumentDTO> handleSearch(SearchObject<OrganisationDocumentSearchCriteria> criteria)
        throw new UnsupportedOperationException("bw.co.knowvera.organisation.document.OrganisationDocumentService.handleSearch(SearchObject<OrganisationDocumentSearchCriteria> criteria) Not implemented!");
    }

    /**
     * @see bw.co.knowvera.organisation.document.OrganisationDocumentService#findByOrganisation(String)
     */
    @Override
    protected List<OrganisationDocumentDTO> handleFindByOrganisation(String organisationId)
        throws Exception
    {

        Specification<OrganisationDocument> specification = (root, query, criteriaBuilder) -> 
            criteriaBuilder.equal(root.get("organisation").get("id"), organisationId);

        // TODO implement protected  Collection<OrganisationDocumentDTO> handleFindByOrganisation(String organisationId)
        throw new UnsupportedOperationException("bw.co.knowvera.organisation.document.OrganisationDocumentService.handleFindByOrganisation(String organisationId) Not implemented!");
    }

    /**
     * @see bw.co.knowvera.organisation.document.OrganisationDocumentService#findByOrganisation(String, Integer, Integer)
     */
    @Override
    protected Page<OrganisationDocumentDTO> handleFindByOrganisation(String organisationId, Integer pageNumber, Integer pageSize)
        throws Exception
    {
        // TODO implement protected  Page<OrganisationDocumentDTO> handleFindByOrganisation(String organisationId, Integer pageNumber, Integer pageSize)
        throw new UnsupportedOperationException("bw.co.knowvera.organisation.document.OrganisationDocumentService.handleFindByOrganisation(String organisationId, Integer pageNumber, Integer pageSize) Not implemented!");
    }

    /**
     * @see bw.co.knowvera.organisation.document.OrganisationDocumentService#findByStatus(OrganisationDocumentStatus)
     */
    @Override
    protected List<OrganisationDocumentDTO> handleFindByStatus(OrganisationDocumentStatus status)
        throws Exception
    {
        // TODO implement protected  Collection<OrganisationDocumentDTO> handleFindByStatus(OrganisationDocumentStatus status)
        throw new UnsupportedOperationException("bw.co.knowvera.organisation.document.OrganisationDocumentService.handleFindByStatus(OrganisationDocumentStatus status) Not implemented!");
    }

    /**
     * @see bw.co.knowvera.organisation.document.OrganisationDocumentService#findByStatus(OrganisationDocumentStatus, Integer, Integer)
     */
    @Override
    protected Page<OrganisationDocumentDTO> handleFindByStatus(OrganisationDocumentStatus status, Integer pageNumber, Integer pageSize)
        throws Exception
    {
        // TODO implement protected  Page<OrganisationDocumentDTO> handleFindByStatus(OrganisationDocumentStatus status, Integer pageNumber, Integer pageSize)
        throw new UnsupportedOperationException("bw.co.knowvera.organisation.document.OrganisationDocumentService.handleFindByStatus(OrganisationDocumentStatus status, Integer pageNumber, Integer pageSize) Not implemented!");
    }

}