package bw.co.knowvera.document.processor;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import bw.co.knowvera.QueueObject;
import bw.co.knowvera.kyc.KycRecordDTO;
import bw.co.knowvera.kyc.KycRecordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Service
public class KycVerificationService {

    private static final Logger log = LoggerFactory.getLogger(KycVerificationService.class);

    private final KycRecordService kycRecordService;

    public KycVerificationService(KycRecordService kycRecordService) {
        this.kycRecordService = kycRecordService;
    }

    @RabbitListener(queues = "${app.rabbitmq.kycVerificationQueue}")
    public void handleKycVerification(QueueObject queueObject) {
        log.info("Processing KYC verification for KYC record ID: {}", queueObject.objectId());

        try {
            KycRecordDTO record = kycRecordService.runVerification(queueObject.objectId(), "AI-AGENT");

            if (record == null) {
                log.warn("KYC record not found for ID: {}", queueObject.objectId());
                return;
            }

            log.info("KYC verification completed for record ID: {} — status: {}",
                    record.getId(), record.getKycStatus());

        } catch (Exception e) {
            log.error("Failed to run KYC verification for record ID: {}", queueObject.objectId(), e);
        }
    }
}
