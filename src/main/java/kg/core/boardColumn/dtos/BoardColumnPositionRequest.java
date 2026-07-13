package kg.core.boardColumn.dtos;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Запрос на изменение позиции колонки при Drag and Drop")
public record BoardColumnPositionRequest(

        @NotNull
        @Schema(description = "Новая позиция колонки на доске") Integer position

) {}
