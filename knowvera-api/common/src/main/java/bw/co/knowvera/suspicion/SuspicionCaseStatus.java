package bw.co.knowvera.suspicion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * TODO: Model Documentation for bw.co.knowvera.suspicion.SuspicionCaseStatus
 */
public enum SuspicionCaseStatus
{
    /** TODO: Model Documentation for Enumeration Literal OPEN value "OPEN" */
    OPEN("OPEN"),
    /** TODO: Model Documentation for Enumeration Literal ESCALATED value "ESCALATED" */
    ESCALATED("ESCALATED"),
    /** TODO: Model Documentation for Enumeration Literal REPORTED value "REPORTED" */
    REPORTED("REPORTED"),
    /** TODO: Model Documentation for Enumeration Literal CLOSED value "CLOSED" */
    CLOSED("CLOSED");

    /**
     * The serial version UID of this class. Needed for serialization.
     */
    private static final long serialVersionUID = 1658967147625056947L;

    private final String enumValue;

    /**
     * The constructor with enumeration literal value allowing
     * super classes to access it.
     */
    private SuspicionCaseStatus(String value)
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
     * Retrieves an instance of SuspicionCaseStatus from <code>its name</code>.
     *
     * @param name the name to create the SuspicionCaseStatus from.
     * @return The enumeration literal named after the 'name' argument
     */
    public static SuspicionCaseStatus fromString(String name)
    {
        return SuspicionCaseStatus.valueOf(name);
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
     * Returns an instance of SuspicionCaseStatus from String <code>value</code>.
     * Required by JAXB2 enumeration implementation
     *
     * @param value the value to create the SuspicionCaseStatus from.
     * @return static Enumeration with corresponding value
     */
    public static SuspicionCaseStatus fromValue(String value)
    {
        // Allow either the Enum name or value string to be used to retrieve the enum
        for (SuspicionCaseStatus enumName: SuspicionCaseStatus.values())
        {
            if (enumName.getValue().equals(value))
            {
                return enumName;
            }
        }
        for (String enumName: SuspicionCaseStatus.names())
        {
            if (enumName.equals(value))
            {
                return SuspicionCaseStatus.valueOf(enumName);
            }
        }
        throw new IllegalArgumentException("SuspicionCaseStatus.fromValue(" + value + ')');
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
        return SuspicionCaseStatus.literals;
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
        return SuspicionCaseStatus.names;
    }

    private static Map<String, SuspicionCaseStatus> values = new LinkedHashMap<String, SuspicionCaseStatus>(4, 1);
    private static List<String> literals = new ArrayList<String>(4);
    private static List<String> names = new ArrayList<String>(4);
    private static List<SuspicionCaseStatus> valueList = new ArrayList<SuspicionCaseStatus>(4);

    /**
     * Initializes the values.
     */
    static
    {
        synchronized (SuspicionCaseStatus.values)
        {
            SuspicionCaseStatus.values.put(OPEN.enumValue, OPEN);
            SuspicionCaseStatus.values.put(ESCALATED.enumValue, ESCALATED);
            SuspicionCaseStatus.values.put(REPORTED.enumValue, REPORTED);
            SuspicionCaseStatus.values.put(CLOSED.enumValue, CLOSED);
        }
        synchronized (SuspicionCaseStatus.valueList)
        {
            SuspicionCaseStatus.valueList.add(OPEN);
            SuspicionCaseStatus.valueList.add(ESCALATED);
            SuspicionCaseStatus.valueList.add(REPORTED);
            SuspicionCaseStatus.valueList.add(CLOSED);
            SuspicionCaseStatus.valueList = Collections.unmodifiableList(valueList);
        }
        synchronized (SuspicionCaseStatus.literals)
        {
            SuspicionCaseStatus.literals.add(OPEN.enumValue);
            SuspicionCaseStatus.literals.add(ESCALATED.enumValue);
            SuspicionCaseStatus.literals.add(REPORTED.enumValue);
            SuspicionCaseStatus.literals.add(CLOSED.enumValue);
            SuspicionCaseStatus.literals = Collections.unmodifiableList(literals);
        }
        synchronized (SuspicionCaseStatus.names)
        {
            SuspicionCaseStatus.names.add("OPEN");
            SuspicionCaseStatus.names.add("ESCALATED");
            SuspicionCaseStatus.names.add("REPORTED");
            SuspicionCaseStatus.names.add("CLOSED");
            SuspicionCaseStatus.names = Collections.unmodifiableList(names);
        }
    }
}