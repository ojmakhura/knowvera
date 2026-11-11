package bw.co.knowvera.sequence;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * TODO: Model Documentation for bw.co.knowvera.sequence.SequencePartType
 */
public enum SequencePartType
{
    /** TODO: Model Documentation for Enumeration Literal YEAR value "YEAR" */
    YEAR("YEAR"),
    /** TODO: Model Documentation for Enumeration Literal MONTH value "MONTH" */
    MONTH("MONTH"),
    /** TODO: Model Documentation for Enumeration Literal COUNTER value "COUNTER" */
    COUNTER("COUNTER"),
    /** TODO: Model Documentation for Enumeration Literal STATIC value "STATIC" */
    STATIC("STATIC");

    /**
     * The serial version UID of this class. Needed for serialization.
     */
    private static final long serialVersionUID = -1950522635390306334L;

    private final String enumValue;

    /**
     * The constructor with enumeration literal value allowing
     * super classes to access it.
     */
    private SequencePartType(String value)
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
     * Retrieves an instance of SequencePartType from <code>its name</code>.
     *
     * @param name the name to create the SequencePartType from.
     * @return The enumeration literal named after the 'name' argument
     */
    public static SequencePartType fromString(String name)
    {
        return SequencePartType.valueOf(name);
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
     * Returns an instance of SequencePartType from String <code>value</code>.
     * Required by JAXB2 enumeration implementation
     *
     * @param value the value to create the SequencePartType from.
     * @return static Enumeration with corresponding value
     */
    public static SequencePartType fromValue(String value)
    {
        // Allow either the Enum name or value string to be used to retrieve the enum
        for (SequencePartType enumName: SequencePartType.values())
        {
            if (enumName.getValue().equals(value))
            {
                return enumName;
            }
        }
        for (String enumName: SequencePartType.names())
        {
            if (enumName.equals(value))
            {
                return SequencePartType.valueOf(enumName);
            }
        }
        throw new IllegalArgumentException("SequencePartType.fromValue(" + value + ')');
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
        return SequencePartType.literals;
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
        return SequencePartType.names;
    }

    private static Map<String, SequencePartType> values = new LinkedHashMap<String, SequencePartType>(4, 1);
    private static List<String> literals = new ArrayList<String>(4);
    private static List<String> names = new ArrayList<String>(4);
    private static List<SequencePartType> valueList = new ArrayList<SequencePartType>(4);

    /**
     * Initializes the values.
     */
    static
    {
        synchronized (SequencePartType.values)
        {
            SequencePartType.values.put(YEAR.enumValue, YEAR);
            SequencePartType.values.put(MONTH.enumValue, MONTH);
            SequencePartType.values.put(COUNTER.enumValue, COUNTER);
            SequencePartType.values.put(STATIC.enumValue, STATIC);
        }
        synchronized (SequencePartType.valueList)
        {
            SequencePartType.valueList.add(YEAR);
            SequencePartType.valueList.add(MONTH);
            SequencePartType.valueList.add(COUNTER);
            SequencePartType.valueList.add(STATIC);
            SequencePartType.valueList = Collections.unmodifiableList(valueList);
        }
        synchronized (SequencePartType.literals)
        {
            SequencePartType.literals.add(YEAR.enumValue);
            SequencePartType.literals.add(MONTH.enumValue);
            SequencePartType.literals.add(COUNTER.enumValue);
            SequencePartType.literals.add(STATIC.enumValue);
            SequencePartType.literals = Collections.unmodifiableList(literals);
        }
        synchronized (SequencePartType.names)
        {
            SequencePartType.names.add("YEAR");
            SequencePartType.names.add("MONTH");
            SequencePartType.names.add("COUNTER");
            SequencePartType.names.add("STATIC");
            SequencePartType.names = Collections.unmodifiableList(names);
        }
    }
}