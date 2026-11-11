package bw.co.knowvera.organisation.document;

import bw.co.knowvera.PropertySearchOrder;
import bw.co.knowvera.SearchObject;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Set;
import org.springframework.data.domain.Page;

/**
 * TODO: Model Documentation for bw.co.knowvera.organisation.document.OrganisationDocumentService
 */
public interface OrganisationDocumentService
{
    /**
     * TODO: Model Documentation for
     * bw.co.knowvera.organisation.document.OrganisationDocumentService.findById
     * @param id TODO: Model Documentation for
bw.co.knowvera.organisation.document.OrganisationDocumentService.findById(id)
     * @return OrganisationDocumentDTO
     */
    public OrganisationDocumentDTO findById(String id) ;

    /**
     * TODO: Model Documentation for
     * bw.co.knowvera.organisation.document.OrganisationDocumentService.save
     * @param clientRequest TODO: Model Documentation for
bw.co.knowvera.organisation.document.OrganisationDocumentService.save(clientRequest)
     * @return OrganisationDocumentDTO
     */
    public OrganisationDocumentDTO save(@Valid OrganisationDocumentDTO clientRequest) ;

    /**
     * TODO: Model Documentation for
     * bw.co.knowvera.organisation.document.OrganisationDocumentService.remove
     * @param id TODO: Model Documentation for
bw.co.knowvera.organisation.document.OrganisationDocumentService.remove(id)
     * @return boolean
     */
    public boolean remove(String id) ;

    /**
     * TODO: Model Documentation for
     * bw.co.knowvera.organisation.document.OrganisationDocumentService.getAll
     * @return List<OrganisationDocumentDTO>
     */
    public List<OrganisationDocumentDTO> getAll() ;

    /**
     * TODO: Model Documentation for
     * bw.co.knowvera.organisation.document.OrganisationDocumentService.search
     * @param criteria TODO: Model Documentation for
bw.co.knowvera.organisation.document.OrganisationDocumentService.search(criteria)
     * @param sortProperties TODO: Model Documentation for
bw.co.knowvera.organisation.document.OrganisationDocumentService.search(sortProperties)
     * @return List<OrganisationDocumentDTO>
     */
    public List<OrganisationDocumentDTO> search(@Valid OrganisationDocumentSearchCriteria criteria, @Valid Set<PropertySearchOrder> sortProperties) ;

    /**
     * TODO: Model Documentation for
     * bw.co.knowvera.organisation.document.OrganisationDocumentService.getAll
     * @param pageNumber TODO: Model Documentation for
bw.co.knowvera.organisation.document.OrganisationDocumentService.getAll(pageNumber)
     * @param pageSize TODO: Model Documentation for
bw.co.knowvera.organisation.document.OrganisationDocumentService.getAll(pageSize)
     * @return Page<OrganisationDocumentDTO>
     */
    public Page<OrganisationDocumentDTO> getAll(Integer pageNumber, Integer pageSize) ;

    /**
     * TODO: Model Documentation for
     * bw.co.knowvera.organisation.document.OrganisationDocumentService.search
     * @param criteria TODO: Model Documentation for
bw.co.knowvera.organisation.document.OrganisationDocumentService.search(criteria)
     * @return Page<OrganisationDocumentDTO>
     */
    public Page<OrganisationDocumentDTO> search(SearchObject<OrganisationDocumentSearchCriteria> criteria) ;

    /**
     * TODO: Model Documentation for
     * bw.co.knowvera.organisation.document.OrganisationDocumentService.findByOrganisation
     * @param organisationId TODO: Model Documentation for
bw.co.knowvera.organisation.document.OrganisationDocumentService.findByOrganisation(organisationId)
     * @return List<OrganisationDocumentDTO>
     */
    public List<OrganisationDocumentDTO> findByOrganisation(String organisationId) ;

    /**
     * TODO: Model Documentation for
     * bw.co.knowvera.organisation.document.OrganisationDocumentService.findByOrganisation
     * @param organisationId TODO: Model Documentation for
bw.co.knowvera.organisation.document.OrganisationDocumentService.findByOrganisation(organisationId)
     * @param pageNumber TODO: Model Documentation for
bw.co.knowvera.organisation.document.OrganisationDocumentService.findByOrganisation(pageNumber)
     * @param pageSize TODO: Model Documentation for
bw.co.knowvera.organisation.document.OrganisationDocumentService.findByOrganisation(pageSize)
     * @return Page<OrganisationDocumentDTO>
     */
    public Page<OrganisationDocumentDTO> findByOrganisation(String organisationId, Integer pageNumber, Integer pageSize) ;

    /**
     * TODO: Model Documentation for
     * bw.co.knowvera.organisation.document.OrganisationDocumentService.findByStatus
     * @param status TODO: Model Documentation for
bw.co.knowvera.organisation.document.OrganisationDocumentService.findByStatus(status)
     * @return List<OrganisationDocumentDTO>
     */
    public List<OrganisationDocumentDTO> findByStatus(OrganisationDocumentStatus status) ;

    /**
     * TODO: Model Documentation for
     * bw.co.knowvera.organisation.document.OrganisationDocumentService.findByStatus
     * @param status TODO: Model Documentation for
bw.co.knowvera.organisation.document.OrganisationDocumentService.findByStatus(status)
     * @param pageNumber TODO: Model Documentation for
bw.co.knowvera.organisation.document.OrganisationDocumentService.findByStatus(pageNumber)
     * @param pageSize TODO: Model Documentation for
bw.co.knowvera.organisation.document.OrganisationDocumentService.findByStatus(pageSize)
     * @return Page<OrganisationDocumentDTO>
     */
    public Page<OrganisationDocumentDTO> findByStatus(OrganisationDocumentStatus status, Integer pageNumber, Integer pageSize) ;

}