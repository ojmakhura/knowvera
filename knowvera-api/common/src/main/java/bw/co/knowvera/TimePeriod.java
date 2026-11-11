package bw.co.knowvera;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * TODO: Model Documentation for bw.co.knowvera.TimePeriod
 */
public enum TimePeriod
{
    /** TODO: Model Documentation for Enumeration Literal WEEK value "WEEK" */
    WEEK("WEEK"),
    /** TODO: Model Documentation for Enumeration Literal MONTH value "MONTH" */
    MONTH("MONTH"),
    /** TODO: Model Documentation for Enumeration Literal YEAR value "YEAR" */
    YEAR("YEAR");

    /**
     * The serial version UID of this class. Needed for serialization.
     */
    private static final long serialVersionUID = -4268336727282472177L;

    private final String enumValue;

    /**
     * The constructor with enumeration literal value allowing
     * super classes to access it.
     */
    private TimePeriod(String value)
    {
        this.enumValue = value;
    }

    /*
     * Do not override this method directly. Use an alternate name
     * if providing a different toString implementation
     * @see Object#toString()
    @Override
    public String toString()
    {
        return String.valueOf(this.enumValue);
    }
     */

    /**
     * Retrieves an instance of TimePeriod from <code>its name</code>.
     *
     * @param name the name to create the TimePeriod from.
     * @return The enumeration literal named after the 'name' argument
     */
    public static TimePeriod fromString(String name)
    {
        return TimePeriod.valueOf(name);
    }

    /**
     * Returns an enumeration literal String <code>value</code>.
     * Required by JAXB2 enumeration implementation
     *
     * @return String with corresponding value
     */
    public String value()
    {
        return this.enumValue;
    }

    /**
     * Returns an instance of TimePeriod from String <code>value</code>.
     * Required by JAXB2 enumeration implementation
     *
     * @param value the value to create the TimePeriod from.
     * @return static Enumeration with corresponding value
     */
    public static TimePeriod fromValue(String value)
    {
        // Allow either the Enum name or value string to be used to retrieve the enum
        for (TimePeriod enumName: TimePeriod.values())
        {
            if (enumName.getValue().equals(value))
            {
                return enumName;
            }
        }
        for (String enumName: TimePeriod.names())
        {
            if (enumName.equals(value))
            {
                return TimePeriod.valueOf(enumName);
            }
        }
        throw new IllegalArgumentException("TimePeriod.fromValue(" + value + ')');
    }

    /**
     * Gets the underlying value of this type safe enumeration.
     * This method is necessary to comply with DaoBase implementation.
     * @return The name of this literal.
     */
    public String getValue()
    {
        return this.enumValue;
    }

    /**
     * Returns an unmodifiable list containing the literals that are known by this enumeration.
     *
     * @return A List containing the actual literals defined by this enumeration, this list
     *         can not be modified.
     */
    public static List<String> literals()
    {
        return TimePeriod.literals;
    }

    /**
     * Returns an unmodifiable list containing the names of the literals that are known
     * by this enumeration.
     *
     * @return A List containing the actual names of the literals defined by this
     *         enumeration, this list can not be modified.
     */
    public static List<String> names()
    {
        return TimePeriod.names;
    }

    private static Map<String, TimePeriod> values = new LinkedHashMap<String, TimePeriod>(3, 1);
    private static List<String> literals = new ArrayList<String>(3);
    private static List<String> names = new ArrayList<String>(3);
    private static List<TimePeriod> valueList = new ArrayList<TimePeriod>(3);

    /**
     * Initializes the values.
     */
    static
    {
        synchronized (TimePeriod.values)
        {
            TimePeriod.values.put(WEEK.enumValue, WEEK);
            TimePeriod.values.put(MONTH.enumValue, MONTH);
            TimePeriod.values.put(YEAR.enumValue, YEAR);
        }
        synchronized (TimePeriod.valueList)
        {
            TimePeriod.valueList.add(WEEK);
            TimePeriod.valueList.add(MONTH);
            TimePeriod.valueList.add(YEAR);
            TimePeriod.valueList = Collections.unmodifiableList(valueList);
        }
        synchronized (TimePeriod.literals)
        {
            TimePeriod.literals.add(WEEK.enumValue);
            TimePeriod.literals.add(MONTH.enumValue);
            TimePeriod.literals.add(YEAR.enumValue);
            TimePeriod.literals = Collections.unmodifiableList(literals);
        }
        synchronized (TimePeriod.names)
        {
            TimePeriod.names.add("WEEK");
            TimePeriod.names.add("MONTH");
            TimePeriod.names.add("YEAR");
            TimePeriod.names = Collections.unmodifiableList(names);
        }
    }
}