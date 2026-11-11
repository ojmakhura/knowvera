package bw.co.knowvera.document;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * TODO: Model Documentation for bw.co.knowvera.document.DocumentVerificationStatus
 */
public enum DocumentVerificationStatus
{
    /** TODO: Model Documentation for Enumeration Literal UNVERIFIED value "UNVERIFIED" */
    UNVERIFIED("UNVERIFIED"),
    /** TODO: Model Documentation for Enumeration Literal VERIFIED value "VERIFIED" */
    VERIFIED("VERIFIED"),
    /** TODO: Model Documentation for Enumeration Literal REJECTED value "REJECTED" */
    REJECTED("REJECTED"),
    /** TODO: Model Documentation for Enumeration Literal MANUAL_REVIEW value "MANUAL_REVIEW" */
    MANUAL_REVIEW("MANUAL_REVIEW"),
    /** TODO: Model Documentation for Enumeration Literal IN_PROGRESS value "IN_PROGRESS" */
    IN_PROGRESS("IN_PROGRESS");

    /**
     * The serial version UID of this class. Needed for serialization.
     */
    private static final long serialVersionUID = 6006263614481138200L;

    private final String enumValue;

    /**
     * The constructor with enumeration literal value allowing
     * super classes to access it.
     */
    private DocumentVerificationStatus(String value)
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
     * Retrieves an instance of DocumentVerificationStatus from <code>its name</code>.
     *
     * @param name the name to create the DocumentVerificationStatus from.
     * @return The enumeration literal named after the 'name' argument
     */
    public static DocumentVerificationStatus fromString(String name)
    {
        return DocumentVerificationStatus.valueOf(name);
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
     * Returns an instance of DocumentVerificationStatus from String <code>value</code>.
     * Required by JAXB2 enumeration implementation
     *
     * @param value the value to create the DocumentVerificationStatus from.
     * @return static Enumeration with corresponding value
     */
    public static DocumentVerificationStatus fromValue(String value)
    {
        // Allow either the Enum name or value string to be used to retrieve the enum
        for (DocumentVerificationStatus enumName: DocumentVerificationStatus.values())
        {
            if (enumName.getValue().equals(value))
            {
                return enumName;
            }
        }
        for (String enumName: DocumentVerificationStatus.names())
        {
            if (enumName.equals(value))
            {
                return DocumentVerificationStatus.valueOf(enumName);
            }
        }
        throw new IllegalArgumentException("DocumentVerificationStatus.fromValue(" + value + ')');
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
        return DocumentVerificationStatus.literals;
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
        return DocumentVerificationStatus.names;
    }

    private static Map<String, DocumentVerificationStatus> values = new LinkedHashMap<String, DocumentVerificationStatus>(5, 1);
    private static List<String> literals = new ArrayList<String>(5);
    private static List<String> names = new ArrayList<String>(5);
    private static List<DocumentVerificationStatus> valueList = new ArrayList<DocumentVerificationStatus>(5);

    /**
     * Initializes the values.
     */
    static
    {
        synchronized (DocumentVerificationStatus.values)
        {
            DocumentVerificationStatus.values.put(UNVERIFIED.enumValue, UNVERIFIED);
            DocumentVerificationStatus.values.put(VERIFIED.enumValue, VERIFIED);
            DocumentVerificationStatus.values.put(REJECTED.enumValue, REJECTED);
            DocumentVerificationStatus.values.put(MANUAL_REVIEW.enumValue, MANUAL_REVIEW);
            DocumentVerificationStatus.values.put(IN_PROGRESS.enumValue, IN_PROGRESS);
        }
        synchronized (DocumentVerificationStatus.valueList)
        {
            DocumentVerificationStatus.valueList.add(UNVERIFIED);
            DocumentVerificationStatus.valueList.add(VERIFIED);
            DocumentVerificationStatus.valueList.add(REJECTED);
            DocumentVerificationStatus.valueList.add(MANUAL_REVIEW);
            DocumentVerificationStatus.valueList.add(IN_PROGRESS);
            DocumentVerificationStatus.valueList = Collections.unmodifiableList(valueList);
        }
        synchronized (DocumentVerificationStatus.literals)
        {
            DocumentVerificationStatus.literals.add(UNVERIFIED.enumValue);
            DocumentVerificationStatus.literals.add(VERIFIED.enumValue);
            DocumentVerificationStatus.literals.add(REJECTED.enumValue);
            DocumentVerificationStatus.literals.add(MANUAL_REVIEW.enumValue);
            DocumentVerificationStatus.literals.add(IN_PROGRESS.enumValue);
            DocumentVerificationStatus.literals = Collections.unmodifiableList(literals);
        }
        synchronized (DocumentVerificationStatus.names)
        {
            DocumentVerificationStatus.names.add("UNVERIFIED");
            DocumentVerificationStatus.names.add("VERIFIED");
            DocumentVerificationStatus.names.add("REJECTED");
            DocumentVerificationStatus.names.add("MANUAL_REVIEW");
            DocumentVerificationStatus.names.add("IN_PROGRESS");
            DocumentVerificationStatus.names = Collections.unmodifiableList(names);
        }
    }
}