package bw.co.knowvera.form;

import jakarta.validation.Valid;
import java.util.List;
import org.jspecify.annotations.Nullable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/forms")
public interface FormApi {

    @GetMapping(
        value = "/{id}"
    )
    @PreAuthorize(value = "hasAnyAuthority('SCOPE_forms:view', 'SCOPE_forms:view-own')")
    public ResponseEntity<FormDTO> findById(@PathVariable(name = "id", required = true) String id) throws Exception;

    @GetMapping(
        value = "/by-org/{organisationId}"
    )
    @PreAuthorize(value = "hasAnyAuthority('SCOPE_forms:list', 'SCOPE_forms:list-own')")
    public @Nullable ResponseEntity<List<FormDTO>> findByOrganisation(@PathVariable(name = "organisationId", required = true) String organisationId) throws Exception;

    @GetMapping(
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_forms:list')")
    public @Nullable ResponseEntity<List<FormDTO>> getAll() throws Exception;

    @DeleteMapping(
        value = "/{id}"
    )
    @PreAuthorize(value = "hasAnyAuthority('SCOPE_forms:delete', 'SCOPE_forms:delete-own')")
    public ResponseEntity<Boolean> remove(@PathVariable(name = "id", required = true) String id) throws Exception;

    @PostMapping(
    )
    @PreAuthorize(value = "hasAnyAuthority('SCOPE_forms:edit', 'SCOPE_forms:edit-own')")
    public ResponseEntity<FormDTO> save(@RequestBody(required = true) @Valid FormDTO form) throws Exception;

}
