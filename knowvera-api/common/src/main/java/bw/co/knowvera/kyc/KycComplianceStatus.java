package bw.co.knowvera.kyc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * TODO: Model Documentation for bw.co.knowvera.kyc.KycComplianceStatus
 */
public enum KycComplianceStatus
{
    /** TODO: Model Documentation for Enumeration Literal CURRENT value "CURRENT" */
    CURRENT("CURRENT"),
    /** TODO: Model Documentation for Enumeration Literal EXPIRED value "EXPIRED" */
    EXPIRED("EXPIRED"),
    /** TODO: Model Documentation for Enumeration Literal ABSENT value "ABSENT" */
    ABSENT("ABSENT"),
    /** TODO: Model Documentation for Enumeration Literal INCOMPLETE value "INCOMPLETE" */
    INCOMPLETE("INCOMPLETE"),
    /** TODO: Model Documentation for Enumeration Literal DOCUMENT_VERIFICATION_FAILED value "DOCUMENT_VERIFICATION_FAILED" */
    DOCUMENT_VERIFICATION_FAILED("DOCUMENT_VERIFICATION_FAILED"),
    /** TODO: Model Documentation for Enumeration Literal PENDING_APPROVAL value "PENDING_APPROVAL" */
    PENDING_APPROVAL("PENDING_APPROVAL");

    /**
     * The serial version UID of this class. Needed for serialization.
     */
    private static final long serialVersionUID = -2231717261194585196L;

    private final String enumValue;

    /**
     * The constructor with enumeration literal value allowing
     * super classes to access it.
     */
    private KycComplianceStatus(String value)
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
     * Retrieves an instance of KycComplianceStatus from <code>its name</code>.
     *
     * @param name the name to create the KycComplianceStatus from.
     * @return The enumeration literal named after the 'name' argument
     */
    public static KycComplianceStatus fromString(String name)
    {
        return KycComplianceStatus.valueOf(name);
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
     * Returns an instance of KycComplianceStatus from String <code>value</code>.
     * Required by JAXB2 enumeration implementation
     *
     * @param value the value to create the KycComplianceStatus from.
     * @return static Enumeration with corresponding value
     */
    public static KycComplianceStatus fromValue(String value)
    {
        // Allow either the Enum name or value string to be used to retrieve the enum
        for (KycComplianceStatus enumName: KycComplianceStatus.values())
        {
            if (enumName.getValue().equals(value))
            {
                return enumName;
            }
        }
        for (String enumName: KycComplianceStatus.names())
        {
            if (enumName.equals(value))
            {
                return KycComplianceStatus.valueOf(enumName);
            }
        }
        throw new IllegalArgumentException("KycComplianceStatus.fromValue(" + value + ')');
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
        return KycComplianceStatus.literals;
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
        return KycComplianceStatus.names;
    }

    private static Map<String, KycComplianceStatus> values = new LinkedHashMap<String, KycComplianceStatus>(6, 1);
    private static List<String> literals = new ArrayList<String>(6);
    private static List<String> names = new ArrayList<String>(6);
    private static List<KycComplianceStatus> valueList = new ArrayList<KycComplianceStatus>(6);

    /**
     * Initializes the values.
     */
    static
    {
        synchronized (KycComplianceStatus.values)
        {
            KycComplianceStatus.values.put(CURRENT.enumValue, CURRENT);
            KycComplianceStatus.values.put(EXPIRED.enumValue, EXPIRED);
            KycComplianceStatus.values.put(ABSENT.enumValue, ABSENT);
            KycComplianceStatus.values.put(INCOMPLETE.enumValue, INCOMPLETE);
            KycComplianceStatus.values.put(DOCUMENT_VERIFICATION_FAILED.enumValue, DOCUMENT_VERIFICATION_FAILED);
            KycComplianceStatus.values.put(PENDING_APPROVAL.enumValue, PENDING_APPROVAL);
        }
        synchronized (KycComplianceStatus.valueList)
        {
            KycComplianceStatus.valueList.add(CURRENT);
            KycComplianceStatus.valueList.add(EXPIRED);
            KycComplianceStatus.valueList.add(ABSENT);
            KycComplianceStatus.valueList.add(INCOMPLETE);
            KycComplianceStatus.valueList.add(DOCUMENT_VERIFICATION_FAILED);
            KycComplianceStatus.valueList.add(PENDING_APPROVAL);
            KycComplianceStatus.valueList = Collections.unmodifiableList(valueList);
        }
        synchronized (KycComplianceStatus.literals)
        {
            KycComplianceStatus.literals.add(CURRENT.enumValue);
            KycComplianceStatus.literals.add(EXPIRED.enumValue);
            KycComplianceStatus.literals.add(ABSENT.enumValue);
            KycComplianceStatus.literals.add(INCOMPLETE.enumValue);
            KycComplianceStatus.literals.add(DOCUMENT_VERIFICATION_FAILED.enumValue);
            KycComplianceStatus.literals.add(PENDING_APPROVAL.enumValue);
            KycComplianceStatus.literals = Collections.unmodifiableList(literals);
        }
        synchronized (KycComplianceStatus.names)
        {
            KycComplianceStatus.names.add("CURRENT");
            KycComplianceStatus.names.add("EXPIRED");
            KycComplianceStatus.names.add("ABSENT");
            KycComplianceStatus.names.add("INCOMPLETE");
            KycComplianceStatus.names.add("DOCUMENT_VERIFICATION_FAILED");
            KycComplianceStatus.names.add("PENDING_APPROVAL");
            KycComplianceStatus.names = Collections.unmodifiableList(names);
        }
    }
}