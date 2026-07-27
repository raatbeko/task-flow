package kg.core.task.service;

import kg.core.base.service.CrudService;
import kg.core.task.dtos.TaskDto;
import kg.core.task.dtos.UpdatePosition;
import kg.core.task.model.Priority;
import kg.core.task.model.Task;
import kg.core.task.model.TaskDocument;

import java.util.List;

public interface TaskService extends CrudService<Task, Long> {

    void delete(Long id);

    void updatePosition(Long id, UpdatePosition request);

    List<Task> findAllByBoardColumnId(Long boardColumnId);

    List<TaskDocument> search(String query);
}
