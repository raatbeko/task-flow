package kg.core.project.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import kg.core.user.model.AuthProvider;
import kg.core.user.model.Role;

import java.time.LocalDateTime;

@Schema(description = "DTO ответа с данными пользователя")
public record ProjectResponse(
                @Schema(description = "ID пользователя") Long id,
        @Schema(description = "Дата создания записи") LocalDateTime createdAt,
        @Schema(description = "Дата обновления записи") LocalDateTime updatedAt,
        @Schema(description = "Email пользователя") String email,
        @Schema(description = "Имя пользователя") String username,
        @Schema(description = "Номер телефона пользователя") String phoneNumber,
        @Schema(description = "Роль пользователя") Role role,
        @Schema(description = "Провайдер аутентификации") AuthProvider provider
) {
}
