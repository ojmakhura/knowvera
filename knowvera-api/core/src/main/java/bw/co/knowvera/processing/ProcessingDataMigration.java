package bw.co.knowvera.processing;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Component;

/**
 * Copies what the former veritas-processor service held in its own database into this one, once:
 * <ol>
 * <li>the processing profiles (every version, with their steps, providers and options);</li>
 * <li>each data processor's prompts and field definitions onto the document type it served (by
 * DOCUMENT_TYPE.DATA_PROCESSOR_ID, else by the same code): a field is matched by name, ignoring case;
 * one the document type lacks is added (target type INDIVIDUAL, no matching rule: review it);</li>
 * <li>the job history (within app.processing.jobs.retention when set).</li>
 * </ol>
 * It runs when app.processing.migration.source-url names that database, at start-up before the
 * default profile is made (DefaultProfileInitializer) and on one node at a time (a PostgreSQL
 * advisory lock). Each part is recorded in PROCESSING_MIGRATION when
 * done and never runs again; a part that fails is logged and tried again at the next start. The
 * source is only read.
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ProcessingDataMigration implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(ProcessingDataMigration.class);
    private static final long LOCK_KEY = 55123487091234999L;
    static final String PROFILES = "profiles";
    static final String DOCUMENT_TYPES = "document-types";
    static final String JOBS = "jobs";

    private final JdbcTemplate target;
    private final String sourceUrl;
    private final String sourceUsername;
    private final String sourcePassword;
    private final Duration retention;

    public ProcessingDataMigration(JdbcTemplate target, ProcessingProperties properties,
            @Value("${app.processing.migration.source-url:}") String sourceUrl,
            @Value("${app.processing.migration.username:}") String sourceUsername,
            @Value("${app.processing.migration.password:}") String sourcePassword) {
        this.target = target;
        this.sourceUrl = sourceUrl;
        this.sourceUsername = sourceUsername;
        this.sourcePassword = sourcePassword;
        this.retention = properties.getJobs().getRetention();
    }

    @Override
    public void run(ApplicationArguments args) {
        migrate();
    }

    public void migrate() {
        if (sourceUrl == null || sourceUrl.isBlank()) {
            return;
        }
        try {
            Boolean locked = target.queryForObject("SELECT pg_try_advisory_lock(?)", Boolean.class, LOCK_KEY);
            if (!Boolean.TRUE.equals(locked)) {
                log.info("Processing data migration skipped: another instance is running it");
                return;
            }
        } catch (RuntimeException e) {
            log.warn("Processing data migration skipped: {}", e.getMessage());
            return;
        }
        try {
            run(new JdbcTemplate(source()));
        } finally {
            try {
                target.queryForObject("SELECT pg_advisory_unlock(?)", Boolean.class, LOCK_KEY);
            } catch (RuntimeException e) {
                log.warn("Could not release the processing data migration lock: {}", e.getMessage());
            }
        }
    }

    void run(JdbcTemplate source) {
        target.execute("CREATE TABLE IF NOT EXISTS processing_migration (part VARCHAR(50) PRIMARY KEY, "
                + "done_at TIMESTAMP WITH TIME ZONE NOT NULL, summary TEXT)");
        part(PROFILES, () -> profiles(source));
        part(DOCUMENT_TYPES, () -> documentTypes(source));
        part(JOBS, () -> jobs(source));
    }

    private void part(String part, java.util.function.Supplier<String> work) {
        Integer done = target.queryForObject("SELECT COUNT(*) FROM processing_migration WHERE part = ?",
                Integer.class, part);
        if (done != null && done > 0) {
            return;
        }
        try {
            String summary = work.get();
            target.update("INSERT INTO processing_migration (part, done_at, summary) VALUES (?, ?, ?)", part,
                    Timestamp.from(Instant.now()), summary);
            log.info("Processing data migration, {}: {}", part, summary);
        } catch (RuntimeException e) {
            log.error("Processing data migration, {}: failed, tried again at the next start: {}", part,
                    e.getMessage(), e);
        }
    }

    /** Every profile version this database does not have yet (same code and version), with its steps. */
    String profiles(JdbcTemplate source) {
        if (!tableExists(source, "veritas_profile")) {
            return "nothing to copy";
        }
        int profiles = 0;
        int skipped = 0;
        int steps = 0;
        for (Map<String, Object> profile : source.queryForList(
                "SELECT id, code, version, name, description, created_at, created_by FROM veritas_profile ORDER BY code, version")) {
            Integer present = target.queryForObject("SELECT COUNT(*) FROM processing_profile WHERE code = ? AND version = ?",
                    Integer.class, profile.get("code"), profile.get("version"));
            if (present != null && present > 0) {
                skipped++;
                continue;
            }
            target.update("INSERT INTO processing_profile (id, code, version, name, description, created_at, created_by) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?)", profile.get("id"), profile.get("code"), profile.get("version"),
                    profile.get("name"), profile.get("description"), profile.get("created_at"), profile.get("created_by"));
            Object profileId = profile.get("id");
            steps += copy(source, "SELECT id, profile_id, position, step_type, model, system_prompt, user_prompt "
                    + "FROM veritas_profile_step WHERE profile_id = ?",
                    "INSERT INTO processing_profile_step (id, profile_id, position, step_type, model, system_prompt, user_prompt) "
                            + "VALUES (?, ?, ?, ?, ?, ?, ?)", profileId);
            copy(source, "SELECT p.step_id, p.position, p.provider FROM veritas_profile_step_provider p "
                    + "JOIN veritas_profile_step s ON s.id = p.step_id WHERE s.profile_id = ?",
                    "INSERT INTO processing_profile_step_provider (step_id, position, provider) VALUES (?, ?, ?)", profileId);
            copy(source, "SELECT o.step_id, o.option_key, o.option_value FROM veritas_profile_step_option o "
                    + "JOIN veritas_profile_step s ON s.id = o.step_id WHERE s.profile_id = ?",
                    "INSERT INTO processing_profile_step_option (step_id, option_key, option_value) VALUES (?, ?, ?)", profileId);
            profiles++;
        }
        return profiles + " profile versions copied (" + steps + " steps), " + skipped + " already here";
    }

    /** Each data processor's prompts and field definitions onto its document type. */
    String documentTypes(JdbcTemplate source) {
        if (!tableExists(source, "data_processor")) {
            return "nothing to copy";
        }
        int types = 0;
        int updated = 0;
        int added = 0;
        StringBuilder unmatched = new StringBuilder();
        for (Map<String, Object> type : target.queryForList(
                "SELECT id, code, data_processor_id FROM document_type")) {
            UUID typeId = (UUID) type.get("id");
            String processorId = (String) type.get("data_processor_id");
            List<Map<String, Object>> processors = processorId != null && !processorId.isBlank()
                    ? source.queryForList("SELECT id, validation_prompts::text AS validation_prompts, "
                            + "text_extraction_prompts::text AS text_extraction_prompts FROM data_processor WHERE id = ?::uuid",
                            processorId.trim())
                    : source.queryForList("SELECT id, validation_prompts::text AS validation_prompts, "
                            + "text_extraction_prompts::text AS text_extraction_prompts FROM data_processor "
                            + "WHERE lower(code) = lower(?)", type.get("code"));
            if (processors.isEmpty()) {
                continue;
            }
            Map<String, Object> processor = processors.getFirst();
            types++;
            // Prompts set here since the merge win
            target.update("UPDATE document_type SET validation_prompts = ?::jsonb WHERE id = ? AND "
                    + "(validation_prompts IS NULL OR jsonb_array_length(validation_prompts) = 0) AND ?::jsonb IS NOT NULL",
                    processor.get("validation_prompts"), typeId, processor.get("validation_prompts"));
            target.update("UPDATE document_type SET text_extraction_prompts = ?::jsonb WHERE id = ? AND "
                    + "(text_extraction_prompts IS NULL OR jsonb_array_length(text_extraction_prompts) = 0) AND ?::jsonb IS NOT NULL",
                    processor.get("text_extraction_prompts"), typeId, processor.get("text_extraction_prompts"));

            for (Map<String, Object> field : source.queryForList("SELECT field, field_label, field_type, format, many, "
                    + "mandatory FROM expected_field WHERE data_processor_fk = ?", processor.get("id"))) {
                int rows = target.update("UPDATE expected_field SET field_label = ?, field_type = COALESCE(?, 'STRING'), "
                        + "format = ?, many = ?, mandatory = ? WHERE document_type_fk = ? AND lower(field) = lower(?)",
                        field.get("field_label"), field.get("field_type"), field.get("format"), field.get("many"),
                        field.get("mandatory"), typeId, field.get("field"));
                if (rows > 0) {
                    updated += rows;
                } else {
                    target.update("INSERT INTO expected_field (id, field, field_label, field_type, format, many, mandatory, "
                            + "target_type, exact_match, document_type_fk, created_at, created_by) "
                            + "VALUES (?, ?, ?, COALESCE(?, 'STRING'), ?, ?, ?, 'INDIVIDUAL', false, ?, ?, 'processing-migration')",
                            UUID.randomUUID(), field.get("field"), field.get("field_label"), field.get("field_type"),
                            field.get("format"), field.get("many"), field.get("mandatory"), typeId,
                            Timestamp.from(Instant.now()));
                    added++;
                    unmatched.append(unmatched.isEmpty() ? "" : ", ").append(type.get("code")).append('.')
                            .append(field.get("field"));
                }
            }
        }
        return types + " document types, " + updated + " field definitions copied, " + added
                + " fields added (review their target type and matching rule): " + unmatched;
    }

    /** The job history, unless this database has jobs of its own already. */
    String jobs(JdbcTemplate source) {
        if (count(target, "processing_job") > 0) {
            return "skipped: jobs exist here already";
        }
        if (!tableExists(source, "veritas_job")) {
            return "nothing to copy";
        }
        Timestamp since = retention == null ? new Timestamp(0) : Timestamp.from(Instant.now().minus(retention));
        int jobs = copy(source, "SELECT id, correlation_id, submitted_by, profile_code, profile_version, status, started_at, "
                + "finished_at, request_json, result_text, fields_json, classification_json, error FROM veritas_job "
                + "WHERE started_at >= ?",
                "INSERT INTO processing_job (id, correlation_id, submitted_by, profile_code, profile_version, status, "
                        + "started_at, finished_at, request_json, result_text, fields_json, classification_json, error) "
                        + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                since);
        int steps = copy(source, "SELECT s.job_id, s.position, s.step_type, s.status, s.provider, s.model, s.duration_ms, "
                + "s.failures FROM veritas_job_step s JOIN veritas_job j ON j.id = s.job_id WHERE j.started_at >= ?",
                "INSERT INTO processing_job_step (job_id, position, step_type, status, provider, model, duration_ms, failures) "
                        + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
                since);
        return jobs + " jobs, " + steps + " job steps";
    }

    private int copy(JdbcTemplate source, String select, String insert, Object... args) {
        List<Object[]> rows = source.query(select, (rs, n) -> {
            Object[] row = new Object[rs.getMetaData().getColumnCount()];
            for (int i = 0; i < row.length; i++) {
                row[i] = rs.getObject(i + 1);
            }
            return row;
        }, args);
        if (!rows.isEmpty()) {
            target.batchUpdate(insert, rows);
        }
        return rows.size();
    }

    private static int count(JdbcTemplate jdbc, String table) {
        Integer count = jdbc.queryForObject("SELECT COUNT(*) FROM " + table, Integer.class);
        return count == null ? 0 : count;
    }

    private static boolean tableExists(JdbcTemplate jdbc, String table) {
        Integer count = jdbc.queryForObject("SELECT COUNT(*) FROM information_schema.tables "
                + "WHERE table_schema = current_schema() AND table_name = ?", Integer.class, table);
        return count != null && count > 0;
    }

    private DriverManagerDataSource source() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource(sourceUrl, sourceUsername, sourcePassword);
        dataSource.setDriverClassName("org.postgresql.Driver");
        return dataSource;
    }
}
