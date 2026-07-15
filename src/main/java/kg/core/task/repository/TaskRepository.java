package kg.core.task.repository;

import kg.core.base.search.BaseRepository;
import kg.core.task.model.Task;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends BaseRepository<Task, Long> {
    List<Task> findByBoardColumnIdOrderByPositionAsc(Long boardColumnId);

    int countByBoardColumnId(Long boardColumnId);
}
