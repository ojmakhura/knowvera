package bw.co.knowvera.veritas;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.List;

import org.junit.jupiter.api.Test;

import bw.co.knowvera.TargetEntity;
import bw.co.knowvera.document.type.field.ExpectedFieldDTO;
import bw.co.knowvera.document.type.field.ExpectedFieldType;

class VeritasProcessorSyncTest {

    private static ExpectedFieldDTO field(String name, ExpectedFieldType type) {
        ExpectedFieldDTO field = new ExpectedFieldDTO();
        field.setField(name);
        field.setFieldType(type);
        field.setTargetType(TargetEntity.ORGANISATION);
        field.setMatchTo("registrationNumber");
        return field;
    }

    private static bw.co.roguesystems.ai.processor.field.ExpectedFieldDTO processorField(String id, String name) {
        bw.co.roguesystems.ai.processor.field.ExpectedFieldDTO field = new bw.co.roguesystems.ai.processor.field.ExpectedFieldDTO();
        field.setId(id);
        field.setField(name);
        return field;
    }

    @Test
    void aSavedFieldUpdatesTheProcessorsFieldOfItsNameAndANewOneIsAdded() {
        var toSave = VeritasProcessorSync.toSave(List.of(processorField("id-1", "idNumber")),
                List.of(field("IdNumber", ExpectedFieldType.NUMBER), field("dateOfBirth", ExpectedFieldType.DATE)),
                List.of());

        assertEquals(List.of("IdNumber", "dateOfBirth"), toSave.stream().map(f -> f.getField()).toList());
        assertEquals("id-1", toSave.getFirst().getId());
        assertNull(toSave.getLast().getId());
        assertEquals("NUMBER", toSave.getFirst().getFieldType().name());
    }

    @Test
    void anExpectedFieldTheProcessorLacksIsAddedAndTheOthersAreLeftAlone() {
        var toSave = VeritasProcessorSync.toSave(List.of(processorField("id-1", "idNumber")), List.of(),
                List.of(field("idNumber", ExpectedFieldType.STRING), field("surname", ExpectedFieldType.STRING)));

        assertEquals(List.of("surname"), toSave.stream().map(f -> f.getField()).toList());
    }

    @Test
    void onlyTheDefinitionGoesToTheProcessor() {
        var converted = VeritasProcessorSync.convert(field("uin", ExpectedFieldType.STRING));

        assertEquals("uin", converted.getField());
        assertEquals("STRING", converted.getFieldType().name());
        assertEquals(false, converted.getMany());
    }
}
