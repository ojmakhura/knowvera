package bw.co.knowvera.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.net.URI;
import java.time.Instant;
import java.util.List;

@RestControllerAdvice
@Order(Ordered.LOWEST_PRECEDENCE)
public class FallbackExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(FallbackExceptionHandler.class);

    /**
     * @PreAuthorize and ownership (@RequiresOwnership) denials. Without this the generic
     * handler below would turn them into a 500.
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ProblemDetail> handleAccessDenied(
            AccessDeniedException ex, WebRequest request) {

        LOGGER.debug("Access denied for request [{}]: {}",
                request.getDescription(false), ex.getMessage());

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.FORBIDDEN,
                "You do not have permission to perform this action."
        );
        problemDetail.setType(URI.create("forbidden"));
        problemDetail.setTitle("FORBIDDEN");
        problemDetail.setInstance(URI.create(
                request.getDescription(false).replace("uri=", "")
        ));
        problemDetail.setProperty("timestamp", Instant.now());
        problemDetail.setProperty("errors", List.of());

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(problemDetail);
    }

    /** Uploads over spring.servlet.multipart limits. */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ProblemDetail> handleUploadTooLarge(
            MaxUploadSizeExceededException ex, WebRequest request) {

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.CONTENT_TOO_LARGE, "The upload is larger than allowed.");
        problemDetail.setInstance(URI.create(request.getDescription(false).replace("uri=", "")));
        problemDetail.setProperty("timestamp", Instant.now());
        problemDetail.setProperty("errors", List.of());

        return ResponseEntity.status(HttpStatus.CONTENT_TOO_LARGE).body(problemDetail);
    }

    /**
     * Exceptions that carry their own HTTP status (e.g. 400 for invalid input). Without this the
     * generic handler below would turn them into a 500.
     */
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ProblemDetail> handleResponseStatus(
            ResponseStatusException ex, WebRequest request) {

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(ex.getStatusCode(), ex.getReason());
        problemDetail.setInstance(URI.create(
                request.getDescription(false).replace("uri=", "")
        ));
        problemDetail.setProperty("timestamp", Instant.now());
        problemDetail.setProperty("errors", List.of());

        return ResponseEntity
                .status(ex.getStatusCode())
                .body(problemDetail);
    }

    /**
     * Fallback handler for any other unexpected exception, so clients never
     * see a raw stack trace or an unstructured 500.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleGenericException(
            Exception ex, WebRequest request) {

        LOGGER.error("Unhandled exception handling request [{}]",
                request.getDescription(false), ex);

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "An unexpected error occurred. Please contact support if the problem persists."
        );
        problemDetail.setType(URI.create("internal-server-error"));
        problemDetail.setTitle("INTERNAL_SERVER_ERROR");
        problemDetail.setInstance(URI.create(
                request.getDescription(false).replace("uri=", "")
        ));
        problemDetail.setProperty("timestamp", Instant.now());
        problemDetail.setProperty("errors", List.of());

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(problemDetail);
    }
}