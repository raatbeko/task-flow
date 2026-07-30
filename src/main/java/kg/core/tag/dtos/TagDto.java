package kg.core.tag.dtos;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO ответа с данными тега")
public record TagDto(
        @Schema(description = "ID проекта") Long projectId,
        @Schema(description = "Название тега") String name,
        @Schema(description = "Цвет тега") String color
) {
}