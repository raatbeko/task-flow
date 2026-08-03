package kg.core.task.service;

import kg.core.base.service.CrudService;
import kg.core.task.dtos.UpdateDto;
import kg.core.task.dtos.UpdatePosition;
import kg.core.task.model.Task;

import java.util.List;

public interface TaskService extends CrudService<Task, Long> {

    void delete(Long id);

    void updatePosition(Long id, UpdateDto request);

    void moveTask(Long id, UpdatePosition request);

    List<Task> findAllByBoardColumnId(Long boardColumnId);

    void updatePurposeTags(Long id, UpdateDto request);

    void updatePurposeUsers(Long id, UpdateDto request);

    void replacePurposeTags(Long id, UpdateDto request);

    void replacePurposeUsers(Long id, UpdateDto request);

    Task duplicate(Long id);
}
