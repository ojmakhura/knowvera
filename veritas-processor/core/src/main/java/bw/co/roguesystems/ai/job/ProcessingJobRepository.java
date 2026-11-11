package bw.co.roguesystems.ai.job;

import java.time.Instant;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProcessingJobRepository extends JpaRepository<ProcessingJob, String> {

    List<ProcessingJob> findByStatusAndStartedAtBefore(JobStatus status, Instant startedBefore);

    /** The oldest jobs started before then, a batch at a time. */
    List<ProcessingJob> findTop200ByStartedAtBeforeOrderByStartedAt(Instant startedBefore);
}
