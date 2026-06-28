package kg.core.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import kg.core.user.dtos.UserResponse;
import kg.core.user.mapper.UserMapper;
import kg.core.user.model.User;
import kg.core.utils.PathUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping(PathUtils.USERS)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(
        name = "Users",
        description = "Получение данных пользователя"
)
@SecurityRequirement(name = "bearer-jwt")
public class UserController {

    UserMapper userMapper;

    @GetMapping("/me")
    @Operation(
            summary = "Получить текущего пользователя",
            description = "Возвращает данные авторизованного пользователя"
    )
    public UserResponse getCurrentUser(@AuthenticationPrincipal User currentUser) {
        return userMapper.toResponse(currentUser);
    }
}