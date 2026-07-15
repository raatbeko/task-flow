package kg.core.boardMember.endpoint;

import kg.core.boardMember.dtos.BoardMemberResponse;
import kg.core.boardMember.dtos.InviteBoardMemberRequest;
import kg.core.boardMember.dtos.UpdateBoardRoleRequest;

public interface BoardMemberEndpoint {

    BoardMemberResponse invite(InviteBoardMemberRequest inviteBoardMemberRequest);
    BoardMemberResponse updateRole(Long id, UpdateBoardRoleRequest updateBoardRoleRequest);
    void removeMember(Long id);
}
