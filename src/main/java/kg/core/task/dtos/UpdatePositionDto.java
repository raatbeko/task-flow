package kg.core.task.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

@Schema(description = "DTO для изменения позиции задачи")
public record UpdatePositionDto(
        @NotNull @PositiveOrZero
        @Schema(description = "Значение позиции") Integer position
) {
}
