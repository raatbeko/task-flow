package kg.core.task.dtos;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO для изменения позиции задачи, назначения тега, назначения пользователя")
public record UpdateDto(
        @Schema(description = "Значение позиции") Long position,
        @Schema(description = "Значения ID тега") Long[] idTags,
        @Schema(description = "Значения ID пользователя") Long[] idUsers
) {
}
