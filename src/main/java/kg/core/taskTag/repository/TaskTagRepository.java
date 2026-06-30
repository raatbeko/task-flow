package kg.core.taskTag.repository;

import kg.core.base.search.BaseRepository;
import kg.core.taskTag.model.TaskTag;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskTagRepository extends BaseRepository<TaskTag, Long> {
}
