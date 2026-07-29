package kg.core.project.dtos;


import io.swagger.v3.oas.annotations.media.Schema;
import kg.core.project.model.ProjectStatus;


public record ProjectResponse(
        @Schema(description = "Название проекта") String name,
        @Schema(description = "Описание проекта") String description,
        @Schema(description = "владелец проекта") Long ownerId,
        @Schema(description = "Статус проекта") ProjectStatus status
) {

}
