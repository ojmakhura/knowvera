package bw.co.knowvera.suspicion;


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

import java.net.URI;
import java.time.Instant;
import java.util.List;

/**
 * Translates errors thrown by {@link SuspicionCaseApiServiceBase} into RFC 9457 compliant
 * {@link ProblemDetail} payloads for the suspicionCaseApi endpoints.
 *
 * Scoped to {@link SuspicionCaseApi} only, and ordered ahead
 * of global exception handlers so that this advice's friendlier messages take precedence.
 */
@RestControllerAdvice(assignableTypes = SuspicionCaseApi.class)
@Order(Ordered.HIGHEST_PRECEDENCE)
public class SuspicionCaseApiAdvice {

    private static final Logger LOGGER = LoggerFactory.getLogger(SuspicionCaseApiAdvice.class);
    private static final MediaType PROBLEM_JSON_MEDIA_TYPE = MediaType.parseMediaType("application/problem+json");

    /**
     * Ordered list of validation-message mappings.
     * Each entry matches a method-name substring and (optionally) a
     * field-name substring against the raw exception message, and
     * supplies the user-facing replacement text (RFC 9457 'detail').
     *
     * Entries are checked in order — more specific field matches
     * (nested attribute paths) are listed ahead of their less specific
     * parent-field counterparts within the same operation.
     */
    private static final List<bw.co.knowvera.ValidationMapping> VALIDATION_MAPPINGS = List.of(
         new bw.co.knowvera.ValidationMapping(".raise(", "'suspicionCase'",
                HttpStatus.BAD_REQUEST,
                "missing-parameter",
                "Suspicion Case is required.")
        , new bw.co.knowvera.ValidationMapping(".findById(", "'id'",
                HttpStatus.BAD_REQUEST,
                "missing-parameter",
                "Id is required.")
        , new bw.co.knowvera.ValidationMapping(".findByRecord(", "'kycRecordId'",
                HttpStatus.BAD_REQUEST,
                "missing-parameter",
                "Kyc Record Id is required.")
        , new bw.co.knowvera.ValidationMapping(".addNote(", "'id'",
                HttpStatus.BAD_REQUEST,
                "missing-parameter",
                "Id is required.")
        , new bw.co.knowvera.ValidationMapping(".addNote(", "'note'",
                HttpStatus.BAD_REQUEST,
                "missing-parameter",
                "Note is required.")
        , new bw.co.knowvera.ValidationMapping(".escalate(", "'id'",
                HttpStatus.BAD_REQUEST,
                "missing-parameter",
                "Id is required.")
        , new bw.co.knowvera.ValidationMapping(".recordReport(", "'id'",
                HttpStatus.BAD_REQUEST,
                "missing-parameter",
                "Id is required.")
        , new bw.co.knowvera.ValidationMapping(".recordReport(", "'fiaReference'",
                HttpStatus.BAD_REQUEST,
                "missing-parameter",
                "Fia Reference is required.")
        , new bw.co.knowvera.ValidationMapping(".close(", "'id'",
                HttpStatus.BAD_REQUEST,
                "missing-parameter",
                "Id is required.")
        , new bw.co.knowvera.ValidationMapping(".close(", "'outcome'",
                HttpStatus.BAD_REQUEST,
                "missing-parameter",
                "Outcome is required.")
        , new bw.co.knowvera.ValidationMapping(".draftReport(", "'id'",
                HttpStatus.BAD_REQUEST,
                "missing-parameter",
                "Id is required.")
    );

    private static final String FALLBACK_MESSAGE = "Unknown error has occurred. Please contact administrator.";

    /**
     * Handles manual argument validation checks thrown by SuspicionCaseApiServiceBase.
     * Maps generated validation messages to friendly human-readable descriptions (RFC 9457 Bad Request).
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ProblemDetail> handleIllegalArgument(IllegalArgumentException ex, WebRequest request) {

        String message = ex.getMessage() == null ? "" : ex.getMessage();
        bw.co.knowvera.ValidationMapping mapping = findMapping(message).orElse(null);
        String friendly = mapping == null
                ? FALLBACK_MESSAGE
                : mapping.friendlyMessage();

        HttpStatus status = mapping == null
                ? HttpStatus.INTERNAL_SERVER_ERROR
                : mapping.status();

        String type = mapping == null
                ? "service-error"
                : mapping.type();

        List<String> errors = message.isBlank()
                ? List.of()
                : List.of(message);

        LOGGER.warn("SuspicionCaseApi validation failed: {}", message);

        ProblemDetail problemDetail = buildProblemDetail(
                status,
                type,
                "Bad Request",
                friendly,
                request,
                errors
        );

        return ResponseEntity
                .status(status)
                .contentType(PROBLEM_JSON_MEDIA_TYPE)
                .body(problemDetail);
    }


    /**
     * Handles {@link bw.co.knowvera.suspicion.SuspicionCaseServiceException} thrown by referenced services (RFC 9457 Internal Server Error).
     */
    @ExceptionHandler(bw.co.knowvera.suspicion.SuspicionCaseServiceException.class)
    public ResponseEntity<ProblemDetail> handleSuspicionCaseServiceException(bw.co.knowvera.suspicion.SuspicionCaseServiceException ex, WebRequest request) {

        String message = ex.getMessage() == null ? "" : ex.getMessage();
        bw.co.knowvera.ValidationMapping mapping = findMapping(message).orElse(null);
        String friendly = mapping == null
                ? ex.getMessage()
                : mapping.friendlyMessage();

        HttpStatus status = mapping == null
                ? HttpStatus.INTERNAL_SERVER_ERROR
                : mapping.status();

        String type = mapping == null
                ? "service-error"
                : mapping.type();
                
        List<String> errors = message.isBlank()
                ? List.of()
                : List.of(message);
        LOGGER.error("Referenced service error (SuspicionCaseService)", ex);

        ProblemDetail problemDetail = buildProblemDetail(
                status,
                type,
                "Dependency Error",
                friendly,
                request,
                errors
        );

        return ResponseEntity
                .status(status)
                .contentType(PROBLEM_JSON_MEDIA_TYPE)
                .body(problemDetail);
    }

    /**
     * Builds an RFC 9457 {@link ProblemDetail} response body, with
     * {@code timestamp} and {@code errors} as extension members (RFC 9457 Section 3.2).
     */
    private ProblemDetail buildProblemDetail(
            HttpStatus status,
            String typeSuffix,
            String title,
            String detail,
            WebRequest request,
            List<String> errors) {

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, detail);
        problemDetail.setType(URI.create(typeSuffix));
        problemDetail.setTitle(title);
        problemDetail.setInstance(URI.create(getInstanceUri(request)));
        problemDetail.setProperty("timestamp", Instant.now());
        problemDetail.setProperty("errors", errors);

        return problemDetail;
    }

    /**
     * Extracts the request URI from the Spring WebRequest.
     */
    private String getInstanceUri(WebRequest request) {
        return request.getDescription(false)
                .replace("uri=", "");
    }

    /**
     * Finds the first validation mapping that matches the given message.
     *
     * @param message the validation message to match
     * @return an {@link Optional} containing the matching {@link ValidationMapping}, or empty if none found
     */
    private java.util.Optional<bw.co.knowvera.ValidationMapping> findMapping(String message) {
        return VALIDATION_MAPPINGS.stream()
                .filter(mapping -> mapping.matches(message))
                .findFirst();
    }
}