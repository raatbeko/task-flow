package kg.core.boardMember.repository;

import kg.core.base.search.BaseRepository;
import kg.core.boardMember.model.BoardMember;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardMemberRepository extends BaseRepository<BoardMember, Long> {
}
