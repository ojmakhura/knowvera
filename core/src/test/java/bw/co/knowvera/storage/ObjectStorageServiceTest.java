package bw.co.knowvera.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.time.Instant;
import java.util.List;
import java.util.function.Consumer;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CommonPrefix;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.HeadBucketRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;
import software.amazon.awssdk.services.s3.model.NoSuchBucketException;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.model.S3Object;
import software.amazon.awssdk.services.s3.paginators.ListObjectsV2Iterable;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("unchecked")
class ObjectStorageServiceTest {

    @Mock
    private S3Client s3;

    private ObjectStorageService service(boolean createBucket) {
        return new ObjectStorageService(s3, "knowvera", createBucket);
    }

    private static PutObjectRequest built(Consumer<PutObjectRequest.Builder> consumer) {
        PutObjectRequest.Builder builder = PutObjectRequest.builder();
        consumer.accept(builder);
        return builder.build();
    }

    @Test
    void uploadPutsTheObjectAndChecksTheBucketOnlyOnce() throws Exception {
        ObjectStorageService service = service(true);

        assertEquals("docs/a.pdf", service.uploadFile(" docs/a.pdf ", new ByteArrayInputStream(new byte[3]), 3,
                "application/pdf"));
        service.uploadFile("docs/b.pdf", new ByteArrayInputStream(new byte[1]), 1, "application/pdf");

        verify(s3, times(1)).headBucket(any(Consumer.class));
        ArgumentCaptor<Consumer<PutObjectRequest.Builder>> request = ArgumentCaptor.forClass(Consumer.class);
        verify(s3, times(2)).putObject(request.capture(), any(RequestBody.class));
        PutObjectRequest first = built(request.getAllValues().get(0));
        assertEquals("knowvera", first.bucket());
        assertEquals("docs/a.pdf", first.key());
        assertEquals("application/pdf", first.contentType());
        assertEquals(3L, first.contentLength());
    }

    @Test
    void missingBucketIsCreated() throws Exception {
        when(s3.headBucket(any(Consumer.class))).thenThrow(NoSuchBucketException.builder().statusCode(404).build());

        service(true).uploadFile("a", new ByteArrayInputStream(new byte[0]), 0, "text/plain");

        ArgumentCaptor<Consumer<CreateBucketRequest.Builder>> request = ArgumentCaptor.forClass(Consumer.class);
        verify(s3).createBucket(request.capture());
        CreateBucketRequest.Builder builder = CreateBucketRequest.builder();
        request.getValue().accept(builder);
        assertEquals("knowvera", builder.build().bucket());
    }

    @Test
    void aBare404CountsAsAMissingBucket() throws Exception {
        // Some S3-compatible servers answer HEAD bucket with a 404 and no NoSuchBucket code
        when(s3.headBucket(any(Consumer.class))).thenThrow(S3Exception.builder().statusCode(404).build());

        service(true).uploadFile("a", new ByteArrayInputStream(new byte[0]), 0, "text/plain");

        verify(s3).createBucket(any(Consumer.class));
    }

    @Test
    void missingBucketIsAnErrorWhenCreatingIsOff() {
        when(s3.headBucket(any(Consumer.class))).thenThrow(NoSuchBucketException.builder().statusCode(404).build());

        IllegalStateException e = assertThrows(IllegalStateException.class,
                () -> service(false).uploadFile("a", new ByteArrayInputStream(new byte[0]), 0, "text/plain"));
        assertTrue(e.getMessage().contains("knowvera"));
        verify(s3, never()).createBucket(any(Consumer.class));
        verify(s3, never()).putObject(any(Consumer.class), any(RequestBody.class));
    }

    @Test
    void otherBucketErrorsAreNotTreatedAsMissing() {
        when(s3.headBucket(any(Consumer.class))).thenThrow(S3Exception.builder().statusCode(403).build());

        assertThrows(S3Exception.class,
                () -> service(true).uploadFile("a", new ByteArrayInputStream(new byte[0]), 0, "text/plain"));
        verify(s3, never()).createBucket(any(Consumer.class));
    }

    @Test
    void listReturnsFilesAndSubdirectoriesUnderTheDirectory() throws Exception {
        when(s3.listObjectsV2Paginator(any(ListObjectsV2Request.class)))
                .thenAnswer(call -> new ListObjectsV2Iterable(s3, call.getArgument(0)));
        when(s3.listObjectsV2(any(ListObjectsV2Request.class))).thenReturn(ListObjectsV2Response.builder()
                .contents(S3Object.builder().key("requests/a.xlsx").size(10L).lastModified(Instant.EPOCH).build())
                .commonPrefixes(CommonPrefix.builder().prefix("requests/2026/").build())
                .isTruncated(false)
                .build());

        List<ObjectStorageService.StoredObject> objects = service(true).getFileList("/requests");

        ArgumentCaptor<ListObjectsV2Request> request = ArgumentCaptor.forClass(ListObjectsV2Request.class);
        verify(s3).listObjectsV2(request.capture());
        assertEquals("requests/", request.getValue().prefix(), "no leading slash, one trailing slash");
        assertEquals("/", request.getValue().delimiter(), "one level, not recursive");
        assertEquals(List.of(
                new ObjectStorageService.StoredObject("requests/2026/", 0, null, true),
                new ObjectStorageService.StoredObject("requests/a.xlsx", 10, Instant.EPOCH, false)), objects);
    }

    @Test
    void blankObjectNamesAreRejected() {
        assertThrows(IllegalArgumentException.class, () -> service(true).downloadFile(" "));
        assertThrows(IllegalArgumentException.class, () -> service(true).deleteFile(null));
        verify(s3, never()).headBucket(any(HeadBucketRequest.class));
    }
}
