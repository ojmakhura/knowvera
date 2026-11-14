package bw.co.knowvera.individual;

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
 * TODO: Model Documentation for bw.co.knowvera.individual.IndividualApi
 */
@RequestMapping("/individuals")
public interface IndividualApi {

    @GetMapping(
        value = "/{id}"
    )
    @PreAuthorize(value = "hasAnyAuthority('SCOPE_individuals:view', 'SCOPE_individuals:view-own')")
    public ResponseEntity<IndividualDTO> findById(@PathVariable(name = "id", required = true) String id) throws Exception;

    @GetMapping(
        
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_individuals:list')")
    public @Nullable ResponseEntity<List<IndividualListDTO>> getAll() throws Exception;

    @GetMapping(
        value = "/paged"
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_individuals:list')")
    public @Nullable ResponseEntity<Page<IndividualListDTO>> getAllPaged(@RequestParam(name = "pageNumber", required = false) @Nullable Integer pageNumber, @RequestParam(name = "pageSize", required = false) @Nullable Integer pageSize) throws Exception;

    @GetMapping(
        value = "/organisation/{organisationId}/organisationId/{organisationId}"
    )
    @PreAuthorize(value = "hasAnyAuthority('SCOPE_individuals:list', 'SCOPE_individuals:list-own')")
    public @Nullable ResponseEntity<List<IndividualListDTO>> getOrganisationClients(@PathVariable(name = "organisationId", required = false) @Nullable String organisationId) throws Exception;

    @GetMapping(
        value = "/organisation/{organisationId}/paged"
    )
    @PreAuthorize(value = "hasAnyAuthority('SCOPE_individuals:list', 'SCOPE_individuals:list-own')")
    public @Nullable ResponseEntity<Page<IndividualListDTO>> getOrganisationClientsPaged(@PathVariable(name = "organisationId", required = true) String organisationId, @RequestParam(name = "pageNumber", required = false) @Nullable Integer pageNumber, @RequestParam(name = "pageSize", required = false) @Nullable Integer pageSize) throws Exception;

    @GetMapping(
        value = "/me"
    )
    public ResponseEntity<IndividualDTO> loadMe() throws Exception;

    @GetMapping(
        value = "/request/{requestId}"
    )
    public ResponseEntity<IndividualDTO> loadRequestIndividual(@PathVariable(name = "requestId", required = true) String requestId, @RequestParam(name = "identityConfirmationToken", required = true) String identityConfirmationToken, @RequestParam(name = "identityNo", required = true) String identityNo) throws Exception;

    @PostMapping(
        value = "/search/paged"
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_individuals:list')")
    public @Nullable ResponseEntity<Page<IndividualListDTO>> pagedSearch(@RequestBody(required = true) @Valid SearchObject<IndividualSearchCriteria> criteria) throws Exception;

    @DeleteMapping(
        value = "/{id}"
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_individuals:delete')")
    public ResponseEntity<Boolean> remove(@PathVariable(name = "id", required = true) String id) throws Exception;

    @PostMapping(
        
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_individuals:edit')")
    public ResponseEntity<IndividualDTO> save(@RequestBody(required = true) @Valid IndividualDTO individual) throws Exception;

    @PostMapping(
        value = "search"
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_individuals:list')")
    public @Nullable ResponseEntity<List<IndividualListDTO>> search(@RequestBody(required = false) @Nullable @Valid SearchObject<IndividualSearchCriteria> criteria) throws Exception;

    @GetMapping(
        value = "/{id}/verification"
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_individuals:verify')")
    public ResponseEntity<IndividualDTO> verifyIndividual(@PathVariable(name = "id", required = true) String id) throws Exception;
}