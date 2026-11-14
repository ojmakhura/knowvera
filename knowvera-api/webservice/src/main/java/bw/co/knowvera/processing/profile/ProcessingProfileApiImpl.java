package bw.co.knowvera.processing.profile;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;

import bw.co.knowvera.logging.Audit;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Processing Profiles", description = "The profiles that process documents")
public class ProcessingProfileApiImpl implements ProcessingProfileApi {

    private final ProcessingProfileService profiles;

    public ProcessingProfileApiImpl(ProcessingProfileService profiles) {
        this.profiles = profiles;
    }

    @Override
    @Operation(summary = "Profiles", description = "The latest version of every profile")
    public ResponseEntity<List<ProfileSummary>> findAll() {
        return ResponseEntity.ok(profiles.summaries());
    }

    @Override
    @Operation(summary = "Profile", description = "The latest version of a profile")
    public ResponseEntity<ProcessingProfileDTO> findLatest(String code) {
        return ResponseEntity.ok(profiles.findLatest(code));
    }

    @Override
    @Operation(summary = "Profile versions", description = "Every version of a profile, newest first")
    public ResponseEntity<List<ProcessingProfileDTO>> findVersions(String code) {
        return ResponseEntity.ok(profiles.versions(code));
    }

    @Override
    @Operation(summary = "Profile version", description = "One version of a profile")
    public ResponseEntity<ProcessingProfileDTO> findVersion(String code, int version) {
        return ResponseEntity.ok(profiles.findVersion(code, version));
    }

    @Override
    @Audit(entity = "PROCESSING_PROFILE", eventLabel = "#code", logData = false)
    @Operation(summary = "Save profile", description = "Saves the profile as its next version (or returns the "
            + "latest version unchanged when it already holds these steps)")
    public ResponseEntity<ProcessingProfileDTO> save(String code, ProcessingProfileDTO profile) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String user = authentication == null ? "anonymousUser" : authentication.getName();
        return ResponseEntity.ok(profiles.save(code, profile, user));
    }
}
