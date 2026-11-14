package bw.co.knowvera.screening;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * TODO: Model Documentation for bw.co.knowvera.screening.ScreeningHitStatus
 */
public enum ScreeningHitStatus
{
    /** TODO: Model Documentation for Enumeration Literal PENDING value "PENDING" */
    PENDING("PENDING"),
    /** TODO: Model Documentation for Enumeration Literal FALSE_POSITIVE value "FALSE_POSITIVE" */
    FALSE_POSITIVE("FALSE_POSITIVE"),
    /** TODO: Model Documentation for Enumeration Literal CONFIRMED value "CONFIRMED" */
    CONFIRMED("CONFIRMED");

    /**
     * The serial version UID of this class. Needed for serialization.
     */
    private static final long serialVersionUID = -6734688023199677303L;

    private final String enumValue;

    /**
     * The constructor with enumeration literal value allowing
     * super classes to access it.
     */
    private ScreeningHitStatus(String value)
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
     * Retrieves an instance of ScreeningHitStatus from <code>its name</code>.
     *
     * @param name the name to create the ScreeningHitStatus from.
     * @return The enumeration literal named after the 'name' argument
     */
    public static ScreeningHitStatus fromString(String name)
    {
        return ScreeningHitStatus.valueOf(name);
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
     * Returns an instance of ScreeningHitStatus from String <code>value</code>.
     * Required by JAXB2 enumeration implementation
     *
     * @param value the value to create the ScreeningHitStatus from.
     * @return static Enumeration with corresponding value
     */
    public static ScreeningHitStatus fromValue(String value)
    {
        // Allow either the Enum name or value string to be used to retrieve the enum
        for (ScreeningHitStatus enumName: ScreeningHitStatus.values())
        {
            if (enumName.getValue().equals(value))
            {
                return enumName;
            }
        }
        for (String enumName: ScreeningHitStatus.names())
        {
            if (enumName.equals(value))
            {
                return ScreeningHitStatus.valueOf(enumName);
            }
        }
        throw new IllegalArgumentException("ScreeningHitStatus.fromValue(" + value + ')');
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
        return ScreeningHitStatus.literals;
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
        return ScreeningHitStatus.names;
    }

    private static Map<String, ScreeningHitStatus> values = new LinkedHashMap<String, ScreeningHitStatus>(3, 1);
    private static List<String> literals = new ArrayList<String>(3);
    private static List<String> names = new ArrayList<String>(3);
    private static List<ScreeningHitStatus> valueList = new ArrayList<ScreeningHitStatus>(3);

    /**
     * Initializes the values.
     */
    static
    {
        synchronized (ScreeningHitStatus.values)
        {
            ScreeningHitStatus.values.put(PENDING.enumValue, PENDING);
            ScreeningHitStatus.values.put(FALSE_POSITIVE.enumValue, FALSE_POSITIVE);
            ScreeningHitStatus.values.put(CONFIRMED.enumValue, CONFIRMED);
        }
        synchronized (ScreeningHitStatus.valueList)
        {
            ScreeningHitStatus.valueList.add(PENDING);
            ScreeningHitStatus.valueList.add(FALSE_POSITIVE);
            ScreeningHitStatus.valueList.add(CONFIRMED);
            ScreeningHitStatus.valueList = Collections.unmodifiableList(valueList);
        }
        synchronized (ScreeningHitStatus.literals)
        {
            ScreeningHitStatus.literals.add(PENDING.enumValue);
            ScreeningHitStatus.literals.add(FALSE_POSITIVE.enumValue);
            ScreeningHitStatus.literals.add(CONFIRMED.enumValue);
            ScreeningHitStatus.literals = Collections.unmodifiableList(literals);
        }
        synchronized (ScreeningHitStatus.names)
        {
            ScreeningHitStatus.names.add("PENDING");
            ScreeningHitStatus.names.add("FALSE_POSITIVE");
            ScreeningHitStatus.names.add("CONFIRMED");
            ScreeningHitStatus.names = Collections.unmodifiableList(names);
        }
    }
}