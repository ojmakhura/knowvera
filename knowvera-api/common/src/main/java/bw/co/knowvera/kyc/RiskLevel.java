package bw.co.knowvera.kyc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * TODO: Model Documentation for bw.co.knowvera.kyc.RiskLevel
 */
public enum RiskLevel
{
    /** TODO: Model Documentation for Enumeration Literal LOW value "LOW" */
    LOW("LOW"),
    /** TODO: Model Documentation for Enumeration Literal MEDIUM value "MEDIUM" */
    MEDIUM("MEDIUM"),
    /** TODO: Model Documentation for Enumeration Literal HIGH value "HIGH" */
    HIGH("HIGH");

    /**
     * The serial version UID of this class. Needed for serialization.
     */
    private static final long serialVersionUID = 2134239101260248820L;

    private final String enumValue;

    /**
     * The constructor with enumeration literal value allowing
     * super classes to access it.
     */
    private RiskLevel(String value)
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
     * Retrieves an instance of RiskLevel from <code>its name</code>.
     *
     * @param name the name to create the RiskLevel from.
     * @return The enumeration literal named after the 'name' argument
     */
    public static RiskLevel fromString(String name)
    {
        return RiskLevel.valueOf(name);
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
     * Returns an instance of RiskLevel from String <code>value</code>.
     * Required by JAXB2 enumeration implementation
     *
     * @param value the value to create the RiskLevel from.
     * @return static Enumeration with corresponding value
     */
    public static RiskLevel fromValue(String value)
    {
        // Allow either the Enum name or value string to be used to retrieve the enum
        for (RiskLevel enumName: RiskLevel.values())
        {
            if (enumName.getValue().equals(value))
            {
                return enumName;
            }
        }
        for (String enumName: RiskLevel.names())
        {
            if (enumName.equals(value))
            {
                return RiskLevel.valueOf(enumName);
            }
        }
        throw new IllegalArgumentException("RiskLevel.fromValue(" + value + ')');
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
        return RiskLevel.literals;
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
        return RiskLevel.names;
    }

    private static Map<String, RiskLevel> values = new LinkedHashMap<String, RiskLevel>(3, 1);
    private static List<String> literals = new ArrayList<String>(3);
    private static List<String> names = new ArrayList<String>(3);
    private static List<RiskLevel> valueList = new ArrayList<RiskLevel>(3);

    /**
     * Initializes the values.
     */
    static
    {
        synchronized (RiskLevel.values)
        {
            RiskLevel.values.put(LOW.enumValue, LOW);
            RiskLevel.values.put(MEDIUM.enumValue, MEDIUM);
            RiskLevel.values.put(HIGH.enumValue, HIGH);
        }
        synchronized (RiskLevel.valueList)
        {
            RiskLevel.valueList.add(LOW);
            RiskLevel.valueList.add(MEDIUM);
            RiskLevel.valueList.add(HIGH);
            RiskLevel.valueList = Collections.unmodifiableList(valueList);
        }
        synchronized (RiskLevel.literals)
        {
            RiskLevel.literals.add(LOW.enumValue);
            RiskLevel.literals.add(MEDIUM.enumValue);
            RiskLevel.literals.add(HIGH.enumValue);
            RiskLevel.literals = Collections.unmodifiableList(literals);
        }
        synchronized (RiskLevel.names)
        {
            RiskLevel.names.add("LOW");
            RiskLevel.names.add("MEDIUM");
            RiskLevel.names.add("HIGH");
            RiskLevel.names = Collections.unmodifiableList(names);
        }
    }
}