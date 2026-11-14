package bw.co.knowvera.form;

import jakarta.validation.Valid;
import java.util.List;

/**
 * Custom forms: an organisation designs a form (sections of fields) that its clients have to fill.
 * The answers are handled by {@link bw.co.knowvera.form.submission.FormSubmissionService}.
 */
public interface FormService
{
    /**
     * @param id the form's id
     * @return the form with its sections and fields
     */
    public FormDTO findById(String id);

    /**
     * Creates the form, or updates it when it has an id. The sections and fields of the form are
     * replaced by the ones given: those with an id are updated, those without are added and the
     * stored ones left out are removed. A field that clients have already answered cannot be removed.
     * The order of the sections, and of the fields in a section, is the order in which they are given.
     * A form saved as published is required from the organisation's clients: it is added to the
     * KYC records those clients already have, and to the others when the client starts its KYC.
     * @param form the form with all its sections and fields
     * @return the saved form
     */
    public FormDTO save(@Valid FormDTO form);

    /**
     * Removes a form nobody has been asked to fill; archive it otherwise.
     * @param id the form's id
     * @return true
     */
    public boolean remove(String id);

    /**
     * @return the forms of every organisation
     */
    public List<FormDTO> getAll();

    /**
     * @param organisationId the organisation's id
     * @return the organisation's forms, by name
     */
    public List<FormDTO> findByOrganisation(String organisationId);
}
