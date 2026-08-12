package kg.core.task.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "DTO для назначения пользователей задаче")
public record UpdateUsersDto(
        @NotNull
        @Schema(description = "Значения ID пользователя") Long[] idUsers
) {
}
