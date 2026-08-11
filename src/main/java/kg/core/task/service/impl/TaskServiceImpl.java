package kg.core.task.service.impl;

import kg.core.base.exception.ConflictException;
import kg.core.base.exception.NotFoundException;
import kg.core.base.service.impl.DefaultCrudService;
import kg.core.board.model.Board;
import kg.core.board.model.BoardStatus;
import kg.core.boardMember.model.BoardRole;
import kg.core.project.model.Project;
import kg.core.project.model.ProjectStatus;
import kg.core.projectMember.repository.ProjectMemberRepository;
import kg.core.security.validator.AccessGuard;
import kg.core.tag.model.Tag;
import kg.core.tag.repository.TagRepository;
import kg.core.task.dtos.UpdatePositionDto;
import kg.core.task.dtos.UpdateTagsDto;
import kg.core.task.dtos.UpdateUsersDto;
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
    AccessGuard accessGuard;

    public TaskServiceImpl(TaskRepository repository, TagRepository tagRepository,
                           UserRepository userRepository, ProjectMemberRepository projectMemberRepository, AccessGuard accessGuard) {
        super(repository);
        this.repository = repository;
        this.tagRepository = tagRepository;
        this.userRepository = userRepository;
        this.projectMemberRepository = projectMemberRepository;
        this.accessGuard = accessGuard;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Task> findAllByBoardColumnId(Long boardColumnId) {
        return repository.findByBoardColumnIdOrderByPositionAsc(boardColumnId);
    }

    @Override
    @Transactional
    public void updatePurposeTags(Long id, UpdateTagsDto request) {
        Task task = find(id);

        Board board = task.getBoardColumn().getBoard();
        accessGuard.requireBoardRole(board.getId(), board.getProject().getId(), BoardRole.EDITOR);

        addTagsToTask(task, task.getBoardColumn().getBoard().getProject().getId(), request.idTags());
        repository.save(task);
    }

    @Override
    @Transactional
    public void updatePurposeUsers(Long id, UpdateUsersDto request) {
        Task task = find(id);

        Board board = task.getBoardColumn().getBoard();
        accessGuard.requireBoardRole(board.getId(), board.getProject().getId(), BoardRole.EDITOR);

        addUsersToTask(task, task.getBoardColumn().getBoard().getProject().getId(), request.idUsers());
        repository.save(task);
    }

    @Override
    @Transactional
    public void replacePurposeTags(Long id, UpdateTagsDto request) {
        Task task = find(id);

        Board board = task.getBoardColumn().getBoard();
        accessGuard.requireBoardRole(board.getId(), board.getProject().getId(), BoardRole.EDITOR);

        task.getTags().clear();
        addTagsToTask(task, task.getBoardColumn().getBoard().getProject().getId(), request.idTags());
    }

    @Override
    @Transactional
    public void replacePurposeUsers(Long id, UpdateUsersDto request) {
        Task task = find(id);

        Board board = task.getBoardColumn().getBoard();
        accessGuard.requireBoardRole(board.getId(), board.getProject().getId(), BoardRole.EDITOR);

        task.getAssignees().clear();
        addUsersToTask(task, task.getBoardColumn().getBoard().getProject().getId(), request.idUsers());
    }

    @Override
    @Transactional
    public Task save(Task task) {

        Project project = task.getBoardColumn().getBoard().getProject();

        if (project.getStatus() == ProjectStatus.ARCHIVED) {
            throw new ConflictException("Проект заархивирован, действие недоступно");
        }

        if (task.getBoardColumn().getBoard().getStatus() == BoardStatus.ARCHIVED) {
            throw new ConflictException("Доска заархивирована, действие недоступно");
        }

        Board board = task.getBoardColumn().getBoard();
        accessGuard.requireBoardRole(board.getId(), board.getProject().getId(), BoardRole.EDITOR);

        if (task.getId() == null) {
            task.setPosition(repository.findNextPosition(task.getBoardColumn().getId()));
        }
        return repository.save(task);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Task task = find(id);

        Long boardColumnId = task.getBoardColumn().getId();
        repository.delete(task);

        List<Task> remaining = repository.findByBoardColumnIdOrderByPositionAsc(boardColumnId);
        for (int i = 0; i < remaining.size(); i++) {
            remaining.get(i).setPosition(i);
        }
        repository.saveAll(remaining);

    }

    @Override
    @Transactional
    public void updatePosition(Long id, UpdatePositionDto request) {
        Task task = find(id);
        Long boardColumnId = task.getBoardColumn().getId();

        Board board = task.getBoardColumn().getBoard();
        accessGuard.requireBoardRole(board.getId(), board.getProject().getId(), BoardRole.EDITOR);

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

    public void addTagsToTask(Task task, Long projectId, Long[] tagIds) {
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

    public void addUsersToTask(Task task, Long projectId, Long[] userIds) {
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