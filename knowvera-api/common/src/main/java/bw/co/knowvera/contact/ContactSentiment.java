package bw.co.knowvera.contact;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * TODO: Model Documentation for bw.co.knowvera.contact.ContactSentiment
 */
public enum ContactSentiment
{
    /** TODO: Model Documentation for Enumeration Literal POSITIVE value "POSITIVE" */
    POSITIVE("POSITIVE"),
    /** TODO: Model Documentation for Enumeration Literal NEUTRAL value "NEUTRAL" */
    NEUTRAL("NEUTRAL"),
    /** TODO: Model Documentation for Enumeration Literal NEGATIVE value "NEGATIVE" */
    NEGATIVE("NEGATIVE"),
    /** TODO: Model Documentation for Enumeration Literal CONFUSED value "CONFUSED" */
    CONFUSED("CONFUSED"),
    /** TODO: Model Documentation for Enumeration Literal SUSPICIOUS value "SUSPICIOUS" */
    SUSPICIOUS("SUSPICIOUS");

    /**
     * The serial version UID of this class. Needed for serialization.
     */
    private static final long serialVersionUID = 7409832579225392599L;

    private final String enumValue;

    /**
     * The constructor with enumeration literal value allowing
     * super classes to access it.
     */
    private ContactSentiment(String value)
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
     * Retrieves an instance of ContactSentiment from <code>its name</code>.
     *
     * @param name the name to create the ContactSentiment from.
     * @return The enumeration literal named after the 'name' argument
     */
    public static ContactSentiment fromString(String name)
    {
        return ContactSentiment.valueOf(name);
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
     * Returns an instance of ContactSentiment from String <code>value</code>.
     * Required by JAXB2 enumeration implementation
     *
     * @param value the value to create the ContactSentiment from.
     * @return static Enumeration with corresponding value
     */
    public static ContactSentiment fromValue(String value)
    {
        // Allow either the Enum name or value string to be used to retrieve the enum
        for (ContactSentiment enumName: ContactSentiment.values())
        {
            if (enumName.getValue().equals(value))
            {
                return enumName;
            }
        }
        for (String enumName: ContactSentiment.names())
        {
            if (enumName.equals(value))
            {
                return ContactSentiment.valueOf(enumName);
            }
        }
        throw new IllegalArgumentException("ContactSentiment.fromValue(" + value + ')');
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
        return ContactSentiment.literals;
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
        return ContactSentiment.names;
    }

    private static Map<String, ContactSentiment> values = new LinkedHashMap<String, ContactSentiment>(5, 1);
    private static List<String> literals = new ArrayList<String>(5);
    private static List<String> names = new ArrayList<String>(5);
    private static List<ContactSentiment> valueList = new ArrayList<ContactSentiment>(5);

    /**
     * Initializes the values.
     */
    static
    {
        synchronized (ContactSentiment.values)
        {
            ContactSentiment.values.put(POSITIVE.enumValue, POSITIVE);
            ContactSentiment.values.put(NEUTRAL.enumValue, NEUTRAL);
            ContactSentiment.values.put(NEGATIVE.enumValue, NEGATIVE);
            ContactSentiment.values.put(CONFUSED.enumValue, CONFUSED);
            ContactSentiment.values.put(SUSPICIOUS.enumValue, SUSPICIOUS);
        }
        synchronized (ContactSentiment.valueList)
        {
            ContactSentiment.valueList.add(POSITIVE);
            ContactSentiment.valueList.add(NEUTRAL);
            ContactSentiment.valueList.add(NEGATIVE);
            ContactSentiment.valueList.add(CONFUSED);
            ContactSentiment.valueList.add(SUSPICIOUS);
            ContactSentiment.valueList = Collections.unmodifiableList(valueList);
        }
        synchronized (ContactSentiment.literals)
        {
            ContactSentiment.literals.add(POSITIVE.enumValue);
            ContactSentiment.literals.add(NEUTRAL.enumValue);
            ContactSentiment.literals.add(NEGATIVE.enumValue);
            ContactSentiment.literals.add(CONFUSED.enumValue);
            ContactSentiment.literals.add(SUSPICIOUS.enumValue);
            ContactSentiment.literals = Collections.unmodifiableList(literals);
        }
        synchronized (ContactSentiment.names)
        {
            ContactSentiment.names.add("POSITIVE");
            ContactSentiment.names.add("NEUTRAL");
            ContactSentiment.names.add("NEGATIVE");
            ContactSentiment.names.add("CONFUSED");
            ContactSentiment.names.add("SUSPICIOUS");
            ContactSentiment.names = Collections.unmodifiableList(names);
        }
    }
}