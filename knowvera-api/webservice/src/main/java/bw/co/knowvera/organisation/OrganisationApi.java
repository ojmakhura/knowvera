package bw.co.knowvera.organisation;

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
 * TODO: Model Documentation for bw.co.knowvera.organisation.OrganisationApi
 */
@RequestMapping("/organisations")
public interface OrganisationApi {

    @GetMapping(
        value = "/{id}"
    )
    @PreAuthorize(value = "hasAnyAuthority('SCOPE_organisations:view', 'SCOPE_organisations:view-own')")
    public ResponseEntity<OrganisationDTO> findById(@PathVariable(name = "id", required = true) String id) throws Exception;

    @GetMapping(
        value = "/registration/{registrationNo}"
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_organisations:view')")
    public ResponseEntity<OrganisationDTO> findByRegistrationNo(@PathVariable(name = "registrationNo", required = true) String registrationNo) throws Exception;

    @GetMapping(
        value = "/list/{id}"
    )
    @PreAuthorize(value = "hasAnyAuthority('SCOPE_organisations:view', 'SCOPE_organisations:view-own')")
    public ResponseEntity<OrganisationListDTO> findListOrganisationById(@PathVariable(name = "id", required = true) String id) throws Exception;

    @GetMapping(
        
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_organisations:list')")
    public @Nullable ResponseEntity<List<OrganisationListDTO>> getAll() throws Exception;

    @GetMapping(
        value = "/paged"
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_organisations:list')")
    public @Nullable ResponseEntity<Page<OrganisationListDTO>> getAllPaged(@RequestParam(name = "pageNumber", required = false) @Nullable Integer pageNumber, @RequestParam(name = "pageSize", required = false) @Nullable Integer pageSize) throws Exception;

    @GetMapping(
        value = "/mine"
    )
    public ResponseEntity<OrganisationDTO> loadMyOrganisation() throws Exception;

    @GetMapping(
        value = "/request/{requestId}"
    )
    public ResponseEntity<OrganisationDTO> loadRequestOrganisation(@PathVariable(name = "requestId", required = true) String requestId, @RequestParam(name = "identityConfirmationToken", required = true) String identityConfirmationToken, @RequestParam(name = "registrationNo", required = true) String registrationNo) throws Exception;

    @PostMapping(
        value = "/search/paged"
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_organisations:list')")
    public @Nullable ResponseEntity<Page<OrganisationListDTO>> pagedSearch(@RequestBody(required = true) @Valid SearchObject<OrganisationSearchCriteria> criteria) throws Exception;

    @DeleteMapping(
        value = "/{id}"
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_organisations:delete')")
    public ResponseEntity<Boolean> remove(@PathVariable(name = "id", required = true) String id) throws Exception;

    @PostMapping(
        
    )
    @PreAuthorize(value = "hasAnyAuthority('SCOPE_organisations:edit', 'SCOPE_organisations:edit-own')")
    public ResponseEntity<OrganisationDTO> save(@RequestBody(required = true) @Valid OrganisationDTO organisation) throws Exception;

    @PostMapping(
        value = "search"
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_organisations:list')")
    public @Nullable ResponseEntity<List<OrganisationListDTO>> search(@RequestBody(required = false) @Nullable @Valid SearchObject<OrganisationSearchCriteria> criteria) throws Exception;

    @GetMapping(
        value = "/{id}/verification"
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_organisations:verify')")
    public ResponseEntity<OrganisationDTO> verifyOrganisation(@PathVariable(name = "id", required = true) String id) throws Exception;
}