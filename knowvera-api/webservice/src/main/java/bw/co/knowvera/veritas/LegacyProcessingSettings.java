package bw.co.knowvera.veritas;

import java.util.List;
import java.util.Map;

import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.json.JsonMapper;

/**
 * Reads what the removed in-process pipeline was configured with, straight from its tables (Hibernate
 * keeps columns it no longer maps): the settings' tool selectors and the document types' prompts.
 * Missing tables or columns (a database that never had the pipeline) read as nothing.
 */
@Component
public class LegacyProcessingSettings {

    private static final Logger log = LoggerFactory.getLogger(LegacyProcessingSettings.class);

    /** The tool selector join tables and their tool columns. */
    public enum Stage {
        TEXT_EXTRACTION("SETTINGS2TEXT_EXTRACTION_TOOLS", "TEXT_EXTRACTION_TOOLS_FK"),
        TEXT_CLEANUP("SETTINGS2TEXT_CLEANUP_TOOLS", "TEXT_CLEANUP_TOOLS_FK"),
        TEXT_PROCESSING("SETTINGS2TEXT_PROCESSING_TOOLS", "TEXT_PROCESSING_TOOLS_FK"),
        DOCUMENT_CONFIRMATION("DOCUMENT_CONFIRMATION_TOOLS2SETTINGS", "DOCUMENT_CONFIRMATION_TOOLS_FK");

        private final String joinTable;
        private final String toolColumn;

        Stage(String joinTable, String toolColumn) {
            this.joinTable = joinTable;
            this.toolColumn = toolColumn;
        }
    }

    /** A legacy tool (TESSERACT, OLLAMA, LM_STUDIO, GEMINI) and its position in the chain. */
    public record LegacyTool(String tool, @Nullable Integer position) {
    }

    /** A legacy prompt: role (system or user) and content. */
    public record LegacyPrompt(@Nullable String role, @Nullable String content) {
    }

    /** A document type with the prompts it had. */
    public record LegacyDocumentType(String id, @Nullable String code, @Nullable String name,
            List<LegacyPrompt> validationPrompts, List<LegacyPrompt> textExtractionPrompts) {
    }

    private final JdbcTemplate jdbc;
    private final JsonMapper jsonMapper;

    public LegacyProcessingSettings(JdbcTemplate jdbc, JsonMapper jsonMapper) {
        this.jdbc = jdbc;
        this.jsonMapper = jsonMapper;
    }

    /** The stage's tools, in chain order. */
    public List<LegacyTool> tools(Stage stage) {
        try {
            return jdbc.query("SELECT t.TOOL, t.POSITION FROM TOOL_SELECTOR t JOIN " + stage.joinTable + " j ON j."
                    + stage.toolColumn + " = t.ID ORDER BY t.POSITION",
                    (rs, row) -> new LegacyTool(rs.getString(1), (Integer) rs.getObject(2)));
        } catch (DataAccessException e) {
            log.info("No legacy {} tools: {}", stage, e.getMostSpecificCause().getMessage());
            return List.of();
        }
    }

    public List<LegacyDocumentType> documentTypes() {
        try {
            return jdbc.query("SELECT ID, CODE, NAME, CAST(VALIDATION_PROMPTS AS VARCHAR), "
                    + "CAST(TEXT_EXTRACTION_PROMPTS AS VARCHAR) FROM DOCUMENT_TYPE",
                    (rs, row) -> new LegacyDocumentType(rs.getString(1), rs.getString(2), rs.getString(3),
                            prompts(rs.getString(4)), prompts(rs.getString(5))));
        } catch (DataAccessException e) {
            log.info("No legacy document type prompts: {}", e.getMostSpecificCause().getMessage());
            return List.of();
        }
    }

    List<LegacyPrompt> prompts(@Nullable String json) {
        if (json == null || json.isBlank()) {
            return List.of();
        }
        List<Map<String, Object>> raw = jsonMapper.readValue(json, new TypeReference<List<Map<String, Object>>>() {
        });
        return raw.stream()
                .map(p -> new LegacyPrompt(p.get("role") == null ? null : String.valueOf(p.get("role")),
                        p.get("content") == null ? null : String.valueOf(p.get("content"))))
                .toList();
    }
}
