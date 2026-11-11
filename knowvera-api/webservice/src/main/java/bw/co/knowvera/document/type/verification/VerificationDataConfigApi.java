package bw.co.knowvera.document.type.verification;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 
 * TODO: Model Documentation for bw.co.knowvera.document.type.verification.VerificationDataConfigApi
 */
@RequestMapping("/verification-data-configs")
public interface VerificationDataConfigApi {

    @GetMapping(
        value = "/{id}"
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_verification-data-configs:view')")
    public ResponseEntity<VerificationDataConfigDTO> findById(@PathVariable(name = "id", required = true) String id) throws Exception;

    @DeleteMapping(
        value = "/{id}"
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_verification-data-configs:delete')")
    public ResponseEntity<Boolean> remove(@PathVariable(name = "id", required = true) String id) throws Exception;

    @PostMapping(
        
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_verification-data-configs:edit')")
    public ResponseEntity<VerificationDataConfigDTO> save(@RequestBody(required = true) @Valid VerificationDataConfigDTO verificationDataConfig) throws Exception;
}