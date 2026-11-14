package bw.co.knowvera.individual.employment;

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
 * TODO: Model Documentation for bw.co.knowvera.individual.employment.EmploymentRecordApi
 */
@RequestMapping("/employment-records")
public interface EmploymentRecordApi {

    @GetMapping(
        value = "/{id}"
    )
    @PreAuthorize(value = "hasAnyAuthority('SCOPE_employment-records:view', 'SCOPE_employment-records:view-own')")
    public ResponseEntity<EmploymentRecordDTO> findById(@PathVariable(name = "id", required = true) String id) throws Exception;

    @GetMapping(
        value = "/by-individual/{individualId}"
    )
    @PreAuthorize(value = "hasAnyAuthority('SCOPE_employment-records:list', 'SCOPE_employment-records:list-own')")
    public @Nullable ResponseEntity<List<EmploymentRecordDTO>> findByIndividual(@PathVariable(name = "individualId", required = true) String individualId) throws Exception;

    @GetMapping(
        
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_employment-records:list')")
    public @Nullable ResponseEntity<List<EmploymentRecordDTO>> getAll() throws Exception;

    @GetMapping(
        value = "/paged"
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_employment-records:list')")
    public @Nullable ResponseEntity<Page<EmploymentRecordDTO>> getAllPaged(@RequestParam(name = "pageNumber", required = false) @Nullable Integer pageNumber, @RequestParam(name = "pageSize", required = false) @Nullable Integer pageSize) throws Exception;

    @GetMapping(
        value = "/search/paged"
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_employment-records:list')")
    public @Nullable ResponseEntity<Page<EmploymentRecordDTO>> pagedSearch(@RequestParam(name = "criteria", required = false) @Nullable String criteria, @RequestParam(name = "pageNumber", required = false) @Nullable Integer pageNumber, @RequestParam(name = "pageSize", required = false) @Nullable Integer pageSize) throws Exception;

    @DeleteMapping(
        value = "/{id}"
    )
    @PreAuthorize(value = "hasAnyAuthority('SCOPE_employment-records:delete', 'SCOPE_employment-records:delete-own')")
    public ResponseEntity<Boolean> remove(@PathVariable(name = "id", required = true) String id) throws Exception;

    @PostMapping(
        
    )
    @PreAuthorize(value = "hasAnyAuthority('SCOPE_employment-records:edit', 'SCOPE_employment-records:edit-own')")
    public ResponseEntity<EmploymentRecordDTO> save(@RequestBody(required = true) @Valid EmploymentRecordDTO employmentRecord) throws Exception;

    @GetMapping(
        value = "/search"
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_employment-records:list')")
    public @Nullable ResponseEntity<List<EmploymentRecordDTO>> search(@RequestParam(name = "criteria", required = false) @Nullable String criteria) throws Exception;
}