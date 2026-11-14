package bw.co.knowvera.form;

import bw.co.knowvera.AuditTracker;
import bw.co.knowvera.auth.RequiresOwnership;
import bw.co.knowvera.logging.Audit;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Forms", description = "Custom forms an organisation designs for its clients to fill.")
public class FormApiImpl implements FormApi {

    private final FormService formService;

    public FormApiImpl(FormService formService) {
        this.formService = formService;
    }

    @Override
    @Operation(summary = "Find Form by ID", description = "A form with its sections and fields")
    @Audit(entity = "FORM", eventLabel = "#id", logData = false)
    @RequiresOwnership(scope = "forms:view", target = "FORM", id = "#id")
    public ResponseEntity<FormDTO> findById(String id) throws Exception {
        return ResponseEntity.ok(formService.findById(id));
    }

    @Override
    @Operation(summary = "Find Forms by Organisation", description = "The forms of an organisation")
    @Audit(entity = "FORM", eventLabel = "#organisationId", logData = false)
    @RequiresOwnership(scope = "forms:list", target = "ORGANISATION", id = "#organisationId")
    public ResponseEntity<List<FormDTO>> findByOrganisation(String organisationId) throws Exception {
        return ResponseEntity.ok(formService.findByOrganisation(organisationId));
    }

    @Override
    @Operation(summary = "Get All Forms", description = "The forms of every organisation")
    @Audit(entity = "FORM", logData = false)
    public ResponseEntity<List<FormDTO>> getAll() throws Exception {
        return ResponseEntity.ok(formService.getAll());
    }

    @Override
    @Operation(summary = "Remove Form", description = "Remove a form nobody has been asked to fill")
    @Audit(entity = "FORM", eventLabel = "#id", logData = false)
    @RequiresOwnership(scope = "forms:delete", target = "FORM", id = "#id")
    public ResponseEntity<Boolean> remove(String id) throws Exception {
        return ResponseEntity.ok(formService.remove(id));
    }

    @Override
    @Operation(summary = "Save Form", description = "Create a form, or update it with all its sections and fields. "
            + "Sections and fields with an id are updated, those without are added, stored ones left out are removed.")
    @Audit(entity = "FORM", eventLabel = "#form.id", logData = false)
    @RequiresOwnership(scope = "forms:edit", target = "ORGANISATION", id = "#form.organisationId", record = "FORM", recordId = "#form.id")
    public ResponseEntity<FormDTO> save(FormDTO form) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AuditTracker.auditTrail(form, authentication);
        return ResponseEntity.ok(formService.save(form));
    }
}
