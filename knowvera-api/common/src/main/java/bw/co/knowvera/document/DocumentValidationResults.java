package bw.co.knowvera.document;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Map;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.jspecify.annotations.Nullable;

/**
 * TODO: Model Documentation for DocumentValidationResults
 */
public class DocumentValidationResults
    implements Serializable, Comparable<DocumentValidationResults>
{
    /** The serial version UID of this class. Needed for serialization. */
    private static final long serialVersionUID = 8588918393230000635L;

    // Class attributes
    /**
     * TODO: Model Documentation for DocumentValidationResults.expectedType
     */
    protected String expectedType;
    /**
     * TODO: Model Documentation for DocumentValidationResults.detectedType
     */
    protected String detectedType;
    /**
     * TODO: Model Documentation for DocumentValidationResults.match
     */
    @Nullable
    protected Boolean match = false;
    /**
     * TODO: Model Documentation for DocumentValidationResults.score
     */
    @Nullable
    protected Double score;
    /**
     * TODO: Model Documentation for DocumentValidationResults.signalScores
     */
    protected Map signalScores;
    /**
     * TODO: Model Documentation for DocumentValidationResults.reason
     */
    protected String reason;
    /**
     * TODO: Model Documentation for
     * DocumentValidationResults.informationMatchResults
     */
    protected DocumentVerificationStatus informationMatchResults;
    /**
     * TODO: Model Documentation for
     * DocumentValidationResults.informationMatchDetails
     */
    @Nullable
    protected String informationMatchDetails;
    /**
     * TODO: Model Documentation for DocumentValidationResults.typeMatch
     */
    @Nullable
    protected Boolean typeMatch = false;

    /** Default Constructor with no properties */
    public DocumentValidationResults()
    {
        // Documented empty block - avoid compiler warning - no super constructor
    }

    /**
     * Constructor taking only required properties
     * @param expectedTypeIn String TODO: Model Documentation for DocumentValidationResults.expectedType
     * @param detectedTypeIn String TODO: Model Documentation for DocumentValidationResults.detectedType
     * @param signalScoresIn Map TODO: Model Documentation for DocumentValidationResults.signalScores
     * @param reasonIn String TODO: Model Documentation for DocumentValidationResults.reason
     * @param informationMatchResultsIn DocumentVerificationStatus TODO: Model Documentation for
DocumentValidationResults.informationMatchResults
     */
    public DocumentValidationResults(final String expectedTypeIn, final String detectedTypeIn, final Map signalScoresIn, final String reasonIn, final DocumentVerificationStatus informationMatchResultsIn)
    {
        this.expectedType = expectedTypeIn;
        this.detectedType = detectedTypeIn;
        this.signalScores = signalScoresIn;
        this.reason = reasonIn;
        this.informationMatchResults = informationMatchResultsIn;
    }

    /**
     * Constructor with all properties
     * @param expectedTypeIn String
     * @param detectedTypeIn String
     * @param matchIn Boolean
     * @param scoreIn Double
     * @param signalScoresIn Map
     * @param reasonIn String
     * @param informationMatchResultsIn DocumentVerificationStatus
     * @param informationMatchDetailsIn String
     * @param typeMatchIn Boolean
     */
    public DocumentValidationResults(final String expectedTypeIn, final String detectedTypeIn, final Boolean matchIn, final Double scoreIn, final Map signalScoresIn, final String reasonIn, final DocumentVerificationStatus informationMatchResultsIn, final String informationMatchDetailsIn, final Boolean typeMatchIn)
    {
        this.expectedType = expectedTypeIn;
        this.detectedType = detectedTypeIn;
        this.match = matchIn;
        this.score = scoreIn;
        this.signalScores = signalScoresIn;
        this.reason = reasonIn;
        this.informationMatchResults = informationMatchResultsIn;
        this.informationMatchDetails = informationMatchDetailsIn;
        this.typeMatch = typeMatchIn;
    }

    /**
     * Copies constructor from other DocumentValidationResults
     *
     * @param otherBean Cannot be <code>null</code>
     * @throws NullPointerException if the argument is <code>null</code>
     */
    public DocumentValidationResults(final DocumentValidationResults otherBean)
    {
        this.expectedType = otherBean.getExpectedType();
        this.detectedType = otherBean.getDetectedType();
        this.match = otherBean.getMatch();
        this.score = otherBean.getScore();
        this.signalScores = otherBean.getSignalScores();
        this.reason = otherBean.getReason();
        this.informationMatchResults = otherBean.getInformationMatchResults();
        this.informationMatchDetails = otherBean.getInformationMatchDetails();
        this.typeMatch = otherBean.getTypeMatch();
    }

    /**
     * Copies all properties from the argument value object into this value object.
     * @param otherBean Cannot be <code>null</code>
     */
    public void copy(final DocumentValidationResults otherBean)
    {
        if (null != otherBean)
        {
            this.setExpectedType(otherBean.getExpectedType());
            this.setDetectedType(otherBean.getDetectedType());
            this.setMatch(otherBean.getMatch());
            this.setScore(otherBean.getScore());
            this.setSignalScores(otherBean.getSignalScores());
            this.setReason(otherBean.getReason());
            this.setInformationMatchResults(otherBean.getInformationMatchResults());
            this.setInformationMatchDetails(otherBean.getInformationMatchDetails());
            this.setTypeMatch(otherBean.getTypeMatch());
        }
    }

    /**
     * TODO: Model Documentation for DocumentValidationResults.expectedType
     * Get the expectedType Attribute
     * @return expectedType String
     */
    public String getExpectedType()
    {
        return this.expectedType;
    }

    /**
     * TODO: Model Documentation for DocumentValidationResults.expectedType
     * @param value String
     */
    public void setExpectedType(final String value)
    {
        this.expectedType = value;
    }

    /**
     * TODO: Model Documentation for DocumentValidationResults.detectedType
     * Get the detectedType Attribute
     * @return detectedType String
     */
    public String getDetectedType()
    {
        return this.detectedType;
    }

    /**
     * TODO: Model Documentation for DocumentValidationResults.detectedType
     * @param value String
     */
    public void setDetectedType(final String value)
    {
        this.detectedType = value;
    }

    /**
     * TODO: Model Documentation for DocumentValidationResults.match
     * Get the match Attribute
     * @return match Boolean
     */
    public Boolean getMatch()
    {
        return this.match;
    }

    /**
     * TODO: Model Documentation for DocumentValidationResults.match
     * @param value Boolean
     */
    public void setMatch(final Boolean value)
    {
        this.match = value;
    }

    /**
     * TODO: Model Documentation for DocumentValidationResults.score
     * Get the score Attribute
     * @return score Double
     */
    public Double getScore()
    {
        return this.score;
    }

    /**
     * TODO: Model Documentation for DocumentValidationResults.score
     * @param value Double
     */
    public void setScore(final Double value)
    {
        this.score = value;
    }

    /**
     * TODO: Model Documentation for DocumentValidationResults.signalScores
     * Get the signalScores Attribute
     * @return signalScores Map
     */
    public Map getSignalScores()
    {
        return this.signalScores;
    }

    /**
     * TODO: Model Documentation for DocumentValidationResults.signalScores
     * @param value Map
     */
    public void setSignalScores(final Map value)
    {
        this.signalScores = value;
    }

    /**
     * TODO: Model Documentation for DocumentValidationResults.reason
     * Get the reason Attribute
     * @return reason String
     */
    public String getReason()
    {
        return this.reason;
    }

    /**
     * TODO: Model Documentation for DocumentValidationResults.reason
     * @param value String
     */
    public void setReason(final String value)
    {
        this.reason = value;
    }

    /**
     * TODO: Model Documentation for
     * DocumentValidationResults.informationMatchResults
     * Get the informationMatchResults Attribute
     * @return informationMatchResults DocumentVerificationStatus
     */
    public DocumentVerificationStatus getInformationMatchResults()
    {
        return this.informationMatchResults;
    }

    /**
     * TODO: Model Documentation for
     * DocumentValidationResults.informationMatchResults
     * @param value DocumentVerificationStatus
     */
    public void setInformationMatchResults(final DocumentVerificationStatus value)
    {
        this.informationMatchResults = value;
    }

    /**
     * TODO: Model Documentation for
     * DocumentValidationResults.informationMatchDetails
     * Get the informationMatchDetails Attribute
     * @return informationMatchDetails String
     */
    public String getInformationMatchDetails()
    {
        return this.informationMatchDetails;
    }

    /**
     * TODO: Model Documentation for
     * DocumentValidationResults.informationMatchDetails
     * @param value String
     */
    public void setInformationMatchDetails(final String value)
    {
        this.informationMatchDetails = value;
    }

    /**
     * TODO: Model Documentation for DocumentValidationResults.typeMatch
     * Get the typeMatch Attribute
     * @return typeMatch Boolean
     */
    public Boolean getTypeMatch()
    {
        return this.typeMatch;
    }

    /**
     * TODO: Model Documentation for DocumentValidationResults.typeMatch
     * @param value Boolean
     */
    public void setTypeMatch(final Boolean value)
    {
        this.typeMatch = value;
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
        DocumentValidationResults rhs = (DocumentValidationResults) object;
        return new EqualsBuilder()
            .append(this.getExpectedType(), rhs.getExpectedType())
            .append(this.getDetectedType(), rhs.getDetectedType())
            .append(this.getMatch(), rhs.getMatch())
            .append(this.getScore(), rhs.getScore())
            .append(this.getSignalScores(), rhs.getSignalScores())
            .append(this.getReason(), rhs.getReason())
            .append(this.getInformationMatchResults(), rhs.getInformationMatchResults())
            .append(this.getInformationMatchDetails(), rhs.getInformationMatchDetails())
            .append(this.getTypeMatch(), rhs.getTypeMatch())
            .isEquals();
    }

    /**
     * @param object to compare this object against
     * @return int if equal
     * @see Comparable#compareTo(Object)
     */
    public int compareTo(final DocumentValidationResults object)
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
            .append(this.getExpectedType(), object.getExpectedType())
            .append(this.getDetectedType(), object.getDetectedType())
            .append(this.getMatch(), object.getMatch())
            .append(this.getScore(), object.getScore())
            .append(this.getSignalScores(), object.getSignalScores())
            .append(this.getReason(), object.getReason())
            .append(this.getInformationMatchResults(), object.getInformationMatchResults())
            .append(this.getInformationMatchDetails(), object.getInformationMatchDetails())
            .append(this.getTypeMatch(), object.getTypeMatch())
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
            .append(this.getExpectedType())
            .append(this.getDetectedType())
            .append(this.getMatch())
            .append(this.getScore())
            .append(this.getSignalScores())
            .append(this.getReason())
            .append(this.getInformationMatchResults())
            .append(this.getInformationMatchDetails())
            .append(this.getTypeMatch())
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
        builder.append("\t\"expectedType\" : \"" + this.getExpectedType() + "\"");
        builder.append("\t,\"detectedType\" : \"" + this.getDetectedType() + "\"");
        builder.append("\t,\"match\" : \"" + this.getMatch() + "\"");
        builder.append("\t,\"score\" : \"" + this.getScore() + "\"");
        builder.append("\t,\"signalScores\" : \"" + this.getSignalScores() + "\"");
        builder.append("\t,\"reason\" : \"" + this.getReason() + "\"");
        builder.append("\t,\"informationMatchResults\" : \"" + this.getInformationMatchResults() + "\"");
        builder.append("\t,\"informationMatchDetails\" : \"" + this.getInformationMatchDetails() + "\"");
        builder.append("\t,\"typeMatch\" : \"" + this.getTypeMatch() + "\"");
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

        final DocumentValidationResults that = (DocumentValidationResults)thatObject;
        return
            equal(this.getExpectedType(), that.getExpectedType())
            && equal(this.getDetectedType(), that.getDetectedType())
            && equal(this.getMatch(), that.getMatch())
            && equal(this.getScore(), that.getScore())
            && equal(this.getSignalScores(), that.getSignalScores())
            && equal(this.getReason(), that.getReason())
            && equal(this.getInformationMatchResults(), that.getInformationMatchResults())
            && equal(this.getInformationMatchDetails(), that.getInformationMatchDetails())
            && equal(this.getTypeMatch(), that.getTypeMatch())
        ;
    }

    /**
     * This is a convenient helper method which is able to detect whether or not two values are equal. Two values
     * are equal when they are both {@code null}, are arrays of the same length with equal elements or are
     * equal objects (this includes {@link java.util.Collection} and {@link Map} instances).
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
        else // note that the following also covers java.util.Collection and Map
        {
            equal = first.equals(second);
        }

        return equal;
    }

}