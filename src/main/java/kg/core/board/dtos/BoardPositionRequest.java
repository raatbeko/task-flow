package kg.core.board.dtos;


import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Запрос на обновление позиции доски")
public record BoardPositionRequest(

        @Schema(description = "Позиция доски") Integer position

) {
}