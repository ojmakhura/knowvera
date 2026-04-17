package bw.co.centralkyc.document.processor;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Objects;
import java.util.UUID;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import bw.co.centralkyc.QueueObject;
import bw.co.centralkyc.TargetEntity;
import bw.co.centralkyc.document.DocumentDTO;
import bw.co.centralkyc.kyc.KycRecord;
import bw.co.centralkyc.kyc.KycRecordDTO;
import bw.co.centralkyc.kyc.KycRecordRepository;
import bw.co.centralkyc.kyc.KycRecordService;
import bw.co.centralkyc.organisation.Organisation;
import bw.co.centralkyc.organisation.OrganisationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrganisationVerificationService {

    // private final KycRecordRepository kycRecordRepository;
    // private final KycRecordService kycRecordService;
    private final OrganisationRepository organisationRepository;

    @RabbitListener(queues = "${app.rabbitmq.organisationVerificationQueue}")
    public void handleOrganisationVerification(DocumentDTO document) {
        log.info("Processing organisation verification for organisation ID: {}", document.getTargetId());

        try {
            
            Organisation org = organisationRepository.findById(UUID.fromString(document.getTargetId()))
                    .orElse(null);

            if (org == null) {
                log.warn("No organisation found for organisation ID: {}", document.getTargetId());
                return;
            }

            // document.gete

            // if(document.)

        } catch (Exception e) {
            log.error("Failed to run organisation verification for organisation ID: {}", document.getTargetId(), e);
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