package bw.co.knowvera.sequence;

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

/**
 * 
 * TODO: Model Documentation for bw.co.knowvera.sequence.SequenceGeneratorApi
 */
@RequestMapping("/sequences")
public interface SequenceGeneratorApi {

    @GetMapping(
        value = "/{id}"
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_sequences:view')")
    public ResponseEntity<SequenceGeneratorDTO> findById(@PathVariable(name = "id", required = true) String id) throws Exception;

    @GetMapping(
        value = "/by-name"
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_sequences:view')")
    public ResponseEntity<SequenceGeneratorDTO> findByName(@RequestParam(name = "name", required = true) String name) throws Exception;

    @GetMapping(
        
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_sequences:list')")
    public @Nullable ResponseEntity<List<SequenceGeneratorDTO>> getAll() throws Exception;

    @DeleteMapping(
        value = "/{id}"
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_sequences:delete')")
    public ResponseEntity<Boolean> remove(@PathVariable(name = "id", required = true) String id) throws Exception;

    @PostMapping(
        
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_sequences:edit')")
    public ResponseEntity<SequenceGeneratorDTO> save(@RequestBody(required = true) @Valid SequenceGeneratorDTO sequenceGenerator) throws Exception;

    @GetMapping(
        value = "/search"
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_sequences:list')")
    public @Nullable ResponseEntity<List<SequenceGeneratorDTO>> search(@RequestParam(name = "criteria", required = false) @Nullable String criteria) throws Exception;
}