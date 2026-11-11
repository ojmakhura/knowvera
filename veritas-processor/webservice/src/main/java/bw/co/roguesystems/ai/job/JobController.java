package bw.co.roguesystems.ai.job;

import java.util.Map;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import bw.co.roguesystems.ai.messaging.ResultPublisher;
import bw.co.roguesystems.ai.pipeline.PipelineRunner;
import bw.co.roguesystems.ai.web.Callers;
import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/jobs")
public class JobController {

    private final PipelineRunner runner;
    private final ProcessingJobService jobs;
    private final ResultPublisher publisher;

    public JobController(PipelineRunner runner, ProcessingJobService jobs, ResultPublisher publisher) {
        this.runner = runner;
        this.jobs = jobs;
        this.publisher = publisher;
    }

    @PostMapping
    @Operation(summary = "Submit job", description = "Queues the job (its result goes to the results queue), or "
            + "with wait=true runs it now and returns the result, e.g. to try a profile on a sample document")
    public ResponseEntity<?> submit(@RequestBody ProcessingJobRequest request,
            @RequestParam(defaultValue = "false") boolean wait, Authentication authentication) {
        String caller = Callers.name(authentication);
        ProcessingJobRequest job = request.jobId() == null || request.jobId().isBlank()
                ? request.withJobId(UUID.randomUUID().toString())
                : request;
        if (wait) {
            return ResponseEntity.ok(runner.run(job, caller));
        }
        jobs.checkSubmittable(job.jobId(), caller);
        publisher.submit(job, caller);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(Map.of("jobId", job.jobId()));
    }

    @GetMapping("/{jobId}")
    @Operation(summary = "Job", description = "A job's status and, once finished, its result. Jobs submitted "
            + "through this API are only shown to who submitted them")
    public ResponseEntity<ProcessingJobResult> find(@PathVariable String jobId, Authentication authentication) {
        return jobs.find(jobId, Callers.name(authentication)).map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
