package bw.co.knowvera.form;

/**
 * Life cycle of a custom form: only a published form can be given to clients to fill.
 */
public enum FormStatus
{
    /** Being designed: not yet available to clients. */
    DRAFT,
    /** Available: clients can be asked to fill it. */
    PUBLISHED,
    /** Withdrawn: existing submissions stay, no new ones are created. */
    ARCHIVED
}
