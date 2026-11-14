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
import bw.co.roguesystems.ai.processor.DataProcessorDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * What the document type screens show of veritas-processor's configuration: the processing
 * profiles a document type can use, and the data processor it is linked to.
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

    @GetMapping("/processing/data-processors")
    @PreAuthorize("hasAuthority('SCOPE_document-types:list')")
    @Operation(summary = "Data Processors", description = "The data processors of veritas-processor a document "
            + "type can be linked to")
    public ResponseEntity<List<DataProcessorDTO>> dataProcessors() {
        try {
            return ResponseEntity.ok(client.processors());
        } catch (RuntimeException e) {
            throw unavailable(e);
        }
    }

    @GetMapping("/{id}/data-processor")
    @PreAuthorize("hasAuthority('SCOPE_document-types:view')")
    @Operation(summary = "Data Processor", description = "The veritas-processor data processor the document type "
            + "is linked to: what is done with its documents and the fields expected in them (no content when "
            + "it does not exist yet)")
    public ResponseEntity<DataProcessorDTO> dataProcessor(@PathVariable(name = "id") String id) {
        DocumentTypeDTO type = documentTypeService.findById(id);
        if (type == null) {
            return ResponseEntity.notFound().build();
        }
        String processorId = VeritasJobFactory.processorId(type);
        DataProcessorDTO processor;
        try {
            processor = processorId == null ? null : client.processor(processorId);
        } catch (RuntimeException e) {
            throw unavailable(e);
        }
        return processor == null ? ResponseEntity.noContent().build() : ResponseEntity.ok(processor);
    }

    private static ResponseStatusException unavailable(RuntimeException e) {
        return new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
                "veritas-processor could not be reached: " + e.getMessage(), e);
    }
}
