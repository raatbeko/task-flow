package kg.core.boardMember.service;

import kg.core.base.service.CrudService;
import kg.core.boardMember.model.BoardMember;
import kg.core.boardMember.model.BoardRole;

public interface BoardMemberService extends CrudService<BoardMember, Long> {

    BoardMember invite(Long memberId, Long boardId, String email, BoardRole role);

    BoardMember updateRole(Long memberId, BoardRole role);

    void removeMember(Long memberId);


}
