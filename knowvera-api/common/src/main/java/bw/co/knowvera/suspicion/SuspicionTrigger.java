package bw.co.knowvera.suspicion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * TODO: Model Documentation for bw.co.knowvera.suspicion.SuspicionTrigger
 */
public enum SuspicionTrigger
{
    /** TODO: Model Documentation for Enumeration Literal SANCTIONS_MATCH value "SANCTIONS_MATCH" */
    SANCTIONS_MATCH("SANCTIONS_MATCH"),
    /** TODO: Model Documentation for Enumeration Literal INCOMPLETE_DUE_DILIGENCE value "INCOMPLETE_DUE_DILIGENCE" */
    INCOMPLETE_DUE_DILIGENCE("INCOMPLETE_DUE_DILIGENCE"),
    /** TODO: Model Documentation for Enumeration Literal UNUSUAL_ACTIVITY value "UNUSUAL_ACTIVITY" */
    UNUSUAL_ACTIVITY("UNUSUAL_ACTIVITY"),
    /** TODO: Model Documentation for Enumeration Literal OTHER value "OTHER" */
    OTHER("OTHER");

    /**
     * The serial version UID of this class. Needed for serialization.
     */
    private static final long serialVersionUID = -8619482658362618947L;

    private final String enumValue;

    /**
     * The constructor with enumeration literal value allowing
     * super classes to access it.
     */
    private SuspicionTrigger(String value)
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
     * Retrieves an instance of SuspicionTrigger from <code>its name</code>.
     *
     * @param name the name to create the SuspicionTrigger from.
     * @return The enumeration literal named after the 'name' argument
     */
    public static SuspicionTrigger fromString(String name)
    {
        return SuspicionTrigger.valueOf(name);
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
     * Returns an instance of SuspicionTrigger from String <code>value</code>.
     * Required by JAXB2 enumeration implementation
     *
     * @param value the value to create the SuspicionTrigger from.
     * @return static Enumeration with corresponding value
     */
    public static SuspicionTrigger fromValue(String value)
    {
        // Allow either the Enum name or value string to be used to retrieve the enum
        for (SuspicionTrigger enumName: SuspicionTrigger.values())
        {
            if (enumName.getValue().equals(value))
            {
                return enumName;
            }
        }
        for (String enumName: SuspicionTrigger.names())
        {
            if (enumName.equals(value))
            {
                return SuspicionTrigger.valueOf(enumName);
            }
        }
        throw new IllegalArgumentException("SuspicionTrigger.fromValue(" + value + ')');
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
        return SuspicionTrigger.literals;
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
        return SuspicionTrigger.names;
    }

    private static Map<String, SuspicionTrigger> values = new LinkedHashMap<String, SuspicionTrigger>(4, 1);
    private static List<String> literals = new ArrayList<String>(4);
    private static List<String> names = new ArrayList<String>(4);
    private static List<SuspicionTrigger> valueList = new ArrayList<SuspicionTrigger>(4);

    /**
     * Initializes the values.
     */
    static
    {
        synchronized (SuspicionTrigger.values)
        {
            SuspicionTrigger.values.put(SANCTIONS_MATCH.enumValue, SANCTIONS_MATCH);
            SuspicionTrigger.values.put(INCOMPLETE_DUE_DILIGENCE.enumValue, INCOMPLETE_DUE_DILIGENCE);
            SuspicionTrigger.values.put(UNUSUAL_ACTIVITY.enumValue, UNUSUAL_ACTIVITY);
            SuspicionTrigger.values.put(OTHER.enumValue, OTHER);
        }
        synchronized (SuspicionTrigger.valueList)
        {
            SuspicionTrigger.valueList.add(SANCTIONS_MATCH);
            SuspicionTrigger.valueList.add(INCOMPLETE_DUE_DILIGENCE);
            SuspicionTrigger.valueList.add(UNUSUAL_ACTIVITY);
            SuspicionTrigger.valueList.add(OTHER);
            SuspicionTrigger.valueList = Collections.unmodifiableList(valueList);
        }
        synchronized (SuspicionTrigger.literals)
        {
            SuspicionTrigger.literals.add(SANCTIONS_MATCH.enumValue);
            SuspicionTrigger.literals.add(INCOMPLETE_DUE_DILIGENCE.enumValue);
            SuspicionTrigger.literals.add(UNUSUAL_ACTIVITY.enumValue);
            SuspicionTrigger.literals.add(OTHER.enumValue);
            SuspicionTrigger.literals = Collections.unmodifiableList(literals);
        }
        synchronized (SuspicionTrigger.names)
        {
            SuspicionTrigger.names.add("SANCTIONS_MATCH");
            SuspicionTrigger.names.add("INCOMPLETE_DUE_DILIGENCE");
            SuspicionTrigger.names.add("UNUSUAL_ACTIVITY");
            SuspicionTrigger.names.add("OTHER");
            SuspicionTrigger.names = Collections.unmodifiableList(names);
        }
    }
}