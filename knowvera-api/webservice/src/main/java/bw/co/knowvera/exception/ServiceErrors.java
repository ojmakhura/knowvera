package bw.co.knowvera.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;

/**
 * What a caller is told when a service fails for a reason no advice has a message for. The
 * exception's own text (class names, SQL, constraint names) goes to the log, never the response.
 */
public final class ServiceErrors {

    public static final String UNEXPECTED = "Something went wrong on our side and this could not be completed. "
            + "Please try again, and contact support if it keeps happening.";

    public static final String CONFLICT = "This could not be saved because it conflicts with a record that "
            + "already exists.";

    /** How the generated service bases start the message of the exception they wrap a failure in. */
    private static final String WRAPPED = "Error performing";

    private ServiceErrors() {
    }

    /**
     * The response status: 400 when a service refused the request with its own message, 409 for a
     * database conflict (duplicate, still referenced), else 500.
     */
    public static HttpStatus status(Throwable error) {
        if (refusal(error) != null) {
            return HttpStatus.BAD_REQUEST;
        }
        return isConflict(error) ? HttpStatus.CONFLICT : HttpStatus.INTERNAL_SERVER_ERROR;
    }

    /** The message for the response, in plain words. */
    public static String message(Throwable error) {
        String refusal = refusal(error);
        if (refusal != null) {
            return refusal;
        }
        return isConflict(error) ? CONFLICT : UNEXPECTED;
    }

    /**
     * The message a service wrote for the caller ("Organisation X is retained until ..."), if that
     * is what failed. Every service call is wrapped as "Error performing '...' --> cause"; the
     * service's own refusal is a service exception inside that, with a message and no cause.
     */
    private static String refusal(Throwable error) {

        for (Throwable cause = error; cause != null; cause = cause.getCause() == cause ? null : cause.getCause()) {
            String message = cause.getMessage();
            if (cause.getCause() == null && cause.getClass().getSimpleName().endsWith("ServiceException")
                    && message != null && !message.isBlank() && !message.startsWith(WRAPPED)) {
                return message;
            }
        }
        return null;
    }

    private static boolean isConflict(Throwable error) {

        // Services wrap what they throw, sometimes only as text ("... --> ...DataIntegrityViolationException: ...")
        for (Throwable cause = error; cause != null; cause = cause.getCause() == cause ? null : cause.getCause()) {
            if (cause instanceof DataIntegrityViolationException
                    || (cause.getMessage() != null && cause.getMessage().contains("DataIntegrityViolationException"))) {
                return true;
            }
        }
        return false;
    }
}
