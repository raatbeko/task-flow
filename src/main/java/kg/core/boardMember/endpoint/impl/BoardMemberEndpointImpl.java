package kg.core.boardMember.endpoint.impl;

import kg.core.boardMember.dtos.BoardMemberResponse;
import kg.core.boardMember.dtos.InviteBoardMemberRequest;
import kg.core.boardMember.dtos.UpdateBoardRoleRequest;
import kg.core.boardMember.endpoint.BoardMemberEndpoint;
import kg.core.boardMember.mapper.BoardMemberMapper;
import kg.core.boardMember.model.BoardMember;
import kg.core.boardMember.service.BoardMemberService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BoardMemberEndpointImpl implements BoardMemberEndpoint {

    BoardMemberService boardMemberService;
    BoardMemberMapper boardMemberMapper;


    @Override
    public BoardMemberResponse invite(InviteBoardMemberRequest inviteBoardMemberRequest) {
        BoardMember boardMember = boardMemberService.invite(inviteBoardMemberRequest.projectMemberId(),
                inviteBoardMemberRequest.boardId(), inviteBoardMemberRequest.email(),
                inviteBoardMemberRequest.role());

        return boardMemberMapper.toResponse(boardMember);
    }

    @Override
    public BoardMemberResponse updateRole(Long id, UpdateBoardRoleRequest updateBoardRoleRequest) {
        BoardMember boardMember = boardMemberService.updateRole(id, updateBoardRoleRequest.role());
        return boardMemberMapper.toResponse(boardMember);
    }

    @Override
    public void removeMember(Long id) {
        boardMemberService.removeMember(id);
    }
}
