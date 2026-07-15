package kg.core.projectMember.dtos;

import kg.core.projectMember.model.InvitationStatus;

public record RespondInvitationRequest(
        InvitationStatus status
) {
}
