package kg.core.boardMember.repository;

import  kg.core.base.search.BaseRepository;
import kg.core.boardMember.model.BoardMember;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

@Repository
public interface BoardMemberRepository extends BaseRepository<BoardMember, Long> {

    Optional<BoardMember> findByBoardIdAndProjectMemberUserId(Long boardId, Long userId);

    @Modifying
    @Query("delete from BoardMember bm where bm.board.id = :boardId")
    void deleteByBoardId(@Param("boardId") Long boardId);
}
