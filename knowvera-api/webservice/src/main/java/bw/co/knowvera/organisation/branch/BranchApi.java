package bw.co.knowvera.organisation.branch;

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
 * TODO: Model Documentation for bw.co.knowvera.organisation.branch.BranchApi
 */
@RequestMapping("/organisation-branches")
public interface BranchApi {

    @GetMapping(
        value = "/{id}"
    )
    @PreAuthorize(value = "hasAnyAuthority('SCOPE_organisation-branches:view', 'SCOPE_organisation-branches:view-own')")
    public ResponseEntity<BranchDTO> findById(@PathVariable(name = "id", required = true) String id) throws Exception;

    @GetMapping(
        value = "/by-org/{organisationId}"
    )
    @PreAuthorize(value = "hasAnyAuthority('SCOPE_organisation-branches:list', 'SCOPE_organisation-branches:list-own')")
    public @Nullable ResponseEntity<List<BranchDTO>> findByOrganisation(@PathVariable(name = "organisationId", required = true) String organisationId) throws Exception;

    @GetMapping(
        value = "/by-org/{organisationId}/paged"
    )
    @PreAuthorize(value = "hasAnyAuthority('SCOPE_organisation-branches:list', 'SCOPE_organisation-branches:list-own')")
    public ResponseEntity<Page<BranchDTO>> findByOrganisationPaged(@PathVariable(name = "organisationId", required = true) String organisationId, @RequestParam(name = "pageNumber", required = false) @Nullable Integer pageNumber, @RequestParam(name = "pageSize", required = false) @Nullable Integer pageSize) throws Exception;

    @GetMapping(
        
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_organisation-branches:list')")
    public @Nullable ResponseEntity<List<BranchDTO>> getAll() throws Exception;

    @GetMapping(
        value = "/paged"
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_organisation-branches:list')")
    public @Nullable ResponseEntity<Page<BranchDTO>> getAllPaged(@RequestParam(name = "pageNumber", required = false) @Nullable Integer pageNumber, @RequestParam(name = "pageSize", required = false) @Nullable Integer pageSize) throws Exception;

    @GetMapping(
        value = "/search/paged"
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_organisation-branches:list')")
    public @Nullable ResponseEntity<Page<BranchDTO>> pagedSearch(@RequestParam(name = "criteria", required = false) @Nullable String criteria, @RequestParam(name = "pageNumber", required = false) @Nullable Integer pageNumber, @RequestParam(name = "pageSize", required = false) @Nullable Integer pageSize) throws Exception;

    @DeleteMapping(
        value = "/{id}"
    )
    @PreAuthorize(value = "hasAnyAuthority('SCOPE_organisation-branches:delete', 'SCOPE_organisation-branches:delete-own')")
    public ResponseEntity<Boolean> remove(@PathVariable(name = "id", required = true) String id) throws Exception;

    @PostMapping(
        
    )
    @PreAuthorize(value = "hasAnyAuthority('SCOPE_organisation-branches:edit', 'SCOPE_organisation-branches:edit-own')")
    public ResponseEntity<BranchDTO> save(@RequestBody(required = true) @Valid BranchDTO branch) throws Exception;

    @GetMapping(
        value = "search"
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_organisation-branches:list')")
    public @Nullable ResponseEntity<List<BranchDTO>> search(@RequestParam(name = "criteria", required = false) @Nullable String criteria) throws Exception;
}