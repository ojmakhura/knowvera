// false
// $entity.generalization.embeddableSuperclass
package bw.co.knowvera;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * TODO: Model Documentation for bw.co.knowvera.Auditable
 */
@MappedSuperclass
public abstract class Auditable
    implements Serializable{
    /**
     * The serial version UID of this class. Needed for serialization.
     */
    private static final long serialVersionUID = 6496144094265008779L;

    // Generate 5 attributes

    @Column(name = "CREATED_AT", unique = false, nullable = false, insertable = true, updatable = true)
    private LocalDateTime createdAt;

    /**
     * TODO: Model Documentation for bw.co.knowvera.Auditable.createdAt
     * @return this.createdAt LocalDateTime
     */
    public LocalDateTime getCreatedAt()
    {
        return this.createdAt;
    }

    /**
     * TODO: Model Documentation for bw.co.knowvera.Auditable.createdAt
     * @param createdAtIn LocalDateTime
     */
    public void setCreatedAt(LocalDateTime createdAtIn)
    {
        this.createdAt = createdAtIn;
    }


    @Column(name = "MODIFIED_AT", unique = false, nullable = true, insertable = true, updatable = true)
    private LocalDateTime modifiedAt;

    /**
     * TODO: Model Documentation for bw.co.knowvera.Auditable.modifiedAt
     * @return this.modifiedAt LocalDateTime
     */
    public LocalDateTime getModifiedAt()
    {
        return this.modifiedAt;
    }

    /**
     * TODO: Model Documentation for bw.co.knowvera.Auditable.modifiedAt
     * @param modifiedAtIn LocalDateTime
     */
    public void setModifiedAt(LocalDateTime modifiedAtIn)
    {
        this.modifiedAt = modifiedAtIn;
    }


    @Column(name = "CREATED_BY", unique = false, nullable = false, insertable = true, updatable = true)
    private String createdBy;

    /**
     * TODO: Model Documentation for bw.co.knowvera.Auditable.createdBy
     * @return this.createdBy String
     */
    public String getCreatedBy()
    {
        return this.createdBy;
    }

    /**
     * TODO: Model Documentation for bw.co.knowvera.Auditable.createdBy
     * @param createdByIn String
     */
    public void setCreatedBy(String createdByIn)
    {
        this.createdBy = createdByIn;
    }


    @Column(name = "MODIFIED_BY", unique = false, nullable = true, insertable = true, updatable = true)
    private String modifiedBy;

    /**
     * TODO: Model Documentation for bw.co.knowvera.Auditable.modifiedBy
     * @return this.modifiedBy String
     */
    public String getModifiedBy()
    {
        return this.modifiedBy;
    }

    /**
     * TODO: Model Documentation for bw.co.knowvera.Auditable.modifiedBy
     * @param modifiedByIn String
     */
    public void setModifiedBy(String modifiedByIn)
    {
        this.modifiedBy = modifiedByIn;
    }

}