package kg.core.task.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import kg.core.task.model.Priority;
import kg.core.task.model.Task;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Set;

@Schema(description = "DTO ответа с данными задачи")
public record TaskDto(
        @NotBlank @Size(max = 50)
        @Schema(description = "Название задачи") String title,

        @Schema(description = "Описание задачи") String description,

        @NotNull
        @Schema(description = "Приоритет задачи") Priority priority,

        @NotNull
        @Schema(description = "ID колонки") Long boardColumnId,

        @NotNull @PositiveOrZero
        @Schema(description = "Позиция задачи") Integer position,

        @Schema(description = "Пользователь назначенный на задачу") Set<Long> assignees,

        @Schema(description = "Теги прикрепленные к задаче") Long[] tags,

        @NotNull
        @Schema(description = "Дедлайн задачи") LocalDateTime dueDate
) {
}
