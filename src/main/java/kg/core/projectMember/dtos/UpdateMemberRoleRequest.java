package kg.core.projectMember.dtos;

import kg.core.projectMember.model.Role;

public record UpdateMemberRoleRequest(
        Role role
) {
}
