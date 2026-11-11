// false
// true
package bw.co.knowvera.organisation.branch;

import bw.co.knowvera.Auditable;
import bw.co.knowvera.document.Document;
import bw.co.knowvera.individual.Individual;
import bw.co.knowvera.organisation.Organisation;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.hibernate.annotations.UuidGenerator;

/**
 * TODO: Model Documentation for Branch
 */
@Entity
@Table(
    name = "BRANCH")
// Uncomment to enable caching for Branch
// @org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.read-write)
@NamedQueries
({
    @NamedQuery(name = "Branch.findAll", query = "SELECT b FROM Branch AS b"),
})
public class Branch
    extends Auditable
{
    /**
     * The serial version UID of this class. Needed for serialization.
     */
    private static final long serialVersionUID = 464932920585610661L;

    // Generate 5 attributes

    @Column(name = "CODE", unique = true, nullable = false, insertable = true, updatable = true)
    private String code;

    /**
     * TODO: Model Documentation for Branch.code
     * @return this.code String
     */
    public String getCode()
    {
        return this.code;
    }

    /**
     * TODO: Model Documentation for Branch.code
     * @param codeIn String
     */
    public void setCode(String codeIn)
    {
        this.code = codeIn;
    }


    @Column(name = "NAME", unique = true, nullable = false, insertable = true, updatable = true)
    private String name;

    /**
     * TODO: Model Documentation for Branch.name
     * @return this.name String
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * TODO: Model Documentation for Branch.name
     * @param nameIn String
     */
    public void setName(String nameIn)
    {
        this.name = nameIn;
    }


    @Column(name = "DESCRIPTION", unique = false, nullable = true, insertable = true, updatable = true)
    private String description;

    /**
     * TODO: Model Documentation for Branch.description
     * @return this.description String
     */
    public String getDescription()
    {
        return this.description;
    }

    /**
     * TODO: Model Documentation for Branch.description
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
     * TODO: Model Documentation for Branch.id
     * @return this.id UUID
     */
    public UUID getId()
    {
        return this.id;
    }

    /**
     * TODO: Model Documentation for Branch.id
     * @param idIn UUID
     */
    public void setId(UUID idIn)
    {
        this.id = idIn;
    }


    @Column(name = "PHYSICAL_ADDRESS", unique = false, nullable = true, insertable = true, updatable = true)
    private String physicalAddress;

    /**
     * TODO: Model Documentation for Branch.physicalAddress
     * @return this.physicalAddress String
     */
    public String getPhysicalAddress()
    {
        return this.physicalAddress;
    }

    /**
     * TODO: Model Documentation for Branch.physicalAddress
     * @param physicalAddressIn String
     */
    public void setPhysicalAddress(String physicalAddressIn)
    {
        this.physicalAddress = physicalAddressIn;
    }

    // Generate 3 associations
    @ManyToOne(optional = false,  fetch = FetchType.LAZY)
    @JoinColumn(name = "ORGANISATION_FK")
    private Organisation organisation;

    /**
     * TODO: Model Documentation for Organisation
     * @return this.organisation Organisation
     */

    public Organisation getOrganisation()
    {
        return this.organisation;
    }

    /**
     * TODO: Model Documentation for Organisation
     * @param organisationIn Organisation
     */
    public void setOrganisation(Organisation organisationIn)
    {
        this.organisation = organisationIn;
    }

    @OneToMany( mappedBy = "branch")
    private List<Individual> individuals = new ArrayList<Individual>();
    
    /**
     * TODO: Model Documentation for Individual
     * @return this.individuals List<Individual>
     */

    public List<Individual> getIndividuals()
    {
        return this.individuals;
    }

    /**
     * TODO: Model Documentation for Individual
     * @param individualsIn List<Individual>
     */
    public void setIndividuals(List<Individual> individualsIn)
    {
        this.individuals = individualsIn;
    }

    /**
     * TODO: Model Documentation for Individual
     * @param elementToAdd Individual
     * @return <tt>true</tt> if this collection changed as a result of the
     *         call
     */
    public boolean addIndividuals(Individual elementToAdd)
    {
        return this.individuals.add(elementToAdd);
    }

    /**
     * TODO: Model Documentation for Individual
     * @param elementToRemove Individual
     * @return <tt>true</tt> if this collection changed as a result of the
     *         call
     */
    public boolean removeIndividuals(Individual elementToRemove)
    {
        return this.individuals.remove(elementToRemove);
    }

    @OneToMany()
    @JoinTable
    (
        name = "BRANCH2DOCUMENTS",
        joinColumns = {@JoinColumn(name = "BRANCH_FK")},
        inverseJoinColumns = {@JoinColumn(name = "DOCUMENTS_FK")}
    )
    private List<Document> documents = new ArrayList<Document>();
    
    /**
     * TODO: Model Documentation for Document
     * @return this.documents List<Document>
     */

    public List<Document> getDocuments()
    {
        return this.documents;
    }

    /**
     * TODO: Model Documentation for Document
     * @param documentsIn List<Document>
     */
    public void setDocuments(List<Document> documentsIn)
    {
        this.documents = documentsIn;
    }

    /**
     * TODO: Model Documentation for Document
     * @param elementToAdd Document
     * @return <tt>true</tt> if this collection changed as a result of the
     *         call
     */
    public boolean addDocuments(Document elementToAdd)
    {
        return this.documents.add(elementToAdd);
    }

    /**
     * TODO: Model Documentation for Document
     * @param elementToRemove Document
     * @return <tt>true</tt> if this collection changed as a result of the
     *         call
     */
    public boolean removeDocuments(Document elementToRemove)
    {
        return this.documents.remove(elementToRemove);
    }

    /**
     * Returns <code>true</code> if the argument is an Branch instance and all identifiers for this entity
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
        if (!(object instanceof Branch))
        {
            return false;
        }
        final Branch that = (Branch)object;
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
     * Constructs new instances of {@link Branch}.
     */
    public static final class Factory
    {
        /**
         * Constructs a new instance of {@link Branch}.
         * @return new Branch()
         */
        public static Branch newInstance()
        {
            return new Branch();
        }

        /**
         * Constructs a new instance of {@link Branch}, taking all required and/or
         * read-only properties as arguments, except for identifiers.
         * @param createdAt LocalDateTime
         * @param createdBy String
         * @param code String
         * @param name String
         * @param organisation Organisation
         * @return newInstance
         */
        public static Branch newInstance(LocalDateTime createdAt, String createdBy, String code, String name, Organisation organisation)
        {
            final Branch entity = new Branch();
            entity.setCreatedAt(createdAt);
            entity.setCreatedBy(createdBy);
            entity.setCode(code);
            entity.setName(name);
            entity.setOrganisation(organisation);
            return entity;
        }

        /**
         * Constructs a new instance of {@link Branch}, taking all possible properties
         * (except the identifier(s))as arguments.
         * @param createdAt LocalDateTime
         * @param modifiedAt LocalDateTime
         * @param createdBy String
         * @param modifiedBy String
         * @param code String
         * @param name String
         * @param description String
         * @param physicalAddress String
         * @param organisation Organisation
         * @param individuals List<Individual>
         * @param documents List<Document>
         * @return newInstance Branch
         */
        public static Branch newInstance(LocalDateTime createdAt, LocalDateTime modifiedAt, String createdBy, String modifiedBy, String code, String name, String description, String physicalAddress, Organisation organisation, List<Individual> individuals, List<Document> documents)
        {
            final Branch entity = new Branch();
            entity.setCreatedAt(createdAt);
            entity.setModifiedAt(modifiedAt);
            entity.setCreatedBy(createdBy);
            entity.setModifiedBy(modifiedBy);
            entity.setCode(code);
            entity.setName(name);
            entity.setDescription(description);
            entity.setPhysicalAddress(physicalAddress);
            entity.setOrganisation(organisation);
            entity.setIndividuals(individuals);
            entity.setDocuments(documents);
            return entity;
        }
    }

    /**
     * @param other
     * @return a negative integer, zero, or a positive integer as this object is less than, equal to, or greater than the specified object.
     * @see Comparable#compareTo
     */
    public int compareTo(Branch other)
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
            if (this.getPhysicalAddress() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getPhysicalAddress().compareTo(other.getPhysicalAddress()));
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
        sb.append("Branch(");
        sb.append(super.toString());
        sb.append(" code=").append(getCode());
        sb.append(" name=").append(getName());
        sb.append(" description=").append(getDescription());
        sb.append(" id=").append(getId());
        sb.append(" physicalAddress=").append(getPhysicalAddress());
        sb.append(" createdAt=").append(getCreatedAt());
        sb.append(" modifiedAt=").append(getModifiedAt());
        sb.append(" createdBy=").append(getCreatedBy());
        sb.append(" modifiedBy=").append(getModifiedBy());
        sb.append(")");
        return sb.toString();
    }
}