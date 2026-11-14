package bw.co.roguesystems.ai.processor.field;

import jakarta.validation.Valid;
import java.util.List;
import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.Page;
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
 * TODO: Model Documentation for bw.co.roguesystems.ai.processor.field.ExpectedFieldApi
 */
@RequestMapping("/expected-fields")
public interface ExpectedFieldApi {

    @PostMapping(
        value = "/data-processors"
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_expected-fields:list')")
    public @Nullable ResponseEntity<List<ExpectedFieldDTO>> findByDataProcessor(@RequestBody(required = true) @Valid List<String> dataProcessorIds) throws Exception;

    @PostMapping(
        value = "/data-processors/page"
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_expected-fields:list')")
    public ResponseEntity<Page<ExpectedFieldDTO>> findByDataProcessorPage(@RequestBody(required = true) @Valid List<String> dataProcessorIds, @RequestParam(name = "pageNumber", required = false) @Nullable Integer pageNumber, @RequestParam(name = "pageSize", required = false) @Nullable Integer pageSize) throws Exception;

    @GetMapping(
        value = "/{id}"
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_expected-fields:view')")
    public ResponseEntity<ExpectedFieldDTO> findById(@PathVariable(name = "id", required = true) String id) throws Exception;

    @DeleteMapping(
        value = "/{id}"
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_expected-fields:delete')")
    public ResponseEntity<Boolean> remove(@PathVariable(name = "id", required = true) String id) throws Exception;

    @PostMapping(
        
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_expected-fields:edit')")
    public ResponseEntity<ExpectedFieldDTO> save(@RequestBody(required = true) @Valid ExpectedFieldDTO expectedField) throws Exception;
}