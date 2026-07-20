package kg.core.task.service.impl;

import kg.core.base.exception.NotFoundException;
import kg.core.base.service.impl.DefaultCrudService;
import kg.core.tag.model.Tag;
import kg.core.tag.repository.TagRepository;
import kg.core.task.dtos.PurposeTags;
import kg.core.task.dtos.UpdatePosition;
import kg.core.task.model.Task;
import kg.core.task.repository.TaskRepository;
import kg.core.task.service.TaskService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TaskServiceImpl extends DefaultCrudService<Task, Long> implements TaskService {

    TaskRepository repository;
    TagRepository tagRepository;

    public TaskServiceImpl(TaskRepository repository, TagRepository tagRepository) {
        super(repository);
        this.repository = repository;
        this.tagRepository = tagRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Task> findAllByBoardColumnId(Long boardColumnId) {
        return repository.findByBoardColumnIdOrderByPositionAsc(boardColumnId);
    }

    @Override
    @Transactional
    public void updatePurpose(Long id, PurposeTags request) {
        Task task = find(id);
        Tag tag = tagRepository.findById(request.purpose())
                .orElseThrow(() -> new NotFoundException("Тег не найден"));
        task.getTags().add(tag);
        repository.save(task);
    }

    @Override
    @Transactional
    public Task save(Task task) {
        if (task.getId() == null) {
            task.setPosition(repository.countByBoardColumnId(task.getBoardColumn().getId()));
        }
        return repository.save(task);
    }

    @Override
    public void delete(Long id) {
        Task task = get(id);
            repository.delete(task);
    }

    @Override
    @Transactional
    public void updatePosition(Long id, UpdatePosition request) {
        Task task = get(id);
        Long boardColumnId = task.getBoardColumn().getId();
        int oldPosition = task.getPosition();
        int newPosition = request.position() != null ? request.position().intValue() : -1;

        if (newPosition == oldPosition) return;

        List<Task> columnTasks = repository.findByBoardColumnIdOrderByPositionAsc(boardColumnId);

        int maxPosition = columnTasks.size() - 1;
        if (newPosition < 0 || newPosition > maxPosition) {
            newPosition = maxPosition;
        }
        if (newPosition == oldPosition) return;

        columnTasks.remove((int) oldPosition);
        columnTasks.add(newPosition, task);

        for (int i = 0; i < columnTasks.size(); i++) {
            columnTasks.get(i).setPosition(i);
        }

        repository.saveAll(columnTasks);
    }

}
