package bw.co.knowvera.novu;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;

import java.util.Map;

import org.junit.jupiter.api.Test;

import co.novu.Novu;
import co.novu.models.components.SubscriberPayloadDto;
import co.novu.models.components.TriggerEventRequestDto;

class NovuQueueProcessorTest {

    @Test
    void triggerSendsTheSubscriberInlineWithTheMessageIdAsTransaction() {

        TriggerEventRequestDto request = NovuQueueProcessor.triggerRequest("novu-new-user", Map.of(
                "email", "ann@example.com",
                "firstName", "Ann",
                "surname", "Applicant"), "msg-1");

        SubscriberPayloadDto subscriber = request.to().subscriberPayloadDto().orElseThrow();
        assertEquals("ann@example.com", subscriber.subscriberId());
        assertEquals("ann@example.com", subscriber.email().get());
        assertEquals("Ann", subscriber.firstName().get());
        assertEquals("Applicant", subscriber.lastName().get());
        assertEquals("novu-new-user", request.workflowId());
        assertEquals("msg-1", request.transactionId().orElseThrow());
        assertEquals("Ann", request.payload().orElseThrow().get("firstName"));
    }

    @Test
    void clientRequestSubscriberIsNamedAfterTheRecipient() {

        TriggerEventRequestDto request = NovuQueueProcessor.triggerRequest("novu-new-org-client-request", Map.of(
                "email", "kyc@client.example",
                "recipientName", "Client Co"), null);

        SubscriberPayloadDto subscriber = request.to().subscriberPayloadDto().orElseThrow();
        assertEquals("Client Co", subscriber.firstName().get());
        assertFalse(subscriber.lastName().isPresent());
        assertTrue(request.transactionId().isPresent(), "a transaction id is generated without a message id");
    }

    @Test
    void messagesWithoutEmailAreDropped() {

        Novu novu = mock(Novu.class);
        NovuQueueProcessor processor = new NovuQueueProcessor(novu);

        processor.processNewOrgClientRequestQueue(Map.of("recipientName", "Ann"), "msg-1");

        verifyNoInteractions(novu);
    }
}
