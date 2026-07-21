package kg.core.task.endpoint;

import jakarta.validation.Valid;
import kg.core.task.dtos.TaskDto;
import kg.core.task.dtos.UpdateDto;

import java.util.List;

public interface TaskEndpoint {

    TaskDto get(Long id);

    List<TaskDto> getAll(Long boardColumnId);

    TaskDto create(TaskDto dto);

    TaskDto update(Long id, TaskDto dto);

    UpdateDto changePosition(Long id, @Valid UpdateDto request);

    UpdateDto purposeTags(Long id, @Valid UpdateDto request);

    UpdateDto purposeUsers(Long id, @Valid UpdateDto request);
}
