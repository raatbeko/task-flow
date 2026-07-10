package kg.core.board.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import kg.core.board.model.BoardStatus;

@Schema(description = "Ответ с данными о доске")
public record BoardResponse(

        @Schema(description = "Идентификатор доски") Long id,

        @Schema(description = "Название доски") String name,

        @Schema(description = "Описание доски") String description,

        @Schema(description = "Позиция доски в проекте") Integer position,

        @Schema(description = "Текущий статус доски (ACTIVE, ARCHIVED)") BoardStatus boardStatus,

        @Schema(description = "Идентификатор проекта, к которому принадлежит доска") Long projectId

) {
}
