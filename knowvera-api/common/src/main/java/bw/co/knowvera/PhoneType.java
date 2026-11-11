package bw.co.knowvera;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * TODO: Model Documentation for bw.co.knowvera.PhoneType
 */
public enum PhoneType
{
    /** TODO: Model Documentation for Enumeration Literal MOBILE value "MOBILE" */
    MOBILE("MOBILE"),
    /** TODO: Model Documentation for Enumeration Literal WORK value "WORK" */
    WORK("WORK"),
    /** TODO: Model Documentation for Enumeration Literal HOME value "HOME" */
    HOME("HOME"),
    /** TODO: Model Documentation for Enumeration Literal MAIN value "MAIN" */
    MAIN("MAIN"),
    /** TODO: Model Documentation for Enumeration Literal OTHER value "OTHER" */
    OTHER("OTHER");

    /**
     * The serial version UID of this class. Needed for serialization.
     */
    private static final long serialVersionUID = 6455171423134150704L;

    private final String enumValue;

    /**
     * The constructor with enumeration literal value allowing
     * super classes to access it.
     */
    private PhoneType(String value)
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
     * Retrieves an instance of PhoneType from <code>its name</code>.
     *
     * @param name the name to create the PhoneType from.
     * @return The enumeration literal named after the 'name' argument
     */
    public static PhoneType fromString(String name)
    {
        return PhoneType.valueOf(name);
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
     * Returns an instance of PhoneType from String <code>value</code>.
     * Required by JAXB2 enumeration implementation
     *
     * @param value the value to create the PhoneType from.
     * @return static Enumeration with corresponding value
     */
    public static PhoneType fromValue(String value)
    {
        // Allow either the Enum name or value string to be used to retrieve the enum
        for (PhoneType enumName: PhoneType.values())
        {
            if (enumName.getValue().equals(value))
            {
                return enumName;
            }
        }
        for (String enumName: PhoneType.names())
        {
            if (enumName.equals(value))
            {
                return PhoneType.valueOf(enumName);
            }
        }
        throw new IllegalArgumentException("PhoneType.fromValue(" + value + ')');
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
        return PhoneType.literals;
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
        return PhoneType.names;
    }

    private static Map<String, PhoneType> values = new LinkedHashMap<String, PhoneType>(5, 1);
    private static List<String> literals = new ArrayList<String>(5);
    private static List<String> names = new ArrayList<String>(5);
    private static List<PhoneType> valueList = new ArrayList<PhoneType>(5);

    /**
     * Initializes the values.
     */
    static
    {
        synchronized (PhoneType.values)
        {
            PhoneType.values.put(MOBILE.enumValue, MOBILE);
            PhoneType.values.put(WORK.enumValue, WORK);
            PhoneType.values.put(HOME.enumValue, HOME);
            PhoneType.values.put(MAIN.enumValue, MAIN);
            PhoneType.values.put(OTHER.enumValue, OTHER);
        }
        synchronized (PhoneType.valueList)
        {
            PhoneType.valueList.add(MOBILE);
            PhoneType.valueList.add(WORK);
            PhoneType.valueList.add(HOME);
            PhoneType.valueList.add(MAIN);
            PhoneType.valueList.add(OTHER);
            PhoneType.valueList = Collections.unmodifiableList(valueList);
        }
        synchronized (PhoneType.literals)
        {
            PhoneType.literals.add(MOBILE.enumValue);
            PhoneType.literals.add(WORK.enumValue);
            PhoneType.literals.add(HOME.enumValue);
            PhoneType.literals.add(MAIN.enumValue);
            PhoneType.literals.add(OTHER.enumValue);
            PhoneType.literals = Collections.unmodifiableList(literals);
        }
        synchronized (PhoneType.names)
        {
            PhoneType.names.add("MOBILE");
            PhoneType.names.add("WORK");
            PhoneType.names.add("HOME");
            PhoneType.names.add("MAIN");
            PhoneType.names.add("OTHER");
            PhoneType.names = Collections.unmodifiableList(names);
        }
    }
}