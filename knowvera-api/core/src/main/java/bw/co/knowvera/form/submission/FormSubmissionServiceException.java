package bw.co.knowvera.form.submission;

/**
 * Thrown by {@link FormSubmissionService}. Without a cause it states a rule the caller broke, in words
 * meant for the user; with a cause it wraps an unexpected failure.
 */
public class FormSubmissionServiceException
    extends RuntimeException
{
    private static final long serialVersionUID = 6093318827415570236L;

    private final boolean notFound;

    public FormSubmissionServiceException(String message)
    {
        this(message, false);
    }

    public FormSubmissionServiceException(String message, boolean notFound)
    {
        super(message);
        this.notFound = notFound;
    }

    public FormSubmissionServiceException(String message, Throwable throwable)
    {
        super(message, throwable);
        this.notFound = false;
    }

    /** @return a rule violation for a record that does not exist */
    public static FormSubmissionServiceException notFound(String message)
    {
        return new FormSubmissionServiceException(message, true);
    }

    /** @return true when the requested record does not exist */
    public boolean isNotFound()
    {
        return this.notFound;
    }
}
