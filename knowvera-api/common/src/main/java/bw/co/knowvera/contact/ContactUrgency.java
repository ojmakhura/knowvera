package bw.co.knowvera.contact;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * TODO: Model Documentation for bw.co.knowvera.contact.ContactUrgency
 */
public enum ContactUrgency
{
    /** TODO: Model Documentation for Enumeration Literal LOW value "LOW" */
    LOW("LOW"),
    /** TODO: Model Documentation for Enumeration Literal NORMAL value "NORMAL" */
    NORMAL("NORMAL"),
    /** TODO: Model Documentation for Enumeration Literal HIGH value "HIGH" */
    HIGH("HIGH"),
    /** TODO: Model Documentation for Enumeration Literal CRITICAL value "CRITICAL" */
    CRITICAL("CRITICAL");

    /**
     * The serial version UID of this class. Needed for serialization.
     */
    private static final long serialVersionUID = 2144174524644718688L;

    private final String enumValue;

    /**
     * The constructor with enumeration literal value allowing
     * super classes to access it.
     */
    private ContactUrgency(String value)
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
     * Retrieves an instance of ContactUrgency from <code>its name</code>.
     *
     * @param name the name to create the ContactUrgency from.
     * @return The enumeration literal named after the 'name' argument
     */
    public static ContactUrgency fromString(String name)
    {
        return ContactUrgency.valueOf(name);
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
     * Returns an instance of ContactUrgency from String <code>value</code>.
     * Required by JAXB2 enumeration implementation
     *
     * @param value the value to create the ContactUrgency from.
     * @return static Enumeration with corresponding value
     */
    public static ContactUrgency fromValue(String value)
    {
        // Allow either the Enum name or value string to be used to retrieve the enum
        for (ContactUrgency enumName: ContactUrgency.values())
        {
            if (enumName.getValue().equals(value))
            {
                return enumName;
            }
        }
        for (String enumName: ContactUrgency.names())
        {
            if (enumName.equals(value))
            {
                return ContactUrgency.valueOf(enumName);
            }
        }
        throw new IllegalArgumentException("ContactUrgency.fromValue(" + value + ')');
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
        return ContactUrgency.literals;
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
        return ContactUrgency.names;
    }

    private static Map<String, ContactUrgency> values = new LinkedHashMap<String, ContactUrgency>(4, 1);
    private static List<String> literals = new ArrayList<String>(4);
    private static List<String> names = new ArrayList<String>(4);
    private static List<ContactUrgency> valueList = new ArrayList<ContactUrgency>(4);

    /**
     * Initializes the values.
     */
    static
    {
        synchronized (ContactUrgency.values)
        {
            ContactUrgency.values.put(LOW.enumValue, LOW);
            ContactUrgency.values.put(NORMAL.enumValue, NORMAL);
            ContactUrgency.values.put(HIGH.enumValue, HIGH);
            ContactUrgency.values.put(CRITICAL.enumValue, CRITICAL);
        }
        synchronized (ContactUrgency.valueList)
        {
            ContactUrgency.valueList.add(LOW);
            ContactUrgency.valueList.add(NORMAL);
            ContactUrgency.valueList.add(HIGH);
            ContactUrgency.valueList.add(CRITICAL);
            ContactUrgency.valueList = Collections.unmodifiableList(valueList);
        }
        synchronized (ContactUrgency.literals)
        {
            ContactUrgency.literals.add(LOW.enumValue);
            ContactUrgency.literals.add(NORMAL.enumValue);
            ContactUrgency.literals.add(HIGH.enumValue);
            ContactUrgency.literals.add(CRITICAL.enumValue);
            ContactUrgency.literals = Collections.unmodifiableList(literals);
        }
        synchronized (ContactUrgency.names)
        {
            ContactUrgency.names.add("LOW");
            ContactUrgency.names.add("NORMAL");
            ContactUrgency.names.add("HIGH");
            ContactUrgency.names.add("CRITICAL");
            ContactUrgency.names = Collections.unmodifiableList(names);
        }
    }
}