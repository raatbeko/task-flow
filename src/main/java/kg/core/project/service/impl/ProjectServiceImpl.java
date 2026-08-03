package kg.core.project.service.impl;

import kg.core.base.exception.ConflictException;
import kg.core.base.exception.NotFoundException;
import kg.core.base.service.impl.DefaultCrudService;
import kg.core.project.model.Project;
import kg.core.project.model.ProjectStatus;
import kg.core.project.repository.ProjectRepository;
import kg.core.project.service.ProjectService;
import kg.core.projectMember.model.InvitationStatus;
import kg.core.projectMember.model.ProjectMember;
import kg.core.projectMember.model.ProjectRole;
import kg.core.projectMember.repository.ProjectMemberRepository;
import kg.core.user.model.User;
import kg.core.utils.UserProvider;
import kg.core.board.model.Board;
import kg.core.board.repository.BoardRepository;
import kg.core.boardColumn.model.BoardColumn;
import kg.core.boardColumn.repository.BoardColumnRepository;
import kg.core.task.model.Task;
import kg.core.task.repository.TaskRepository;
import kg.core.tag.model.Tag;
import kg.core.tag.repository.TagRepository;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProjectServiceImpl extends DefaultCrudService<Project, Long> implements ProjectService {

    ProjectRepository repository;
    UserProvider userProvider;
    ProjectMemberRepository projectMemberRepository;
    BoardRepository boardRepository;
    BoardColumnRepository boardColumnRepository;
    TaskRepository taskRepository;
    TagRepository tagRepository;

    public ProjectServiceImpl(ProjectRepository repository, UserProvider userProvider,
                              ProjectMemberRepository projectMemberRepository,
                              BoardRepository boardRepository, BoardColumnRepository boardColumnRepository,
                              TaskRepository taskRepository, TagRepository tagRepository) {
        super(repository);
        this.repository = repository;
        this.userProvider = userProvider;
        this.projectMemberRepository = projectMemberRepository;
        this.boardRepository = boardRepository;
        this.boardColumnRepository = boardColumnRepository;
        this.taskRepository = taskRepository;
        this.tagRepository = tagRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Project> findAll() {
        User currentUser = userProvider.getCurrentUser();
        return repository.findAllByOwner(currentUser);
    }

    @Override
    @Transactional(readOnly = true)
    public Project find(Long id) {
        Project project = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Проект с id: " + id + " не найден!"));
        User currentUser = userProvider.getCurrentUser();
        if (!project.getOwner().getId().equals(currentUser.getId())) {
            throw new NotFoundException("Проект с id: " + id + " не найден!");
        }
        return project;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Project project = find(id);
        if (project.getStatus() == ProjectStatus.ACTIVE) {
            repository.delete(project);
        } else {
            throw new ConflictException("Проект с id " + id + " заархивирован. Сначала восстановите проект.");
        }
    }

    @Override
    @Transactional
    public void archive(Long id) {
        Project project = find(id);
        project.setStatus(ProjectStatus.ARCHIVED);
        repository.save(project);
    }

    @Override
    @Transactional
    public void unarchive(Long id) {
        Project project = find(id);
        project.setStatus(ProjectStatus.ACTIVE);
        repository.save(project);
    }

    @Override
    @Transactional
    public Project create(Project project) {

        Project newProject = save(project);

        ProjectMember projectMember = new ProjectMember();
        projectMember.setProject(newProject);
        projectMember.setUser(userProvider.getCurrentUser());
        projectMember.setInvitationStatus(InvitationStatus.ACCEPTED);
        projectMember.setRole(ProjectRole.OWNER);
        projectMemberRepository.save(projectMember);

        return newProject;
    }

    @Override
    @Transactional
    public Project duplicate(Long id) {
        Project originalProject = find(id);

        Project copyProject = new Project();
        copyProject.setName(originalProject.getName());
        copyProject.setDescription(originalProject.getDescription());
        copyProject.setOwner(originalProject.getOwner());
        copyProject.setStatus(ProjectStatus.ACTIVE);

        Project savedProject = repository.save(copyProject);

        List<Board> originalBoards = boardRepository.findByProjectIdOrderByPositionAsc(originalProject.getId());

        for (Board originalBoard : originalBoards) {
            Board copyBoard = new Board();
            copyBoard.setProject(savedProject);
            copyBoard.setName(originalBoard.getName());
            copyBoard.setDescription(originalBoard.getDescription());
            copyBoard.setPosition(originalBoard.getPosition());
            copyBoard.setStatus(originalBoard.getStatus());

            Board savedBoard = boardRepository.save(copyBoard);

            List<BoardColumn> originalColumns = boardColumnRepository.findByBoardIdOrderByPositionAsc(originalBoard.getId());

            for (BoardColumn originalColumn : originalColumns) {
                BoardColumn copyColumn = new BoardColumn();
                copyColumn.setBoard(savedBoard);
                copyColumn.setName(originalColumn.getName());
                copyColumn.setPosition(originalColumn.getPosition());

                BoardColumn savedColumn = boardColumnRepository.save(copyColumn);

                List<Task> originalTasks = taskRepository.findByBoardColumnIdOrderByPositionAsc(originalColumn.getId());

                for (Task originalTask : originalTasks) {
                    Task copyTask = new Task();
                    copyTask.setBoardColumn(savedColumn);
                    copyTask.setTitle(originalTask.getTitle());
                    copyTask.setDescription(originalTask.getDescription());
                    copyTask.setPriority(originalTask.getPriority());
                    copyTask.setDueDate(originalTask.getDueDate());
                    copyTask.setPosition(originalTask.getPosition());
                    copyTask.getTags().addAll(originalTask.getTags());
                    copyTask.getAssignees().addAll(originalTask.getAssignees());

                    taskRepository.save(copyTask);
                }
            }
        }

        return savedProject;
    }

}
