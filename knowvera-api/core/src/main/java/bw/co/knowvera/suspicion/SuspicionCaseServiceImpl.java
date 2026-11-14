package bw.co.knowvera.suspicion;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import bw.co.knowvera.TargetEntity;
import bw.co.knowvera.individual.Individual;
import bw.co.knowvera.individual.IndividualRepository;
import bw.co.knowvera.kyc.KycRecord;
import bw.co.knowvera.kyc.KycRecordRepository;
import bw.co.knowvera.kyc.RelatedParty;
import bw.co.knowvera.organisation.Organisation;
import bw.co.knowvera.organisation.OrganisationRepository;

/**
 * Suspicious transaction cases: raised by staff (or by a confirmed sanctions match), decided by
 * the MLRO and reported to the Financial Intelligence Agency within 5 working days of the
 * suspicion arising (FI Act s.38, Regulations r.22). Cases are confidential: nothing here may
 * reach the customer (tipping off, s.46).
 *
 * @see bw.co.knowvera.suspicion.SuspicionCaseService
 */
@Service("suspicionCaseService")
@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
public class SuspicionCaseServiceImpl
    extends SuspicionCaseServiceBase
{
    /** Working days the FIA allows from suspicion to report (Regulations r.22(1)). */
    static final int REPORTING_WORKING_DAYS = 5;

    private static final DateTimeFormatter NOTE_TIME = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private final KycRecordRepository kycRecordRepository;
    private final IndividualRepository individualRepository;
    private final OrganisationRepository organisationRepository;

    public SuspicionCaseServiceImpl(
        SuspicionCaseRepository suspicionCaseRepository,
        SuspicionCaseMapper suspicionCaseMapper,
        KycRecordRepository kycRecordRepository,
        IndividualRepository individualRepository,
        OrganisationRepository organisationRepository) {

        super(
            suspicionCaseRepository,
            suspicionCaseMapper        );

        this.kycRecordRepository = kycRecordRepository;
        this.individualRepository = individualRepository;
        this.organisationRepository = organisationRepository;
    }

    /**
     * The last day to report a suspicion that arose on {@code suspicionDate}: five working days
     * later. Weekends are skipped; public holidays are not known here, so the deadline may be
     * a day early around them, never late.
     */
    static LocalDate reportDeadline(LocalDate suspicionDate) {

        LocalDate date = suspicionDate;
        int added = 0;
        while (added < REPORTING_WORKING_DAYS) {
            date = date.plusDays(1);
            if (date.getDayOfWeek() != DayOfWeek.SATURDAY && date.getDayOfWeek() != DayOfWeek.SUNDAY) {
                added++;
            }
        }
        return date;
    }

    /**
     * @see bw.co.knowvera.suspicion.SuspicionCaseService#raise(SuspicionCaseDTO, String)
     */
    @Override
    protected SuspicionCaseDTO handleRaise(SuspicionCaseDTO suspicionCase, String user)
        throws Exception
    {
        if (StringUtils.isBlank(suspicionCase.getGrounds())) {
            throw new SuspicionCaseServiceException("State the grounds for the suspicion");
        }

        SuspicionCase entity = SuspicionCase.Factory.newInstance();
        entity.setKycRecordId(StringUtils.trimToNull(suspicionCase.getKycRecordId()));
        entity.setSubjectName(StringUtils.defaultIfBlank(suspicionCase.getSubjectName(), subjectOf(entity.getKycRecordId())));
        if (StringUtils.isBlank(entity.getSubjectName())) {
            throw new SuspicionCaseServiceException("Name the person or entity the suspicion is about");
        }

        entity.setCause(suspicionCase.getCause() == null ? SuspicionTrigger.OTHER : suspicionCase.getCause());
        entity.setGrounds(suspicionCase.getGrounds());
        entity.setStatus(SuspicionCaseStatus.OPEN);
        entity.setSuspicionDate(suspicionCase.getSuspicionDate() == null ? LocalDate.now() : suspicionCase.getSuspicionDate());
        entity.setReportDeadline(reportDeadline(entity.getSuspicionDate()));
        entity.setRef("STR-" + entity.getSuspicionDate().getYear() + "-" + RandomStringUtils.secure().nextAlphanumeric(6).toUpperCase());
        entity.setNotes(new ArrayList<>());
        entity.setCreatedAt(LocalDateTime.now());
        entity.setCreatedBy(user);
        note(entity, user, "Raised: " + entity.getCause());

        return suspicionCaseMapper.toSuspicionCaseDTO(suspicionCaseRepository.save(entity));
    }

    /**
     * @see bw.co.knowvera.suspicion.SuspicionCaseService#findById(String)
     */
    @Override
    protected SuspicionCaseDTO handleFindById(String id)
        throws Exception
    {
        return suspicionCaseMapper.toSuspicionCaseDTO(load(id));
    }

    /**
     * @see bw.co.knowvera.suspicion.SuspicionCaseService#findActive()
     */
    @Override
    protected List<SuspicionCaseDTO> handleFindActive()
        throws Exception
    {
        List<SuspicionCase> cases = new ArrayList<>(suspicionCaseRepository.findAll((root, query, builder) ->
                builder.notEqual(root.get("status"), SuspicionCaseStatus.CLOSED)));
        cases.sort(Comparator.comparing(SuspicionCase::getReportDeadline, Comparator.nullsLast(Comparator.naturalOrder())));
        return suspicionCaseMapper.toSuspicionCaseDTOCollection(cases);
    }

    /**
     * @see bw.co.knowvera.suspicion.SuspicionCaseService#findByRecord(String)
     */
    @Override
    protected List<SuspicionCaseDTO> handleFindByRecord(String kycRecordId)
        throws Exception
    {
        return suspicionCaseMapper.toSuspicionCaseDTOCollection(suspicionCaseRepository.findAll((root, query, builder) ->
                builder.equal(root.get("kycRecordId"), kycRecordId)));
    }

    /**
     * @see bw.co.knowvera.suspicion.SuspicionCaseService#addNote(String, String, String)
     */
    @Override
    protected SuspicionCaseDTO handleAddNote(String id, String note, String user)
        throws Exception
    {
        if (StringUtils.isBlank(note)) {
            throw new SuspicionCaseServiceException("The note is empty");
        }
        SuspicionCase entity = load(id);
        note(entity, user, note);
        return save(entity, user);
    }

    /**
     * @see bw.co.knowvera.suspicion.SuspicionCaseService#escalate(String, String)
     */
    @Override
    protected SuspicionCaseDTO handleEscalate(String id, String user)
        throws Exception
    {
        SuspicionCase entity = load(id);
        if (entity.getStatus() != SuspicionCaseStatus.OPEN) {
            throw new SuspicionCaseServiceException("Only an open case can be escalated");
        }
        entity.setStatus(SuspicionCaseStatus.ESCALATED);
        note(entity, user, "Escalated to the MLRO");
        return save(entity, user);
    }

    /**
     * Records the report filed with the FIA (Form C, through its online portal).
     *
     * @see bw.co.knowvera.suspicion.SuspicionCaseService#recordReport(String, String, String)
     */
    @Override
    protected SuspicionCaseDTO handleRecordReport(String id, String fiaReference, String user)
        throws Exception
    {
        if (StringUtils.isBlank(fiaReference)) {
            throw new SuspicionCaseServiceException("Enter the FIA's reference for the report");
        }
        SuspicionCase entity = load(id);
        if (entity.getStatus() == SuspicionCaseStatus.CLOSED) {
            throw new SuspicionCaseServiceException("The case is closed");
        }
        entity.setStatus(SuspicionCaseStatus.REPORTED);
        entity.setFiaReference(fiaReference.trim());
        entity.setReportedAt(LocalDateTime.now());
        String late = entity.getReportedAt().toLocalDate().isAfter(entity.getReportDeadline())
                ? " (after the deadline of " + entity.getReportDeadline() + ")" : "";
        note(entity, user, "Reported to the FIA, reference " + entity.getFiaReference() + late);
        return save(entity, user);
    }

    /**
     * @see bw.co.knowvera.suspicion.SuspicionCaseService#close(String, String, String)
     */
    @Override
    protected SuspicionCaseDTO handleClose(String id, String outcome, String user)
        throws Exception
    {
        if (StringUtils.isBlank(outcome)) {
            throw new SuspicionCaseServiceException("Record why the case is closed");
        }
        SuspicionCase entity = load(id);
        entity.setStatus(SuspicionCaseStatus.CLOSED);
        entity.setOutcome(outcome);
        note(entity, user, "Closed: " + outcome);
        return save(entity, user);
    }

    /**
     * The content of a suspicious transaction report (Form C) for the MLRO to file on the FIA
     * portal: the subject's identification, the grounds and the case history.
     *
     * @see bw.co.knowvera.suspicion.SuspicionCaseService#draftReport(String)
     */
    @Override
    protected String handleDraftReport(String id)
        throws Exception
    {
        SuspicionCase entity = load(id);
        StringBuilder report = new StringBuilder();

        report.append("SUSPICIOUS TRANSACTION REPORT - DRAFT (Form C, Financial Intelligence Regulations r.21)\n");
        report.append("CONFIDENTIAL: disclosing this report or the suspicion to the subject is an offence (FI Act s.46)\n\n");
        report.append("Case: ").append(entity.getRef()).append('\n');
        report.append("Suspicion arose: ").append(entity.getSuspicionDate()).append('\n');
        report.append("Report due by: ").append(entity.getReportDeadline()).append(" (5 working days, r.22(1))\n");
        report.append("Cause: ").append(entity.getCause()).append("\n\n");

        report.append("SUBJECT\n");
        report.append("Name: ").append(entity.getSubjectName()).append('\n');
        KycRecord record = entity.getKycRecordId() == null ? null
                : kycRecordRepository.findById(UUID.fromString(entity.getKycRecordId())).orElse(null);
        if (record != null) {
            report.append("KYC record: ").append(StringUtils.defaultString(record.getRef())).append('\n');
            subjectDetails(record, report);
            if (record.getRelatedParties() != null && !record.getRelatedParties().isEmpty()) {
                report.append("Related parties:\n");
                for (RelatedParty party : record.getRelatedParties()) {
                    report.append("  - ").append(StringUtils.defaultString(party.getName()))
                            .append(", ").append(party.getRole())
                            .append(party.getOwnershipPercentage() == null ? "" : ", " + party.getOwnershipPercentage() + "%")
                            .append(StringUtils.isBlank(party.getIdentityNo()) ? "" : ", ID " + party.getIdentityNo())
                            .append('\n');
                }
            }
            if (record.getSourceOfFunds() != null && !record.getSourceOfFunds().isEmpty()) {
                report.append("Declared source of funds: ").append(record.getSourceOfFunds()).append('\n');
            }
        }

        report.append("\nGROUNDS FOR SUSPICION\n").append(StringUtils.defaultString(entity.getGrounds())).append("\n\n");
        report.append("CASE HISTORY\n");
        for (String note : entity.getNotes() == null ? List.<String>of() : entity.getNotes()) {
            report.append("  ").append(note).append('\n');
        }
        report.append("\nTo complete on the FIA portal: the transactions concerned (dates, amounts, accounts, parties)");
        report.append(" and the reporting officer's details (FI Act s.43).\n");
        return report.toString();
    }

    private void subjectDetails(KycRecord record, StringBuilder report) {

        if (StringUtils.isBlank(record.getTargetId())) {
            return;
        }
        if (record.getTarget() == TargetEntity.INDIVIDUAL) {
            individualRepository.findById(UUID.fromString(record.getTargetId())).ifPresent(individual -> {
                report.append("Identity: ").append(individual.getIdentityType()).append(' ').append(individual.getIdentityNo()).append('\n');
                report.append("Date of birth: ").append(individual.getDateOfBirth()).append('\n');
                report.append("Nationality: ").append(StringUtils.defaultString(individual.getNationality())).append('\n');
                report.append("Physical address: ").append(StringUtils.defaultString(individual.getPhysicalAddress())).append('\n');
                report.append("Postal address: ").append(StringUtils.defaultString(individual.getPostalAddress())).append('\n');
            });
        } else if (record.getTarget() == TargetEntity.ORGANISATION) {
            organisationRepository.findById(UUID.fromString(record.getTargetId())).ifPresent(organisation -> {
                report.append("Registration: ").append(organisation.getRegistrationNo())
                        .append(" (").append(StringUtils.defaultString(organisation.getCountryOfRegistration())).append(")\n");
                report.append("Trading name: ").append(StringUtils.defaultString(organisation.getTradingName())).append('\n');
                report.append("Tax / VAT number: ").append(StringUtils.defaultString(organisation.getTaxNumber()))
                        .append(" / ").append(StringUtils.defaultString(organisation.getVatNumber())).append('\n');
                report.append("Physical address: ").append(StringUtils.defaultString(organisation.getPhysicalAddress())).append('\n');
            });
        }
    }

    private String subjectOf(String kycRecordId) {

        if (kycRecordId == null) {
            return null;
        }
        KycRecord record = kycRecordRepository.findById(UUID.fromString(kycRecordId)).orElse(null);
        if (record == null || StringUtils.isBlank(record.getTargetId())) {
            return null;
        }
        if (record.getTarget() == TargetEntity.INDIVIDUAL) {
            Individual individual = individualRepository.findById(UUID.fromString(record.getTargetId())).orElse(null);
            return individual == null ? null : StringUtils.normalizeSpace(StringUtils.defaultString(individual.getFirstName())
                    + " " + StringUtils.defaultString(individual.getMiddleName()) + " " + StringUtils.defaultString(individual.getSurname()));
        }
        Organisation organisation = organisationRepository.findById(UUID.fromString(record.getTargetId())).orElse(null);
        return organisation == null ? null : organisation.getName();
    }

    private SuspicionCase load(String id) {
        return suspicionCaseRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new SuspicionCaseServiceException("Suspicion case not found: " + id));
    }

    private SuspicionCaseDTO save(SuspicionCase entity, String user) {
        entity.setModifiedAt(LocalDateTime.now());
        entity.setModifiedBy(user);
        return suspicionCaseMapper.toSuspicionCaseDTO(suspicionCaseRepository.save(entity));
    }

    /** The case history: who did what and when, never edited. */
    private static void note(SuspicionCase entity, String user, String text) {
        if (entity.getNotes() == null) {
            entity.setNotes(new ArrayList<>());
        }
        entity.getNotes().add(LocalDateTime.now().format(NOTE_TIME) + " " + StringUtils.defaultIfBlank(user, "system") + ": " + text);
    }
}
