// false
// true
package bw.co.knowvera.screening;

import bw.co.knowvera.Auditable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;

/**
 * TODO: Model Documentation for SanctionsListEntry
 */
@Entity
@Table(
    name = "SANCTIONS_LIST_ENTRY", uniqueConstraints = {
        @UniqueConstraint(
            name="SANCTIONS_ENTRY_UNIQUE",
            columnNames={
                "SOURCE",
                "REFERENCE"
            }
        )
    }
)
// Uncomment to enable caching for SanctionsListEntry
// @org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.read-write)
@NamedQuery(name = "SanctionsListEntry.findAll", query = "SELECT s FROM SanctionsListEntry AS s")
public class SanctionsListEntry
    extends Auditable
{
    /**
     * The serial version UID of this class. Needed for serialization.
     */
    private static final long serialVersionUID = -1522626723974143720L;

    // Generate 11 attributes
    @Id
    @UuidGenerator(style = UuidGenerator.Style.VERSION_7)

    @Column(name = "ID", unique = false, nullable = false, insertable = true, updatable = true)
    private UUID id;

    /**
     * TODO: Model Documentation for SanctionsListEntry.id
     * @return this.id UUID
     */
    public UUID getId()
    {
        return this.id;
    }

    /**
     * TODO: Model Documentation for SanctionsListEntry.id
     * @param idIn UUID
     */
    public void setId(UUID idIn)
    {
        this.id = idIn;
    }


    @Column(name = "SOURCE", unique = false, nullable = false, insertable = true, updatable = true)
    @Enumerated(EnumType.STRING)
    private ScreeningListSource source;

    /**
     * TODO: Model Documentation for SanctionsListEntry.source
     * @return this.source ScreeningListSource
     */
    public ScreeningListSource getSource()
    {
        return this.source;
    }

    /**
     * TODO: Model Documentation for SanctionsListEntry.source
     * @param sourceIn ScreeningListSource
     */
    public void setSource(ScreeningListSource sourceIn)
    {
        this.source = sourceIn;
    }


    @Column(name = "REFERENCE", unique = false, nullable = false, insertable = true, updatable = true)
    private String reference;

    /**
     * TODO: Model Documentation for SanctionsListEntry.reference
     * @return this.reference String
     */
    public String getReference()
    {
        return this.reference;
    }

    /**
     * TODO: Model Documentation for SanctionsListEntry.reference
     * @param referenceIn String
     */
    public void setReference(String referenceIn)
    {
        this.reference = referenceIn;
    }


    @Column(name = "SUBJECT_TYPE", unique = false, nullable = false, insertable = true, updatable = true)
    @Enumerated(EnumType.STRING)
    private ScreeningSubjectType subjectType;

    /**
     * TODO: Model Documentation for SanctionsListEntry.subjectType
     * @return this.subjectType ScreeningSubjectType
     */
    public ScreeningSubjectType getSubjectType()
    {
        return this.subjectType;
    }

    /**
     * TODO: Model Documentation for SanctionsListEntry.subjectType
     * @param subjectTypeIn ScreeningSubjectType
     */
    public void setSubjectType(ScreeningSubjectType subjectTypeIn)
    {
        this.subjectType = subjectTypeIn;
    }


    @Column(name = "NAME", unique = false, nullable = false, insertable = true, updatable = true)
    private String name;

    /**
     * TODO: Model Documentation for SanctionsListEntry.name
     * @return this.name String
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * TODO: Model Documentation for SanctionsListEntry.name
     * @param nameIn String
     */
    public void setName(String nameIn)
    {
        this.name = nameIn;
    }


    @Column(name = "ALIASES", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private List<String> aliases;

    /**
     * TODO: Model Documentation for SanctionsListEntry.aliases
     * @return this.aliases List<String>
     */
    public List<String> getAliases()
    {
        return this.aliases;
    }

    /**
     * TODO: Model Documentation for SanctionsListEntry.aliases
     * @param aliasesIn List<String>
     */
    public void setAliases(List<String> aliasesIn)
    {
        this.aliases = aliasesIn;
    }


    @Column(name = "DATE_OF_BIRTHS", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private List<String> dateOfBirths;

    /**
     * TODO: Model Documentation for SanctionsListEntry.dateOfBirths
     * @return this.dateOfBirths List<String>
     */
    public List<String> getDateOfBirths()
    {
        return this.dateOfBirths;
    }

    /**
     * TODO: Model Documentation for SanctionsListEntry.dateOfBirths
     * @param dateOfBirthsIn List<String>
     */
    public void setDateOfBirths(List<String> dateOfBirthsIn)
    {
        this.dateOfBirths = dateOfBirthsIn;
    }


    @Column(name = "NATIONALITIES", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private List<String> nationalities;

    /**
     * TODO: Model Documentation for SanctionsListEntry.nationalities
     * @return this.nationalities List<String>
     */
    public List<String> getNationalities()
    {
        return this.nationalities;
    }

    /**
     * TODO: Model Documentation for SanctionsListEntry.nationalities
     * @param nationalitiesIn List<String>
     */
    public void setNationalities(List<String> nationalitiesIn)
    {
        this.nationalities = nationalitiesIn;
    }


    @Column(name = "LISTED_ON", unique = false, nullable = true, insertable = true, updatable = true)
    private LocalDate listedOn;

    /**
     * TODO: Model Documentation for SanctionsListEntry.listedOn
     * @return this.listedOn LocalDate
     */
    public LocalDate getListedOn()
    {
        return this.listedOn;
    }

    /**
     * TODO: Model Documentation for SanctionsListEntry.listedOn
     * @param listedOnIn LocalDate
     */
    public void setListedOn(LocalDate listedOnIn)
    {
        this.listedOn = listedOnIn;
    }


    @Column(name = "LIST_VERSION", unique = false, nullable = true, insertable = true, updatable = true)
    private String listVersion;

    /**
     * TODO: Model Documentation for SanctionsListEntry.listVersion
     * @return this.listVersion String
     */
    public String getListVersion()
    {
        return this.listVersion;
    }

    /**
     * TODO: Model Documentation for SanctionsListEntry.listVersion
     * @param listVersionIn String
     */
    public void setListVersion(String listVersionIn)
    {
        this.listVersion = listVersionIn;
    }


    @Column(name = "COMMENTS", unique = false, nullable = true, insertable = true, updatable = true, columnDefinition = "TEXT")
    private String comments;

    /**
     * TODO: Model Documentation for SanctionsListEntry.comments
     * @return this.comments String
     */
    public String getComments()
    {
        return this.comments;
    }

    /**
     * TODO: Model Documentation for SanctionsListEntry.comments
     * @param commentsIn String
     */
    public void setComments(String commentsIn)
    {
        this.comments = commentsIn;
    }

    /**
     * Returns <code>true</code> if the argument is an SanctionsListEntry instance and all identifiers for this entity
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
        if (!(object instanceof SanctionsListEntry))
        {
            return false;
        }
        final SanctionsListEntry that = (SanctionsListEntry)object;
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
     * Constructs new instances of {@link SanctionsListEntry}.
     */
    public static final class Factory
    {
        /**
         * Constructs a new instance of {@link SanctionsListEntry}.
         * @return new SanctionsListEntry()
         */
        public static SanctionsListEntry newInstance()
        {
            return new SanctionsListEntry();
        }

        /**
         * Constructs a new instance of {@link SanctionsListEntry}, taking all required and/or
         * read-only properties as arguments, except for identifiers.
         * @param createdAt LocalDateTime
         * @param createdBy String
         * @param source ScreeningListSource
         * @param reference String
         * @param subjectType ScreeningSubjectType
         * @param name String
         * @return newInstance
         */
        public static SanctionsListEntry newInstance(LocalDateTime createdAt, String createdBy, ScreeningListSource source, String reference, ScreeningSubjectType subjectType, String name)
        {
            final SanctionsListEntry entity = new SanctionsListEntry();
            entity.setCreatedAt(createdAt);
            entity.setCreatedBy(createdBy);
            entity.setSource(source);
            entity.setReference(reference);
            entity.setSubjectType(subjectType);
            entity.setName(name);
            return entity;
        }

        /**
         * Constructs a new instance of {@link SanctionsListEntry}, taking all possible properties
         * (except the identifier(s))as arguments.
         * @param createdAt LocalDateTime
         * @param modifiedAt LocalDateTime
         * @param createdBy String
         * @param modifiedBy String
         * @param source ScreeningListSource
         * @param reference String
         * @param subjectType ScreeningSubjectType
         * @param name String
         * @param aliases List<String>
         * @param dateOfBirths List<String>
         * @param nationalities List<String>
         * @param listedOn LocalDate
         * @param listVersion String
         * @param comments String
         * @return newInstance SanctionsListEntry
         */
        public static SanctionsListEntry newInstance(LocalDateTime createdAt, LocalDateTime modifiedAt, String createdBy, String modifiedBy, ScreeningListSource source, String reference, ScreeningSubjectType subjectType, String name, List<String> aliases, List<String> dateOfBirths, List<String> nationalities, LocalDate listedOn, String listVersion, String comments)
        {
            final SanctionsListEntry entity = new SanctionsListEntry();
            entity.setCreatedAt(createdAt);
            entity.setModifiedAt(modifiedAt);
            entity.setCreatedBy(createdBy);
            entity.setModifiedBy(modifiedBy);
            entity.setSource(source);
            entity.setReference(reference);
            entity.setSubjectType(subjectType);
            entity.setName(name);
            entity.setAliases(aliases);
            entity.setDateOfBirths(dateOfBirths);
            entity.setNationalities(nationalities);
            entity.setListedOn(listedOn);
            entity.setListVersion(listVersion);
            entity.setComments(comments);
            return entity;
        }
    }

    /**
     * @param other
     * @return a negative integer, zero, or a positive integer as this object is less than, equal to, or greater than the specified object.
     * @see Comparable#compareTo
     */
    public int compareTo(SanctionsListEntry other)
    {
        int cmp = 0;
        if (this.getId() != null)
        {
            cmp = this.getId().compareTo(other.getId());
        }
        else
        {
            if (this.getSource() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getSource().compareTo(other.getSource()));
            }
            if (this.getReference() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getReference().compareTo(other.getReference()));
            }
            if (this.getSubjectType() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getSubjectType().compareTo(other.getSubjectType()));
            }
            if (this.getName() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getName().compareTo(other.getName()));
            }
            if (this.getAliases() != null)
            {
                // Compare Collection sizes for ordering
                cmp = (cmp != 0 ? cmp : Math.abs(this.getAliases().size()-other.getAliases().size()));
            }
            if (this.getDateOfBirths() != null)
            {
                // Compare Collection sizes for ordering
                cmp = (cmp != 0 ? cmp : Math.abs(this.getDateOfBirths().size()-other.getDateOfBirths().size()));
            }
            if (this.getNationalities() != null)
            {
                // Compare Collection sizes for ordering
                cmp = (cmp != 0 ? cmp : Math.abs(this.getNationalities().size()-other.getNationalities().size()));
            }
            if (this.getListedOn() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getListedOn().compareTo(other.getListedOn()));
            }
            if (this.getListVersion() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getListVersion().compareTo(other.getListVersion()));
            }
            if (this.getComments() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getComments().compareTo(other.getComments()));
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
        sb.append("SanctionsListEntry(");
        sb.append(super.toString());
        sb.append(" id=").append(getId());
        sb.append(" source=").append(getSource());
        sb.append(" reference=").append(getReference());
        sb.append(" subjectType=").append(getSubjectType());
        sb.append(" name=").append(getName());
        sb.append(" aliases=").append(getAliases());
        sb.append(" dateOfBirths=").append(getDateOfBirths());
        sb.append(" nationalities=").append(getNationalities());
        sb.append(" listedOn=").append(getListedOn());
        sb.append(" listVersion=").append(getListVersion());
        sb.append(" comments=").append(getComments());
        sb.append(" createdAt=").append(getCreatedAt());
        sb.append(" modifiedAt=").append(getModifiedAt());
        sb.append(" createdBy=").append(getCreatedBy());
        sb.append(" modifiedBy=").append(getModifiedBy());
        sb.append(")");
        return sb.toString();
    }
}