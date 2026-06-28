package kg.core.auth.dtos;

import jakarta.validation.constraints.NotBlank;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Запрос на вход")
public record LoginRequest(
        @NotBlank
        @Schema(description = "Email или username пользователя", example = "user@example.com") String identifier,

        @NotBlank
        @Schema(description = "Пароль пользователя", example = "P@ssw0rd") String password
) {
}
