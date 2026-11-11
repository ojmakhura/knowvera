package bw.co.knowvera;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * TODO: Model Documentation for bw.co.knowvera.SortOrder
 */
public enum SortOrder
{
    /** TODO: Model Documentation for Enumeration Literal DESC value "DESC" */
    DESC("DESC"),
    /** TODO: Model Documentation for Enumeration Literal ASC value "ASC" */
    ASC("ASC");

    /**
     * The serial version UID of this class. Needed for serialization.
     */
    private static final long serialVersionUID = -7962208394802642511L;

    private final String enumValue;

    /**
     * The constructor with enumeration literal value allowing
     * super classes to access it.
     */
    private SortOrder(String value)
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
     * Retrieves an instance of SortOrder from <code>its name</code>.
     *
     * @param name the name to create the SortOrder from.
     * @return The enumeration literal named after the 'name' argument
     */
    public static SortOrder fromString(String name)
    {
        return SortOrder.valueOf(name);
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
     * Returns an instance of SortOrder from String <code>value</code>.
     * Required by JAXB2 enumeration implementation
     *
     * @param value the value to create the SortOrder from.
     * @return static Enumeration with corresponding value
     */
    public static SortOrder fromValue(String value)
    {
        // Allow either the Enum name or value string to be used to retrieve the enum
        for (SortOrder enumName: SortOrder.values())
        {
            if (enumName.getValue().equals(value))
            {
                return enumName;
            }
        }
        for (String enumName: SortOrder.names())
        {
            if (enumName.equals(value))
            {
                return SortOrder.valueOf(enumName);
            }
        }
        throw new IllegalArgumentException("SortOrder.fromValue(" + value + ')');
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
        return SortOrder.literals;
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
        return SortOrder.names;
    }

    private static Map<String, SortOrder> values = new LinkedHashMap<String, SortOrder>(2, 1);
    private static List<String> literals = new ArrayList<String>(2);
    private static List<String> names = new ArrayList<String>(2);
    private static List<SortOrder> valueList = new ArrayList<SortOrder>(2);

    /**
     * Initializes the values.
     */
    static
    {
        synchronized (SortOrder.values)
        {
            SortOrder.values.put(DESC.enumValue, DESC);
            SortOrder.values.put(ASC.enumValue, ASC);
        }
        synchronized (SortOrder.valueList)
        {
            SortOrder.valueList.add(DESC);
            SortOrder.valueList.add(ASC);
            SortOrder.valueList = Collections.unmodifiableList(valueList);
        }
        synchronized (SortOrder.literals)
        {
            SortOrder.literals.add(DESC.enumValue);
            SortOrder.literals.add(ASC.enumValue);
            SortOrder.literals = Collections.unmodifiableList(literals);
        }
        synchronized (SortOrder.names)
        {
            SortOrder.names.add("DESC");
            SortOrder.names.add("ASC");
            SortOrder.names = Collections.unmodifiableList(names);
        }
    }
}