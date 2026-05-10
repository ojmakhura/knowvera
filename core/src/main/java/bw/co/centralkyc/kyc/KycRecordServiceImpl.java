// license-header java merge-point
/**
 * This is only generated once! It will never be overwritten.
 * You can (and have to!) safely modify it by hand.
 * TEMPLATE:    SpringServiceImpl.vsl in andromda-spring cartridge
 * MODEL CLASS: AndroMDAModel::backend::bw.co.centralkyc::individual::kyc::KycRecordService
 * STEREOTYPE:  Service
 */
package bw.co.centralkyc.kyc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Strings;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import bw.co.centralkyc.KeyFieldMatchResult;
import bw.co.centralkyc.PropertySearchOrder;
import bw.co.centralkyc.SearchObject;
import bw.co.centralkyc.TargetEntity;
import bw.co.centralkyc.document.DataVerification;
import bw.co.centralkyc.document.DataVerificationStatus;
import bw.co.centralkyc.document.Document;
import bw.co.centralkyc.document.DocumentDTO;
import bw.co.centralkyc.document.DocumentRepository;
import bw.co.centralkyc.document.DocumentVerificationStatus;
import bw.co.centralkyc.document.type.DocumentType;
import bw.co.centralkyc.individual.Individual;
import bw.co.centralkyc.individual.IndividualRepository;
import bw.co.centralkyc.kyc.fields.GroupFieldValue;
import bw.co.centralkyc.kyc.fields.KycReportSection;
import bw.co.centralkyc.kyc.fields.KycReportSectionRepository;
import bw.co.centralkyc.kyc.fields.ValueData;
import bw.co.centralkyc.organisation.Organisation;
import bw.co.centralkyc.organisation.OrganisationRepository;
import bw.co.centralkyc.sequence.SequenceGenerator;
import bw.co.centralkyc.sequence.SequenceGeneratorRepository;
import bw.co.centralkyc.sequence.SequenceGeneratorService;
import bw.co.centralkyc.sequence.SequencePart;
import bw.co.centralkyc.sequence.SequencePartType;
import bw.co.centralkyc.settings.Settings;
import bw.co.centralkyc.settings.SettingsRepository;
import bw.co.centralkyc.settings.kyc.GroupField;
import bw.co.centralkyc.settings.kyc.KycFieldGroup;
import bw.co.centralkyc.user.UserDTO;
import jakarta.validation.Valid;

/**
 * @see bw.co.centralkyc.kyc.KycRecordService
 */
@Service("kycRecordService")
@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
public class KycRecordServiceImpl
        extends KycRecordServiceBase {

    private static final String SEQUENCE_NAME = "KYC_RECORD_REF";

    private final IndividualRepository individualRepository;
    private final OrganisationRepository organisationRepository;
    private final SettingsRepository settingsRepository;
    private final KycRecordMapper kycRecordMapper;
    private final DocumentRepository documentRepository;
    private final SequenceGeneratorRepository sequenceGeneratorRepository;
    private final SequenceGeneratorService sequenceGeneratorService;
    private final KycReportSectionRepository kycReportSectionRepository;

    public KycRecordServiceImpl(KycRecordRepository kycRecordRepository,
            KycRecordMapper kycRecordMapper, MessageSource messageSource,
            SettingsRepository settingsRepository, KycRecordMapper kycRecordMpper,
            IndividualRepository individualRepository, DocumentRepository documentRepository,
            SequenceGeneratorRepository sequenceGeneratorRepository, SequenceGeneratorService sequenceGeneratorService,
            KycReportSectionRepository kycReportSectionRepository, OrganisationRepository organisationRepository) {
        super(kycRecordRepository, kycRecordMapper, messageSource);
        // TODO Auto-generated constructor stub
        this.individualRepository = individualRepository;
        this.settingsRepository = settingsRepository;
        this.organisationRepository = organisationRepository;
        this.kycRecordMapper = kycRecordMpper;
        this.documentRepository = documentRepository;
        this.sequenceGeneratorRepository = sequenceGeneratorRepository;
        this.sequenceGeneratorService = sequenceGeneratorService;
        this.kycReportSectionRepository = kycReportSectionRepository;
    }

    /**
     * @see bw.co.centralkyc.individual.kyc.KycRecordService#findById(String)
     */
    @Override
    protected KycRecordDTO handleFindById(String id)
            throws Exception {

        KycRecord kycRecord = this.kycRecordRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new Exception("KycRecord not found for id: " + id));

        return this.kycRecordMapper.toKycRecordDTO(kycRecord);
    }

    /**
     * @see bw.co.centralkyc.individual.kyc.KycRecordService#save(KycRecordDTO)
     */
    @Override
    protected KycRecordDTO handleSave(KycRecordDTO kycRecord)
            throws Exception {
        Settings settings = this.settingsRepository.findAll().stream().findFirst()
                .orElseThrow(() -> new Exception("Settings not found"));

        int kycDuration = settings.getKycDuration() != null ? settings.getKycDuration() : 2; // Default to 2 years if
                                                                                             // not set

        KycRecord kycRecordEntity = this.kycRecordMapper.kycRecordDTOToEntity(kycRecord);
        kycRecordEntity.setEmploymentRecord(null);

        if (kycRecordEntity.getUploadDate() == null) {
            kycRecordEntity.setUploadDate(LocalDate.now());
        }

        if (kycRecordEntity.getExpiryDate() == null) {

            kycRecordEntity.setExpiryDate(kycRecordEntity.getUploadDate().plusYears(kycDuration));
        }

        this.checkRef(kycRecordEntity);

        kycRecordEntity = this.kycRecordRepository.save(kycRecordEntity);
        updateOwnerStatus(kycRecordEntity);

        return this.kycRecordMapper.toKycRecordDTO(kycRecordEntity);
    }

    /**
     * @see bw.co.centralkyc.individual.kyc.KycRecordService#remove(String)
     */
    @Override
    protected boolean handleRemove(String id)
            throws Exception {

        if (!kycRecordRepository.existsById(UUID.fromString(id))) {

            throw new KycRecordServiceException("KycRecord not found for id: " + id);

        }

        kycRecordRepository.deleteById(UUID.fromString(id));
        return true;

    }

    /**
     * @see bw.co.centralkyc.individual.kyc.KycRecordService#getAll()
     */
    @Override
    protected List<KycRecordListDTO> handleGetAll()
            throws Exception {

        List<KycRecord> kycRecords = this.kycRecordRepository.findAll();
        return this.kycRecordMapper.toKycRecordListDTOCollection(kycRecords);
    }

    private Specification<KycRecord> createSpecification(KycRecordSearchCriteria criteria) {

        Specification<KycRecord> spec = ((root, query, builder) -> builder.conjunction());

        if (criteria.getTarget() != null) {

            spec = (root, query, cb) -> cb.equal(root.get("target"), criteria.getTarget());
        }

        if (criteria.getTargetIds() != null && criteria.getTargetIds().size() > 0) {

            Specification<KycRecord> targetIdSpec = (root, query, cb) -> root.get("targetId")
                    .in(criteria.getTargetIds());

            spec = spec == null ? targetIdSpec : spec.and(targetIdSpec);
        }

        return spec;
    }

    /**
     * @see bw.co.centralkyc.individual.kyc.KycRecordService#search(String)
     */
    @Override
    protected List<KycRecordListDTO> handleSearch(KycRecordSearchCriteria criteria,
            Set<PropertySearchOrder> searchOrders)
            throws Exception {
        Specification<KycRecord> spec = this.createSpecification(criteria);

        Sort sort = Sort.by(Direction.DESC, "createdAt");

        Collection<KycRecord> kycRecords = spec == null ? this.kycRecordRepository.findAll(sort)
                : this.kycRecordRepository.findAll(spec, sort);
        return this.kycRecordMapper.toKycRecordListDTOCollection(kycRecords);
    }

    /**
     * @see bw.co.centralkyc.individual.kyc.KycRecordService#getAll(Integer,
     *      Integer)
     */
    @Override
    protected Page<KycRecordListDTO> handleGetAll(Integer pageNumber, Integer pageSize)
            throws Exception {

        Page<KycRecord> kycRecords = this.kycRecordRepository.findAll(PageRequest.of(pageNumber, pageSize));
        return kycRecords.map(kycRecord -> {
            try {
                return this.kycRecordMapper.toKycRecordListDTO(kycRecord);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    private List<KycRecordListDTO> toKycRecordListDTOCollection(Collection<KycRecord> kycRecords) {

        List<KycRecordListDTO> dtos = new ArrayList<>();

        for (KycRecord record : kycRecords) {

            KycRecordListDTO dto = this.setOwnerInformation(record);

            if (dto != null) {
                dtos.add(dto);
            }
        }

        return dtos;
    }

    private KycRecordListDTO setOwnerInformation(KycRecord kycRecord) {

        if (kycRecord.getTarget() == TargetEntity.INDIVIDUAL) {

            Individual individual = this.individualRepository.findById(UUID.fromString(kycRecord.getTargetId()))
                    .orElse(null);

            if (individual != null) {

                String name = individual.getFirstName() + " " + individual.getSurname();

                return new KycRecordListDTO(kycRecord.getId().toString(), kycRecord.getRef(),
                        individual.getIdentityNo(), name, kycRecord.getKycStatus(), kycRecord.getExpiryDate());
            }
        } else {

            Organisation organisation = this.organisationRepository.findById(UUID.fromString(kycRecord.getTargetId()))
                    .orElse(null);

            if (organisation != null) {

                return new KycRecordListDTO(kycRecord.getId().toString(), kycRecord.getRef(),
                        organisation.getRegistrationNo(), organisation.getName(), kycRecord.getKycStatus(),
                        kycRecord.getExpiryDate());
            }

        }

        return null;
    }

    /**
     * @see bw.co.centralkyc.individual.kyc.KycRecordService#search(String, Integer,
     *      Integer)
     */
    @Override
    protected Page<KycRecordListDTO> handleSearch(SearchObject<KycRecordSearchCriteria> criteria)
            throws Exception {
        Sort sort = Sort.by(Direction.DESC, "createdAt");

        PageRequest pageRequest = PageRequest.of(
                criteria.getPageNumber(),
                criteria.getPageSize(),
                sort);

        Specification<KycRecord> specification = this.createSpecification(criteria.getCriteria());

        Page<KycRecord> kycRecords = specification == null ? this.kycRecordRepository.findAll(pageRequest)
                : this.kycRecordRepository.findAll(specification, pageRequest);

        return kycRecords.map(this::setOwnerInformation);
    }

    /**
     * @see bw.co.centralkyc.individual.kyc.KycRecordService#findByIndividual(String)
     */
    @Override
    protected List<KycRecordListDTO> handleFindByIndividual(String individualId)
            throws Exception {

        Specification<KycRecord> specification = (root, query, cb) -> cb.and(cb.equal(root.get("target"), "INDIVIDUAL"),
                cb.equal(root.get("targetId"), individualId));

        Collection<KycRecord> kycRecords = this.kycRecordRepository.findAll(specification);
        return this.toKycRecordListDTOCollection(kycRecords);
    }

    /**
     * @see bw.co.centralkyc.individual.kyc.KycRecordService#findByIdentityNo(String)
     */
    @Override
    protected List<KycRecordListDTO> handleFindByIdentityNo(String identityNo)
            throws Exception {

        Individual individual = this.individualRepository.findByIdentityNo(identityNo)
                .orElseThrow(() -> new Exception("Individual not found for identityNo: " + identityNo));

        List<KycRecordListDTO> records = this.findByIndividual(individual.getId().toString()).stream()
                .map(record -> {

                    String name = individual.getFirstName() + " " + individual.getSurname();
                    return new KycRecordListDTO(record.id(), record.ref(), individual.getIdentityNo(),
                            name, record.kycStatus(), record.expiryDate());
                }).toList();

        return records;
    }

    @Override
    protected List<KycRecordListDTO> handleFindByOrganisation(String organisationId) throws Exception {

        Specification<KycRecord> specification = (root, query, cb) -> cb
                .and(cb.equal(root.get("target"), "ORGANISATION"), cb.equal(root.get("targetId"), organisationId));

        Collection<KycRecord> kycRecords = this.kycRecordRepository.findAll(specification);
        return this.toKycRecordListDTOCollection(kycRecords);
    }

    @Override
    protected Page<KycRecordListDTO> handleFindByIndividual(String individualId, Integer pageNumber, Integer pageSize)
            throws Exception {

        Specification<KycRecord> specification = (root, query, cb) -> cb.and(cb.equal(root.get("target"), "INDIVIDUAL"),
                cb.equal(root.get("targetId"), individualId));

        Page<KycRecord> kycRecords = this.kycRecordRepository.findAll(specification,
                PageRequest.of(pageNumber, pageSize));
        return kycRecords.map(this::setOwnerInformation);
    }

    @Override
    protected Page<KycRecordListDTO> handleFindByIdentityNo(String identityNo, Integer pageNumber, Integer pageSize)
            throws Exception {

        Individual individual = this.individualRepository.findByIdentityNo(identityNo)
                .orElseThrow(() -> new Exception("Individual not found for identityNo: " + identityNo));

        return this.handleFindByIndividual(individual.getId().toString(), pageNumber, pageSize);
    }

    @Override
    protected Page<KycRecordListDTO> handleFindByOrganisation(String organisationId, Integer pageNumber,
            Integer pageSize) throws Exception {

        Specification<KycRecord> specification = (root, query, cb) -> cb
                .and(cb.equal(root.get("target"), "ORGANISATION"), cb.equal(root.get("targetId"), organisationId));

        Page<KycRecord> kycRecords = this.kycRecordRepository.findAll(specification,
                PageRequest.of(pageNumber, pageSize));
        return kycRecords.map(this::setOwnerInformation);
    }

    @Override
    protected KycRecordDTO handleCreateTargetRecord(String targetId, TargetEntity target, String user)
            throws Exception {

        Settings settings = this.settingsRepository.findAll().stream().findFirst()
                .orElseThrow(() -> new Exception("Settings not found"));

        KycRecord record = KycRecord.Factory.newInstance();
        record.setTarget(target);
        record.setTargetId(targetId);
        record.setCreatedAt(LocalDateTime.now());
        record.setCreatedBy(user);
        record.setUploadDate(LocalDate.now());
        record.setKycStatus(KycComplianceStatus.INCOMPLETE);
        record.setExpiryDate(record.getUploadDate().plusDays(settings.getKycDuration()));

        record = this.kycRecordRepository.save(record);
        updateOwnerStatus(record);

        return this.kycRecordMapper.toKycRecordDTO(record);
    }

    @Override
    protected boolean handleConfirmOwnership(String kycRecordId, UserDTO user) throws Exception {

        if (StringUtils.isBlank(user.getUserId())) {
            return false;
        }

        KycRecord kycRecord = this.kycRecordRepository.findById(UUID.fromString(kycRecordId))
                .orElseThrow(() -> new Exception("KycRecord not found for id: " + kycRecordId));

        if (StringUtils.isBlank(kycRecord.getTargetId()) || kycRecord.getTarget() == null) {
            return false;
        }

        if (kycRecord.getTarget() == TargetEntity.INDIVIDUAL) {

            Individual individual = this.individualRepository.findById(UUID.fromString(kycRecord.getTargetId()))
                    .orElseThrow(() -> new Exception("Individual not found for id: " + kycRecord.getTargetId()));

            if (individual == null || individual.getId() == null) {
                return false;
            }

            return kycRecord.getTargetId().equals(individual.getId().toString());

        } else if (kycRecord.getTarget() == TargetEntity.ORGANISATION) {

            if (StringUtils.isBlank(user.getOrganisationId())) {

                return false;
            }

            Organisation organisation = this.organisationRepository.findById(UUID.fromString(user.getOrganisationId()))
                    .orElseThrow(() -> new Exception("Organisation not found for id: " + user.getOrganisationId()));

            if (organisation == null || organisation.getId() == null) {
                return false;
            }

            return kycRecord.getTargetId().equals(organisation.getId().toString());

        }

        return false;
    }

    @Override
    protected KycRecordDTO handleFindLatestValidForOwner(String ownerId, TargetEntity ownerType, LocalDate today)
            throws Exception {

        KycRecord record = kycRecordRepository.findLatestValidForOwner(ownerId, ownerType, today)
                .orElse(null);

        return record == null ? null : kycRecordMapper.toKycRecordDTO(record);
    }

    private void checkRef(KycRecord kycRecord) {
        if (StringUtils.isBlank(kycRecord.getRef())) {

            SequenceGenerator sequenceGenerator = sequenceGeneratorRepository.findByName(SEQUENCE_NAME).orElse(null);

            if (sequenceGenerator == null) {

                sequenceGenerator = new SequenceGenerator();
                sequenceGenerator.setName(SEQUENCE_NAME);
                sequenceGenerator.setTargetEntity(TargetEntity.KYC_RECORD);

                List<SequencePart> sequenceParts = new ArrayList<>();

                SequencePart counterPart = new SequencePart();
                counterPart.setPosition(0);
                counterPart.setType(SequencePartType.STATIC);
                counterPart.setInitialValue("KR-");
                counterPart.setName(SEQUENCE_NAME + "_PREFIX");
                counterPart.setSequenceGenerator(sequenceGenerator);
                sequenceParts.add(counterPart);

                counterPart = new SequencePart();
                counterPart.setPosition(1);
                counterPart.setType(SequencePartType.YEAR);
                counterPart.setName(SEQUENCE_NAME + "_YEAR");
                counterPart.setInitialValue("");
                counterPart.setSequenceGenerator(sequenceGenerator);
                sequenceParts.add(counterPart);

                counterPart = new SequencePart();
                counterPart.setPosition(2);
                counterPart.setType(SequencePartType.STATIC);
                counterPart.setInitialValue("/");
                counterPart.setName(SEQUENCE_NAME + "_YEAR_SLASH");
                counterPart.setSequenceGenerator(sequenceGenerator);
                sequenceParts.add(counterPart);

                counterPart = new SequencePart();
                counterPart.setPosition(3);
                counterPart.setType(SequencePartType.COUNTER);
                counterPart.setName(SEQUENCE_NAME + "_COUNTER");
                counterPart.setInitialValue("0000000");
                counterPart.setSequenceGenerator(sequenceGenerator);
                sequenceParts.add(counterPart);

                sequenceGenerator.setSequenceParts(sequenceParts);
                sequenceGenerator = sequenceGeneratorRepository.save(sequenceGenerator);
            }

            String nextRef = sequenceGeneratorService.generateNextSequenceValue(SEQUENCE_NAME, true);
            kycRecord.setRef(nextRef);
        }
    }

    @Override
    protected KycRecordDTO handleCreateNew(KycRecordDTO record, String user)
            throws Exception {

        if (StringUtils.isBlank(record.getId())) {
            record.setEmploymentRecord(null);
        }

        KycRecord kycRecord = this.kycRecordMapper.kycRecordDTOToEntity(record);
        Collection<Document> docs = kycRecord.getDocuments();
        kycRecord.setDocuments(new ArrayList<>()); // Detach documents to avoid persistence issues, we'll handle them
                                                   // after saving the KYC record

        if (kycRecord.getUploadDate() == null) {
            kycRecord.setUploadDate(LocalDate.now());
        }

        if (kycRecord.getExpiryDate() == null) {
            Settings settings = this.settingsRepository.findAll().stream().findFirst()
                    .orElseThrow(() -> new Exception("Settings not found"));

            int kycDuration = settings.getKycDuration() != null ? settings.getKycDuration() : 2; // Default to 2 years
                                                                                                 // if
                                                                                                 // not set

            kycRecord.setExpiryDate(kycRecord.getUploadDate().plusYears(kycDuration));
        }

        this.checkRef(kycRecord);

        kycRecord = this.kycRecordRepository.save(kycRecord);
        updateOwnerStatus(kycRecord);
        if (docs != null && docs.size() > 0) {
            for (Document doc : docs) {

                doc.setTargetId(kycRecord.getId().toString());
                doc.setTarget(TargetEntity.KYC_RECORD);
            }
            kycRecord.getDocuments().addAll(documentRepository.saveAll(docs));
        }

        return this.kycRecordMapper.toKycRecordDTO(kycRecord);
    }

    @Override
    protected KycRecordDTO handleRemoveRecordFile(String id, String documentId) throws Exception {

        Document document = documentRepository.findById(UUID.fromString(documentId))
                .orElseThrow(() -> new Exception("Document not found for id: " + documentId));

        if (!TargetEntity.KYC_RECORD.equals(document.getTarget())) {
            throw new KycRecordServiceException(
                    "Document with id: " + documentId + " is not associated with a KYC record");
        }

        KycRecord kycRecord = kycRecordRepository.findById(UUID.fromString(id))
                .orElseThrow(
                        () -> new KycRecordServiceException("KycRecord not found for id: " + document.getTargetId()));

        if (!kycRecord.getId().toString().equals(document.getTargetId())) {
            throw new KycRecordServiceException(
                    "Document with id: " + documentId + " is not associated with KycRecord with id: " + id);
        }

        kycRecord.getDocuments().remove(document);

        return this.kycRecordMapper.toKycRecordDTO(kycRecord);
    }

    @Override
    protected KycRecordDTO handleUpdateRecordFiles(String id, @Valid List<DocumentDTO> documents) throws Exception {

        KycRecord record = kycRecordRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new KycRecordServiceException("KycRecord not found for id: " + id));

        for (DocumentDTO docDto : documents) {

            Document document = documentRepository.findById(UUID.fromString(docDto.getId()))
                    .orElseThrow(() -> new Exception("Document not found for id: " + docDto.getId()));

            if (!TargetEntity.KYC_RECORD.equals(document.getTarget())) {
                throw new KycRecordServiceException(
                        "Document with id: " + docDto.getId() + " is not associated with a KYC record");
            }

            if (!record.getId().toString().equals(document.getTargetId())) {
                throw new KycRecordServiceException(
                        "Document with id: " + docDto.getId() + " is not associated with KycRecord with id: " + id);
            }

            Document exists = record.getDocuments().stream()
                    .filter(doc -> doc.getId().equals(document.getId()))
                    .findFirst()
                    .orElse(null);

            if (exists == null) {

                record.addDocuments(document);
            }
        }

        record = this.kycRecordRepository.save(record);
        updateOwnerStatus(record);

        return this.kycRecordMapper.toKycRecordDTO(record);
    }

    @Override
    protected KycRecordSummary handleFindSummaryById(String id) throws Exception {

        KycRecord kycRecord = this.kycRecordRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new KycRecordServiceException("KycRecord not found for id: " + id));

        KycRecordSummary summary = kycRecordMapper.toKycRecordSummary(kycRecord);

        String name = null;
        String identityNo = null;
        String emailAddress = null;

        if (kycRecord.getTarget() == TargetEntity.INDIVIDUAL) {

            Individual individual = this.individualRepository.findById(UUID.fromString(kycRecord.getTargetId()))
                    .orElseThrow(() -> new KycRecordServiceException(
                            "Individual not found for id: " + kycRecord.getTargetId()));

            name = individual.getFirstName() + " " + individual.getSurname();
            identityNo = individual.getIdentityNo();
            emailAddress = individual.getEmailAddress();

            summary.setIdentityType(individual.getIdentityType());
            summary.setPostalAddress(individual.getPostalAddress());
            summary.setPhysicalAddress(individual.getPhysicalAddress());

            summary.setPhoneNumbers(individual.getPhoneNumbers());

        } else {

            Organisation organisation = this.organisationRepository.findById(UUID.fromString(kycRecord.getTargetId()))
                    .orElseThrow(() -> new KycRecordServiceException(
                            "Organisation not found for id: " + kycRecord.getTargetId()));

            name = organisation.getName();
            identityNo = organisation.getRegistrationNo();
            emailAddress = organisation.getContactEmailAddress();

            summary.setPostalAddress(organisation.getPostalAddress());
            summary.setPhysicalAddress(organisation.getPhysicalAddress());
            summary.setPhoneNumbers(organisation.getPhoneNumbers());
        }

        summary.setName(name);
        summary.setIdentityNo(identityNo);
        summary.setEmailAddress(emailAddress);

        return summary;
    }

    @Override
    protected KycRecordDTO handleRunVerification(String id, String user) throws Exception {

        KycRecord kycRecord = this.kycRecordRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new KycRecordServiceException("KycRecord not found for id: " + id));

        // Run verification logic here, for example:
        // - Check if all required documents are uploaded and verified
        // - Check if the record is expired
        // - Update the KYC status accordingly

        boolean allDocumentsVerified = kycRecord.getDocuments() != null && kycRecord.getDocuments().stream()
                .allMatch(doc -> doc.getVerificationStatus() == DocumentVerificationStatus.REJECTED
                        || doc.getVerificationStatus() == DocumentVerificationStatus.VERIFIED);

        boolean hasRejectedDocuments = kycRecord.getDocuments() != null && kycRecord.getDocuments().stream()
                .anyMatch(doc -> doc.getVerificationStatus() == DocumentVerificationStatus.REJECTED);

        boolean hasManualReview = kycRecord.getDocuments() != null && kycRecord.getDocuments().stream()
                .anyMatch(doc -> doc.getVerificationStatus() == DocumentVerificationStatus.MANUAL_REVIEW);

        if (allDocumentsVerified && kycRecord.getExpiryDate() != null
                && kycRecord.getExpiryDate().isAfter(LocalDate.now())) {

            if (hasRejectedDocuments) {
                kycRecord.setKycStatus(KycComplianceStatus.DOCUMENT_VERIFICATION_FAILED);
            } else {

                if (hasManualReview) {
                    kycRecord.setKycStatus(KycComplianceStatus.INCOMPLETE);
                } else {

                    if (kycRecord.getDataVerificationSummaries().size() > 0) {

                        boolean allValid = kycRecord.getDataVerificationSummaries().stream()
                                .allMatch(summary -> summary.verificationStatus() == DataVerificationStatus.VERIFIED);

                        if (allValid) {
                            kycRecord.setKycStatus(KycComplianceStatus.CURRENT);
                        } else {

                            boolean hasUnverified = kycRecord.getDataVerificationSummaries().stream()
                                    .anyMatch(summary -> summary
                                            .verificationStatus() == DataVerificationStatus.UNVERIFIED);

                            if (hasUnverified) {
                                kycRecord.setKycStatus(KycComplianceStatus.INCOMPLETE);
                            } else {

                                boolean allProcessed = kycRecord.getDataVerificationSummaries().stream()
                                        .allMatch(summary -> summary
                                                .verificationStatus() != DataVerificationStatus.UNVERIFIED);
                                if (allProcessed) {
                                    kycRecord.setKycStatus(KycComplianceStatus.DOCUMENT_VERIFICATION_FAILED);
                                } else {

                                    kycRecord.setKycStatus(KycComplianceStatus.INCOMPLETE);
                                }
                            }
                        }

                    } else {

                        kycRecord.setKycStatus(KycComplianceStatus.INCOMPLETE);
                    }

                }
            }

        } else {

            if (kycRecord.getExpiryDate().isBefore(LocalDate.now())) {
                kycRecord.setKycStatus(KycComplianceStatus.EXPIRED);
            } else {
                kycRecord.setKycStatus(KycComplianceStatus.INCOMPLETE);
            }
        }

        kycRecord.setModifiedAt(LocalDateTime.now());
        kycRecord.setModifiedBy(user);
        updateOwnerStatus(kycRecord);
        kycRecord = this.kycRecordRepository.save(kycRecord);

        return this.generateKycReport(id, user);

    }

    private void updateOwnerStatus(KycRecord kycRecord) {

        if (kycRecord.getTarget() == TargetEntity.INDIVIDUAL) {

            Individual individual = this.individualRepository.findById(UUID.fromString(kycRecord.getTargetId()))
                    .orElse(null);

            if (individual != null) {
                individual.setKycStatus(kycRecord.getKycStatus());
                this.individualRepository.save(individual);
            }
        } else if (kycRecord.getTarget() == TargetEntity.ORGANISATION) {

            Organisation organisation = this.organisationRepository.findById(UUID.fromString(kycRecord.getTargetId()))
                    .orElse(null);

            if (organisation != null) {
                organisation.setKycStatus(kycRecord.getKycStatus());
                this.organisationRepository.save(organisation);
            }
        }
    }

    @Override
    @Transactional
    protected KycRecordDTO handleGenerateKycReport(String id, String user) throws Exception {

        Settings settings = this.settingsRepository.findAll().stream().findFirst()
                .orElseThrow(() -> new Exception("Settings not found"));

        KycRecord kycRecord = this.kycRecordRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new KycRecordServiceException("KycRecord not found for id: " + id));

        // Clean up existing report sections to avoid duplication when regenerating the
        // report
        if (CollectionUtils.isNotEmpty(kycRecord.getKycReportSections())) {
            kycReportSectionRepository.deleteAll(kycRecord.getKycReportSections());
            kycRecord.getKycReportSections().clear();
            kycRecord = this.kycRecordRepository.save(kycRecord);
        }

        kycRecord.setKycReportSections(new ArrayList<>());

        List<KycFieldGroup> kycFieldGroups = kycRecord.getTarget() == TargetEntity.INDIVIDUAL
                ? settings.getIndividualKycFieldGroups()
                : settings.getOrganisationKycFieldGroups();

        List<DocumentVerificationStatus> documentVerificationStatuses = List.of(DocumentVerificationStatus.VERIFIED,
                DocumentVerificationStatus.REJECTED);

        List<Document> completedDocuments = kycRecord.getDocuments().stream()
                .filter(r -> documentVerificationStatuses.contains(r.getVerificationStatus()))
                .toList();

        Map<UUID, Document> documentMap = completedDocuments.stream()
                .collect(Collectors.toMap(doc -> doc.getDocumentType().getId(), doc -> doc));

        // Map<String, KeyFieldMatchResult> matchResultMap = completedDocuments.stream()
        //         .flatMap(doc -> doc.getDataVerifications().stream())
        //         .flatMap(verification -> verification.getKeyFieldMatches().stream())
        //         .collect(Collectors.toMap(match -> match.getKeyField(), match -> match));

        Map<String, KeyFieldMatchResult> matchResultMap = completedDocuments.stream()
                .flatMap(doc -> doc.getDataVerifications().stream())
                .flatMap(verification -> verification.getKeyFieldMatches().stream())
                .collect(Collectors.toMap(
                        KeyFieldMatchResult::getKeyField,
                        match -> match,
                        (existing, replacement) -> {

                            if(StringUtils.isNotBlank(replacement.getExtractedValue())) {

                                existing.setExtractedValue(existing.getExtractedValue() + ", " + replacement.getExtractedValue());
                            }
                            
                            if(StringUtils.isNotBlank(replacement.getExpectedValue())) {

                                existing.setExpectedValue(existing.getExpectedValue() + ", " + replacement.getExpectedValue());
                            }

                            return existing;
                        }));

        for (KycFieldGroup group : kycFieldGroups) {

            KycReportSection section = new KycReportSection();
            section.setLabel(group.getLabel());
            section.setPosition(group.getPosition());

            section.setGroupFieldValues(new ArrayList<>());

            for (GroupField field : group.getGroupFields()) {

                DocumentType documentType = field.getExpectedField().getDocumentType();
                Document document = documentMap.get(documentType.getId());

                if (document == null || document.getDataVerifications() == null
                        || document.getDataVerifications().isEmpty()) {

                    continue;
                }

                if (document != null) {

                    KeyFieldMatchResult matchResult = matchResultMap.get(field.getExpectedField().getField());
                    if (matchResult != null) {
                        GroupFieldValue fieldValue = new GroupFieldValue();
                        fieldValue.setExpectedField(field.getExpectedField());
                        fieldValue.setPosition(field.getPosition());
                        fieldValue.setKycReportSection(section);

                        ValueData data = new ValueData();
                        data.setSimilarity(matchResult.getSimilarity());
                        data.setExpectedValue(matchResult.getExpectedValue());
                        data.setExtractedValue(matchResult.getExtractedValue());
                        data.setMandatory(matchResult.getMandatory());
                        data.setSuccess(matchResult.getSuccess());

                        fieldValue.setData(data);

                        section.getGroupFieldValues().add(fieldValue);
                    }
                }
            }

            section.setKycRecord(kycRecord);
            kycRecord.getKycReportSections().add(section);
        }

        kycRecord.setModifiedAt(LocalDateTime.now());
        kycRecord.setModifiedBy(user);
        kycRecord = this.kycRecordRepository.save(kycRecord);
        updateOwnerStatus(kycRecord);

        return this.kycRecordMapper.toKycRecordDTO(kycRecord);
    }
}
