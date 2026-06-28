package kg.core.storage;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.errors.MinioException;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.UUID;

@Slf4j
@Service
@Profile("minio")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MinioFileStorageService implements FileStorageService {

    MinioClient minioClient;
    String bucket;

    public MinioFileStorageService(
            @Value("${minio.endpoint}") String endpoint,
            @Value("${minio.access-key}") String accessKey,
            @Value("${minio.secret-key}") String secretKey,
            @Value("${minio.bucket}") String bucket
    ) {
        this.minioClient = MinioClient.builder()
                .endpoint(endpoint)
                .credentials(accessKey, secretKey)
                .build();
        this.bucket = bucket;
    }

    @Override
    public String upload(InputStream inputStream, String fileName, String contentType) {
        String objectName = generateObjectName(fileName);
        try {
            PutObjectArgs args = PutObjectArgs.builder()
                    .bucket(bucket)
                    .object(objectName)
                    .stream(inputStream, -1, 10 * 1024 * 1024)
                    .contentType(contentType)
                    .build();
            minioClient.putObject(args);
            return objectName;
        } catch (Exception e) {
            if (e instanceof MinioException) {
                log.error("MinIO error while uploading file {}: {}", fileName, e.getMessage());
            } else {
                log.error("Unexpected error while uploading file {}", fileName, e);
            }
            throw new RuntimeException("Failed to upload file to MinIO", e);
        }
    }

    @Override
    public InputStream download(String storageKey) {
        try {
            GetObjectArgs args = GetObjectArgs.builder()
                    .bucket(bucket)
                    .object(storageKey)
                    .build();
            return minioClient.getObject(args);
        } catch (Exception e) {
            if (e instanceof MinioException) {
                log.error("MinIO error while downloading object {}: {}", storageKey, e.getMessage());
            } else {
                log.error("Unexpected error while downloading object {}", storageKey, e);
            }
            throw new RuntimeException("Failed to download file from MinIO", e);
        }
    }

    @Override
    public void delete(String storageKey) {
        try {
            RemoveObjectArgs args = RemoveObjectArgs.builder()
                    .bucket(bucket)
                    .object(storageKey)
                    .build();
            minioClient.removeObject(args);
        } catch (Exception e) {
            if (e instanceof MinioException) {
                log.error("MinIO error while deleting object {}: {}", storageKey, e.getMessage());
            } else {
                log.error("Unexpected error while deleting object {}", storageKey, e);
            }
            throw new RuntimeException("Failed to delete file from MinIO", e);
        }
    }

    private String generateObjectName(String fileName) {
        String cleanFileName = fileName == null ? "file" : fileName.replaceAll("[^a-zA-Z0-9\\.\\-_]", "_");
        return UUID.randomUUID() + "_" + cleanFileName;
    }
}
