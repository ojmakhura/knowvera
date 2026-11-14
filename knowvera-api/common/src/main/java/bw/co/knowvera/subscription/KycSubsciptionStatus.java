package bw.co.knowvera.subscription;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * TODO: Model Documentation for bw.co.knowvera.subscription.KycSubsciptionStatus
 */
public enum KycSubsciptionStatus
{
    /** TODO: Model Documentation for Enumeration Literal ACTIVE value "ACTIVE" */
    ACTIVE("ACTIVE"),
    /** TODO: Model Documentation for Enumeration Literal INACTIVE value "INACTIVE" */
    INACTIVE("INACTIVE"),
    /** TODO: Model Documentation for Enumeration Literal CANCELLED value "CANCELLED" */
    CANCELLED("CANCELLED");

    /**
     * The serial version UID of this class. Needed for serialization.
     */
    private static final long serialVersionUID = 5179083679674101917L;

    private final String enumValue;

    /**
     * The constructor with enumeration literal value allowing
     * super classes to access it.
     */
    private KycSubsciptionStatus(String value)
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
     * Retrieves an instance of KycSubsciptionStatus from <code>its name</code>.
     *
     * @param name the name to create the KycSubsciptionStatus from.
     * @return The enumeration literal named after the 'name' argument
     */
    public static KycSubsciptionStatus fromString(String name)
    {
        return KycSubsciptionStatus.valueOf(name);
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
     * Returns an instance of KycSubsciptionStatus from String <code>value</code>.
     * Required by JAXB2 enumeration implementation
     *
     * @param value the value to create the KycSubsciptionStatus from.
     * @return static Enumeration with corresponding value
     */
    public static KycSubsciptionStatus fromValue(String value)
    {
        // Allow either the Enum name or value string to be used to retrieve the enum
        for (KycSubsciptionStatus enumName: KycSubsciptionStatus.values())
        {
            if (enumName.getValue().equals(value))
            {
                return enumName;
            }
        }
        for (String enumName: KycSubsciptionStatus.names())
        {
            if (enumName.equals(value))
            {
                return KycSubsciptionStatus.valueOf(enumName);
            }
        }
        throw new IllegalArgumentException("KycSubsciptionStatus.fromValue(" + value + ')');
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
        return KycSubsciptionStatus.literals;
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
        return KycSubsciptionStatus.names;
    }

    private static Map<String, KycSubsciptionStatus> values = new LinkedHashMap<String, KycSubsciptionStatus>(3, 1);
    private static List<String> literals = new ArrayList<String>(3);
    private static List<String> names = new ArrayList<String>(3);
    private static List<KycSubsciptionStatus> valueList = new ArrayList<KycSubsciptionStatus>(3);

    /**
     * Initializes the values.
     */
    static
    {
        synchronized (KycSubsciptionStatus.values)
        {
            KycSubsciptionStatus.values.put(ACTIVE.enumValue, ACTIVE);
            KycSubsciptionStatus.values.put(INACTIVE.enumValue, INACTIVE);
            KycSubsciptionStatus.values.put(CANCELLED.enumValue, CANCELLED);
        }
        synchronized (KycSubsciptionStatus.valueList)
        {
            KycSubsciptionStatus.valueList.add(ACTIVE);
            KycSubsciptionStatus.valueList.add(INACTIVE);
            KycSubsciptionStatus.valueList.add(CANCELLED);
            KycSubsciptionStatus.valueList = Collections.unmodifiableList(valueList);
        }
        synchronized (KycSubsciptionStatus.literals)
        {
            KycSubsciptionStatus.literals.add(ACTIVE.enumValue);
            KycSubsciptionStatus.literals.add(INACTIVE.enumValue);
            KycSubsciptionStatus.literals.add(CANCELLED.enumValue);
            KycSubsciptionStatus.literals = Collections.unmodifiableList(literals);
        }
        synchronized (KycSubsciptionStatus.names)
        {
            KycSubsciptionStatus.names.add("ACTIVE");
            KycSubsciptionStatus.names.add("INACTIVE");
            KycSubsciptionStatus.names.add("CANCELLED");
            KycSubsciptionStatus.names = Collections.unmodifiableList(names);
        }
    }
}