package bw.co.knowvera.individual;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * TODO: Model Documentation for bw.co.knowvera.individual.Sex
 */
public enum Sex
{
    /** TODO: Model Documentation for Enumeration Literal MALE value "MALE" */
    MALE("MALE"),
    /** TODO: Model Documentation for Enumeration Literal FEMALE value "FEMALE" */
    FEMALE("FEMALE"),
    /** TODO: Model Documentation for Enumeration Literal OTHER value "OTHER" */
    OTHER("OTHER"),
    /** TODO: Model Documentation for Enumeration Literal UNSPECIFIED value "UNSPECIFIED" */
    UNSPECIFIED("UNSPECIFIED");

    /**
     * The serial version UID of this class. Needed for serialization.
     */
    private static final long serialVersionUID = -6347270197898051101L;

    private final String enumValue;

    /**
     * The constructor with enumeration literal value allowing
     * super classes to access it.
     */
    private Sex(String value)
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
     * Retrieves an instance of Sex from <code>its name</code>.
     *
     * @param name the name to create the Sex from.
     * @return The enumeration literal named after the 'name' argument
     */
    public static Sex fromString(String name)
    {
        return Sex.valueOf(name);
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
     * Returns an instance of Sex from String <code>value</code>.
     * Required by JAXB2 enumeration implementation
     *
     * @param value the value to create the Sex from.
     * @return static Enumeration with corresponding value
     */
    public static Sex fromValue(String value)
    {
        // Allow either the Enum name or value string to be used to retrieve the enum
        for (Sex enumName: Sex.values())
        {
            if (enumName.getValue().equals(value))
            {
                return enumName;
            }
        }
        for (String enumName: Sex.names())
        {
            if (enumName.equals(value))
            {
                return Sex.valueOf(enumName);
            }
        }
        throw new IllegalArgumentException("Sex.fromValue(" + value + ')');
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
        return Sex.literals;
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
        return Sex.names;
    }

    private static Map<String, Sex> values = new LinkedHashMap<String, Sex>(4, 1);
    private static List<String> literals = new ArrayList<String>(4);
    private static List<String> names = new ArrayList<String>(4);
    private static List<Sex> valueList = new ArrayList<Sex>(4);

    /**
     * Initializes the values.
     */
    static
    {
        synchronized (Sex.values)
        {
            Sex.values.put(MALE.enumValue, MALE);
            Sex.values.put(FEMALE.enumValue, FEMALE);
            Sex.values.put(OTHER.enumValue, OTHER);
            Sex.values.put(UNSPECIFIED.enumValue, UNSPECIFIED);
        }
        synchronized (Sex.valueList)
        {
            Sex.valueList.add(MALE);
            Sex.valueList.add(FEMALE);
            Sex.valueList.add(OTHER);
            Sex.valueList.add(UNSPECIFIED);
            Sex.valueList = Collections.unmodifiableList(valueList);
        }
        synchronized (Sex.literals)
        {
            Sex.literals.add(MALE.enumValue);
            Sex.literals.add(FEMALE.enumValue);
            Sex.literals.add(OTHER.enumValue);
            Sex.literals.add(UNSPECIFIED.enumValue);
            Sex.literals = Collections.unmodifiableList(literals);
        }
        synchronized (Sex.names)
        {
            Sex.names.add("MALE");
            Sex.names.add("FEMALE");
            Sex.names.add("OTHER");
            Sex.names.add("UNSPECIFIED");
            Sex.names = Collections.unmodifiableList(names);
        }
    }
}