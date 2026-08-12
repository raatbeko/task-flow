package kg.core.boardColumn.repository;

import kg.core.base.search.BaseRepository;
import kg.core.boardColumn.model.BoardColumn;
import kg.core.task.model.Task;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardColumnRepository extends BaseRepository<BoardColumn, Long> {

    List<BoardColumn> findByBoardIdOrderByPositionAsc(Long boardId);
    @Modifying
    @Query("delete from BoardColumn bc where bc.board.id = :boardId")
    void deleteByBoardId(@Param("boardId") Long boardId);

    @Query("select coalesce(max(bc.position), -1) + 1 from BoardColumn bc where bc.board.id = :boardId")
    Integer findNextPosition(@Param("boardId") Long boardId);
}
