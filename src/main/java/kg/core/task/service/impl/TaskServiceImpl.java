package kg.core.task.service.impl;

import kg.core.base.exception.NotFoundException;
import kg.core.base.service.impl.DefaultCrudService;
import kg.core.boardColumn.model.BoardColumn;
import kg.core.boardColumn.repository.BoardColumnRepository;
import kg.core.projectMember.repository.ProjectMemberRepository;
import kg.core.tag.model.Tag;
import kg.core.tag.repository.TagRepository;
import kg.core.task.dtos.UpdateDto;
import kg.core.task.dtos.UpdatePosition;
import kg.core.task.model.Task;
import kg.core.task.repository.TaskRepository;
import kg.core.task.service.TaskService;
import kg.core.user.model.User;
import kg.core.user.repository.UserRepository;
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
    UserRepository userRepository;
    ProjectMemberRepository projectMemberRepository;
    BoardColumnRepository boardColumnRepository;

    public TaskServiceImpl(TaskRepository repository, TagRepository tagRepository,
                           UserRepository userRepository, ProjectMemberRepository projectMemberRepository,
                           BoardColumnRepository boardColumnRepository) {
        super(repository);
        this.repository = repository;
        this.tagRepository = tagRepository;
        this.userRepository = userRepository;
        this.projectMemberRepository = projectMemberRepository;
        this.boardColumnRepository = boardColumnRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Task> findAllByBoardColumnId(Long boardColumnId) {
        return repository.findByBoardColumnIdOrderByPositionAsc(boardColumnId);
    }

    @Override
    @Transactional
    public void updatePurposeTags(Long id, UpdateDto request) {
        Task task = find(id);
        addTagsToTask(task, task.getBoardColumn().getBoard().getProject().getId(), request.idTags());
        repository.save(task);
    }

    @Override
    @Transactional
    public void updatePurposeUsers(Long id, UpdateDto request) {
        Task task = find(id);
        addUsersToTask(task, task.getBoardColumn().getBoard().getProject().getId(), request.idUsers());
        repository.save(task);
    }

    @Override
    @Transactional
    public void replacePurposeTags(Long id, UpdateDto request) {
        Task task = find(id);
        task.getTags().clear();
        addTagsToTask(task, task.getBoardColumn().getBoard().getProject().getId(), request.idTags());
    }

    @Override
    @Transactional
    public void replacePurposeUsers(Long id, UpdateDto request) {
        Task task = find(id);
        task.getAssignees().clear();
        addUsersToTask(task, task.getBoardColumn().getBoard().getProject().getId(), request.idUsers());
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
    @Transactional
    public void delete(Long id) {
        Task task = find(id);
        Long boardColumnId = task.getBoardColumn().getId();
        repository.delete(task);

        List<Task> columnTasks = repository.findByBoardColumnIdOrderByPositionAsc(boardColumnId);

        for (int i = 0; i < columnTasks.size(); i++) {
            columnTasks.get(i).setPosition(i);
        }

        repository.saveAll(columnTasks);
    }

    @Override
    @Transactional
    public void updatePosition(Long id, UpdateDto request) {
        Task task = find(id);
        int oldPosition = task.getPosition();
        int newPosition = request.position() != null ? request.position().intValue() : -1;

        if (newPosition == oldPosition) return;

        List<Task> columnTasks = repository.findByBoardColumnIdOrderByPositionAsc(task.getBoardColumn().getId());
        columnTasks.removeIf(t -> t.getId().equals(task.getId()));

        insertAndRenumber(columnTasks, task, newPosition);
    }

    @Override
    @Transactional
    public void moveTask(Long id, UpdatePosition request) {
        Task task = find(id);
        Long oldColumnId = task.getBoardColumn().getId();
        int newPosition = request.newPosition() != null ? request.newPosition().intValue() : -1;

        List<Task> oldColumnTasks = repository.findByBoardColumnIdOrderByPositionAsc(oldColumnId);
        oldColumnTasks.removeIf(t -> t.getId().equals(task.getId()));

        boolean crossColumn = request.columnId() != null && !request.columnId().equals(oldColumnId);

        if (crossColumn) {
            BoardColumn column = boardColumnRepository.findById(request.columnId())
                    .orElseThrow(() -> new NotFoundException("Колонка с id: " + request.columnId() + " не найдена!"));
            renumberAndSave(oldColumnTasks);
            task.setBoardColumn(column);
        }

        List<Task> columnTasks = crossColumn
                ? repository.findByBoardColumnIdOrderByPositionAsc(request.columnId())
                : oldColumnTasks;

        insertAndRenumber(columnTasks, task, newPosition);
    }

    private void insertAndRenumber(List<Task> tasks, Task task, int position) {
        if (position < 0 || position > tasks.size()) {
            position = tasks.size();
        }

        tasks.add(position, task);
        renumberAndSave(tasks);
    }

    private void renumberAndSave(List<Task> tasks) {
        for (int i = 0; i < tasks.size(); i++) {
            tasks.get(i).setPosition(i);
        }

        repository.saveAll(tasks);
    }

    private void addTagsToTask(Task task, Long projectId, Long[] tagIds) {
        if (tagIds == null) return;
        for (Long tagId : tagIds) {
            Tag tag = tagRepository.findById(tagId)
                    .orElseThrow(() -> new NotFoundException("Тег не найден с id: " + tagId));

            if (!tag.getProject().getId().equals(projectId)) {
                throw new NotFoundException("Тег с id: " + tagId + " не принадлежит проекту");
            }

            task.getTags().add(tag);
        }
    }

    private void addUsersToTask(Task task, Long projectId, Long[] userIds) {
        if (userIds == null) return;
        for (Long userId : userIds) {
            if (!projectMemberRepository.existsByProjectIdAndUserId(projectId, userId)) {
                throw new NotFoundException("Пользователь не найден в проекте с id: " + userId);
            }
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new NotFoundException("Пользователь не найден с id: " + userId));
            task.getAssignees().add(user);
        }
    }

}