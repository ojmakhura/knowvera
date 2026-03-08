package bw.co.centralkyc.minio;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.minio.BucketExistsArgs;
import io.minio.GetObjectArgs;
import io.minio.ListObjectsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.ObjectWriteResponse;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.Result;
import io.minio.messages.Item;

@Service
public class MinioService {

    private final MinioClient minioClient;
    private final String bucketName;    

    public MinioService(MinioClient minioClient, @Value("${minio.bucket-name}") String bucketName) {
        this.minioClient = minioClient;
        this.bucketName = bucketName;
    }

    public String uploadFile(String objectName, InputStream data, long size, String contentType) throws Exception {
        String safeObjectName = requireObjectName(objectName);

        // Check if the bucket exists, if not create it
        boolean bucketExists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
        if (!bucketExists) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
        }
        
        ObjectWriteResponse response = minioClient.putObject(
            PutObjectArgs.builder()
                .bucket(bucketName)
                .object(safeObjectName)
                .stream(data, size, -1)
                .contentType(contentType)
                .build()
        );

        return response.object();
    }

    public InputStream downloadFile(String objectName) throws Exception {
        String safeObjectName = requireObjectName(objectName);

        return minioClient.getObject(
            GetObjectArgs.builder()
                .bucket(bucketName)
                .object(safeObjectName)
                .build()
        );
    }

    public void deleteFile(String objectName) throws Exception {
        String safeObjectName = requireObjectName(objectName);

        minioClient.removeObject(
            RemoveObjectArgs.builder()
                .bucket(bucketName)
                .object(safeObjectName)
                .build()
        );
    }

    public Collection<Item> getFileList(String directory) throws Exception {

        if(directory == null || directory.isEmpty()) {
            directory = "/";

        } else if(!directory.endsWith("/")) {

            directory = directory + "/";
        }

        ListObjectsArgs args = ListObjectsArgs.builder()
            .bucket(bucketName)
            .prefix(directory)
            .recursive(false)
            .build();

        

        Iterable<Result<Item>> results = minioClient.listObjects(args);
        List<Item> items = new ArrayList<>();
        for (Result<Item> result : results) {
            Item item = result.get();
            items.add(item);
        }

        return items;
    }

    private String requireObjectName(String objectName) {
        if (objectName == null || objectName.trim().isEmpty()) {
            throw new IllegalArgumentException("Minio object name must not be null or blank");
        }
        return objectName.trim();
    }
}
