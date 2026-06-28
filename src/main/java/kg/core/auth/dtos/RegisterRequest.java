package kg.core.auth.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Запрос на регистрацию пользователя")
public record RegisterRequest(
        @NotBlank
        @Email
        @Schema(description = "Email пользователя", example = "user@example.com") String email,

        @NotBlank
        @Size(min = 3, max = 50)
        @Schema(description = "Имя пользователя", example = "john_doe") String username,

        @NotBlank
        @Pattern(regexp = "^\\+?\\d{7,15}$", message = "Invalid phone number format")
        @Schema(description = "Номер телефона Кыргызстана", example = "+996700123456") String phoneNumber,

        @NotBlank
        @Size(min = 6, max = 100)
        @Schema(description = "Пароль пользователя", example = "P@ssw0rd") String password
) {
}
