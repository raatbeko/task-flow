package kg.core.attachment.service;

import kg.core.attachment.model.Attachment;
import kg.core.base.service.CrudService;
import org.springframework.web.multipart.MultipartFile;

public interface AttachmentService extends CrudService<Attachment, Long> {

    Attachment upload(MultipartFile file);
}
