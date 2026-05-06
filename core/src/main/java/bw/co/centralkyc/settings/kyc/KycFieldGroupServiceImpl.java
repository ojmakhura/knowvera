// license-header java merge-point
/**
 * This is only generated once! It will never be overwritten.
 * You can (and have to!) safely modify it by hand.
 * TEMPLATE:    SpringServiceImpl.vsl in andromda-spring cartridge
 * MODEL CLASS: AndroMDAModel::backend::bw.co.centralkyc::settings::kyc::KycFieldGroupService
 * STEREOTYPE:  Service
 */
package bw.co.centralkyc.settings.kyc;

import bw.co.centralkyc.settings.Settings;
import bw.co.centralkyc.settings.SettingsRepository;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import bw.co.centralkyc.TargetEntity;

/**
 * @see bw.co.centralkyc.settings.kyc.KycFieldGroupService
 */
@Service("kycFieldGroupService")
@Validated
public class KycFieldGroupServiceImpl
        extends KycFieldGroupServiceBase {

    private final GroupFieldRepository groupFieldRepository;
    private final SettingsRepository settingsRepository;

    public KycFieldGroupServiceImpl(
            KycFieldGroupDao kycFieldGroupDao,
            KycFieldGroupRepository kycFieldGroupRepository,
            KycFieldGroupMapper kycFieldGroupMapper,
            GroupFieldRepository groupFieldRepository,
            SettingsRepository settingsRepository,
            MessageSource messageSource) {

        super(
                kycFieldGroupDao,
                kycFieldGroupRepository,
                kycFieldGroupMapper,
                messageSource);

        this.groupFieldRepository = groupFieldRepository;
        this.settingsRepository = settingsRepository;
    }

    /**
     * @see bw.co.centralkyc.settings.kyc.KycFieldGroupService#findById(String)
     */
    @Override
    protected KycFieldGroupDTO handleFindById(String id)
            throws Exception {

        KycFieldGroup kycFieldGroup = this.kycFieldGroupRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new KycFieldGroupServiceException(
                        messageSource.getMessage("kycFieldGroup.notFound", new Object[] { id }, null)));

        return this.kycFieldGroupMapper.toKycFieldGroupDTO(kycFieldGroup);
    }

    /**
     * @see bw.co.centralkyc.settings.kyc.KycFieldGroupService#save(@Valid
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

                settings.getOrganisationKycFieldGroups().add(kycFieldGroup);
                kycFieldGroup.setOrganisationSettings(settings);

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
     * @see bw.co.centralkyc.settings.kyc.KycFieldGroupService#remove(String)
     */
    @Override
    protected boolean handleRemove(String id)
            throws Exception {

        if (!this.kycFieldGroupRepository.existsById(UUID.fromString(id))) {
            throw new KycFieldGroupServiceException(
                    messageSource.getMessage("kycFieldGroup.notFound", new Object[] { id }, null));
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
                        messageSource.getMessage("kycFieldGroup.notFound", new Object[] { id }, null)));

        return this.kycFieldGroupMapper.toKycFieldGroupDTO(kycFieldGroup);
    }
}