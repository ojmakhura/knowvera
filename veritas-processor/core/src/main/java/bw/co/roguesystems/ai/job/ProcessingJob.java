package bw.co.roguesystems.ai.job;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OrderColumn;
import jakarta.persistence.Table;

/**
 * A processed document: the profile version that ran, every step's provider and model, and the
 * outputs, so any extraction can be traced back to how it was produced.
 */
@Entity
@Table(name = "veritas_job", indexes = {
        @Index(name = "ix_veritas_job_correlation", columnList = "correlation_id"),
        @Index(name = "ix_veritas_job_started_at", columnList = "started_at") })
public class ProcessingJob {

    @Id
    @Column(name = "id", nullable = false, length = 64)
    private String id;

    @Column(name = "correlation_id")
    private String correlationId;

    /** Who submitted the job through the API; null for jobs from the queue. */
    @Column(name = "submitted_by")
    private String submittedBy;

    @Column(name = "profile_code", nullable = false, length = 100)
    private String profileCode;

    @Column(name = "profile_version")
    private Integer profileVersion;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private JobStatus status;

    @Column(name = "started_at", nullable = false)
    private Instant startedAt;

    @Column(name = "finished_at")
    private Instant finishedAt;

    /** The request, without its text, as JSON. */
    @Column(name = "request_json", columnDefinition = "TEXT")
    private String requestJson;

    @Column(name = "result_text", columnDefinition = "TEXT")
    private String text;

    @Column(name = "fields_json", columnDefinition = "TEXT")
    private String fieldsJson;

    @Column(name = "classification_json", columnDefinition = "TEXT")
    private String classificationJson;

    @Column(name = "error", columnDefinition = "TEXT")
    private String error;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "veritas_job_step", joinColumns = @JoinColumn(name = "job_id"))
    @OrderColumn(name = "position")
    private List<JobStep> steps = new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }

    public String getSubmittedBy() {
        return submittedBy;
    }

    public void setSubmittedBy(String submittedBy) {
        this.submittedBy = submittedBy;
    }

    public String getProfileCode() {
        return profileCode;
    }

    public void setProfileCode(String profileCode) {
        this.profileCode = profileCode;
    }

    public Integer getProfileVersion() {
        return profileVersion;
    }

    public void setProfileVersion(Integer profileVersion) {
        this.profileVersion = profileVersion;
    }

    public JobStatus getStatus() {
        return status;
    }

    public void setStatus(JobStatus status) {
        this.status = status;
    }

    public Instant getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(Instant startedAt) {
        this.startedAt = startedAt;
    }

    public Instant getFinishedAt() {
        return finishedAt;
    }

    public void setFinishedAt(Instant finishedAt) {
        this.finishedAt = finishedAt;
    }

    public String getRequestJson() {
        return requestJson;
    }

    public void setRequestJson(String requestJson) {
        this.requestJson = requestJson;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getFieldsJson() {
        return fieldsJson;
    }

    public void setFieldsJson(String fieldsJson) {
        this.fieldsJson = fieldsJson;
    }

    public String getClassificationJson() {
        return classificationJson;
    }

    public void setClassificationJson(String classificationJson) {
        this.classificationJson = classificationJson;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public List<JobStep> getSteps() {
        return steps;
    }

    public void setSteps(List<JobStep> steps) {
        this.steps = steps;
    }
}
