package bw.co.knowvera.subscription;

import bw.co.knowvera.SearchObject;
import jakarta.validation.Valid;
import java.util.List;
import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 
 * TODO: Model Documentation for bw.co.knowvera.subscription.KycSubscriptionApi
 */
@RequestMapping("/subscriptions")
public interface KycSubscriptionApi {

    @GetMapping(
        value = "/{id}"
    )
    @PreAuthorize(value = "hasAnyAuthority('SCOPE_subscriptions:view', 'SCOPE_subscriptions:view-own')")
    public ResponseEntity<KycSubscriptionDTO> findById(@PathVariable(name = "id", required = true) String id) throws Exception;

    @GetMapping(
        value = "/by-organisation/{organisationId}"
    )
    @PreAuthorize(value = "hasAnyAuthority('SCOPE_subscriptions:list', 'SCOPE_subscriptions:list-own')")
    public @Nullable ResponseEntity<List<KycSubscriptionDTO>> findByOrganisation(@PathVariable(name = "organisationId", required = true) String organisationId) throws Exception;

    @GetMapping(
        
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_subscriptions:list')")
    public @Nullable ResponseEntity<List<KycSubscriptionDTO>> getAll() throws Exception;

    @GetMapping(
        value = "/paged"
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_subscriptions:list')")
    public @Nullable ResponseEntity<Page<KycSubscriptionDTO>> getAllPaged(@RequestParam(name = "pageNumber", required = false) @Nullable Integer pageNumber, @RequestParam(name = "pageSize", required = false) @Nullable Integer pageSize) throws Exception;

    @PostMapping(
        value = "/search/paged"
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_subscriptions:list')")
    public @Nullable ResponseEntity<Page<KycSubscriptionDTO>> pagedSearch(@RequestBody(required = false) @Nullable @Valid SearchObject<SubscriptionSearchCriteria> criteria) throws Exception;

    @DeleteMapping(
        value = "/{id}"
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_subscriptions:delete')")
    public ResponseEntity<Boolean> remove(@PathVariable(name = "id", required = true) String id) throws Exception;

    @PostMapping(
        
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_subscriptions:edit')")
    public ResponseEntity<KycSubscriptionDTO> save(@RequestBody(required = true) @Valid KycSubscriptionDTO subscription) throws Exception;

    @PostMapping(
        value = "/search"
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_subscriptions:list')")
    public @Nullable ResponseEntity<List<KycSubscriptionDTO>> search(@RequestBody(required = false) @Nullable @Valid SearchObject<SubscriptionSearchCriteria> criteria) throws Exception;
}