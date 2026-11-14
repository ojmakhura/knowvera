/** Where a client's answers to a form stand. */
export enum FormSubmissionStatus {
    NEW = "NEW",
    DRAFT = "DRAFT",
    SUBMITTED = "SUBMITTED",
    RETURNED = "RETURNED",
    ACCEPTED = "ACCEPTED"
}

/** The states in which the client can still change the answers. */
export const EDITABLE_FORM_SUBMISSION_STATUSES = [
    FormSubmissionStatus.NEW,
    FormSubmissionStatus.DRAFT,
    FormSubmissionStatus.RETURNED,
];
