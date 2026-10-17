package bw.co.knowvera.novu;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelCallback;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.server.ResponseStatusException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.GetResponse;

import bw.co.knowvera.messaging.ConfirmedPublisher;

@ExtendWith(MockitoExtension.class)
class NovuDeadLetterServiceTest {

    @Mock
    private RabbitTemplate rabbitTemplate;
    @Mock
    private AmqpAdmin amqpAdmin;
    @Mock
    private ConfirmedPublisher publisher;
    @Mock
    private Channel channel;

    private NovuDeadLetterService service;

    @BeforeEach
    void setUp() {
        service = new NovuDeadLetterService(rabbitTemplate, amqpAdmin, publisher,
                "q.user", "x.user", "user", "q.org-user", "x.org-user", "org-user",
                "q.request", "x.request", "request", "q.kyc", "x.kyc", "kyc");
    }

    @SuppressWarnings("unchecked")
    private void runCallbacksOnTheMockChannel() {
        when(rabbitTemplate.execute(any(ChannelCallback.class)))
                .thenAnswer(call -> ((ChannelCallback<Object>) call.getArgument(0)).doInRabbit(channel));
    }

    private static GetResponse deadLetter(long tag, String messageId) {
        AMQP.BasicProperties props = new AMQP.BasicProperties.Builder()
                .messageId(messageId)
                .contentType("application/json")
                .headers(Map.of("x-exception-message", "Novu down", "x-original-exchange", "x.user",
                        "__TypeId__", "java.util.HashMap"))
                .build();
        return new GetResponse(new Envelope(tag, false, "x.novu-dead-letter", "user"), props,
                "{\"email\":\"ann@example.com\"}".getBytes(StandardCharsets.UTF_8), 0);
    }

    @Test
    void movesMessagesBackAndAcknowledgesEachOnlyAfterItsCopyIsConfirmed() throws Exception {
        runCallbacksOnTheMockChannel();
        when(channel.basicGet("q.user.dlq", false))
                .thenReturn(deadLetter(1, "m-1"), deadLetter(2, "m-2"), (GetResponse) null);
        when(publisher.publishAndWait(eq("x.user"), eq("user"), any(Message.class), any())).thenReturn(true);

        assertEquals(2, service.redrive("q.user", 100));

        verify(channel).basicAck(1, false);
        verify(channel).basicAck(2, false);
        ArgumentCaptor<Message> copy = ArgumentCaptor.forClass(Message.class);
        verify(publisher, org.mockito.Mockito.times(2)).publishAndWait(eq("x.user"), eq("user"), copy.capture(), any());
        Message first = copy.getAllValues().get(0);
        assertEquals("m-1", first.getMessageProperties().getMessageId(), "Novu's transactionId is kept");
        assertFalse(first.getMessageProperties().getHeaders().containsKey("x-exception-message"));
        assertFalse(first.getMessageProperties().getHeaders().containsKey("x-original-exchange"));
        assertEquals("java.util.HashMap", first.getMessageProperties().getHeaders().get("__TypeId__"));
    }

    @Test
    void stopsAndKeepsTheMessageWhenTheBrokerDoesNotTakeItBack() throws Exception {
        runCallbacksOnTheMockChannel();
        when(channel.basicGet("q.user.dlq", false)).thenReturn(deadLetter(7, "m-7"));
        when(publisher.publishAndWait(any(), any(), any(Message.class), any())).thenReturn(false);

        assertEquals(0, service.redrive("q.user", 100));

        verify(channel, never()).basicAck(anyLong(), anyBoolean());
        verify(channel).basicNack(7, false, true);
    }

    @Test
    void respectsTheBatchSize() throws Exception {
        runCallbacksOnTheMockChannel();
        when(channel.basicGet("q.user.dlq", false)).thenReturn(deadLetter(1, "a"), deadLetter(2, "b"));
        when(publisher.publishAndWait(any(), any(), any(Message.class), any())).thenReturn(true);

        assertEquals(1, service.redrive("q.user", 1));
        verify(channel, org.mockito.Mockito.times(1)).basicGet("q.user.dlq", false);
    }

    @Test
    void rejectsUnknownQueuesAndBadBatchSizes() {
        assertEquals(404, assertThrows(ResponseStatusException.class, () -> service.redrive("q.other", 10))
                .getStatusCode().value());
        assertEquals(400, assertThrows(ResponseStatusException.class, () -> service.redrive("q.user", 0))
                .getStatusCode().value());
        assertEquals(400, assertThrows(ResponseStatusException.class,
                () -> service.redrive("q.user", NovuDeadLetterService.MAX_BATCH + 1)).getStatusCode().value());
    }
}
