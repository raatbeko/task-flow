package kg.core.storage;

import java.io.InputStream;

public interface FileStorageService {

    String upload(InputStream inputStream, String fileName, String contentType);

    InputStream download(String storageKey);

    void delete(String storageKey);
}
