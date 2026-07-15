package kg.core.projectMember.dtos;

import kg.core.projectMember.model.ProjectRole;

public record UpdateMemberRoleRequest(
        Long id,
        ProjectRole role
) {
}
