package bw.co.knowvera.form.submission;

import bw.co.knowvera.form.Form;
import bw.co.knowvera.form.FormRepository;
import bw.co.knowvera.form.FormStatus;
import bw.co.knowvera.kyc.KycRecord;
import bw.co.knowvera.kyc.KycRecordRepository;
import bw.co.knowvera.messaging.FormRequiredNotification;
import bw.co.knowvera.organisation.client.ClientRequest;
import bw.co.knowvera.organisation.client.ClientRequestRepository;
import bw.co.knowvera.organisation.client.ClientRequestStatus;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

/**
 * Which forms a client has to fill as part of its KYC: every published form, meant for that kind
 * of client, of the organisations onboarding it (their client requests that were not rejected).
 * A required form becomes a submission on the client's KYC record, where the client fills it.
 * <p>
 * The two directions of the same rule: a client doing its KYC gets the forms of its organisations
 * ({@link #requireFor}), and a form that is published reaches the clients that already have a
 * KYC record ({@link #requireFrom}).
 */
@Component
public class FormRequirements
{
    private final FormRepository formRepository;
    private final FormSubmissionRepository formSubmissionRepository;
    private final ClientRequestRepository clientRequestRepository;
    private final KycRecordRepository kycRecordRepository;
    private final FormRequiredNotification formRequiredNotification;

    public FormRequirements(
        FormRepository formRepository,
        FormSubmissionRepository formSubmissionRepository,
        ClientRequestRepository clientRequestRepository,
        KycRecordRepository kycRecordRepository,
        FormRequiredNotification formRequiredNotification) {
        this.formRepository = formRepository;
        this.formSubmissionRepository = formSubmissionRepository;
        this.clientRequestRepository = clientRequestRepository;
        this.kycRecordRepository = kycRecordRepository;
        this.formRequiredNotification = formRequiredNotification;
    }

    /**
     * Adds to a KYC record the forms its client has to fill and does not have yet.
     * @param record the KYC record of the client
     * @param user who triggers it
     * @return the number of forms added
     */
    public int requireFor(KycRecord record, String user)
    {
        if (record.getTarget() == null || record.getTargetId() == null)
        {
            return 0;
        }
        String clientId = record.getTargetId().trim().toLowerCase();

        // The organisations onboarding the client; an organisation is not its own client
        Specification<ClientRequest> onboarding = (root, query, builder) -> builder.and(
                builder.equal(root.get("target"), record.getTarget()),
                builder.equal(builder.lower(root.get("targetId")), clientId),
                builder.notEqual(root.get("status"), ClientRequestStatus.REJECTED));
        Set<UUID> organisations = clientRequestRepository.findAll(onboarding).stream()
                .map(request -> request.getOrganisation().getId())
                .filter(organisation -> !organisation.toString().equals(clientId))
                .collect(Collectors.toSet());
        if (organisations.isEmpty())
        {
            return 0;
        }

        int added = 0;
        for (Form form : formRepository.findByOrganisationIdInAndStatusAndTargetType(
                organisations, FormStatus.PUBLISHED, record.getTarget()))
        {
            added += add(form, record, user) != null ? 1 : 0;
        }
        return added;
    }

    /**
     * Adds a published form to the KYC records that the organisation's clients already have, so
     * that those clients are asked to fill it, and notifies them. Clients without a KYC record get
     * it when they start one.
     * @param form the form
     * @param user who triggers it
     * @return the number of KYC records the form was added to
     */
    public int requireFrom(Form form, String user)
    {
        if (form.getStatus() != FormStatus.PUBLISHED)
        {
            return 0;
        }
        String organisationId = form.getOrganisation().getId().toString();

        Specification<ClientRequest> clients = (root, query, builder) -> builder.and(
                builder.equal(root.get("organisation").get("id"), form.getOrganisation().getId()),
                builder.equal(root.get("target"), form.getTargetType()),
                builder.notEqual(root.get("status"), ClientRequestStatus.REJECTED));
        Set<String> clientIds = clientRequestRepository.findAll(clients).stream()
                .map(ClientRequest::getTargetId)
                .filter(id -> id != null && !id.isBlank())
                .map(id -> id.trim().toLowerCase())
                .filter(id -> !id.equals(organisationId))
                .collect(Collectors.toSet());
        if (clientIds.isEmpty())
        {
            return 0;
        }

        Specification<KycRecord> records = (root, query, builder) -> builder.and(
                builder.equal(root.get("target"), form.getTargetType()),
                builder.lower(root.get("targetId")).in(clientIds));
        // These clients are not on their KYC page: they are told that there is a form to fill
        List<UUID> added = new ArrayList<UUID>();
        for (KycRecord record : kycRecordRepository.findAll(records))
        {
            FormSubmission submission = add(form, record, user);
            if (submission != null && submission.getId() != null)
            {
                added.add(submission.getId());
            }
        }
        formRequiredNotification.queue(added);
        return added.size();
    }

    /**
     * A form is asked for once per KYC record, whatever became of the answers.
     * @return the new submission, or null when the record already has the form
     */
    private FormSubmission add(Form form, KycRecord record, String user)
    {
        if (formSubmissionRepository.existsByFormIdAndKycRecordId(form.getId(), record.getId()))
        {
            return null;
        }
        FormSubmission submission = FormSubmission.Factory.newInstance();
        submission.setForm(form);
        submission.setKycRecord(record);
        submission.setStatus(FormSubmissionStatus.NEW);
        submission.setCreatedBy(user);
        submission.setCreatedAt(LocalDateTime.now());
        return formSubmissionRepository.save(submission);
    }
}
