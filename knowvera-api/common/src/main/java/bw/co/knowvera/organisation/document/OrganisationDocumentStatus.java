package bw.co.knowvera.organisation.document;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * TODO: Model Documentation for bw.co.knowvera.organisation.document.OrganisationDocumentStatus
 */
public enum OrganisationDocumentStatus
{
    /** TODO: Model Documentation for Enumeration Literal ACTIVE value "ACTIVE" */
    ACTIVE("ACTIVE"),
    /** TODO: Model Documentation for Enumeration Literal INACTIVE value "INACTIVE" */
    INACTIVE("INACTIVE");

    /**
     * The serial version UID of this class. Needed for serialization.
     */
    private static final long serialVersionUID = 9015767844806726743L;

    private final String enumValue;

    /**
     * The constructor with enumeration literal value allowing
     * super classes to access it.
     */
    private OrganisationDocumentStatus(String value)
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
     * Retrieves an instance of OrganisationDocumentStatus from <code>its name</code>.
     *
     * @param name the name to create the OrganisationDocumentStatus from.
     * @return The enumeration literal named after the 'name' argument
     */
    public static OrganisationDocumentStatus fromString(String name)
    {
        return OrganisationDocumentStatus.valueOf(name);
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
     * Returns an instance of OrganisationDocumentStatus from String <code>value</code>.
     * Required by JAXB2 enumeration implementation
     *
     * @param value the value to create the OrganisationDocumentStatus from.
     * @return static Enumeration with corresponding value
     */
    public static OrganisationDocumentStatus fromValue(String value)
    {
        // Allow either the Enum name or value string to be used to retrieve the enum
        for (OrganisationDocumentStatus enumName: OrganisationDocumentStatus.values())
        {
            if (enumName.getValue().equals(value))
            {
                return enumName;
            }
        }
        for (String enumName: OrganisationDocumentStatus.names())
        {
            if (enumName.equals(value))
            {
                return OrganisationDocumentStatus.valueOf(enumName);
            }
        }
        throw new IllegalArgumentException("OrganisationDocumentStatus.fromValue(" + value + ')');
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
        return OrganisationDocumentStatus.literals;
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
        return OrganisationDocumentStatus.names;
    }

    private static Map<String, OrganisationDocumentStatus> values = new LinkedHashMap<String, OrganisationDocumentStatus>(2, 1);
    private static List<String> literals = new ArrayList<String>(2);
    private static List<String> names = new ArrayList<String>(2);
    private static List<OrganisationDocumentStatus> valueList = new ArrayList<OrganisationDocumentStatus>(2);

    /**
     * Initializes the values.
     */
    static
    {
        synchronized (OrganisationDocumentStatus.values)
        {
            OrganisationDocumentStatus.values.put(ACTIVE.enumValue, ACTIVE);
            OrganisationDocumentStatus.values.put(INACTIVE.enumValue, INACTIVE);
        }
        synchronized (OrganisationDocumentStatus.valueList)
        {
            OrganisationDocumentStatus.valueList.add(ACTIVE);
            OrganisationDocumentStatus.valueList.add(INACTIVE);
            OrganisationDocumentStatus.valueList = Collections.unmodifiableList(valueList);
        }
        synchronized (OrganisationDocumentStatus.literals)
        {
            OrganisationDocumentStatus.literals.add(ACTIVE.enumValue);
            OrganisationDocumentStatus.literals.add(INACTIVE.enumValue);
            OrganisationDocumentStatus.literals = Collections.unmodifiableList(literals);
        }
        synchronized (OrganisationDocumentStatus.names)
        {
            OrganisationDocumentStatus.names.add("ACTIVE");
            OrganisationDocumentStatus.names.add("INACTIVE");
            OrganisationDocumentStatus.names = Collections.unmodifiableList(names);
        }
    }
}