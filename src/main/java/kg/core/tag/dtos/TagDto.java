package kg.core.tag.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "DTO ответа с данными тега")
public record TagDto(
        @NotNull
        @Schema(description = "ID проекта") Long projectId,

        @NotBlank @Size(max = 50)
        @Schema(description = "Название тега") String name,

        @NotBlank @Size(max = 50)
        @Schema(description = "Цвет тега") String color
) {
}