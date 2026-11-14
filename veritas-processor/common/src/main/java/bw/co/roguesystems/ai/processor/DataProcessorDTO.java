package bw.co.roguesystems.ai.processor;

import bw.co.roguesystems.ai.AuditableDTO;
import bw.co.roguesystems.ai.processor.field.ExpectedFieldDTO;
import bw.co.roguesystems.ai.llm.PromptMessage;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.jspecify.annotations.Nullable;

/**
 * TODO: Model Documentation for DataProcessorDTO
 */
public class DataProcessorDTO
    extends AuditableDTO
{
    /** The serial version UID of this class. Needed for serialization. */
    private static final long serialVersionUID = 6857144212495260893L;

    // Class attributes
    /**
     * TODO: Model Documentation for DataProcessorDTO.code
     */
    protected String code;
    /**
     * TODO: Model Documentation for DataProcessorDTO.name
     */
    protected String name;
    /**
     * TODO: Model Documentation for DataProcessorDTO.description
     */
    @Nullable
    protected String description;
    /**
     * TODO: Model Documentation for DataProcessorDTO.expectedFields
     */
    @Nullable
    protected List<ExpectedFieldDTO> expectedFields;
    /**
     * TODO: Model Documentation for DataProcessorDTO.expectedFieldCount
     */
    @Nullable
    protected Integer expectedFieldCount;
    /**
     * The prompt that validates a document against this processor
     */
    @Nullable
    protected List<PromptMessage> validationPrompts;

    /**
     * The prompt that extracts the text of a document
     */
    @Nullable
    protected List<PromptMessage> textExtractionPrompts;

    /** Default Constructor with no properties */
    public DataProcessorDTO()
    {
        super();
    }

    /**
     * Constructor taking only required properties
     * @param codeIn String TODO: Model Documentation for DataProcessorDTO.code
     * @param nameIn String TODO: Model Documentation for DataProcessorDTO.name
     */
    public DataProcessorDTO(final String codeIn, final String nameIn)
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
     * @param expectedFieldCountIn Integer
     */
    public DataProcessorDTO(final String idIn, final String createdByIn, final LocalDateTime createdAtIn, final String modifiedByIn, final LocalDateTime modifiedAtIn, final String codeIn, final String nameIn, final String descriptionIn, final List<ExpectedFieldDTO> expectedFieldsIn, final Integer expectedFieldCountIn)
    {
        super(idIn, createdByIn, createdAtIn, modifiedByIn, modifiedAtIn);
        this.code = codeIn;
        this.name = nameIn;
        this.description = descriptionIn;
        this.expectedFields = expectedFieldsIn;
        this.expectedFieldCount = expectedFieldCountIn;
    }

    /**
     * Copies constructor from other DataProcessorDTO
     *
     * @param otherBean Cannot be <code>null</code>
     * @throws NullPointerException if the argument is <code>null</code>
     */
    public DataProcessorDTO(final DataProcessorDTO otherBean)
    {
        super(otherBean);
        this.code = otherBean.getCode();
        this.name = otherBean.getName();
        this.description = otherBean.getDescription();
        this.expectedFields = otherBean.getExpectedFields();
        this.expectedFieldCount = otherBean.getExpectedFieldCount();
        this.validationPrompts = otherBean.getValidationPrompts();
        this.textExtractionPrompts = otherBean.getTextExtractionPrompts();
    }

    /**
     * Copies all properties from the argument value object into this value object.
     * @param otherBean Cannot be <code>null</code>
     */
    public void copy(final DataProcessorDTO otherBean)
    {
        if (null != otherBean)
        {
            super.copy(otherBean);
            this.setCode(otherBean.getCode());
            this.setName(otherBean.getName());
            this.setDescription(otherBean.getDescription());
            this.setExpectedFields(otherBean.getExpectedFields());
            this.setExpectedFieldCount(otherBean.getExpectedFieldCount());
            this.setValidationPrompts(otherBean.getValidationPrompts());
            this.setTextExtractionPrompts(otherBean.getTextExtractionPrompts());
        }
    }

    /**
     * TODO: Model Documentation for DataProcessorDTO.code
     * Get the code Attribute
     * @return code String
     */
    public String getCode()
    {
        return this.code;
    }

    /**
     * TODO: Model Documentation for DataProcessorDTO.code
     * @param value String
     */
    public void setCode(final String value)
    {
        this.code = value;
    }

    /**
     * TODO: Model Documentation for DataProcessorDTO.name
     * Get the name Attribute
     * @return name String
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * TODO: Model Documentation for DataProcessorDTO.name
     * @param value String
     */
    public void setName(final String value)
    {
        this.name = value;
    }

    /**
     * TODO: Model Documentation for DataProcessorDTO.description
     * Get the description Attribute
     * @return description String
     */
    public String getDescription()
    {
        return this.description;
    }

    /**
     * TODO: Model Documentation for DataProcessorDTO.description
     * @param value String
     */
    public void setDescription(final String value)
    {
        this.description = value;
    }

    /**
     * TODO: Model Documentation for DataProcessorDTO.expectedFields
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
     * TODO: Model Documentation for DataProcessorDTO.expectedFields
     * @param value List<ExpectedFieldDTO>
     */
    public void setExpectedFields(final List<ExpectedFieldDTO> value)
    {
        this.expectedFields = value;
    }


    /**
     * DataProcessorDTO prompt messages
     * @return validationPrompts List<PromptMessage>
     */
    public List<PromptMessage> getValidationPrompts()
    {
        return this.validationPrompts;
    }

    /**
     * @param value List<PromptMessage>
     */
    public void setValidationPrompts(final List<PromptMessage> value)
    {
        this.validationPrompts = value;
    }


    /**
     * DataProcessorDTO prompt messages
     * @return textExtractionPrompts List<PromptMessage>
     */
    public List<PromptMessage> getTextExtractionPrompts()
    {
        return this.textExtractionPrompts;
    }

    /**
     * @param value List<PromptMessage>
     */
    public void setTextExtractionPrompts(final List<PromptMessage> value)
    {
        this.textExtractionPrompts = value;
    }

    /**
     * TODO: Model Documentation for DataProcessorDTO.expectedFieldCount
     * Get the expectedFieldCount Attribute
     * @return expectedFieldCount Integer
     */
    public Integer getExpectedFieldCount()
    {
        return this.expectedFieldCount;
    }

    /**
     * TODO: Model Documentation for DataProcessorDTO.expectedFieldCount
     * @param value Integer
     */
    public void setExpectedFieldCount(final Integer value)
    {
        this.expectedFieldCount = value;
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
        DataProcessorDTO rhs = (DataProcessorDTO) object;
        return new EqualsBuilder()
            .appendSuper(super.equals(object))
            .append(this.getCode(), rhs.getCode())
            .append(this.getName(), rhs.getName())
            .append(this.getDescription(), rhs.getDescription())
            .append(this.getExpectedFields(), rhs.getExpectedFields())
            .append(this.getExpectedFieldCount(), rhs.getExpectedFieldCount())
            .append(this.getValidationPrompts(), rhs.getValidationPrompts())
            .append(this.getTextExtractionPrompts(), rhs.getTextExtractionPrompts())
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
        if (!(object instanceof DataProcessorDTO))
        {
            return -1;
        }
        DataProcessorDTO myClass = (DataProcessorDTO)object;
        return new CompareToBuilder()
            .appendSuper(super.compareTo(object))
            .append(this.getCode(), myClass.getCode())
            .append(this.getName(), myClass.getName())
            .append(this.getDescription(), myClass.getDescription())
            .append(this.getExpectedFields(), myClass.getExpectedFields())
            .append(this.getExpectedFieldCount(), myClass.getExpectedFieldCount())
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
            .append(this.getExpectedFieldCount())
            .append(this.getValidationPrompts())
            .append(this.getTextExtractionPrompts())
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
        builder.append("\t,\"expectedFieldCount\" : \"" + this.getExpectedFieldCount() + "\"");
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
        final DataProcessorDTO that = (DataProcessorDTO)thatObject;
        return super.equalProperties(that)
            && equal(this.getCode(), that.getCode())
            && equal(this.getName(), that.getName())
            && equal(this.getDescription(), that.getDescription())
            && equal(this.getExpectedFields(), that.getExpectedFields())
            && equal(this.getExpectedFieldCount(), that.getExpectedFieldCount())
        ;
    }

}