package bw.co.knowvera.organisation.client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * TODO: Model Documentation for bw.co.knowvera.organisation.client.ClientRequestStatus
 */
public enum ClientRequestStatus
{
    /** TODO: Model Documentation for Enumeration Literal PENDING value "PENDING" */
    PENDING("PENDING"),
    /** TODO: Model Documentation for Enumeration Literal CONTACTED value "CONTACTED" */
    CONTACTED("CONTACTED"),
    /** TODO: Model Documentation for Enumeration Literal ACCEPTED value "ACCEPTED" */
    ACCEPTED("ACCEPTED"),
    /** TODO: Model Documentation for Enumeration Literal REJECTED value "REJECTED" */
    REJECTED("REJECTED");

    /**
     * The serial version UID of this class. Needed for serialization.
     */
    private static final long serialVersionUID = 6968905329277583605L;

    private final String enumValue;

    /**
     * The constructor with enumeration literal value allowing
     * super classes to access it.
     */
    private ClientRequestStatus(String value)
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
     * Retrieves an instance of ClientRequestStatus from <code>its name</code>.
     *
     * @param name the name to create the ClientRequestStatus from.
     * @return The enumeration literal named after the 'name' argument
     */
    public static ClientRequestStatus fromString(String name)
    {
        return ClientRequestStatus.valueOf(name);
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
     * Returns an instance of ClientRequestStatus from String <code>value</code>.
     * Required by JAXB2 enumeration implementation
     *
     * @param value the value to create the ClientRequestStatus from.
     * @return static Enumeration with corresponding value
     */
    public static ClientRequestStatus fromValue(String value)
    {
        // Allow either the Enum name or value string to be used to retrieve the enum
        for (ClientRequestStatus enumName: ClientRequestStatus.values())
        {
            if (enumName.getValue().equals(value))
            {
                return enumName;
            }
        }
        for (String enumName: ClientRequestStatus.names())
        {
            if (enumName.equals(value))
            {
                return ClientRequestStatus.valueOf(enumName);
            }
        }
        throw new IllegalArgumentException("ClientRequestStatus.fromValue(" + value + ')');
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
        return ClientRequestStatus.literals;
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
        return ClientRequestStatus.names;
    }

    private static Map<String, ClientRequestStatus> values = new LinkedHashMap<String, ClientRequestStatus>(4, 1);
    private static List<String> literals = new ArrayList<String>(4);
    private static List<String> names = new ArrayList<String>(4);
    private static List<ClientRequestStatus> valueList = new ArrayList<ClientRequestStatus>(4);

    /**
     * Initializes the values.
     */
    static
    {
        synchronized (ClientRequestStatus.values)
        {
            ClientRequestStatus.values.put(PENDING.enumValue, PENDING);
            ClientRequestStatus.values.put(CONTACTED.enumValue, CONTACTED);
            ClientRequestStatus.values.put(ACCEPTED.enumValue, ACCEPTED);
            ClientRequestStatus.values.put(REJECTED.enumValue, REJECTED);
        }
        synchronized (ClientRequestStatus.valueList)
        {
            ClientRequestStatus.valueList.add(PENDING);
            ClientRequestStatus.valueList.add(CONTACTED);
            ClientRequestStatus.valueList.add(ACCEPTED);
            ClientRequestStatus.valueList.add(REJECTED);
            ClientRequestStatus.valueList = Collections.unmodifiableList(valueList);
        }
        synchronized (ClientRequestStatus.literals)
        {
            ClientRequestStatus.literals.add(PENDING.enumValue);
            ClientRequestStatus.literals.add(CONTACTED.enumValue);
            ClientRequestStatus.literals.add(ACCEPTED.enumValue);
            ClientRequestStatus.literals.add(REJECTED.enumValue);
            ClientRequestStatus.literals = Collections.unmodifiableList(literals);
        }
        synchronized (ClientRequestStatus.names)
        {
            ClientRequestStatus.names.add("PENDING");
            ClientRequestStatus.names.add("CONTACTED");
            ClientRequestStatus.names.add("ACCEPTED");
            ClientRequestStatus.names.add("REJECTED");
            ClientRequestStatus.names = Collections.unmodifiableList(names);
        }
    }
}