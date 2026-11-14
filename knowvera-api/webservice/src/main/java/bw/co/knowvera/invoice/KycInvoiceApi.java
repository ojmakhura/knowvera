package bw.co.knowvera.invoice;

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
import org.springframework.web.multipart.MultipartFile;

/**
 * 
 * TODO: Model Documentation for bw.co.knowvera.invoice.KycInvoiceApi
 */
@RequestMapping("/invoices")
public interface KycInvoiceApi {

    @GetMapping(
        value = "/{id}"
    )
    @PreAuthorize(value = "hasAnyAuthority('SCOPE_invoices:view', 'SCOPE_invoices:view-own')")
    public ResponseEntity<KycInvoiceDTO> findById(@PathVariable(name = "id", required = true) String id) throws Exception;

    @GetMapping(
        value = "/by-organisation/{organisationId}"
    )
    @PreAuthorize(value = "hasAnyAuthority('SCOPE_invoices:list', 'SCOPE_invoices:list-own')")
    public @Nullable ResponseEntity<List<KycInvoiceDTO>> findByOrganisation(@PathVariable(name = "organisationId", required = true) String organisationId) throws Exception;

    @GetMapping(
        value = "/by-organisation/{organisationId}/paged"
    )
    @PreAuthorize(value = "hasAnyAuthority('SCOPE_invoices:list', 'SCOPE_invoices:list-own')")
    public @Nullable ResponseEntity<Page<KycInvoiceDTO>> findByOrganisationPaged(@PathVariable(name = "organisationId", required = true) String organisationId, @RequestParam(name = "pageNumber", required = false) @Nullable Integer pageNumber, @RequestParam(name = "pageSize", required = false) @Nullable Integer pageSize) throws Exception;

    @GetMapping(
        value = "/by-subscription/{subscriptionId}"
    )
    @PreAuthorize(value = "hasAnyAuthority('SCOPE_invoices:list', 'SCOPE_invoices:list-own')")
    public @Nullable ResponseEntity<List<KycInvoiceDTO>> findBySubscription(@PathVariable(name = "subscriptionId", required = true) String subscriptionId) throws Exception;

    @GetMapping(
        value = "/by-subscription/{subscriptionId}/paged"
    )
    @PreAuthorize(value = "hasAnyAuthority('SCOPE_invoices:list', 'SCOPE_invoices:list-own')")
    public @Nullable ResponseEntity<Page<KycInvoiceDTO>> findBySubscriptionPaged(@PathVariable(name = "subscriptionId", required = true) String subscriptionId, @RequestParam(name = "pageNumber", required = false) @Nullable Integer pageNumber, @RequestParam(name = "pageSize", required = false) @Nullable Integer pageSize) throws Exception;

    @GetMapping(
        value = "/generate/{subscriptionId}"
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_invoices:edit')")
    public @Nullable ResponseEntity<List<KycInvoiceDTO>> generateInvoice(@PathVariable(name = "subscriptionId", required = true) String subscriptionId) throws Exception;

    @GetMapping(
        
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_invoices:list')")
    public @Nullable ResponseEntity<List<KycInvoiceDTO>> getAll() throws Exception;

    @GetMapping(
        value = "/paged"
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_invoices:list')")
    public @Nullable ResponseEntity<Page<KycInvoiceDTO>> getAllPaged(@RequestParam(name = "pageNumber", required = false) @Nullable Integer pageNumber, @RequestParam(name = "pageSize", required = false) @Nullable Integer pageSize) throws Exception;

    @PostMapping(
        value = "/search/paged"
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_invoices:list')")
    public @Nullable ResponseEntity<Page<KycInvoiceDTO>> pagedSearch(@RequestBody(required = false) @Nullable @Valid SearchObject<InvoiceSearchCriteria> criteria) throws Exception;

    @DeleteMapping(
        value = "/{id}"
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_invoices:delete')")
    public ResponseEntity<Boolean> remove(@PathVariable(name = "id", required = true) String id) throws Exception;

    @PostMapping(
        
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_invoices:edit')")
    public ResponseEntity<KycInvoiceDTO> save(@RequestBody(required = true) @Valid KycInvoiceDTO invoice) throws Exception;

    @PostMapping(
        value = "/search"
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_invoices:list')")
    public @Nullable ResponseEntity<List<KycInvoiceDTO>> search(@RequestBody(required = false) @Nullable @Valid SearchObject<InvoiceSearchCriteria> criteria) throws Exception;

    @PostMapping(
        value = "/{id}/upload"
    )
    @PreAuthorize(value = "hasAnyAuthority('SCOPE_invoices:submit', 'SCOPE_invoices:submit-own')")
    public ResponseEntity<KycInvoiceDTO> upload(@PathVariable(name = "id", required = true) String id, @RequestParam(name = "purpose", required = true) UploadPurpose purpose, @RequestParam(name = "file", required = true) MultipartFile file) throws Exception;
}