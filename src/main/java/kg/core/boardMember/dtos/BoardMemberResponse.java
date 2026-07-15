package kg.core.boardMember.dtos;

import kg.core.boardMember.model.BoardRole;

public record BoardMemberResponse (
        Long boardId,
        Long projectMemberId,
        Long userId,
        String username,
        String email,
        BoardRole role
){
}