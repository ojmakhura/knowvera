package bw.co.knowvera.processing;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import bw.co.knowvera.processing.profile.ProcessingProfileService;
import bw.co.knowvera.processing.profile.ProfileSummary;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * What the document type screens show of the processing configuration: the processing profiles a
 * document type can use.
 */
@RestController
@RequestMapping("/document-types")
@Tag(name = "Document Types", description = "Operations related to document types.")
public class ProcessingConfigApi {

    private final ProcessingProfileService profiles;

    public ProcessingConfigApi(ProcessingProfileService profiles) {
        this.profiles = profiles;
    }

    @GetMapping("/processing/profiles")
    @PreAuthorize("hasAuthority('SCOPE_document-types:list')")
    @Operation(summary = "Processing Profiles", description = "The processing profiles a document type can use "
            + "(the latest version of each)")
    public ResponseEntity<List<ProfileSummary>> processingProfiles() {
        return ResponseEntity.ok(profiles.summaries());
    }
}
