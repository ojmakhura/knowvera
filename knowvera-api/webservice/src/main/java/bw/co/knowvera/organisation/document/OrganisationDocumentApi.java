package bw.co.knowvera.organisation.document;

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
 * TODO: Model Documentation for bw.co.knowvera.organisation.document.OrganisationDocumentApi
 */
@RequestMapping("/organisation-document-types")
public interface OrganisationDocumentApi {

    @GetMapping(
        value = "/{id}"
    )
    @PreAuthorize(value = "hasAnyAuthority('SCOPE_organisation-document-types:view', 'SCOPE_organisation-document-types:view-own')")
    public ResponseEntity<OrganisationDocumentDTO> findById(@PathVariable(name = "id", required = true) String id) throws Exception;

    @GetMapping(
        value = "/by-org/{organisationId}"
    )
    @PreAuthorize(value = "hasAnyAuthority('SCOPE_organisation-document-types:list', 'SCOPE_organisation-document-types:list-own')")
    public @Nullable ResponseEntity<List<OrganisationDocumentDTO>> findByOrganisation(@PathVariable(name = "organisationId", required = true) String organisationId) throws Exception;

    @GetMapping(
        value = "/by-org/{organisationId}/paged"
    )
    @PreAuthorize(value = "hasAnyAuthority('SCOPE_organisation-document-types:list', 'SCOPE_organisation-document-types:list-own')")
    public @Nullable ResponseEntity<Page<OrganisationDocumentDTO>> findByOrganisationPaged(@PathVariable(name = "organisationId", required = true) String organisationId, @RequestParam(name = "pageNumber", required = false) @Nullable Integer pageNumber, @RequestParam(name = "pageSize", required = false) @Nullable Integer pageSize) throws Exception;

    @GetMapping(
        value = "/status"
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_organisation-document-types:list')")
    public @Nullable ResponseEntity<List<OrganisationDocumentDTO>> findByStatus(@RequestParam(name = "status", required = true) OrganisationDocumentStatus status) throws Exception;

    @GetMapping(
        value = "/status/paged"
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_organisation-document-types:list')")
    public @Nullable ResponseEntity<Page<OrganisationDocumentDTO>> findByStatusPaged(@RequestParam(name = "status", required = true) OrganisationDocumentStatus status, @RequestParam(name = "pageNumber", required = false) @Nullable Integer pageNumber, @RequestParam(name = "pageSize", required = false) @Nullable Integer pageSize) throws Exception;

    @GetMapping(
        value = "all"
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_organisation-document-types:list')")
    public @Nullable ResponseEntity<List<OrganisationDocumentDTO>> getAll() throws Exception;

    @GetMapping(
        value = "/paged"
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_organisation-document-types:list')")
    public @Nullable ResponseEntity<Page<OrganisationDocumentDTO>> getAllPaged(@RequestParam(name = "pageNumber", required = false) @Nullable Integer pageNumber, @RequestParam(name = "pageSize", required = false) @Nullable Integer pageSize) throws Exception;

    @PostMapping(
        value = "/search/paged"
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_organisation-document-types:list')")
    public @Nullable ResponseEntity<Page<OrganisationDocumentDTO>> pagedSearch(@RequestBody(required = false) @Nullable @Valid SearchObject<OrganisationDocumentSearchCriteria> criteria) throws Exception;

    @DeleteMapping(
        value = "/{id}"
    )
    @PreAuthorize(value = "hasAnyAuthority('SCOPE_organisation-document-types:delete', 'SCOPE_organisation-document-types:delete-own')")
    public ResponseEntity<Boolean> remove(@PathVariable(name = "id", required = true) String id) throws Exception;

    @PostMapping(
        
    )
    @PreAuthorize(value = "hasAnyAuthority('SCOPE_organisation-document-types:edit', 'SCOPE_organisation-document-types:edit-own')")
    public ResponseEntity<OrganisationDocumentDTO> save(@RequestBody(required = true) @Valid OrganisationDocumentDTO clientRequest) throws Exception;

    @PostMapping(
        value = "search"
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_organisation-document-types:list')")
    public @Nullable ResponseEntity<List<OrganisationDocumentDTO>> search(@RequestBody(required = false) @Nullable @Valid SearchObject<OrganisationDocumentSearchCriteria> criteria) throws Exception;
}