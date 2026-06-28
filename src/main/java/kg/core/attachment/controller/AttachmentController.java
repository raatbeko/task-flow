package kg.core.attachment.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kg.core.attachment.model.Attachment;
import kg.core.attachment.repository.AttachmentRepository;
import kg.core.storage.FileStorageService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.InputStream;

@RestController
@RequiredArgsConstructor
@RequestMapping("/attachments")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Attachments", description = "Скачивание файлов-вложений")
public class AttachmentController {

    AttachmentRepository attachmentRepository;
    FileStorageService fileStorageService;

    @GetMapping("/{uuid}")
    @Operation(summary = "Скачать вложение", description = "Скачивает содержимое вложения по его UUID")
    public ResponseEntity<Resource> download(@PathVariable String uuid) {
        Attachment attachment = attachmentRepository.findByUuid(uuid)
                .orElseThrow(() -> new RuntimeException("Attachment not found"));

        InputStream is = fileStorageService.download(attachment.getStorageKey());
        InputStreamResource resource = new InputStreamResource(is);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + attachment.getFileName() + "\"")
                .contentType(MediaType.parseMediaType(attachment.getContentType()))
                .body(resource);
    }
}
