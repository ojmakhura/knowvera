package bw.co.knowvera;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * TODO: Model Documentation for bw.co.knowvera.GeneralStatus
 */
public enum GeneralStatus
{
    /** TODO: Model Documentation for Enumeration Literal ACTIVE value "ACTIVE" */
    ACTIVE("ACTIVE"),
    /** TODO: Model Documentation for Enumeration Literal INACTIVE value "INACTIVE" */
    INACTIVE("INACTIVE");

    /**
     * The serial version UID of this class. Needed for serialization.
     */
    private static final long serialVersionUID = -5468043088570025413L;

    private final String enumValue;

    /**
     * The constructor with enumeration literal value allowing
     * super classes to access it.
     */
    private GeneralStatus(String value)
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
     * Retrieves an instance of GeneralStatus from <code>its name</code>.
     *
     * @param name the name to create the GeneralStatus from.
     * @return The enumeration literal named after the 'name' argument
     */
    public static GeneralStatus fromString(String name)
    {
        return GeneralStatus.valueOf(name);
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
     * Returns an instance of GeneralStatus from String <code>value</code>.
     * Required by JAXB2 enumeration implementation
     *
     * @param value the value to create the GeneralStatus from.
     * @return static Enumeration with corresponding value
     */
    public static GeneralStatus fromValue(String value)
    {
        // Allow either the Enum name or value string to be used to retrieve the enum
        for (GeneralStatus enumName: GeneralStatus.values())
        {
            if (enumName.getValue().equals(value))
            {
                return enumName;
            }
        }
        for (String enumName: GeneralStatus.names())
        {
            if (enumName.equals(value))
            {
                return GeneralStatus.valueOf(enumName);
            }
        }
        throw new IllegalArgumentException("GeneralStatus.fromValue(" + value + ')');
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
        return GeneralStatus.literals;
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
        return GeneralStatus.names;
    }

    private static Map<String, GeneralStatus> values = new LinkedHashMap<String, GeneralStatus>(2, 1);
    private static List<String> literals = new ArrayList<String>(2);
    private static List<String> names = new ArrayList<String>(2);
    private static List<GeneralStatus> valueList = new ArrayList<GeneralStatus>(2);

    /**
     * Initializes the values.
     */
    static
    {
        synchronized (GeneralStatus.values)
        {
            GeneralStatus.values.put(ACTIVE.enumValue, ACTIVE);
            GeneralStatus.values.put(INACTIVE.enumValue, INACTIVE);
        }
        synchronized (GeneralStatus.valueList)
        {
            GeneralStatus.valueList.add(ACTIVE);
            GeneralStatus.valueList.add(INACTIVE);
            GeneralStatus.valueList = Collections.unmodifiableList(valueList);
        }
        synchronized (GeneralStatus.literals)
        {
            GeneralStatus.literals.add(ACTIVE.enumValue);
            GeneralStatus.literals.add(INACTIVE.enumValue);
            GeneralStatus.literals = Collections.unmodifiableList(literals);
        }
        synchronized (GeneralStatus.names)
        {
            GeneralStatus.names.add("ACTIVE");
            GeneralStatus.names.add("INACTIVE");
            GeneralStatus.names = Collections.unmodifiableList(names);
        }
    }
}