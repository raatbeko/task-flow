package kg.core.taskAssignee.repository;

import kg.core.base.search.BaseRepository;
import kg.core.taskAssignee.model.TaskAssignee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskAssigneeRepository extends BaseRepository<TaskAssignee, Long> {
}
