// false
// true
package bw.co.roguesystems.ai.processor.field;

import bw.co.roguesystems.ai.Auditable;
import bw.co.roguesystems.ai.processor.DataProcessor;
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
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDateTime;
import java.util.UUID;
import org.hibernate.annotations.UuidGenerator;

/**
 * TODO: Model Documentation for ExpectedField
 */
@Entity
@Table(
    name = "EXPECTED_FIELD", uniqueConstraints = {
        @UniqueConstraint(
            name="EXPECTED_FIELD_UNIQUE",
            columnNames={
                "FIELD",
                "DATA_PROCESSOR_FK"
            }
        )
    }
)
// Uncomment to enable caching for ExpectedField
// @org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.read-write)
@NamedQueries
({
    @NamedQuery(name = "ExpectedField.findAll", query = "SELECT e FROM ExpectedField AS e"),
})
public class ExpectedField
    extends Auditable
{
    /**
     * The serial version UID of this class. Needed for serialization.
     */
    private static final long serialVersionUID = 5534284184199931990L;

    // Generate 10 attributes
    @Id
    @UuidGenerator(style = UuidGenerator.Style.VERSION_7)

    @Column(name = "ID", unique = true, nullable = false, insertable = true, updatable = true)
    private UUID id;

    /**
     * TODO: Model Documentation for ExpectedField.id
     * @return this.id UUID
     */
    public UUID getId()
    {
        return this.id;
    }

    /**
     * TODO: Model Documentation for ExpectedField.id
     * @param idIn UUID
     */
    public void setId(UUID idIn)
    {
        this.id = idIn;
    }


    @Column(name = "FIELD", unique = false, nullable = false, insertable = true, updatable = true)
    private String field;

    /**
     * TODO: Model Documentation for ExpectedField.field
     * @return this.field String
     */
    public String getField()
    {
        return this.field;
    }

    /**
     * TODO: Model Documentation for ExpectedField.field
     * @param fieldIn String
     */
    public void setField(String fieldIn)
    {
        this.field = fieldIn;
    }


    @Column(name = "MANDATORY", unique = false, nullable = true, insertable = true, updatable = true)
    private Boolean mandatory = Boolean.valueOf(false);

    /**
     * TODO: Model Documentation for ExpectedField.mandatory
     * @return this.mandatory Boolean
     */
    public Boolean getMandatory()
    {
        return this.mandatory;
    }

    /**
     * TODO: Model Documentation for ExpectedField.mandatory
     * @param mandatoryIn Boolean
     */
    public void setMandatory(Boolean mandatoryIn)
    {
        this.mandatory = mandatoryIn;
    }


    @Column(name = "FORMAT", unique = false, nullable = true, insertable = true, updatable = true)
    private String format;

    /**
     * TODO: Model Documentation for ExpectedField.format
     * @return this.format String
     */
    public String getFormat()
    {
        return this.format;
    }

    /**
     * TODO: Model Documentation for ExpectedField.format
     * @param formatIn String
     */
    public void setFormat(String formatIn)
    {
        this.format = formatIn;
    }


    @Column(name = "MANY", unique = false, nullable = true, insertable = true, updatable = true)
    private Boolean many = Boolean.valueOf(false);

    /**
     * TODO: Model Documentation for ExpectedField.many
     * @return this.many Boolean
     */
    public Boolean getMany()
    {
        return this.many;
    }

    /**
     * TODO: Model Documentation for ExpectedField.many
     * @param manyIn Boolean
     */
    public void setMany(Boolean manyIn)
    {
        this.many = manyIn;
    }


    @Column(name = "FIELD_TYPE", unique = false, nullable = false, insertable = true, updatable = true)
    @Enumerated(EnumType.STRING)
    private ExpectedFieldType fieldType;

    /**
     * TODO: Model Documentation for ExpectedField.fieldType
     * @return this.fieldType ExpectedFieldType
     */
    public ExpectedFieldType getFieldType()
    {
        return this.fieldType;
    }

    /**
     * TODO: Model Documentation for ExpectedField.fieldType
     * @param fieldTypeIn ExpectedFieldType
     */
    public void setFieldType(ExpectedFieldType fieldTypeIn)
    {
        this.fieldType = fieldTypeIn;
    }


    @Column(name = "FIELD_LABEL", unique = false, nullable = true, insertable = true, updatable = true)
    private String fieldLabel;

    /**
     * TODO: Model Documentation for ExpectedField.fieldLabel
     * @return this.fieldLabel String
     */
    public String getFieldLabel()
    {
        return this.fieldLabel;
    }

    /**
     * TODO: Model Documentation for ExpectedField.fieldLabel
     * @param fieldLabelIn String
     */
    public void setFieldLabel(String fieldLabelIn)
    {
        this.fieldLabel = fieldLabelIn;
    }


    // Generate 3 associations
    


    @ManyToOne(optional = false,  fetch = FetchType.LAZY)
    @JoinColumn(name = "DATA_PROCESSOR_FK")
    private DataProcessor dataProcessor;

    /**
     * TODO: Model Documentation for DataProcessor
     * @return this.dataProcessor DataProcessor
     */

    public DataProcessor getDataProcessor()
    {
        return this.dataProcessor;
    }

    /**
     * TODO: Model Documentation for DataProcessor
     * @param dataProcessorIn DataProcessor
     */
    public void setDataProcessor(DataProcessor dataProcessorIn)
    {
        this.dataProcessor = dataProcessorIn;
    }

    


    /**
     * Returns <code>true</code> if the argument is an ExpectedField instance and all identifiers for this entity
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
        if (!(object instanceof ExpectedField))
        {
            return false;
        }
        final ExpectedField that = (ExpectedField)object;
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
     * Constructs new instances of {@link ExpectedField}.
     */
    public static final class Factory
    {
        /**
         * Constructs a new instance of {@link ExpectedField}.
         * @return new ExpectedField()
         */
        public static ExpectedField newInstance()
        {
            return new ExpectedField();
        }

        /**
         * Constructs a new instance of {@link ExpectedField}, taking all required and/or
         * read-only properties as arguments, except for identifiers.
         * @param createdAt LocalDateTime
         * @param createdBy String
         * @param field String
         * @param fieldType ExpectedFieldType
         * @param dataProcessor DataProcessor
         * @return newInstance
         */
        public static ExpectedField newInstance(LocalDateTime createdAt, String createdBy, String field, ExpectedFieldType fieldType, DataProcessor dataProcessor)
        {
            final ExpectedField entity = new ExpectedField();
            entity.setCreatedAt(createdAt);
            entity.setCreatedBy(createdBy);
            entity.setField(field);
            entity.setFieldType(fieldType);
            entity.setDataProcessor(dataProcessor);
            return entity;
        }

        /**
         * Constructs a new instance of {@link ExpectedField}, taking all possible properties
         * (except the identifier(s))as arguments.
         * @param createdAt LocalDateTime
         * @param modifiedAt LocalDateTime
         * @param createdBy String
         * @param modifiedBy String
         * @param field String
         * @param mandatory Boolean
         * @param format String
         * @param many Boolean
         * @param fieldType ExpectedFieldType
         * @param fieldLabel String
         * @param dataProcessor DataProcessor
         * @return newInstance ExpectedField
         */
        public static ExpectedField newInstance(LocalDateTime createdAt, LocalDateTime modifiedAt, String createdBy, String modifiedBy, String field, Boolean mandatory, String format, Boolean many, ExpectedFieldType fieldType, String fieldLabel, DataProcessor dataProcessor)
        {
            final ExpectedField entity = new ExpectedField();
            entity.setCreatedAt(createdAt);
            entity.setModifiedAt(modifiedAt);
            entity.setCreatedBy(createdBy);
            entity.setModifiedBy(modifiedBy);
            entity.setField(field);
            entity.setMandatory(mandatory);
            entity.setFormat(format);
            entity.setMany(many);
            entity.setFieldType(fieldType);
            entity.setFieldLabel(fieldLabel);
            entity.setDataProcessor(dataProcessor);
            return entity;
        }
    }

    /**
     * @param other
     * @return a negative integer, zero, or a positive integer as this object is less than, equal to, or greater than the specified object.
     * @see Comparable#compareTo
     */
    public int compareTo(ExpectedField other)
    {
        int cmp = 0;
        if (this.getId() != null)
        {
            cmp = this.getId().compareTo(other.getId());
        }
        else
        {
            if (this.getField() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getField().compareTo(other.getField()));
            }
            if (this.getMandatory() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getMandatory().compareTo(other.getMandatory()));
            }
            if (this.getFormat() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getFormat().compareTo(other.getFormat()));
            }
            {
            }
            {
            }
            if (this.getMany() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getMany().compareTo(other.getMany()));
            }
            if (this.getFieldType() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getFieldType().compareTo(other.getFieldType()));
            }
            if (this.getFieldLabel() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getFieldLabel().compareTo(other.getFieldLabel()));
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
        sb.append("ExpectedField(");
        sb.append(super.toString());
        sb.append(" id=").append(getId());
        sb.append(" field=").append(getField());
        sb.append(" mandatory=").append(getMandatory());
        sb.append(" format=").append(getFormat());
        sb.append(" many=").append(getMany());
        sb.append(" fieldType=").append(getFieldType());
        sb.append(" fieldLabel=").append(getFieldLabel());
        sb.append(" createdAt=").append(getCreatedAt());
        sb.append(" modifiedAt=").append(getModifiedAt());
        sb.append(" createdBy=").append(getCreatedBy());
        sb.append(" modifiedBy=").append(getModifiedBy());
        sb.append(")");
        return sb.toString();
    }
}