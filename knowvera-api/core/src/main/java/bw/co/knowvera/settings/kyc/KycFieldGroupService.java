package bw.co.knowvera.settings.kyc;

import bw.co.knowvera.TargetEntity;
import jakarta.validation.Valid;
import java.util.List;

/**
 * TODO: Model Documentation for bw.co.knowvera.settings.kyc.KycFieldGroupService
 */
public interface KycFieldGroupService
{
    /**
     * TODO: Model Documentation for bw.co.knowvera.settings.kyc.KycFieldGroupService.findById
     * @param id TODO: Model Documentation for bw.co.knowvera.settings.kyc.KycFieldGroupService.findById(id)
     * @return KycFieldGroupDTO
     */
    public KycFieldGroupDTO findById(String id) ;

    /**
     * TODO: Model Documentation for bw.co.knowvera.settings.kyc.KycFieldGroupService.save
     * @param fieldGroup TODO: Model Documentation for bw.co.knowvera.settings.kyc.KycFieldGroupService.save(fieldGroup)
     * @return KycFieldGroupDTO
     */
    public KycFieldGroupDTO save(@Valid KycFieldGroupDTO fieldGroup) ;

    /**
     * TODO: Model Documentation for bw.co.knowvera.settings.kyc.KycFieldGroupService.remove
     * @param id TODO: Model Documentation for bw.co.knowvera.settings.kyc.KycFieldGroupService.remove(id)
     * @return boolean
     */
    public boolean remove(String id) ;

    /**
     * TODO: Model Documentation for bw.co.knowvera.settings.kyc.KycFieldGroupService.findByTarget
     * @param targetType TODO: Model Documentation for
bw.co.knowvera.settings.kyc.KycFieldGroupService.findByTarget(targetType)
     * @return List<KycFieldGroupDTO>
     */
    public List<KycFieldGroupDTO> findByTarget(TargetEntity targetType) ;

    /**
     * TODO: Model Documentation for bw.co.knowvera.settings.kyc.KycFieldGroupService.removeField
     * @param id TODO: Model Documentation for bw.co.knowvera.settings.kyc.KycFieldGroupService.removeField(id)
     * @param fieldId TODO: Model Documentation for bw.co.knowvera.settings.kyc.KycFieldGroupService.removeField(fieldId)
     * @return KycFieldGroupDTO
     */
    public KycFieldGroupDTO removeField(String id, String fieldId) ;

}