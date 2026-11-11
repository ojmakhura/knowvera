package bw.co.knowvera.invoice;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * TODO: Model Documentation for bw.co.knowvera.invoice.UploadPurpose
 */
public enum UploadPurpose
{
    /** TODO: Model Documentation for Enumeration Literal INVOICE value "INVOICE" */
    INVOICE("INVOICE"),
    /** TODO: Model Documentation for Enumeration Literal PAYMENT value "PAYMENT" */
    PAYMENT("PAYMENT");

    /**
     * The serial version UID of this class. Needed for serialization.
     */
    private static final long serialVersionUID = 7933502559105099358L;

    private final String enumValue;

    /**
     * The constructor with enumeration literal value allowing
     * super classes to access it.
     */
    private UploadPurpose(String value)
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
     * Retrieves an instance of UploadPurpose from <code>its name</code>.
     *
     * @param name the name to create the UploadPurpose from.
     * @return The enumeration literal named after the 'name' argument
     */
    public static UploadPurpose fromString(String name)
    {
        return UploadPurpose.valueOf(name);
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
     * Returns an instance of UploadPurpose from String <code>value</code>.
     * Required by JAXB2 enumeration implementation
     *
     * @param value the value to create the UploadPurpose from.
     * @return static Enumeration with corresponding value
     */
    public static UploadPurpose fromValue(String value)
    {
        // Allow either the Enum name or value string to be used to retrieve the enum
        for (UploadPurpose enumName: UploadPurpose.values())
        {
            if (enumName.getValue().equals(value))
            {
                return enumName;
            }
        }
        for (String enumName: UploadPurpose.names())
        {
            if (enumName.equals(value))
            {
                return UploadPurpose.valueOf(enumName);
            }
        }
        throw new IllegalArgumentException("UploadPurpose.fromValue(" + value + ')');
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
        return UploadPurpose.literals;
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
        return UploadPurpose.names;
    }

    private static Map<String, UploadPurpose> values = new LinkedHashMap<String, UploadPurpose>(2, 1);
    private static List<String> literals = new ArrayList<String>(2);
    private static List<String> names = new ArrayList<String>(2);
    private static List<UploadPurpose> valueList = new ArrayList<UploadPurpose>(2);

    /**
     * Initializes the values.
     */
    static
    {
        synchronized (UploadPurpose.values)
        {
            UploadPurpose.values.put(INVOICE.enumValue, INVOICE);
            UploadPurpose.values.put(PAYMENT.enumValue, PAYMENT);
        }
        synchronized (UploadPurpose.valueList)
        {
            UploadPurpose.valueList.add(INVOICE);
            UploadPurpose.valueList.add(PAYMENT);
            UploadPurpose.valueList = Collections.unmodifiableList(valueList);
        }
        synchronized (UploadPurpose.literals)
        {
            UploadPurpose.literals.add(INVOICE.enumValue);
            UploadPurpose.literals.add(PAYMENT.enumValue);
            UploadPurpose.literals = Collections.unmodifiableList(literals);
        }
        synchronized (UploadPurpose.names)
        {
            UploadPurpose.names.add("INVOICE");
            UploadPurpose.names.add("PAYMENT");
            UploadPurpose.names = Collections.unmodifiableList(names);
        }
    }
}