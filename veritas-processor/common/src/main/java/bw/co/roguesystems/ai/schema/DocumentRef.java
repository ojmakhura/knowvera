package bw.co.roguesystems.ai.schema;

/**
 * Where the processor finds the file: an object in the shared S3 bucket.
 *
 * @param objectKey   the object key (a full URL to the object is accepted too)
 * @param contentType e.g. application/pdf
 * @param fileName    the original file name
 */
public record DocumentRef(String objectKey, String contentType, String fileName) {
}
