package bw.co.knowvera.form.submission;

import bw.co.knowvera.kyc.KycRecordListDTO;
import bw.co.knowvera.auth.CurrentCaller;
import bw.co.knowvera.auth.KycAuthorisationService;
import bw.co.knowvera.auth.RequiresOwnership;
import bw.co.knowvera.logging.Audit;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Form Submissions", description = "The answers of clients to the custom forms of an organisation.")
public class FormSubmissionApiImpl implements FormSubmissionApi {

    private final FormSubmissionService formSubmissionService;
    private final CurrentCaller currentCaller;

    public FormSubmissionApiImpl(FormSubmissionService formSubmissionService, CurrentCaller currentCaller) {
        this.formSubmissionService = formSubmissionService;
        this.currentCaller = currentCaller;
    }

    @Override
    @Operation(summary = "Ask a client to fill a form", description = "Adds the form to the client's KYC record, where the client fills it. "
            + "The form must be published and meant for that kind of client.")
    @Audit(entity = "FORM_SUBMISSION", eventLabel = "#assignment.formId", logData = false)
    @RequiresOwnership(scope = "form-submissions:edit", target = KycAuthorisationService.FORM, id = "#assignment.formId")
    public ResponseEntity<FormSubmissionDTO> createSubmission(FormAssignmentDTO assignment) throws Exception {
        return ResponseEntity.ok(formSubmissionService.createSubmission(assignment, caller()));
    }

    @Override
    @Operation(summary = "Find Submissions by Form", description = "The submissions of a form, without their answers")
    @Audit(entity = "FORM_SUBMISSION", eventLabel = "#formId", logData = false)
    @RequiresOwnership(scope = "form-submissions:list", target = KycAuthorisationService.FORM, id = "#formId")
    public ResponseEntity<List<FormSubmissionDTO>> findByForm(String formId) throws Exception {
        return ResponseEntity.ok(formSubmissionService.findByForm(formId));
    }

    @Override
    @Operation(summary = "Find Submission by ID", description = "A submission with its answers and the definition of its form")
    @Audit(entity = "FORM_SUBMISSION", eventLabel = "#id", logData = false)
    @RequiresOwnership(scope = "form-submissions:view", target = KycAuthorisationService.FORM_SUBMISSION, id = "#id")
    public ResponseEntity<FormSubmissionDTO> findById(String id) throws Exception {
        return ResponseEntity.ok(formSubmissionService.findById(id));
    }

    @Override
    @Operation(summary = "Forms of a KYC record", description = "The forms that are part of a KYC record, without their answers. "
            + "The forms the record's client has to fill and does not have yet are added first: "
            + "every published form of the organisations onboarding the client")
    @Audit(entity = "FORM_SUBMISSION", eventLabel = "#kycRecordId", logData = false)
    @RequiresOwnership(scope = "form-submissions:view", target = "KYC_RECORD", id = "#kycRecordId")
    public ResponseEntity<List<FormSubmissionDTO>> findByKycRecord(String kycRecordId) throws Exception {
        return ResponseEntity.ok(formSubmissionService.findRequired(kycRecordId, caller()));
    }

    @Override
    @Operation(summary = "Find Submissions by Organisation", description = "The submissions of the forms an organisation issued, without their answers")
    @Audit(entity = "FORM_SUBMISSION", eventLabel = "#organisationId", logData = false)
    @RequiresOwnership(scope = "form-submissions:list", target = "ORGANISATION", id = "#organisationId")
    public ResponseEntity<List<FormSubmissionDTO>> findByOrganisation(String organisationId) throws Exception {
        return ResponseEntity.ok(formSubmissionService.findByOrganisation(organisationId));
    }

    @Override
    @Operation(summary = "My forms to fill", description = "The forms on the KYC records of the caller and of the caller's organisation: "
            + "those they were asked to fill and every published form of the organisations onboarding them")
    @Audit(entity = "FORM_SUBMISSION", eventLabel = "'mine'", logData = false)
    public ResponseEntity<List<FormSubmissionDTO>> findMySubmissions(FormSubmissionStatus status) throws Exception {
        List<FormSubmissionDTO> submissions = new ArrayList<>();
        for (KycRecordListDTO record : currentCaller.kycRecords()) {
            // Also adds to the record the forms its organisations require and it does not have yet
            formSubmissionService.findRequired(record.id(), caller()).stream()
                    .filter(submission -> status == null || submission.getStatus() == status)
                    .forEach(submissions::add);
        }
        return ResponseEntity.ok(submissions);
    }

    @Override
    @Operation(summary = "Remove Submission", description = "Withdraw a form a client was asked to fill, with its answers")
    @Audit(entity = "FORM_SUBMISSION", eventLabel = "#id", logData = false)
    @RequiresOwnership(scope = "form-submissions:delete", target = KycAuthorisationService.FORM_SUBMISSION_ISSUER, id = "#id")
    public ResponseEntity<Boolean> remove(String id) throws Exception {
        return ResponseEntity.ok(formSubmissionService.remove(id));
    }

    @Override
    @Operation(summary = "Save answers", description = "Saves answers without submitting them. Only the given fields change; an empty answer clears the field.")
    @Audit(entity = "FORM_SUBMISSION", eventLabel = "#id", logData = false)
    @RequiresOwnership(scope = "form-submissions:edit", target = KycAuthorisationService.FORM_SUBMISSION, id = "#id")
    public ResponseEntity<FormSubmissionDTO> saveValues(String id, List<FormFieldValueDTO> values) throws Exception {
        return ResponseEntity.ok(formSubmissionService.saveValues(id, values, caller()));
    }

    @Override
    @Operation(summary = "Search Submissions", description = "Submissions by form, organisation, client and status, without their answers")
    @Audit(entity = "FORM_SUBMISSION", logData = false)
    public ResponseEntity<List<FormSubmissionDTO>> search(FormSubmissionSearchCriteria criteria) throws Exception {
        return ResponseEntity.ok(formSubmissionService.search(criteria));
    }

    @Override
    @Operation(summary = "Submit answers", description = "Submits the answers to the organisation. Every required field must be answered.")
    @Audit(entity = "FORM_SUBMISSION", eventLabel = "#id", logData = false)
    @RequiresOwnership(scope = "form-submissions:submit", target = KycAuthorisationService.FORM_SUBMISSION, id = "#id")
    public ResponseEntity<FormSubmissionDTO> submit(String id) throws Exception {
        return ResponseEntity.ok(formSubmissionService.submit(id, caller()));
    }

    @Override
    @Operation(summary = "Accept or return a submission", description = "The organisation's decision on submitted answers: ACCEPTED, or RETURNED with a note on what to correct.")
    @Audit(entity = "FORM_SUBMISSION", eventLabel = "#id", logData = false)
    @RequiresOwnership(scope = "form-submissions:review", target = KycAuthorisationService.FORM_SUBMISSION_ISSUER, id = "#id")
    public ResponseEntity<FormSubmissionDTO> updateStatus(String id, FormSubmissionStatus status, String note) throws Exception {
        return ResponseEntity.ok(formSubmissionService.updateStatus(id, status, note, caller()));
    }

    private static String caller() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication == null ? "anonymousUser" : authentication.getName();
    }
}
