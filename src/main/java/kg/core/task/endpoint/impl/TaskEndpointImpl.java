package kg.core.task.endpoint.impl;

import kg.core.task.dtos.TaskDto;
import kg.core.task.dtos.TaskSearchRequest;
import kg.core.task.dtos.UpdatePosition;
import kg.core.task.endpoint.TaskEndpoint;
import kg.core.task.mapper.TaskMapper;
import kg.core.task.model.Task;
import kg.core.task.model.TaskDocument;
import kg.core.task.repository.TaskSearchRepository;
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
    TaskSearchRepository searchRepository;

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

        TaskDocument document = mapper.toDocument(task);
        searchRepository.save(document);

        return mapper.toDto(task);
    }

    @Override
    public TaskDto update(Long id, TaskDto dto) {
        Task task = service.find(id);
        service.save(mapper.update(dto, task));
        return mapper.toDto(task);
    }

    @Override
    public UpdatePosition changePosition(Long id, UpdatePosition request) {
        service.updatePosition(id, request);
        return request;
    }

    @Override
    public List<TaskDto> search(TaskSearchRequest request) {
        List<TaskDocument> documents = service.search(request.query());
        return mapper.documentsToDtos(documents);
    }

}
