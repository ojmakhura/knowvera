package bw.co.knowvera.individual;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * TODO: Model Documentation for bw.co.knowvera.individual.IndividualIdentityType
 */
public enum IndividualIdentityType
{
    /** TODO: Model Documentation for Enumeration Literal OMANG value "OMANG" */
    OMANG("OMANG"),
    /** TODO: Model Documentation for Enumeration Literal PASSPORT value "PASSPORT" */
    PASSPORT("PASSPORT"),
    /** TODO: Model Documentation for Enumeration Literal RESIDENCE_PERMIT value "RESIDENCE_PERMIT" */
    RESIDENCE_PERMIT("RESIDENCE_PERMIT"),
    /** TODO: Model Documentation for Enumeration Literal BIRTH_CERTIFICATE value "BIRTH_CERTIFICATE" */
    BIRTH_CERTIFICATE("BIRTH_CERTIFICATE"),
    /** TODO: Model Documentation for Enumeration Literal REFUGEE_ID value "REFUGEE_ID" */
    REFUGEE_ID("REFUGEE_ID"),
    /** TODO: Model Documentation for Enumeration Literal DRIVERS_LICENCE value "DRIVERS_LICENCE" */
    DRIVERS_LICENCE("DRIVERS_LICENCE");

    /**
     * The serial version UID of this class. Needed for serialization.
     */
    private static final long serialVersionUID = 3441803794053739740L;

    private final String enumValue;

    /**
     * The constructor with enumeration literal value allowing
     * super classes to access it.
     */
    private IndividualIdentityType(String value)
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
     * Retrieves an instance of IndividualIdentityType from <code>its name</code>.
     *
     * @param name the name to create the IndividualIdentityType from.
     * @return The enumeration literal named after the 'name' argument
     */
    public static IndividualIdentityType fromString(String name)
    {
        return IndividualIdentityType.valueOf(name);
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
     * Returns an instance of IndividualIdentityType from String <code>value</code>.
     * Required by JAXB2 enumeration implementation
     *
     * @param value the value to create the IndividualIdentityType from.
     * @return static Enumeration with corresponding value
     */
    public static IndividualIdentityType fromValue(String value)
    {
        // Allow either the Enum name or value string to be used to retrieve the enum
        for (IndividualIdentityType enumName: IndividualIdentityType.values())
        {
            if (enumName.getValue().equals(value))
            {
                return enumName;
            }
        }
        for (String enumName: IndividualIdentityType.names())
        {
            if (enumName.equals(value))
            {
                return IndividualIdentityType.valueOf(enumName);
            }
        }
        throw new IllegalArgumentException("IndividualIdentityType.fromValue(" + value + ')');
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
        return IndividualIdentityType.literals;
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
        return IndividualIdentityType.names;
    }

    private static Map<String, IndividualIdentityType> values = new LinkedHashMap<String, IndividualIdentityType>(6, 1);
    private static List<String> literals = new ArrayList<String>(6);
    private static List<String> names = new ArrayList<String>(6);
    private static List<IndividualIdentityType> valueList = new ArrayList<IndividualIdentityType>(6);

    /**
     * Initializes the values.
     */
    static
    {
        synchronized (IndividualIdentityType.values)
        {
            IndividualIdentityType.values.put(OMANG.enumValue, OMANG);
            IndividualIdentityType.values.put(PASSPORT.enumValue, PASSPORT);
            IndividualIdentityType.values.put(RESIDENCE_PERMIT.enumValue, RESIDENCE_PERMIT);
            IndividualIdentityType.values.put(BIRTH_CERTIFICATE.enumValue, BIRTH_CERTIFICATE);
            IndividualIdentityType.values.put(REFUGEE_ID.enumValue, REFUGEE_ID);
            IndividualIdentityType.values.put(DRIVERS_LICENCE.enumValue, DRIVERS_LICENCE);
        }
        synchronized (IndividualIdentityType.valueList)
        {
            IndividualIdentityType.valueList.add(OMANG);
            IndividualIdentityType.valueList.add(PASSPORT);
            IndividualIdentityType.valueList.add(RESIDENCE_PERMIT);
            IndividualIdentityType.valueList.add(BIRTH_CERTIFICATE);
            IndividualIdentityType.valueList.add(REFUGEE_ID);
            IndividualIdentityType.valueList.add(DRIVERS_LICENCE);
            IndividualIdentityType.valueList = Collections.unmodifiableList(valueList);
        }
        synchronized (IndividualIdentityType.literals)
        {
            IndividualIdentityType.literals.add(OMANG.enumValue);
            IndividualIdentityType.literals.add(PASSPORT.enumValue);
            IndividualIdentityType.literals.add(RESIDENCE_PERMIT.enumValue);
            IndividualIdentityType.literals.add(BIRTH_CERTIFICATE.enumValue);
            IndividualIdentityType.literals.add(REFUGEE_ID.enumValue);
            IndividualIdentityType.literals.add(DRIVERS_LICENCE.enumValue);
            IndividualIdentityType.literals = Collections.unmodifiableList(literals);
        }
        synchronized (IndividualIdentityType.names)
        {
            IndividualIdentityType.names.add("OMANG");
            IndividualIdentityType.names.add("PASSPORT");
            IndividualIdentityType.names.add("RESIDENCE_PERMIT");
            IndividualIdentityType.names.add("BIRTH_CERTIFICATE");
            IndividualIdentityType.names.add("REFUGEE_ID");
            IndividualIdentityType.names.add("DRIVERS_LICENCE");
            IndividualIdentityType.names = Collections.unmodifiableList(names);
        }
    }
}