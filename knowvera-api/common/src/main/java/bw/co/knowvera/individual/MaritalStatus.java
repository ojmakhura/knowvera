package bw.co.knowvera.individual;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * TODO: Model Documentation for bw.co.knowvera.individual.MaritalStatus
 */
public enum MaritalStatus
{
    /** TODO: Model Documentation for Enumeration Literal SINGLE value "SINGLE" */
    SINGLE("SINGLE"),
    /** TODO: Model Documentation for Enumeration Literal MARRIED value "MARRIED" */
    MARRIED("MARRIED"),
    /** TODO: Model Documentation for Enumeration Literal DIVORCED value "DIVORCED" */
    DIVORCED("DIVORCED"),
    /** TODO: Model Documentation for Enumeration Literal WIDOWED value "WIDOWED" */
    WIDOWED("WIDOWED"),
    /** TODO: Model Documentation for Enumeration Literal OTHER value "OTHER" */
    OTHER("OTHER");

    /**
     * The serial version UID of this class. Needed for serialization.
     */
    private static final long serialVersionUID = -3491708226172310178L;

    private final String enumValue;

    /**
     * The constructor with enumeration literal value allowing
     * super classes to access it.
     */
    private MaritalStatus(String value)
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
     * Retrieves an instance of MaritalStatus from <code>its name</code>.
     *
     * @param name the name to create the MaritalStatus from.
     * @return The enumeration literal named after the 'name' argument
     */
    public static MaritalStatus fromString(String name)
    {
        return MaritalStatus.valueOf(name);
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
     * Returns an instance of MaritalStatus from String <code>value</code>.
     * Required by JAXB2 enumeration implementation
     *
     * @param value the value to create the MaritalStatus from.
     * @return static Enumeration with corresponding value
     */
    public static MaritalStatus fromValue(String value)
    {
        // Allow either the Enum name or value string to be used to retrieve the enum
        for (MaritalStatus enumName: MaritalStatus.values())
        {
            if (enumName.getValue().equals(value))
            {
                return enumName;
            }
        }
        for (String enumName: MaritalStatus.names())
        {
            if (enumName.equals(value))
            {
                return MaritalStatus.valueOf(enumName);
            }
        }
        throw new IllegalArgumentException("MaritalStatus.fromValue(" + value + ')');
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
        return MaritalStatus.literals;
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
        return MaritalStatus.names;
    }

    private static Map<String, MaritalStatus> values = new LinkedHashMap<String, MaritalStatus>(5, 1);
    private static List<String> literals = new ArrayList<String>(5);
    private static List<String> names = new ArrayList<String>(5);
    private static List<MaritalStatus> valueList = new ArrayList<MaritalStatus>(5);

    /**
     * Initializes the values.
     */
    static
    {
        synchronized (MaritalStatus.values)
        {
            MaritalStatus.values.put(SINGLE.enumValue, SINGLE);
            MaritalStatus.values.put(MARRIED.enumValue, MARRIED);
            MaritalStatus.values.put(DIVORCED.enumValue, DIVORCED);
            MaritalStatus.values.put(WIDOWED.enumValue, WIDOWED);
            MaritalStatus.values.put(OTHER.enumValue, OTHER);
        }
        synchronized (MaritalStatus.valueList)
        {
            MaritalStatus.valueList.add(SINGLE);
            MaritalStatus.valueList.add(MARRIED);
            MaritalStatus.valueList.add(DIVORCED);
            MaritalStatus.valueList.add(WIDOWED);
            MaritalStatus.valueList.add(OTHER);
            MaritalStatus.valueList = Collections.unmodifiableList(valueList);
        }
        synchronized (MaritalStatus.literals)
        {
            MaritalStatus.literals.add(SINGLE.enumValue);
            MaritalStatus.literals.add(MARRIED.enumValue);
            MaritalStatus.literals.add(DIVORCED.enumValue);
            MaritalStatus.literals.add(WIDOWED.enumValue);
            MaritalStatus.literals.add(OTHER.enumValue);
            MaritalStatus.literals = Collections.unmodifiableList(literals);
        }
        synchronized (MaritalStatus.names)
        {
            MaritalStatus.names.add("SINGLE");
            MaritalStatus.names.add("MARRIED");
            MaritalStatus.names.add("DIVORCED");
            MaritalStatus.names.add("WIDOWED");
            MaritalStatus.names.add("OTHER");
            MaritalStatus.names = Collections.unmodifiableList(names);
        }
    }
}