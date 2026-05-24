package bw.co.centralkyc.audit;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class AuditLogPartitionInitializer {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuditLogPartitionInitializer.class);

    private final JdbcTemplate jdbcTemplate;

    @Value("${app.audit.partition.months-history:12}")
    private int monthsHistory;

    @Value("${app.audit.partition.months-future:3}")
    private int monthsFuture;

    public AuditLogPartitionInitializer(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Scheduled(cron = "0 0 0 1 * *")
    public void createNextMonth() {
        createMonthPartitions(LocalDate.now().plusMonths(1), LocalDate.now().plusMonths(2));
    }

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void ensureMonthlyPartitions() {
        if (!isPostgres()) {
            return;
        }

        if (!tableExists("audit_log")) {
            LOGGER.debug("Skipping audit partition setup because audit_log does not exist yet.");
            return;
        }

        if (!isPartitioned()) {
            repartitionExistingTable();
        }

        createMonthPartitions(resolveStartMonth(null), resolveEndMonthExclusive(null));
        createMaintenanceIndexes();
    }

    private boolean isPostgres() {
        try {
            String version = jdbcTemplate.queryForObject("SELECT version()", String.class);
            return version != null && version.toLowerCase().contains("postgresql");
        } catch (Exception ex) {
            LOGGER.warn("Could not determine database engine for audit partition setup: {}", ex.getMessage());
            return false;
        }
    }

    private boolean tableExists(String tableName) {
        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = current_schema() AND table_name = ?",
                Integer.class,
                tableName);
        return count != null && count > 0;
    }

    private boolean isPartitioned() {
        Boolean partitioned = jdbcTemplate.queryForObject(
                """
                        SELECT EXISTS (
                          SELECT 1
                          FROM pg_partitioned_table pt
                          JOIN pg_class c ON c.oid = pt.partrelid
                          JOIN pg_namespace n ON n.oid = c.relnamespace
                          WHERE c.relname = 'audit_log' AND n.nspname = current_schema()
                        )
                        """,
                Boolean.class);
        return Boolean.TRUE.equals(partitioned);
    }

    private void repartitionExistingTable() {
        Instant minTimestamp = queryBoundaryInstant("SELECT MIN(\"timestamp\") FROM audit_log");
        Instant maxTimestamp = queryBoundaryInstant("SELECT MAX(\"timestamp\") FROM audit_log");

        LocalDate startMonth = resolveStartMonth(minTimestamp);
        LocalDate endMonthExclusive = resolveEndMonthExclusive(maxTimestamp);

        LOGGER.info("Repartitioning audit_log table by month from {} to {}", startMonth,
                endMonthExclusive.minusMonths(1));

        jdbcTemplate.execute("ALTER TABLE audit_log RENAME TO audit_log_unpartitioned");

        jdbcTemplate.execute(
                """
                        CREATE TABLE audit_log (
                            id uuid NOT NULL,
                            username varchar(255) NOT NULL,
                            \"timestamp\" timestamptz NOT NULL,
                            event varchar(255) NOT NULL,
                            ip_address varchar(255) NOT NULL,
                            agent varchar(255) NOT NULL,
                            event_label varchar(255),
                            log_data jsonb,
                            entity_type varchar(255) NOT NULL,
                            user_id varchar(255) NOT NULL,
                            trace_id varchar(255),
                            span_id varchar(255),
                            PRIMARY KEY (id, \"timestamp\")
                        ) PARTITION BY RANGE (\"timestamp\")
                        """);

        createMonthPartitions(startMonth, endMonthExclusive);
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS audit_log_default PARTITION OF audit_log DEFAULT");

        jdbcTemplate.execute(
                """
                        INSERT INTO audit_log (
                            id, username, \"timestamp\", event, ip_address, agent,
                            event_label, log_data, entity_type, user_id, trace_id, span_id
                        )
                        SELECT
                            id, username, \"timestamp\", event, ip_address, agent,
                            event_label, log_data, entity_type, user_id, trace_id, span_id
                        FROM audit_log_unpartitioned
                        """);

        jdbcTemplate.execute("DROP TABLE audit_log_unpartitioned");
    }

    private void createMonthPartitions(LocalDate startMonth, LocalDate endMonthExclusive) {
        LocalDate cursor = startMonth;
        while (cursor.isBefore(endMonthExclusive)) {
            LocalDate next = cursor.plusMonths(1);
            String partitionName = partitionName(cursor);
            String from = cursor.atStartOfDay().toInstant(ZoneOffset.UTC).toString();
            String to = next.atStartOfDay().toInstant(ZoneOffset.UTC).toString();

            String sql = """
                    CREATE TABLE IF NOT EXISTS %s
                    PARTITION OF audit_log
                    FOR VALUES FROM ('%s') TO ('%s')
                    """.formatted(partitionName, from, to);

            jdbcTemplate.execute(sql);

            cursor = next;
        }
    }

    private String partitionName(LocalDate monthStart) {
        return "audit_log_" + monthStart.getYear() + "_" + String.format("%02d", monthStart.getMonthValue());
    }

    private LocalDate resolveStartMonth(Instant dataMin) {
        LocalDate nowMonth = LocalDate.now(ZoneOffset.UTC).withDayOfMonth(1);
        LocalDate configuredStart = nowMonth.minusMonths(monthsHistory);
        if (dataMin == null) {
            return configuredStart;
        }
        LocalDate dataStart = dataMin.atZone(ZoneOffset.UTC).toLocalDate().withDayOfMonth(1);
        return dataStart.isBefore(configuredStart) ? dataStart : configuredStart;
    }

    private LocalDate resolveEndMonthExclusive(Instant dataMax) {
        LocalDate nowMonth = LocalDate.now(ZoneOffset.UTC).withDayOfMonth(1);
        LocalDate configuredEndExclusive = nowMonth.plusMonths(monthsFuture + 1L);
        if (dataMax == null) {
            return configuredEndExclusive;
        }
        LocalDate dataEndExclusive = dataMax.atZone(ZoneOffset.UTC).toLocalDate().withDayOfMonth(1).plusMonths(1);
        return dataEndExclusive.isAfter(configuredEndExclusive) ? dataEndExclusive : configuredEndExclusive;
    }

    private Instant queryBoundaryInstant(String sql) {
        return jdbcTemplate.query(sql, rs -> {
            if (!rs.next()) {
                return null;
            }
            Timestamp timestamp = rs.getTimestamp(1);
            return timestamp == null ? null : timestamp.toInstant();
        });
    }

    private void createMaintenanceIndexes() {
        jdbcTemplate.execute("CREATE INDEX IF NOT EXISTS idx_audit_log_timestamp ON audit_log (\"timestamp\" DESC)");
        jdbcTemplate.execute("CREATE INDEX IF NOT EXISTS idx_audit_log_entity_type ON audit_log (entity_type)");
        jdbcTemplate.execute("CREATE INDEX IF NOT EXISTS idx_audit_log_user_id ON audit_log (user_id)");
    }
}
