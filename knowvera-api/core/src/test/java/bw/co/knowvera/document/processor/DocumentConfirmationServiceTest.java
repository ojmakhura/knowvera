package bw.co.knowvera.document.processor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import bw.co.knowvera.QueueObject;
import bw.co.knowvera.TargetEntity;
import bw.co.knowvera.document.DocumentAnalyticsStatus;
import bw.co.knowvera.document.DocumentDTO;
import bw.co.knowvera.document.DocumentService;
import bw.co.knowvera.document.DocumentValidationResults;
import bw.co.knowvera.document.DocumentVerificationStatus;
import bw.co.knowvera.kyc.KycRecord;
import bw.co.knowvera.kyc.KycRecordRepository;
import bw.co.knowvera.matcher.UniversalStringMatcher;
import bw.co.knowvera.properties.RabbitProperties;

@ExtendWith(MockitoExtension.class)
class DocumentConfirmationServiceTest {

    @Mock
    private DocumentService documentService;
    @Mock
    private KycRecordRepository kycRecordRepository;
    @Mock
    private RabbitTemplate rabbitTemplate;
    @Mock
    private RabbitProperties rabbitProperties;

    private DocumentConfirmationService service;
    private DocumentDTO document;

    @BeforeEach
    void setUp() {
        service = new DocumentConfirmationService(documentService, kycRecordRepository, new UniversalStringMatcher(),
                rabbitTemplate, rabbitProperties);
        document = new DocumentDTO();
        document.setId("doc-1");
        document.setTarget(TargetEntity.INDIVIDUAL);
        document.setTargetId("ind-1");
        lenient().when(documentService.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
    }

    private static DocumentValidationResults results(String detected, Boolean match, Double score) {
        DocumentValidationResults results = new DocumentValidationResults();
        results.setExpectedType("National ID");
        results.setDetectedType(detected);
        results.setMatch(match);
        results.setScore(score);
        return results;
    }

    @Test
    void aMatchGoesToManualReview() {
        DocumentDTO saved = service.applyConfirmation(results("National ID", true, 0.9), document);

        assertEquals(DocumentVerificationStatus.MANUAL_REVIEW, saved.getVerificationStatus());
        assertEquals(DocumentAnalyticsStatus.TYPE_CONFIRMATION_COMPLETE, saved.getAnalyticsStatus());
        assertEquals(Boolean.TRUE, saved.getValidationResults().getTypeMatch());
        verify(rabbitTemplate, never()).convertAndSend(anyString(), anyString(), any(Object.class));
    }

    @Test
    void aTypeMismatchRejectsAndVerifiesTheTargetAgain() {
        when(rabbitProperties.getIndividualVerificationQueueExchange()).thenReturn("x.individual");
        when(rabbitProperties.getIndividualVerificationQueueRoutingKey()).thenReturn("individual");

        DocumentDTO saved = service.applyConfirmation(results("Payslip", true, 0.9), document);

        assertEquals(DocumentVerificationStatus.REJECTED, saved.getVerificationStatus());
        assertEquals(Boolean.FALSE, saved.getValidationResults().getTypeMatch());
        verify(rabbitTemplate).convertAndSend("x.individual", "individual",
                new QueueObject("ind-1", TargetEntity.INDIVIDUAL, "ind-1"));
    }

    @Test
    void aMismatchIsRejectedOnlyBelowTheScoreThreshold() {
        assertEquals(DocumentVerificationStatus.REJECTED,
                service.applyConfirmation(results("National ID", false, 0.2), document).getVerificationStatus());
        assertEquals(DocumentVerificationStatus.MANUAL_REVIEW,
                service.applyConfirmation(results("National ID", false, 0.5), document).getVerificationStatus());
        assertEquals(DocumentVerificationStatus.MANUAL_REVIEW,
                service.applyConfirmation(results("National ID", null, null), document).getVerificationStatus());
    }

    @Test
    void aKycRecordDocumentVerifiesItsRecord() {
        UUID recordId = UUID.randomUUID();
        KycRecord record = new KycRecord();
        record.setId(recordId);
        record.setTarget(TargetEntity.INDIVIDUAL);
        record.setTargetId("ind-1");
        document.setTarget(TargetEntity.KYC_RECORD);
        document.setTargetId(recordId.toString());
        when(kycRecordRepository.findById(recordId)).thenReturn(Optional.of(record));
        when(rabbitProperties.getKycVerificationQueueExchange()).thenReturn("x.kyc");
        when(rabbitProperties.getKycVerificationQueueRoutingKey()).thenReturn("kyc");

        service.applyConfirmation(results("Payslip", false, 0.0), document);

        verify(rabbitTemplate).convertAndSend(eq("x.kyc"), eq("kyc"),
                eq(new QueueObject(recordId.toString(), TargetEntity.INDIVIDUAL, "ind-1")));
    }

    @Test
    void informationConfirmationIsQueued() {
        when(rabbitProperties.getInformationConfirmationQueueExchange()).thenReturn("x.info");
        when(rabbitProperties.getInformationConfirmationQueueRoutingKey()).thenReturn("info");

        service.dispatchInformationConfirmation(document);

        verify(rabbitTemplate).convertAndSend("x.info", "info", new QueueObject("doc-1", TargetEntity.INDIVIDUAL, "ind-1"));
    }
}
