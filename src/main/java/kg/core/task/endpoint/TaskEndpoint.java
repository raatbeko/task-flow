package kg.core.task.endpoint;

import jakarta.validation.Valid;
import kg.core.task.dtos.PurposeTags;
import kg.core.task.dtos.TaskDto;
import kg.core.task.dtos.UpdatePosition;

import java.util.List;

public interface TaskEndpoint {

    TaskDto get(Long id);

    List<TaskDto> getAll(Long boardColumnId);

    TaskDto create(TaskDto dto);

    TaskDto update(Long id, TaskDto dto);

    UpdatePosition changePosition(Long id, @Valid UpdatePosition request);

    PurposeTags purposeTags(Long id, @Valid PurposeTags request);
}
