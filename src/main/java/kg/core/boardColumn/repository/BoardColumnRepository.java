package kg.core.boardColumn.repository;

import kg.core.base.search.BaseRepository;
import kg.core.boardColumn.model.BoardColumn;
import kg.core.task.model.Task;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardColumnRepository extends BaseRepository<BoardColumn, Long> {

    int countByBoardId(Long id);
    List<BoardColumn> findByBoardIdOrderByPositionAsc(Long boardId);
}
