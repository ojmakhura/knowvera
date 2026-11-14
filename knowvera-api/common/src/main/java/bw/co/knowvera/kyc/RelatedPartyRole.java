package bw.co.knowvera.kyc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * TODO: Model Documentation for bw.co.knowvera.kyc.RelatedPartyRole
 */
public enum RelatedPartyRole
{
    /** TODO: Model Documentation for Enumeration Literal DIRECTOR value "DIRECTOR" */
    DIRECTOR("DIRECTOR"),
    /** TODO: Model Documentation for Enumeration Literal SHAREHOLDER value "SHAREHOLDER" */
    SHAREHOLDER("SHAREHOLDER"),
    /** TODO: Model Documentation for Enumeration Literal BENEFICIAL_OWNER value "BENEFICIAL_OWNER" */
    BENEFICIAL_OWNER("BENEFICIAL_OWNER"),
    /** TODO: Model Documentation for Enumeration Literal MANAGER value "MANAGER" */
    MANAGER("MANAGER"),
    /** TODO: Model Documentation for Enumeration Literal AUTHORISED_SIGNATORY value "AUTHORISED_SIGNATORY" */
    AUTHORISED_SIGNATORY("AUTHORISED_SIGNATORY"),
    /** TODO: Model Documentation for Enumeration Literal AGENT value "AGENT" */
    AGENT("AGENT"),
    /** TODO: Model Documentation for Enumeration Literal PARTNER value "PARTNER" */
    PARTNER("PARTNER"),
    /** TODO: Model Documentation for Enumeration Literal TRUSTEE value "TRUSTEE" */
    TRUSTEE("TRUSTEE"),
    /** TODO: Model Documentation for Enumeration Literal SETTLOR value "SETTLOR" */
    SETTLOR("SETTLOR"),
    /** TODO: Model Documentation for Enumeration Literal BENEFICIARY value "BENEFICIARY" */
    BENEFICIARY("BENEFICIARY"),
    /** TODO: Model Documentation for Enumeration Literal PROTECTOR value "PROTECTOR" */
    PROTECTOR("PROTECTOR");

    /**
     * The serial version UID of this class. Needed for serialization.
     */
    private static final long serialVersionUID = 3898020166803048181L;

    private final String enumValue;

    /**
     * The constructor with enumeration literal value allowing
     * super classes to access it.
     */
    private RelatedPartyRole(String value)
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
     * Retrieves an instance of RelatedPartyRole from <code>its name</code>.
     *
     * @param name the name to create the RelatedPartyRole from.
     * @return The enumeration literal named after the 'name' argument
     */
    public static RelatedPartyRole fromString(String name)
    {
        return RelatedPartyRole.valueOf(name);
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
     * Returns an instance of RelatedPartyRole from String <code>value</code>.
     * Required by JAXB2 enumeration implementation
     *
     * @param value the value to create the RelatedPartyRole from.
     * @return static Enumeration with corresponding value
     */
    public static RelatedPartyRole fromValue(String value)
    {
        // Allow either the Enum name or value string to be used to retrieve the enum
        for (RelatedPartyRole enumName: RelatedPartyRole.values())
        {
            if (enumName.getValue().equals(value))
            {
                return enumName;
            }
        }
        for (String enumName: RelatedPartyRole.names())
        {
            if (enumName.equals(value))
            {
                return RelatedPartyRole.valueOf(enumName);
            }
        }
        throw new IllegalArgumentException("RelatedPartyRole.fromValue(" + value + ')');
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
        return RelatedPartyRole.literals;
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
        return RelatedPartyRole.names;
    }

    private static Map<String, RelatedPartyRole> values = new LinkedHashMap<String, RelatedPartyRole>(11, 1);
    private static List<String> literals = new ArrayList<String>(11);
    private static List<String> names = new ArrayList<String>(11);
    private static List<RelatedPartyRole> valueList = new ArrayList<RelatedPartyRole>(11);

    /**
     * Initializes the values.
     */
    static
    {
        synchronized (RelatedPartyRole.values)
        {
            RelatedPartyRole.values.put(DIRECTOR.enumValue, DIRECTOR);
            RelatedPartyRole.values.put(SHAREHOLDER.enumValue, SHAREHOLDER);
            RelatedPartyRole.values.put(BENEFICIAL_OWNER.enumValue, BENEFICIAL_OWNER);
            RelatedPartyRole.values.put(MANAGER.enumValue, MANAGER);
            RelatedPartyRole.values.put(AUTHORISED_SIGNATORY.enumValue, AUTHORISED_SIGNATORY);
            RelatedPartyRole.values.put(AGENT.enumValue, AGENT);
            RelatedPartyRole.values.put(PARTNER.enumValue, PARTNER);
            RelatedPartyRole.values.put(TRUSTEE.enumValue, TRUSTEE);
            RelatedPartyRole.values.put(SETTLOR.enumValue, SETTLOR);
            RelatedPartyRole.values.put(BENEFICIARY.enumValue, BENEFICIARY);
            RelatedPartyRole.values.put(PROTECTOR.enumValue, PROTECTOR);
        }
        synchronized (RelatedPartyRole.valueList)
        {
            RelatedPartyRole.valueList.add(DIRECTOR);
            RelatedPartyRole.valueList.add(SHAREHOLDER);
            RelatedPartyRole.valueList.add(BENEFICIAL_OWNER);
            RelatedPartyRole.valueList.add(MANAGER);
            RelatedPartyRole.valueList.add(AUTHORISED_SIGNATORY);
            RelatedPartyRole.valueList.add(AGENT);
            RelatedPartyRole.valueList.add(PARTNER);
            RelatedPartyRole.valueList.add(TRUSTEE);
            RelatedPartyRole.valueList.add(SETTLOR);
            RelatedPartyRole.valueList.add(BENEFICIARY);
            RelatedPartyRole.valueList.add(PROTECTOR);
            RelatedPartyRole.valueList = Collections.unmodifiableList(valueList);
        }
        synchronized (RelatedPartyRole.literals)
        {
            RelatedPartyRole.literals.add(DIRECTOR.enumValue);
            RelatedPartyRole.literals.add(SHAREHOLDER.enumValue);
            RelatedPartyRole.literals.add(BENEFICIAL_OWNER.enumValue);
            RelatedPartyRole.literals.add(MANAGER.enumValue);
            RelatedPartyRole.literals.add(AUTHORISED_SIGNATORY.enumValue);
            RelatedPartyRole.literals.add(AGENT.enumValue);
            RelatedPartyRole.literals.add(PARTNER.enumValue);
            RelatedPartyRole.literals.add(TRUSTEE.enumValue);
            RelatedPartyRole.literals.add(SETTLOR.enumValue);
            RelatedPartyRole.literals.add(BENEFICIARY.enumValue);
            RelatedPartyRole.literals.add(PROTECTOR.enumValue);
            RelatedPartyRole.literals = Collections.unmodifiableList(literals);
        }
        synchronized (RelatedPartyRole.names)
        {
            RelatedPartyRole.names.add("DIRECTOR");
            RelatedPartyRole.names.add("SHAREHOLDER");
            RelatedPartyRole.names.add("BENEFICIAL_OWNER");
            RelatedPartyRole.names.add("MANAGER");
            RelatedPartyRole.names.add("AUTHORISED_SIGNATORY");
            RelatedPartyRole.names.add("AGENT");
            RelatedPartyRole.names.add("PARTNER");
            RelatedPartyRole.names.add("TRUSTEE");
            RelatedPartyRole.names.add("SETTLOR");
            RelatedPartyRole.names.add("BENEFICIARY");
            RelatedPartyRole.names.add("PROTECTOR");
            RelatedPartyRole.names = Collections.unmodifiableList(names);
        }
    }
}