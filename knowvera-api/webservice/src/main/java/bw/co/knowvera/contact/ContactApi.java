package bw.co.knowvera.contact;

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
 * TODO: Model Documentation for bw.co.knowvera.contact.ContactApi
 */
@RequestMapping("/contacts")
public interface ContactApi {

    @GetMapping(
        value = "/{id}"
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_contacts:view')")
    public ResponseEntity<ContactDTO> findById(@PathVariable(name = "id", required = true) String id) throws Exception;

    @GetMapping(
        value = "/by-type/{type}"
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_contacts:list')")
    public @Nullable ResponseEntity<List<ContactDTO>> findByType(@PathVariable(name = "type", required = true) ContactType type) throws Exception;

    @GetMapping(
        value = "/by-type/{type}/paged"
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_contacts:list')")
    public @Nullable ResponseEntity<Page<ContactDTO>> findByTypePaged(@PathVariable(name = "type", required = true) ContactType type, @RequestParam(name = "pageNumber", required = false) @Nullable Integer pageNumber, @RequestParam(name = "pageSize", required = false) @Nullable Integer pageSize) throws Exception;

    @GetMapping(
        
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_contacts:list')")
    public @Nullable ResponseEntity<List<ContactDTO>> getAll() throws Exception;

    @GetMapping(
        value = "/paged"
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_contacts:list')")
    public @Nullable ResponseEntity<Page<ContactDTO>> getAllPaged(@RequestParam(name = "pageNumber", required = false) @Nullable Integer pageNumber, @RequestParam(name = "pageSize", required = false) @Nullable Integer pageSize) throws Exception;

    @DeleteMapping(
        value = "/{id}"
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_contacts:delete')")
    public ResponseEntity<Boolean> remove(@PathVariable(name = "id", required = true) String id) throws Exception;

    @PostMapping(
        
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_contacts:edit')")
    public ResponseEntity<ContactDTO> save(@RequestBody(required = true) @Valid ContactDTO document) throws Exception;

    @GetMapping(
        value = "/search"
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_contacts:list')")
    public @Nullable ResponseEntity<List<ContactDTO>> search(@RequestParam(name = "criteria", required = false) @Nullable String criteria) throws Exception;
}