package bw.co.knowvera.document.type;

import bw.co.knowvera.AuditableDTO;
import bw.co.knowvera.TimePeriod;
import bw.co.knowvera.document.type.field.ExpectedFieldDTO;
import bw.co.knowvera.document.type.verification.VerificationDataConfigDTO;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.jspecify.annotations.Nullable;

/**
 * TODO: Model Documentation for DocumentTypeDTO
 */
public class DocumentTypeDTO
    extends AuditableDTO
{
    /** The serial version UID of this class. Needed for serialization. */
    private static final long serialVersionUID = 6857144212495260893L;

    // Class attributes
    /**
     * TODO: Model Documentation for DocumentTypeDTO.code
     */
    protected String code;
    /**
     * TODO: Model Documentation for DocumentTypeDTO.name
     */
    protected String name;
    /**
     * TODO: Model Documentation for DocumentTypeDTO.description
     */
    @Nullable
    protected String description;
    /**
     * TODO: Model Documentation for DocumentTypeDTO.expectedFields
     */
    @Nullable
    protected List<ExpectedFieldDTO> expectedFields;
    /**
     * TODO: Model Documentation for
     * DocumentTypeDTO.verificationDataConfigs
     */
    @Nullable
    protected List<VerificationDataConfigDTO> verificationDataConfigs;
    /**
     * TODO: Model Documentation for DocumentTypeDTO.expires
     */
    @Nullable
    protected Boolean expires = false;
    /**
     * TODO: Model Documentation for DocumentTypeDTO.expiryField
     */
    @Nullable
    protected ExpectedFieldDTO expiryField;
    /**
     * TODO: Model Documentation for DocumentTypeDTO.expiryPeriod
     */
    @Nullable
    protected TimePeriod expiryPeriod;
    /**
     * TODO: Model Documentation for DocumentTypeDTO.expiresIn
     */
    @Nullable
    protected Integer expiresIn;
    /**
     * TODO: Model Documentation for DocumentTypeDTO.expectedFieldCount
     */
    @Nullable
    protected Integer expectedFieldCount;
    /**
     * TODO: Model Documentation for
     * DocumentTypeDTO.verificationConfigCount
     */
    @Nullable
    protected Integer verificationConfigCount;
    /**
     * The veritas-processor profile that processes documents of this type (null: the default
     * profile, app.processing.default-profile).
     */
    @Nullable
    protected String processingProfileCode;

    /** Default Constructor with no properties */
    public DocumentTypeDTO()
    {
        super();
    }

    /**
     * Constructor taking only required properties
     * @param codeIn String TODO: Model Documentation for DocumentTypeDTO.code
     * @param nameIn String TODO: Model Documentation for DocumentTypeDTO.name
     */
    public DocumentTypeDTO(final String codeIn, final String nameIn)
    {
        super();
        this.code = codeIn;
        this.name = nameIn;
    }

    /**
     * Constructor with all properties
     * @param idIn String
     * @param createdByIn String
     * @param createdAtIn LocalDateTime
     * @param modifiedByIn String
     * @param modifiedAtIn LocalDateTime
     * @param codeIn String
     * @param nameIn String
     * @param descriptionIn String
     * @param expectedFieldsIn List<ExpectedFieldDTO>
     * @param verificationDataConfigsIn List<VerificationDataConfigDTO>
     * @param expiresIn Boolean
     * @param expiryFieldIn ExpectedFieldDTO
     * @param expiryPeriodIn TimePeriod
     * @param expiresInIn Integer
     * @param expectedFieldCountIn Integer
     * @param verificationConfigCountIn Integer
     */
    public DocumentTypeDTO(final String idIn, final String createdByIn, final LocalDateTime createdAtIn, final String modifiedByIn, final LocalDateTime modifiedAtIn, final String codeIn, final String nameIn, final String descriptionIn, final List<ExpectedFieldDTO> expectedFieldsIn, final List<VerificationDataConfigDTO> verificationDataConfigsIn, final Boolean expiresIn, final ExpectedFieldDTO expiryFieldIn, final TimePeriod expiryPeriodIn, final Integer expiresInIn, final Integer expectedFieldCountIn, final Integer verificationConfigCountIn)
    {
        super(idIn, createdByIn, createdAtIn, modifiedByIn, modifiedAtIn);
        this.code = codeIn;
        this.name = nameIn;
        this.description = descriptionIn;
        this.expectedFields = expectedFieldsIn;
        this.verificationDataConfigs = verificationDataConfigsIn;
        this.expires = expiresIn;
        this.expiryField = expiryFieldIn;
        this.expiryPeriod = expiryPeriodIn;
        this.expiresIn = expiresInIn;
        this.expectedFieldCount = expectedFieldCountIn;
        this.verificationConfigCount = verificationConfigCountIn;
    }

    /**
     * Copies constructor from other DocumentTypeDTO
     *
     * @param otherBean Cannot be <code>null</code>
     * @throws NullPointerException if the argument is <code>null</code>
     */
    public DocumentTypeDTO(final DocumentTypeDTO otherBean)
    {
        super(otherBean);
        this.code = otherBean.getCode();
        this.name = otherBean.getName();
        this.description = otherBean.getDescription();
        this.expectedFields = otherBean.getExpectedFields();
        this.verificationDataConfigs = otherBean.getVerificationDataConfigs();
        this.expires = otherBean.getExpires();
        this.expiryField = otherBean.getExpiryField();
        this.expiryPeriod = otherBean.getExpiryPeriod();
        this.expiresIn = otherBean.getExpiresIn();
        this.expectedFieldCount = otherBean.getExpectedFieldCount();
        this.verificationConfigCount = otherBean.getVerificationConfigCount();
        this.processingProfileCode = otherBean.getProcessingProfileCode();
    }

    /**
     * Copies all properties from the argument value object into this value object.
     * @param otherBean Cannot be <code>null</code>
     */
    public void copy(final DocumentTypeDTO otherBean)
    {
        if (null != otherBean)
        {
            super.copy(otherBean);
            this.setCode(otherBean.getCode());
            this.setName(otherBean.getName());
            this.setDescription(otherBean.getDescription());
            this.setExpectedFields(otherBean.getExpectedFields());
            this.setVerificationDataConfigs(otherBean.getVerificationDataConfigs());
            this.setExpires(otherBean.getExpires());
            this.setExpiryField(otherBean.getExpiryField());
            this.setExpiryPeriod(otherBean.getExpiryPeriod());
            this.setExpiresIn(otherBean.getExpiresIn());
            this.setExpectedFieldCount(otherBean.getExpectedFieldCount());
            this.setVerificationConfigCount(otherBean.getVerificationConfigCount());
            this.setProcessingProfileCode(otherBean.getProcessingProfileCode());
        }
    }

    /**
     * TODO: Model Documentation for DocumentTypeDTO.code
     * Get the code Attribute
     * @return code String
     */
    public String getCode()
    {
        return this.code;
    }

    /**
     * TODO: Model Documentation for DocumentTypeDTO.code
     * @param value String
     */
    public void setCode(final String value)
    {
        this.code = value;
    }

    /**
     * TODO: Model Documentation for DocumentTypeDTO.name
     * Get the name Attribute
     * @return name String
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * TODO: Model Documentation for DocumentTypeDTO.name
     * @param value String
     */
    public void setName(final String value)
    {
        this.name = value;
    }

    /**
     * TODO: Model Documentation for DocumentTypeDTO.description
     * Get the description Attribute
     * @return description String
     */
    public String getDescription()
    {
        return this.description;
    }

    /**
     * TODO: Model Documentation for DocumentTypeDTO.description
     * @param value String
     */
    public void setDescription(final String value)
    {
        this.description = value;
    }

    /**
     * TODO: Model Documentation for DocumentTypeDTO.expectedFields
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
     * TODO: Model Documentation for DocumentTypeDTO.expectedFields
     * @param value List<ExpectedFieldDTO>
     */
    public void setExpectedFields(final List<ExpectedFieldDTO> value)
    {
        this.expectedFields = value;
    }





    /**
     * TODO: Model Documentation for
     * DocumentTypeDTO.verificationDataConfigs
     * Get the verificationDataConfigs Attribute
     * @return verificationDataConfigs List<VerificationDataConfigDTO>
     */
    public List<VerificationDataConfigDTO> getVerificationDataConfigs()
    {
        if (this.verificationDataConfigs == null)
        {
            this.verificationDataConfigs = new ArrayList<VerificationDataConfigDTO>();
        }
        return this.verificationDataConfigs;
    }

    /**
     * TODO: Model Documentation for
     * DocumentTypeDTO.verificationDataConfigs
     * @param value List<VerificationDataConfigDTO>
     */
    public void setVerificationDataConfigs(final List<VerificationDataConfigDTO> value)
    {
        this.verificationDataConfigs = value;
    }

    /**
     * TODO: Model Documentation for DocumentTypeDTO.expires
     * Get the expires Attribute
     * @return expires Boolean
     */
    public Boolean getExpires()
    {
        return this.expires;
    }

    /**
     * TODO: Model Documentation for DocumentTypeDTO.expires
     * @param value Boolean
     */
    public void setExpires(final Boolean value)
    {
        this.expires = value;
    }

    /**
     * TODO: Model Documentation for DocumentTypeDTO.expiryField
     * Get the expiryField Attribute
     * @return expiryField ExpectedFieldDTO
     */
    public ExpectedFieldDTO getExpiryField()
    {
        return this.expiryField;
    }

    /**
     * TODO: Model Documentation for DocumentTypeDTO.expiryField
     * @param value ExpectedFieldDTO
     */
    public void setExpiryField(final ExpectedFieldDTO value)
    {
        this.expiryField = value;
    }

    /**
     * TODO: Model Documentation for DocumentTypeDTO.expiryPeriod
     * Get the expiryPeriod Attribute
     * @return expiryPeriod TimePeriod
     */
    public TimePeriod getExpiryPeriod()
    {
        return this.expiryPeriod;
    }

    /**
     * TODO: Model Documentation for DocumentTypeDTO.expiryPeriod
     * @param value TimePeriod
     */
    public void setExpiryPeriod(final TimePeriod value)
    {
        this.expiryPeriod = value;
    }

    /**
     * TODO: Model Documentation for DocumentTypeDTO.expiresIn
     * Get the expiresIn Attribute
     * @return expiresIn Integer
     */
    public Integer getExpiresIn()
    {
        return this.expiresIn;
    }

    /**
     * TODO: Model Documentation for DocumentTypeDTO.expiresIn
     * @param value Integer
     */
    public void setExpiresIn(final Integer value)
    {
        this.expiresIn = value;
    }

    /**
     * The veritas-processor profile for documents of this type
     * @return processingProfileCode String
     */
    public String getProcessingProfileCode()
    {
        return this.processingProfileCode;
    }

    /**
     * @param value String
     */
    public void setProcessingProfileCode(final String value)
    {
        this.processingProfileCode = value;
    }

    /**
     * TODO: Model Documentation for DocumentTypeDTO.expectedFieldCount
     * Get the expectedFieldCount Attribute
     * @return expectedFieldCount Integer
     */
    public Integer getExpectedFieldCount()
    {
        return this.expectedFieldCount;
    }

    /**
     * TODO: Model Documentation for DocumentTypeDTO.expectedFieldCount
     * @param value Integer
     */
    public void setExpectedFieldCount(final Integer value)
    {
        this.expectedFieldCount = value;
    }

    /**
     * TODO: Model Documentation for
     * DocumentTypeDTO.verificationConfigCount
     * Get the verificationConfigCount Attribute
     * @return verificationConfigCount Integer
     */
    public Integer getVerificationConfigCount()
    {
        return this.verificationConfigCount;
    }

    /**
     * TODO: Model Documentation for
     * DocumentTypeDTO.verificationConfigCount
     * @param value Integer
     */
    public void setVerificationConfigCount(final Integer value)
    {
        this.verificationConfigCount = value;
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
        DocumentTypeDTO rhs = (DocumentTypeDTO) object;
        return new EqualsBuilder()
            .appendSuper(super.equals(object))
            .append(this.getCode(), rhs.getCode())
            .append(this.getName(), rhs.getName())
            .append(this.getDescription(), rhs.getDescription())
            .append(this.getExpectedFields(), rhs.getExpectedFields())
            .append(this.getVerificationDataConfigs(), rhs.getVerificationDataConfigs())
            .append(this.getExpires(), rhs.getExpires())
            .append(this.getExpiryField(), rhs.getExpiryField())
            .append(this.getExpiryPeriod(), rhs.getExpiryPeriod())
            .append(this.getExpiresIn(), rhs.getExpiresIn())
            .append(this.getExpectedFieldCount(), rhs.getExpectedFieldCount())
            .append(this.getVerificationConfigCount(), rhs.getVerificationConfigCount())
            .append(this.getProcessingProfileCode(), rhs.getProcessingProfileCode())
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
        if (!(object instanceof DocumentTypeDTO))
        {
            return -1;
        }
        DocumentTypeDTO myClass = (DocumentTypeDTO)object;
        return new CompareToBuilder()
            .appendSuper(super.compareTo(object))
            .append(this.getCode(), myClass.getCode())
            .append(this.getName(), myClass.getName())
            .append(this.getDescription(), myClass.getDescription())
            .append(this.getExpectedFields(), myClass.getExpectedFields())
            .append(this.getVerificationDataConfigs(), myClass.getVerificationDataConfigs())
            .append(this.getExpires(), myClass.getExpires())
            .append(this.getExpiryField(), myClass.getExpiryField())
            .append(this.getExpiryPeriod(), myClass.getExpiryPeriod())
            .append(this.getExpiresIn(), myClass.getExpiresIn())
            .append(this.getExpectedFieldCount(), myClass.getExpectedFieldCount())
            .append(this.getVerificationConfigCount(), myClass.getVerificationConfigCount())
            .append(this.getProcessingProfileCode(), myClass.getProcessingProfileCode())
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
            .append(this.getCode())
            .append(this.getName())
            .append(this.getDescription())
            .append(this.getExpectedFields())
            .append(this.getVerificationDataConfigs())
            .append(this.getExpires())
            .append(this.getExpiryField())
            .append(this.getExpiryPeriod())
            .append(this.getExpiresIn())
            .append(this.getExpectedFieldCount())
            .append(this.getVerificationConfigCount())
            .append(this.getProcessingProfileCode())
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
        builder.append("\t\"code\" : \"" + this.getCode() + "\"");
        builder.append("\t,\"name\" : \"" + this.getName() + "\"");
        builder.append("\t,\"description\" : \"" + this.getDescription() + "\"");
        builder.append("\t,\"expectedFields\" : \"" + this.getExpectedFields() + "\"");
        builder.append("\t,\"verificationDataConfigs\" : \"" + this.getVerificationDataConfigs() + "\"");
        builder.append("\t,\"expires\" : \"" + this.getExpires() + "\"");
        builder.append("\t,\"expiryField\" : \"" + this.getExpiryField() + "\"");
        builder.append("\t,\"expiryPeriod\" : \"" + this.getExpiryPeriod() + "\"");
        builder.append("\t,\"expiresIn\" : \"" + this.getExpiresIn() + "\"");
        builder.append("\t,\"expectedFieldCount\" : \"" + this.getExpectedFieldCount() + "\"");
        builder.append("\t,\"verificationConfigCount\" : \"" + this.getVerificationConfigCount() + "\"");
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
        final DocumentTypeDTO that = (DocumentTypeDTO)thatObject;
        return super.equalProperties(that)
            && equal(this.getCode(), that.getCode())
            && equal(this.getName(), that.getName())
            && equal(this.getDescription(), that.getDescription())
            && equal(this.getExpectedFields(), that.getExpectedFields())
            && equal(this.getVerificationDataConfigs(), that.getVerificationDataConfigs())
            && equal(this.getExpires(), that.getExpires())
            && equal(this.getExpiryField(), that.getExpiryField())
            && equal(this.getExpiryPeriod(), that.getExpiryPeriod())
            && equal(this.getExpiresIn(), that.getExpiresIn())
            && equal(this.getExpectedFieldCount(), that.getExpectedFieldCount())
            && equal(this.getVerificationConfigCount(), that.getVerificationConfigCount())
        ;
    }

}