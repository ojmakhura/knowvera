package bw.co.knowvera.veritas;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import bw.co.roguesystems.ai.schema.FieldSpec;
import bw.co.roguesystems.ai.schema.FieldType;

class VeritasSchemaSyncTest {

    private static FieldSpec field(String name, FieldType type) {
        return new FieldSpec(name, null, type, null, false, false);
    }

    private static List<String> names(List<FieldSpec> fields) {
        return fields.stream().map(FieldSpec::name).toList();
    }

    @Test
    void savedFieldsReplaceTheSchemasFieldsOfTheirNameAndNewOnesAreAdded() {
        List<FieldSpec> merged = VeritasSchemaSync.merge(
                List.of(field("idNumber", FieldType.STRING), field("surname", FieldType.STRING)),
                List.of(field("IdNumber", FieldType.NUMBER), field("dateOfBirth", FieldType.DATE)),
                Set.of(), Set.of());

        assertEquals(List.of("IdNumber", "surname", "dateOfBirth"), names(merged));
        assertEquals(FieldType.NUMBER, merged.getFirst().type());
    }

    @Test
    void aRemovedOrRenamedExpectedFieldLeavesTheSchemaButFieldsConfiguredInVeritasStay() {
        List<FieldSpec> merged = VeritasSchemaSync.merge(
                List.of(field("idNumber", FieldType.STRING), field("gender", FieldType.STRING),
                        field("onlyInVeritas", FieldType.STRING)),
                List.of(field("idNo", FieldType.STRING)),
                Set.of("idnumber", "gender"), Set.of("idno"));

        assertEquals(List.of("onlyInVeritas", "idNo"), names(merged));
    }

    @Test
    void anExpectedFieldTheSchemaLacksBecomesATextField() {
        List<FieldSpec> merged = VeritasSchemaSync.merge(List.of(field("a", FieldType.DATE)), List.of(), Set.of(),
                Set.of("a", "b"));

        assertEquals(List.of("a", "b"), names(merged));
        assertEquals(FieldType.DATE, merged.getFirst().type());
        assertEquals(FieldType.STRING, merged.getLast().type());
    }

    @Test
    void aFieldSavedAgainIsNotRemoved() {
        assertEquals(List.of("a"), names(VeritasSchemaSync.merge(List.of(field("a", FieldType.STRING)),
                List.of(field("a", FieldType.NUMBER)), Set.of("a"), Set.of("a"))));
    }
}
