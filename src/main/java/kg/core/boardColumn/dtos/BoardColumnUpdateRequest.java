package kg.core.boardColumn.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Запрос на обновление колонки")
public record BoardColumnUpdateRequest (

        @NotBlank
        @Schema(description = "Имя колонки") String name

) {
}
