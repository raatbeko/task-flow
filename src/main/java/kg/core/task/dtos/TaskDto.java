package kg.core.task.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import kg.core.task.model.Priority;
import kg.core.task.model.Task;

import java.time.LocalDateTime;
import java.util.Collection;

@Schema(description = "DTO ответа с данными задачи")
public record TaskDto(
        @Schema(description = "Название задачи") String title,
        @Schema(description = "Описание задачи") String description,
        @Schema(description = "Приоритет задачи") Priority priority,
        @Schema(description = "ID колонки") Long boardColumnId,
        @Schema(description = "Позиция задачи") int position,
        @Schema(description = "Пользователь назначенный на задачу") Long assignees,
        @Schema(description = "Теги прикрепленные к задаче") Long[] tags,
        @Schema(description = "Дедлайн задачи") LocalDateTime dueDate
) {
}
