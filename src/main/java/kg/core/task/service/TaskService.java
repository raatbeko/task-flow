package kg.core.task.service;

import kg.core.base.service.CrudService;
import kg.core.task.dtos.UpdatePositionDto;
import kg.core.task.dtos.UpdateTagsDto;
import kg.core.task.dtos.UpdateUsersDto;
import kg.core.task.model.Task;

import java.util.List;

public interface TaskService extends CrudService<Task, Long> {

    void delete(Long id);

    List<Task> findAllByBoardColumnId(Long boardColumnId);

    void updatePosition(Long id, UpdatePositionDto request);

    void updatePurposeTags(Long id, UpdateTagsDto request);

    void updatePurposeUsers(Long id, UpdateUsersDto request);

    void replacePurposeTags(Long id, UpdateTagsDto request);

    void replacePurposeUsers(Long id, UpdateUsersDto request);
}
