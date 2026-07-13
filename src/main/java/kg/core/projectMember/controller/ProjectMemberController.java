package kg.core.projectMember.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kg.core.projectMember.dtos.InviteMemberRequest;
import kg.core.projectMember.dtos.ProjectMemberResponse;
import kg.core.projectMember.dtos.UpdateMemberRoleRequest;
import kg.core.projectMember.mapper.ProjectMemberMapper;
import kg.core.projectMember.model.RespondInvitationRequest;
import kg.core.projectMember.service.ProjectMemberService;
import kg.core.utils.PathUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping(PathUtils.PROJECT_MEMBER)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(
        name = "Project member",
        description = "Управление участниками проекта"
)
@SecurityRequirement(name = "bearer-jwt")
public class ProjectMemberController {

    ProjectMemberService projectMemberService;
    ProjectMemberMapper projectMemberMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Пригласить участника проекта",
            description = "Возвращяет информацию об участнике "
    )
    public ProjectMemberResponse invite(@Valid @RequestBody InviteMemberRequest request) {
        return projectMemberMapper.toResponse(projectMemberService.invite(request));
    }

    @PatchMapping ("/{memberId}/role")
    @Operation(
            summary = "Обновляет роль участника",
            description = "Возвращяет информацию об участнике с обновленной ролью"
    )
    public ProjectMemberResponse updateRole(@PathVariable Long memberId, UpdateMemberRoleRequest request){
        return projectMemberMapper.toResponse(projectMemberService.updateRole(memberId, request));
    }

    @PatchMapping("/{memberId}/respond")
    @Operation(
            summary = "Ответить на приглащение",
            description = "Возвращяет информацию об участнике и его статус"
    )
    public ProjectMemberResponse respondToInvitation(@PathVariable Long memberId, RespondInvitationRequest request){
        return projectMemberMapper.toResponse(projectMemberService.respondToInvitation(memberId, request));
    }

    @DeleteMapping("/{memberId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Удалить участника проекта",
            description = "удаляет участника из проекта"
    )
    public void removeMember(@PathVariable Long memberId){
        projectMemberService.removeMember(memberId);
    }

    @DeleteMapping("/{projectId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Покинуть проект",
            description = "Текущий пользователь покидает проект, если он не владелец"
    )
    public void leaveProject(@PathVariable Long projectId){
        projectMemberService.leaveProject(projectId);
    }
}
