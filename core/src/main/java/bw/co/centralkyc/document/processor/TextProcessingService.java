package bw.co.centralkyc.document.processor;

import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import bw.co.centralkyc.document.DocumentDTO;
import bw.co.centralkyc.document.DocumentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class TextProcessingService {

    private final DocumentService documentService;

    @Async
    public void processExtractedText(String documentId) {
        log.info("Processing extracted text for document ID: {}", documentId);
        // Implement your text processing logic here
        // For example, you could perform keyword extraction, sentiment analysis, etc.

        try {
            DocumentDTO document = documentService.findById(documentId); // Replace with actual retrieval logic

            if (document == null) {
                log.warn("Document not found for ID: {}", documentId);
                return;
            }

            String extractedText = document.getFileContent(); // Assuming this contains the extracted text

            if(StringUtils.isBlank(extractedText)) {
                log.warn("Extracted text is empty for document ID: {}", documentId);
                return;
            }

            log.info("Completed text processing for document ID: {}", documentId);
        } catch (Exception e) {
            log.error("Text processing interrupted for document ID: {}", documentId, e);
        }
    }

}
