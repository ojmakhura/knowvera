package bw.co.centralkyc.document.processor;

import bw.co.centralkyc.document.DocumentDTO;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import bw.co.centralkyc.QueueObject;
import bw.co.centralkyc.kyc.KycRecordDTO;
import bw.co.centralkyc.kyc.KycRecordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class KycVerificationService {

    private final KycRecordService kycRecordService;

    @RabbitListener(queues = "${app.rabbitmq.kycVerificationQueue}")
    public void handleKycVerification(DocumentDTO document) {
        log.info("Processing KYC verification for KYC record ID: {}", document.getTargetId());

        try {
            KycRecordDTO record = kycRecordService.runVerification(document.getTargetId(), "AI-AGENT");

            if (record == null) {
                log.warn("KYC record not found for ID: {}", document.getTargetId());
                return;
            }

            log.info("KYC verification completed for record ID: {} — status: {}",
                    record.getId(), record.getKycStatus());

        } catch (Exception e) {
            log.error("Failed to run KYC verification for record ID: {}", document.getTargetId(), e);
        }
    }
}
