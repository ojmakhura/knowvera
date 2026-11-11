package bw.co.roguesystems.ai.job;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.json.JsonMapper;

/**
 * The job history.
 */
@Service
public class ProcessingJobService {

    private final ProcessingJobRepository repository;
    private final JsonMapper jsonMapper;

    public ProcessingJobService(ProcessingJobRepository repository, JsonMapper jsonMapper) {
        this.repository = repository;
        this.jsonMapper = jsonMapper;
    }

    /**
     * Records a job as running; a job id that ran before starts over.
     *
     * @param submittedBy who submitted it through the API (null for jobs from the queue)
     * @throws IllegalArgumentException when the id is the job of another API caller
     */
    @Transactional
    public void start(ProcessingJobRequest request, String submittedBy) {
        ProcessingJob job = repository.findById(request.jobId()).orElseGet(ProcessingJob::new);
        checkOwner(job, submittedBy);
        job.setId(request.jobId());
        job.setSubmittedBy(submittedBy);
        job.setCorrelationId(request.correlationId());
        job.setProfileCode(request.profileCode());
        job.setProfileVersion(null);
        job.setStatus(JobStatus.RUNNING);
        job.setStartedAt(Instant.now());
        job.setFinishedAt(null);
        job.setRequestJson(jsonMapper.writeValueAsString(request.withoutText()));
        job.setText(null);
        job.setFieldsJson(null);
        job.setClassificationJson(null);
        job.setError(null);
        job.getSteps().clear();
        repository.save(job);
    }

    @Transactional
    public void finish(ProcessingJobResult result) {
        ProcessingJob job = repository.findById(result.jobId()).orElseThrow();
        job.setProfileVersion(result.profileVersion());
        job.setStatus(result.status());
        job.setFinishedAt(Instant.now());
        job.setText(result.text());
        job.setFieldsJson(result.fields() == null ? null : jsonMapper.writeValueAsString(result.fields()));
        job.setClassificationJson(
                result.classification() == null ? null : jsonMapper.writeValueAsString(result.classification()));
        job.setError(result.error());
        job.getSteps().clear();
        result.steps().forEach(step -> job.getSteps().add(new JobStep(step)));
        repository.save(job);
    }

    /**
     * A job as {@code caller} may see it: jobs submitted through the API are their submitter's,
     * jobs from the queue are readable by every caller allowed to read jobs.
     */
    @Transactional(readOnly = true)
    public Optional<ProcessingJobResult> find(String jobId, String caller) {
        return repository.findById(jobId)
                .filter(job -> job.getSubmittedBy() == null || job.getSubmittedBy().equals(caller))
                .map(this::toResult);
    }

    /** @throws IllegalArgumentException when the job id is the job of another API caller */
    @Transactional(readOnly = true)
    public void checkSubmittable(String jobId, String submittedBy) {
        repository.findById(jobId).ifPresent(job -> checkOwner(job, submittedBy));
    }

    private static void checkOwner(ProcessingJob job, String submittedBy) {
        if (job.getSubmittedBy() != null && !job.getSubmittedBy().equals(submittedBy)) {
            throw new IllegalArgumentException("Job id " + job.getId() + " is in use: choose another");
        }
    }

    /**
     * Fails the jobs that have been running since before {@code startedBefore}: the processor
     * stopped while it ran them. Returns their results, for their callers.
     */
    @Transactional
    public List<ProcessingJobResult> failStale(Instant startedBefore) {
        List<ProcessingJob> stale = repository.findByStatusAndStartedAtBefore(JobStatus.RUNNING, startedBefore);
        stale.forEach(job -> {
            job.setStatus(JobStatus.FAILED);
            job.setFinishedAt(Instant.now());
            job.setError("The job was interrupted: the processor stopped while it ran. Submit it again.");
        });
        return repository.saveAll(stale).stream().map(this::toResult).toList();
    }

    /** Deletes a batch of the jobs started before then; returns how many, 0 when none are left. */
    @Transactional
    public int purge(Instant startedBefore) {
        List<ProcessingJob> old = repository.findTop200ByStartedAtBeforeOrderByStartedAt(startedBefore);
        repository.deleteAll(old);
        return old.size();
    }

    private ProcessingJobResult toResult(ProcessingJob job) {
        ProcessingJobRequest request = job.getRequestJson() == null ? null
                : jsonMapper.readValue(job.getRequestJson(), ProcessingJobRequest.class);
        Map<String, Object> fields = job.getFieldsJson() == null ? null
                : jsonMapper.readValue(job.getFieldsJson(), new TypeReference<Map<String, Object>>() {
                });
        Classification classification = job.getClassificationJson() == null ? null
                : jsonMapper.readValue(job.getClassificationJson(), Classification.class);
        return new ProcessingJobResult(job.getId(), job.getCorrelationId(), job.getProfileCode(),
                job.getProfileVersion(), job.getStatus(), job.getText(), fields, classification,
                job.getSteps().stream().map(JobStep::toResult).toList(), job.getError(),
                request == null ? null : request.attributes());
    }
}
