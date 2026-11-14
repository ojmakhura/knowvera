package bw.co.knowvera.settings;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * TODO: Model Documentation for bw.co.knowvera.settings.DocumentTypePurpose
 */
public enum DocumentTypePurpose
{
    /** TODO: Model Documentation for Enumeration Literal ORGANISATION value "ORGANISATION" */
    ORGANISATION("ORGANISATION"),
    /** TODO: Model Documentation for Enumeration Literal ORGANISATION_KYC value "ORGANISATION_KYC" */
    ORGANISATION_KYC("ORGANISATION_KYC"),
    /** TODO: Model Documentation for Enumeration Literal INDIVIDUAL value "INDIVIDUAL" */
    INDIVIDUAL("INDIVIDUAL"),
    /** TODO: Model Documentation for Enumeration Literal INDIVIDUAL_KYC value "INDIVIDUAL_KYC" */
    INDIVIDUAL_KYC("INDIVIDUAL_KYC");

    /**
     * The serial version UID of this class. Needed for serialization.
     */
    private static final long serialVersionUID = 6998448788349825874L;

    private final String enumValue;

    /**
     * The constructor with enumeration literal value allowing
     * super classes to access it.
     */
    private DocumentTypePurpose(String value)
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
     * Retrieves an instance of DocumentTypePurpose from <code>its name</code>.
     *
     * @param name the name to create the DocumentTypePurpose from.
     * @return The enumeration literal named after the 'name' argument
     */
    public static DocumentTypePurpose fromString(String name)
    {
        return DocumentTypePurpose.valueOf(name);
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
     * Returns an instance of DocumentTypePurpose from String <code>value</code>.
     * Required by JAXB2 enumeration implementation
     *
     * @param value the value to create the DocumentTypePurpose from.
     * @return static Enumeration with corresponding value
     */
    public static DocumentTypePurpose fromValue(String value)
    {
        // Allow either the Enum name or value string to be used to retrieve the enum
        for (DocumentTypePurpose enumName: DocumentTypePurpose.values())
        {
            if (enumName.getValue().equals(value))
            {
                return enumName;
            }
        }
        for (String enumName: DocumentTypePurpose.names())
        {
            if (enumName.equals(value))
            {
                return DocumentTypePurpose.valueOf(enumName);
            }
        }
        throw new IllegalArgumentException("DocumentTypePurpose.fromValue(" + value + ')');
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
        return DocumentTypePurpose.literals;
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
        return DocumentTypePurpose.names;
    }

    private static Map<String, DocumentTypePurpose> values = new LinkedHashMap<String, DocumentTypePurpose>(4, 1);
    private static List<String> literals = new ArrayList<String>(4);
    private static List<String> names = new ArrayList<String>(4);
    private static List<DocumentTypePurpose> valueList = new ArrayList<DocumentTypePurpose>(4);

    /**
     * Initializes the values.
     */
    static
    {
        synchronized (DocumentTypePurpose.values)
        {
            DocumentTypePurpose.values.put(ORGANISATION.enumValue, ORGANISATION);
            DocumentTypePurpose.values.put(ORGANISATION_KYC.enumValue, ORGANISATION_KYC);
            DocumentTypePurpose.values.put(INDIVIDUAL.enumValue, INDIVIDUAL);
            DocumentTypePurpose.values.put(INDIVIDUAL_KYC.enumValue, INDIVIDUAL_KYC);
        }
        synchronized (DocumentTypePurpose.valueList)
        {
            DocumentTypePurpose.valueList.add(ORGANISATION);
            DocumentTypePurpose.valueList.add(ORGANISATION_KYC);
            DocumentTypePurpose.valueList.add(INDIVIDUAL);
            DocumentTypePurpose.valueList.add(INDIVIDUAL_KYC);
            DocumentTypePurpose.valueList = Collections.unmodifiableList(valueList);
        }
        synchronized (DocumentTypePurpose.literals)
        {
            DocumentTypePurpose.literals.add(ORGANISATION.enumValue);
            DocumentTypePurpose.literals.add(ORGANISATION_KYC.enumValue);
            DocumentTypePurpose.literals.add(INDIVIDUAL.enumValue);
            DocumentTypePurpose.literals.add(INDIVIDUAL_KYC.enumValue);
            DocumentTypePurpose.literals = Collections.unmodifiableList(literals);
        }
        synchronized (DocumentTypePurpose.names)
        {
            DocumentTypePurpose.names.add("ORGANISATION");
            DocumentTypePurpose.names.add("ORGANISATION_KYC");
            DocumentTypePurpose.names.add("INDIVIDUAL");
            DocumentTypePurpose.names.add("INDIVIDUAL_KYC");
            DocumentTypePurpose.names = Collections.unmodifiableList(names);
        }
    }
}