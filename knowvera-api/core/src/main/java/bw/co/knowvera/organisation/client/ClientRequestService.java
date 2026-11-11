package bw.co.knowvera.organisation.client;

import bw.co.knowvera.PropertySearchOrder;
import bw.co.knowvera.SearchObject;
import bw.co.knowvera.TargetEntity;
import bw.co.knowvera.document.DocumentDTO;
import jakarta.validation.Valid;
import java.io.InputStream;
import java.util.List;
import java.util.Set;
import org.springframework.data.domain.Page;

/**
 * TODO: Model Documentation for bw.co.knowvera.organisation.client.ClientRequestService
 */
public interface ClientRequestService
{
    /**
     * TODO: Model Documentation for
     * bw.co.knowvera.organisation.client.ClientRequestService.findById
     * @param id TODO: Model Documentation for bw.co.knowvera.organisation.client.ClientRequestService.findById(id)
     * @return ClientRequestDTO
     */
    public ClientRequestDTO findById(String id) ;

    /**
     * TODO: Model Documentation for bw.co.knowvera.organisation.client.ClientRequestService.save
     * @param clientRequest TODO: Model Documentation for
bw.co.knowvera.organisation.client.ClientRequestService.save(clientRequest)
     * @return ClientRequestDTO
     */
    public ClientRequestDTO save(@Valid ClientRequestDTO clientRequest) ;

    /**
     * TODO: Model Documentation for bw.co.knowvera.organisation.client.ClientRequestService.remove
     * @param id TODO: Model Documentation for bw.co.knowvera.organisation.client.ClientRequestService.remove(id)
     * @return boolean
     */
    public boolean remove(String id) ;

    /**
     * TODO: Model Documentation for bw.co.knowvera.organisation.client.ClientRequestService.getAll
     * @return List<ClientRequestDTO>
     */
    public List<ClientRequestDTO> getAll() ;

    /**
     * TODO: Model Documentation for bw.co.knowvera.organisation.client.ClientRequestService.search
     * @param criteria TODO: Model Documentation for
bw.co.knowvera.organisation.client.ClientRequestService.search(criteria)
     * @param sortProperties TODO: Model Documentation for
bw.co.knowvera.organisation.client.ClientRequestService.search(sortProperties)
     * @return List<ClientRequestDTO>
     */
    public List<ClientRequestDTO> search(@Valid ClientRequestSearchCriteria criteria, @Valid Set<PropertySearchOrder> sortProperties) ;

    /**
     * TODO: Model Documentation for bw.co.knowvera.organisation.client.ClientRequestService.getAll
     * @param pageNumber TODO: Model Documentation for
bw.co.knowvera.organisation.client.ClientRequestService.getAll(pageNumber)
     * @param pageSize TODO: Model Documentation for
bw.co.knowvera.organisation.client.ClientRequestService.getAll(pageSize)
     * @return Page<ClientRequestDTO>
     */
    public Page<ClientRequestDTO> getAll(Integer pageNumber, Integer pageSize) ;

    /**
     * TODO: Model Documentation for bw.co.knowvera.organisation.client.ClientRequestService.search
     * @param criteria TODO: Model Documentation for
bw.co.knowvera.organisation.client.ClientRequestService.search(criteria)
     * @return Page<ClientRequestDTO>
     */
    public Page<ClientRequestDTO> search(SearchObject<ClientRequestSearchCriteria> criteria) ;

    /**
     * TODO: Model Documentation for
     * bw.co.knowvera.organisation.client.ClientRequestService.findByOrganisation
     * @param organisationId TODO: Model Documentation for
bw.co.knowvera.organisation.client.ClientRequestService.findByOrganisation(organisationId)
     * @return List<ClientRequestDTO>
     */
    public List<ClientRequestDTO> findByOrganisation(String organisationId) ;

    /**
     * TODO: Model Documentation for
     * bw.co.knowvera.organisation.client.ClientRequestService.findByOrganisation
     * @param organisationId TODO: Model Documentation for
bw.co.knowvera.organisation.client.ClientRequestService.findByOrganisation(organisationId)
     * @param pageNumber TODO: Model Documentation for
bw.co.knowvera.organisation.client.ClientRequestService.findByOrganisation(pageNumber)
     * @param pageSize TODO: Model Documentation for
bw.co.knowvera.organisation.client.ClientRequestService.findByOrganisation(pageSize)
     * @return Page<ClientRequestDTO>
     */
    public Page<ClientRequestDTO> findByOrganisation(String organisationId, Integer pageNumber, Integer pageSize) ;

    /**
     * TODO: Model Documentation for
     * bw.co.knowvera.organisation.client.ClientRequestService.findByStatus
     * @param status TODO: Model Documentation for
bw.co.knowvera.organisation.client.ClientRequestService.findByStatus(status)
     * @return List<ClientRequestDTO>
     */
    public List<ClientRequestDTO> findByStatus(ClientRequestStatus status) ;

    /**
     * TODO: Model Documentation for
     * bw.co.knowvera.organisation.client.ClientRequestService.uploadRequests
     * @param inputStream TODO: Model Documentation for
bw.co.knowvera.organisation.client.ClientRequestService.uploadRequests(inputStream)
     * @param user TODO: Model Documentation for
bw.co.knowvera.organisation.client.ClientRequestService.uploadRequests(user)
     * @param organisationId TODO: Model Documentation for
bw.co.knowvera.organisation.client.ClientRequestService.uploadRequests(organisationId)
     * @param document TODO: Model Documentation for
bw.co.knowvera.organisation.client.ClientRequestService.uploadRequests(document)
     * @param target TODO: Model Documentation for
bw.co.knowvera.organisation.client.ClientRequestService.uploadRequests(target)
     * @param organisation TODO: Model Documentation for
bw.co.knowvera.organisation.client.ClientRequestService.uploadRequests(organisation)
     * @return Page<ClientRequestDTO>
     */
    public Page<ClientRequestDTO> uploadRequests(InputStream inputStream, String user, String organisationId, @Valid DocumentDTO document, TargetEntity target, String organisation) ;

    /**
     * TODO: Model Documentation for
     * bw.co.knowvera.organisation.client.ClientRequestService.findByIndividual
     * @param individualId TODO: Model Documentation for
bw.co.knowvera.organisation.client.ClientRequestService.findByIndividual(individualId)
     * @return List<ClientRequestDTO>
     */
    public List<ClientRequestDTO> findByIndividual(String individualId) ;

    /**
     * TODO: Model Documentation for
     * bw.co.knowvera.organisation.client.ClientRequestService.findByIndividual
     * @param individualId TODO: Model Documentation for
bw.co.knowvera.organisation.client.ClientRequestService.findByIndividual(individualId)
     * @param pageNumber TODO: Model Documentation for
bw.co.knowvera.organisation.client.ClientRequestService.findByIndividual(pageNumber)
     * @param pageSize TODO: Model Documentation for
bw.co.knowvera.organisation.client.ClientRequestService.findByIndividual(pageSize)
     * @return Page<ClientRequestDTO>
     */
    public Page<ClientRequestDTO> findByIndividual(String individualId, Integer pageNumber, Integer pageSize) ;

    /**
     * TODO: Model Documentation for
     * bw.co.knowvera.organisation.client.ClientRequestService.findByDocument
     * @param documentId TODO: Model Documentation for
bw.co.knowvera.organisation.client.ClientRequestService.findByDocument(documentId)
     * @return List<ClientRequestDTO>
     */
    public List<ClientRequestDTO> findByDocument(String documentId) ;

    /**
     * TODO: Model Documentation for
     * bw.co.knowvera.organisation.client.ClientRequestService.findByDocument
     * @param documentId TODO: Model Documentation for
bw.co.knowvera.organisation.client.ClientRequestService.findByDocument(documentId)
     * @param pageNumber TODO: Model Documentation for
bw.co.knowvera.organisation.client.ClientRequestService.findByDocument(pageNumber)
     * @param pageSize TODO: Model Documentation for
bw.co.knowvera.organisation.client.ClientRequestService.findByDocument(pageSize)
     * @return Page<ClientRequestDTO>
     */
    public Page<ClientRequestDTO> findByDocument(String documentId, Integer pageNumber, Integer pageSize) ;

    /**
     * TODO: Model Documentation for
     * bw.co.knowvera.organisation.client.ClientRequestService.findByStatus
     * @param status TODO: Model Documentation for
bw.co.knowvera.organisation.client.ClientRequestService.findByStatus(status)
     * @param pageNumber TODO: Model Documentation for
bw.co.knowvera.organisation.client.ClientRequestService.findByStatus(pageNumber)
     * @param pageSize TODO: Model Documentation for
bw.co.knowvera.organisation.client.ClientRequestService.findByStatus(pageSize)
     * @return Page<ClientRequestDTO>
     */
    public Page<ClientRequestDTO> findByStatus(ClientRequestStatus status, Integer pageNumber, Integer pageSize) ;

    /**
     * TODO: Model Documentation for
     * bw.co.knowvera.organisation.client.ClientRequestService.findByTarget
     * @param target TODO: Model Documentation for
bw.co.knowvera.organisation.client.ClientRequestService.findByTarget(target)
     * @param targetId TODO: Model Documentation for
bw.co.knowvera.organisation.client.ClientRequestService.findByTarget(targetId)
     * @return List<ClientRequestDTO>
     */
    public List<ClientRequestDTO> findByTarget(TargetEntity target, String targetId) ;

    /**
     * TODO: Model Documentation for
     * bw.co.knowvera.organisation.client.ClientRequestService.findByTarget
     * @param target TODO: Model Documentation for
bw.co.knowvera.organisation.client.ClientRequestService.findByTarget(target)
     * @param targetId TODO: Model Documentation for
bw.co.knowvera.organisation.client.ClientRequestService.findByTarget(targetId)
     * @param pageNumber TODO: Model Documentation for
bw.co.knowvera.organisation.client.ClientRequestService.findByTarget(pageNumber)
     * @param pageSize TODO: Model Documentation for
bw.co.knowvera.organisation.client.ClientRequestService.findByTarget(pageSize)
     * @return Page<ClientRequestDTO>
     */
    public Page<ClientRequestDTO> findByTarget(TargetEntity target, String targetId, Integer pageNumber, Integer pageSize) ;

    /**
     * TODO: Model Documentation for
     * bw.co.knowvera.organisation.client.ClientRequestService.findByTargetAndOrganisation
     * @param target TODO: Model Documentation for
bw.co.knowvera.organisation.client.ClientRequestService.findByTargetAndOrganisation(target)
     * @param targetId TODO: Model Documentation for
bw.co.knowvera.organisation.client.ClientRequestService.findByTargetAndOrganisation(targetId)
     * @param organisationId TODO: Model Documentation for
bw.co.knowvera.organisation.client.ClientRequestService.findByTargetAndOrganisation(organisationId)
     * @return List<ClientRequestDTO>
     */
    public List<ClientRequestDTO> findByTargetAndOrganisation(TargetEntity target, String targetId, String organisationId) ;

    /**
     * TODO: Model Documentation for
     * bw.co.knowvera.organisation.client.ClientRequestService.findByTargetAndOrganisation
     * @param target TODO: Model Documentation for
bw.co.knowvera.organisation.client.ClientRequestService.findByTargetAndOrganisation(target)
     * @param targetId TODO: Model Documentation for
bw.co.knowvera.organisation.client.ClientRequestService.findByTargetAndOrganisation(targetId)
     * @param organisationId TODO: Model Documentation for
bw.co.knowvera.organisation.client.ClientRequestService.findByTargetAndOrganisation(organisationId)
     * @param pageNumber TODO: Model Documentation for
bw.co.knowvera.organisation.client.ClientRequestService.findByTargetAndOrganisation(pageNumber)
     * @param pageSize TODO: Model Documentation for
bw.co.knowvera.organisation.client.ClientRequestService.findByTargetAndOrganisation(pageSize)
     * @return Page<ClientRequestDTO>
     */
    public Page<ClientRequestDTO> findByTargetAndOrganisation(TargetEntity target, String targetId, String organisationId, Integer pageNumber, Integer pageSize) ;

    /**
     * TODO: Model Documentation for
     * bw.co.knowvera.organisation.client.ClientRequestService.updateStatus
     * @param id TODO: Model Documentation for
bw.co.knowvera.organisation.client.ClientRequestService.updateStatus(id)
     * @param status TODO: Model Documentation for
bw.co.knowvera.organisation.client.ClientRequestService.updateStatus(status)
     * @return ClientRequestDTO
     */
    public ClientRequestDTO updateStatus(String id, ClientRequestStatus status) ;

    /**
     * TODO: Model Documentation for
     * bw.co.knowvera.organisation.client.ClientRequestService.confirmToken
     * @param requestId TODO: Model Documentation for
bw.co.knowvera.organisation.client.ClientRequestService.confirmToken(requestId)
     * @param token TODO: Model Documentation for
bw.co.knowvera.organisation.client.ClientRequestService.confirmToken(token)
     * @return String
     */
    public String confirmToken(String requestId, String token) ;

    /**
     * TODO: Model Documentation for
     * bw.co.knowvera.organisation.client.ClientRequestService.countByStatus
     * @param status TODO: Model Documentation for
bw.co.knowvera.organisation.client.ClientRequestService.countByStatus(status)
     * @return Long
     */
    public Long countByStatus(ClientRequestStatus status) ;

    /**
     * TODO: Model Documentation for
     * bw.co.knowvera.organisation.client.ClientRequestService.countByStatusAndOrganisationId
     * @param status TODO: Model Documentation for
bw.co.knowvera.organisation.client.ClientRequestService.countByStatusAndOrganisationId(status)
     * @param organisationId TODO: Model Documentation for
bw.co.knowvera.organisation.client.ClientRequestService.countByStatusAndOrganisationId(organisationId)
     * @return Long
     */
    public Long countByStatusAndOrganisationId(ClientRequestStatus status, String organisationId) ;

    /**
     * TODO: Model Documentation for bw.co.knowvera.organisation.client.ClientRequestService.count
     * @return Long
     */
    public Long count() ;

    /**
     * TODO: Model Documentation for
     * bw.co.knowvera.organisation.client.ClientRequestService.confirmRegistration
     * @param id TODO: Model Documentation for
bw.co.knowvera.organisation.client.ClientRequestService.confirmRegistration(id)
     * @param confirm TODO: Model Documentation for
bw.co.knowvera.organisation.client.ClientRequestService.confirmRegistration(confirm)
     * @param registrationToken TODO: Model Documentation for
bw.co.knowvera.organisation.client.ClientRequestService.confirmRegistration(registrationToken)
     * @return boolean
     */
    public boolean confirmRegistration(String id, Boolean confirm, String registrationToken) ;

    /**
     * TODO: Model Documentation for
     * bw.co.knowvera.organisation.client.ClientRequestService.findUserReadyRequests
     * @return ClientRequestDTO
     */
    public ClientRequestDTO findUserReadyRequests() ;

    /**
     * TODO: Model Documentation for
     * bw.co.knowvera.organisation.client.ClientRequestService.findUserReadyRequests
     * @param pageNumber TODO: Model Documentation for
bw.co.knowvera.organisation.client.ClientRequestService.findUserReadyRequests(pageNumber)
     * @param pageSize TODO: Model Documentation for
bw.co.knowvera.organisation.client.ClientRequestService.findUserReadyRequests(pageSize)
     * @return ClientRequestDTO
     */
    public ClientRequestDTO findUserReadyRequests(Integer pageNumber, Integer pageSize) ;

}