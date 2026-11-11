package bw.co.knowvera.organisation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * TODO: Model Documentation for bw.co.knowvera.organisation.OrganisationLegalForm
 */
public enum OrganisationLegalForm
{
    /** TODO: Model Documentation for Enumeration Literal PRIVATE_COMPANY value "PRIVATE_COMPANY" */
    PRIVATE_COMPANY("PRIVATE_COMPANY"),
    /** TODO: Model Documentation for Enumeration Literal PUBLIC_COMPANY value "PUBLIC_COMPANY" */
    PUBLIC_COMPANY("PUBLIC_COMPANY"),
    /** TODO: Model Documentation for Enumeration Literal CLOSE_COMPANY value "CLOSE_COMPANY" */
    CLOSE_COMPANY("CLOSE_COMPANY"),
    /** TODO: Model Documentation for Enumeration Literal PARTNERSHIP value "PARTNERSHIP" */
    PARTNERSHIP("PARTNERSHIP"),
    /** TODO: Model Documentation for Enumeration Literal TRUST value "TRUST" */
    TRUST("TRUST"),
    /** TODO: Model Documentation for Enumeration Literal SOCIETY value "SOCIETY" */
    SOCIETY("SOCIETY"),
    /** TODO: Model Documentation for Enumeration Literal SOLE_PROPRIETOR value "SOLE_PROPRIETOR" */
    SOLE_PROPRIETOR("SOLE_PROPRIETOR"),
    /** TODO: Model Documentation for Enumeration Literal OTHER value "OTHER" */
    OTHER("OTHER");

    /**
     * The serial version UID of this class. Needed for serialization.
     */
    private static final long serialVersionUID = 525506564093405842L;

    private final String enumValue;

    /**
     * The constructor with enumeration literal value allowing
     * super classes to access it.
     */
    private OrganisationLegalForm(String value)
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
     * Retrieves an instance of OrganisationLegalForm from <code>its name</code>.
     *
     * @param name the name to create the OrganisationLegalForm from.
     * @return The enumeration literal named after the 'name' argument
     */
    public static OrganisationLegalForm fromString(String name)
    {
        return OrganisationLegalForm.valueOf(name);
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
     * Returns an instance of OrganisationLegalForm from String <code>value</code>.
     * Required by JAXB2 enumeration implementation
     *
     * @param value the value to create the OrganisationLegalForm from.
     * @return static Enumeration with corresponding value
     */
    public static OrganisationLegalForm fromValue(String value)
    {
        // Allow either the Enum name or value string to be used to retrieve the enum
        for (OrganisationLegalForm enumName: OrganisationLegalForm.values())
        {
            if (enumName.getValue().equals(value))
            {
                return enumName;
            }
        }
        for (String enumName: OrganisationLegalForm.names())
        {
            if (enumName.equals(value))
            {
                return OrganisationLegalForm.valueOf(enumName);
            }
        }
        throw new IllegalArgumentException("OrganisationLegalForm.fromValue(" + value + ')');
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
        return OrganisationLegalForm.literals;
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
        return OrganisationLegalForm.names;
    }

    private static Map<String, OrganisationLegalForm> values = new LinkedHashMap<String, OrganisationLegalForm>(8, 1);
    private static List<String> literals = new ArrayList<String>(8);
    private static List<String> names = new ArrayList<String>(8);
    private static List<OrganisationLegalForm> valueList = new ArrayList<OrganisationLegalForm>(8);

    /**
     * Initializes the values.
     */
    static
    {
        synchronized (OrganisationLegalForm.values)
        {
            OrganisationLegalForm.values.put(PRIVATE_COMPANY.enumValue, PRIVATE_COMPANY);
            OrganisationLegalForm.values.put(PUBLIC_COMPANY.enumValue, PUBLIC_COMPANY);
            OrganisationLegalForm.values.put(CLOSE_COMPANY.enumValue, CLOSE_COMPANY);
            OrganisationLegalForm.values.put(PARTNERSHIP.enumValue, PARTNERSHIP);
            OrganisationLegalForm.values.put(TRUST.enumValue, TRUST);
            OrganisationLegalForm.values.put(SOCIETY.enumValue, SOCIETY);
            OrganisationLegalForm.values.put(SOLE_PROPRIETOR.enumValue, SOLE_PROPRIETOR);
            OrganisationLegalForm.values.put(OTHER.enumValue, OTHER);
        }
        synchronized (OrganisationLegalForm.valueList)
        {
            OrganisationLegalForm.valueList.add(PRIVATE_COMPANY);
            OrganisationLegalForm.valueList.add(PUBLIC_COMPANY);
            OrganisationLegalForm.valueList.add(CLOSE_COMPANY);
            OrganisationLegalForm.valueList.add(PARTNERSHIP);
            OrganisationLegalForm.valueList.add(TRUST);
            OrganisationLegalForm.valueList.add(SOCIETY);
            OrganisationLegalForm.valueList.add(SOLE_PROPRIETOR);
            OrganisationLegalForm.valueList.add(OTHER);
            OrganisationLegalForm.valueList = Collections.unmodifiableList(valueList);
        }
        synchronized (OrganisationLegalForm.literals)
        {
            OrganisationLegalForm.literals.add(PRIVATE_COMPANY.enumValue);
            OrganisationLegalForm.literals.add(PUBLIC_COMPANY.enumValue);
            OrganisationLegalForm.literals.add(CLOSE_COMPANY.enumValue);
            OrganisationLegalForm.literals.add(PARTNERSHIP.enumValue);
            OrganisationLegalForm.literals.add(TRUST.enumValue);
            OrganisationLegalForm.literals.add(SOCIETY.enumValue);
            OrganisationLegalForm.literals.add(SOLE_PROPRIETOR.enumValue);
            OrganisationLegalForm.literals.add(OTHER.enumValue);
            OrganisationLegalForm.literals = Collections.unmodifiableList(literals);
        }
        synchronized (OrganisationLegalForm.names)
        {
            OrganisationLegalForm.names.add("PRIVATE_COMPANY");
            OrganisationLegalForm.names.add("PUBLIC_COMPANY");
            OrganisationLegalForm.names.add("CLOSE_COMPANY");
            OrganisationLegalForm.names.add("PARTNERSHIP");
            OrganisationLegalForm.names.add("TRUST");
            OrganisationLegalForm.names.add("SOCIETY");
            OrganisationLegalForm.names.add("SOLE_PROPRIETOR");
            OrganisationLegalForm.names.add("OTHER");
            OrganisationLegalForm.names = Collections.unmodifiableList(names);
        }
    }
}