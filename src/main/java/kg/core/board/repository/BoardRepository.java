package kg.core.board.repository;

import kg.core.base.search.BaseRepository;
import kg.core.board.model.Board;
import kg.core.boardColumn.model.BoardColumn;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardRepository extends BaseRepository<Board, Long> {
    int countByProjectId(Long projectId);
    List<Board> findByProjectIdOrderByPositionAsc(Long projectId);

    @Modifying
    @Query("delete from Board b where b.project.id = :projectId")
    void deleteByProjectId(@Param("projectId") Long projectId);

    @Query("select b.id from Board b where b.project.id = :projectId")
    List<Long> findIdsByProjectId(@Param("projectId") Long projectId);

    @Query("select coalesce(max(bc.position), -1) + 1 from BoardColumn bc where bc.board.id = :boardId")
    Integer findNextPosition(@Param("boardId") Long boardId);
}
