package kg.core.task.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import kg.core.task.model.Priority;

import java.time.LocalDateTime;

@Schema(description = "DTO ответа с данными задачи")
public record TaskDto(
        @Schema(description = "Название задачи") String title,
        @Schema(description = "Описание задачи") String description,
        @Schema(description = "Приоритет задачи") Priority priority,
        @Schema(description = "ID колонки") Long boardColumnId,
        @Schema(description = "Позиция задачи") int position,
        @Schema(description = "Дедлайн задачи") LocalDateTime dueDate
) {
}
