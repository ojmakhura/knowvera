// license-header java merge-point
/**
 * This is only generated once! It will never be overwritten.
 * You can (and have to!) safely modify it by hand.
 * TEMPLATE:    SpringServiceImpl.vsl in andromda-spring cartridge
 * MODEL CLASS: AndroMDAModel::backend::bw.co.knowvera::settings::kyc::KycFieldGroupService
 * STEREOTYPE:  Service
 */
package bw.co.knowvera.settings.kyc;

import bw.co.knowvera.settings.Settings;
import bw.co.knowvera.settings.SettingsRepository;
import bw.co.knowvera.settings.kyc.GroupField;
import bw.co.knowvera.settings.kyc.GroupFieldRepository;
import bw.co.knowvera.settings.kyc.KycFieldGroup;
import bw.co.knowvera.settings.kyc.KycFieldGroupDTO;
import bw.co.knowvera.settings.kyc.KycFieldGroupRepository;
import bw.co.knowvera.settings.kyc.KycFieldGroupServiceBase;
import bw.co.knowvera.settings.kyc.KycFieldGroupServiceException;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import bw.co.knowvera.TargetEntity;

/**
 * @see bw.co.knowvera.settings.kyc.KycFieldGroupService
 */
@Service("kycFieldGroupService")
@Validated
public class KycFieldGroupServiceImpl
        extends KycFieldGroupServiceBase {

    private final GroupFieldRepository groupFieldRepository;
    private final SettingsRepository settingsRepository;

    public KycFieldGroupServiceImpl(
            KycFieldGroupRepository kycFieldGroupRepository,
            KycFieldGroupMapper kycFieldGroupMapper,
            GroupFieldRepository groupFieldRepository,
            SettingsRepository settingsRepository) {

        super(
                kycFieldGroupRepository,
                kycFieldGroupMapper);

        this.groupFieldRepository = groupFieldRepository;
        this.settingsRepository = settingsRepository;
    }

    /**
     * @see bw.co.knowvera.settings.kyc.KycFieldGroupService#findById(String)
     */
    @Override
    protected KycFieldGroupDTO handleFindById(String id)
            throws Exception {

        KycFieldGroup kycFieldGroup = this.kycFieldGroupRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new KycFieldGroupServiceException("KycFieldGroup not found for id: " + id));

        return this.kycFieldGroupMapper.toKycFieldGroupDTO(kycFieldGroup);
    }

    /**
     * @see bw.co.knowvera.settings.kyc.KycFieldGroupService#save(@Valid
     *      KycFieldGroupDTO)
     */
    @Override
    protected KycFieldGroupDTO handleSave(@Valid KycFieldGroupDTO fieldGroup)
            throws Exception {

        KycFieldGroup kycFieldGroup = kycFieldGroupMapper.kycFieldGroupDTOToEntity(fieldGroup);

        boolean isNew = kycFieldGroup.getId() == null;

        if (kycFieldGroup.getGroupFields() != null) {

            for (GroupField field : kycFieldGroup.getGroupFields()) {
                field.setKycFieldGroup(kycFieldGroup);
            }

        }

        Settings settings = settingsRepository.findAll().get(0);

        if (isNew) {

            if(kycFieldGroup.getTargetType() == TargetEntity.INDIVIDUAL) {

                settings.getIndividualKycFieldGroups().add(kycFieldGroup);
                kycFieldGroup.setIndividualSettings(settings);

            } else if(kycFieldGroup.getTargetType() == TargetEntity.ORGANISATION) {

                settings.getOrganisationKycFieldGroups().add(kycFieldGroup);
                kycFieldGroup.setOrganisationSettings(settings);
            }

//            settings = settingsRepository.save(settings);
            kycFieldGroup = this.kycFieldGroupRepository.save(kycFieldGroup);

        } else {

            kycFieldGroup = this.kycFieldGroupRepository.save(kycFieldGroup);
        }


        return this.kycFieldGroupMapper.toKycFieldGroupDTO(kycFieldGroup);
    }

    /**
     * @see bw.co.knowvera.settings.kyc.KycFieldGroupService#remove(String)
     */
    @Override
    protected boolean handleRemove(String id)
            throws Exception {

        if (!this.kycFieldGroupRepository.existsById(UUID.fromString(id))) {
            throw new KycFieldGroupServiceException(
                    "KycFieldGroup not found for id: " + id);
        }

        this.kycFieldGroupRepository.deleteById(UUID.fromString(id));
        return true;
    }

    @Override
    protected List<KycFieldGroupDTO> handleFindByTarget(TargetEntity targetType) throws Exception {

        Specification<KycFieldGroup> specification = (root, query, criteriaBuilder) -> criteriaBuilder
                .equal(root.get("targetType"), targetType);

        List<KycFieldGroup> kycFieldGroups = this.kycFieldGroupRepository.findAll(specification);

        return this.kycFieldGroupMapper.toKycFieldGroupDTOCollection(kycFieldGroups);
    }

    @Override
    protected KycFieldGroupDTO handleRemoveField(String id, String fieldId) throws Exception {

        groupFieldRepository.deleteById(UUID.fromString(fieldId));;

        KycFieldGroup kycFieldGroup = this.kycFieldGroupRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new KycFieldGroupServiceException(
                        "KycFieldGroup not found for id: " + id));

        return this.kycFieldGroupMapper.toKycFieldGroupDTO(kycFieldGroup);
    }
}