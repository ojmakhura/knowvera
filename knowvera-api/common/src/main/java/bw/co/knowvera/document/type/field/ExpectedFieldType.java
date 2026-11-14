package bw.co.knowvera.document.type.field;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * TODO: Model Documentation for bw.co.knowvera.document.type.field.ExpectedFieldType
 */
public enum ExpectedFieldType
{
    /** TODO: Model Documentation for Enumeration Literal BOOLEAN value "BOOLEAN" */
    BOOLEAN("BOOLEAN"),
    /** TODO: Model Documentation for Enumeration Literal STRING value "STRING" */
    STRING("STRING"),
    /** TODO: Model Documentation for Enumeration Literal DATE value "DATE" */
    DATE("DATE"),
    /** TODO: Model Documentation for Enumeration Literal NUMBER value "NUMBER" */
    NUMBER("NUMBER"),
    /** TODO: Model Documentation for Enumeration Literal OBJECT value "OBJECT" */
    OBJECT("OBJECT");

    /**
     * The serial version UID of this class. Needed for serialization.
     */
    private static final long serialVersionUID = -2676216134301770910L;

    private final String enumValue;

    /**
     * The constructor with enumeration literal value allowing
     * super classes to access it.
     */
    private ExpectedFieldType(String value)
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
     * Retrieves an instance of ExpectedFieldType from <code>its name</code>.
     *
     * @param name the name to create the ExpectedFieldType from.
     * @return The enumeration literal named after the 'name' argument
     */
    public static ExpectedFieldType fromString(String name)
    {
        return ExpectedFieldType.valueOf(name);
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
     * Returns an instance of ExpectedFieldType from String <code>value</code>.
     * Required by JAXB2 enumeration implementation
     *
     * @param value the value to create the ExpectedFieldType from.
     * @return static Enumeration with corresponding value
     */
    public static ExpectedFieldType fromValue(String value)
    {
        // Allow either the Enum name or value string to be used to retrieve the enum
        for (ExpectedFieldType enumName: ExpectedFieldType.values())
        {
            if (enumName.getValue().equals(value))
            {
                return enumName;
            }
        }
        for (String enumName: ExpectedFieldType.names())
        {
            if (enumName.equals(value))
            {
                return ExpectedFieldType.valueOf(enumName);
            }
        }
        throw new IllegalArgumentException("ExpectedFieldType.fromValue(" + value + ')');
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
        return ExpectedFieldType.literals;
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
        return ExpectedFieldType.names;
    }

    private static Map<String, ExpectedFieldType> values = new LinkedHashMap<String, ExpectedFieldType>(5, 1);
    private static List<String> literals = new ArrayList<String>(5);
    private static List<String> names = new ArrayList<String>(5);
    private static List<ExpectedFieldType> valueList = new ArrayList<ExpectedFieldType>(5);

    /**
     * Initializes the values.
     */
    static
    {
        synchronized (ExpectedFieldType.values)
        {
            ExpectedFieldType.values.put(BOOLEAN.enumValue, BOOLEAN);
            ExpectedFieldType.values.put(STRING.enumValue, STRING);
            ExpectedFieldType.values.put(DATE.enumValue, DATE);
            ExpectedFieldType.values.put(NUMBER.enumValue, NUMBER);
            ExpectedFieldType.values.put(OBJECT.enumValue, OBJECT);
        }
        synchronized (ExpectedFieldType.valueList)
        {
            ExpectedFieldType.valueList.add(BOOLEAN);
            ExpectedFieldType.valueList.add(STRING);
            ExpectedFieldType.valueList.add(DATE);
            ExpectedFieldType.valueList.add(NUMBER);
            ExpectedFieldType.valueList.add(OBJECT);
            ExpectedFieldType.valueList = Collections.unmodifiableList(valueList);
        }
        synchronized (ExpectedFieldType.literals)
        {
            ExpectedFieldType.literals.add(BOOLEAN.enumValue);
            ExpectedFieldType.literals.add(STRING.enumValue);
            ExpectedFieldType.literals.add(DATE.enumValue);
            ExpectedFieldType.literals.add(NUMBER.enumValue);
            ExpectedFieldType.literals.add(OBJECT.enumValue);
            ExpectedFieldType.literals = Collections.unmodifiableList(literals);
        }
        synchronized (ExpectedFieldType.names)
        {
            ExpectedFieldType.names.add("BOOLEAN");
            ExpectedFieldType.names.add("STRING");
            ExpectedFieldType.names.add("DATE");
            ExpectedFieldType.names.add("NUMBER");
            ExpectedFieldType.names.add("OBJECT");
            ExpectedFieldType.names = Collections.unmodifiableList(names);
        }
    }
}