// false
// true
package bw.co.knowvera.organisation.client;

import bw.co.knowvera.Auditable;
import bw.co.knowvera.TargetEntity;
import bw.co.knowvera.document.Document;
import bw.co.knowvera.organisation.Organisation;
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
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDateTime;
import java.util.UUID;
import org.hibernate.annotations.UuidGenerator;

/**
 * TODO: Model Documentation for ClientRequest
 */
@Entity
@Table(
    name = "CLIENT_REQUEST", uniqueConstraints = {
        @UniqueConstraint(
            name="TARGET_ATTACHMENT_UNIQUE",
            columnNames={
                "TARGET",
                "TARGET_ID",
                "ORGANISATION_FK"
            }
        )
    }
)
// Uncomment to enable caching for ClientRequest
// @org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.read-write)
@NamedQueries
({
    @NamedQuery(name = "ClientRequest.findAll", query = "SELECT c FROM ClientRequest AS c"),
})
public class ClientRequest
    extends Auditable
{
    /**
     * The serial version UID of this class. Needed for serialization.
     */
    private static final long serialVersionUID = 2045807048193756871L;

    // Generate 9 attributes
    @Id
    @UuidGenerator(style = UuidGenerator.Style.VERSION_7)

    @Column(name = "ID", unique = true, nullable = false, insertable = true, updatable = true)
    private UUID id;

    /**
     * TODO: Model Documentation for ClientRequest.id
     * @return this.id UUID
     */
    public UUID getId()
    {
        return this.id;
    }

    /**
     * TODO: Model Documentation for ClientRequest.id
     * @param idIn UUID
     */
    public void setId(UUID idIn)
    {
        this.id = idIn;
    }


    @Column(name = "STATUS", unique = false, nullable = false, insertable = true, updatable = true)
    @Enumerated(EnumType.STRING)
    private ClientRequestStatus status = ClientRequestStatus.PENDING;

    /**
     * TODO: Model Documentation for ClientRequest.status
     * @return this.status ClientRequestStatus
     */
    public ClientRequestStatus getStatus()
    {
        return this.status;
    }

    /**
     * TODO: Model Documentation for ClientRequest.status
     * @param statusIn ClientRequestStatus
     */
    public void setStatus(ClientRequestStatus statusIn)
    {
        this.status = statusIn;
    }


    @Column(name = "TARGET", unique = false, nullable = false, insertable = true, updatable = true)
    @Enumerated(EnumType.STRING)
    private TargetEntity target;

    /**
     * TODO: Model Documentation for ClientRequest.target
     * @return this.target TargetEntity
     */
    public TargetEntity getTarget()
    {
        return this.target;
    }

    /**
     * TODO: Model Documentation for ClientRequest.target
     * @param targetIn TargetEntity
     */
    public void setTarget(TargetEntity targetIn)
    {
        this.target = targetIn;
    }


    @Column(name = "TARGET_ID", unique = false, nullable = false, insertable = true, updatable = true)
    private String targetId;

    /**
     * TODO: Model Documentation for ClientRequest.targetId
     * @return this.targetId String
     */
    public String getTargetId()
    {
        return this.targetId;
    }

    /**
     * TODO: Model Documentation for ClientRequest.targetId
     * @param targetIdIn String
     */
    public void setTargetId(String targetIdIn)
    {
        this.targetId = targetIdIn;
    }


    @Column(name = "ACCOUNT_REQUEST_TOKEN", unique = false, nullable = true, insertable = true, updatable = true)
    private String accountRequestToken;

    /**
     * TODO: Model Documentation for
     * ClientRequest.accountRequestToken
     * @return this.accountRequestToken String
     */
    public String getAccountRequestToken()
    {
        return this.accountRequestToken;
    }

    /**
     * TODO: Model Documentation for
     * ClientRequest.accountRequestToken
     * @param accountRequestTokenIn String
     */
    public void setAccountRequestToken(String accountRequestTokenIn)
    {
        this.accountRequestToken = accountRequestTokenIn;
    }


    @Column(name = "REQUEST_TOKEN_ISSUE_DATE", unique = false, nullable = true, insertable = true, updatable = true)
    private LocalDateTime requestTokenIssueDate;

    /**
     * TODO: Model Documentation for
     * ClientRequest.requestTokenIssueDate
     * @return this.requestTokenIssueDate LocalDateTime
     */
    public LocalDateTime getRequestTokenIssueDate()
    {
        return this.requestTokenIssueDate;
    }

    /**
     * TODO: Model Documentation for
     * ClientRequest.requestTokenIssueDate
     * @param requestTokenIssueDateIn LocalDateTime
     */
    public void setRequestTokenIssueDate(LocalDateTime requestTokenIssueDateIn)
    {
        this.requestTokenIssueDate = requestTokenIssueDateIn;
    }


    @Column(name = "IDENTITY_CONFIRMATION_TOKEN", unique = false, nullable = true, insertable = true, updatable = true)
    private String identityConfirmationToken;

    /**
     * TODO: Model Documentation for
     * ClientRequest.identityConfirmationToken
     * @return this.identityConfirmationToken String
     */
    public String getIdentityConfirmationToken()
    {
        return this.identityConfirmationToken;
    }

    /**
     * TODO: Model Documentation for
     * ClientRequest.identityConfirmationToken
     * @param identityConfirmationTokenIn String
     */
    public void setIdentityConfirmationToken(String identityConfirmationTokenIn)
    {
        this.identityConfirmationToken = identityConfirmationTokenIn;
    }


    @Column(name = "REGISTRATION_TOKEN", unique = false, nullable = true, insertable = true, updatable = true)
    private String registrationToken;

    /**
     * TODO: Model Documentation for
     * ClientRequest.registrationToken
     * @return this.registrationToken String
     */
    public String getRegistrationToken()
    {
        return this.registrationToken;
    }

    /**
     * TODO: Model Documentation for
     * ClientRequest.registrationToken
     * @param registrationTokenIn String
     */
    public void setRegistrationToken(String registrationTokenIn)
    {
        this.registrationToken = registrationTokenIn;
    }


    @Column(name = "REF", unique = true, nullable = false, insertable = true, updatable = true)
    private String ref;

    /**
     * TODO: Model Documentation for ClientRequest.ref
     * @return this.ref String
     */
    public String getRef()
    {
        return this.ref;
    }

    /**
     * TODO: Model Documentation for ClientRequest.ref
     * @param refIn String
     */
    public void setRef(String refIn)
    {
        this.ref = refIn;
    }

    // Generate 2 associations
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

    @OneToOne( fetch = FetchType.LAZY)
    @JoinColumn(name = "DOCUMENT_FK", nullable = true, unique = false)
    private Document document;

    /**
     * TODO: Model Documentation for Document
     * @return this.document Document
     */

    public Document getDocument()
    {
        return this.document;
    }

    /**
     * TODO: Model Documentation for Document
     * @param documentIn Document
     */
    public void setDocument(Document documentIn)
    {
        this.document = documentIn;
    }

    /**
     * Returns <code>true</code> if the argument is an ClientRequest instance and all identifiers for this entity
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
        if (!(object instanceof ClientRequest))
        {
            return false;
        }
        final ClientRequest that = (ClientRequest)object;
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
     * Constructs new instances of {@link ClientRequest}.
     */
    public static final class Factory
    {
        /**
         * Constructs a new instance of {@link ClientRequest}.
         * @return new ClientRequest()
         */
        public static ClientRequest newInstance()
        {
            return new ClientRequest();
        }

        /**
         * Constructs a new instance of {@link ClientRequest}, taking all required and/or
         * read-only properties as arguments, except for identifiers.
         * @param createdAt LocalDateTime
         * @param createdBy String
         * @param status ClientRequestStatus
         * @param target TargetEntity
         * @param targetId String
         * @param ref String
         * @param organisation Organisation
         * @return newInstance
         */
        public static ClientRequest newInstance(LocalDateTime createdAt, String createdBy, ClientRequestStatus status, TargetEntity target, String targetId, String ref, Organisation organisation)
        {
            final ClientRequest entity = new ClientRequest();
            entity.setCreatedAt(createdAt);
            entity.setCreatedBy(createdBy);
            entity.setStatus(status);
            entity.setTarget(target);
            entity.setTargetId(targetId);
            entity.setRef(ref);
            entity.setOrganisation(organisation);
            return entity;
        }

        /**
         * Constructs a new instance of {@link ClientRequest}, taking all possible properties
         * (except the identifier(s))as arguments.
         * @param createdAt LocalDateTime
         * @param modifiedAt LocalDateTime
         * @param createdBy String
         * @param modifiedBy String
         * @param status ClientRequestStatus
         * @param target TargetEntity
         * @param targetId String
         * @param accountRequestToken String
         * @param requestTokenIssueDate LocalDateTime
         * @param identityConfirmationToken String
         * @param registrationToken String
         * @param ref String
         * @param organisation Organisation
         * @param document Document
         * @return newInstance ClientRequest
         */
        public static ClientRequest newInstance(LocalDateTime createdAt, LocalDateTime modifiedAt, String createdBy, String modifiedBy, ClientRequestStatus status, TargetEntity target, String targetId, String accountRequestToken, LocalDateTime requestTokenIssueDate, String identityConfirmationToken, String registrationToken, String ref, Organisation organisation, Document document)
        {
            final ClientRequest entity = new ClientRequest();
            entity.setCreatedAt(createdAt);
            entity.setModifiedAt(modifiedAt);
            entity.setCreatedBy(createdBy);
            entity.setModifiedBy(modifiedBy);
            entity.setStatus(status);
            entity.setTarget(target);
            entity.setTargetId(targetId);
            entity.setAccountRequestToken(accountRequestToken);
            entity.setRequestTokenIssueDate(requestTokenIssueDate);
            entity.setIdentityConfirmationToken(identityConfirmationToken);
            entity.setRegistrationToken(registrationToken);
            entity.setRef(ref);
            entity.setOrganisation(organisation);
            entity.setDocument(document);
            return entity;
        }
    }

    /**
     * @param other
     * @return a negative integer, zero, or a positive integer as this object is less than, equal to, or greater than the specified object.
     * @see Comparable#compareTo
     */
    public int compareTo(ClientRequest other)
    {
        int cmp = 0;
        if (this.getId() != null)
        {
            cmp = this.getId().compareTo(other.getId());
        }
        else
        {
            if (this.getStatus() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getStatus().compareTo(other.getStatus()));
            }
            if (this.getTarget() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getTarget().compareTo(other.getTarget()));
            }
            if (this.getTargetId() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getTargetId().compareTo(other.getTargetId()));
            }
            if (this.getAccountRequestToken() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getAccountRequestToken().compareTo(other.getAccountRequestToken()));
            }
            if (this.getRequestTokenIssueDate() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getRequestTokenIssueDate().compareTo(other.getRequestTokenIssueDate()));
            }
            if (this.getIdentityConfirmationToken() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getIdentityConfirmationToken().compareTo(other.getIdentityConfirmationToken()));
            }
            if (this.getRegistrationToken() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getRegistrationToken().compareTo(other.getRegistrationToken()));
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
        sb.append("ClientRequest(");
        sb.append(super.toString());
        sb.append(" id=").append(getId());
        sb.append(" status=").append(getStatus());
        sb.append(" target=").append(getTarget());
        sb.append(" targetId=").append(getTargetId());
        sb.append(" accountRequestToken=").append(getAccountRequestToken());
        sb.append(" requestTokenIssueDate=").append(getRequestTokenIssueDate());
        sb.append(" identityConfirmationToken=").append(getIdentityConfirmationToken());
        sb.append(" registrationToken=").append(getRegistrationToken());
        sb.append(" ref=").append(getRef());
        sb.append(" createdAt=").append(getCreatedAt());
        sb.append(" modifiedAt=").append(getModifiedAt());
        sb.append(" createdBy=").append(getCreatedBy());
        sb.append(" modifiedBy=").append(getModifiedBy());
        sb.append(")");
        return sb.toString();
    }
}