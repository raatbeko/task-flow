package kg.core.task.endpoint;

import jakarta.validation.Valid;
import kg.core.task.dtos.TaskDto;
import kg.core.task.dtos.UpdateDto;
import kg.core.task.dtos.UpdatePosition;

import java.util.List;

public interface TaskEndpoint {

    TaskDto get(Long id);

    List<TaskDto> getAll(Long boardColumnId);

    TaskDto create(TaskDto dto);

    TaskDto update(Long id, TaskDto dto);

    UpdateDto changePosition(Long id, @Valid UpdateDto request);

    UpdateDto purposeTags(Long id, @Valid UpdateDto request);

    UpdateDto purposeUsers(Long id, @Valid UpdateDto request);

    UpdateDto replacePurposeTags(Long id, @Valid UpdateDto request);

    UpdateDto replacePurposeUsers(Long id, @Valid UpdateDto request);

    void delete(Long id);

    TaskDto duplicate(Long id);

    UpdateDto move(Long id, @Valid UpdatePosition request);
}