package bw.co.knowvera.document.processor;

import java.util.UUID;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import bw.co.knowvera.QueueObject;
import bw.co.knowvera.organisation.Organisation;
import bw.co.knowvera.organisation.OrganisationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrganisationVerificationService {

    private final OrganisationRepository organisationRepository;

    @RabbitListener(queues = "${app.rabbitmq.organisationVerificationQueue}")
    public void handleOrganisationVerification(QueueObject queueObject) {
        log.info("Processing organisation verification for organisation ID: {}", queueObject.objectId());

        try {
            Organisation org = organisationRepository.findById(UUID.fromString(queueObject.objectId()))
                    .orElse(null);

            if (org == null) {
                log.warn("No organisation found for organisation ID: {}", queueObject.objectId());
                return;
            }

        } catch (Exception e) {
            log.error("Failed to run organisation verification for organisation ID: {}", queueObject.objectId(), e);
        }
    }
}
