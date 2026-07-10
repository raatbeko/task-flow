package kg.core.board.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Запрос на создание новой доски")
public record BoardCreateRequest (

        @NotNull
        @Schema(description = "Идентификатор проекта, к которому относится доска") Long projectId,

        @NotBlank
        @Schema(description = "Название доски") String name,

        @Schema(description = "Описание доски") String description
){
}
