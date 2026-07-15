package kg.core.projectMember.dtos;

import kg.core.projectMember.model.InvitationStatus;
import kg.core.projectMember.model.ProjectRole;

public record ProjectMemberResponse (
        Long projectId,
        Long userId,
        String username,
        String email,
        ProjectRole role,
        InvitationStatus invitationStatus

){
}
