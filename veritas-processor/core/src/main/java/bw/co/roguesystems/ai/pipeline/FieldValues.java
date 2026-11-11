package bw.co.roguesystems.ai.pipeline;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import bw.co.roguesystems.ai.schema.FieldSpec;

/**
 * Puts extracted values in the shape their fields declare, so callers get the same JSON whatever
 * model produced it: every requested field present (null when not found), numbers and booleans as
 * JSON values, dates as ISO dates, lists for fields that hold many values. A value that cannot be
 * converted is kept as the model gave it. Keys the model added are kept.
 */
final class FieldValues {

    /** Day first, as on Botswana documents. */
    private static final List<DateTimeFormatter> DATE_FORMATS = List.of(
            DateTimeFormatter.ISO_LOCAL_DATE,
            DateTimeFormatter.ofPattern("d/M/uuuu"),
            DateTimeFormatter.ofPattern("d-M-uuuu"),
            DateTimeFormatter.ofPattern("d.M.uuuu"),
            DateTimeFormatter.ofPattern("uuuu/M/d"),
            DateTimeFormatter.ofPattern("d MMM uuuu", Locale.ENGLISH),
            DateTimeFormatter.ofPattern("d MMMM uuuu", Locale.ENGLISH));

    private FieldValues() {
    }

    static Map<String, Object> normalise(Map<String, Object> extracted, List<FieldSpec> fields) {
        Map<String, Object> values = new LinkedHashMap<>();
        if (fields != null) {
            for (FieldSpec field : fields) {
                values.put(field.name(), value(extracted.get(field.name()), field));
            }
        }
        // Not putIfAbsent: a requested field normalised to null must stay null
        extracted.forEach((key, value) -> {
            if (!values.containsKey(key)) {
                values.put(key, value);
            }
        });
        return values;
    }

    private static Object value(Object raw, FieldSpec field) {
        if (raw == null || (raw instanceof String text && (text.isBlank() || text.equalsIgnoreCase("null")))) {
            return null;
        }
        if (field.many()) {
            Collection<?> items = raw instanceof Collection<?> collection ? collection : List.of(raw);
            List<Object> list = new ArrayList<>();
            items.forEach(item -> list.add(single(item, field)));
            return list;
        }
        return single(raw, field);
    }

    private static Object single(Object raw, FieldSpec field) {
        if (raw == null) {
            return null;
        }
        return switch (field.typeOrDefault()) {
            case NUMBER -> number(raw);
            case BOOLEAN -> bool(raw);
            case DATE -> isoDate(field) ? date(raw) : raw;
            case STRING -> raw instanceof String ? ((String) raw).trim() : raw;
            case OBJECT -> raw;
        };
    }

    private static Object number(Object raw) {
        if (raw instanceof Number) {
            return raw;
        }
        String digits = String.valueOf(raw).replaceAll("[^0-9,.\\-]", "");
        if (digits.contains(",") && digits.contains(".")) {
            // The separator that comes last is the decimal one: "1,250.50" and "1.250,50"
            digits = digits.lastIndexOf(',') > digits.lastIndexOf('.')
                    ? digits.replace(".", "").replace(",", ".")
                    : digits.replace(",", "");
        } else if (digits.contains(",")) {
            // "1 250,50" decimal comma, or "1,250" thousands
            digits = digits.matches(".*,\\d{1,2}$") ? digits.replace(",", ".") : digits.replace(",", "");
        }
        try {
            return new BigDecimal(digits);
        } catch (NumberFormatException e) {
            return raw;
        }
    }

    private static Object bool(Object raw) {
        if (raw instanceof Boolean) {
            return raw;
        }
        return switch (String.valueOf(raw).trim().toLowerCase(Locale.ROOT)) {
            case "true", "yes", "y", "1" -> Boolean.TRUE;
            case "false", "no", "n", "0" -> Boolean.FALSE;
            default -> raw;
        };
    }

    private static boolean isoDate(FieldSpec field) {
        return field.format() == null || field.format().isBlank()
                || field.format().replace(" ", "").equalsIgnoreCase("YYYY-MM-DD");
    }

    private static Object date(Object raw) {
        String text = String.valueOf(raw).trim();
        for (DateTimeFormatter format : DATE_FORMATS) {
            try {
                return LocalDate.parse(text, format).toString();
            } catch (DateTimeParseException e) {
                // next format
            }
        }
        return raw;
    }

    /** What the model is told to put in a field. */
    static String describe(FieldSpec field) {
        String shape = field.format() != null && !field.format().isBlank() ? field.format()
                : switch (field.typeOrDefault()) {
                    case STRING -> "text";
                    case NUMBER -> "number";
                    case DATE -> "YYYY-MM-DD";
                    case BOOLEAN -> "true or false";
                    case OBJECT -> "object";
                };
        String described = field.many() ? "list of " + shape : shape;
        return field.label() == null || field.label().isBlank() ? described : field.label() + " (" + described + ")";
    }

}
