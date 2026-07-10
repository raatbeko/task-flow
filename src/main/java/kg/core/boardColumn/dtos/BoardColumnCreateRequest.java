package kg.core.boardColumn.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Запрос на создание новой колонки")
public record BoardColumnCreateRequest(

        @NotNull
        @Schema(description = "Идентификатор доски, к которой относится колонка") Long boardId,

        @NotBlank
        @Schema(description = "Название колонки") String name

) {}
