package bw.co.knowvera.contact;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * TODO: Model Documentation for bw.co.knowvera.contact.ContactType
 */
public enum ContactType
{
    /** TODO: Model Documentation for Enumeration Literal ENQUIRY value "ENQUIRY" */
    ENQUIRY("ENQUIRY"),
    /** TODO: Model Documentation for Enumeration Literal COMMENT value "COMMENT" */
    COMMENT("COMMENT"),
    /** TODO: Model Documentation for Enumeration Literal COMPLAINT value "COMPLAINT" */
    COMPLAINT("COMPLAINT");

    /**
     * The serial version UID of this class. Needed for serialization.
     */
    private static final long serialVersionUID = -3384895032187132054L;

    private final String enumValue;

    /**
     * The constructor with enumeration literal value allowing
     * super classes to access it.
     */
    private ContactType(String value)
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
     * Retrieves an instance of ContactType from <code>its name</code>.
     *
     * @param name the name to create the ContactType from.
     * @return The enumeration literal named after the 'name' argument
     */
    public static ContactType fromString(String name)
    {
        return ContactType.valueOf(name);
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
     * Returns an instance of ContactType from String <code>value</code>.
     * Required by JAXB2 enumeration implementation
     *
     * @param value the value to create the ContactType from.
     * @return static Enumeration with corresponding value
     */
    public static ContactType fromValue(String value)
    {
        // Allow either the Enum name or value string to be used to retrieve the enum
        for (ContactType enumName: ContactType.values())
        {
            if (enumName.getValue().equals(value))
            {
                return enumName;
            }
        }
        for (String enumName: ContactType.names())
        {
            if (enumName.equals(value))
            {
                return ContactType.valueOf(enumName);
            }
        }
        throw new IllegalArgumentException("ContactType.fromValue(" + value + ')');
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
        return ContactType.literals;
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
        return ContactType.names;
    }

    private static Map<String, ContactType> values = new LinkedHashMap<String, ContactType>(3, 1);
    private static List<String> literals = new ArrayList<String>(3);
    private static List<String> names = new ArrayList<String>(3);
    private static List<ContactType> valueList = new ArrayList<ContactType>(3);

    /**
     * Initializes the values.
     */
    static
    {
        synchronized (ContactType.values)
        {
            ContactType.values.put(ENQUIRY.enumValue, ENQUIRY);
            ContactType.values.put(COMMENT.enumValue, COMMENT);
            ContactType.values.put(COMPLAINT.enumValue, COMPLAINT);
        }
        synchronized (ContactType.valueList)
        {
            ContactType.valueList.add(ENQUIRY);
            ContactType.valueList.add(COMMENT);
            ContactType.valueList.add(COMPLAINT);
            ContactType.valueList = Collections.unmodifiableList(valueList);
        }
        synchronized (ContactType.literals)
        {
            ContactType.literals.add(ENQUIRY.enumValue);
            ContactType.literals.add(COMMENT.enumValue);
            ContactType.literals.add(COMPLAINT.enumValue);
            ContactType.literals = Collections.unmodifiableList(literals);
        }
        synchronized (ContactType.names)
        {
            ContactType.names.add("ENQUIRY");
            ContactType.names.add("COMMENT");
            ContactType.names.add("COMPLAINT");
            ContactType.names = Collections.unmodifiableList(names);
        }
    }
}