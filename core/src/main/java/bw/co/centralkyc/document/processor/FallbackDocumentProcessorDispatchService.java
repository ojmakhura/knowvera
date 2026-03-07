package bw.co.centralkyc.document.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
public class FallbackDocumentProcessorDispatchService {

    private final static Logger log = LoggerFactory.getLogger(FallbackDocumentProcessorDispatchService.class);
    
     @RabbitListener(queues = "q.fall-back-document-dispatch")
    public void handleFailedDocumentDispatch(String documentId) {
        log.error("Failed to dispatch document with id: {}", documentId);
    }

}
