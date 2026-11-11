package bw.co.roguesystems.ai.schema;

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

/**
 * Schemas: named sets of fields to extract from text.
 */
@RestController
@RequestMapping("/schemas")
public class SchemaController {

    private final ExtractionSchemaService schemas;

    public SchemaController(ExtractionSchemaService schemas) {
        this.schemas = schemas;
    }

    @GetMapping
    @Operation(summary = "Schemas", description = "The latest version of every schema")
    public List<SchemaSummary> list() {
        return schemas.summaries();
    }

    @GetMapping("/{code}")
    @Operation(summary = "Schema", description = "The latest version of a schema")
    public ExtractionSchemaDTO latest(@PathVariable String code) {
        return schemas.findLatest(code);
    }

    @GetMapping("/{code}/versions")
    @Operation(summary = "Schema versions", description = "Every version of a schema, newest first")
    public List<ExtractionSchemaDTO> versions(@PathVariable String code) {
        return schemas.versions(code);
    }

    @GetMapping("/{code}/versions/{version}")
    @Operation(summary = "Schema version", description = "One version of a schema")
    public ExtractionSchemaDTO version(@PathVariable String code, @PathVariable int version) {
        return schemas.findVersion(code, version);
    }

    @PutMapping("/{code}")
    @Operation(summary = "Save schema", description = "Saves the schema as its next version (or returns the "
            + "latest version unchanged when it already holds these fields)")
    public ExtractionSchemaDTO save(@PathVariable String code, @RequestBody ExtractionSchemaDTO schema,
            Authentication authentication) {
        return schemas.save(code, schema, Callers.name(authentication));
    }
}
