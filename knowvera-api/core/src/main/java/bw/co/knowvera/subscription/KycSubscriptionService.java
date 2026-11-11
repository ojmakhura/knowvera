package bw.co.knowvera.subscription;

import bw.co.knowvera.PropertySearchOrder;
import bw.co.knowvera.SearchObject;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Set;
import org.springframework.data.domain.Page;

/**
 * TODO: Model Documentation for bw.co.knowvera.subscription.KycSubscriptionService
 */
public interface KycSubscriptionService
{
    /**
     * TODO: Model Documentation for bw.co.knowvera.subscription.KycSubscriptionService.findById
     * @param id TODO: Model Documentation for bw.co.knowvera.subscription.KycSubscriptionService.findById(id)
     * @return KycSubscriptionDTO
     */
    public KycSubscriptionDTO findById(String id) ;

    /**
     * TODO: Model Documentation for bw.co.knowvera.subscription.KycSubscriptionService.save
     * @param subscription TODO: Model Documentation for bw.co.knowvera.subscription.KycSubscriptionService.save(subscription)
     * @return KycSubscriptionDTO
     */
    public KycSubscriptionDTO save(@Valid KycSubscriptionDTO subscription) ;

    /**
     * TODO: Model Documentation for bw.co.knowvera.subscription.KycSubscriptionService.remove
     * @param id TODO: Model Documentation for bw.co.knowvera.subscription.KycSubscriptionService.remove(id)
     * @return boolean
     */
    public boolean remove(String id) ;

    /**
     * TODO: Model Documentation for bw.co.knowvera.subscription.KycSubscriptionService.getAll
     * @return List<KycSubscriptionDTO>
     */
    public List<KycSubscriptionDTO> getAll() ;

    /**
     * TODO: Model Documentation for bw.co.knowvera.subscription.KycSubscriptionService.search
     * @param criteria TODO: Model Documentation for bw.co.knowvera.subscription.KycSubscriptionService.search(criteria)
     * @param sortOrders TODO: Model Documentation for bw.co.knowvera.subscription.KycSubscriptionService.search(sortOrders)
     * @return List<KycSubscriptionDTO>
     */
    public List<KycSubscriptionDTO> search(@Valid SubscriptionSearchCriteria criteria, @Valid Set<PropertySearchOrder> sortOrders) ;

    /**
     * TODO: Model Documentation for bw.co.knowvera.subscription.KycSubscriptionService.getAll
     * @param pageNumber TODO: Model Documentation for bw.co.knowvera.subscription.KycSubscriptionService.getAll(pageNumber)
     * @param pageSize TODO: Model Documentation for bw.co.knowvera.subscription.KycSubscriptionService.getAll(pageSize)
     * @return Page<KycSubscriptionDTO>
     */
    public Page<KycSubscriptionDTO> getAll(Integer pageNumber, Integer pageSize) ;

    /**
     * TODO: Model Documentation for bw.co.knowvera.subscription.KycSubscriptionService.search
     * @param criteria TODO: Model Documentation for bw.co.knowvera.subscription.KycSubscriptionService.search(criteria)
     * @return Page<KycSubscriptionDTO>
     */
    public Page<KycSubscriptionDTO> search(SearchObject<SubscriptionSearchCriteria> criteria) ;

    /**
     * TODO: Model Documentation for
     * bw.co.knowvera.subscription.KycSubscriptionService.findByOrganisation
     * @param organisationId TODO: Model Documentation for
bw.co.knowvera.subscription.KycSubscriptionService.findByOrganisation(organisationId)
     * @param user TODO: Model Documentation for
bw.co.knowvera.subscription.KycSubscriptionService.findByOrganisation(user)
     * @return List<KycSubscriptionDTO>
     */
    public List<KycSubscriptionDTO> findByOrganisation(String organisationId, String user) ;

    /**
     * TODO: Model Documentation for
     * bw.co.knowvera.subscription.KycSubscriptionService.countByStatus
     * @param status TODO: Model Documentation for
bw.co.knowvera.subscription.KycSubscriptionService.countByStatus(status)
     * @return Long
     */
    public Long countByStatus(KycSubsciptionStatus status) ;

    /**
     * TODO: Model Documentation for bw.co.knowvera.subscription.KycSubscriptionService.count
     * @return Long
     */
    public Long count() ;

}