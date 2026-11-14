package bw.co.knowvera.processing.profile;

import java.net.URI;
import java.time.Instant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

/**
 * Errors of the {@link ProcessingProfileApi} endpoints as RFC 9457 problem details: an unknown
 * profile or version is 404, an invalid profile 400, and two saves of a profile at once 409.
 */
@RestControllerAdvice(assignableTypes = ProcessingProfileApi.class)
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ProcessingProfileApiAdvice {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProcessingProfileApiAdvice.class);
    private static final MediaType PROBLEM_JSON = MediaType.parseMediaType("application/problem+json");

    @ExceptionHandler(ProfileNotFoundException.class)
    public ResponseEntity<ProblemDetail> notFound(ProfileNotFoundException e, WebRequest request) {
        return problem(HttpStatus.NOT_FOUND, "profile-not-found", e.getMessage(), request);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ProblemDetail> invalid(IllegalArgumentException e, WebRequest request) {
        return problem(HttpStatus.BAD_REQUEST, "invalid-profile", e.getMessage(), request);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ProblemDetail> conflict(DataIntegrityViolationException e, WebRequest request) {
        LOGGER.warn("A profile was saved twice at once: {}", e.getMostSpecificCause().getMessage());
        return problem(HttpStatus.CONFLICT, "profile-conflict",
                "The profile was changed by someone else at the same time. Reload it and save again.", request);
    }

    private static ResponseEntity<ProblemDetail> problem(HttpStatus status, String type, String detail,
            WebRequest request) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(status, detail);
        problem.setType(URI.create("urn:knowvera:problem:" + type));
        problem.setTitle(status.getReasonPhrase());
        problem.setProperty("timestamp", Instant.now());
        problem.setInstance(URI.create(request.getDescription(false).replaceFirst("^uri=", "")));
        return ResponseEntity.status(status).contentType(PROBLEM_JSON).body(problem);
    }
}
