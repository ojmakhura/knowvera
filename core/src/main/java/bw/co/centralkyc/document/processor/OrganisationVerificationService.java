package bw.co.centralkyc.document.processor;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Objects;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import bw.co.centralkyc.QueueObject;
import bw.co.centralkyc.TargetEntity;
import bw.co.centralkyc.kyc.KycRecord;
import bw.co.centralkyc.kyc.KycRecordDTO;
import bw.co.centralkyc.kyc.KycRecordRepository;
import bw.co.centralkyc.kyc.KycRecordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrganisationVerificationService {

    private final KycRecordRepository kycRecordRepository;
    private final KycRecordService kycRecordService;

    @RabbitListener(queues = "${app.rabbitmq.organisationVerificationQueue}")
    public void handleOrganisationVerification(QueueObject queueObject) {
        log.info("Processing organisation verification for organisation ID: {}", queueObject.objectId());

        try {
            KycRecord record = kycRecordRepository.findAll().stream()
                    .filter(item -> item.getTarget() == TargetEntity.ORGANISATION)
                    .filter(item -> Objects.equals(item.getTargetId(), queueObject.objectId()))
                    .max(Comparator.comparing(this::getRecordTimestamp,
                            Comparator.nullsLast(Comparator.naturalOrder())))
                    .orElse(null);

            if (record == null) {
                log.warn("No KYC record found for organisation ID: {}", queueObject.objectId());
                return;
            }

            KycRecordDTO updatedRecord = kycRecordService.runVerification(record.getId().toString(), "AI-AGENT");
            log.info("Organisation verification completed for organisation ID: {} using KYC record {} with status {}",
                    queueObject.objectId(), updatedRecord.getId(), updatedRecord.getKycStatus());
        } catch (Exception e) {
            log.error("Failed to run organisation verification for organisation ID: {}", queueObject.objectId(), e);
        }
    }

    private LocalDateTime getRecordTimestamp(KycRecord record) {
        if (record.getModifiedAt() != null) {
            return record.getModifiedAt();
        }

        if (record.getCreatedAt() != null) {
            return record.getCreatedAt();
        }

        return record.getUploadDate() != null ? record.getUploadDate().atStartOfDay() : null;
    }
}