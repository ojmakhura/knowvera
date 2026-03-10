// license-header java merge-point
/**
 * This is only generated once! It will never be overwritten.
 * You can (and have to!) safely modify it by hand.
 * TEMPLATE:    SpringServiceImpl.vsl in andromda-spring cartridge
 * MODEL CLASS: AndroMDAModel::backend::bw.co.centralkyc::individual::kyc::KycRecordService
 * STEREOTYPE:  Service
 */
package bw.co.centralkyc.kyc;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;

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

import bw.co.centralkyc.SearchObject;
import bw.co.centralkyc.TargetEntity;
import bw.co.centralkyc.document.Document;
import bw.co.centralkyc.document.DocumentDTO;
import bw.co.centralkyc.document.DocumentRepository;
import bw.co.centralkyc.individual.Individual;
import bw.co.centralkyc.individual.IndividualRepository;
import bw.co.centralkyc.kyc.verification.KycVerification;
import bw.co.centralkyc.kyc.verification.VerificationStatus;
import bw.co.centralkyc.organisation.Organisation;
import bw.co.centralkyc.organisation.OrganisationRepository;
import bw.co.centralkyc.settings.Settings;
import bw.co.centralkyc.settings.SettingsRepository;
import bw.co.centralkyc.user.UserDTO;

/**
 * @see bw.co.centralkyc.kyc.KycRecordService
 */
@Service("kycRecordService")
@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
public class KycRecordServiceImpl
        extends KycRecordServiceBase {

    private final IndividualRepository individualRepository;
    private final OrganisationRepository organisationRepository;
    private final SettingsRepository settingsRepository;
    private final KycRecordMapper kycRecordMapper;
    private final DocumentRepository documentRepository;

    public KycRecordServiceImpl(KycRecordDao kycRecordDao, KycRecordRepository kycRecordRepository,
            KycRecordMapper kycRecordMapper, MessageSource messageSource,
            SettingsRepository settingsRepository, KycRecordMapper kycRecordMpper,
            IndividualRepository individualRepository, DocumentRepository documentRepository,
            OrganisationRepository organisationRepository) {
        super(kycRecordDao, kycRecordRepository, kycRecordMapper, messageSource);
        // TODO Auto-generated constructor stub
        this.individualRepository = individualRepository;
        this.settingsRepository = settingsRepository;
        this.organisationRepository = organisationRepository;
        this.kycRecordMapper = kycRecordMpper;
        this.documentRepository = documentRepository;
    }


    /**
     * @see bw.co.centralkyc.individual.kyc.KycRecordService#findById(String)
     */
    @Override
    protected KycRecordDTO handleFindById(String id)
            throws Exception {

        KycRecord kycRecord = this.kycRecordRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new Exception("KycRecord not found for id: " + id));

        return this.kycRecordDao.toKycRecordDTO(kycRecord);
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

        KycRecord kycRecordEntity = this.kycRecordDao.kycRecordDTOToEntity(kycRecord);

        if (kycRecordEntity.getUploadDate() == null) {
            kycRecordEntity.setUploadDate(LocalDate.now());
        }

        if (kycRecordEntity.getExpiryDate() == null) {

            kycRecordEntity.setExpiryDate(kycRecordEntity.getUploadDate().plusYears(kycDuration));
        }

        if(kycRecordEntity.getId() == null) {

            KycVerification verification = KycVerification.Factory.newInstance();
            verification.setCreatedAt(kycRecordEntity.getCreatedAt());
            verification.setCreatedBy(kycRecordEntity.getCreatedBy());
            verification.setKycRecord(kycRecordEntity);
            verification.setEmploymentVerification(VerificationStatus.UNVERIFIED);
            verification.setPepStatusVerification(VerificationStatus.UNVERIFIED);
            verification.setSanctionsDetailsVerification(VerificationStatus.UNVERIFIED);
            verification.setSanctionsMatchVerification(VerificationStatus.UNVERIFIED);

            kycRecordEntity.setKycVerification(verification);
        }

        kycRecordEntity = this.kycRecordRepository.save(kycRecordEntity);

        return this.kycRecordDao.toKycRecordDTO(kycRecordEntity);
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
    protected Collection<KycRecordDTO> handleGetAll()
            throws Exception {

        Collection<KycRecord> kycRecords = this.kycRecordRepository.findAll();
        return this.kycRecordDao.toKycRecordDTOCollection(kycRecords);
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
    protected Collection<KycRecordDTO> handleSearch(KycRecordSearchCriteria criteria)
            throws Exception {
        Specification<KycRecord> spec = this.createSpecification(criteria);

        Sort sort = Sort.by(Direction.DESC, "createdAt");

        Collection<KycRecord> kycRecords = spec == null ? this.kycRecordRepository.findAll(sort)
                : this.kycRecordRepository.findAll(spec, sort);
        return this.kycRecordDao.toKycRecordDTOCollection(kycRecords);
    }

    /**
     * @see bw.co.centralkyc.individual.kyc.KycRecordService#getAll(Integer,
     *      Integer)
     */
    @Override
    protected Page<KycRecordDTO> handleGetAll(Integer pageNumber, Integer pageSize)
            throws Exception {

        Page<KycRecord> kycRecords = this.kycRecordRepository.findAll(PageRequest.of(pageNumber, pageSize));
        return kycRecords.map(kycRecord -> {
            try {
                return this.kycRecordDao.toKycRecordDTO(kycRecord);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * @see bw.co.centralkyc.individual.kyc.KycRecordService#search(String, Integer,
     *      Integer)
     */
    @Override
    protected Page<KycRecordDTO> handleSearch(SearchObject<KycRecordSearchCriteria> criteria)
            throws Exception {
        Sort sort = Sort.by(Direction.DESC, "createdAt");

        PageRequest pageRequest = PageRequest.of(
                criteria.getPageNumber(),
                criteria.getPageSize(),
                sort);

        Specification<KycRecord> specification = this.createSpecification(criteria.getCriteria());

        Page<KycRecord> kycRecords = specification == null ? this.kycRecordRepository.findAll(pageRequest)
                : this.kycRecordRepository.findAll(specification, pageRequest);

        return kycRecords.map(kycRecordDao::toKycRecordDTO);
    }

    /**
     * @see bw.co.centralkyc.individual.kyc.KycRecordService#findByIndividual(String)
     */
    @Override
    protected Collection<KycRecordDTO> handleFindByIndividual(String individualId)
            throws Exception {

        Specification<KycRecord> specification = (root, query, cb) -> cb.and(cb.equal(root.get("target"), "INDIVIDUAL"),
                cb.equal(root.get("targetId"), individualId));

        Collection<KycRecord> kycRecords = this.kycRecordRepository.findAll(specification);
        return this.kycRecordDao.toKycRecordDTOCollection(kycRecords);
    }

    /**
     * @see bw.co.centralkyc.individual.kyc.KycRecordService#findByIdentityNo(String)
     */
    @Override
    protected Collection<KycRecordDTO> handleFindByIdentityNo(String identityNo)
            throws Exception {

        Individual individual = this.individualRepository.findByIdentityNo(identityNo)
                .orElseThrow(() -> new Exception("Individual not found for identityNo: " + identityNo));

        return this.findByIndividual(individual.getId().toString());
    }

    @Override
    protected Collection<KycRecordDTO> handleFindByOrganisation(String organisationId) throws Exception {

        Specification<KycRecord> specification = (root, query, cb) -> cb
                .and(cb.equal(root.get("target"), "ORGANISATION"), cb.equal(root.get("targetId"), organisationId));

        Collection<KycRecord> kycRecords = this.kycRecordRepository.findAll(specification);
        return this.kycRecordDao.toKycRecordDTOCollection(kycRecords);
    }

    @Override
    protected Page<KycRecordDTO> handleFindByIndividual(String individualId, Integer pageNumber, Integer pageSize)
            throws Exception {

        Specification<KycRecord> specification = (root, query, cb) -> cb.and(cb.equal(root.get("target"), "INDIVIDUAL"),
                cb.equal(root.get("targetId"), individualId));

        Page<KycRecord> kycRecords = this.kycRecordRepository.findAll(specification,
                PageRequest.of(pageNumber, pageSize));
        return kycRecords.map(kycRecord -> {
            try {
                return this.kycRecordDao.toKycRecordDTO(kycRecord);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    protected Page<KycRecordDTO> handleFindByIdentityNo(String identityNo, Integer pageNumber, Integer pageSize)
            throws Exception {

        Individual individual = this.individualRepository.findByIdentityNo(identityNo)
                .orElseThrow(() -> new Exception("Individual not found for identityNo: " + identityNo));

        return this.handleFindByIndividual(individual.getId().toString(), pageNumber, pageSize);
    }

    @Override
    protected Page<KycRecordDTO> handleFindByOrganisation(String organisationId, Integer pageNumber,
            Integer pageSize) throws Exception {

        Specification<KycRecord> specification = (root, query, cb) -> cb
                .and(cb.equal(root.get("target"), "ORGANISATION"), cb.equal(root.get("targetId"), organisationId));

        Page<KycRecord> kycRecords = this.kycRecordRepository.findAll(specification,
                PageRequest.of(pageNumber, pageSize));
        return kycRecords.map(kycRecord -> {
            try {
                return this.kycRecordDao.toKycRecordDTO(kycRecord);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
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

        return this.kycRecordDao.toKycRecordDTO(record);
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

            return Strings.CS.equals(individual.getUserId(), user.getUserId());

        } else if (kycRecord.getTarget() == TargetEntity.ORGANISATION) {

            if (StringUtils.isBlank(user.getOrganisationId())) {

                return false;
            }

            Organisation organisation = this.organisationRepository.findById(UUID.fromString(kycRecord.getTargetId()))
                    .orElseThrow(() -> new Exception("Organisation not found for id: " + kycRecord.getTargetId()));

            return Strings.CS.equals(organisation.getId().toString(), user.getOrganisationId());

        }

        return false;
    }

    @Override
    protected KycRecordDTO handleFindLatestValidForOwner(String ownerId, TargetEntity ownerType, LocalDate today)
            throws Exception {

        KycRecord record = kycRecordRepository.findLatestValidForOwner(ownerId, ownerType, today)
                .orElse(null);

        return record == null ? null : kycRecordDao.toKycRecordDTO(record);
    }


    @Override
    protected KycRecordDTO handleCreateNew(KycRecordDTO record, String user)
            throws Exception {

        if(StringUtils.isBlank(record.getId())) {
            record.setEmploymentRecord(null);
        }

        KycRecord kycRecord = this.kycRecordMapper.kycRecordDTOToEntity(record);
        Collection<Document> docs = kycRecord.getDocuments();
        kycRecord.getDocuments().clear(); // Detach documents to avoid persistence issues, we'll handle them after saving the KYC record

        if (kycRecord.getUploadDate() == null) {
            kycRecord.setUploadDate(LocalDate.now());
        }

        kycRecord = this.kycRecordRepository.save(kycRecord);
        if (docs != null && docs.size() > 0) {
           for (Document doc : docs) {
                
                doc.setTargetId(kycRecord.getId().toString());
                doc.setTarget(TargetEntity.KYC_RECORD);
            }
            kycRecord.getDocuments().addAll(documentRepository.saveAll(docs));
        }

        return this.kycRecordMapper.toKycRecordDTO(kycRecord);
    }

}