package bw.co.knowvera.suspicion;

import org.apache.commons.lang3.exception.ExceptionUtils;

/**
 * The default exception thrown for unexpected errors occurring
 * within {@link bw.co.knowvera.suspicion.SuspicionCaseService}.
 */
public class SuspicionCaseServiceException
    extends RuntimeException
{
    /**
     * The serial version UID of this class. Needed for serialization.
     */
    private static final long serialVersionUID = -3734116660183194152L;

    /**
     * The default constructor for <code>SuspicionCaseServiceException</code>.
     */
    public SuspicionCaseServiceException()
    {
        // Documented empty block
    }

    /**
     * Constructs a new instance of <code>SuspicionCaseServiceException</code>.
     *
     * @param throwable the parent Throwable
     */
    public SuspicionCaseServiceException(Throwable throwable)
    {
        super(findRootCause(throwable));
    }

    /**
     * Constructs a new instance of <code>SuspicionCaseServiceException</code>.
     *
     * @param message the throwable message.
     */
    public SuspicionCaseServiceException(String message)
    {
        super(message);
    }

    /**
     * Constructs a new instance of <code>SuspicionCaseServiceException</code>.
     *
     * @param message the throwable message.
     * @param throwable the parent of this Throwable.
     */
    public SuspicionCaseServiceException(String message, Throwable throwable)
    {
        super(message, findRootCause(throwable));
    }

    /**
     * Finds the root cause of the parent exception
     * by traveling up the exception tree
     */
    private static Throwable findRootCause(Throwable th)
    {
        if (th != null)
        {
            // Reflectively get any exception causes.
            try
            {
                th = ExceptionUtils.getRootCause(th);
            }
            catch (Exception ex)
            {
                // just print the exception and continue
                ex.printStackTrace();
            }

            if (th.getCause() != null)
            {
                th = th.getCause();
                th = findRootCause(th);
            }
        }
        return th;
    }

    private Object[] messageArguments;

    /**
     * Gets the message arguments that can be used by message resources (in
     * something like the presentation tier)
     *
     * @return messageArguments
     */
    public Object[] getMessageArguments()
    {
        return this.messageArguments;
    }

    /**
     * Sets the message arguments that can be used by message resources (in
     * something like the presentation tier)
     *
     * @param messageArgumentsIn
     */
    public void setMessageArguments(Object[] messageArgumentsIn)
    {
        this.messageArguments = messageArgumentsIn;
    }
}