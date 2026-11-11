package bw.co.knowvera.kyc.fields;

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
 * TODO: Model Documentation for bw.co.knowvera.kyc.fields.KycReportSectionApi
 */
@RequestMapping("/kyc-report-sections")
public interface KycReportSectionApi {

    @PostMapping(
        value = "/field-value"
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_kyc-report-sections:edit')")
    public ResponseEntity<KycReportSectionDTO> addFieldValue(@RequestBody(required = true) @Valid GroupFieldValueDTO fieldValue) throws Exception;

    @GetMapping(
        value = "/{id}"
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_kyc-report-sections:view')")
    public ResponseEntity<KycReportSectionDTO> findById(@PathVariable(name = "id", required = true) String id) throws Exception;

    @DeleteMapping(
        value = "/{id}"
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_kyc-report-sections:delete')")
    public ResponseEntity<Boolean> remove(@PathVariable(name = "id", required = true) String id) throws Exception;

    @DeleteMapping(
        value = "/{id}/field-value/{fieldValueId}"
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_kyc-report-sections:delete')")
    public ResponseEntity<KycReportSectionDTO> removeFieldValue(@PathVariable(name = "id", required = true) String id, @PathVariable(name = "fieldValueId", required = true) String fieldValueId) throws Exception;

    @PostMapping(
        
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_kyc-report-sections:edit')")
    public ResponseEntity<KycReportSectionDTO> save(@RequestBody(required = true) @Valid KycReportSectionDTO kycReportSection) throws Exception;
}