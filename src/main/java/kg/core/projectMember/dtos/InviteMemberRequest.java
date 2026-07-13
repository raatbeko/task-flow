package kg.core.projectMember.dtos;

import kg.core.projectMember.model.Role;

public record InviteMemberRequest(
        String email,
        String username,
        Long projectId,
        Role role
) {
}
