package bw.co.knowvera.processing.profile;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * The processing profiles: the steps, providers, models and prompts that process documents. A save
 * adds a version; versions are never changed or removed.
 */
@RequestMapping("/processing-profiles")
public interface ProcessingProfileApi {

    @GetMapping
    @PreAuthorize("hasAuthority('SCOPE_processing-profiles:list')")
    public ResponseEntity<List<ProfileSummary>> findAll();

    @GetMapping("/{code}")
    @PreAuthorize("hasAuthority('SCOPE_processing-profiles:view')")
    public ResponseEntity<ProcessingProfileDTO> findLatest(@PathVariable(name = "code") String code);

    @GetMapping("/{code}/versions")
    @PreAuthorize("hasAuthority('SCOPE_processing-profiles:list')")
    public ResponseEntity<List<ProcessingProfileDTO>> findVersions(@PathVariable(name = "code") String code);

    @GetMapping("/{code}/versions/{version}")
    @PreAuthorize("hasAuthority('SCOPE_processing-profiles:view')")
    public ResponseEntity<ProcessingProfileDTO> findVersion(@PathVariable(name = "code") String code,
            @PathVariable(name = "version") int version);

    @PutMapping("/{code}")
    @PreAuthorize("hasAuthority('SCOPE_processing-profiles:edit')")
    public ResponseEntity<ProcessingProfileDTO> save(@PathVariable(name = "code") String code,
            @RequestBody ProcessingProfileDTO profile);
}
