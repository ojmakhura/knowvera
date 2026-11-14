package bw.co.knowvera.kyc;

import bw.co.knowvera.SearchObject;
import bw.co.knowvera.TargetEntity;
import bw.co.knowvera.document.DocumentDTO;
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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

/**
 * 
 * TODO: Model Documentation for bw.co.knowvera.kyc.KycRecordApi
 */
@RequestMapping("/kyc-records")
public interface KycRecordApi {

    @GetMapping(
        value = "/individual/{individualId}"
    )
    @PreAuthorize(value = "hasAnyAuthority('SCOPE_kyc-records:submit', 'SCOPE_kyc-records:submit-own')")
    public ResponseEntity<KycRecordDTO> createIndividualRecord(@PathVariable(name = "individualId", required = true) String individualId) throws Exception;

    @PostMapping(
        value = "/new"        , consumes = "multipart/form-data"
    )
    @PreAuthorize(value = "hasAnyAuthority('SCOPE_kyc-records:submit', 'SCOPE_kyc-records:submit-own')")
    public ResponseEntity<KycRecordDTO> createNew(@RequestPart(name = "record", required = true) KycRecordDTO record, @RequestPart(name = "files", required = false) @Nullable List<MultipartFile> files) throws Exception;

    @GetMapping(
        value = "/organisation/{organisationId}"
    )
    @PreAuthorize(value = "hasAnyAuthority('SCOPE_kyc-records:submit', 'SCOPE_kyc-records:submit-own')")
    public ResponseEntity<KycRecordDTO> createOrganisationRecord(@PathVariable(name = "organisationId", required = true) String organisationId) throws Exception;

    @GetMapping(
        value = "/{id}"
    )
    @PreAuthorize(value = "hasAnyAuthority('SCOPE_kyc-records:view', 'SCOPE_kyc-records:view-own')")
    public ResponseEntity<KycRecordDTO> findById(@PathVariable(name = "id", required = true) String id) throws Exception;

    @GetMapping(
        value = "/by-identity/{identityNo}"
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_kyc-records:list')")
    public @Nullable ResponseEntity<List<KycRecordListDTO>> findByIdentityNo(@PathVariable(name = "identityNo", required = true) String identityNo) throws Exception;

    @GetMapping(
        value = "/by-identity/{identityNo}/paged"
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_kyc-records:list')")
    public @Nullable ResponseEntity<Page<KycRecordListDTO>> findByIdentityNoPaged(@PathVariable(name = "identityNo", required = true) String identityNo, @RequestParam(name = "pageNumber", required = false) @Nullable Integer pageNumber, @RequestParam(name = "pageSize", required = false) @Nullable Integer pageSize) throws Exception;

    @GetMapping(
        value = "/by-individual/{individualId}"
    )
    @PreAuthorize(value = "hasAnyAuthority('SCOPE_kyc-records:list', 'SCOPE_kyc-records:list-own')")
    public @Nullable ResponseEntity<List<KycRecordListDTO>> findByIndividual(@PathVariable(name = "individualId", required = true) String individualId) throws Exception;

    @GetMapping(
        value = "/by-individual/{individualId}/paged"
    )
    @PreAuthorize(value = "hasAnyAuthority('SCOPE_kyc-records:list', 'SCOPE_kyc-records:list-own')")
    public @Nullable ResponseEntity<Page<KycRecordListDTO>> findByIndividualPaged(@PathVariable(name = "individualId", required = true) String individualId, @RequestParam(name = "pageNumber", required = false) @Nullable Integer pageNumber, @RequestParam(name = "pageSize", required = false) @Nullable Integer pageSize) throws Exception;

    @GetMapping(
        value = "/by-organisation/{organisationId}"
    )
    @PreAuthorize(value = "hasAnyAuthority('SCOPE_kyc-records:list', 'SCOPE_kyc-records:list-own')")
    public @Nullable ResponseEntity<List<KycRecordListDTO>> findByOrganisation(@PathVariable(name = "organisationId", required = true) String organisationId) throws Exception;

    @GetMapping(
        value = "/by-organisation-registration/{registrationNo}"
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_kyc-records:list')")
    public @Nullable ResponseEntity<List<KycRecordListDTO>> findByOrganisationRegistration(@PathVariable(name = "registrationNo", required = true) String registrationNo) throws Exception;

    @GetMapping(
        value = "/by-organisation_registration/{registrationNo}/paged"
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_kyc-records:list')")
    public @Nullable ResponseEntity<Page<KycRecordListDTO>> findByOrganisationRegistrationPaged(@PathVariable(name = "registrationNo", required = true) String registrationNo, @RequestParam(name = "pageNumber", required = false) @Nullable Integer pageNumber, @RequestParam(name = "pageSize", required = false) @Nullable Integer pageSize) throws Exception;

    @GetMapping(
        value = "/client-summary-kyc/{organisationId}/individuals"
    )
    @PreAuthorize(value = "hasAnyAuthority('SCOPE_kyc-records:list', 'SCOPE_kyc-records:list-own')")
    public @Nullable ResponseEntity<List<KycRecordSummary>> findClientIndividualSummaries(@PathVariable(name = "organisationId", required = false) @Nullable String organisationId) throws Exception;

    @GetMapping(
        value = "/client-summary-kyc/{organisationId}/individuals/page"
    )
    @PreAuthorize(value = "hasAnyAuthority('SCOPE_kyc-records:list', 'SCOPE_kyc-records:list-own')")
    public ResponseEntity<Page<KycRecordSummary>> findClientIndividualSummariesPaged(@PathVariable(name = "organisationId", required = false) @Nullable String organisationId, @RequestParam(name = "pageNumber", required = false) @Nullable Integer pageNumber, @RequestParam(name = "pageSize", required = false) @Nullable Integer pageSize) throws Exception;

    @GetMapping(
        value = "/client-summary-kyc/{organisationId}/organisations"
    )
    @PreAuthorize(value = "hasAnyAuthority('SCOPE_kyc-records:list', 'SCOPE_kyc-records:list-own')")
    public @Nullable ResponseEntity<List<KycRecordSummary>> findClientOrganisationSummaries(@PathVariable(name = "organisationId", required = false) @Nullable String organisationId) throws Exception;

    @GetMapping(
        value = "/client-summary-kyc/{organisationId}/organisations/page"
    )
    @PreAuthorize(value = "hasAnyAuthority('SCOPE_kyc-records:list', 'SCOPE_kyc-records:list-own')")
    public ResponseEntity<Page<KycRecordSummary>> findClientOrganisationSummariesPage(@PathVariable(name = "organisationId", required = false) @Nullable String organisationId, @RequestParam(name = "pageNumber", required = false) @Nullable Integer pageNumber, @RequestParam(name = "pageSize", required = false) @Nullable Integer pageSize) throws Exception;

    @GetMapping(
        value = "/my_current"
    )
    public ResponseEntity<KycRecordDTO> findMyCurrentRecord(@RequestParam(name = "ownerType", required = true) TargetEntity ownerType) throws Exception;

    @GetMapping(
        value = "/mine"
    )
    public @Nullable ResponseEntity<List<KycRecordListDTO>> findMyRecords() throws Exception;

    @GetMapping(
        value = "/mine/paged"
    )
    public ResponseEntity<Page<KycRecordListDTO>> findMyRecordsPaged(@RequestParam(name = "pageNumber", required = false) @Nullable Integer pageNumber, @RequestParam(name = "pageSize", required = false) @Nullable Integer pageSize) throws Exception;

    @GetMapping(
        value = "/{id}/summary"
    )
    @PreAuthorize(value = "hasAnyAuthority('SCOPE_kyc-records:view', 'SCOPE_kyc-records:view-own')")
    public ResponseEntity<KycRecordSummary> findSummaryById(@PathVariable(name = "id", required = true) String id, @RequestParam(name = "organisationId", required = false) @Nullable String organisationId) throws Exception;

    @GetMapping(
        value = "/{id}/report"
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_kyc-records:export')")
    public ResponseEntity<KycRecordDTO> generateKycReport(@PathVariable(name = "id", required = true) String id) throws Exception;

    @GetMapping(
        
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_kyc-records:list')")
    public @Nullable ResponseEntity<List<KycRecordListDTO>> getAll() throws Exception;

    @GetMapping(
        value = "/paged"
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_kyc-records:list')")
    public @Nullable ResponseEntity<Page<KycRecordListDTO>> getAllPaged(@RequestParam(name = "pageNumber", required = false) @Nullable Integer pageNumber, @RequestParam(name = "pageSize", required = false) @Nullable Integer pageSize) throws Exception;

    @PostMapping(
        value = "/search/paged"
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_kyc-records:list')")
    public @Nullable ResponseEntity<Page<KycRecordListDTO>> pagedSearch(@RequestBody(required = false) @Nullable @Valid SearchObject<KycRecordSearchCriteria> criteria) throws Exception;

    @PostMapping(
        value = "/search/summaries/paged"
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_kyc-records:list')")
    public @Nullable ResponseEntity<Page<KycRecordSummary>> pagedSearchSummaries(@RequestBody(required = false) @Nullable @Valid SearchObject<KycRecordSearchCriteria> criteria) throws Exception;

    @DeleteMapping(
        value = "/{id}"
    )
    @PreAuthorize(value = "hasAnyAuthority('SCOPE_kyc-records:delete', 'SCOPE_kyc-records:delete-own')")
    public ResponseEntity<Boolean> remove(@PathVariable(name = "id", required = true) String id) throws Exception;

    @DeleteMapping(
        value = "/{id}/remove-file/{documentId}"
    )
    @PreAuthorize(value = "hasAnyAuthority('SCOPE_kyc-records:submit', 'SCOPE_kyc-records:submit-own')")
    public ResponseEntity<KycRecordDTO> removeRecordFile(@PathVariable(name = "id", required = true) String id, @PathVariable(name = "documentId", required = true) String documentId) throws Exception;

    @GetMapping(
        value = "/{id}/verification"
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_kyc-records:verify')")
    public ResponseEntity<KycRecordDTO> runVerification(@PathVariable(name = "id", required = true) String id) throws Exception;

    @PostMapping(
        
    )
    @PreAuthorize(value = "hasAnyAuthority('SCOPE_kyc-records:edit', 'SCOPE_kyc-records:edit-own')")
    public ResponseEntity<KycRecordDTO> save(@RequestBody(required = true) @Valid KycRecordDTO kycRecord) throws Exception;

    @PostMapping(
        value = "/search"
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_kyc-records:list')")
    public @Nullable ResponseEntity<List<KycRecordListDTO>> search(@RequestBody(required = false) @Nullable @Valid KycRecordSearchCriteria criteria) throws Exception;

    @PostMapping(
        value = "/search/summaries"
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_kyc-records:list')")
    public @Nullable ResponseEntity<List<KycRecordSummary>> searchSummaries(@RequestBody(required = false) @Nullable @Valid KycRecordSearchCriteria criteria) throws Exception;

    @PostMapping(
        value = "/{id}/update-files"        , consumes = "multipart/form-data"
    )
    @PreAuthorize(value = "hasAnyAuthority('SCOPE_kyc-records:submit', 'SCOPE_kyc-records:submit-own')")
    public ResponseEntity<KycRecordDTO> updateRecordFiles(@PathVariable(name = "id", required = true) String id, @RequestPart(name = "documents", required = true) List<DocumentDTO> documents, @RequestPart(name = "files", required = true) List<MultipartFile> files) throws Exception;

    @GetMapping(
        value = "/{id}/status"
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_kyc-records:review')")
    public ResponseEntity<KycRecordDTO> updateStatus(@PathVariable(name = "id", required = true) String id, @RequestParam(name = "kycStatus", required = true) KycComplianceStatus kycStatus) throws Exception;
}