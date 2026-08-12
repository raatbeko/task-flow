package kg.core.task.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "DTO для назначения тегов задаче")
public record UpdateTagsDto(
        @NotNull
        @Schema(description = "Значения ID тега") Long[] idTags
) {
}
