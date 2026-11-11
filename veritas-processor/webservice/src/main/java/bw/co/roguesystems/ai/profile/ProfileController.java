package bw.co.roguesystems.ai.profile;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import bw.co.roguesystems.ai.web.Callers;
import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/profiles")
public class ProfileController {

    private final ProcessingProfileService profiles;

    public ProfileController(ProcessingProfileService profiles) {
        this.profiles = profiles;
    }

    @GetMapping
    @Operation(summary = "Profiles", description = "The latest version of every profile")
    public List<ProfileSummary> list() {
        return profiles.summaries();
    }

    @GetMapping("/{code}")
    @Operation(summary = "Profile", description = "The latest version of a profile")
    public ProcessingProfileDTO latest(@PathVariable String code) {
        return profiles.findLatest(code);
    }

    @GetMapping("/{code}/versions")
    @Operation(summary = "Profile versions", description = "Every version of a profile, newest first")
    public List<ProcessingProfileDTO> versions(@PathVariable String code) {
        return profiles.versions(code);
    }

    @GetMapping("/{code}/versions/{version}")
    @Operation(summary = "Profile version", description = "One version of a profile")
    public ProcessingProfileDTO version(@PathVariable String code, @PathVariable int version) {
        return profiles.findVersion(code, version);
    }

    @PutMapping("/{code}")
    @Operation(summary = "Save profile", description = "Saves the profile as its next version (or returns the "
            + "latest version unchanged when it already holds these steps)")
    public ProcessingProfileDTO save(@PathVariable String code, @RequestBody ProcessingProfileDTO profile,
            Authentication authentication) {
        return profiles.save(code, profile, Callers.name(authentication));
    }
}
