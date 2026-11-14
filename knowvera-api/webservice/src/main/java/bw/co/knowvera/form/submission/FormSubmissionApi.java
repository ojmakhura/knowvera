package bw.co.knowvera.form.submission;

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
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/form-submissions")
public interface FormSubmissionApi {

    @PostMapping(
    )
    @PreAuthorize(value = "hasAnyAuthority('SCOPE_form-submissions:edit', 'SCOPE_form-submissions:edit-own')")
    public ResponseEntity<FormSubmissionDTO> createSubmission(@RequestBody(required = true) @Valid FormAssignmentDTO assignment) throws Exception;

    @GetMapping(
        value = "/by-form/{formId}"
    )
    @PreAuthorize(value = "hasAnyAuthority('SCOPE_form-submissions:list', 'SCOPE_form-submissions:list-own')")
    public @Nullable ResponseEntity<List<FormSubmissionDTO>> findByForm(@PathVariable(name = "formId", required = true) String formId) throws Exception;

    @GetMapping(
        value = "/{id}"
    )
    @PreAuthorize(value = "hasAnyAuthority('SCOPE_form-submissions:view', 'SCOPE_form-submissions:view-own')")
    public ResponseEntity<FormSubmissionDTO> findById(@PathVariable(name = "id", required = true) String id) throws Exception;

    @GetMapping(
        value = "/by-record/{kycRecordId}"
    )
    @PreAuthorize(value = "hasAnyAuthority('SCOPE_form-submissions:view', 'SCOPE_form-submissions:view-own')")
    public @Nullable ResponseEntity<List<FormSubmissionDTO>> findByKycRecord(@PathVariable(name = "kycRecordId", required = true) String kycRecordId) throws Exception;

    @GetMapping(
        value = "/by-org/{organisationId}"
    )
    @PreAuthorize(value = "hasAnyAuthority('SCOPE_form-submissions:list', 'SCOPE_form-submissions:list-own')")
    public @Nullable ResponseEntity<List<FormSubmissionDTO>> findByOrganisation(@PathVariable(name = "organisationId", required = true) String organisationId) throws Exception;

    @GetMapping(
        value = "/mine"
    )
    public @Nullable ResponseEntity<List<FormSubmissionDTO>> findMySubmissions(@RequestParam(name = "status", required = false) @Nullable FormSubmissionStatus status) throws Exception;

    @DeleteMapping(
        value = "/{id}"
    )
    @PreAuthorize(value = "hasAnyAuthority('SCOPE_form-submissions:delete', 'SCOPE_form-submissions:delete-own')")
    public ResponseEntity<Boolean> remove(@PathVariable(name = "id", required = true) String id) throws Exception;

    @PostMapping(
        value = "/{id}/values"
    )
    @PreAuthorize(value = "hasAnyAuthority('SCOPE_form-submissions:edit', 'SCOPE_form-submissions:edit-own')")
    public ResponseEntity<FormSubmissionDTO> saveValues(@PathVariable(name = "id", required = true) String id, @RequestBody(required = true) List<FormFieldValueDTO> values) throws Exception;

    @PostMapping(
        value = "/search"
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_form-submissions:list')")
    public @Nullable ResponseEntity<List<FormSubmissionDTO>> search(@RequestBody(required = true) FormSubmissionSearchCriteria criteria) throws Exception;

    @PostMapping(
        value = "/{id}/submit"
    )
    @PreAuthorize(value = "hasAnyAuthority('SCOPE_form-submissions:submit', 'SCOPE_form-submissions:submit-own')")
    public ResponseEntity<FormSubmissionDTO> submit(@PathVariable(name = "id", required = true) String id) throws Exception;

    @PostMapping(
        value = "/{id}/status"
    )
    @PreAuthorize(value = "hasAnyAuthority('SCOPE_form-submissions:review', 'SCOPE_form-submissions:review-own')")
    public ResponseEntity<FormSubmissionDTO> updateStatus(@PathVariable(name = "id", required = true) String id, @RequestParam(name = "status", required = true) FormSubmissionStatus status, @RequestParam(name = "note", required = false) @Nullable String note) throws Exception;

}
