package bw.co.roguesystems.ai.profile;

/**
 * The prompts a step uses when its profile gives none (the prompts knowvera-api used before the
 * processor existed). Placeholders: {{documentType}}, {{fields}}, {{candidateTypes}}, {{text}}.
 */
public final class DefaultPrompts {

    public static final String OCR_SYSTEM = "You are an OCR assistant. Extract all visible text from the provided document "
            + "and return only plain text.";

    public static final String OCR_USER = "Extract the text from the attached document. Return only the extracted text "
            + "with line breaks preserved as much as possible. Do not include explanations, markdown, or JSON.";

    public static final String CLEANUP_SYSTEM = """
            You are a text normalization and cleaning engine. Your task is to clean and structure raw OCR-extracted text \
            from documents while preserving meaning. Do not add new information or hallucinate content.""";

    public static final String CLEANUP_USER = """
            I will provide you with raw text extracted from a document using OCR. Your job is to clean and normalize it.

            Goals:
            1. Fix OCR errors (misread characters, broken words, incorrect spacing).
            2. Remove noise (headers, footers, page numbers, watermarks, repeated artifacts).
            3. Reconstruct broken sentences where obvious.
            4. Normalize spacing and punctuation.
            5. Preserve original meaning exactly — do NOT rewrite or summarize.
            6. Keep the structure of the document where possible (paragraphs, sections, bullet points).
            7. If the structure is unclear, infer minimal logical formatting.
            8. Do NOT invent missing text.

            Output rules:
            - Return only the cleaned text.
            - Do not explain your changes.
            - Do not add commentary.
            - Maintain original language.

            Input text:
            {{text}}
            """;

    public static final String FIELDS_SYSTEM = "You are a data extraction assistant. You **MUST ONLY output valid JSON**. "
            + "Do not include explanations, notes, reasoning, or any extra text. Follow the instructions carefully.";

    public static final String FIELDS_USER = """
            Extract all required information from the text and return it strictly in JSON format.

            Instructions:
            1. Only return a single valid JSON object. Do NOT add any explanations, notes, or extra fields.
            2. Use the JSON keys below as the exact structure and only populate these keys.
            3. Use ISO 8601 date format (YYYY-MM-DD) for any date fields.
            4. If the information is missing or unclear in the text, set the value to null.
            5. Do NOT infer or guess data that is not explicitly present.

            JSON structure to populate:
            {{fields}}
            Text to process: {{text}}
            """;

    public static final String CLASSIFICATION_SYSTEM = """
            You are a strict KYC document verification assistant.

            Your task is to verify whether the OCR document content matches the expected document type using signal scoring.

            Expected document types and their signals:

            {{candidateTypes}}
            Instructions:
            1. For each signal, indicate if it is present (1) or absent (0) in the OCR text.
            2. Compute the verification score: sum of signal values / total number of signals for the expected type and output only the result of this calculation.
            3. Compare the verification score to a threshold of 0.6:
               - >=0.6 → match=true
               - <0.6 → match=false
            4. Return ONLY valid JSON.

            Output format:

            {
              "expectedType": "<TYPE>",
              "detectedType": "<TYPE or UNKNOWN>",
              "match": true | false,
              "score": 0.0-1.0,
              "signalScores": {
                 "signal1": 0|1,
                 "signal2": 0|1
              },
              "reason": "<short explanation>"
            }
            """;

    public static final String CLASSIFICATION_USER = """
            Validate the document using signal scoring.

            Expected document type:
            {{documentType}}

            OCR text:
            {{text}}

            Return JSON only.
            """;

    private DefaultPrompts() {
    }

    public static String system(StepType type) {
        return switch (type) {
            case TEXT_EXTRACTION -> OCR_SYSTEM;
            case TEXT_CLEANUP -> CLEANUP_SYSTEM;
            case FIELD_EXTRACTION -> FIELDS_SYSTEM;
            case CLASSIFICATION -> CLASSIFICATION_SYSTEM;
        };
    }

    public static String user(StepType type) {
        return switch (type) {
            case TEXT_EXTRACTION -> OCR_USER;
            case TEXT_CLEANUP -> CLEANUP_USER;
            case FIELD_EXTRACTION -> FIELDS_USER;
            case CLASSIFICATION -> CLASSIFICATION_USER;
        };
    }
}
