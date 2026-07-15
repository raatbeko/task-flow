package kg.core.boardMember.dtos;

import kg.core.boardMember.model.BoardRole;

public record UpdateBoardRoleRequest (
        BoardRole role
){
}
