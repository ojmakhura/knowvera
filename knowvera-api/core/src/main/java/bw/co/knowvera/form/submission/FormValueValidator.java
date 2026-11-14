package bw.co.knowvera.form.submission;

import bw.co.knowvera.form.field.FormField;
import bw.co.knowvera.form.field.FormFieldType;
import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;

/**
 * Checks a client's answer against the rules of the field it answers.
 */
public final class FormValueValidator
{
    /** Longest text accepted where the field sets no limit of its own. */
    public static final int MAX_TEXT_LENGTH = 10000;

    private static final Pattern EMAIL = Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");
    private static final Pattern TEL = Pattern.compile("^\\+?[0-9][0-9 ()-]{4,24}$");

    private FormValueValidator()
    {
    }

    /**
     * @param field the answered field
     * @param value the answer; ignored for MULTI_SELECT
     * @param values the chosen options of a MULTI_SELECT field
     * @param enforceRequired false while a draft is saved: an empty answer is then accepted
     * @return what is wrong with the answer, or empty when it is acceptable
     */
    public static Optional<String> validate(FormField field, String value, List<String> values,
            boolean enforceRequired)
    {
        FormFieldType type = field.getFieldType();
        boolean required = Boolean.TRUE.equals(field.getRequired());

        if (type == FormFieldType.MULTI_SELECT)
        {
            if (values == null || values.isEmpty())
            {
                return required && enforceRequired ? Optional.of("is required") : Optional.empty();
            }
            for (String choice : values)
            {
                if (!isOption(field, choice))
                {
                    return Optional.of("'" + choice + "' is not one of the options");
                }
            }
            return Optional.empty();
        }

        if (StringUtils.isBlank(value))
        {
            return required && enforceRequired ? Optional.of("is required") : Optional.empty();
        }
        String text = value.trim();
        if (text.length() > MAX_TEXT_LENGTH)
        {
            return Optional.of("is longer than " + MAX_TEXT_LENGTH + " characters");
        }

        try
        {
            switch (type)
            {
                case NUMBER:
                    return number(field, text);
                case DATE:
                    LocalDate.parse(text);
                    return Optional.empty();
                case DATETIME:
                    LocalDateTime.parse(text);
                    return Optional.empty();
                case TIME:
                    LocalTime.parse(text);
                    return Optional.empty();
                case EMAIL:
                    return EMAIL.matcher(text).matches() ? Optional.empty() : Optional.of("is not an email address");
                case URL:
                    URI uri = new URI(text);
                    return uri.getHost() != null && ("http".equalsIgnoreCase(uri.getScheme())
                            || "https".equalsIgnoreCase(uri.getScheme()))
                            ? Optional.empty() : Optional.of("is not a web address");
                case CHECKBOX:
                    if (!"true".equalsIgnoreCase(text) && !"false".equalsIgnoreCase(text))
                    {
                        return Optional.of("must be true or false");
                    }
                    // A required tick box has to be ticked, e.g. a declaration
                    return required && enforceRequired && !"true".equalsIgnoreCase(text)
                            ? Optional.of("must be ticked") : Optional.empty();
                case SELECT:
                case RADIO:
                    return isOption(field, text) ? Optional.empty() : Optional.of("is not one of the options");
                case TEL:
                    if (!TEL.matcher(text).matches())
                    {
                        return Optional.of("is not a telephone number");
                    }
                    return text(field, text);
                default:
                    return text(field, text);
            }
        }
        catch (Exception e)
        {
            return Optional.of(switch (type)
            {
                case NUMBER -> "is not a number";
                case DATE -> "is not a date (yyyy-MM-dd)";
                case DATETIME -> "is not a date and time (yyyy-MM-ddTHH:mm)";
                case TIME -> "is not a time (HH:mm)";
                case URL -> "is not a web address";
                default -> "is not valid";
            });
        }
    }

    private static Optional<String> number(FormField field, String text)
    {
        BigDecimal number = new BigDecimal(text);
        if (field.getMinValue() != null && number.compareTo(field.getMinValue()) < 0)
        {
            return Optional.of("must be at least " + field.getMinValue().toPlainString());
        }
        if (field.getMaxValue() != null && number.compareTo(field.getMaxValue()) > 0)
        {
            return Optional.of("must be at most " + field.getMaxValue().toPlainString());
        }
        return Optional.empty();
    }

    private static Optional<String> text(FormField field, String text)
    {
        if (field.getMinLength() != null && text.length() < field.getMinLength())
        {
            return Optional.of("must be at least " + field.getMinLength() + " characters long");
        }
        if (field.getMaxLength() != null && text.length() > field.getMaxLength())
        {
            return Optional.of("must be at most " + field.getMaxLength() + " characters long");
        }
        if (StringUtils.isNotBlank(field.getPattern()) && !Pattern.matches(field.getPattern(), text))
        {
            return Optional.of("does not have the expected format");
        }
        return Optional.empty();
    }

    private static boolean isOption(FormField field, String choice)
    {
        return choice != null && field.getOptions() != null && field.getOptions().contains(choice.trim());
    }
}
