package kg.core.attachment.dtos;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO метаданных вложения")
public record AttachmentDto(
        @Schema(description = "ID вложения в базе данных") Long id,
        @Schema(description = "UUID вложения, используемый в URL") String uuid,
        @Schema(description = "Исходное имя файла") String fileName,
        @Schema(description = "MIME-тип содержимого") String contentType,
        @Schema(description = "Размер файла в байтах") Long size
) {
}
