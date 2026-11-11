package bw.co.knowvera.document;

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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

/**
 * 
 * TODO: Model Documentation for bw.co.knowvera.document.DocumentApi
 */
@RequestMapping("/documents")
public interface DocumentApi {

    @GetMapping(
        value = "/{id}/analysis"
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_documents:verify')")
    public ResponseEntity<DocumentDTO> analyseDocument(@PathVariable(name = "id", required = true) String id) throws Exception;

    @GetMapping(
        value = "/{id}/download"
    )
    @PreAuthorize(value = "hasAnyAuthority('SCOPE_documents:export', 'SCOPE_documents:export-own')")
    public ResponseEntity<InputStreamResource> downloadFile(@PathVariable(name = "id", required = true) String id) throws Exception;

    @GetMapping(
        value = "/download"
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_documents:export')")
    public ResponseEntity<InputStreamResource> downloadFileByUrl(@RequestParam(name = "objectName", required = true) String objectName) throws Exception;

    @GetMapping(
        value = "/documentTypeId/{documentTypeId}"
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_documents:list')")
    public @Nullable ResponseEntity<List<DocumentListDTO>> findByDocumentType(@PathVariable(name = "documentTypeId", required = true) String documentTypeId) throws Exception;

    @GetMapping(
        value = "/{id}"
    )
    @PreAuthorize(value = "hasAnyAuthority('SCOPE_documents:view', 'SCOPE_documents:view-own')")
    public ResponseEntity<DocumentDTO> findById(@PathVariable(name = "id", required = true) String id) throws Exception;

    @GetMapping(
        value = "/target/{target}/{targetId}"
    )
    @PreAuthorize(value = "hasAnyAuthority('SCOPE_documents:list', 'SCOPE_documents:list-own')")
    public @Nullable ResponseEntity<List<DocumentListDTO>> findByTarget(@PathVariable(name = "target", required = true) TargetEntity target, @PathVariable(name = "targetId", required = true) String targetId) throws Exception;

    @GetMapping(
        value = "/mine"
    )
    public @Nullable ResponseEntity<List<DocumentListDTO>> findMyDocuments(@RequestParam(name = "target", required = true) TargetEntity target) throws Exception;

    @GetMapping(
        value = "/mine/paged"
    )
    public ResponseEntity<Page<DocumentListDTO>> findMyDocumentsPaged(@RequestParam(name = "target", required = true) TargetEntity target, @RequestParam(name = "pageNumber", required = false) @Nullable Integer pageNumber, @RequestParam(name = "pageSize", required = false) @Nullable Integer pageSize) throws Exception;

    @GetMapping(
        
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_documents:list')")
    public @Nullable ResponseEntity<List<DocumentListDTO>> getAll() throws Exception;

    @GetMapping(
        value = "/paged"
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_documents:list')")
    public @Nullable ResponseEntity<Page<DocumentListDTO>> getAllPaged(@RequestParam(name = "pageNumber", required = false) @Nullable Integer pageNumber, @RequestParam(name = "pageSize", required = false) @Nullable Integer pageSize) throws Exception;

    @DeleteMapping(
        value = "/{id}"
    )
    @PreAuthorize(value = "hasAnyAuthority('SCOPE_documents:delete', 'SCOPE_documents:delete-own')")
    public ResponseEntity<Boolean> remove(@PathVariable(name = "id", required = true) String id) throws Exception;

    @PostMapping(
        
    )
    @PreAuthorize(value = "hasAnyAuthority('SCOPE_documents:edit', 'SCOPE_documents:edit-own')")
    public ResponseEntity<DocumentDTO> save(@RequestBody(required = true) @Valid DocumentDTO document) throws Exception;

    @PostMapping(
        value = "/search"
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_documents:list')")
    public @Nullable ResponseEntity<List<DocumentListDTO>> search(@RequestBody(required = true) @Valid SearchObject<DocumentSearchCriteria> criteria) throws Exception;

    @PostMapping(
        value = "/search/paged"
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_documents:list')")
    public ResponseEntity<Page<DocumentListDTO>> searchPaged(@RequestBody(required = true) @Valid SearchObject<DocumentSearchCriteria> criteria) throws Exception;

    @GetMapping(
        value = "/{id}/text-extraction"
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_documents:verify')")
    public ResponseEntity<DocumentDTO> textExtration(@PathVariable(name = "id", required = true) String id, @RequestParam(name = "block", required = false) @Nullable Boolean block) throws Exception;

    @PostMapping(
        value = "/{id}/update"
    )
    @PreAuthorize(value = "hasAnyAuthority('SCOPE_documents:edit', 'SCOPE_documents:edit-own')")
    public ResponseEntity<DocumentDTO> updateDocument(@PathVariable(name = "id", required = true) String id, @RequestPart(name = "file", required = true) MultipartFile file) throws Exception;

    @PutMapping(
        value = "/{id}/content"
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_documents:submit')")
    public ResponseEntity<DocumentDTO> updateFileContent(@PathVariable(name = "id", required = true) String id, @RequestBody(required = true) @Valid String content) throws Exception;

    @GetMapping(
        value = "/{id}/verification-status"
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_documents:review')")
    public ResponseEntity<DocumentDTO> updateVerificationStatus(@PathVariable(name = "id", required = true) String id, @RequestParam(name = "verificationStatus", required = true) DocumentVerificationStatus verificationStatus) throws Exception;

    @PostMapping(
        value = "/upload/{target}/{targetId}/type/{documentTypeId}"
    )
    @PreAuthorize(value = "hasAnyAuthority('SCOPE_documents:submit', 'SCOPE_documents:submit-own')")
    public ResponseEntity<DocumentDTO> upload(@PathVariable(name = "target", required = true) TargetEntity target, @PathVariable(name = "targetId", required = true) String targetId, @PathVariable(name = "documentTypeId", required = true) String documentTypeId, @RequestParam(name = "purpose", required = false) @Nullable String purpose, @RequestBody(required = true) @Valid MultipartFile file) throws Exception;

    @GetMapping(
        value = "/{id}/data-verification"
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_documents:verify')")
    public ResponseEntity<DocumentDTO> verifyData(@PathVariable(name = "id", required = true) String id) throws Exception;
}