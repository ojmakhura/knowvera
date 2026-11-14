package bw.co.knowvera;

import java.io.Serializable;
import java.util.Arrays;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.jspecify.annotations.Nullable;

/**
 * TODO: Model Documentation for KeyFieldMatchResult
 */
public class KeyFieldMatchResult
    implements Serializable, Comparable<KeyFieldMatchResult>
{
    /** The serial version UID of this class. Needed for serialization. */
    private static final long serialVersionUID = -7248276981825332820L;

    // Class attributes
    /**
     * TODO: Model Documentation for KeyFieldMatchResult.keyField
     */
    protected String keyField;
    /**
     * TODO: Model Documentation for KeyFieldMatchResult.expectedValue
     */
    protected String expectedValue;
    /**
     * TODO: Model Documentation for KeyFieldMatchResult.extractedValue
     */
    protected String extractedValue;
    /**
     * TODO: Model Documentation for KeyFieldMatchResult.similarity
     */
    @Nullable
    protected Double similarity;
    /**
     * TODO: Model Documentation for KeyFieldMatchResult.mandatory
     */
    @Nullable
    protected Boolean mandatory = true;
    /**
     * TODO: Model Documentation for KeyFieldMatchResult.success
     */
    @Nullable
    protected Boolean success = true;

    /** Default Constructor with no properties */
    public KeyFieldMatchResult()
    {
        // Documented empty block - avoid compiler warning - no super constructor
    }

    /**
     * Constructor taking only required properties
     * @param keyFieldIn String TODO: Model Documentation for KeyFieldMatchResult.keyField
     * @param expectedValueIn String TODO: Model Documentation for KeyFieldMatchResult.expectedValue
     * @param extractedValueIn String TODO: Model Documentation for KeyFieldMatchResult.extractedValue
     */
    public KeyFieldMatchResult(final String keyFieldIn, final String expectedValueIn, final String extractedValueIn)
    {
        this.keyField = keyFieldIn;
        this.expectedValue = expectedValueIn;
        this.extractedValue = extractedValueIn;
    }

    /**
     * Constructor with all properties
     * @param keyFieldIn String
     * @param expectedValueIn String
     * @param extractedValueIn String
     * @param similarityIn Double
     * @param mandatoryIn Boolean
     * @param successIn Boolean
     */
    public KeyFieldMatchResult(final String keyFieldIn, final String expectedValueIn, final String extractedValueIn, final Double similarityIn, final Boolean mandatoryIn, final Boolean successIn)
    {
        this.keyField = keyFieldIn;
        this.expectedValue = expectedValueIn;
        this.extractedValue = extractedValueIn;
        this.similarity = similarityIn;
        this.mandatory = mandatoryIn;
        this.success = successIn;
    }

    /**
     * Copies constructor from other KeyFieldMatchResult
     *
     * @param otherBean Cannot be <code>null</code>
     * @throws NullPointerException if the argument is <code>null</code>
     */
    public KeyFieldMatchResult(final KeyFieldMatchResult otherBean)
    {
        this.keyField = otherBean.getKeyField();
        this.expectedValue = otherBean.getExpectedValue();
        this.extractedValue = otherBean.getExtractedValue();
        this.similarity = otherBean.getSimilarity();
        this.mandatory = otherBean.getMandatory();
        this.success = otherBean.getSuccess();
    }

    /**
     * Copies all properties from the argument value object into this value object.
     * @param otherBean Cannot be <code>null</code>
     */
    public void copy(final KeyFieldMatchResult otherBean)
    {
        if (null != otherBean)
        {
            this.setKeyField(otherBean.getKeyField());
            this.setExpectedValue(otherBean.getExpectedValue());
            this.setExtractedValue(otherBean.getExtractedValue());
            this.setSimilarity(otherBean.getSimilarity());
            this.setMandatory(otherBean.getMandatory());
            this.setSuccess(otherBean.getSuccess());
        }
    }

    /**
     * TODO: Model Documentation for KeyFieldMatchResult.keyField
     * Get the keyField Attribute
     * @return keyField String
     */
    public String getKeyField()
    {
        return this.keyField;
    }

    /**
     * TODO: Model Documentation for KeyFieldMatchResult.keyField
     * @param value String
     */
    public void setKeyField(final String value)
    {
        this.keyField = value;
    }

    /**
     * TODO: Model Documentation for KeyFieldMatchResult.expectedValue
     * Get the expectedValue Attribute
     * @return expectedValue String
     */
    public String getExpectedValue()
    {
        return this.expectedValue;
    }

    /**
     * TODO: Model Documentation for KeyFieldMatchResult.expectedValue
     * @param value String
     */
    public void setExpectedValue(final String value)
    {
        this.expectedValue = value;
    }

    /**
     * TODO: Model Documentation for KeyFieldMatchResult.extractedValue
     * Get the extractedValue Attribute
     * @return extractedValue String
     */
    public String getExtractedValue()
    {
        return this.extractedValue;
    }

    /**
     * TODO: Model Documentation for KeyFieldMatchResult.extractedValue
     * @param value String
     */
    public void setExtractedValue(final String value)
    {
        this.extractedValue = value;
    }

    /**
     * TODO: Model Documentation for KeyFieldMatchResult.similarity
     * Get the similarity Attribute
     * @return similarity Double
     */
    public Double getSimilarity()
    {
        return this.similarity;
    }

    /**
     * TODO: Model Documentation for KeyFieldMatchResult.similarity
     * @param value Double
     */
    public void setSimilarity(final Double value)
    {
        this.similarity = value;
    }

    /**
     * TODO: Model Documentation for KeyFieldMatchResult.mandatory
     * Get the mandatory Attribute
     * @return mandatory Boolean
     */
    public Boolean getMandatory()
    {
        return this.mandatory;
    }

    /**
     * TODO: Model Documentation for KeyFieldMatchResult.mandatory
     * @param value Boolean
     */
    public void setMandatory(final Boolean value)
    {
        this.mandatory = value;
    }

    /**
     * TODO: Model Documentation for KeyFieldMatchResult.success
     * Get the success Attribute
     * @return success Boolean
     */
    public Boolean getSuccess()
    {
        return this.success;
    }

    /**
     * TODO: Model Documentation for KeyFieldMatchResult.success
     * @param value Boolean
     */
    public void setSuccess(final Boolean value)
    {
        this.success = value;
    }

    /**
     * @param object to compare this object against
     * @return boolean if equal
     * @see Object#equals(Object)
     */
    @Override
    public boolean equals(final Object object)
    {
        if (object==null || object.getClass() != this.getClass())
        {
             return false;
        }
        // Check if the same object instance
        if (object==this)
        {
            return true;
        }
        KeyFieldMatchResult rhs = (KeyFieldMatchResult) object;
        return new EqualsBuilder()
            .append(this.getKeyField(), rhs.getKeyField())
            .append(this.getExpectedValue(), rhs.getExpectedValue())
            .append(this.getExtractedValue(), rhs.getExtractedValue())
            .append(this.getSimilarity(), rhs.getSimilarity())
            .append(this.getMandatory(), rhs.getMandatory())
            .append(this.getSuccess(), rhs.getSuccess())
            .isEquals();
    }

    /**
     * @param object to compare this object against
     * @return int if equal
     * @see Comparable#compareTo(Object)
     */
    public int compareTo(final KeyFieldMatchResult object)
    {
        if (object==null)
        {
            return -1;
        }
        // Check if the same object instance
        if (object==this)
        {
            return 0;
        }
        return new CompareToBuilder()
            .append(this.getKeyField(), object.getKeyField())
            .append(this.getExpectedValue(), object.getExpectedValue())
            .append(this.getExtractedValue(), object.getExtractedValue())
            .append(this.getSimilarity(), object.getSimilarity())
            .append(this.getMandatory(), object.getMandatory())
            .append(this.getSuccess(), object.getSuccess())
            .toComparison();
    }

    /**
     * @return int hashCode value
     * @see Object#hashCode()
     */
    @Override
    public int hashCode()
    {
        return new HashCodeBuilder(1249046965, -82296885)
            .append(this.getKeyField())
            .append(this.getExpectedValue())
            .append(this.getExtractedValue())
            .append(this.getSimilarity())
            .append(this.getMandatory())
            .append(this.getSuccess())
            .toHashCode();
    }

    /**
     * @return String representation of object
     * @see Object#toString()
     */
    @Override
    public String toString()
    {

        StringBuilder builder = new StringBuilder();
        builder.append("{\n");
        builder.append("\t\"keyField\" : \"" + this.getKeyField() + "\"");
        builder.append("\t,\"expectedValue\" : \"" + this.getExpectedValue() + "\"");
        builder.append("\t,\"extractedValue\" : \"" + this.getExtractedValue() + "\"");
        builder.append("\t,\"similarity\" : \"" + this.getSimilarity() + "\"");
        builder.append("\t,\"mandatory\" : \"" + this.getMandatory() + "\"");
        builder.append("\t,\"success\" : \"" + this.getSuccess() + "\"");
        builder.append("}");
        return builder.toString();

    }

    /**
     * Compares the properties of this instance to the properties of the argument. This method will return
     * {@code false} as soon as it detects that the argument is {@code null} or not of the same type as
     * (or a sub-type of) this instance's type.
     *
     * <p/>For array, collection or map properties the comparison will be done one level deep, in other words:
     * the elements will be compared using the {@code equals()} operation.
     *
     * <p/>Note that two properties will be considered equal when both values are {@code null}.
     *
     * @param thatObject the object containing the properties to compare against this instance
     * @return this method will return {@code true} in case the argument has the same type as this class, or is a
     *      sub-type of this class and all properties as found on this class have equal values when queried on that
     *      argument instance; in all other cases this method will return {@code false}
     */
    public boolean equalProperties(final Object thatObject)
    {
        if (thatObject == null || !this.getClass().isAssignableFrom(thatObject.getClass()))
        {
            return false;
        }

        final KeyFieldMatchResult that = (KeyFieldMatchResult)thatObject;
        return
            equal(this.getKeyField(), that.getKeyField())
            && equal(this.getExpectedValue(), that.getExpectedValue())
            && equal(this.getExtractedValue(), that.getExtractedValue())
            && equal(this.getSimilarity(), that.getSimilarity())
            && equal(this.getMandatory(), that.getMandatory())
            && equal(this.getSuccess(), that.getSuccess())
        ;
    }

    /**
     * This is a convenient helper method which is able to detect whether or not two values are equal. Two values
     * are equal when they are both {@code null}, are arrays of the same length with equal elements or are
     * equal objects (this includes {@link java.util.Collection} and {@link java.util.Map} instances).
     *
     * <p/>Note that for array, collection or map instances the comparison runs one level deep.
     *
     * @param first the first object to compare, may be {@code null}
     * @param second the second object to compare, may be {@code null}
     * @return this method will return {@code true} in case both objects are equal as explained above;
     *      in all other cases this method will return {@code false}
     */
    protected static boolean equal(final Object first, final Object second)
    {
        final boolean equal;

        if (first == null)
        {
            equal = (second == null);
        }
        else if (first.getClass().isArray() && (second != null) && second.getClass().isArray())
        {
            equal = Arrays.equals((Object[])first, (Object[])second);
        }
        else // note that the following also covers java.util.Collection and java.util.Map
        {
            equal = first.equals(second);
        }

        return equal;
    }

}