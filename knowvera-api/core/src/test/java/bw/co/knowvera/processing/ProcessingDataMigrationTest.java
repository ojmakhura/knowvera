package bw.co.knowvera.processing;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.startsWith;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;

class ProcessingDataMigrationTest {

    private final JdbcTemplate target = mock(JdbcTemplate.class);
    private final JdbcTemplate source = mock(JdbcTemplate.class);

    private ProcessingDataMigration migration(String sourceUrl) {
        return new ProcessingDataMigration(target, new ProcessingProperties(), sourceUrl, "user", "password");
    }

    private static Map<String, Object> row(Object... keysAndValues) {
        Map<String, Object> row = new HashMap<>();
        for (int i = 0; i < keysAndValues.length; i += 2) {
            row.put((String) keysAndValues[i], keysAndValues[i + 1]);
        }
        return row;
    }

    @Test
    void withoutASourceDatabaseNothingIsDone() {
        migration("").migrate();
        migration(null).migrate();

        verifyNoInteractions(target);
    }

    @Test
    void aPartDoneBeforeIsNotRunAgain() {
        when(target.queryForObject(startsWith("SELECT COUNT(*) FROM processing_migration"), eq(Integer.class), anyString()))
                .thenReturn(1);

        migration("jdbc:postgresql://db/veritas").run(source);

        verifyNoInteractions(source);
        verify(target, never()).update(startsWith("INSERT INTO processing_migration"), any(), any(), any());
    }

    @Test
    void aProcessorsFieldsGoOntoTheFieldsOfTheSameNameAndTheOthersAreAdded() {
        UUID typeId = UUID.randomUUID();
        UUID processorId = UUID.randomUUID();
        when(source.queryForObject(contains("information_schema.tables"), eq(Integer.class), eq("data_processor")))
                .thenReturn(1);
        when(target.queryForList("SELECT id, code, data_processor_id FROM document_type"))
                .thenReturn(List.of(row("id", typeId, "code", "NID", "data_processor_id", processorId.toString())));
        when(source.queryForList(contains("FROM data_processor WHERE id = ?::uuid"), eq(processorId.toString())))
                .thenReturn(List.of(row("id", processorId, "validation_prompts", "[{\"role\":\"system\",\"content\":\"x\"}]",
                        "text_extraction_prompts", null)));
        when(source.queryForList(contains("FROM expected_field WHERE data_processor_fk = ?"), eq(processorId)))
                .thenReturn(List.of(
                        row("field", "idNumber", "field_label", "ID number", "field_type", "STRING", "format", null,
                                "many", false, "mandatory", true),
                        row("field", "dateOfBirth", "field_label", "Date of birth", "field_type", "DATE",
                                "format", "yyyy-MM-dd", "many", false, "mandatory", false)));
        // idNumber exists on the document type, dateOfBirth does not
        when(target.update(startsWith("UPDATE expected_field"), any(), any(), any(), any(), any(), eq(typeId), eq("idNumber")))
                .thenReturn(1);

        String summary = migration("jdbc:postgresql://db/veritas").documentTypes(source);

        verify(target, times(1)).update(startsWith("INSERT INTO expected_field"), any(), eq("dateOfBirth"), any(), any(),
                any(), any(), any(), eq(typeId), any());
        verify(target).update(startsWith("UPDATE document_type SET validation_prompts"),
                eq("[{\"role\":\"system\",\"content\":\"x\"}]"), eq(typeId), any());
        assertThat(summary).contains("1 document types", "1 field definitions copied", "1 fields added", "NID.dateOfBirth");
    }

    @Test
    void aDocumentTypeWithoutALinkFindsItsProcessorByCode() {
        UUID typeId = UUID.randomUUID();
        when(source.queryForObject(contains("information_schema.tables"), eq(Integer.class), eq("data_processor")))
                .thenReturn(1);
        when(target.queryForList("SELECT id, code, data_processor_id FROM document_type"))
                .thenReturn(List.of(row("id", typeId, "code", "PAYSLIP", "data_processor_id", null)));

        String summary = migration("jdbc:postgresql://db/veritas").documentTypes(source);

        verify(source).queryForList(contains("WHERE lower(code) = lower(?)"), eq("PAYSLIP"));
        assertThat(summary).startsWith("0 document types");
    }

    @Test
    void aSourceWithoutTheTablesHasNothingToCopy() {
        when(source.queryForObject(contains("information_schema.tables"), eq(Integer.class), anyString())).thenReturn(0);

        ProcessingDataMigration migration = migration("jdbc:postgresql://db/veritas");

        assertThat(migration.profiles(source)).isEqualTo("nothing to copy");
        assertThat(migration.documentTypes(source)).isEqualTo("nothing to copy");
        assertThat(migration.jobs(source)).isEqualTo("nothing to copy");
    }
}
