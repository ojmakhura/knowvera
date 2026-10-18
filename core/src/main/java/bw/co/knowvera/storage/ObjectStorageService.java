package bw.co.knowvera.storage;

import java.io.InputStream;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CommonPrefix;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;
import software.amazon.awssdk.services.s3.model.NoSuchBucketException;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.model.S3Object;

/**
 * Document files in an S3-compatible bucket (the S3Client comes from Spring Cloud AWS, spring.cloud.aws.*). Objects are
 * addressed by name; the name returned by {@link #uploadFile} is what documents store.
 */
@Service
public class ObjectStorageService {

    /** An object (or, for {@code directory}, a common prefix) found by {@link #getFileList}. */
    public record StoredObject(String name, long size, Instant lastModified, boolean directory) {
    }

    private final S3Client s3Client;
    private final String bucketName;
    private final boolean createBucket;

    /** Set once the bucket is known to exist, so uploads do not check it every time. */
    private volatile boolean bucketReady;

    public ObjectStorageService(S3Client s3Client, @Value("${storage.s3.bucket}") String bucketName,
            @Value("${storage.s3.create-bucket:true}") boolean createBucket) {
        this.s3Client = s3Client;
        this.bucketName = bucketName;
        this.createBucket = createBucket;
    }

    /** Stores {@code size} bytes of {@code data} under {@code objectName}; returns the object's name. */
    public String uploadFile(String objectName, InputStream data, long size, String contentType) throws Exception {
        String key = requireObjectName(objectName);
        ensureBucket();

        s3Client.putObject(request -> request.bucket(bucketName).key(key).contentType(contentType)
                .contentLength(size), RequestBody.fromInputStream(data, size));
        return key;
    }

    /** The object's content; the caller closes the stream. */
    public InputStream downloadFile(String objectName) throws Exception {
        String key = requireObjectName(objectName);
        return s3Client.getObject(request -> request.bucket(bucketName).key(key));
    }

    public void deleteFile(String objectName) throws Exception {
        String key = requireObjectName(objectName);
        s3Client.deleteObject(request -> request.bucket(bucketName).key(key));
    }

    /** The objects and sub-directories directly under {@code directory} (not recursive). */
    public List<StoredObject> getFileList(String directory) throws Exception {

        // S3 keys have no leading slash; "" lists the bucket's top level
        String prefix = directory == null ? "" : directory.replaceFirst("^/+", "");
        if (!prefix.isEmpty() && !prefix.endsWith("/")) {
            prefix = prefix + "/";
        }

        ListObjectsV2Request request = ListObjectsV2Request.builder()
                .bucket(bucketName)
                .prefix(prefix)
                .delimiter("/")
                .build();

        List<StoredObject> objects = new ArrayList<>();
        for (ListObjectsV2Response page : s3Client.listObjectsV2Paginator(request)) {
            for (CommonPrefix common : page.commonPrefixes()) {
                objects.add(new StoredObject(common.prefix(), 0, null, true));
            }
            for (S3Object object : page.contents()) {
                objects.add(new StoredObject(object.key(), object.size(), object.lastModified(), false));
            }
        }
        return objects;
    }

    private void ensureBucket() {
        if (bucketReady) {
            return;
        }
        try {
            s3Client.headBucket(request -> request.bucket(bucketName));
        } catch (S3Exception e) {
            // NoSuchBucket from AWS; some S3-compatible servers answer a bare 404
            if (!(e instanceof NoSuchBucketException) && e.statusCode() != 404) {
                throw e;
            }
            if (!createBucket) {
                throw new IllegalStateException("Storage bucket '" + bucketName + "' does not exist "
                        + "(storage.s3.create-bucket is off: create it on the storage server)", e);
            }
            try {
                s3Client.createBucket(request -> request.bucket(bucketName));
            } catch (S3Exception created) {
                // Another instance created it meanwhile
                if (!"BucketAlreadyOwnedByYou".equals(created.awsErrorDetails().errorCode())) {
                    throw created;
                }
            }
        }
        bucketReady = true;
    }

    private String requireObjectName(String objectName) {
        if (objectName == null || objectName.trim().isEmpty()) {
            throw new IllegalArgumentException("Storage object name must not be null or blank");
        }
        return objectName.trim();
    }
}
