package bw.co.knowvera.individual;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * TODO: Model Documentation for bw.co.knowvera.individual.EmploymentStatus
 */
public enum EmploymentStatus
{
    /** TODO: Model Documentation for Enumeration Literal EMPLOYED value "EMPLOYED" */
    EMPLOYED("EMPLOYED"),
    /** TODO: Model Documentation for Enumeration Literal SELF_EMPLOYED value "SELF_EMPLOYED" */
    SELF_EMPLOYED("SELF_EMPLOYED"),
    /** TODO: Model Documentation for Enumeration Literal STUDENT value "STUDENT" */
    STUDENT("STUDENT"),
    /** TODO: Model Documentation for Enumeration Literal RETIRED value "RETIRED" */
    RETIRED("RETIRED"),
    /** TODO: Model Documentation for Enumeration Literal UNEMPLOYED value "UNEMPLOYED" */
    UNEMPLOYED("UNEMPLOYED");

    /**
     * The serial version UID of this class. Needed for serialization.
     */
    private static final long serialVersionUID = 5177530445858457577L;

    private final String enumValue;

    /**
     * The constructor with enumeration literal value allowing
     * super classes to access it.
     */
    private EmploymentStatus(String value)
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
     * Retrieves an instance of EmploymentStatus from <code>its name</code>.
     *
     * @param name the name to create the EmploymentStatus from.
     * @return The enumeration literal named after the 'name' argument
     */
    public static EmploymentStatus fromString(String name)
    {
        return EmploymentStatus.valueOf(name);
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
     * Returns an instance of EmploymentStatus from String <code>value</code>.
     * Required by JAXB2 enumeration implementation
     *
     * @param value the value to create the EmploymentStatus from.
     * @return static Enumeration with corresponding value
     */
    public static EmploymentStatus fromValue(String value)
    {
        // Allow either the Enum name or value string to be used to retrieve the enum
        for (EmploymentStatus enumName: EmploymentStatus.values())
        {
            if (enumName.getValue().equals(value))
            {
                return enumName;
            }
        }
        for (String enumName: EmploymentStatus.names())
        {
            if (enumName.equals(value))
            {
                return EmploymentStatus.valueOf(enumName);
            }
        }
        throw new IllegalArgumentException("EmploymentStatus.fromValue(" + value + ')');
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
        return EmploymentStatus.literals;
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
        return EmploymentStatus.names;
    }

    private static Map<String, EmploymentStatus> values = new LinkedHashMap<String, EmploymentStatus>(5, 1);
    private static List<String> literals = new ArrayList<String>(5);
    private static List<String> names = new ArrayList<String>(5);
    private static List<EmploymentStatus> valueList = new ArrayList<EmploymentStatus>(5);

    /**
     * Initializes the values.
     */
    static
    {
        synchronized (EmploymentStatus.values)
        {
            EmploymentStatus.values.put(EMPLOYED.enumValue, EMPLOYED);
            EmploymentStatus.values.put(SELF_EMPLOYED.enumValue, SELF_EMPLOYED);
            EmploymentStatus.values.put(STUDENT.enumValue, STUDENT);
            EmploymentStatus.values.put(RETIRED.enumValue, RETIRED);
            EmploymentStatus.values.put(UNEMPLOYED.enumValue, UNEMPLOYED);
        }
        synchronized (EmploymentStatus.valueList)
        {
            EmploymentStatus.valueList.add(EMPLOYED);
            EmploymentStatus.valueList.add(SELF_EMPLOYED);
            EmploymentStatus.valueList.add(STUDENT);
            EmploymentStatus.valueList.add(RETIRED);
            EmploymentStatus.valueList.add(UNEMPLOYED);
            EmploymentStatus.valueList = Collections.unmodifiableList(valueList);
        }
        synchronized (EmploymentStatus.literals)
        {
            EmploymentStatus.literals.add(EMPLOYED.enumValue);
            EmploymentStatus.literals.add(SELF_EMPLOYED.enumValue);
            EmploymentStatus.literals.add(STUDENT.enumValue);
            EmploymentStatus.literals.add(RETIRED.enumValue);
            EmploymentStatus.literals.add(UNEMPLOYED.enumValue);
            EmploymentStatus.literals = Collections.unmodifiableList(literals);
        }
        synchronized (EmploymentStatus.names)
        {
            EmploymentStatus.names.add("EMPLOYED");
            EmploymentStatus.names.add("SELF_EMPLOYED");
            EmploymentStatus.names.add("STUDENT");
            EmploymentStatus.names.add("RETIRED");
            EmploymentStatus.names.add("UNEMPLOYED");
            EmploymentStatus.names = Collections.unmodifiableList(names);
        }
    }
}