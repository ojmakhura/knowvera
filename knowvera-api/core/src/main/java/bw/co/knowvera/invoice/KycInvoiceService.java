package bw.co.knowvera.invoice;

import bw.co.knowvera.PropertySearchOrder;
import bw.co.knowvera.SearchObject;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Set;
import org.springframework.data.domain.Page;

/**
 * TODO: Model Documentation for bw.co.knowvera.invoice.KycInvoiceService
 */
public interface KycInvoiceService
{
    /**
     * TODO: Model Documentation for bw.co.knowvera.invoice.KycInvoiceService.findById
     * @param id TODO: Model Documentation for bw.co.knowvera.invoice.KycInvoiceService.findById(id)
     * @return KycInvoiceDTO
     */
    public KycInvoiceDTO findById(String id) ;

    /**
     * TODO: Model Documentation for bw.co.knowvera.invoice.KycInvoiceService.save
     * @param invoice TODO: Model Documentation for bw.co.knowvera.invoice.KycInvoiceService.save(invoice)
     * @return KycInvoiceDTO
     */
    public KycInvoiceDTO save(@Valid KycInvoiceDTO invoice) ;

    /**
     * TODO: Model Documentation for bw.co.knowvera.invoice.KycInvoiceService.remove
     * @param id TODO: Model Documentation for bw.co.knowvera.invoice.KycInvoiceService.remove(id)
     * @return boolean
     */
    public boolean remove(String id) ;

    /**
     * TODO: Model Documentation for bw.co.knowvera.invoice.KycInvoiceService.getAll
     * @return List<KycInvoiceDTO>
     */
    public List<KycInvoiceDTO> getAll() ;

    /**
     * TODO: Model Documentation for bw.co.knowvera.invoice.KycInvoiceService.search
     * @param criteria TODO: Model Documentation for bw.co.knowvera.invoice.KycInvoiceService.search(criteria)
     * @param sortOrders TODO: Model Documentation for bw.co.knowvera.invoice.KycInvoiceService.search(sortOrders)
     * @return List<KycInvoiceDTO>
     */
    public List<KycInvoiceDTO> search(@Valid InvoiceSearchCriteria criteria, @Valid Set<PropertySearchOrder> sortOrders) ;

    /**
     * TODO: Model Documentation for bw.co.knowvera.invoice.KycInvoiceService.getAll
     * @param pageNumber TODO: Model Documentation for bw.co.knowvera.invoice.KycInvoiceService.getAll(pageNumber)
     * @param pageSize TODO: Model Documentation for bw.co.knowvera.invoice.KycInvoiceService.getAll(pageSize)
     * @return Page<KycInvoiceDTO>
     */
    public Page<KycInvoiceDTO> getAll(Integer pageNumber, Integer pageSize) ;

    /**
     * TODO: Model Documentation for bw.co.knowvera.invoice.KycInvoiceService.search
     * @param criteria TODO: Model Documentation for bw.co.knowvera.invoice.KycInvoiceService.search(criteria)
     * @return Page<KycInvoiceDTO>
     */
    public Page<KycInvoiceDTO> search(SearchObject<InvoiceSearchCriteria> criteria) ;

    /**
     * TODO: Model Documentation for bw.co.knowvera.invoice.KycInvoiceService.upload
     * @param id TODO: Model Documentation for bw.co.knowvera.invoice.KycInvoiceService.upload(id)
     * @param uploadUrl TODO: Model Documentation for bw.co.knowvera.invoice.KycInvoiceService.upload(uploadUrl)
     * @param purpose TODO: Model Documentation for bw.co.knowvera.invoice.KycInvoiceService.upload(purpose)
     * @param user TODO: Model Documentation for bw.co.knowvera.invoice.KycInvoiceService.upload(user)
     * @return KycInvoiceDTO
     */
    public KycInvoiceDTO upload(String id, String uploadUrl, UploadPurpose purpose, String user) ;

    /**
     * TODO: Model Documentation for bw.co.knowvera.invoice.KycInvoiceService.generateInvoice
     * @param subscriptionId TODO: Model Documentation for
bw.co.knowvera.invoice.KycInvoiceService.generateInvoice(subscriptionId)
     * @param user TODO: Model Documentation for bw.co.knowvera.invoice.KycInvoiceService.generateInvoice(user)
     * @return List<KycInvoiceDTO>
     */
    public List<KycInvoiceDTO> generateInvoice(String subscriptionId, String user) ;

    /**
     * TODO: Model Documentation for bw.co.knowvera.invoice.KycInvoiceService.findByOrganisation
     * @param organisationId TODO: Model Documentation for
bw.co.knowvera.invoice.KycInvoiceService.findByOrganisation(organisationId)
     * @return List<KycInvoiceDTO>
     */
    public List<KycInvoiceDTO> findByOrganisation(String organisationId) ;

    /**
     * TODO: Model Documentation for bw.co.knowvera.invoice.KycInvoiceService.findBySubscription
     * @param subscriptionId TODO: Model Documentation for
bw.co.knowvera.invoice.KycInvoiceService.findBySubscription(subscriptionId)
     * @return List<KycInvoiceDTO>
     */
    public List<KycInvoiceDTO> findBySubscription(String subscriptionId) ;

    /**
     * TODO: Model Documentation for bw.co.knowvera.invoice.KycInvoiceService.findByOrganisation
     * @param organisationId TODO: Model Documentation for
bw.co.knowvera.invoice.KycInvoiceService.findByOrganisation(organisationId)
     * @param pageNumber TODO: Model Documentation for
bw.co.knowvera.invoice.KycInvoiceService.findByOrganisation(pageNumber)
     * @param pageSize TODO: Model Documentation for bw.co.knowvera.invoice.KycInvoiceService.findByOrganisation(pageSize)
     * @return Page<KycInvoiceDTO>
     */
    public Page<KycInvoiceDTO> findByOrganisation(String organisationId, Integer pageNumber, Integer pageSize) ;

    /**
     * TODO: Model Documentation for bw.co.knowvera.invoice.KycInvoiceService.findBySubscription
     * @param subscriptionId TODO: Model Documentation for
bw.co.knowvera.invoice.KycInvoiceService.findBySubscription(subscriptionId)
     * @param pageNumber TODO: Model Documentation for
bw.co.knowvera.invoice.KycInvoiceService.findBySubscription(pageNumber)
     * @param pageSize TODO: Model Documentation for bw.co.knowvera.invoice.KycInvoiceService.findBySubscription(pageSize)
     * @return Page<KycInvoiceDTO>
     */
    public Page<KycInvoiceDTO> findBySubscription(String subscriptionId, Integer pageNumber, Integer pageSize) ;

    /**
     * TODO: Model Documentation for
     * bw.co.knowvera.invoice.KycInvoiceService.countInvoicesByOrganisationId
     * @param organisationId TODO: Model Documentation for
bw.co.knowvera.invoice.KycInvoiceService.countInvoicesByOrganisationId(organisationId)
     * @return Long
     */
    public Long countInvoicesByOrganisationId(String organisationId) ;

    /**
     * TODO: Model Documentation for bw.co.knowvera.invoice.KycInvoiceService.countInvoices
     * @param paid TODO: Model Documentation for bw.co.knowvera.invoice.KycInvoiceService.countInvoices(paid)
     * @return Long
     */
    public Long countInvoices(Boolean paid) ;

    /**
     * TODO: Model Documentation for
     * bw.co.knowvera.invoice.KycInvoiceService.countOrganisationInvoices
     * @param paid TODO: Model Documentation for
bw.co.knowvera.invoice.KycInvoiceService.countOrganisationInvoices(paid)
     * @param organisationId TODO: Model Documentation for
bw.co.knowvera.invoice.KycInvoiceService.countOrganisationInvoices(organisationId)
     * @return Long
     */
    public Long countOrganisationInvoices(Boolean paid, String organisationId) ;

    /**
     * TODO: Model Documentation for bw.co.knowvera.invoice.KycInvoiceService.count
     * @return Long
     */
    public Long count() ;

}