package kg.core.task.endpoint;

import jakarta.validation.Valid;
import kg.core.task.dtos.*;

import java.util.List;

public interface TaskEndpoint {

    TaskDto get(Long id);

    void delete(Long id);

    List<TaskDto> getAll(Long boardColumnId);

    TaskDto create(TaskDto dto);

    TaskDto update(Long id, TaskDto dto);

    TaskDto changePosition(Long id, @Valid UpdatePositionDto request);
    TaskDto purposeTags(Long id, @Valid UpdateTagsDto request);
    TaskDto purposeUsers(Long id, @Valid UpdateUsersDto request);
    TaskDto replacePurposeTags(Long id, @Valid UpdateTagsDto request);
    TaskDto replacePurposeUsers(Long id, @Valid UpdateUsersDto request);
}