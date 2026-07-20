package kg.core.boardColumn.dtos;

import io.swagger.v3.oas.annotations.media.Schema;


@Schema(description = "Запрос на обновление позиции колонки")
public record BoardColumnPositionRequest(

        @Schema(description = "Позиция колонки") Integer position

) {
}
