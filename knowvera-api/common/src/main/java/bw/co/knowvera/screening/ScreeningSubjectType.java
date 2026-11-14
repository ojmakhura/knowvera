package bw.co.knowvera.screening;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * TODO: Model Documentation for bw.co.knowvera.screening.ScreeningSubjectType
 */
public enum ScreeningSubjectType
{
    /** TODO: Model Documentation for Enumeration Literal INDIVIDUAL value "INDIVIDUAL" */
    INDIVIDUAL("INDIVIDUAL"),
    /** TODO: Model Documentation for Enumeration Literal ENTITY value "ENTITY" */
    ENTITY("ENTITY");

    /**
     * The serial version UID of this class. Needed for serialization.
     */
    private static final long serialVersionUID = -8418047686917353837L;

    private final String enumValue;

    /**
     * The constructor with enumeration literal value allowing
     * super classes to access it.
     */
    private ScreeningSubjectType(String value)
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
     * Retrieves an instance of ScreeningSubjectType from <code>its name</code>.
     *
     * @param name the name to create the ScreeningSubjectType from.
     * @return The enumeration literal named after the 'name' argument
     */
    public static ScreeningSubjectType fromString(String name)
    {
        return ScreeningSubjectType.valueOf(name);
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
     * Returns an instance of ScreeningSubjectType from String <code>value</code>.
     * Required by JAXB2 enumeration implementation
     *
     * @param value the value to create the ScreeningSubjectType from.
     * @return static Enumeration with corresponding value
     */
    public static ScreeningSubjectType fromValue(String value)
    {
        // Allow either the Enum name or value string to be used to retrieve the enum
        for (ScreeningSubjectType enumName: ScreeningSubjectType.values())
        {
            if (enumName.getValue().equals(value))
            {
                return enumName;
            }
        }
        for (String enumName: ScreeningSubjectType.names())
        {
            if (enumName.equals(value))
            {
                return ScreeningSubjectType.valueOf(enumName);
            }
        }
        throw new IllegalArgumentException("ScreeningSubjectType.fromValue(" + value + ')');
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
        return ScreeningSubjectType.literals;
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
        return ScreeningSubjectType.names;
    }

    private static Map<String, ScreeningSubjectType> values = new LinkedHashMap<String, ScreeningSubjectType>(2, 1);
    private static List<String> literals = new ArrayList<String>(2);
    private static List<String> names = new ArrayList<String>(2);
    private static List<ScreeningSubjectType> valueList = new ArrayList<ScreeningSubjectType>(2);

    /**
     * Initializes the values.
     */
    static
    {
        synchronized (ScreeningSubjectType.values)
        {
            ScreeningSubjectType.values.put(INDIVIDUAL.enumValue, INDIVIDUAL);
            ScreeningSubjectType.values.put(ENTITY.enumValue, ENTITY);
        }
        synchronized (ScreeningSubjectType.valueList)
        {
            ScreeningSubjectType.valueList.add(INDIVIDUAL);
            ScreeningSubjectType.valueList.add(ENTITY);
            ScreeningSubjectType.valueList = Collections.unmodifiableList(valueList);
        }
        synchronized (ScreeningSubjectType.literals)
        {
            ScreeningSubjectType.literals.add(INDIVIDUAL.enumValue);
            ScreeningSubjectType.literals.add(ENTITY.enumValue);
            ScreeningSubjectType.literals = Collections.unmodifiableList(literals);
        }
        synchronized (ScreeningSubjectType.names)
        {
            ScreeningSubjectType.names.add("INDIVIDUAL");
            ScreeningSubjectType.names.add("ENTITY");
            ScreeningSubjectType.names = Collections.unmodifiableList(names);
        }
    }
}