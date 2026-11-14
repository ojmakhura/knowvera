package bw.co.knowvera.form.submission;

import jakarta.validation.Valid;
import java.util.List;

/**
 * The answers of clients to custom forms. The published forms of an organisation are required
 * from its clients as part of their KYC: each becomes a submission on the client's KYC record.
 * The client saves answers, submits them and the organisation accepts them or returns them for
 * correction.
 */
public interface FormSubmissionService
{
    /**
     * Asks a client to fill a published form as part of its KYC record. A record has a form to
     * fill only once at a time.
     * @param assignment the form and the client
     * @param user who asks
     * @return the new submission, without answers
     */
    public FormSubmissionDTO createSubmission(@Valid FormAssignmentDTO assignment, String user);

    /**
     * @param id the submission's id
     * @return the submission with its answers and the definition of its form
     */
    public FormSubmissionDTO findById(String id);

    /**
     * @param formId the form's id
     * @return the submissions of the form, newest first, without their answers
     */
    public List<FormSubmissionDTO> findByForm(String formId);

    /**
     * @param organisationId the id of the organisation that issued the forms
     * @return the submissions of the organisation's forms, newest first, without their answers
     */
    public List<FormSubmissionDTO> findByOrganisation(String organisationId);

    /**
     * @param kycRecordId the id of a KYC record
     * @return the forms that are part of the KYC record, newest first, without their answers
     */
    public List<FormSubmissionDTO> findByKycRecord(String kycRecordId);

    /**
     * The forms of a KYC record, after adding the ones its client has to fill and does not have
     * yet: every published form, meant for that kind of client, of the organisations that asked to
     * onboard the client (client requests that were not rejected).
     * @param kycRecordId the id of the client's KYC record
     * @param user who asks
     * @return the forms that are part of the KYC record, newest first, without their answers
     */
    public List<FormSubmissionDTO> findRequired(String kycRecordId, String user);

    /**
     * @param criteria the filters; empty ones are ignored
     * @return the matching submissions, newest first, without their answers
     */
    public List<FormSubmissionDTO> search(FormSubmissionSearchCriteria criteria);

    /**
     * Saves answers without submitting them. Only the given fields change; an empty answer clears
     * the field. Answers must fit their field (a number, one of the options...), but required
     * fields may still be empty.
     * @param id the submission's id
     * @param values the answers, by the id of their field
     * @param user who saves
     * @return the submission with its answers
     */
    public FormSubmissionDTO saveValues(String id, List<FormFieldValueDTO> values, String user);

    /**
     * Submits the answers to the organisation. Every required field must be answered.
     * @param id the submission's id
     * @param user who submits
     * @return the submitted submission
     */
    public FormSubmissionDTO submit(String id, String user);

    /**
     * The organisation's decision on submitted answers.
     * @param id the submission's id
     * @param status ACCEPTED, or RETURNED to have the client correct the answers
     * @param note the reason; required when the answers are returned
     * @param user who decides
     * @return the submission
     */
    public FormSubmissionDTO updateStatus(String id, FormSubmissionStatus status, String note, String user);

    /**
     * @param id the submission's id
     * @return true
     */
    public boolean remove(String id);
}
