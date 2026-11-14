// false
// true
package bw.co.roguesystems.ai.processor;

import bw.co.roguesystems.ai.Auditable;
import bw.co.roguesystems.ai.llm.PromptMessage;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import bw.co.roguesystems.ai.processor.field.ExpectedField;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;

/**
 * TODO: Model Documentation for DataProcessor
 */
@Entity
@Table(
    name = "DATA_PROCESSOR")
// Uncomment to enable caching for DataProcessor
// @org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.read-write)
@NamedQueries
({
    @NamedQuery(name = "DataProcessor.findAll", query = "SELECT d FROM DataProcessor AS d"),
})
public class DataProcessor
    extends Auditable
{
    /**
     * The serial version UID of this class. Needed for serialization.
     */
    private static final long serialVersionUID = -6756439568402042544L;

    // Generate 9 attributes

    @Column(name = "CODE", unique = true, nullable = false, insertable = true, updatable = true)
    private String code;

    /**
     * TODO: Model Documentation for DataProcessor.code
     * @return this.code String
     */
    public String getCode()
    {
        return this.code;
    }

    /**
     * TODO: Model Documentation for DataProcessor.code
     * @param codeIn String
     */
    public void setCode(String codeIn)
    {
        this.code = codeIn;
    }


    @Column(name = "NAME", unique = true, nullable = false, insertable = true, updatable = true)
    private String name;

    /**
     * TODO: Model Documentation for DataProcessor.name
     * @return this.name String
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * TODO: Model Documentation for DataProcessor.name
     * @param nameIn String
     */
    public void setName(String nameIn)
    {
        this.name = nameIn;
    }


    @Column(name = "DESCRIPTION", unique = false, nullable = true, insertable = true, updatable = true)
    private String description;

    /**
     * TODO: Model Documentation for DataProcessor.description
     * @return this.description String
     */
    public String getDescription()
    {
        return this.description;
    }

    /**
     * TODO: Model Documentation for DataProcessor.description
     * @param descriptionIn String
     */
    public void setDescription(String descriptionIn)
    {
        this.description = descriptionIn;
    }

    @Id
    @UuidGenerator(style = UuidGenerator.Style.VERSION_7)

    @Column(name = "ID", unique = true, nullable = false, insertable = true, updatable = true)
    private UUID id;

    /**
     * TODO: Model Documentation for DataProcessor.id
     * @return this.id UUID
     */
    public UUID getId()
    {
        return this.id;
    }

    /**
     * TODO: Model Documentation for DataProcessor.id
     * @param idIn UUID
     */
    public void setId(UUID idIn)
    {
        this.id = idIn;
    }


    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "VALIDATION_PROMPTS", unique = false, nullable = true, insertable = true, updatable = true, columnDefinition = "jsonb")
    private List<PromptMessage> validationPrompts = new ArrayList<PromptMessage>();

    /**
     * TODO: Model Documentation for DataProcessor.validationPrompts
     * @return this.validationPrompts List<PromptMessage>
     */
    public List<PromptMessage> getValidationPrompts()
    {
        return this.validationPrompts;
    }

    /**
     * TODO: Model Documentation for DataProcessor.validationPrompts
     * @param validationPromptsIn List<PromptMessage>
     */
    public void setValidationPrompts(List<PromptMessage> validationPromptsIn)
    {
        this.validationPrompts = validationPromptsIn;
    }

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "TEXT_EXTRACTION_PROMPTS", unique = false, nullable = true, insertable = true, updatable = true, columnDefinition = "jsonb")
    private List<PromptMessage> textExtractionPrompts = new ArrayList<PromptMessage>();

    /**
     * TODO: Model Documentation for DataProcessor.textExtractionPrompts
     * @return this.textExtractionPrompts List<PromptMessage>
     */
    public List<PromptMessage> getTextExtractionPrompts()
    {
        return this.textExtractionPrompts;
    }

    /**
     * TODO: Model Documentation for DataProcessor.textExtractionPrompts
     * @param textExtractionPromptsIn List<PromptMessage>
     */
    public void setTextExtractionPrompts(List<PromptMessage> textExtractionPromptsIn)
    {
        this.textExtractionPrompts = textExtractionPromptsIn;
    }


    // Generate 4 associations
    @OneToMany(cascade = {CascadeType.ALL} , mappedBy = "dataProcessor",  fetch = FetchType.EAGER)
    private List<ExpectedField> expectedFields = new ArrayList<ExpectedField>();
    
    /**
     * TODO: Model Documentation for ExpectedField
     * @return this.expectedFields List<ExpectedField>
     */

    public List<ExpectedField> getExpectedFields()
    {
        return this.expectedFields;
    }

    /**
     * TODO: Model Documentation for ExpectedField
     * @param expectedFieldsIn List<ExpectedField>
     */
    public void setExpectedFields(List<ExpectedField> expectedFieldsIn)
    {
        this.expectedFields = expectedFieldsIn;
    }

    /**
     * TODO: Model Documentation for ExpectedField
     * @param elementToAdd ExpectedField
     * @return <tt>true</tt> if this collection changed as a result of the
     *         call
     */
    public boolean addExpectedFields(ExpectedField elementToAdd)
    {
        return this.expectedFields.add(elementToAdd);
    }

    /**
     * TODO: Model Documentation for ExpectedField
     * @param elementToRemove ExpectedField
     * @return <tt>true</tt> if this collection changed as a result of the
     *         call
     */
    public boolean removeExpectedFields(ExpectedField elementToRemove)
    {
        return this.expectedFields.remove(elementToRemove);
    }


    


    /**
     * Returns <code>true</code> if the argument is an DataProcessor instance and all identifiers for this entity
     * equal the identifiers of the argument entity. The <code>equals</code> method of the parent entity
     * will also need to return <code>true</code>. Returns <code>false</code> otherwise.
     *
     * @see Auditable#equals(Object)
     */
    @Override
    public boolean equals(Object object)
    {
        if (this == object)
        {
            return true;
        }
        if (!(object instanceof DataProcessor))
        {
            return false;
        }
        final DataProcessor that = (DataProcessor)object;
        if (this.id == null || that.getId() == null || !this.id.equals(that.getId()))
        {
            return false;
        }
        return true;
    }

    /**
     * Returns a hash code based on this entity's identifiers and the hash code of the parent entity.
     *
     * @see Auditable#hashCode()
     */
    @Override
    public int hashCode()
    {
        int hashCode = super.hashCode();
        hashCode = 29 * hashCode + (this.id == null ? 0 : this.id.hashCode());

        return hashCode;
    }

    /**
     * Constructs new instances of {@link DataProcessor}.
     */
    public static final class Factory
    {
        /**
         * Constructs a new instance of {@link DataProcessor}.
         * @return new DataProcessor()
         */
        public static DataProcessor newInstance()
        {
            return new DataProcessor();
        }

        /**
         * Constructs a new instance of {@link DataProcessor}, taking all required and/or
         * read-only properties as arguments, except for identifiers.
         * @param createdAt LocalDateTime
         * @param createdBy String
         * @param code String
         * @param name String
         * @return newInstance
         */
        public static DataProcessor newInstance(LocalDateTime createdAt, String createdBy, String code, String name)
        {
            final DataProcessor entity = new DataProcessor();
            entity.setCreatedAt(createdAt);
            entity.setCreatedBy(createdBy);
            entity.setCode(code);
            entity.setName(name);
            return entity;
        }

        /**
         * Constructs a new instance of {@link DataProcessor}, taking all possible properties
         * (except the identifier(s))as arguments.
         * @param createdAt LocalDateTime
         * @param modifiedAt LocalDateTime
         * @param createdBy String
         * @param modifiedBy String
         * @param code String
         * @param name String
         * @param description String
         * @param expectedFields List<ExpectedField>
         * @return newInstance DataProcessor
         */
        public static DataProcessor newInstance(LocalDateTime createdAt, LocalDateTime modifiedAt, String createdBy, String modifiedBy, String code, String name, String description, List<ExpectedField> expectedFields)
        {
            final DataProcessor entity = new DataProcessor();
            entity.setCreatedAt(createdAt);
            entity.setModifiedAt(modifiedAt);
            entity.setCreatedBy(createdBy);
            entity.setModifiedBy(modifiedBy);
            entity.setCode(code);
            entity.setName(name);
            entity.setDescription(description);
            entity.setExpectedFields(expectedFields);
            return entity;
        }
    }

    /**
     * @param other
     * @return a negative integer, zero, or a positive integer as this object is less than, equal to, or greater than the specified object.
     * @see Comparable#compareTo
     */
    public int compareTo(DataProcessor other)
    {
        int cmp = 0;
        if (this.getId() != null)
        {
            cmp = this.getId().compareTo(other.getId());
        }
        else
        {
            if (this.getCode() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getCode().compareTo(other.getCode()));
            }
            if (this.getName() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getName().compareTo(other.getName()));
            }
            if (this.getDescription() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getDescription().compareTo(other.getDescription()));
            }
            {
            }
            {
            }
            {
            }
        }
        return cmp;
    }

    /**
     * Returns a String representation of the object
     * @return String Textual representation of the object displaying name/value pairs for all attributes
     * @see Object#toString
     */
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("DataProcessor(");
        sb.append(super.toString());
        sb.append(" code=").append(getCode());
        sb.append(" name=").append(getName());
        sb.append(" description=").append(getDescription());
        sb.append(" id=").append(getId());
        sb.append(" createdAt=").append(getCreatedAt());
        sb.append(" modifiedAt=").append(getModifiedAt());
        sb.append(" createdBy=").append(getCreatedBy());
        sb.append(" modifiedBy=").append(getModifiedBy());
        sb.append(")");
        return sb.toString();
    }
}