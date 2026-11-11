package bw.co.knowvera.screening;

import org.apache.commons.lang3.exception.ExceptionUtils;

/**
 * The default exception thrown for unexpected errors occurring
 * within {@link bw.co.knowvera.screening.ScreeningService}.
 */
public class ScreeningServiceException
    extends RuntimeException
{
    /**
     * The serial version UID of this class. Needed for serialization.
     */
    private static final long serialVersionUID = -1678078926297891409L;

    /**
     * The default constructor for <code>ScreeningServiceException</code>.
     */
    public ScreeningServiceException()
    {
        // Documented empty block
    }

    /**
     * Constructs a new instance of <code>ScreeningServiceException</code>.
     *
     * @param throwable the parent Throwable
     */
    public ScreeningServiceException(Throwable throwable)
    {
        super(findRootCause(throwable));
    }

    /**
     * Constructs a new instance of <code>ScreeningServiceException</code>.
     *
     * @param message the throwable message.
     */
    public ScreeningServiceException(String message)
    {
        super(message);
    }

    /**
     * Constructs a new instance of <code>ScreeningServiceException</code>.
     *
     * @param message the throwable message.
     * @param throwable the parent of this Throwable.
     */
    public ScreeningServiceException(String message, Throwable throwable)
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