package kg.core.projectMember.dtos;

import kg.core.projectMember.model.InvitationStatus;
import kg.core.projectMember.model.Role;

public record ProjectMemberResponse (
        Long projectId,
        Long userId,
        String username,
        String email,
        Role role,
        InvitationStatus invitationStatus

){
}
