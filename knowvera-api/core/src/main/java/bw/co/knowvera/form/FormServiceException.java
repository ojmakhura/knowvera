package bw.co.knowvera.form;

/**
 * Thrown by {@link FormService}. Without a cause it states a rule the caller broke, in words
 * meant for the user; with a cause it wraps an unexpected failure.
 */
public class FormServiceException
    extends RuntimeException
{
    private static final long serialVersionUID = -4127093387715620248L;

    private final boolean notFound;

    public FormServiceException(String message)
    {
        this(message, false);
    }

    public FormServiceException(String message, boolean notFound)
    {
        super(message);
        this.notFound = notFound;
    }

    public FormServiceException(String message, Throwable throwable)
    {
        super(message, throwable);
        this.notFound = false;
    }

    /** @return a rule violation for a record that does not exist */
    public static FormServiceException notFound(String message)
    {
        return new FormServiceException(message, true);
    }

    /** @return true when the requested record does not exist */
    public boolean isNotFound()
    {
        return this.notFound;
    }
}
