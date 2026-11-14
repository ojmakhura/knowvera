package bw.co.knowvera.organisation.client;

import bw.co.knowvera.SearchObject;
import bw.co.knowvera.TargetEntity;
import jakarta.validation.Valid;
import java.util.List;
import org.jspecify.annotations.Nullable;
import org.springframework.core.io.InputStreamResource;
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
import org.springframework.web.multipart.MultipartFile;

/**
 * 
 * TODO: Model Documentation for bw.co.knowvera.organisation.client.ClientRequestApi
 */
@RequestMapping("/client-requests")
public interface ClientRequestApi {

    @GetMapping(
        value = "/{id}/confirm"
    )
    public ResponseEntity<Boolean> confirmRegistration(@PathVariable(name = "id", required = true) String id, @RequestParam(name = "confirm", required = false) @Nullable Boolean confirm, @RequestParam(name = "registrationToken", required = true) String registrationToken) throws Exception;

    @GetMapping(
        value = "/confirm-token/{requestId}"
    )
    public ResponseEntity<String> confirmToken(@PathVariable(name = "requestId", required = true) String requestId, @RequestParam(name = "token", required = true) String token) throws Exception;

    @GetMapping(
        value = "/download-template"
    )
    @PreAuthorize(value = "hasAnyAuthority('SCOPE_client-requests:export', 'SCOPE_client-requests:export-own')")
    public ResponseEntity<InputStreamResource> downloadRequestTemplate() throws Exception;

    @GetMapping(
        value = "/by-document/{documentId}"
    )
    @PreAuthorize(value = "hasAnyAuthority('SCOPE_client-requests:list', 'SCOPE_client-requests:list-own')")
    public @Nullable ResponseEntity<List<ClientRequestDTO>> findByDocument(@PathVariable(name = "documentId", required = true) String documentId) throws Exception;

    @GetMapping(
        value = "/by-document/{documentId}/paged"
    )
    @PreAuthorize(value = "hasAnyAuthority('SCOPE_client-requests:list', 'SCOPE_client-requests:list-own')")
    public @Nullable ResponseEntity<Page<ClientRequestDTO>> findByDocumentPaged(@PathVariable(name = "documentId", required = true) String documentId, @RequestParam(name = "pageNumber", required = false) @Nullable Integer pageNumber, @RequestParam(name = "pageSize", required = false) @Nullable Integer pageSize) throws Exception;

    @GetMapping(
        value = "/{id}"
    )
    @PreAuthorize(value = "hasAnyAuthority('SCOPE_client-requests:view', 'SCOPE_client-requests:view-own')")
    public ResponseEntity<ClientRequestDTO> findById(@PathVariable(name = "id", required = true) String id) throws Exception;

    @GetMapping(
        value = "/by-individual/{individualId}"
    )
    @PreAuthorize(value = "hasAnyAuthority('SCOPE_client-requests:list', 'SCOPE_client-requests:list-own')")
    public @Nullable ResponseEntity<List<ClientRequestDTO>> findByIndividual(@PathVariable(name = "individualId", required = true) String individualId) throws Exception;

    @GetMapping(
        value = "/by-individual/{individualId}/paged"
    )
    @PreAuthorize(value = "hasAnyAuthority('SCOPE_client-requests:list', 'SCOPE_client-requests:list-own')")
    public @Nullable ResponseEntity<Page<ClientRequestDTO>> findByIndividualPaged(@PathVariable(name = "individualId", required = true) String individualId, @RequestParam(name = "pageNumber", required = false) @Nullable Integer pageNumber, @RequestParam(name = "pageSize", required = false) @Nullable Integer pageSize) throws Exception;

    @GetMapping(
        value = "/by-org/{organisationId}"
    )
    @PreAuthorize(value = "hasAnyAuthority('SCOPE_client-requests:list', 'SCOPE_client-requests:list-own')")
    public @Nullable ResponseEntity<List<ClientRequestDTO>> findByOrganisation(@PathVariable(name = "organisationId", required = true) String organisationId, @RequestParam(name = "target", required = false) @Nullable TargetEntity target) throws Exception;

    @GetMapping(
        value = "/by-org/{organisationId}/paged"
    )
    @PreAuthorize(value = "hasAnyAuthority('SCOPE_client-requests:list', 'SCOPE_client-requests:list-own')")
    public @Nullable ResponseEntity<Page<ClientRequestDTO>> findByOrganisationPaged(@PathVariable(name = "organisationId", required = true) String organisationId, @RequestParam(name = "pageNumber", required = false) @Nullable Integer pageNumber, @RequestParam(name = "pageSize", required = false) @Nullable Integer pageSize, @RequestParam(name = "target", required = false) @Nullable TargetEntity target) throws Exception;

    @GetMapping(
        value = "/status"
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_client-requests:list')")
    public @Nullable ResponseEntity<List<ClientRequestDTO>> findByStatus(@RequestParam(name = "status", required = true) ClientRequestStatus status) throws Exception;

    @GetMapping(
        value = "/status/paged"
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_client-requests:list')")
    public @Nullable ResponseEntity<Page<ClientRequestDTO>> findByStatusPaged(@RequestParam(name = "status", required = true) ClientRequestStatus status, @RequestParam(name = "pageNumber", required = false) @Nullable Integer pageNumber, @RequestParam(name = "pageSize", required = false) @Nullable Integer pageSize) throws Exception;

    @GetMapping(
        value = "/target/{target}/{targetId}"
    )
    @PreAuthorize(value = "hasAnyAuthority('SCOPE_client-requests:list', 'SCOPE_client-requests:list-own')")
    public @Nullable ResponseEntity<List<ClientRequestDTO>> findByTarget(@PathVariable(name = "target", required = true) TargetEntity target, @PathVariable(name = "targetId", required = true) String targetId) throws Exception;

    @GetMapping(
        value = "/target/{target}/{targetId}/paged"
    )
    @PreAuthorize(value = "hasAnyAuthority('SCOPE_client-requests:list', 'SCOPE_client-requests:list-own')")
    public @Nullable ResponseEntity<Page<ClientRequestDTO>> findByTargetPaged(@PathVariable(name = "target", required = true) TargetEntity target, @PathVariable(name = "targetId", required = true) String targetId, @RequestParam(name = "pageNumber", required = false) @Nullable Integer pageNumber, @RequestParam(name = "pageSize", required = false) @Nullable Integer pageSize) throws Exception;

    @GetMapping(
        value = "/by-org/{organisationId}/individuals"
    )
    @PreAuthorize(value = "hasAnyAuthority('SCOPE_client-requests:list', 'SCOPE_client-requests:list-own')")
    public @Nullable ResponseEntity<List<ClientRequestDTO>> findIndividualsByOrganisation(@PathVariable(name = "organisationId", required = true) String organisationId) throws Exception;

    @GetMapping(
        value = "/by-org/{organisationId}/individuals/paged"
    )
    @PreAuthorize(value = "hasAnyAuthority('SCOPE_client-requests:list', 'SCOPE_client-requests:list-own')")
    public @Nullable ResponseEntity<Page<ClientRequestDTO>> findIndividualsByOrganisationPaged(@PathVariable(name = "organisationId", required = true) String organisationId, @RequestParam(name = "pageNumber", required = false) @Nullable Integer pageNumber, @RequestParam(name = "pageSize", required = false) @Nullable Integer pageSize) throws Exception;

    @GetMapping(
        value = "/my-organisation"
    )
    public @Nullable ResponseEntity<List<ClientRequestDTO>> findMyOrganisationRequests() throws Exception;

    @GetMapping(
        value = "/mine"
    )
    public @Nullable ResponseEntity<List<ClientRequestDTO>> findMyRequests() throws Exception;

    @GetMapping(
        value = "/by-org/{organisationId}/organisations"
    )
    @PreAuthorize(value = "hasAnyAuthority('SCOPE_client-requests:list', 'SCOPE_client-requests:list-own')")
    public @Nullable ResponseEntity<List<ClientRequestDTO>> findOrganisationsByOrganisation(@PathVariable(name = "organisationId", required = true) String organisationId) throws Exception;

    @GetMapping(
        value = "/by-org/{organisationId}/organisations/paged"
    )
    @PreAuthorize(value = "hasAnyAuthority('SCOPE_client-requests:list', 'SCOPE_client-requests:list-own')")
    public @Nullable ResponseEntity<Page<ClientRequestDTO>> findOrganisationsByOrganisationPaged(@PathVariable(name = "organisationId", required = true) String organisationId, @RequestParam(name = "pageNumber", required = false) @Nullable Integer pageNumber, @RequestParam(name = "pageSize", required = false) @Nullable Integer pageSize) throws Exception;

    @GetMapping(
        value = "/user-ready"
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_client-requests:view')")
    public ResponseEntity<ClientRequestDTO> findUserReadyRequests() throws Exception;

    @GetMapping(
        value = "/user-ready/paged"
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_client-requests:view')")
    public ResponseEntity<ClientRequestDTO> findUserReadyRequestsPaged(@PathVariable(name = "pageNumber", required = false) @Nullable Integer pageNumber, @PathVariable(name = "pageSize", required = false) @Nullable Integer pageSize) throws Exception;

    @GetMapping(
        
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_client-requests:list')")
    public @Nullable ResponseEntity<List<ClientRequestDTO>> getAll() throws Exception;

    @GetMapping(
        value = "/paged"
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_client-requests:list')")
    public @Nullable ResponseEntity<Page<ClientRequestDTO>> getAllPaged(@RequestParam(name = "pageNumber", required = false) @Nullable Integer pageNumber, @RequestParam(name = "pageSize", required = false) @Nullable Integer pageSize) throws Exception;

    @PostMapping(
        value = "/search/paged"
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_client-requests:list')")
    public @Nullable ResponseEntity<Page<ClientRequestDTO>> pagedSearch(@RequestBody(required = false) @Nullable @Valid SearchObject<ClientRequestSearchCriteria> criteria) throws Exception;

    @DeleteMapping(
        value = "/{id}"
    )
    @PreAuthorize(value = "hasAnyAuthority('SCOPE_client-requests:delete', 'SCOPE_client-requests:delete-own')")
    public ResponseEntity<Boolean> remove(@PathVariable(name = "id", required = true) String id) throws Exception;

    @PostMapping(
        
    )
    @PreAuthorize(value = "hasAnyAuthority('SCOPE_client-requests:edit', 'SCOPE_client-requests:edit-own')")
    public ResponseEntity<ClientRequestDTO> save(@RequestBody(required = true) @Valid ClientRequestDTO clientRequest) throws Exception;

    @PostMapping(
        value = "search"
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_client-requests:list')")
    public @Nullable ResponseEntity<List<ClientRequestDTO>> search(@RequestBody(required = false) @Nullable @Valid SearchObject<ClientRequestSearchCriteria> criteria) throws Exception;

    @GetMapping(
        value = "/{id}/status"
    )
    @PreAuthorize(value = "hasAnyAuthority('SCOPE_client-requests:review', 'SCOPE_client-requests:review-own')")
    public ResponseEntity<ClientRequestDTO> updateStatus(@PathVariable(name = "id", required = true) String id, @RequestParam(name = "status", required = true) ClientRequestStatus status) throws Exception;

    @PostMapping(
        value = "/{organisationId}/upload"
    )
    @PreAuthorize(value = "hasAnyAuthority('SCOPE_client-requests:import', 'SCOPE_client-requests:import-own')")
    public @Nullable ResponseEntity<Page<ClientRequestDTO>> uploadRequests(@RequestParam(name = "file", required = true) MultipartFile file, @PathVariable(name = "organisationId", required = false) @Nullable String organisationId, @RequestParam(name = "target", required = false) @Nullable TargetEntity target) throws Exception;
}