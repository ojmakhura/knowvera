package bw.co.knowvera.screening;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * TODO: Model Documentation for bw.co.knowvera.screening.ScreeningListSource
 */
public enum ScreeningListSource
{
    /** TODO: Model Documentation for Enumeration Literal UN_SECURITY_COUNCIL value "UN_SECURITY_COUNCIL" */
    UN_SECURITY_COUNCIL("UN_SECURITY_COUNCIL"),
    /** TODO: Model Documentation for Enumeration Literal BOTSWANA value "BOTSWANA" */
    BOTSWANA("BOTSWANA");

    /**
     * The serial version UID of this class. Needed for serialization.
     */
    private static final long serialVersionUID = -6696814831543136563L;

    private final String enumValue;

    /**
     * The constructor with enumeration literal value allowing
     * super classes to access it.
     */
    private ScreeningListSource(String value)
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
     * Retrieves an instance of ScreeningListSource from <code>its name</code>.
     *
     * @param name the name to create the ScreeningListSource from.
     * @return The enumeration literal named after the 'name' argument
     */
    public static ScreeningListSource fromString(String name)
    {
        return ScreeningListSource.valueOf(name);
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
     * Returns an instance of ScreeningListSource from String <code>value</code>.
     * Required by JAXB2 enumeration implementation
     *
     * @param value the value to create the ScreeningListSource from.
     * @return static Enumeration with corresponding value
     */
    public static ScreeningListSource fromValue(String value)
    {
        // Allow either the Enum name or value string to be used to retrieve the enum
        for (ScreeningListSource enumName: ScreeningListSource.values())
        {
            if (enumName.getValue().equals(value))
            {
                return enumName;
            }
        }
        for (String enumName: ScreeningListSource.names())
        {
            if (enumName.equals(value))
            {
                return ScreeningListSource.valueOf(enumName);
            }
        }
        throw new IllegalArgumentException("ScreeningListSource.fromValue(" + value + ')');
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
        return ScreeningListSource.literals;
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
        return ScreeningListSource.names;
    }

    private static Map<String, ScreeningListSource> values = new LinkedHashMap<String, ScreeningListSource>(2, 1);
    private static List<String> literals = new ArrayList<String>(2);
    private static List<String> names = new ArrayList<String>(2);
    private static List<ScreeningListSource> valueList = new ArrayList<ScreeningListSource>(2);

    /**
     * Initializes the values.
     */
    static
    {
        synchronized (ScreeningListSource.values)
        {
            ScreeningListSource.values.put(UN_SECURITY_COUNCIL.enumValue, UN_SECURITY_COUNCIL);
            ScreeningListSource.values.put(BOTSWANA.enumValue, BOTSWANA);
        }
        synchronized (ScreeningListSource.valueList)
        {
            ScreeningListSource.valueList.add(UN_SECURITY_COUNCIL);
            ScreeningListSource.valueList.add(BOTSWANA);
            ScreeningListSource.valueList = Collections.unmodifiableList(valueList);
        }
        synchronized (ScreeningListSource.literals)
        {
            ScreeningListSource.literals.add(UN_SECURITY_COUNCIL.enumValue);
            ScreeningListSource.literals.add(BOTSWANA.enumValue);
            ScreeningListSource.literals = Collections.unmodifiableList(literals);
        }
        synchronized (ScreeningListSource.names)
        {
            ScreeningListSource.names.add("UN_SECURITY_COUNCIL");
            ScreeningListSource.names.add("BOTSWANA");
            ScreeningListSource.names = Collections.unmodifiableList(names);
        }
    }
}