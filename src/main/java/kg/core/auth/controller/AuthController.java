package kg.core.auth.controller;

import jakarta.validation.Valid;
import kg.core.auth.dtos.AuthResponse;
import kg.core.auth.dtos.LoginRequest;
import kg.core.auth.dtos.RefreshTokenRequest;
import kg.core.auth.dtos.RegisterRequest;
import kg.core.auth.service.AuthService;
import kg.core.utils.PathUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Validated
@RestController
@RequestMapping(PathUtils.AUTH)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(
        name = "Auth",
        description = "Аутентификация и управление токенами"
)
public class AuthController {

    AuthService authService;

    @PostMapping("/register")
    @Operation(
            summary = "Регистрация нового пользователя",
            description = "Регистрирует пользователя по email, имени, телефону и паролю"
    )
    public AuthResponse register(@Valid @RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    @Operation(
            summary = "Вход",
            description = "Аутентифицирует пользователя и возвращает JWT access/refresh токены"
    )
    public AuthResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @PostMapping("/refresh")
    @Operation(
            summary = "Обновление access-токена",
            description = "Обновляет JWT access-токен с помощью refresh-токена"
    )
    public AuthResponse refresh(@Valid @RequestBody RefreshTokenRequest request) {
        return authService.refresh(request.refreshToken());
    }
}
