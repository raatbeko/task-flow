package kg.core.project.dtos;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO ответа с данными проекта")
public record ProjectDto(
        @Schema(description = "Название проекта") String name,
        @Schema(description = "Описание проекта") String description
) {
}
