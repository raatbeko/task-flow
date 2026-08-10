package kg.core.boardMember.repository;

import  kg.core.base.search.BaseRepository;
import kg.core.boardMember.model.BoardMember;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BoardMemberRepository extends BaseRepository<BoardMember, Long> {

    Optional<BoardMember> findByBoardIdAndProjectMemberUserId(Long boardId, Long userId);
}
