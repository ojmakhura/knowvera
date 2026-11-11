package bw.co.knowvera.screening;

import java.util.List;
import org.jspecify.annotations.Nullable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 
 * TODO: Model Documentation for bw.co.knowvera.screening.ScreeningApi
 */
@RequestMapping("/screening")
public interface ScreeningApi {

    @GetMapping(
        value = "/records/{kycRecordId}/hits"
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_screening:list')")
    public @Nullable ResponseEntity<List<ScreeningHitDTO>> findHitsByRecord(@PathVariable(name = "kycRecordId", required = true) String kycRecordId) throws Exception;

    @GetMapping(
        value = "/hits/pending"
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_screening:list')")
    public @Nullable ResponseEntity<List<ScreeningHitDTO>> findPendingHits() throws Exception;

    @GetMapping(
        value = "/lists"
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_screening:list')")
    public @Nullable ResponseEntity<List<ScreeningListStatus>> getListStatus() throws Exception;

    @PostMapping(
        value = "/hits/{id}/review"
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_screening:review')")
    public ResponseEntity<ScreeningHitDTO> reviewHit(@RequestParam(name = "id", required = true) String id, @RequestParam(name = "status", required = true) ScreeningHitStatus status, @RequestParam(name = "notes", required = true) String notes) throws Exception;

    @PostMapping(
        value = "/records/{kycRecordId}/screen"
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_screening:verify')")
    public @Nullable ResponseEntity<List<ScreeningHitDTO>> screenRecord(@RequestParam(name = "kycRecordId", required = true) String kycRecordId) throws Exception;

    @PostMapping(
        value = "/lists/update"
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_screening:edit')")
    public ResponseEntity<ScreeningListStatus> updateSanctionsList() throws Exception;
}