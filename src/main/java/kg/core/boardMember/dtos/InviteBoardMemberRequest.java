package kg.core.boardMember.dtos;

import kg.core.boardMember.model.BoardRole;

public record InviteBoardMemberRequest(
        Long boardId,
        Long projectMemberId,
        BoardRole role,
        String email,
        String username
) {
}
