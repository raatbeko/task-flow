package kg.core.projectMember.dtos;

import kg.core.projectMember.model.ProjectRole;

public record InviteProjectMemberRequest(
        String email,
        String username,
        Long projectId,
        ProjectRole role
) {
}
