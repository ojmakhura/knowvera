package bw.co.knowvera.individual;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * TODO: Model Documentation for bw.co.knowvera.individual.PepStatus
 */
public enum PepStatus
{
    /** TODO: Model Documentation for Enumeration Literal NOT_PEP value "NOT_PEP" */
    NOT_PEP("NOT_PEP"),
    /** TODO: Model Documentation for Enumeration Literal PEP_SELF value "PEP_SELF" */
    PEP_SELF("PEP_SELF"),
    /** TODO: Model Documentation for Enumeration Literal PEP_RELATIVE value "PEP_RELATIVE" */
    PEP_RELATIVE("PEP_RELATIVE"),
    /** TODO: Model Documentation for Enumeration Literal PEP_ASSOCIATE value "PEP_ASSOCIATE" */
    PEP_ASSOCIATE("PEP_ASSOCIATE");

    /**
     * The serial version UID of this class. Needed for serialization.
     */
    private static final long serialVersionUID = -2846875212674380437L;

    private final String enumValue;

    /**
     * The constructor with enumeration literal value allowing
     * super classes to access it.
     */
    private PepStatus(String value)
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
     * Retrieves an instance of PepStatus from <code>its name</code>.
     *
     * @param name the name to create the PepStatus from.
     * @return The enumeration literal named after the 'name' argument
     */
    public static PepStatus fromString(String name)
    {
        return PepStatus.valueOf(name);
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
     * Returns an instance of PepStatus from String <code>value</code>.
     * Required by JAXB2 enumeration implementation
     *
     * @param value the value to create the PepStatus from.
     * @return static Enumeration with corresponding value
     */
    public static PepStatus fromValue(String value)
    {
        // Allow either the Enum name or value string to be used to retrieve the enum
        for (PepStatus enumName: PepStatus.values())
        {
            if (enumName.getValue().equals(value))
            {
                return enumName;
            }
        }
        for (String enumName: PepStatus.names())
        {
            if (enumName.equals(value))
            {
                return PepStatus.valueOf(enumName);
            }
        }
        throw new IllegalArgumentException("PepStatus.fromValue(" + value + ')');
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
        return PepStatus.literals;
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
        return PepStatus.names;
    }

    private static Map<String, PepStatus> values = new LinkedHashMap<String, PepStatus>(4, 1);
    private static List<String> literals = new ArrayList<String>(4);
    private static List<String> names = new ArrayList<String>(4);
    private static List<PepStatus> valueList = new ArrayList<PepStatus>(4);

    /**
     * Initializes the values.
     */
    static
    {
        synchronized (PepStatus.values)
        {
            PepStatus.values.put(NOT_PEP.enumValue, NOT_PEP);
            PepStatus.values.put(PEP_SELF.enumValue, PEP_SELF);
            PepStatus.values.put(PEP_RELATIVE.enumValue, PEP_RELATIVE);
            PepStatus.values.put(PEP_ASSOCIATE.enumValue, PEP_ASSOCIATE);
        }
        synchronized (PepStatus.valueList)
        {
            PepStatus.valueList.add(NOT_PEP);
            PepStatus.valueList.add(PEP_SELF);
            PepStatus.valueList.add(PEP_RELATIVE);
            PepStatus.valueList.add(PEP_ASSOCIATE);
            PepStatus.valueList = Collections.unmodifiableList(valueList);
        }
        synchronized (PepStatus.literals)
        {
            PepStatus.literals.add(NOT_PEP.enumValue);
            PepStatus.literals.add(PEP_SELF.enumValue);
            PepStatus.literals.add(PEP_RELATIVE.enumValue);
            PepStatus.literals.add(PEP_ASSOCIATE.enumValue);
            PepStatus.literals = Collections.unmodifiableList(literals);
        }
        synchronized (PepStatus.names)
        {
            PepStatus.names.add("NOT_PEP");
            PepStatus.names.add("PEP_SELF");
            PepStatus.names.add("PEP_RELATIVE");
            PepStatus.names.add("PEP_ASSOCIATE");
            PepStatus.names = Collections.unmodifiableList(names);
        }
    }
}