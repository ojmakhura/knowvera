package bw.co.knowvera.contact;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * TODO: Model Documentation for bw.co.knowvera.contact.ContactIntent
 */
public enum ContactIntent
{
    /** TODO: Model Documentation for Enumeration Literal VERIFICATION_STATUS value "VERIFICATION_STATUS" */
    VERIFICATION_STATUS("VERIFICATION_STATUS"),
    /** TODO: Model Documentation for Enumeration Literal DOCUMENT_REJECTED value "DOCUMENT_REJECTED" */
    DOCUMENT_REJECTED("DOCUMENT_REJECTED"),
    /** TODO: Model Documentation for Enumeration Literal UPLOAD_PROBLEM value "UPLOAD_PROBLEM" */
    UPLOAD_PROBLEM("UPLOAD_PROBLEM"),
    /** TODO: Model Documentation for Enumeration Literal VERIFICATION_DELAY value "VERIFICATION_DELAY" */
    VERIFICATION_DELAY("VERIFICATION_DELAY"),
    /** TODO: Model Documentation for Enumeration Literal ACCOUNT_LOCKED value "ACCOUNT_LOCKED" */
    ACCOUNT_LOCKED("ACCOUNT_LOCKED"),
    /** TODO: Model Documentation for Enumeration Literal UPDATE_INFORMATION value "UPDATE_INFORMATION" */
    UPDATE_INFORMATION("UPDATE_INFORMATION"),
    /** TODO: Model Documentation for Enumeration Literal RESUBMIT_DOCUMENTS value "RESUBMIT_DOCUMENTS" */
    RESUBMIT_DOCUMENTS("RESUBMIT_DOCUMENTS"),
    /** TODO: Model Documentation for Enumeration Literal TECHNICAL_PROBLEM value "TECHNICAL_PROBLEM" */
    TECHNICAL_PROBLEM("TECHNICAL_PROBLEM"),
    /** TODO: Model Documentation for Enumeration Literal KYC_PROCESS_QUESTION value "KYC_PROCESS_QUESTION" */
    KYC_PROCESS_QUESTION("KYC_PROCESS_QUESTION"),
    /** TODO: Model Documentation for Enumeration Literal VERIFICATION_APPROVED value "VERIFICATION_APPROVED" */
    VERIFICATION_APPROVED("VERIFICATION_APPROVED"),
    /** TODO: Model Documentation for Enumeration Literal DATA_PRIVACY_CONCERN value "DATA_PRIVACY_CONCERN" */
    DATA_PRIVACY_CONCERN("DATA_PRIVACY_CONCERN"),
    /** TODO: Model Documentation for Enumeration Literal IDENTITY_THEFT_REPORT value "IDENTITY_THEFT_REPORT" */
    IDENTITY_THEFT_REPORT("IDENTITY_THEFT_REPORT"),
    /** TODO: Model Documentation for Enumeration Literal TRANSACTION_BLOCKED value "TRANSACTION_BLOCKED" */
    TRANSACTION_BLOCKED("TRANSACTION_BLOCKED"),
    /** TODO: Model Documentation for Enumeration Literal KYB_VERIFICATION value "KYB_VERIFICATION" */
    KYB_VERIFICATION("KYB_VERIFICATION"),
    /** TODO: Model Documentation for Enumeration Literal GENERAL_SUPPORT value "GENERAL_SUPPORT" */
    GENERAL_SUPPORT("GENERAL_SUPPORT");

    /**
     * The serial version UID of this class. Needed for serialization.
     */
    private static final long serialVersionUID = -177586520815752255L;

    private final String enumValue;

    /**
     * The constructor with enumeration literal value allowing
     * super classes to access it.
     */
    private ContactIntent(String value)
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
     * Retrieves an instance of ContactIntent from <code>its name</code>.
     *
     * @param name the name to create the ContactIntent from.
     * @return The enumeration literal named after the 'name' argument
     */
    public static ContactIntent fromString(String name)
    {
        return ContactIntent.valueOf(name);
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
     * Returns an instance of ContactIntent from String <code>value</code>.
     * Required by JAXB2 enumeration implementation
     *
     * @param value the value to create the ContactIntent from.
     * @return static Enumeration with corresponding value
     */
    public static ContactIntent fromValue(String value)
    {
        // Allow either the Enum name or value string to be used to retrieve the enum
        for (ContactIntent enumName: ContactIntent.values())
        {
            if (enumName.getValue().equals(value))
            {
                return enumName;
            }
        }
        for (String enumName: ContactIntent.names())
        {
            if (enumName.equals(value))
            {
                return ContactIntent.valueOf(enumName);
            }
        }
        throw new IllegalArgumentException("ContactIntent.fromValue(" + value + ')');
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
        return ContactIntent.literals;
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
        return ContactIntent.names;
    }

    private static Map<String, ContactIntent> values = new LinkedHashMap<String, ContactIntent>(15, 1);
    private static List<String> literals = new ArrayList<String>(15);
    private static List<String> names = new ArrayList<String>(15);
    private static List<ContactIntent> valueList = new ArrayList<ContactIntent>(15);

    /**
     * Initializes the values.
     */
    static
    {
        synchronized (ContactIntent.values)
        {
            ContactIntent.values.put(VERIFICATION_STATUS.enumValue, VERIFICATION_STATUS);
            ContactIntent.values.put(DOCUMENT_REJECTED.enumValue, DOCUMENT_REJECTED);
            ContactIntent.values.put(UPLOAD_PROBLEM.enumValue, UPLOAD_PROBLEM);
            ContactIntent.values.put(VERIFICATION_DELAY.enumValue, VERIFICATION_DELAY);
            ContactIntent.values.put(ACCOUNT_LOCKED.enumValue, ACCOUNT_LOCKED);
            ContactIntent.values.put(UPDATE_INFORMATION.enumValue, UPDATE_INFORMATION);
            ContactIntent.values.put(RESUBMIT_DOCUMENTS.enumValue, RESUBMIT_DOCUMENTS);
            ContactIntent.values.put(TECHNICAL_PROBLEM.enumValue, TECHNICAL_PROBLEM);
            ContactIntent.values.put(KYC_PROCESS_QUESTION.enumValue, KYC_PROCESS_QUESTION);
            ContactIntent.values.put(VERIFICATION_APPROVED.enumValue, VERIFICATION_APPROVED);
            ContactIntent.values.put(DATA_PRIVACY_CONCERN.enumValue, DATA_PRIVACY_CONCERN);
            ContactIntent.values.put(IDENTITY_THEFT_REPORT.enumValue, IDENTITY_THEFT_REPORT);
            ContactIntent.values.put(TRANSACTION_BLOCKED.enumValue, TRANSACTION_BLOCKED);
            ContactIntent.values.put(KYB_VERIFICATION.enumValue, KYB_VERIFICATION);
            ContactIntent.values.put(GENERAL_SUPPORT.enumValue, GENERAL_SUPPORT);
        }
        synchronized (ContactIntent.valueList)
        {
            ContactIntent.valueList.add(VERIFICATION_STATUS);
            ContactIntent.valueList.add(DOCUMENT_REJECTED);
            ContactIntent.valueList.add(UPLOAD_PROBLEM);
            ContactIntent.valueList.add(VERIFICATION_DELAY);
            ContactIntent.valueList.add(ACCOUNT_LOCKED);
            ContactIntent.valueList.add(UPDATE_INFORMATION);
            ContactIntent.valueList.add(RESUBMIT_DOCUMENTS);
            ContactIntent.valueList.add(TECHNICAL_PROBLEM);
            ContactIntent.valueList.add(KYC_PROCESS_QUESTION);
            ContactIntent.valueList.add(VERIFICATION_APPROVED);
            ContactIntent.valueList.add(DATA_PRIVACY_CONCERN);
            ContactIntent.valueList.add(IDENTITY_THEFT_REPORT);
            ContactIntent.valueList.add(TRANSACTION_BLOCKED);
            ContactIntent.valueList.add(KYB_VERIFICATION);
            ContactIntent.valueList.add(GENERAL_SUPPORT);
            ContactIntent.valueList = Collections.unmodifiableList(valueList);
        }
        synchronized (ContactIntent.literals)
        {
            ContactIntent.literals.add(VERIFICATION_STATUS.enumValue);
            ContactIntent.literals.add(DOCUMENT_REJECTED.enumValue);
            ContactIntent.literals.add(UPLOAD_PROBLEM.enumValue);
            ContactIntent.literals.add(VERIFICATION_DELAY.enumValue);
            ContactIntent.literals.add(ACCOUNT_LOCKED.enumValue);
            ContactIntent.literals.add(UPDATE_INFORMATION.enumValue);
            ContactIntent.literals.add(RESUBMIT_DOCUMENTS.enumValue);
            ContactIntent.literals.add(TECHNICAL_PROBLEM.enumValue);
            ContactIntent.literals.add(KYC_PROCESS_QUESTION.enumValue);
            ContactIntent.literals.add(VERIFICATION_APPROVED.enumValue);
            ContactIntent.literals.add(DATA_PRIVACY_CONCERN.enumValue);
            ContactIntent.literals.add(IDENTITY_THEFT_REPORT.enumValue);
            ContactIntent.literals.add(TRANSACTION_BLOCKED.enumValue);
            ContactIntent.literals.add(KYB_VERIFICATION.enumValue);
            ContactIntent.literals.add(GENERAL_SUPPORT.enumValue);
            ContactIntent.literals = Collections.unmodifiableList(literals);
        }
        synchronized (ContactIntent.names)
        {
            ContactIntent.names.add("VERIFICATION_STATUS");
            ContactIntent.names.add("DOCUMENT_REJECTED");
            ContactIntent.names.add("UPLOAD_PROBLEM");
            ContactIntent.names.add("VERIFICATION_DELAY");
            ContactIntent.names.add("ACCOUNT_LOCKED");
            ContactIntent.names.add("UPDATE_INFORMATION");
            ContactIntent.names.add("RESUBMIT_DOCUMENTS");
            ContactIntent.names.add("TECHNICAL_PROBLEM");
            ContactIntent.names.add("KYC_PROCESS_QUESTION");
            ContactIntent.names.add("VERIFICATION_APPROVED");
            ContactIntent.names.add("DATA_PRIVACY_CONCERN");
            ContactIntent.names.add("IDENTITY_THEFT_REPORT");
            ContactIntent.names.add("TRANSACTION_BLOCKED");
            ContactIntent.names.add("KYB_VERIFICATION");
            ContactIntent.names.add("GENERAL_SUPPORT");
            ContactIntent.names = Collections.unmodifiableList(names);
        }
    }
}