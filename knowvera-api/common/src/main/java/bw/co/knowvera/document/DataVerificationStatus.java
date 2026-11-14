package bw.co.knowvera.document;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * TODO: Model Documentation for bw.co.knowvera.document.DataVerificationStatus
 */
public enum DataVerificationStatus
{
    /** TODO: Model Documentation for Enumeration Literal UNVERIFIED value "UNVERIFIED" */
    UNVERIFIED("UNVERIFIED"),
    /** TODO: Model Documentation for Enumeration Literal VERIFIED value "VERIFIED" */
    VERIFIED("VERIFIED"),
    /** TODO: Model Documentation for Enumeration Literal VERIFICATION_FAILED value "VERIFICATION_FAILED" */
    VERIFICATION_FAILED("VERIFICATION_FAILED");

    /**
     * The serial version UID of this class. Needed for serialization.
     */
    private static final long serialVersionUID = 5836254477723932682L;

    private final String enumValue;

    /**
     * The constructor with enumeration literal value allowing
     * super classes to access it.
     */
    private DataVerificationStatus(String value)
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
     * Retrieves an instance of DataVerificationStatus from <code>its name</code>.
     *
     * @param name the name to create the DataVerificationStatus from.
     * @return The enumeration literal named after the 'name' argument
     */
    public static DataVerificationStatus fromString(String name)
    {
        return DataVerificationStatus.valueOf(name);
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
     * Returns an instance of DataVerificationStatus from String <code>value</code>.
     * Required by JAXB2 enumeration implementation
     *
     * @param value the value to create the DataVerificationStatus from.
     * @return static Enumeration with corresponding value
     */
    public static DataVerificationStatus fromValue(String value)
    {
        // Allow either the Enum name or value string to be used to retrieve the enum
        for (DataVerificationStatus enumName: DataVerificationStatus.values())
        {
            if (enumName.getValue().equals(value))
            {
                return enumName;
            }
        }
        for (String enumName: DataVerificationStatus.names())
        {
            if (enumName.equals(value))
            {
                return DataVerificationStatus.valueOf(enumName);
            }
        }
        throw new IllegalArgumentException("DataVerificationStatus.fromValue(" + value + ')');
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
        return DataVerificationStatus.literals;
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
        return DataVerificationStatus.names;
    }

    private static Map<String, DataVerificationStatus> values = new LinkedHashMap<String, DataVerificationStatus>(3, 1);
    private static List<String> literals = new ArrayList<String>(3);
    private static List<String> names = new ArrayList<String>(3);
    private static List<DataVerificationStatus> valueList = new ArrayList<DataVerificationStatus>(3);

    /**
     * Initializes the values.
     */
    static
    {
        synchronized (DataVerificationStatus.values)
        {
            DataVerificationStatus.values.put(UNVERIFIED.enumValue, UNVERIFIED);
            DataVerificationStatus.values.put(VERIFIED.enumValue, VERIFIED);
            DataVerificationStatus.values.put(VERIFICATION_FAILED.enumValue, VERIFICATION_FAILED);
        }
        synchronized (DataVerificationStatus.valueList)
        {
            DataVerificationStatus.valueList.add(UNVERIFIED);
            DataVerificationStatus.valueList.add(VERIFIED);
            DataVerificationStatus.valueList.add(VERIFICATION_FAILED);
            DataVerificationStatus.valueList = Collections.unmodifiableList(valueList);
        }
        synchronized (DataVerificationStatus.literals)
        {
            DataVerificationStatus.literals.add(UNVERIFIED.enumValue);
            DataVerificationStatus.literals.add(VERIFIED.enumValue);
            DataVerificationStatus.literals.add(VERIFICATION_FAILED.enumValue);
            DataVerificationStatus.literals = Collections.unmodifiableList(literals);
        }
        synchronized (DataVerificationStatus.names)
        {
            DataVerificationStatus.names.add("UNVERIFIED");
            DataVerificationStatus.names.add("VERIFIED");
            DataVerificationStatus.names.add("VERIFICATION_FAILED");
            DataVerificationStatus.names = Collections.unmodifiableList(names);
        }
    }
}