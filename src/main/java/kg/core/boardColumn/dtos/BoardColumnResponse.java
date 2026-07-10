package kg.core.boardColumn.dtos;


import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Ответ с данными о колонке доски")
public record BoardColumnResponse(

        @Schema(description = "Идентификатор колонки") Long id,

        @Schema(description = "Название колонки") String name,

        @Schema(description = "Текущая позиция колонки на доске") Integer position,

        @Schema(description = "Идентификатор доски, которой принадлежит колонка") Long boardId
) {}
