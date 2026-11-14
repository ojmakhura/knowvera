package bw.co.knowvera.processing.job;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import bw.co.knowvera.processing.ProcessingProperties;

/**
 * Keeps the job history honest and no larger than it has to be:
 * <ul>
 * <li>a job still RUNNING after app.processing.jobs.stale-after was interrupted (the processor stopped
 * while it ran): it is failed, and the FAILED result published so its caller hears of it;</li>
 * <li>jobs older than app.processing.jobs.retention are deleted, with the document text and fields they
 * hold (off unless a retention is set).</li>
 * </ul>
 */
@Component
@ConditionalOnProperty(name = "app.processing.worker.enabled", havingValue = "true", matchIfMissing = true)
public class JobMaintenance {

    private static final Logger log = LoggerFactory.getLogger(JobMaintenance.class);

    private final ProcessingJobService jobs;
    private final ResultPublisher publisher;
    private final ProcessingProperties.Jobs settings;

    public JobMaintenance(ProcessingJobService jobs, ResultPublisher publisher, ProcessingProperties properties) {
        this.jobs = jobs;
        this.publisher = publisher;
        this.settings = properties.getJobs();
    }

    @Scheduled(initialDelayString = "${app.processing.jobs.maintenance-interval:PT10M}",
            fixedDelayString = "${app.processing.jobs.maintenance-interval:PT10M}")
    public void run() {
        failStaleJobs();
        purgeOldJobs();
    }

    void failStaleJobs() {
        Duration staleAfter = settings.getStaleAfter();
        if (staleAfter == null || staleAfter.isZero() || staleAfter.isNegative()) {
            return;
        }
        List<ProcessingJobResult> failed = jobs.failStale(Instant.now().minus(staleAfter));
        for (ProcessingJobResult result : failed) {
            log.warn("Job {} was still running after {}: failed as interrupted", result.jobId(), staleAfter);
            try {
                publisher.publish(result);
            } catch (RuntimeException e) {
                log.error("Could not publish the failure of interrupted job {}: {}", result.jobId(), e.getMessage());
            }
        }
    }

    void purgeOldJobs() {
        Duration retention = settings.getRetention();
        if (retention == null || retention.isZero() || retention.isNegative()) {
            return;
        }
        Instant before = Instant.now().minus(retention);
        int deleted = 0;
        for (int batch = jobs.purge(before); batch > 0; batch = jobs.purge(before)) {
            deleted += batch;
        }
        if (deleted > 0) {
            log.info("Deleted {} jobs older than {}", deleted, retention);
        }
    }
}
