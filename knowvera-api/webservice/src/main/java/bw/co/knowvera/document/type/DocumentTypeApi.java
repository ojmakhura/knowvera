package bw.co.knowvera.document.type;

import bw.co.knowvera.document.type.field.ExpectedFieldDTO;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Set;
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
 * TODO: Model Documentation for bw.co.knowvera.document.type.DocumentTypeApi
 */
@RequestMapping("/document-types")
public interface DocumentTypeApi {

    @PostMapping(
        value = "{id}/expected-field"
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_document-types:edit')")
    public ResponseEntity<DocumentTypeDTO> addExpectedField(@PathVariable(name = "id", required = true) String id, @RequestBody(required = true) @Valid Set<ExpectedFieldDTO> expectedFields) throws Exception;

    @GetMapping(
        value = "/{id}"
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_document-types:view')")
    public ResponseEntity<DocumentTypeDTO> findById(@PathVariable(name = "id", required = true) String id) throws Exception;

    @GetMapping(
        
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_document-types:list')")
    public @Nullable ResponseEntity<List<DocumentTypeDTO>> getAll() throws Exception;

    @GetMapping(
        value = "/paged"
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_document-types:list')")
    public @Nullable ResponseEntity<Page<DocumentTypeDTO>> getAllPaged(@RequestParam(name = "pageNumber", required = false) @Nullable Integer pageNumber, @RequestParam(name = "pageSize", required = false) @Nullable Integer pageSize) throws Exception;

    @GetMapping(
        value = "/search/paged"
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_document-types:list')")
    public @Nullable ResponseEntity<Page<DocumentTypeDTO>> pagedSearch(@RequestParam(name = "criteria", required = false) @Nullable String criteria, @RequestParam(name = "pageNumber", required = false) @Nullable Integer pageNumber, @RequestParam(name = "pageSize", required = false) @Nullable Integer pageSize) throws Exception;

    @DeleteMapping(
        value = "/{id}"
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_document-types:delete')")
    public ResponseEntity<Boolean> remove(@PathVariable(name = "id", required = true) String id) throws Exception;

    @PostMapping(
        
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_document-types:edit')")
    public ResponseEntity<DocumentTypeDTO> save(@RequestBody(required = true) @Valid DocumentTypeDTO documentType) throws Exception;

    @GetMapping(
        value = "/search"
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_document-types:list')")
    public @Nullable ResponseEntity<List<DocumentTypeDTO>> search(@RequestParam(name = "criteria", required = false) @Nullable String criteria) throws Exception;
}