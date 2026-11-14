package bw.co.knowvera.suspicion;

import jakarta.validation.Valid;
import java.util.List;
import org.jspecify.annotations.Nullable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 
 * TODO: Model Documentation for bw.co.knowvera.suspicion.SuspicionCaseApi
 */
@RequestMapping("/suspicion-cases")
public interface SuspicionCaseApi {

    @PostMapping(
        value = "/{id}/notes"
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_suspicion-cases:edit')")
    public ResponseEntity<SuspicionCaseDTO> addNote(@RequestParam(name = "id", required = true) String id, @RequestParam(name = "note", required = true) String note) throws Exception;

    @PostMapping(
        value = "/{id}/close"
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_suspicion-cases:review')")
    public ResponseEntity<SuspicionCaseDTO> close(@RequestParam(name = "id", required = true) String id, @RequestParam(name = "outcome", required = true) String outcome) throws Exception;

    @GetMapping(
        value = "/{id}/draft-report"
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_suspicion-cases:export')")
    public ResponseEntity<String> draftReport(@PathVariable(name = "id", required = true) String id) throws Exception;

    @PostMapping(
        value = "/{id}/escalate"
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_suspicion-cases:review')")
    public ResponseEntity<SuspicionCaseDTO> escalate(@RequestParam(name = "id", required = true) String id) throws Exception;

    @GetMapping(
        value = "/active"
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_suspicion-cases:list')")
    public @Nullable ResponseEntity<List<SuspicionCaseDTO>> findActive() throws Exception;

    @GetMapping(
        value = "/{id}"
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_suspicion-cases:view')")
    public ResponseEntity<SuspicionCaseDTO> findById(@PathVariable(name = "id", required = true) String id) throws Exception;

    @GetMapping(
        value = "/by-record/{kycRecordId}"
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_suspicion-cases:list')")
    public @Nullable ResponseEntity<List<SuspicionCaseDTO>> findByRecord(@PathVariable(name = "kycRecordId", required = true) String kycRecordId) throws Exception;

    @PostMapping(
        
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_suspicion-cases:edit')")
    public ResponseEntity<SuspicionCaseDTO> raise(@RequestBody(required = true) @Valid SuspicionCaseDTO suspicionCase) throws Exception;

    @PostMapping(
        value = "/{id}/report"
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_suspicion-cases:review')")
    public ResponseEntity<SuspicionCaseDTO> recordReport(@RequestParam(name = "id", required = true) String id, @RequestParam(name = "fiaReference", required = true) String fiaReference) throws Exception;
}