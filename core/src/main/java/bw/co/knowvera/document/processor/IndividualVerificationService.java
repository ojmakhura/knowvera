package bw.co.knowvera.document.processor;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Objects;
import java.util.UUID;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import bw.co.knowvera.QueueObject;
import bw.co.knowvera.TargetEntity;
import bw.co.knowvera.individual.Individual;
import bw.co.knowvera.individual.IndividualRepository;
import bw.co.knowvera.individual.IndividualServiceException;
import bw.co.knowvera.kyc.KycRecord;
import bw.co.knowvera.kyc.KycRecordDTO;
import bw.co.knowvera.kyc.KycRecordRepository;
import bw.co.knowvera.kyc.KycRecordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class IndividualVerificationService {

    private final IndividualRepository individualRepository;
    private final KycRecordRepository kycRecordRepository;
    private final KycRecordService kycRecordService;

    @RabbitListener(queues = "${app.rabbitmq.individualVerificationQueue}")
    public void handleIndividualVerification(QueueObject queueObject) {
        log.info("Processing individual verification for individual ID: {}", queueObject.objectId());

        try {

            Individual individual = individualRepository.findById(UUID.fromString(queueObject.objectId())).orElseThrow(
                () -> new IndividualServiceException("Individual not found for ID: " + queueObject.objectId())
            );

            KycRecord record = kycRecordRepository.findAll().stream()
                    .filter(item -> item.getTarget() == TargetEntity.INDIVIDUAL)
                    .filter(item -> Objects.equals(item.getTargetId(), queueObject.objectId()))
                    .max(Comparator.comparing(this::getRecordTimestamp,
                            Comparator.nullsLast(Comparator.naturalOrder())))
                    .orElse(null);

            if (record == null) {
                log.warn("No KYC record found for individual ID: {}", queueObject.objectId());
                return;
            }

            KycRecordDTO updatedRecord = kycRecordService.runVerification(record.getId().toString(), "AI-AGENT");
            log.info("Individual verification completed for individual ID: {} using KYC record {} with status {}",
                    queueObject.objectId(), updatedRecord.getId(), updatedRecord.getKycStatus());
        } catch (Exception e) {
            log.error("Failed to run individual verification for individual ID: {}", queueObject.objectId(), e);
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