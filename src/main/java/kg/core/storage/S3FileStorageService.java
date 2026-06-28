package kg.core.storage;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

@Service
@Profile("s3")
public class S3FileStorageService implements FileStorageService {

    @Override
    public String upload(InputStream inputStream, String fileName, String contentType) {
        throw new UnsupportedOperationException("S3FileStorageService.upload is not implemented yet");
    }

    @Override
    public InputStream download(String storageKey) {
        return new ByteArrayInputStream(new byte[0]);
    }

    @Override
    public void delete(String storageKey) {
    }
}
