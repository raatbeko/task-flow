package kg.core.task.dtos;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO для изменения позиции задачи")
public record UpdatePosition(
        @Schema(description = "Позиция задачи") Long position
) {
}
