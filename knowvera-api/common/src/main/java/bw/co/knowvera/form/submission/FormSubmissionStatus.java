package bw.co.knowvera.form.submission;

/**
 * Where a client's answers to a form stand.
 */
public enum FormSubmissionStatus
{
    /** The client has been asked to fill the form and has not started. */
    NEW,
    /** The client has saved answers without submitting them. */
    DRAFT,
    /** The client has submitted the form; the organisation has not decided on it. */
    SUBMITTED,
    /** The organisation sent the form back to the client for correction. */
    RETURNED,
    /** The organisation accepted the answers. */
    ACCEPTED;

    /** @return true while the client can still change the answers */
    public boolean isEditable()
    {
        return this == NEW || this == DRAFT || this == RETURNED;
    }
}
