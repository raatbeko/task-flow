package kg.core.boardMember.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kg.core.boardMember.dtos.BoardMemberResponse;
import kg.core.boardMember.dtos.InviteBoardMemberRequest;
import kg.core.boardMember.dtos.UpdateBoardRoleRequest;
import kg.core.boardMember.endpoint.BoardMemberEndpoint;
import kg.core.utils.PathUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping(PathUtils.BOARD_MEMBER)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(
        name = "Project member",
        description = "Управление участниками проекта"
)
@SecurityRequirement(name = "bearer-jwt")
public class BoardMemberController {

    BoardMemberEndpoint endpoint;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Пригласить участника проекта в доску",
            description = "Возвращяет информацию об участнике "
    )
    public BoardMemberResponse invite(@Valid @RequestBody InviteBoardMemberRequest inviteBoardMemberRequest) {
        return endpoint.invite(inviteBoardMemberRequest);
    }

    @PatchMapping("/{memberId}/role")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Обновляет роль участника проект внутри доски",
            description = "Возвращяет информацию об участнике с обновленной ролью"
    )
    public BoardMemberResponse updateRole(@PathVariable Long memberId, @Valid @RequestBody UpdateBoardRoleRequest request){
        return endpoint.updateRole(memberId, request);
    }

    @DeleteMapping("/{memberId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Удалить участника проекта из доски",
            description = "удаляет участника из проекта из доски"
    )
    public void removeMember(@PathVariable Long memberId){
        endpoint.removeMember(memberId);
    }


}
