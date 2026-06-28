package kg.core.auth.dtos;

import jakarta.validation.constraints.NotBlank;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Запрос на обновление токена")
public record RefreshTokenRequest(
        @NotBlank
        @Schema(description = "Refresh-токен") String refreshToken
) {
}
