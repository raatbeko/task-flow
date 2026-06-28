package kg.core.auth.dtos;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Ответ аутентификации с JWT-токенами и базовой информацией о пользователе")
public record AuthResponse(
        @Schema(description = "JWT access-токен") String accessToken,
        @Schema(description = "JWT refresh-токен") String refreshToken,
        @Schema(description = "Тип токена, всегда 'Bearer'") String tokenType,
        @Schema(description = "Идентификатор пользователя") Long userId,
        @Schema(description = "Email пользователя") String email,
        @Schema(description = "Имя пользователя") String username
) {
    public AuthResponse(String accessToken, String refreshToken, Long userId, String email, String username) {
        this(accessToken, refreshToken, "Bearer", userId, email, username);
    }
}
