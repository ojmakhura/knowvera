package bw.co.knowvera.document;

import bw.co.knowvera.KeyFieldMatchResult;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.jspecify.annotations.Nullable;

/**
 * TODO: Model Documentation for DataVerification
 */
public class DataVerification
    implements Serializable, Comparable<DataVerification>
{
    /** The serial version UID of this class. Needed for serialization. */
    private static final long serialVersionUID = -3739449313855174376L;

    // Class attributes
    /**
     * TODO: Model Documentation for
     * DataVerification.verificationDataConfigId
     */
    protected String verificationDataConfigId;
    /**
     * TODO: Model Documentation for DataVerification.verificationDataName
     */
    protected String verificationDataName;
    /**
     * TODO: Model Documentation for DataVerification.verificationStatus
     */
    protected DataVerificationStatus verificationStatus;
    /**
     * TODO: Model Documentation for DataVerification.verificationBy
     */
    protected String verificationBy;
    /**
     * TODO: Model Documentation for DataVerification.verificationReport
     */
    protected String verificationReport;
    /**
     * TODO: Model Documentation for DataVerification.score
     */
    @Nullable
    protected Double score = 0.0;
    /**
     * TODO: Model Documentation for DataVerification.keyFieldMatches
     */
    @Nullable
    protected List<KeyFieldMatchResult> keyFieldMatches;
    /**
     * TODO: Model Documentation for DataVerification.hasMandatoryFields
     */
    @Nullable
    protected Boolean hasMandatoryFields = false;

    /** Default Constructor with no properties */
    public DataVerification()
    {
        // Documented empty block - avoid compiler warning - no super constructor
    }

    /**
     * Constructor taking only required properties
     * @param verificationDataConfigIdIn String TODO: Model Documentation for DataVerification.verificationDataConfigId
     * @param verificationDataNameIn String TODO: Model Documentation for DataVerification.verificationDataName
     * @param verificationStatusIn DataVerificationStatus TODO: Model Documentation for DataVerification.verificationStatus
     * @param verificationByIn String TODO: Model Documentation for DataVerification.verificationBy
     * @param verificationReportIn String TODO: Model Documentation for DataVerification.verificationReport
     */
    public DataVerification(final String verificationDataConfigIdIn, final String verificationDataNameIn, final DataVerificationStatus verificationStatusIn, final String verificationByIn, final String verificationReportIn)
    {
        this.verificationDataConfigId = verificationDataConfigIdIn;
        this.verificationDataName = verificationDataNameIn;
        this.verificationStatus = verificationStatusIn;
        this.verificationBy = verificationByIn;
        this.verificationReport = verificationReportIn;
    }

    /**
     * Constructor with all properties
     * @param verificationDataConfigIdIn String
     * @param verificationDataNameIn String
     * @param verificationStatusIn DataVerificationStatus
     * @param verificationByIn String
     * @param verificationReportIn String
     * @param scoreIn Double
     * @param keyFieldMatchesIn List<KeyFieldMatchResult>
     * @param hasMandatoryFieldsIn Boolean
     */
    public DataVerification(final String verificationDataConfigIdIn, final String verificationDataNameIn, final DataVerificationStatus verificationStatusIn, final String verificationByIn, final String verificationReportIn, final Double scoreIn, final List<KeyFieldMatchResult> keyFieldMatchesIn, final Boolean hasMandatoryFieldsIn)
    {
        this.verificationDataConfigId = verificationDataConfigIdIn;
        this.verificationDataName = verificationDataNameIn;
        this.verificationStatus = verificationStatusIn;
        this.verificationBy = verificationByIn;
        this.verificationReport = verificationReportIn;
        this.score = scoreIn;
        this.keyFieldMatches = keyFieldMatchesIn;
        this.hasMandatoryFields = hasMandatoryFieldsIn;
    }

    /**
     * Copies constructor from other DataVerification
     *
     * @param otherBean Cannot be <code>null</code>
     * @throws NullPointerException if the argument is <code>null</code>
     */
    public DataVerification(final DataVerification otherBean)
    {
        this.verificationDataConfigId = otherBean.getVerificationDataConfigId();
        this.verificationDataName = otherBean.getVerificationDataName();
        this.verificationStatus = otherBean.getVerificationStatus();
        this.verificationBy = otherBean.getVerificationBy();
        this.verificationReport = otherBean.getVerificationReport();
        this.score = otherBean.getScore();
        this.keyFieldMatches = otherBean.getKeyFieldMatches();
        this.hasMandatoryFields = otherBean.getHasMandatoryFields();
    }

    /**
     * Copies all properties from the argument value object into this value object.
     * @param otherBean Cannot be <code>null</code>
     */
    public void copy(final DataVerification otherBean)
    {
        if (null != otherBean)
        {
            this.setVerificationDataConfigId(otherBean.getVerificationDataConfigId());
            this.setVerificationDataName(otherBean.getVerificationDataName());
            this.setVerificationStatus(otherBean.getVerificationStatus());
            this.setVerificationBy(otherBean.getVerificationBy());
            this.setVerificationReport(otherBean.getVerificationReport());
            this.setScore(otherBean.getScore());
            this.setKeyFieldMatches(otherBean.getKeyFieldMatches());
            this.setHasMandatoryFields(otherBean.getHasMandatoryFields());
        }
    }

    /**
     * TODO: Model Documentation for
     * DataVerification.verificationDataConfigId
     * Get the verificationDataConfigId Attribute
     * @return verificationDataConfigId String
     */
    public String getVerificationDataConfigId()
    {
        return this.verificationDataConfigId;
    }

    /**
     * TODO: Model Documentation for
     * DataVerification.verificationDataConfigId
     * @param value String
     */
    public void setVerificationDataConfigId(final String value)
    {
        this.verificationDataConfigId = value;
    }

    /**
     * TODO: Model Documentation for DataVerification.verificationDataName
     * Get the verificationDataName Attribute
     * @return verificationDataName String
     */
    public String getVerificationDataName()
    {
        return this.verificationDataName;
    }

    /**
     * TODO: Model Documentation for DataVerification.verificationDataName
     * @param value String
     */
    public void setVerificationDataName(final String value)
    {
        this.verificationDataName = value;
    }

    /**
     * TODO: Model Documentation for DataVerification.verificationStatus
     * Get the verificationStatus Attribute
     * @return verificationStatus DataVerificationStatus
     */
    public DataVerificationStatus getVerificationStatus()
    {
        return this.verificationStatus;
    }

    /**
     * TODO: Model Documentation for DataVerification.verificationStatus
     * @param value DataVerificationStatus
     */
    public void setVerificationStatus(final DataVerificationStatus value)
    {
        this.verificationStatus = value;
    }

    /**
     * TODO: Model Documentation for DataVerification.verificationBy
     * Get the verificationBy Attribute
     * @return verificationBy String
     */
    public String getVerificationBy()
    {
        return this.verificationBy;
    }

    /**
     * TODO: Model Documentation for DataVerification.verificationBy
     * @param value String
     */
    public void setVerificationBy(final String value)
    {
        this.verificationBy = value;
    }

    /**
     * TODO: Model Documentation for DataVerification.verificationReport
     * Get the verificationReport Attribute
     * @return verificationReport String
     */
    public String getVerificationReport()
    {
        return this.verificationReport;
    }

    /**
     * TODO: Model Documentation for DataVerification.verificationReport
     * @param value String
     */
    public void setVerificationReport(final String value)
    {
        this.verificationReport = value;
    }

    /**
     * TODO: Model Documentation for DataVerification.score
     * Get the score Attribute
     * @return score Double
     */
    public Double getScore()
    {
        return this.score;
    }

    /**
     * TODO: Model Documentation for DataVerification.score
     * @param value Double
     */
    public void setScore(final Double value)
    {
        this.score = value;
    }

    /**
     * TODO: Model Documentation for DataVerification.keyFieldMatches
     * Get the keyFieldMatches Attribute
     * @return keyFieldMatches List<KeyFieldMatchResult>
     */
    public List<KeyFieldMatchResult> getKeyFieldMatches()
    {
        if (this.keyFieldMatches == null)
        {
            this.keyFieldMatches = new ArrayList<KeyFieldMatchResult>();
        }
        return this.keyFieldMatches;
    }

    /**
     * TODO: Model Documentation for DataVerification.keyFieldMatches
     * @param value List<KeyFieldMatchResult>
     */
    public void setKeyFieldMatches(final List<KeyFieldMatchResult> value)
    {
        this.keyFieldMatches = value;
    }

    /**
     * TODO: Model Documentation for DataVerification.hasMandatoryFields
     * Get the hasMandatoryFields Attribute
     * @return hasMandatoryFields Boolean
     */
    public Boolean getHasMandatoryFields()
    {
        return this.hasMandatoryFields;
    }

    /**
     * TODO: Model Documentation for DataVerification.hasMandatoryFields
     * @param value Boolean
     */
    public void setHasMandatoryFields(final Boolean value)
    {
        this.hasMandatoryFields = value;
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
        DataVerification rhs = (DataVerification) object;
        return new EqualsBuilder()
            .append(this.getVerificationDataConfigId(), rhs.getVerificationDataConfigId())
            .append(this.getVerificationDataName(), rhs.getVerificationDataName())
            .append(this.getVerificationStatus(), rhs.getVerificationStatus())
            .append(this.getVerificationBy(), rhs.getVerificationBy())
            .append(this.getVerificationReport(), rhs.getVerificationReport())
            .append(this.getScore(), rhs.getScore())
            .append(this.getKeyFieldMatches(), rhs.getKeyFieldMatches())
            .append(this.getHasMandatoryFields(), rhs.getHasMandatoryFields())
            .isEquals();
    }

    /**
     * @param object to compare this object against
     * @return int if equal
     * @see Comparable#compareTo(Object)
     */
    public int compareTo(final DataVerification object)
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
            .append(this.getVerificationDataConfigId(), object.getVerificationDataConfigId())
            .append(this.getVerificationDataName(), object.getVerificationDataName())
            .append(this.getVerificationStatus(), object.getVerificationStatus())
            .append(this.getVerificationBy(), object.getVerificationBy())
            .append(this.getVerificationReport(), object.getVerificationReport())
            .append(this.getScore(), object.getScore())
            .append(this.getKeyFieldMatches(), object.getKeyFieldMatches())
            .append(this.getHasMandatoryFields(), object.getHasMandatoryFields())
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
            .append(this.getVerificationDataConfigId())
            .append(this.getVerificationDataName())
            .append(this.getVerificationStatus())
            .append(this.getVerificationBy())
            .append(this.getVerificationReport())
            .append(this.getScore())
            .append(this.getKeyFieldMatches())
            .append(this.getHasMandatoryFields())
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
        builder.append("\t\"verificationDataConfigId\" : \"" + this.getVerificationDataConfigId() + "\"");
        builder.append("\t,\"verificationDataName\" : \"" + this.getVerificationDataName() + "\"");
        builder.append("\t,\"verificationStatus\" : \"" + this.getVerificationStatus() + "\"");
        builder.append("\t,\"verificationBy\" : \"" + this.getVerificationBy() + "\"");
        builder.append("\t,\"verificationReport\" : \"" + this.getVerificationReport() + "\"");
        builder.append("\t,\"score\" : \"" + this.getScore() + "\"");
        builder.append("\t,\"keyFieldMatches\" : \"" + this.getKeyFieldMatches() + "\"");
        builder.append("\t,\"hasMandatoryFields\" : \"" + this.getHasMandatoryFields() + "\"");
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

        final DataVerification that = (DataVerification)thatObject;
        return
            equal(this.getVerificationDataConfigId(), that.getVerificationDataConfigId())
            && equal(this.getVerificationDataName(), that.getVerificationDataName())
            && equal(this.getVerificationStatus(), that.getVerificationStatus())
            && equal(this.getVerificationBy(), that.getVerificationBy())
            && equal(this.getVerificationReport(), that.getVerificationReport())
            && equal(this.getScore(), that.getScore())
            && equal(this.getKeyFieldMatches(), that.getKeyFieldMatches())
            && equal(this.getHasMandatoryFields(), that.getHasMandatoryFields())
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