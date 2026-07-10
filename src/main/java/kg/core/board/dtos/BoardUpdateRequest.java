package kg.core.board.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Запрос на обновление данных доски")
public record BoardUpdateRequest (

        @NotBlank
        @Schema(description = "Новое название доски") String name,

        @Schema(description = "Новое описание доски") String description,

        @NotNull
        @Schema(description = "Новая позиция доски при перемещении") Integer position
){
}
