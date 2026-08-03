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

    UpdatePositionDto changePosition(Long id, @Valid UpdatePositionDto request);
    UpdateTagsDto purposeTags(Long id, @Valid UpdateTagsDto request);
    UpdateUsersDto purposeUsers(Long id, @Valid UpdateUsersDto request);
    UpdateTagsDto replacePurposeTags(Long id, @Valid UpdateTagsDto request);
    UpdateUsersDto replacePurposeUsers(Long id, @Valid UpdateUsersDto request);
}