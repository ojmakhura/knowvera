package bw.co.knowvera;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * TODO: Model Documentation for bw.co.knowvera.TargetEntity
 */
public enum TargetEntity
{
    /** TODO: Model Documentation for Enumeration Literal INDIVIDUAL value "INDIVIDUAL" */
    INDIVIDUAL("INDIVIDUAL"),
    /** TODO: Model Documentation for Enumeration Literal ORGANISATION value "ORGANISATION" */
    ORGANISATION("ORGANISATION"),
    /** TODO: Model Documentation for Enumeration Literal BRANCH value "BRANCH" */
    BRANCH("BRANCH"),
    /** TODO: Model Documentation for Enumeration Literal SUBSCRIPTION value "SUBSCRIPTION" */
    SUBSCRIPTION("SUBSCRIPTION"),
    /** TODO: Model Documentation for Enumeration Literal INVOICE value "INVOICE" */
    INVOICE("INVOICE"),
    /** TODO: Model Documentation for Enumeration Literal QUOTATION value "QUOTATION" */
    QUOTATION("QUOTATION"),
    /** TODO: Model Documentation for Enumeration Literal CLIENT_REQUEST value "CLIENT_REQUEST" */
    CLIENT_REQUEST("CLIENT_REQUEST"),
    /** TODO: Model Documentation for Enumeration Literal KYC_RECORD value "KYC_RECORD" */
    KYC_RECORD("KYC_RECORD"),
    /** TODO: Model Documentation for Enumeration Literal CONTACT value "CONTACT" */
    CONTACT("CONTACT"),
    /** TODO: Model Documentation for Enumeration Literal SETTINGS value "SETTINGS" */
    SETTINGS("SETTINGS"),
    /** TODO: Model Documentation for Enumeration Literal DOCUMENT value "DOCUMENT" */
    DOCUMENT("DOCUMENT");

    /**
     * The serial version UID of this class. Needed for serialization.
     */
    private static final long serialVersionUID = -7931245668981635031L;

    private final String enumValue;

    /**
     * The constructor with enumeration literal value allowing
     * super classes to access it.
     */
    private TargetEntity(String value)
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
     * Retrieves an instance of TargetEntity from <code>its name</code>.
     *
     * @param name the name to create the TargetEntity from.
     * @return The enumeration literal named after the 'name' argument
     */
    public static TargetEntity fromString(String name)
    {
        return TargetEntity.valueOf(name);
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
     * Returns an instance of TargetEntity from String <code>value</code>.
     * Required by JAXB2 enumeration implementation
     *
     * @param value the value to create the TargetEntity from.
     * @return static Enumeration with corresponding value
     */
    public static TargetEntity fromValue(String value)
    {
        // Allow either the Enum name or value string to be used to retrieve the enum
        for (TargetEntity enumName: TargetEntity.values())
        {
            if (enumName.getValue().equals(value))
            {
                return enumName;
            }
        }
        for (String enumName: TargetEntity.names())
        {
            if (enumName.equals(value))
            {
                return TargetEntity.valueOf(enumName);
            }
        }
        throw new IllegalArgumentException("TargetEntity.fromValue(" + value + ')');
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
        return TargetEntity.literals;
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
        return TargetEntity.names;
    }

    private static Map<String, TargetEntity> values = new LinkedHashMap<String, TargetEntity>(11, 1);
    private static List<String> literals = new ArrayList<String>(11);
    private static List<String> names = new ArrayList<String>(11);
    private static List<TargetEntity> valueList = new ArrayList<TargetEntity>(11);

    /**
     * Initializes the values.
     */
    static
    {
        synchronized (TargetEntity.values)
        {
            TargetEntity.values.put(INDIVIDUAL.enumValue, INDIVIDUAL);
            TargetEntity.values.put(ORGANISATION.enumValue, ORGANISATION);
            TargetEntity.values.put(BRANCH.enumValue, BRANCH);
            TargetEntity.values.put(SUBSCRIPTION.enumValue, SUBSCRIPTION);
            TargetEntity.values.put(INVOICE.enumValue, INVOICE);
            TargetEntity.values.put(QUOTATION.enumValue, QUOTATION);
            TargetEntity.values.put(CLIENT_REQUEST.enumValue, CLIENT_REQUEST);
            TargetEntity.values.put(KYC_RECORD.enumValue, KYC_RECORD);
            TargetEntity.values.put(CONTACT.enumValue, CONTACT);
            TargetEntity.values.put(SETTINGS.enumValue, SETTINGS);
            TargetEntity.values.put(DOCUMENT.enumValue, DOCUMENT);
        }
        synchronized (TargetEntity.valueList)
        {
            TargetEntity.valueList.add(INDIVIDUAL);
            TargetEntity.valueList.add(ORGANISATION);
            TargetEntity.valueList.add(BRANCH);
            TargetEntity.valueList.add(SUBSCRIPTION);
            TargetEntity.valueList.add(INVOICE);
            TargetEntity.valueList.add(QUOTATION);
            TargetEntity.valueList.add(CLIENT_REQUEST);
            TargetEntity.valueList.add(KYC_RECORD);
            TargetEntity.valueList.add(CONTACT);
            TargetEntity.valueList.add(SETTINGS);
            TargetEntity.valueList.add(DOCUMENT);
            TargetEntity.valueList = Collections.unmodifiableList(valueList);
        }
        synchronized (TargetEntity.literals)
        {
            TargetEntity.literals.add(INDIVIDUAL.enumValue);
            TargetEntity.literals.add(ORGANISATION.enumValue);
            TargetEntity.literals.add(BRANCH.enumValue);
            TargetEntity.literals.add(SUBSCRIPTION.enumValue);
            TargetEntity.literals.add(INVOICE.enumValue);
            TargetEntity.literals.add(QUOTATION.enumValue);
            TargetEntity.literals.add(CLIENT_REQUEST.enumValue);
            TargetEntity.literals.add(KYC_RECORD.enumValue);
            TargetEntity.literals.add(CONTACT.enumValue);
            TargetEntity.literals.add(SETTINGS.enumValue);
            TargetEntity.literals.add(DOCUMENT.enumValue);
            TargetEntity.literals = Collections.unmodifiableList(literals);
        }
        synchronized (TargetEntity.names)
        {
            TargetEntity.names.add("INDIVIDUAL");
            TargetEntity.names.add("ORGANISATION");
            TargetEntity.names.add("BRANCH");
            TargetEntity.names.add("SUBSCRIPTION");
            TargetEntity.names.add("INVOICE");
            TargetEntity.names.add("QUOTATION");
            TargetEntity.names.add("CLIENT_REQUEST");
            TargetEntity.names.add("KYC_RECORD");
            TargetEntity.names.add("CONTACT");
            TargetEntity.names.add("SETTINGS");
            TargetEntity.names.add("DOCUMENT");
            TargetEntity.names = Collections.unmodifiableList(names);
        }
    }
}