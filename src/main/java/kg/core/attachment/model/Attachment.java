package kg.core.attachment.model;

import jakarta.persistence.*;
import kg.core.base.model.AuditableEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "attachments")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Attachment extends AuditableEntity {

    @Column(nullable = false, unique = true, length = 36)
    String uuid;

    @Column(nullable = false)
    String fileName;

    @Column(nullable = false)
    String contentType;

    @Column(nullable = false)
    Long size;

    @Column(nullable = false)
    String storageKey;
}
