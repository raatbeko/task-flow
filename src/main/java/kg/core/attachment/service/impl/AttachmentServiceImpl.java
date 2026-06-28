package kg.core.attachment.service.impl;

import kg.core.attachment.model.Attachment;
import kg.core.attachment.repository.AttachmentRepository;
import kg.core.attachment.service.AttachmentService;
import kg.core.base.service.impl.DefaultCrudService;
import kg.core.storage.FileStorageService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AttachmentServiceImpl extends DefaultCrudService<Attachment, Long> implements AttachmentService {

    AttachmentRepository repository;
    FileStorageService fileStorageService;

    public AttachmentServiceImpl(AttachmentRepository repository, FileStorageService fileStorageService) {
        super(repository);
        this.repository = repository;
        this.fileStorageService = fileStorageService;
    }

    @Override
    @Transactional
    public Attachment upload(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        String storageKey;
        try (InputStream is = file.getInputStream()) {
            storageKey = fileStorageService.upload(is, file.getOriginalFilename(), file.getContentType());
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload attachment file", e);
        }

        Attachment attachment = new Attachment();
        attachment.setUuid(UUID.randomUUID().toString());
        attachment.setFileName(file.getOriginalFilename());
        attachment.setContentType(file.getContentType());
        attachment.setSize(file.getSize());
        attachment.setStorageKey(storageKey);

        return save(attachment);
    }
}
