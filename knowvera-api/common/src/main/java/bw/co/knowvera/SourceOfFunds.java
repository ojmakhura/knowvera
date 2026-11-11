package bw.co.knowvera;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * TODO: Model Documentation for bw.co.knowvera.SourceOfFunds
 */
public enum SourceOfFunds
{
    /** TODO: Model Documentation for Enumeration Literal SALARY value "SALARY" */
    SALARY("SALARY"),
    /** TODO: Model Documentation for Enumeration Literal BUSINESS_INCOME value "BUSINESS_INCOME" */
    BUSINESS_INCOME("BUSINESS_INCOME"),
    /** TODO: Model Documentation for Enumeration Literal INVESTMENTS value "INVESTMENTS" */
    INVESTMENTS("INVESTMENTS"),
    /** TODO: Model Documentation for Enumeration Literal PENSIONS value "PENSIONS" */
    PENSIONS("PENSIONS"),
    /** TODO: Model Documentation for Enumeration Literal GIFTS value "GIFTS" */
    GIFTS("GIFTS"),
    /** TODO: Model Documentation for Enumeration Literal REMITTANCE value "REMITTANCE" */
    REMITTANCE("REMITTANCE"),
    /** TODO: Model Documentation for Enumeration Literal OTHER value "OTHER" */
    OTHER("OTHER");

    /**
     * The serial version UID of this class. Needed for serialization.
     */
    private static final long serialVersionUID = 8672192391878547088L;

    private final String enumValue;

    /**
     * The constructor with enumeration literal value allowing
     * super classes to access it.
     */
    private SourceOfFunds(String value)
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
     * Retrieves an instance of SourceOfFunds from <code>its name</code>.
     *
     * @param name the name to create the SourceOfFunds from.
     * @return The enumeration literal named after the 'name' argument
     */
    public static SourceOfFunds fromString(String name)
    {
        return SourceOfFunds.valueOf(name);
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
     * Returns an instance of SourceOfFunds from String <code>value</code>.
     * Required by JAXB2 enumeration implementation
     *
     * @param value the value to create the SourceOfFunds from.
     * @return static Enumeration with corresponding value
     */
    public static SourceOfFunds fromValue(String value)
    {
        // Allow either the Enum name or value string to be used to retrieve the enum
        for (SourceOfFunds enumName: SourceOfFunds.values())
        {
            if (enumName.getValue().equals(value))
            {
                return enumName;
            }
        }
        for (String enumName: SourceOfFunds.names())
        {
            if (enumName.equals(value))
            {
                return SourceOfFunds.valueOf(enumName);
            }
        }
        throw new IllegalArgumentException("SourceOfFunds.fromValue(" + value + ')');
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
        return SourceOfFunds.literals;
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
        return SourceOfFunds.names;
    }

    private static Map<String, SourceOfFunds> values = new LinkedHashMap<String, SourceOfFunds>(7, 1);
    private static List<String> literals = new ArrayList<String>(7);
    private static List<String> names = new ArrayList<String>(7);
    private static List<SourceOfFunds> valueList = new ArrayList<SourceOfFunds>(7);

    /**
     * Initializes the values.
     */
    static
    {
        synchronized (SourceOfFunds.values)
        {
            SourceOfFunds.values.put(SALARY.enumValue, SALARY);
            SourceOfFunds.values.put(BUSINESS_INCOME.enumValue, BUSINESS_INCOME);
            SourceOfFunds.values.put(INVESTMENTS.enumValue, INVESTMENTS);
            SourceOfFunds.values.put(PENSIONS.enumValue, PENSIONS);
            SourceOfFunds.values.put(GIFTS.enumValue, GIFTS);
            SourceOfFunds.values.put(REMITTANCE.enumValue, REMITTANCE);
            SourceOfFunds.values.put(OTHER.enumValue, OTHER);
        }
        synchronized (SourceOfFunds.valueList)
        {
            SourceOfFunds.valueList.add(SALARY);
            SourceOfFunds.valueList.add(BUSINESS_INCOME);
            SourceOfFunds.valueList.add(INVESTMENTS);
            SourceOfFunds.valueList.add(PENSIONS);
            SourceOfFunds.valueList.add(GIFTS);
            SourceOfFunds.valueList.add(REMITTANCE);
            SourceOfFunds.valueList.add(OTHER);
            SourceOfFunds.valueList = Collections.unmodifiableList(valueList);
        }
        synchronized (SourceOfFunds.literals)
        {
            SourceOfFunds.literals.add(SALARY.enumValue);
            SourceOfFunds.literals.add(BUSINESS_INCOME.enumValue);
            SourceOfFunds.literals.add(INVESTMENTS.enumValue);
            SourceOfFunds.literals.add(PENSIONS.enumValue);
            SourceOfFunds.literals.add(GIFTS.enumValue);
            SourceOfFunds.literals.add(REMITTANCE.enumValue);
            SourceOfFunds.literals.add(OTHER.enumValue);
            SourceOfFunds.literals = Collections.unmodifiableList(literals);
        }
        synchronized (SourceOfFunds.names)
        {
            SourceOfFunds.names.add("SALARY");
            SourceOfFunds.names.add("BUSINESS_INCOME");
            SourceOfFunds.names.add("INVESTMENTS");
            SourceOfFunds.names.add("PENSIONS");
            SourceOfFunds.names.add("GIFTS");
            SourceOfFunds.names.add("REMITTANCE");
            SourceOfFunds.names.add("OTHER");
            SourceOfFunds.names = Collections.unmodifiableList(names);
        }
    }
}