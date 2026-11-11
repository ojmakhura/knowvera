package bw.co.knowvera;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * TODO: Model Documentation for bw.co.knowvera.IdentityType
 */
public enum IdentityType
{
    /** TODO: Model Documentation for Enumeration Literal OMANG value "OMANG" */
    OMANG("OMANG"),
    /** TODO: Model Documentation for Enumeration Literal PASSPORT value "PASSPORT" */
    PASSPORT("PASSPORT"),
    /** TODO: Model Documentation for Enumeration Literal CIPA value "CIPA" */
    CIPA("CIPA");

    /**
     * The serial version UID of this class. Needed for serialization.
     */
    private static final long serialVersionUID = 6969597796501073149L;

    private final String enumValue;

    /**
     * The constructor with enumeration literal value allowing
     * super classes to access it.
     */
    private IdentityType(String value)
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
     * Retrieves an instance of IdentityType from <code>its name</code>.
     *
     * @param name the name to create the IdentityType from.
     * @return The enumeration literal named after the 'name' argument
     */
    public static IdentityType fromString(String name)
    {
        return IdentityType.valueOf(name);
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
     * Returns an instance of IdentityType from String <code>value</code>.
     * Required by JAXB2 enumeration implementation
     *
     * @param value the value to create the IdentityType from.
     * @return static Enumeration with corresponding value
     */
    public static IdentityType fromValue(String value)
    {
        // Allow either the Enum name or value string to be used to retrieve the enum
        for (IdentityType enumName: IdentityType.values())
        {
            if (enumName.getValue().equals(value))
            {
                return enumName;
            }
        }
        for (String enumName: IdentityType.names())
        {
            if (enumName.equals(value))
            {
                return IdentityType.valueOf(enumName);
            }
        }
        throw new IllegalArgumentException("IdentityType.fromValue(" + value + ')');
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
        return IdentityType.literals;
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
        return IdentityType.names;
    }

    private static Map<String, IdentityType> values = new LinkedHashMap<String, IdentityType>(3, 1);
    private static List<String> literals = new ArrayList<String>(3);
    private static List<String> names = new ArrayList<String>(3);
    private static List<IdentityType> valueList = new ArrayList<IdentityType>(3);

    /**
     * Initializes the values.
     */
    static
    {
        synchronized (IdentityType.values)
        {
            IdentityType.values.put(OMANG.enumValue, OMANG);
            IdentityType.values.put(PASSPORT.enumValue, PASSPORT);
            IdentityType.values.put(CIPA.enumValue, CIPA);
        }
        synchronized (IdentityType.valueList)
        {
            IdentityType.valueList.add(OMANG);
            IdentityType.valueList.add(PASSPORT);
            IdentityType.valueList.add(CIPA);
            IdentityType.valueList = Collections.unmodifiableList(valueList);
        }
        synchronized (IdentityType.literals)
        {
            IdentityType.literals.add(OMANG.enumValue);
            IdentityType.literals.add(PASSPORT.enumValue);
            IdentityType.literals.add(CIPA.enumValue);
            IdentityType.literals = Collections.unmodifiableList(literals);
        }
        synchronized (IdentityType.names)
        {
            IdentityType.names.add("OMANG");
            IdentityType.names.add("PASSPORT");
            IdentityType.names.add("CIPA");
            IdentityType.names = Collections.unmodifiableList(names);
        }
    }
}