package bw.co.knowvera.document.type.verification;

import bw.co.knowvera.AuditableDTO;
import bw.co.knowvera.document.type.field.ExpectedFieldDTO;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.jspecify.annotations.Nullable;

/**
 * TODO: Model Documentation for VerificationDataConfigDTO
 */
public class VerificationDataConfigDTO
    extends AuditableDTO
{
    /** The serial version UID of this class. Needed for serialization. */
    private static final long serialVersionUID = -7507413915626012706L;

    // Class attributes
    /**
     * TODO: Model Documentation for
     * VerificationDataConfigDTO.name
     */
    protected String name;
    /**
     * TODO: Model Documentation for
     * VerificationDataConfigDTO.documentTypeId
     */
    @Nullable
    protected String documentTypeId;
    /**
     * TODO: Model Documentation for
     * VerificationDataConfigDTO.documentType
     */
    @Nullable
    protected String documentType;
    /**
     * TODO: Model Documentation for
     * VerificationDataConfigDTO.expectedFields
     */
    @Nullable
    protected List<ExpectedFieldDTO> expectedFields;

    /** Default Constructor with no properties */
    public VerificationDataConfigDTO()
    {
        super();
    }

    /**
     * Constructor taking only required properties
     * @param nameIn String TODO: Model Documentation for
VerificationDataConfigDTO.name
     */
    public VerificationDataConfigDTO(final String nameIn)
    {
        super();
        this.name = nameIn;
    }

    /**
     * Constructor with all properties
     * @param idIn String
     * @param createdByIn String
     * @param createdAtIn LocalDateTime
     * @param modifiedByIn String
     * @param modifiedAtIn LocalDateTime
     * @param nameIn String
     * @param documentTypeIdIn String
     * @param documentTypeIn String
     * @param expectedFieldsIn List<ExpectedFieldDTO>
     */
    public VerificationDataConfigDTO(final String idIn, final String createdByIn, final LocalDateTime createdAtIn, final String modifiedByIn, final LocalDateTime modifiedAtIn, final String nameIn, final String documentTypeIdIn, final String documentTypeIn, final List<ExpectedFieldDTO> expectedFieldsIn)
    {
        super(idIn, createdByIn, createdAtIn, modifiedByIn, modifiedAtIn);
        this.name = nameIn;
        this.documentTypeId = documentTypeIdIn;
        this.documentType = documentTypeIn;
        this.expectedFields = expectedFieldsIn;
    }

    /**
     * Copies constructor from other VerificationDataConfigDTO
     *
     * @param otherBean Cannot be <code>null</code>
     * @throws NullPointerException if the argument is <code>null</code>
     */
    public VerificationDataConfigDTO(final VerificationDataConfigDTO otherBean)
    {
        super(otherBean);
        this.name = otherBean.getName();
        this.documentTypeId = otherBean.getDocumentTypeId();
        this.documentType = otherBean.getDocumentType();
        this.expectedFields = otherBean.getExpectedFields();
    }

    /**
     * Copies all properties from the argument value object into this value object.
     * @param otherBean Cannot be <code>null</code>
     */
    public void copy(final VerificationDataConfigDTO otherBean)
    {
        if (null != otherBean)
        {
            super.copy(otherBean);
            this.setName(otherBean.getName());
            this.setDocumentTypeId(otherBean.getDocumentTypeId());
            this.setDocumentType(otherBean.getDocumentType());
            this.setExpectedFields(otherBean.getExpectedFields());
        }
    }

    /**
     * TODO: Model Documentation for
     * VerificationDataConfigDTO.name
     * Get the name Attribute
     * @return name String
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * TODO: Model Documentation for
     * VerificationDataConfigDTO.name
     * @param value String
     */
    public void setName(final String value)
    {
        this.name = value;
    }

    /**
     * TODO: Model Documentation for
     * VerificationDataConfigDTO.documentTypeId
     * Get the documentTypeId Attribute
     * @return documentTypeId String
     */
    public String getDocumentTypeId()
    {
        return this.documentTypeId;
    }

    /**
     * TODO: Model Documentation for
     * VerificationDataConfigDTO.documentTypeId
     * @param value String
     */
    public void setDocumentTypeId(final String value)
    {
        this.documentTypeId = value;
    }

    /**
     * TODO: Model Documentation for
     * VerificationDataConfigDTO.documentType
     * Get the documentType Attribute
     * @return documentType String
     */
    public String getDocumentType()
    {
        return this.documentType;
    }

    /**
     * TODO: Model Documentation for
     * VerificationDataConfigDTO.documentType
     * @param value String
     */
    public void setDocumentType(final String value)
    {
        this.documentType = value;
    }

    /**
     * TODO: Model Documentation for
     * VerificationDataConfigDTO.expectedFields
     * Get the expectedFields Attribute
     * @return expectedFields List<ExpectedFieldDTO>
     */
    public List<ExpectedFieldDTO> getExpectedFields()
    {
        if (this.expectedFields == null)
        {
            this.expectedFields = new ArrayList<ExpectedFieldDTO>();
        }
        return this.expectedFields;
    }

    /**
     * TODO: Model Documentation for
     * VerificationDataConfigDTO.expectedFields
     * @param value List<ExpectedFieldDTO>
     */
    public void setExpectedFields(final List<ExpectedFieldDTO> value)
    {
        this.expectedFields = value;
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
        VerificationDataConfigDTO rhs = (VerificationDataConfigDTO) object;
        return new EqualsBuilder()
            .appendSuper(super.equals(object))
            .append(this.getName(), rhs.getName())
            .append(this.getDocumentTypeId(), rhs.getDocumentTypeId())
            .append(this.getDocumentType(), rhs.getDocumentType())
            .append(this.getExpectedFields(), rhs.getExpectedFields())
            .isEquals();
    }

    /**
     * @param object to compare this object against
     * @return int if equal
     * @see Comparable#compareTo(Object)
     */
    public int compareTo(final AuditableDTO object)
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
        if (!(object instanceof VerificationDataConfigDTO))
        {
            return -1;
        }
        VerificationDataConfigDTO myClass = (VerificationDataConfigDTO)object;
        return new CompareToBuilder()
            .appendSuper(super.compareTo(object))
            .append(this.getName(), myClass.getName())
            .append(this.getDocumentTypeId(), myClass.getDocumentTypeId())
            .append(this.getDocumentType(), myClass.getDocumentType())
            .append(this.getExpectedFields(), myClass.getExpectedFields())
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
            .appendSuper(super.hashCode())
            .append(this.getName())
            .append(this.getDocumentTypeId())
            .append(this.getDocumentType())
            .append(this.getExpectedFields())
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
        builder.append("\t\"name\" : \"" + this.getName() + "\"");
        builder.append("\t,\"documentTypeId\" : \"" + this.getDocumentTypeId() + "\"");
        builder.append("\t,\"documentType\" : \"" + this.getDocumentType() + "\"");
        builder.append("\t,\"expectedFields\" : \"" + this.getExpectedFields() + "\"");
        builder.append("\t,\"id\" : \"" + this.getId() + "\"");
        builder.append("\t,\"createdBy\" : \"" + this.getCreatedBy() + "\"");
        builder.append("\t,\"createdAt\" : \"" + this.getCreatedAt() + "\"");
        builder.append("\t,\"modifiedBy\" : \"" + this.getModifiedBy() + "\"");
        builder.append("\t,\"modifiedAt\" : \"" + this.getModifiedAt() + "\"");
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
    @Override
    public boolean equalProperties(final Object thatObject)
    {
        if (thatObject == null || !this.getClass().isAssignableFrom(thatObject.getClass()))
        {
            return false;
        }

        @SuppressWarnings("unchecked")
        final VerificationDataConfigDTO that = (VerificationDataConfigDTO)thatObject;
        return super.equalProperties(that)
            && equal(this.getName(), that.getName())
            && equal(this.getDocumentTypeId(), that.getDocumentTypeId())
            && equal(this.getDocumentType(), that.getDocumentType())
            && equal(this.getExpectedFields(), that.getExpectedFields())
        ;
    }

}