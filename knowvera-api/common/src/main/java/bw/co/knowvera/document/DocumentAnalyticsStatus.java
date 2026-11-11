package bw.co.knowvera.document;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * TODO: Model Documentation for bw.co.knowvera.document.DocumentAnalyticsStatus
 */
public enum DocumentAnalyticsStatus
{
    /** TODO: Model Documentation for Enumeration Literal INITIALISED value "INITIALISED" */
    INITIALISED("INITIALISED"),
    /** TODO: Model Documentation for Enumeration Literal TEXT_EXTRACTION_COMPLETE value "TEXT_EXTRACTION_COMPLETE" */
    TEXT_EXTRACTION_COMPLETE("TEXT_EXTRACTION_COMPLETE"),
    /** TODO: Model Documentation for Enumeration Literal TYPE_CONFIRMATION_COMPLETE value "TYPE_CONFIRMATION_COMPLETE" */
    TYPE_CONFIRMATION_COMPLETE("TYPE_CONFIRMATION_COMPLETE"),
    /** TODO: Model Documentation for Enumeration Literal INFORMATION_EXTRACTION_COMPLETE value "INFORMATION_EXTRACTION_COMPLETE" */
    INFORMATION_EXTRACTION_COMPLETE("INFORMATION_EXTRACTION_COMPLETE"),
    /** TODO: Model Documentation for Enumeration Literal INFORMATION_CONFIRMATION_COMPLETE value "INFORMATION_CONFIRMATION_COMPLETE" */
    INFORMATION_CONFIRMATION_COMPLETE("INFORMATION_CONFIRMATION_COMPLETE"),
    /** TODO: Model Documentation for Enumeration Literal TEXT_CLEANUP_COMPLETE value "TEXT_CLEANUP_COMPLETE" */
    TEXT_CLEANUP_COMPLETE("TEXT_CLEANUP_COMPLETE");

    /**
     * The serial version UID of this class. Needed for serialization.
     */
    private static final long serialVersionUID = -7445859881336776775L;

    private final String enumValue;

    /**
     * The constructor with enumeration literal value allowing
     * super classes to access it.
     */
    private DocumentAnalyticsStatus(String value)
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
     * Retrieves an instance of DocumentAnalyticsStatus from <code>its name</code>.
     *
     * @param name the name to create the DocumentAnalyticsStatus from.
     * @return The enumeration literal named after the 'name' argument
     */
    public static DocumentAnalyticsStatus fromString(String name)
    {
        return DocumentAnalyticsStatus.valueOf(name);
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
     * Returns an instance of DocumentAnalyticsStatus from String <code>value</code>.
     * Required by JAXB2 enumeration implementation
     *
     * @param value the value to create the DocumentAnalyticsStatus from.
     * @return static Enumeration with corresponding value
     */
    public static DocumentAnalyticsStatus fromValue(String value)
    {
        // Allow either the Enum name or value string to be used to retrieve the enum
        for (DocumentAnalyticsStatus enumName: DocumentAnalyticsStatus.values())
        {
            if (enumName.getValue().equals(value))
            {
                return enumName;
            }
        }
        for (String enumName: DocumentAnalyticsStatus.names())
        {
            if (enumName.equals(value))
            {
                return DocumentAnalyticsStatus.valueOf(enumName);
            }
        }
        throw new IllegalArgumentException("DocumentAnalyticsStatus.fromValue(" + value + ')');
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
        return DocumentAnalyticsStatus.literals;
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
        return DocumentAnalyticsStatus.names;
    }

    private static Map<String, DocumentAnalyticsStatus> values = new LinkedHashMap<String, DocumentAnalyticsStatus>(6, 1);
    private static List<String> literals = new ArrayList<String>(6);
    private static List<String> names = new ArrayList<String>(6);
    private static List<DocumentAnalyticsStatus> valueList = new ArrayList<DocumentAnalyticsStatus>(6);

    /**
     * Initializes the values.
     */
    static
    {
        synchronized (DocumentAnalyticsStatus.values)
        {
            DocumentAnalyticsStatus.values.put(INITIALISED.enumValue, INITIALISED);
            DocumentAnalyticsStatus.values.put(TEXT_EXTRACTION_COMPLETE.enumValue, TEXT_EXTRACTION_COMPLETE);
            DocumentAnalyticsStatus.values.put(TYPE_CONFIRMATION_COMPLETE.enumValue, TYPE_CONFIRMATION_COMPLETE);
            DocumentAnalyticsStatus.values.put(INFORMATION_EXTRACTION_COMPLETE.enumValue, INFORMATION_EXTRACTION_COMPLETE);
            DocumentAnalyticsStatus.values.put(INFORMATION_CONFIRMATION_COMPLETE.enumValue, INFORMATION_CONFIRMATION_COMPLETE);
            DocumentAnalyticsStatus.values.put(TEXT_CLEANUP_COMPLETE.enumValue, TEXT_CLEANUP_COMPLETE);
        }
        synchronized (DocumentAnalyticsStatus.valueList)
        {
            DocumentAnalyticsStatus.valueList.add(INITIALISED);
            DocumentAnalyticsStatus.valueList.add(TEXT_EXTRACTION_COMPLETE);
            DocumentAnalyticsStatus.valueList.add(TYPE_CONFIRMATION_COMPLETE);
            DocumentAnalyticsStatus.valueList.add(INFORMATION_EXTRACTION_COMPLETE);
            DocumentAnalyticsStatus.valueList.add(INFORMATION_CONFIRMATION_COMPLETE);
            DocumentAnalyticsStatus.valueList.add(TEXT_CLEANUP_COMPLETE);
            DocumentAnalyticsStatus.valueList = Collections.unmodifiableList(valueList);
        }
        synchronized (DocumentAnalyticsStatus.literals)
        {
            DocumentAnalyticsStatus.literals.add(INITIALISED.enumValue);
            DocumentAnalyticsStatus.literals.add(TEXT_EXTRACTION_COMPLETE.enumValue);
            DocumentAnalyticsStatus.literals.add(TYPE_CONFIRMATION_COMPLETE.enumValue);
            DocumentAnalyticsStatus.literals.add(INFORMATION_EXTRACTION_COMPLETE.enumValue);
            DocumentAnalyticsStatus.literals.add(INFORMATION_CONFIRMATION_COMPLETE.enumValue);
            DocumentAnalyticsStatus.literals.add(TEXT_CLEANUP_COMPLETE.enumValue);
            DocumentAnalyticsStatus.literals = Collections.unmodifiableList(literals);
        }
        synchronized (DocumentAnalyticsStatus.names)
        {
            DocumentAnalyticsStatus.names.add("INITIALISED");
            DocumentAnalyticsStatus.names.add("TEXT_EXTRACTION_COMPLETE");
            DocumentAnalyticsStatus.names.add("TYPE_CONFIRMATION_COMPLETE");
            DocumentAnalyticsStatus.names.add("INFORMATION_EXTRACTION_COMPLETE");
            DocumentAnalyticsStatus.names.add("INFORMATION_CONFIRMATION_COMPLETE");
            DocumentAnalyticsStatus.names.add("TEXT_CLEANUP_COMPLETE");
            DocumentAnalyticsStatus.names = Collections.unmodifiableList(names);
        }
    }
}