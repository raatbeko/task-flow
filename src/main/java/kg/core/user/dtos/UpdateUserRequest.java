package kg.core.user.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import kg.core.user.model.AuthProvider;
import kg.core.user.model.Role;

@Schema(description = "Запрос на обновление данных пользователя (для ADMIN)")
public record UpdateUserRequest(
        @Schema(description = "Новый email пользователя") String email,
        @Schema(description = "Новое имя пользователя") String username,
        @Schema(description = "Новый номер телефона пользователя") String phoneNumber,
        @Schema(description = "Новая роль пользователя") Role role,
        @Schema(description = "Новый провайдер аутентификации") AuthProvider provider
) {
}
