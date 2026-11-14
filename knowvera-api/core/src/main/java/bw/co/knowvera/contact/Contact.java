// false
// true
package bw.co.knowvera.contact;

import bw.co.knowvera.Auditable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;
import org.hibernate.annotations.UuidGenerator;

/**
 * TODO: Model Documentation for Contact
 */
@Entity
@Table(
    name = "CONTACT")
// Uncomment to enable caching for Contact
// @org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.read-write)
@NamedQuery(name = "Contact.findAll", query = "SELECT c FROM Contact AS c")
public class Contact
    extends Auditable
{
    /**
     * The serial version UID of this class. Needed for serialization.
     */
    private static final long serialVersionUID = -2687911782100569054L;

    // Generate 9 attributes
    @Id
    @UuidGenerator(style = UuidGenerator.Style.VERSION_7)

    @Column(name = "ID", unique = true, nullable = false, insertable = true, updatable = true)
    private UUID id;

    /**
     * TODO: Model Documentation for Contact.id
     * @return this.id UUID
     */
    public UUID getId()
    {
        return this.id;
    }

    /**
     * TODO: Model Documentation for Contact.id
     * @param idIn UUID
     */
    public void setId(UUID idIn)
    {
        this.id = idIn;
    }


    @Column(name = "MESSAGE", unique = false, nullable = false, insertable = true, updatable = true, columnDefinition = "TEXT")
    private String message;

    /**
     * TODO: Model Documentation for Contact.message
     * @return this.message String
     */
    public String getMessage()
    {
        return this.message;
    }

    /**
     * TODO: Model Documentation for Contact.message
     * @param messageIn String
     */
    public void setMessage(String messageIn)
    {
        this.message = messageIn;
    }


    @Column(name = "EMAIL", unique = false, nullable = false, insertable = true, updatable = true)
    private String email;

    /**
     * TODO: Model Documentation for Contact.email
     * @return this.email String
     */
    public String getEmail()
    {
        return this.email;
    }

    /**
     * TODO: Model Documentation for Contact.email
     * @param emailIn String
     */
    public void setEmail(String emailIn)
    {
        this.email = emailIn;
    }


    @Column(name = "TYPE", unique = false, nullable = true, insertable = true, updatable = true)
    @Enumerated(EnumType.STRING)
    private ContactType type;

    /**
     * TODO: Model Documentation for Contact.type
     * @return this.type ContactType
     */
    public ContactType getType()
    {
        return this.type;
    }

    /**
     * TODO: Model Documentation for Contact.type
     * @param typeIn ContactType
     */
    public void setType(ContactType typeIn)
    {
        this.type = typeIn;
    }


    @Column(name = "SENTIMENT", unique = false, nullable = true, insertable = true, updatable = true)
    @Enumerated(EnumType.STRING)
    private ContactSentiment sentiment;

    /**
     * TODO: Model Documentation for Contact.sentiment
     * @return this.sentiment ContactSentiment
     */
    public ContactSentiment getSentiment()
    {
        return this.sentiment;
    }

    /**
     * TODO: Model Documentation for Contact.sentiment
     * @param sentimentIn ContactSentiment
     */
    public void setSentiment(ContactSentiment sentimentIn)
    {
        this.sentiment = sentimentIn;
    }


    @Column(name = "URGENCY", unique = false, nullable = true, insertable = true, updatable = true)
    @Enumerated(EnumType.STRING)
    private ContactUrgency urgency;

    /**
     * TODO: Model Documentation for Contact.urgency
     * @return this.urgency ContactUrgency
     */
    public ContactUrgency getUrgency()
    {
        return this.urgency;
    }

    /**
     * TODO: Model Documentation for Contact.urgency
     * @param urgencyIn ContactUrgency
     */
    public void setUrgency(ContactUrgency urgencyIn)
    {
        this.urgency = urgencyIn;
    }


    @Column(name = "INTENT", unique = false, nullable = true, insertable = true, updatable = true)
    @Enumerated(EnumType.STRING)
    private ContactIntent intent;

    /**
     * TODO: Model Documentation for Contact.intent
     * @return this.intent ContactIntent
     */
    public ContactIntent getIntent()
    {
        return this.intent;
    }

    /**
     * TODO: Model Documentation for Contact.intent
     * @param intentIn ContactIntent
     */
    public void setIntent(ContactIntent intentIn)
    {
        this.intent = intentIn;
    }


    @Column(name = "CLASSIFICATION_CONFIDENCE", unique = false, nullable = true, insertable = true, updatable = true)
    private Double classificationConfidence = Double.valueOf(0.0);

    /**
     * TODO: Model Documentation for Contact.classificationConfidence
     * @return this.classificationConfidence Double
     */
    public Double getClassificationConfidence()
    {
        return this.classificationConfidence;
    }

    /**
     * TODO: Model Documentation for Contact.classificationConfidence
     * @param classificationConfidenceIn Double
     */
    public void setClassificationConfidence(Double classificationConfidenceIn)
    {
        this.classificationConfidence = classificationConfidenceIn;
    }


    @Column(name = "REF", unique = true, nullable = false, insertable = true, updatable = true)
    private String ref;

    /**
     * TODO: Model Documentation for Contact.ref
     * @return this.ref String
     */
    public String getRef()
    {
        return this.ref;
    }

    /**
     * TODO: Model Documentation for Contact.ref
     * @param refIn String
     */
    public void setRef(String refIn)
    {
        this.ref = refIn;
    }

    /**
     * Returns <code>true</code> if the argument is an Contact instance and all identifiers for this entity
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
        if (!(object instanceof Contact))
        {
            return false;
        }
        final Contact that = (Contact)object;
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
     * Constructs new instances of {@link Contact}.
     */
    public static final class Factory
    {
        /**
         * Constructs a new instance of {@link Contact}.
         * @return new Contact()
         */
        public static Contact newInstance()
        {
            return new Contact();
        }

        /**
         * Constructs a new instance of {@link Contact}, taking all required and/or
         * read-only properties as arguments, except for identifiers.
         * @param createdAt LocalDateTime
         * @param createdBy String
         * @param message String
         * @param email String
         * @param ref String
         * @return newInstance
         */
        public static Contact newInstance(LocalDateTime createdAt, String createdBy, String message, String email, String ref)
        {
            final Contact entity = new Contact();
            entity.setCreatedAt(createdAt);
            entity.setCreatedBy(createdBy);
            entity.setMessage(message);
            entity.setEmail(email);
            entity.setRef(ref);
            return entity;
        }

        /**
         * Constructs a new instance of {@link Contact}, taking all possible properties
         * (except the identifier(s))as arguments.
         * @param createdAt LocalDateTime
         * @param modifiedAt LocalDateTime
         * @param createdBy String
         * @param modifiedBy String
         * @param message String
         * @param email String
         * @param type ContactType
         * @param sentiment ContactSentiment
         * @param urgency ContactUrgency
         * @param intent ContactIntent
         * @param classificationConfidence Double
         * @param ref String
         * @return newInstance Contact
         */
        public static Contact newInstance(LocalDateTime createdAt, LocalDateTime modifiedAt, String createdBy, String modifiedBy, String message, String email, ContactType type, ContactSentiment sentiment, ContactUrgency urgency, ContactIntent intent, Double classificationConfidence, String ref)
        {
            final Contact entity = new Contact();
            entity.setCreatedAt(createdAt);
            entity.setModifiedAt(modifiedAt);
            entity.setCreatedBy(createdBy);
            entity.setModifiedBy(modifiedBy);
            entity.setMessage(message);
            entity.setEmail(email);
            entity.setType(type);
            entity.setSentiment(sentiment);
            entity.setUrgency(urgency);
            entity.setIntent(intent);
            entity.setClassificationConfidence(classificationConfidence);
            entity.setRef(ref);
            return entity;
        }
    }

    /**
     * @param other
     * @return a negative integer, zero, or a positive integer as this object is less than, equal to, or greater than the specified object.
     * @see Comparable#compareTo
     */
    public int compareTo(Contact other)
    {
        int cmp = 0;
        if (this.getId() != null)
        {
            cmp = this.getId().compareTo(other.getId());
        }
        else
        {
            if (this.getMessage() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getMessage().compareTo(other.getMessage()));
            }
            if (this.getEmail() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getEmail().compareTo(other.getEmail()));
            }
            if (this.getType() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getType().compareTo(other.getType()));
            }
            if (this.getSentiment() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getSentiment().compareTo(other.getSentiment()));
            }
            if (this.getUrgency() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getUrgency().compareTo(other.getUrgency()));
            }
            if (this.getIntent() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getIntent().compareTo(other.getIntent()));
            }
            if (this.getClassificationConfidence() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getClassificationConfidence().compareTo(other.getClassificationConfidence()));
            }
            if (this.getRef() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getRef().compareTo(other.getRef()));
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
        sb.append("Contact(");
        sb.append(super.toString());
        sb.append(" id=").append(getId());
        sb.append(" message=").append(getMessage());
        sb.append(" email=").append(getEmail());
        sb.append(" type=").append(getType());
        sb.append(" sentiment=").append(getSentiment());
        sb.append(" urgency=").append(getUrgency());
        sb.append(" intent=").append(getIntent());
        sb.append(" classificationConfidence=").append(getClassificationConfidence());
        sb.append(" ref=").append(getRef());
        sb.append(" createdAt=").append(getCreatedAt());
        sb.append(" modifiedAt=").append(getModifiedAt());
        sb.append(" createdBy=").append(getCreatedBy());
        sb.append(" modifiedBy=").append(getModifiedBy());
        sb.append(")");
        return sb.toString();
    }
}