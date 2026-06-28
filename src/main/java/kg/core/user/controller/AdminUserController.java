package kg.core.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import kg.core.annotation.SearchParams;
import kg.core.base.search.PageResponse;
import kg.core.user.dtos.UpdateUserRequest;
import kg.core.user.dtos.UserResponse;
import kg.core.user.search.UserSearchRequest;
import kg.core.user.service.AdminUserService;
import kg.core.utils.PathUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping(PathUtils.ADMIN_USERS)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(
        name = "Admin Users",
        description = "Админский доступ к данным пользователей"
)
@SecurityRequirement(name = "bearer-jwt")
public class AdminUserController {

    AdminUserService adminUserService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/search")
    @Operation(
            summary = "Поиск пользователей",
            description = "Возвращает страницу пользователей с фильтрацией по всем основным полям"
    )
    public PageResponse<UserResponse> search(@SearchParams UserSearchRequest searchRequest) {
        return adminUserService.search(searchRequest);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/{id}")
    @Operation(
            summary = "Получить пользователя по ID",
            description = "Возвращает данные пользователя по его идентификатору"
    )
    public UserResponse getById(@PathVariable Long id) {
        return adminUserService.getById(id);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/{id}")
    @Operation(
            summary = "Редактировать пользователя",
            description = "Редактирование полей пользователя (email, username, phone, роль, провайдер)"
    )
    public UserResponse edit(
            @PathVariable Long id,
            @RequestBody UpdateUserRequest request
    ) {
        return adminUserService.edit(id, request);
    }
}
