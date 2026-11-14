package bw.co.knowvera.form;

import bw.co.knowvera.form.submission.FormSubmissionApi;
import bw.co.knowvera.form.submission.FormSubmissionServiceException;
import java.net.URI;
import java.time.Instant;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

/**
 * Errors of the form and form submission APIs as problem details. A rule the caller broke
 * (a service exception without a cause) is a 400, or a 404 for a missing record, with the
 * service's message; anything else is a 500 that does not say what went wrong.
 */
@RestControllerAdvice(assignableTypes = {FormApi.class, FormSubmissionApi.class})
@Order(Ordered.HIGHEST_PRECEDENCE)
public class FormApiAdvice {

    private static final Logger LOGGER = LoggerFactory.getLogger(FormApiAdvice.class);
    private static final MediaType PROBLEM_JSON_MEDIA_TYPE = MediaType.parseMediaType("application/problem+json");
    private static final String FALLBACK_MESSAGE = "Unknown error has occurred. Please contact administrator.";

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ProblemDetail> handleIllegalArgument(IllegalArgumentException ex, WebRequest request) {
        LOGGER.warn("Form API validation failed: {}", ex.getMessage());
        return problem(HttpStatus.BAD_REQUEST, "missing-parameter", "Bad Request",
                "A required value is missing.", ex.getMessage(), request);
    }

    @ExceptionHandler(FormServiceException.class)
    public ResponseEntity<ProblemDetail> handleFormServiceException(FormServiceException ex, WebRequest request) {
        return serviceProblem(ex, ex.isNotFound(), request);
    }

    @ExceptionHandler(FormSubmissionServiceException.class)
    public ResponseEntity<ProblemDetail> handleFormSubmissionServiceException(FormSubmissionServiceException ex,
            WebRequest request) {
        return serviceProblem(ex, ex.isNotFound(), request);
    }

    private ResponseEntity<ProblemDetail> serviceProblem(RuntimeException ex, boolean notFound, WebRequest request) {
        if (ex.getCause() != null) {
            LOGGER.error("Form service error", ex);
            return problem(HttpStatus.INTERNAL_SERVER_ERROR, "service-error", "Service Error",
                    FALLBACK_MESSAGE, null, request);
        }
        LOGGER.warn("Form request rejected: {}", ex.getMessage());
        return notFound
                ? problem(HttpStatus.NOT_FOUND, "not-found", "Not Found", ex.getMessage(), ex.getMessage(), request)
                : problem(HttpStatus.BAD_REQUEST, "invalid-request", "Bad Request", ex.getMessage(), ex.getMessage(), request);
    }

    private ResponseEntity<ProblemDetail> problem(HttpStatus status, String type, String title, String detail,
            String error, WebRequest request) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, detail);
        problemDetail.setType(URI.create(type));
        problemDetail.setTitle(title);
        problemDetail.setInstance(URI.create(request.getDescription(false).replace("uri=", "")));
        problemDetail.setProperty("timestamp", Instant.now());
        problemDetail.setProperty("errors", error == null || error.isBlank() ? List.of() : List.of(error));
        return ResponseEntity.status(status).contentType(PROBLEM_JSON_MEDIA_TYPE).body(problemDetail);
    }
}
