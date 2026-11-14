package bw.co.knowvera.form.submission;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import bw.co.knowvera.form.field.FormField;
import bw.co.knowvera.form.field.FormFieldType;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;

class FormValueValidatorTest {

    private static FormField field(FormFieldType type, boolean required) {
        FormField field = new FormField();
        field.setLabel("Field");
        field.setFieldType(type);
        field.setRequired(required);
        return field;
    }

    private static boolean ok(FormField field, String value) {
        return FormValueValidator.validate(field, value, null, true).isEmpty();
    }

    @Test
    void aRequiredFieldMayBeEmptyOnlyInADraft() {
        FormField name = field(FormFieldType.TEXT, true);
        assertFalse(ok(name, " "));
        assertTrue(FormValueValidator.validate(name, " ", null, false).isEmpty());
        assertTrue(ok(field(FormFieldType.TEXT, false), null));
    }

    @Test
    void textIsCheckedAgainstItsLengthAndPattern() {
        FormField omang = field(FormFieldType.TEXT, true);
        omang.setMinLength(9);
        omang.setMaxLength(9);
        omang.setPattern("[0-9]+");
        assertTrue(ok(omang, "123456789"));
        assertFalse(ok(omang, "12345678"));
        assertFalse(ok(omang, "1234567890"));
        assertFalse(ok(omang, "12345678A"));
    }

    @Test
    void aNumberStaysWithinItsRange() {
        FormField income = field(FormFieldType.NUMBER, false);
        income.setMinValue(BigDecimal.ZERO);
        income.setMaxValue(new BigDecimal("1000000"));
        assertTrue(ok(income, "2500.50"));
        assertFalse(ok(income, "-1"));
        assertFalse(ok(income, "1000000.01"));
        assertFalse(ok(income, "a lot"));
    }

    @Test
    void datesAndTimesAreIso() {
        assertTrue(ok(field(FormFieldType.DATE, false), "2026-11-14"));
        assertFalse(ok(field(FormFieldType.DATE, false), "14/11/2026"));
        assertTrue(ok(field(FormFieldType.DATETIME, false), "2026-11-14T09:30"));
        assertFalse(ok(field(FormFieldType.DATETIME, false), "2026-11-14"));
        assertTrue(ok(field(FormFieldType.TIME, false), "09:30"));
        assertFalse(ok(field(FormFieldType.TIME, false), "half past nine"));
    }

    @Test
    void contactDetailsHaveTheirFormat() {
        assertTrue(ok(field(FormFieldType.EMAIL, false), "kabo@example.co.bw"));
        assertFalse(ok(field(FormFieldType.EMAIL, false), "kabo at example"));
        assertTrue(ok(field(FormFieldType.TEL, false), "+267 71 234 567"));
        assertFalse(ok(field(FormFieldType.TEL, false), "call me"));
        assertTrue(ok(field(FormFieldType.URL, false), "https://example.co.bw/about"));
        assertFalse(ok(field(FormFieldType.URL, false), "javascript:alert(1)"));
    }

    @Test
    void aRequiredTickBoxMustBeTicked() {
        assertTrue(ok(field(FormFieldType.CHECKBOX, true), "true"));
        assertFalse(ok(field(FormFieldType.CHECKBOX, true), "false"));
        assertTrue(ok(field(FormFieldType.CHECKBOX, false), "false"));
        assertFalse(ok(field(FormFieldType.CHECKBOX, false), "yes"));
    }

    @Test
    void choicesComeFromTheOptions() {
        FormField source = field(FormFieldType.SELECT, true);
        source.setOptions(List.of("Salary", "Business"));
        assertTrue(ok(source, "Salary"));
        assertFalse(ok(source, "Inheritance"));

        FormField sources = field(FormFieldType.MULTI_SELECT, true);
        sources.setOptions(List.of("Salary", "Business", "Gift"));
        assertTrue(FormValueValidator.validate(sources, null, List.of("Salary", "Gift"), true).isEmpty());
        assertFalse(FormValueValidator.validate(sources, null, List.of("Salary", "Loan"), true).isEmpty());
        assertFalse(FormValueValidator.validate(sources, null, List.of(), true).isEmpty());
        assertTrue(FormValueValidator.validate(sources, null, List.of(), false).isEmpty());
    }
}
