package bw.co.knowvera.settings.kyc;

import bw.co.knowvera.TargetEntity;
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

/**
 * 
 * TODO: Model Documentation for bw.co.knowvera.settings.kyc.KycFieldGroupApi
 */
@RequestMapping("/kyc-field-groups")
public interface KycFieldGroupApi {

    @GetMapping(
        value = "/{id}"
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_kyc-field-groups:view')")
    public ResponseEntity<KycFieldGroupDTO> findById(@PathVariable(name = "id", required = true) String id) throws Exception;

    @GetMapping(
        value = "/target/{targetType}"
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_kyc-field-groups:list')")
    public @Nullable ResponseEntity<List<KycFieldGroupDTO>> findByTarget(@PathVariable(name = "targetType", required = true) TargetEntity targetType) throws Exception;

    @DeleteMapping(
        value = "/{id}"
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_kyc-field-groups:delete')")
    public ResponseEntity<Boolean> remove(@PathVariable(name = "id", required = true) String id) throws Exception;

    @DeleteMapping(
        value = "/{id}/field/{fieldId}"
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_kyc-field-groups:delete')")
    public ResponseEntity<KycFieldGroupDTO> removeField(@PathVariable(name = "id", required = true) String id, @PathVariable(name = "fieldId", required = true) String fieldId) throws Exception;

    @PostMapping(
        
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_kyc-field-groups:edit')")
    public ResponseEntity<KycFieldGroupDTO> save(@RequestBody(required = true) @Valid KycFieldGroupDTO fieldGroup) throws Exception;
}