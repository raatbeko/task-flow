package kg.core.user.controller;

import jakarta.validation.Valid;
import kg.core.auth.dtos.CompleteProfileRequest;
import kg.core.user.model.User;
import kg.core.user.service.ProfileService;
import kg.core.utils.PathUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@Validated
@RestController
@RequestMapping(PathUtils.USERS)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Profile", description = "Управление профилем пользователя")
@SecurityRequirement(name = "bearer-jwt")
public class ProfileController {

    ProfileService profileService;

    @PatchMapping("/phone")
    @Operation(
            summary = "Обновить номер телефона",
            description = "Обновляет номер телефона текущего пользователя"
    )
    public void updatePhone(
            @AuthenticationPrincipal User currentUser,
            @Valid @RequestBody CompleteProfileRequest request
    ) {
        profileService.updatePhone(currentUser, request);
    }
}
