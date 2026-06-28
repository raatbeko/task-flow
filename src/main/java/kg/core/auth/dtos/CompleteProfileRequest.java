package kg.core.auth.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Запрос на дополнение профиля")
public record CompleteProfileRequest(
        @NotBlank
        @Pattern(regexp = "^\\+?\\d{7,15}$", message = "Invalid phone number format")
        @Schema(description = "Номер телефона Кыргызстана", example = "+996700123456") String phoneNumber
) {
}
