package bw.co.knowvera.settings;

import bw.co.knowvera.document.type.DocumentTypeDTO;
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
 * TODO: Model Documentation for DocumentRequirements
 */
public class DocumentRequirements
    implements Serializable, Comparable<DocumentRequirements>
{
    /** The serial version UID of this class. Needed for serialization. */
    private static final long serialVersionUID = 3654846550194450070L;

    // Class attributes
    /**
     * TODO: Model Documentation for
     * DocumentRequirements.organisationDocuments
     */
    @Nullable
    protected List<DocumentTypeDTO> organisationDocuments;
    /**
     * TODO: Model Documentation for
     * DocumentRequirements.individualDocuments
     */
    @Nullable
    protected List<DocumentTypeDTO> individualDocuments;
    /**
     * TODO: Model Documentation for DocumentRequirements.indKycDocuments
     */
    @Nullable
    protected List<DocumentTypeDTO> indKycDocuments;
    /**
     * TODO: Model Documentation for DocumentRequirements.orgKycDocuments
     */
    @Nullable
    protected List<DocumentTypeDTO> orgKycDocuments;
    /**
     * TODO: Model Documentation for
     * DocumentRequirements.clientRequestFileType
     */
    @Nullable
    protected DocumentTypeDTO clientRequestFileType;
    /**
     * TODO: Model Documentation for DocumentRequirements.user
     */
    @Nullable
    protected String user;
    /**
     * TODO: Model Documentation for
     * DocumentRequirements.maxDataVerificationFailureThreshold
     */
    @Nullable
    protected Double maxDataVerificationFailureThreshold;
    /**
     * TODO: Model Documentation for
     * DocumentRequirements.dataVerificationThreshold
     */
    @Nullable
    protected Double dataVerificationThreshold;
    /**
     * TODO: Model Documentation for
     * DocumentRequirements.documentDurationLimit
     */
    @Nullable
    protected Integer documentDurationLimit;

    /**
     * Constructor taking only required properties
     */
    public DocumentRequirements()
    {
        // Documented empty block - avoid compiler warning - no super constructor
    }

    /**
     * Constructor with all properties
     * @param organisationDocumentsIn List<DocumentTypeDTO>
     * @param individualDocumentsIn List<DocumentTypeDTO>
     * @param indKycDocumentsIn List<DocumentTypeDTO>
     * @param orgKycDocumentsIn List<DocumentTypeDTO>
     * @param clientRequestFileTypeIn DocumentTypeDTO
     * @param userIn String
     * @param maxDataVerificationFailureThresholdIn Double
     * @param dataVerificationThresholdIn Double
     * @param documentDurationLimitIn Integer
     */
    public DocumentRequirements(final List<DocumentTypeDTO> organisationDocumentsIn, final List<DocumentTypeDTO> individualDocumentsIn, final List<DocumentTypeDTO> indKycDocumentsIn, final List<DocumentTypeDTO> orgKycDocumentsIn, final DocumentTypeDTO clientRequestFileTypeIn, final String userIn, final Double maxDataVerificationFailureThresholdIn, final Double dataVerificationThresholdIn, final Integer documentDurationLimitIn)
    {
        this.organisationDocuments = organisationDocumentsIn;
        this.individualDocuments = individualDocumentsIn;
        this.indKycDocuments = indKycDocumentsIn;
        this.orgKycDocuments = orgKycDocumentsIn;
        this.clientRequestFileType = clientRequestFileTypeIn;
        this.user = userIn;
        this.maxDataVerificationFailureThreshold = maxDataVerificationFailureThresholdIn;
        this.dataVerificationThreshold = dataVerificationThresholdIn;
        this.documentDurationLimit = documentDurationLimitIn;
    }

    /**
     * Copies constructor from other DocumentRequirements
     *
     * @param otherBean Cannot be <code>null</code>
     * @throws NullPointerException if the argument is <code>null</code>
     */
    public DocumentRequirements(final DocumentRequirements otherBean)
    {
        this.organisationDocuments = otherBean.getOrganisationDocuments();
        this.individualDocuments = otherBean.getIndividualDocuments();
        this.indKycDocuments = otherBean.getIndKycDocuments();
        this.orgKycDocuments = otherBean.getOrgKycDocuments();
        this.clientRequestFileType = otherBean.getClientRequestFileType();
        this.user = otherBean.getUser();
        this.maxDataVerificationFailureThreshold = otherBean.getMaxDataVerificationFailureThreshold();
        this.dataVerificationThreshold = otherBean.getDataVerificationThreshold();
        this.documentDurationLimit = otherBean.getDocumentDurationLimit();
    }

    /**
     * Copies all properties from the argument value object into this value object.
     * @param otherBean Cannot be <code>null</code>
     */
    public void copy(final DocumentRequirements otherBean)
    {
        if (null != otherBean)
        {
            this.setOrganisationDocuments(otherBean.getOrganisationDocuments());
            this.setIndividualDocuments(otherBean.getIndividualDocuments());
            this.setIndKycDocuments(otherBean.getIndKycDocuments());
            this.setOrgKycDocuments(otherBean.getOrgKycDocuments());
            this.setClientRequestFileType(otherBean.getClientRequestFileType());
            this.setUser(otherBean.getUser());
            this.setMaxDataVerificationFailureThreshold(otherBean.getMaxDataVerificationFailureThreshold());
            this.setDataVerificationThreshold(otherBean.getDataVerificationThreshold());
            this.setDocumentDurationLimit(otherBean.getDocumentDurationLimit());
        }
    }

    /**
     * TODO: Model Documentation for
     * DocumentRequirements.organisationDocuments
     * Get the organisationDocuments Attribute
     * @return organisationDocuments List<DocumentTypeDTO>
     */
    public List<DocumentTypeDTO> getOrganisationDocuments()
    {
        if (this.organisationDocuments == null)
        {
            this.organisationDocuments = new ArrayList<DocumentTypeDTO>();
        }
        return this.organisationDocuments;
    }

    /**
     * TODO: Model Documentation for
     * DocumentRequirements.organisationDocuments
     * @param value List<DocumentTypeDTO>
     */
    public void setOrganisationDocuments(final List<DocumentTypeDTO> value)
    {
        this.organisationDocuments = value;
    }

    /**
     * TODO: Model Documentation for
     * DocumentRequirements.individualDocuments
     * Get the individualDocuments Attribute
     * @return individualDocuments List<DocumentTypeDTO>
     */
    public List<DocumentTypeDTO> getIndividualDocuments()
    {
        if (this.individualDocuments == null)
        {
            this.individualDocuments = new ArrayList<DocumentTypeDTO>();
        }
        return this.individualDocuments;
    }

    /**
     * TODO: Model Documentation for
     * DocumentRequirements.individualDocuments
     * @param value List<DocumentTypeDTO>
     */
    public void setIndividualDocuments(final List<DocumentTypeDTO> value)
    {
        this.individualDocuments = value;
    }

    /**
     * TODO: Model Documentation for DocumentRequirements.indKycDocuments
     * Get the indKycDocuments Attribute
     * @return indKycDocuments List<DocumentTypeDTO>
     */
    public List<DocumentTypeDTO> getIndKycDocuments()
    {
        if (this.indKycDocuments == null)
        {
            this.indKycDocuments = new ArrayList<DocumentTypeDTO>();
        }
        return this.indKycDocuments;
    }

    /**
     * TODO: Model Documentation for DocumentRequirements.indKycDocuments
     * @param value List<DocumentTypeDTO>
     */
    public void setIndKycDocuments(final List<DocumentTypeDTO> value)
    {
        this.indKycDocuments = value;
    }

    /**
     * TODO: Model Documentation for DocumentRequirements.orgKycDocuments
     * Get the orgKycDocuments Attribute
     * @return orgKycDocuments List<DocumentTypeDTO>
     */
    public List<DocumentTypeDTO> getOrgKycDocuments()
    {
        if (this.orgKycDocuments == null)
        {
            this.orgKycDocuments = new ArrayList<DocumentTypeDTO>();
        }
        return this.orgKycDocuments;
    }

    /**
     * TODO: Model Documentation for DocumentRequirements.orgKycDocuments
     * @param value List<DocumentTypeDTO>
     */
    public void setOrgKycDocuments(final List<DocumentTypeDTO> value)
    {
        this.orgKycDocuments = value;
    }

    /**
     * TODO: Model Documentation for
     * DocumentRequirements.clientRequestFileType
     * Get the clientRequestFileType Attribute
     * @return clientRequestFileType DocumentTypeDTO
     */
    public DocumentTypeDTO getClientRequestFileType()
    {
        return this.clientRequestFileType;
    }

    /**
     * TODO: Model Documentation for
     * DocumentRequirements.clientRequestFileType
     * @param value DocumentTypeDTO
     */
    public void setClientRequestFileType(final DocumentTypeDTO value)
    {
        this.clientRequestFileType = value;
    }

    /**
     * TODO: Model Documentation for DocumentRequirements.user
     * Get the user Attribute
     * @return user String
     */
    public String getUser()
    {
        return this.user;
    }

    /**
     * TODO: Model Documentation for DocumentRequirements.user
     * @param value String
     */
    public void setUser(final String value)
    {
        this.user = value;
    }

    /**
     * TODO: Model Documentation for
     * DocumentRequirements.maxDataVerificationFailureThreshold
     * Get the maxDataVerificationFailureThreshold Attribute
     * @return maxDataVerificationFailureThreshold Double
     */
    public Double getMaxDataVerificationFailureThreshold()
    {
        return this.maxDataVerificationFailureThreshold;
    }

    /**
     * TODO: Model Documentation for
     * DocumentRequirements.maxDataVerificationFailureThreshold
     * @param value Double
     */
    public void setMaxDataVerificationFailureThreshold(final Double value)
    {
        this.maxDataVerificationFailureThreshold = value;
    }

    /**
     * TODO: Model Documentation for
     * DocumentRequirements.dataVerificationThreshold
     * Get the dataVerificationThreshold Attribute
     * @return dataVerificationThreshold Double
     */
    public Double getDataVerificationThreshold()
    {
        return this.dataVerificationThreshold;
    }

    /**
     * TODO: Model Documentation for
     * DocumentRequirements.dataVerificationThreshold
     * @param value Double
     */
    public void setDataVerificationThreshold(final Double value)
    {
        this.dataVerificationThreshold = value;
    }

    /**
     * TODO: Model Documentation for
     * DocumentRequirements.documentDurationLimit
     * Get the documentDurationLimit Attribute
     * @return documentDurationLimit Integer
     */
    public Integer getDocumentDurationLimit()
    {
        return this.documentDurationLimit;
    }

    /**
     * TODO: Model Documentation for
     * DocumentRequirements.documentDurationLimit
     * @param value Integer
     */
    public void setDocumentDurationLimit(final Integer value)
    {
        this.documentDurationLimit = value;
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
        DocumentRequirements rhs = (DocumentRequirements) object;
        return new EqualsBuilder()
            .append(this.getOrganisationDocuments(), rhs.getOrganisationDocuments())
            .append(this.getIndividualDocuments(), rhs.getIndividualDocuments())
            .append(this.getIndKycDocuments(), rhs.getIndKycDocuments())
            .append(this.getOrgKycDocuments(), rhs.getOrgKycDocuments())
            .append(this.getClientRequestFileType(), rhs.getClientRequestFileType())
            .append(this.getUser(), rhs.getUser())
            .append(this.getMaxDataVerificationFailureThreshold(), rhs.getMaxDataVerificationFailureThreshold())
            .append(this.getDataVerificationThreshold(), rhs.getDataVerificationThreshold())
            .append(this.getDocumentDurationLimit(), rhs.getDocumentDurationLimit())
            .isEquals();
    }

    /**
     * @param object to compare this object against
     * @return int if equal
     * @see Comparable#compareTo(Object)
     */
    public int compareTo(final DocumentRequirements object)
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
            .append(this.getOrganisationDocuments(), object.getOrganisationDocuments())
            .append(this.getIndividualDocuments(), object.getIndividualDocuments())
            .append(this.getIndKycDocuments(), object.getIndKycDocuments())
            .append(this.getOrgKycDocuments(), object.getOrgKycDocuments())
            .append(this.getClientRequestFileType(), object.getClientRequestFileType())
            .append(this.getUser(), object.getUser())
            .append(this.getMaxDataVerificationFailureThreshold(), object.getMaxDataVerificationFailureThreshold())
            .append(this.getDataVerificationThreshold(), object.getDataVerificationThreshold())
            .append(this.getDocumentDurationLimit(), object.getDocumentDurationLimit())
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
            .append(this.getOrganisationDocuments())
            .append(this.getIndividualDocuments())
            .append(this.getIndKycDocuments())
            .append(this.getOrgKycDocuments())
            .append(this.getClientRequestFileType())
            .append(this.getUser())
            .append(this.getMaxDataVerificationFailureThreshold())
            .append(this.getDataVerificationThreshold())
            .append(this.getDocumentDurationLimit())
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
        builder.append("\t\"organisationDocuments\" : \"" + this.getOrganisationDocuments() + "\"");
        builder.append("\t,\"individualDocuments\" : \"" + this.getIndividualDocuments() + "\"");
        builder.append("\t,\"indKycDocuments\" : \"" + this.getIndKycDocuments() + "\"");
        builder.append("\t,\"orgKycDocuments\" : \"" + this.getOrgKycDocuments() + "\"");
        builder.append("\t,\"clientRequestFileType\" : \"" + this.getClientRequestFileType() + "\"");
        builder.append("\t,\"user\" : \"" + this.getUser() + "\"");
        builder.append("\t,\"maxDataVerificationFailureThreshold\" : \"" + this.getMaxDataVerificationFailureThreshold() + "\"");
        builder.append("\t,\"dataVerificationThreshold\" : \"" + this.getDataVerificationThreshold() + "\"");
        builder.append("\t,\"documentDurationLimit\" : \"" + this.getDocumentDurationLimit() + "\"");
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

        final DocumentRequirements that = (DocumentRequirements)thatObject;
        return
            equal(this.getOrganisationDocuments(), that.getOrganisationDocuments())
            && equal(this.getIndividualDocuments(), that.getIndividualDocuments())
            && equal(this.getIndKycDocuments(), that.getIndKycDocuments())
            && equal(this.getOrgKycDocuments(), that.getOrgKycDocuments())
            && equal(this.getClientRequestFileType(), that.getClientRequestFileType())
            && equal(this.getUser(), that.getUser())
            && equal(this.getMaxDataVerificationFailureThreshold(), that.getMaxDataVerificationFailureThreshold())
            && equal(this.getDataVerificationThreshold(), that.getDataVerificationThreshold())
            && equal(this.getDocumentDurationLimit(), that.getDocumentDurationLimit())
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