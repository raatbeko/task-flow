package kg.core.task.endpoint.impl;

import kg.core.task.dtos.*;
import kg.core.task.endpoint.TaskEndpoint;
import kg.core.task.mapper.TaskMapper;
import kg.core.task.model.Task;
import kg.core.task.service.TaskService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TaskEndpointImpl implements TaskEndpoint {

    TaskMapper mapper;
    TaskService service;

    @Override
    public TaskDto get(Long id) {
        Task response =  service.find(id);
        return mapper.toDto(response);
    }

    @Override
    public List<TaskDto> getAll(Long boardColumnId) {
        List<Task> response =  service.findAllByBoardColumnId(boardColumnId);
        return mapper.toDtos(response);
    }

    @Override
    public TaskDto create(TaskDto dto) {
        Task task = mapper.toEntity(dto);
        service.save(task);

        Long projectId = task.getBoardColumn().getBoard().getProject().getId();

        if (dto.assignees() != null && !dto.assignees().isEmpty()) {
            service.addUsersToTask(task, projectId, dto.assignees().toArray(new Long[0]));
        }

        if (dto.tags() != null) {
            service.addTagsToTask(task, projectId, dto.tags());
        }

        service.save(task);
        return mapper.toDto(task);
    }

    @Override
    public TaskDto update(Long id, TaskDto dto) {
        Task task = service.find(id);
        service.save(mapper.update(dto, task));
        return mapper.toDto(task);
    }

    @Override
    public UpdatePositionDto changePosition(Long id, UpdatePositionDto request) {
        service.updatePosition(id, request);
        return request;
    }

    @Override
    public UpdateTagsDto purposeTags(Long id, UpdateTagsDto request) {
        service.updatePurposeTags(id, request);
        return request;
    }

    @Override
    public UpdateUsersDto purposeUsers(Long id, UpdateUsersDto request) {
        service.updatePurposeUsers(id, request);
        return request;
    }

    @Override
    public UpdateTagsDto replacePurposeTags(Long id, UpdateTagsDto request) {
        service.replacePurposeTags(id, request);
        return request;
    }

    @Override
    public UpdateUsersDto replacePurposeUsers(Long id, UpdateUsersDto request) {
        service.replacePurposeUsers(id, request);
        return request;
    }

    @Override
    public void delete(Long id) {
        service.delete(id);
    }

}
