package bw.co.knowvera;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * TODO: Model Documentation for bw.co.knowvera.SourceOfWealth
 */
public enum SourceOfWealth
{
    /** TODO: Model Documentation for Enumeration Literal EMPLOYMENT_SAVINGS value "EMPLOYMENT_SAVINGS" */
    EMPLOYMENT_SAVINGS("EMPLOYMENT_SAVINGS"),
    /** TODO: Model Documentation for Enumeration Literal BUSINESS_OWNERSHIP value "BUSINESS_OWNERSHIP" */
    BUSINESS_OWNERSHIP("BUSINESS_OWNERSHIP"),
    /** TODO: Model Documentation for Enumeration Literal INVESTMENTS value "INVESTMENTS" */
    INVESTMENTS("INVESTMENTS"),
    /** TODO: Model Documentation for Enumeration Literal INHERITANCE value "INHERITANCE" */
    INHERITANCE("INHERITANCE"),
    /** TODO: Model Documentation for Enumeration Literal PROPERTY_SALE value "PROPERTY_SALE" */
    PROPERTY_SALE("PROPERTY_SALE"),
    /** TODO: Model Documentation for Enumeration Literal PENSION value "PENSION" */
    PENSION("PENSION"),
    /** TODO: Model Documentation for Enumeration Literal GIFT value "GIFT" */
    GIFT("GIFT"),
    /** TODO: Model Documentation for Enumeration Literal OTHER value "OTHER" */
    OTHER("OTHER");

    /**
     * The serial version UID of this class. Needed for serialization.
     */
    private static final long serialVersionUID = -1916418212836584994L;

    private final String enumValue;

    /**
     * The constructor with enumeration literal value allowing
     * super classes to access it.
     */
    private SourceOfWealth(String value)
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
     * Retrieves an instance of SourceOfWealth from <code>its name</code>.
     *
     * @param name the name to create the SourceOfWealth from.
     * @return The enumeration literal named after the 'name' argument
     */
    public static SourceOfWealth fromString(String name)
    {
        return SourceOfWealth.valueOf(name);
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
     * Returns an instance of SourceOfWealth from String <code>value</code>.
     * Required by JAXB2 enumeration implementation
     *
     * @param value the value to create the SourceOfWealth from.
     * @return static Enumeration with corresponding value
     */
    public static SourceOfWealth fromValue(String value)
    {
        // Allow either the Enum name or value string to be used to retrieve the enum
        for (SourceOfWealth enumName: SourceOfWealth.values())
        {
            if (enumName.getValue().equals(value))
            {
                return enumName;
            }
        }
        for (String enumName: SourceOfWealth.names())
        {
            if (enumName.equals(value))
            {
                return SourceOfWealth.valueOf(enumName);
            }
        }
        throw new IllegalArgumentException("SourceOfWealth.fromValue(" + value + ')');
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
        return SourceOfWealth.literals;
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
        return SourceOfWealth.names;
    }

    private static Map<String, SourceOfWealth> values = new LinkedHashMap<String, SourceOfWealth>(8, 1);
    private static List<String> literals = new ArrayList<String>(8);
    private static List<String> names = new ArrayList<String>(8);
    private static List<SourceOfWealth> valueList = new ArrayList<SourceOfWealth>(8);

    /**
     * Initializes the values.
     */
    static
    {
        synchronized (SourceOfWealth.values)
        {
            SourceOfWealth.values.put(EMPLOYMENT_SAVINGS.enumValue, EMPLOYMENT_SAVINGS);
            SourceOfWealth.values.put(BUSINESS_OWNERSHIP.enumValue, BUSINESS_OWNERSHIP);
            SourceOfWealth.values.put(INVESTMENTS.enumValue, INVESTMENTS);
            SourceOfWealth.values.put(INHERITANCE.enumValue, INHERITANCE);
            SourceOfWealth.values.put(PROPERTY_SALE.enumValue, PROPERTY_SALE);
            SourceOfWealth.values.put(PENSION.enumValue, PENSION);
            SourceOfWealth.values.put(GIFT.enumValue, GIFT);
            SourceOfWealth.values.put(OTHER.enumValue, OTHER);
        }
        synchronized (SourceOfWealth.valueList)
        {
            SourceOfWealth.valueList.add(EMPLOYMENT_SAVINGS);
            SourceOfWealth.valueList.add(BUSINESS_OWNERSHIP);
            SourceOfWealth.valueList.add(INVESTMENTS);
            SourceOfWealth.valueList.add(INHERITANCE);
            SourceOfWealth.valueList.add(PROPERTY_SALE);
            SourceOfWealth.valueList.add(PENSION);
            SourceOfWealth.valueList.add(GIFT);
            SourceOfWealth.valueList.add(OTHER);
            SourceOfWealth.valueList = Collections.unmodifiableList(valueList);
        }
        synchronized (SourceOfWealth.literals)
        {
            SourceOfWealth.literals.add(EMPLOYMENT_SAVINGS.enumValue);
            SourceOfWealth.literals.add(BUSINESS_OWNERSHIP.enumValue);
            SourceOfWealth.literals.add(INVESTMENTS.enumValue);
            SourceOfWealth.literals.add(INHERITANCE.enumValue);
            SourceOfWealth.literals.add(PROPERTY_SALE.enumValue);
            SourceOfWealth.literals.add(PENSION.enumValue);
            SourceOfWealth.literals.add(GIFT.enumValue);
            SourceOfWealth.literals.add(OTHER.enumValue);
            SourceOfWealth.literals = Collections.unmodifiableList(literals);
        }
        synchronized (SourceOfWealth.names)
        {
            SourceOfWealth.names.add("EMPLOYMENT_SAVINGS");
            SourceOfWealth.names.add("BUSINESS_OWNERSHIP");
            SourceOfWealth.names.add("INVESTMENTS");
            SourceOfWealth.names.add("INHERITANCE");
            SourceOfWealth.names.add("PROPERTY_SALE");
            SourceOfWealth.names.add("PENSION");
            SourceOfWealth.names.add("GIFT");
            SourceOfWealth.names.add("OTHER");
            SourceOfWealth.names = Collections.unmodifiableList(names);
        }
    }
}