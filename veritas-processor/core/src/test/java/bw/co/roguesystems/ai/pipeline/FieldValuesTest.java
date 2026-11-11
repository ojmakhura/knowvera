package bw.co.roguesystems.ai.pipeline;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import bw.co.roguesystems.ai.schema.FieldSpec;
import bw.co.roguesystems.ai.schema.FieldType;

class FieldValuesTest {

    private static FieldSpec field(String name, FieldType type, String format, boolean many) {
        return new FieldSpec(name, null, type, format, many, false);
    }

    @Test
    void shapesEveryValueAsItsFieldDeclares() {
        Map<String, Object> extracted = new HashMap<>();
        extracted.put("salary", "P 12,500.75");
        extracted.put("deductions", "1 250,50");
        extracted.put("permanent", "Yes");
        extracted.put("dateOfBirth", "14/03/1990");
        extracted.put("issued", "2 March 2021");
        extracted.put("expiry", "2030-01-31");
        extracted.put("names", "KAGO");
        extracted.put("surname", "  MOYO ");
        extracted.put("extra", "kept");

        Map<String, Object> values = FieldValues.normalise(extracted, List.of(
                field("salary", FieldType.NUMBER, null, false),
                field("deductions", FieldType.NUMBER, null, false),
                field("permanent", FieldType.BOOLEAN, null, false),
                field("dateOfBirth", FieldType.DATE, null, false),
                field("issued", FieldType.DATE, "YYYY-MM-DD", false),
                field("expiry", FieldType.DATE, null, false),
                field("names", FieldType.STRING, null, true),
                field("surname", FieldType.STRING, null, false),
                field("employer", FieldType.STRING, null, false)));

        assertThat(values).containsEntry("salary", new BigDecimal("12500.75"))
                .containsEntry("deductions", new BigDecimal("1250.50"))
                .containsEntry("permanent", true)
                .containsEntry("dateOfBirth", "1990-03-14")
                .containsEntry("issued", "2021-03-02")
                .containsEntry("expiry", "2030-01-31")
                .containsEntry("names", List.of("KAGO"))
                .containsEntry("surname", "MOYO")
                .containsEntry("employer", null)
                .containsEntry("extra", "kept");
        assertThat(values.keySet()).startsWith("salary", "deductions");
    }

    @Test
    void theLastSeparatorOfANumberIsTheDecimalOne() {
        Map<String, Object> extracted = new HashMap<>();
        extracted.put("a", "1.250,50");
        extracted.put("b", "1,250.50");

        Map<String, Object> values = FieldValues.normalise(extracted,
                List.of(field("a", FieldType.NUMBER, null, false), field("b", FieldType.NUMBER, null, false)));

        assertThat(values).containsEntry("a", new java.math.BigDecimal("1250.50"))
                .containsEntry("b", new java.math.BigDecimal("1250.50"));
    }

    @Test
    void keepsWhatItCannotConvert() {
        Map<String, Object> values = FieldValues.normalise(
                Map.of("amount", "unknown", "flag", "maybe", "date", "sometime", "custom", "14/03/1990"),
                List.of(field("amount", FieldType.NUMBER, null, false), field("flag", FieldType.BOOLEAN, null, false),
                        field("date", FieldType.DATE, null, false), field("custom", FieldType.DATE, "DD/MM/YYYY", false)));

        assertThat(values).containsEntry("amount", "unknown").containsEntry("flag", "maybe")
                .containsEntry("date", "sometime").containsEntry("custom", "14/03/1990");
    }

    @Test
    void nullAndBlankAreNull() {
        Map<String, Object> extracted = new HashMap<>();
        extracted.put("a", "null");
        extracted.put("b", " ");
        assertThat(FieldValues.normalise(extracted, List.of(field("a", FieldType.NUMBER, null, true),
                field("b", FieldType.STRING, null, false)))).containsEntry("a", null).containsEntry("b", null);
    }

    @Test
    void describesFieldsForTheModel() {
        assertThat(FieldValues.describe(new FieldSpec("n", "Net pay", FieldType.NUMBER, null, false, true)))
                .isEqualTo("Net pay (number)");
        assertThat(FieldValues.describe(new FieldSpec("d", null, FieldType.DATE, "DD/MM/YYYY", true, false)))
                .isEqualTo("list of DD/MM/YYYY");
        assertThat(FieldValues.describe(FieldSpec.text("x"))).isEqualTo("text");
    }
}
