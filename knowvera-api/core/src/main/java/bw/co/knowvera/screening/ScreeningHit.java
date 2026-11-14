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
import java.time.LocalDateTime;
import java.util.UUID;
import org.hibernate.annotations.UuidGenerator;

/**
 * TODO: Model Documentation for ScreeningHit
 */
@Entity
@Table(
    name = "SCREENING_HIT")
// Uncomment to enable caching for ScreeningHit
// @org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.read-write)
@NamedQuery(name = "ScreeningHit.findAll", query = "SELECT s FROM ScreeningHit AS s")
public class ScreeningHit
    extends Auditable
{
    /**
     * The serial version UID of this class. Needed for serialization.
     */
    private static final long serialVersionUID = 6012122361917601761L;

    // Generate 13 attributes
    @Id
    @UuidGenerator(style = UuidGenerator.Style.VERSION_7)

    @Column(name = "ID", unique = false, nullable = false, insertable = true, updatable = true)
    private UUID id;

    /**
     * TODO: Model Documentation for ScreeningHit.id
     * @return this.id UUID
     */
    public UUID getId()
    {
        return this.id;
    }

    /**
     * TODO: Model Documentation for ScreeningHit.id
     * @param idIn UUID
     */
    public void setId(UUID idIn)
    {
        this.id = idIn;
    }


    @Column(name = "KYC_RECORD_ID", unique = false, nullable = false, insertable = true, updatable = true)
    private String kycRecordId;

    /**
     * TODO: Model Documentation for ScreeningHit.kycRecordId
     * @return this.kycRecordId String
     */
    public String getKycRecordId()
    {
        return this.kycRecordId;
    }

    /**
     * TODO: Model Documentation for ScreeningHit.kycRecordId
     * @param kycRecordIdIn String
     */
    public void setKycRecordId(String kycRecordIdIn)
    {
        this.kycRecordId = kycRecordIdIn;
    }


    @Column(name = "SUBJECT_NAME", unique = false, nullable = false, insertable = true, updatable = true)
    private String subjectName;

    /**
     * TODO: Model Documentation for ScreeningHit.subjectName
     * @return this.subjectName String
     */
    public String getSubjectName()
    {
        return this.subjectName;
    }

    /**
     * TODO: Model Documentation for ScreeningHit.subjectName
     * @param subjectNameIn String
     */
    public void setSubjectName(String subjectNameIn)
    {
        this.subjectName = subjectNameIn;
    }


    @Column(name = "SUBJECT_ROLE", unique = false, nullable = true, insertable = true, updatable = true)
    private String subjectRole;

    /**
     * TODO: Model Documentation for ScreeningHit.subjectRole
     * @return this.subjectRole String
     */
    public String getSubjectRole()
    {
        return this.subjectRole;
    }

    /**
     * TODO: Model Documentation for ScreeningHit.subjectRole
     * @param subjectRoleIn String
     */
    public void setSubjectRole(String subjectRoleIn)
    {
        this.subjectRole = subjectRoleIn;
    }


    @Column(name = "SOURCE", unique = false, nullable = false, insertable = true, updatable = true)
    @Enumerated(EnumType.STRING)
    private ScreeningListSource source;

    /**
     * TODO: Model Documentation for ScreeningHit.source
     * @return this.source ScreeningListSource
     */
    public ScreeningListSource getSource()
    {
        return this.source;
    }

    /**
     * TODO: Model Documentation for ScreeningHit.source
     * @param sourceIn ScreeningListSource
     */
    public void setSource(ScreeningListSource sourceIn)
    {
        this.source = sourceIn;
    }


    @Column(name = "ENTRY_REFERENCE", unique = false, nullable = false, insertable = true, updatable = true)
    private String entryReference;

    /**
     * TODO: Model Documentation for ScreeningHit.entryReference
     * @return this.entryReference String
     */
    public String getEntryReference()
    {
        return this.entryReference;
    }

    /**
     * TODO: Model Documentation for ScreeningHit.entryReference
     * @param entryReferenceIn String
     */
    public void setEntryReference(String entryReferenceIn)
    {
        this.entryReference = entryReferenceIn;
    }


    @Column(name = "ENTRY_NAME", unique = false, nullable = false, insertable = true, updatable = true)
    private String entryName;

    /**
     * TODO: Model Documentation for ScreeningHit.entryName
     * @return this.entryName String
     */
    public String getEntryName()
    {
        return this.entryName;
    }

    /**
     * TODO: Model Documentation for ScreeningHit.entryName
     * @param entryNameIn String
     */
    public void setEntryName(String entryNameIn)
    {
        this.entryName = entryNameIn;
    }


    @Column(name = "SCORE", unique = false, nullable = true, insertable = true, updatable = true)
    private Double score;

    /**
     * TODO: Model Documentation for ScreeningHit.score
     * @return this.score Double
     */
    public Double getScore()
    {
        return this.score;
    }

    /**
     * TODO: Model Documentation for ScreeningHit.score
     * @param scoreIn Double
     */
    public void setScore(Double scoreIn)
    {
        this.score = scoreIn;
    }


    @Column(name = "STATUS", unique = false, nullable = false, insertable = true, updatable = true)
    @Enumerated(EnumType.STRING)
    private ScreeningHitStatus status;

    /**
     * TODO: Model Documentation for ScreeningHit.status
     * @return this.status ScreeningHitStatus
     */
    public ScreeningHitStatus getStatus()
    {
        return this.status;
    }

    /**
     * TODO: Model Documentation for ScreeningHit.status
     * @param statusIn ScreeningHitStatus
     */
    public void setStatus(ScreeningHitStatus statusIn)
    {
        this.status = statusIn;
    }


    @Column(name = "LIST_VERSION", unique = false, nullable = true, insertable = true, updatable = true)
    private String listVersion;

    /**
     * TODO: Model Documentation for ScreeningHit.listVersion
     * @return this.listVersion String
     */
    public String getListVersion()
    {
        return this.listVersion;
    }

    /**
     * TODO: Model Documentation for ScreeningHit.listVersion
     * @param listVersionIn String
     */
    public void setListVersion(String listVersionIn)
    {
        this.listVersion = listVersionIn;
    }


    @Column(name = "REVIEWED_BY", unique = false, nullable = true, insertable = true, updatable = true)
    private String reviewedBy;

    /**
     * TODO: Model Documentation for ScreeningHit.reviewedBy
     * @return this.reviewedBy String
     */
    public String getReviewedBy()
    {
        return this.reviewedBy;
    }

    /**
     * TODO: Model Documentation for ScreeningHit.reviewedBy
     * @param reviewedByIn String
     */
    public void setReviewedBy(String reviewedByIn)
    {
        this.reviewedBy = reviewedByIn;
    }


    @Column(name = "REVIEWED_AT", unique = false, nullable = true, insertable = true, updatable = true)
    private LocalDateTime reviewedAt;

    /**
     * TODO: Model Documentation for ScreeningHit.reviewedAt
     * @return this.reviewedAt LocalDateTime
     */
    public LocalDateTime getReviewedAt()
    {
        return this.reviewedAt;
    }

    /**
     * TODO: Model Documentation for ScreeningHit.reviewedAt
     * @param reviewedAtIn LocalDateTime
     */
    public void setReviewedAt(LocalDateTime reviewedAtIn)
    {
        this.reviewedAt = reviewedAtIn;
    }


    @Column(name = "REVIEW_NOTES", unique = false, nullable = true, insertable = true, updatable = true, columnDefinition = "TEXT")
    private String reviewNotes;

    /**
     * TODO: Model Documentation for ScreeningHit.reviewNotes
     * @return this.reviewNotes String
     */
    public String getReviewNotes()
    {
        return this.reviewNotes;
    }

    /**
     * TODO: Model Documentation for ScreeningHit.reviewNotes
     * @param reviewNotesIn String
     */
    public void setReviewNotes(String reviewNotesIn)
    {
        this.reviewNotes = reviewNotesIn;
    }

    /**
     * Returns <code>true</code> if the argument is an ScreeningHit instance and all identifiers for this entity
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
        if (!(object instanceof ScreeningHit))
        {
            return false;
        }
        final ScreeningHit that = (ScreeningHit)object;
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
     * Constructs new instances of {@link ScreeningHit}.
     */
    public static final class Factory
    {
        /**
         * Constructs a new instance of {@link ScreeningHit}.
         * @return new ScreeningHit()
         */
        public static ScreeningHit newInstance()
        {
            return new ScreeningHit();
        }

        /**
         * Constructs a new instance of {@link ScreeningHit}, taking all required and/or
         * read-only properties as arguments, except for identifiers.
         * @param createdAt LocalDateTime
         * @param createdBy String
         * @param kycRecordId String
         * @param subjectName String
         * @param source ScreeningListSource
         * @param entryReference String
         * @param entryName String
         * @param status ScreeningHitStatus
         * @return newInstance
         */
        public static ScreeningHit newInstance(LocalDateTime createdAt, String createdBy, String kycRecordId, String subjectName, ScreeningListSource source, String entryReference, String entryName, ScreeningHitStatus status)
        {
            final ScreeningHit entity = new ScreeningHit();
            entity.setCreatedAt(createdAt);
            entity.setCreatedBy(createdBy);
            entity.setKycRecordId(kycRecordId);
            entity.setSubjectName(subjectName);
            entity.setSource(source);
            entity.setEntryReference(entryReference);
            entity.setEntryName(entryName);
            entity.setStatus(status);
            return entity;
        }

        /**
         * Constructs a new instance of {@link ScreeningHit}, taking all possible properties
         * (except the identifier(s))as arguments.
         * @param createdAt LocalDateTime
         * @param modifiedAt LocalDateTime
         * @param createdBy String
         * @param modifiedBy String
         * @param kycRecordId String
         * @param subjectName String
         * @param subjectRole String
         * @param source ScreeningListSource
         * @param entryReference String
         * @param entryName String
         * @param score Double
         * @param status ScreeningHitStatus
         * @param listVersion String
         * @param reviewedBy String
         * @param reviewedAt LocalDateTime
         * @param reviewNotes String
         * @return newInstance ScreeningHit
         */
        public static ScreeningHit newInstance(LocalDateTime createdAt, LocalDateTime modifiedAt, String createdBy, String modifiedBy, String kycRecordId, String subjectName, String subjectRole, ScreeningListSource source, String entryReference, String entryName, Double score, ScreeningHitStatus status, String listVersion, String reviewedBy, LocalDateTime reviewedAt, String reviewNotes)
        {
            final ScreeningHit entity = new ScreeningHit();
            entity.setCreatedAt(createdAt);
            entity.setModifiedAt(modifiedAt);
            entity.setCreatedBy(createdBy);
            entity.setModifiedBy(modifiedBy);
            entity.setKycRecordId(kycRecordId);
            entity.setSubjectName(subjectName);
            entity.setSubjectRole(subjectRole);
            entity.setSource(source);
            entity.setEntryReference(entryReference);
            entity.setEntryName(entryName);
            entity.setScore(score);
            entity.setStatus(status);
            entity.setListVersion(listVersion);
            entity.setReviewedBy(reviewedBy);
            entity.setReviewedAt(reviewedAt);
            entity.setReviewNotes(reviewNotes);
            return entity;
        }
    }

    /**
     * @param other
     * @return a negative integer, zero, or a positive integer as this object is less than, equal to, or greater than the specified object.
     * @see Comparable#compareTo
     */
    public int compareTo(ScreeningHit other)
    {
        int cmp = 0;
        if (this.getId() != null)
        {
            cmp = this.getId().compareTo(other.getId());
        }
        else
        {
            if (this.getKycRecordId() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getKycRecordId().compareTo(other.getKycRecordId()));
            }
            if (this.getSubjectName() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getSubjectName().compareTo(other.getSubjectName()));
            }
            if (this.getSubjectRole() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getSubjectRole().compareTo(other.getSubjectRole()));
            }
            if (this.getSource() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getSource().compareTo(other.getSource()));
            }
            if (this.getEntryReference() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getEntryReference().compareTo(other.getEntryReference()));
            }
            if (this.getEntryName() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getEntryName().compareTo(other.getEntryName()));
            }
            if (this.getScore() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getScore().compareTo(other.getScore()));
            }
            if (this.getStatus() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getStatus().compareTo(other.getStatus()));
            }
            if (this.getListVersion() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getListVersion().compareTo(other.getListVersion()));
            }
            if (this.getReviewedBy() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getReviewedBy().compareTo(other.getReviewedBy()));
            }
            if (this.getReviewedAt() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getReviewedAt().compareTo(other.getReviewedAt()));
            }
            if (this.getReviewNotes() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getReviewNotes().compareTo(other.getReviewNotes()));
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
        sb.append("ScreeningHit(");
        sb.append(super.toString());
        sb.append(" id=").append(getId());
        sb.append(" kycRecordId=").append(getKycRecordId());
        sb.append(" subjectName=").append(getSubjectName());
        sb.append(" subjectRole=").append(getSubjectRole());
        sb.append(" source=").append(getSource());
        sb.append(" entryReference=").append(getEntryReference());
        sb.append(" entryName=").append(getEntryName());
        sb.append(" score=").append(getScore());
        sb.append(" status=").append(getStatus());
        sb.append(" listVersion=").append(getListVersion());
        sb.append(" reviewedBy=").append(getReviewedBy());
        sb.append(" reviewedAt=").append(getReviewedAt());
        sb.append(" reviewNotes=").append(getReviewNotes());
        sb.append(" createdAt=").append(getCreatedAt());
        sb.append(" modifiedAt=").append(getModifiedAt());
        sb.append(" createdBy=").append(getCreatedBy());
        sb.append(" modifiedBy=").append(getModifiedBy());
        sb.append(")");
        return sb.toString();
    }
}