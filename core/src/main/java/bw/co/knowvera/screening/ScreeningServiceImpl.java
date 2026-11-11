// license-header java merge-point
/**
 * This is only generated once! It will never be overwritten.
 * You can (and have to!) safely modify it by hand.
 * TEMPLATE:    SpringServiceImpl.vsl in andromda-spring cartridge
 * MODEL CLASS: AndroMDAModel::backend::bw.co.knowvera::screening::ScreeningService
 * STEREOTYPE:  Service
 */
package bw.co.knowvera.screening;

import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import bw.co.knowvera.QueueObject;
import bw.co.knowvera.TargetEntity;
import bw.co.knowvera.individual.Individual;
import bw.co.knowvera.individual.IndividualRepository;
import bw.co.knowvera.kyc.KycRecord;
import bw.co.knowvera.kyc.KycRecordRepository;
import bw.co.knowvera.kyc.RelatedParty;
import bw.co.knowvera.organisation.Organisation;
import bw.co.knowvera.organisation.OrganisationRepository;
import bw.co.knowvera.properties.RabbitProperties;
import bw.co.knowvera.suspicion.SuspicionCaseDTO;
import bw.co.knowvera.suspicion.SuspicionCaseService;
import bw.co.knowvera.suspicion.SuspicionCaseStatus;
import bw.co.knowvera.suspicion.SuspicionTrigger;

/**
 * Screens KYC subjects (the customer and everyone in its related parties) against the UN
 * Security Council Consolidated List (FI Act s.27). Potential matches wait for a screening
 * analyst; a confirmed match raises a suspicion case for the MLRO. The list is refreshed daily
 * and every open KYC record is screened again against it.
 *
 * @see bw.co.knowvera.screening.ScreeningService
 */
@Service("screeningService")
@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
public class ScreeningServiceImpl
    extends ScreeningServiceBase
{
    private static final Logger log = LoggerFactory.getLogger(ScreeningServiceImpl.class);

    /** The user automated screening runs as (see KycVerificationService). */
    private static final String AUTOMATED_USER = "AI-AGENT";

    private final KycRecordRepository kycRecordRepository;
    private final IndividualRepository individualRepository;
    private final OrganisationRepository organisationRepository;
    private final SuspicionCaseService suspicionCaseService;
    private final RabbitTemplate rabbitTemplate;
    private final RabbitProperties rabbitProperties;
    private final SanctionsMatcher matcher;
    private final UnSanctionsListParser parser;

    @Value("${app.screening.un-list-url:https://scsanctions.un.org/resources/xml/en/consolidated.xml}")
    private String unListUrl;

    @Value("${app.screening.scheduled-update:true}")
    private boolean scheduledUpdate;

    public ScreeningServiceImpl(
        SanctionsListEntryRepository sanctionsListEntryRepository,
        SanctionsListEntryMapper sanctionsListEntryMapper,         ScreeningHitRepository screeningHitRepository,
        ScreeningHitMapper screeningHitMapper,
        KycRecordRepository kycRecordRepository,
        IndividualRepository individualRepository,
        OrganisationRepository organisationRepository,
        @Lazy SuspicionCaseService suspicionCaseService,
        RabbitTemplate rabbitTemplate,
        RabbitProperties rabbitProperties,
        SanctionsMatcher matcher,
        UnSanctionsListParser parser) {

        super(
            sanctionsListEntryRepository,
            sanctionsListEntryMapper,             screeningHitRepository,
            screeningHitMapper        );

        this.kycRecordRepository = kycRecordRepository;
        this.individualRepository = individualRepository;
        this.organisationRepository = organisationRepository;
        this.suspicionCaseService = suspicionCaseService;
        this.rabbitTemplate = rabbitTemplate;
        this.rabbitProperties = rabbitProperties;
        this.matcher = matcher;
        this.parser = parser;
    }

    /** Refreshes the UN list every day and screens open records against it. */
    @Scheduled(cron = "${app.screening.un-list-cron:0 0 3 * * *}")
    public void refreshDaily() {

        if (!scheduledUpdate) {
            return;
        }
        try {
            updateSanctionsList("SCHEDULER");
        } catch (Exception e) {
            log.error("Could not refresh the UN sanctions list from {}", unListUrl, e);
        }
    }

    /**
     * @see bw.co.knowvera.screening.ScreeningService#updateSanctionsList(String)
     */
    @Override
    protected ScreeningListStatus handleUpdateSanctionsList(String user)
        throws Exception
    {
        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(30))
                .followRedirects(HttpClient.Redirect.NORMAL)
                .build();
        HttpResponse<InputStream> response = client.send(
                HttpRequest.newBuilder(URI.create(unListUrl)).timeout(Duration.ofMinutes(2)).GET().build(),
                HttpResponse.BodyHandlers.ofInputStream());

        if (response.statusCode() != 200) {
            throw new ScreeningServiceException("The UN sanctions list could not be downloaded: HTTP " + response.statusCode());
        }

        UnSanctionsListParser.ParsedList list;
        try (InputStream body = response.body()) {
            list = parser.parse(body);
        }

        return importList(list, user);
    }

    /**
     * Replaces the UN entries with the list's, then screens every open KYC record against it.
     * An empty list is refused: it would silently stop all screening.
     */
    ScreeningListStatus importList(UnSanctionsListParser.ParsedList list, String user) {

        if (list.entries().isEmpty()) {
            throw new ScreeningServiceException("The UN sanctions list is empty: keeping the current list");
        }

        List<SanctionsListEntry> current = sanctionsListEntryRepository.findAll((root, query, builder) ->
                builder.equal(root.get("source"), ScreeningListSource.UN_SECURITY_COUNCIL));
        sanctionsListEntryRepository.deleteAll(current);
        sanctionsListEntryRepository.flush();

        LocalDateTime now = LocalDateTime.now();
        for (SanctionsListEntry entry : list.entries()) {
            entry.setCreatedAt(now);
            entry.setCreatedBy(user);
        }
        sanctionsListEntryRepository.saveAll(list.entries());
        log.info("Imported UN sanctions list {}: {} entries", list.version(), list.entries().size());

        int rescreened = 0;
        for (KycRecord record : kycRecordRepository.findAll((root, query, builder) -> builder.isNull(root.get("closedAt")))) {
            if (!screen(record).isEmpty()) {
                requestVerification(record);
                rescreened++;
            }
        }
        log.info("Rescreened open KYC records: {} with new potential matches", rescreened);

        return status(ScreeningListSource.UN_SECURITY_COUNCIL);
    }

    /**
     * @see bw.co.knowvera.screening.ScreeningService#getListStatus()
     */
    @Override
    protected List<ScreeningListStatus> handleGetListStatus()
        throws Exception
    {
        List<ScreeningListStatus> statuses = new ArrayList<>();
        for (ScreeningListSource source : ScreeningListSource.values()) {
            statuses.add(status(source));
        }
        return statuses;
    }

    private ScreeningListStatus status(ScreeningListSource source) {

        List<SanctionsListEntry> entries = sanctionsListEntryRepository.findAll((root, query, builder) ->
                builder.equal(root.get("source"), source));

        ScreeningListStatus status = new ScreeningListStatus();
        status.setSource(source);
        status.setEntryCount(entries.size());
        entries.stream().map(SanctionsListEntry::getListVersion).filter(StringUtils::isNotBlank)
                .max(Comparator.naturalOrder()).ifPresent(status::setListVersion);
        entries.stream().map(SanctionsListEntry::getCreatedAt).filter(java.util.Objects::nonNull)
                .max(Comparator.naturalOrder()).ifPresent(status::setImportedAt);
        return status;
    }

    /**
     * @see bw.co.knowvera.screening.ScreeningService#screenRecord(String)
     */
    @Override
    protected List<ScreeningHitDTO> handleScreenRecord(String kycRecordId)
        throws Exception
    {
        KycRecord record = kycRecordRepository.findById(UUID.fromString(kycRecordId))
                .orElseThrow(() -> new ScreeningServiceException("KYC record not found: " + kycRecordId));
        screen(record);
        return handleFindHitsByRecord(kycRecordId);
    }

    /**
     * Screens the record's customer and related parties; saves and returns the new potential
     * matches. A match already found for the same person and list entry is not raised again,
     * whatever was decided on it.
     */
    List<ScreeningHit> screen(KycRecord record) {

        if (record.getId() == null) {
            return List.of();
        }

        List<SanctionsListEntry> entries = sanctionsListEntryRepository.findAll();
        if (entries.isEmpty()) {
            log.warn("No sanctions list loaded: KYC record {} was not screened", record.getId());
            return List.of();
        }

        String recordId = record.getId().toString();
        Set<String> known = new HashSet<>();
        for (ScreeningHit hit : hitsOf(recordId)) {
            known.add(key(hit.getSubjectName(), hit.getEntryReference()));
        }

        List<ScreeningHit> created = new ArrayList<>();
        for (SanctionsMatcher.Subject subject : subjects(record)) {
            for (SanctionsMatcher.Match match : matcher.match(subject, entries)) {
                SanctionsListEntry entry = match.entry();
                if (!known.add(key(subject.name(), entry.getReference()))) {
                    continue;
                }

                ScreeningHit hit = ScreeningHit.Factory.newInstance();
                hit.setKycRecordId(recordId);
                hit.setSubjectName(subject.name());
                hit.setSubjectRole(subject.role());
                hit.setSource(entry.getSource());
                hit.setEntryReference(entry.getReference());
                hit.setEntryName(match.matchedName());
                hit.setScore(match.score());
                hit.setStatus(ScreeningHitStatus.PENDING);
                hit.setListVersion(entry.getListVersion());
                hit.setCreatedAt(LocalDateTime.now());
                hit.setCreatedBy(AUTOMATED_USER);
                created.add(hit);
            }
        }

        if (!created.isEmpty()) {
            screeningHitRepository.saveAll(created);
            log.info("KYC record {}: {} potential sanctions matches", recordId, created.size());
        }
        return created;
    }

    private List<SanctionsMatcher.Subject> subjects(KycRecord record) {

        List<SanctionsMatcher.Subject> subjects = new ArrayList<>();

        if (StringUtils.isNotBlank(record.getTargetId())) {
            if (record.getTarget() == TargetEntity.INDIVIDUAL) {
                Individual individual = individualRepository.findById(UUID.fromString(record.getTargetId())).orElse(null);
                if (individual != null) {
                    String name = String.join(" ", nonBlank(individual.getFirstName(), individual.getMiddleName(), individual.getSurname()));
                    Integer year = individual.getDateOfBirth() == null ? null : individual.getDateOfBirth().getYear();
                    subjects.add(new SanctionsMatcher.Subject(name, ScreeningSubjectType.INDIVIDUAL, year, "Customer"));
                }
            } else if (record.getTarget() == TargetEntity.ORGANISATION) {
                Organisation organisation = organisationRepository.findById(UUID.fromString(record.getTargetId())).orElse(null);
                if (organisation != null) {
                    subjects.add(new SanctionsMatcher.Subject(organisation.getName(), ScreeningSubjectType.ENTITY, null, "Customer"));
                    if (StringUtils.isNotBlank(organisation.getTradingName())) {
                        subjects.add(new SanctionsMatcher.Subject(organisation.getTradingName(), ScreeningSubjectType.ENTITY, null,
                                "Customer (trading name)"));
                    }
                }
            }
        }

        if (record.getRelatedParties() != null) {
            for (RelatedParty party : record.getRelatedParties()) {
                if (StringUtils.isNotBlank(party.getName())) {
                    String role = party.getRole() == null ? "Related party" : party.getRole().name();
                    subjects.add(new SanctionsMatcher.Subject(party.getName(), ScreeningSubjectType.INDIVIDUAL, null, role));
                }
            }
        }

        subjects.removeIf(subject -> StringUtils.isBlank(subject.name()));
        return subjects;
    }

    /**
     * @see bw.co.knowvera.screening.ScreeningService#findHitsByRecord(String)
     */
    @Override
    protected List<ScreeningHitDTO> handleFindHitsByRecord(String kycRecordId)
        throws Exception
    {
        return screeningHitMapper.toScreeningHitDTOCollection(hitsOf(kycRecordId));
    }

    private List<ScreeningHit> hitsOf(String kycRecordId) {
        return screeningHitRepository.findAll((root, query, builder) -> builder.equal(root.get("kycRecordId"), kycRecordId));
    }

    /**
     * @see bw.co.knowvera.screening.ScreeningService#findPendingHits()
     */
    @Override
    protected List<ScreeningHitDTO> handleFindPendingHits()
        throws Exception
    {
        List<ScreeningHit> hits = new ArrayList<>(screeningHitRepository.findAll((root, query, builder) ->
                builder.equal(root.get("status"), ScreeningHitStatus.PENDING)));
        hits.sort(Comparator.comparing(ScreeningHit::getScore, Comparator.nullsLast(Comparator.reverseOrder())));
        return screeningHitMapper.toScreeningHitDTOCollection(hits);
    }

    /**
     * Records a screening analyst's decision. A confirmed match raises a suspicion case for
     * the MLRO (no business relationship is permitted, FI Act s.27; report it, s.38). Either
     * way the KYC record is verified again so its risk and status follow the decision.
     *
     * @see bw.co.knowvera.screening.ScreeningService#reviewHit(String, ScreeningHitStatus, String, String)
     */
    @Override
    protected ScreeningHitDTO handleReviewHit(String id, ScreeningHitStatus status, String notes, String user)
        throws Exception
    {
        if (status == ScreeningHitStatus.PENDING) {
            throw new ScreeningServiceException("Decide whether the match is confirmed or a false positive");
        }
        if (StringUtils.isBlank(notes)) {
            throw new ScreeningServiceException("Record the reasons for the decision");
        }

        ScreeningHit hit = screeningHitRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new ScreeningServiceException("Screening hit not found: " + id));

        hit.setStatus(status);
        hit.setReviewNotes(notes);
        hit.setReviewedBy(user);
        hit.setReviewedAt(LocalDateTime.now());
        hit.setModifiedBy(user);
        hit.setModifiedAt(hit.getReviewedAt());
        hit = screeningHitRepository.save(hit);

        KycRecord record = kycRecordRepository.findById(UUID.fromString(hit.getKycRecordId())).orElse(null);

        if (status == ScreeningHitStatus.CONFIRMED) {
            raiseCase(hit, record, user);
        }
        if (record != null) {
            requestVerification(record);
        }

        return screeningHitMapper.toScreeningHitDTO(hit);
    }

    private void raiseCase(ScreeningHit hit, KycRecord record, String user) throws Exception {

        boolean alreadyRaised = suspicionCaseService.findByRecord(hit.getKycRecordId()).stream()
                .anyMatch(c -> c.getCause() == SuspicionTrigger.SANCTIONS_MATCH && c.getStatus() != SuspicionCaseStatus.CLOSED);
        if (alreadyRaised) {
            suspicionCaseService.findByRecord(hit.getKycRecordId()).stream()
                    .filter(c -> c.getCause() == SuspicionTrigger.SANCTIONS_MATCH && c.getStatus() != SuspicionCaseStatus.CLOSED)
                    .findFirst()
                    .ifPresent(c -> addNote(c.getId(), "Confirmed sanctions match: " + describe(hit), user));
            return;
        }

        SuspicionCaseDTO suspicion = new SuspicionCaseDTO();
        suspicion.setKycRecordId(hit.getKycRecordId());
        suspicion.setSubjectName(hit.getSubjectName());
        suspicion.setCause(SuspicionTrigger.SANCTIONS_MATCH);
        suspicion.setSuspicionDate(LocalDate.now());
        suspicion.setGrounds("Confirmed sanctions list match: " + describe(hit) + "\nAnalyst's reasons: " + hit.getReviewNotes());
        suspicionCaseService.raise(suspicion, user);
    }

    private void addNote(String caseId, String note, String user) {
        try {
            suspicionCaseService.addNote(caseId, note, user);
        } catch (Exception e) {
            log.error("Could not add a note to suspicion case {}", caseId, e);
        }
    }

    private static String describe(ScreeningHit hit) {
        return String.format(Locale.ROOT, "%s (%s) matches %s %s \"%s\", score %.2f", hit.getSubjectName(), hit.getSubjectRole(),
                hit.getSource(), hit.getEntryReference(), hit.getEntryName(), hit.getScore() == null ? 0 : hit.getScore());
    }

    /**
     * Verifies the record again (KycVerificationService), which reassesses its risk and status.
     * Sent once the transaction commits, so the verification sees the hits saved here.
     */
    private void requestVerification(KycRecord record) {

        QueueObject message = new QueueObject(record.getId().toString(), TargetEntity.KYC_RECORD, record.getId().toString());
        Runnable send = () -> rabbitTemplate.convertAndSend(
                rabbitProperties.getKycVerificationQueueExchange(),
                rabbitProperties.getKycVerificationQueueRoutingKey(),
                message);

        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    send.run();
                }
            });
        } else {
            send.run();
        }
    }

    private static String key(String name, String reference) {
        return String.join("|", SanctionsMatcher.words(name)) + "#" + reference;
    }

    private static List<String> nonBlank(String... parts) {
        List<String> result = new ArrayList<>();
        for (String part : parts) {
            if (StringUtils.isNotBlank(part)) {
                result.add(part.trim());
            }
        }
        return result;
    }
}
