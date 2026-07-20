package kg.core.task.dtos;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO для добавления тега к задаче")
public record PurposeTags(
        @Schema(description = "Тег задачи") Long purpose
) {
}
