package kg.core.auth.controller;

import kg.core.auth.dtos.AuthResponse;
import kg.core.security.jwt.JwtService;
import kg.core.user.model.User;
import kg.core.user.repository.UserRepository;
import kg.core.utils.PathUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping(PathUtils.AUTH_OAUTH_2 + "/oauth2")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "OAuth2", description = "Колбэки OAuth2-логина")
public class OAuth2SuccessController {

    UserRepository userRepository;
    JwtService jwtService;

    @GetMapping("/success")
    @Operation(summary = "Успешный вход через OAuth2", description = "Возвращает JWT-токены для пользователя, успешно аутентифицированного через OAuth2")
    public AuthResponse success(@AuthenticationPrincipal OAuth2User principal) {
        String email = principal.getAttribute("email");
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("User not found after OAuth2 login"));

        String accessToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        return new AuthResponse(accessToken, refreshToken, user.getId(), user.getEmail(), user.getUsername());
    }
}
