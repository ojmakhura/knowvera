package bw.co.roguesystems.ai.provider;

/**
 * A provider could not produce a result; the step tries its next provider. A retryable failure
 * (the server was unreachable, timed out or failed) may succeed later: when every provider of a
 * step fails and one of them failed that way, the step is tried again.
 */
public class ProviderException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final boolean retryable;

    public ProviderException(String message) {
        this(message, null, false);
    }

    public ProviderException(String message, Throwable cause) {
        this(message, cause, false);
    }

    public ProviderException(String message, Throwable cause, boolean retryable) {
        super(message, cause);
        this.retryable = retryable;
    }

    public boolean isRetryable() {
        return retryable;
    }
}
