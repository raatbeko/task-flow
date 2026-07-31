package kg.core.task.repository;

import kg.core.base.search.BaseRepository;
import kg.core.task.model.Task;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends BaseRepository<Task, Long> {
    List<Task> findByBoardColumnIdOrderByPositionAsc(Long boardColumnId);

    int countByBoardColumnId(Long boardColumnId);

    @Modifying
    @Query("delete from Task t where t.boardColumn.id in (select bc.id from BoardColumn bc where bc.board.id = :boardId)")
    void deleteByBoardId(@Param("boardId") Long boardId);
}