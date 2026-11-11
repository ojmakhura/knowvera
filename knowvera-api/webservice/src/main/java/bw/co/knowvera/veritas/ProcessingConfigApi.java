package bw.co.knowvera.veritas;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import bw.co.knowvera.document.type.DocumentTypeDTO;
import bw.co.knowvera.document.type.DocumentTypeService;
import bw.co.roguesystems.ai.profile.ProfileSummary;
import bw.co.roguesystems.ai.schema.ExtractionSchemaDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * What the document type screens show of veritas-processor's configuration: the processing
 * profiles a document type can use, and the schema that holds the fields extracted from its
 * documents.
 */
@RestController
@RequestMapping("/document-types")
@Tag(name = "Document Types", description = "Operations related to document types.")
public class ProcessingConfigApi {

    private final VeritasProfilesClient client;
    private final DocumentTypeService documentTypeService;

    public ProcessingConfigApi(VeritasProfilesClient client, DocumentTypeService documentTypeService) {
        this.client = client;
        this.documentTypeService = documentTypeService;
    }

    @GetMapping("/processing/profiles")
    @PreAuthorize("hasAuthority('SCOPE_document-types:list')")
    @Operation(summary = "Processing Profiles", description = "The processing profiles of veritas-processor")
    public ResponseEntity<List<ProfileSummary>> processingProfiles() {
        try {
            return ResponseEntity.ok(client.profiles());
        } catch (RuntimeException e) {
            throw unavailable(e);
        }
    }

    @GetMapping("/{id}/extraction-schema")
    @PreAuthorize("hasAuthority('SCOPE_document-types:view')")
    @Operation(summary = "Extraction Schema", description = "The veritas-processor schema that holds the fields "
            + "extracted from the document type's documents (no content when it has none yet)")
    public ResponseEntity<ExtractionSchemaDTO> extractionSchema(@PathVariable(name = "id") String id) {
        DocumentTypeDTO type = documentTypeService.findById(id);
        String code = type == null ? null : VeritasJobFactory.schemaCode(type);
        if (code == null) {
            return ResponseEntity.notFound().build();
        }
        ExtractionSchemaDTO schema;
        try {
            schema = client.schema(code);
        } catch (RuntimeException e) {
            throw unavailable(e);
        }
        return schema == null ? ResponseEntity.noContent().build() : ResponseEntity.ok(schema);
    }

    private static ResponseStatusException unavailable(RuntimeException e) {
        return new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
                "veritas-processor could not be reached: " + e.getMessage(), e);
    }
}
