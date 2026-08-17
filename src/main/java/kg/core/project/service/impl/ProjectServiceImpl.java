package kg.core.project.service.impl;

import kg.core.attachment.repository.AttachmentRepository;
import kg.core.base.exception.ConflictException;
import kg.core.base.exception.NotFoundException;
import kg.core.base.service.impl.DefaultCrudService;
import kg.core.board.repository.BoardRepository;
import kg.core.boardColumn.repository.BoardColumnRepository;
import kg.core.boardMember.repository.BoardMemberRepository;
import kg.core.comment.repository.CommentRepository;
import kg.core.project.model.Project;
import kg.core.project.model.ProjectStatus;
import kg.core.project.repository.ProjectRepository;
import kg.core.project.service.ProjectService;
import kg.core.projectMember.model.InvitationStatus;
import kg.core.projectMember.model.ProjectMember;
import kg.core.projectMember.model.ProjectRole;
import kg.core.projectMember.repository.ProjectMemberRepository;
import kg.core.security.validator.AccessGuard;
import kg.core.tag.repository.TagRepository;
import kg.core.task.repository.TaskRepository;
import kg.core.user.model.User;
import kg.core.utils.UserProvider;
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
    AccessGuard accessGuard;
    CommentRepository commentRepository;
    BoardRepository boardRepository;
    TaskRepository taskRepository;
    AttachmentRepository attachmentRepository;
    BoardMemberRepository boardMemberRepository;
    BoardColumnRepository boardColumnRepository;
    TagRepository tagRepository;


    public ProjectServiceImpl(ProjectRepository repository, UserProvider userProvider,
                              CommentRepository commentRepository, BoardRepository boardRepository,
                              TaskRepository taskRepository, AttachmentRepository attachmentRepository,
                              BoardMemberRepository boardMemberRepository, BoardColumnRepository boardColumnRepository,
                              TagRepository tagRepository, ProjectMemberRepository projectMemberRepository, AccessGuard accessGuard ) {
        super(repository);
        this.repository = repository;
        this.userProvider = userProvider;
        this.projectMemberRepository = projectMemberRepository;
        this.accessGuard = accessGuard;
        this.commentRepository = commentRepository;
        this.boardRepository = boardRepository;
        this.taskRepository = taskRepository;
        this.attachmentRepository = attachmentRepository;
        this.boardMemberRepository = boardMemberRepository;
        this.boardColumnRepository = boardColumnRepository;
        this.tagRepository = tagRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Project> findAll() {
        User currentUser = userProvider.getCurrentUser();
        return repository.findAllProjectsByUserIdAndStatus(currentUser.getId(), InvitationStatus.ACCEPTED);
    }

    @Override
    @Transactional(readOnly = true)
    public Project find(Long id) {
        Project project = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Проект с id: " + id + " не найден!"));
        User currentUser = userProvider.getCurrentUser();

        ProjectMember projectMember = projectMemberRepository.findByProjectIdAndUserId(id, currentUser.getId())
                .orElseThrow(() -> new NotFoundException("Проекс с id: " + id + " не найден!"));

        if(projectMember.getInvitationStatus() != InvitationStatus.ACCEPTED) {
            throw new NotFoundException("Проекс с id: " + id + " не найден!");
        }
        return project;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Project project = find(id);

        accessGuard.requireProjectRole(project.getId(),  ProjectRole.OWNER);

        if (project.getStatus() != ProjectStatus.ARCHIVED) {
            throw new ConflictException("Сначала заархивируйте проект, потом удаляйте");
        }
        List<Long> boardIds = boardRepository.findIdsByProjectId(id);

        for (Long boardId : boardIds) {
            commentRepository.deleteByBoardId(boardId);
            attachmentRepository.deleteByBoardId(boardId);
            taskRepository.deleteByBoardId(boardId);
            boardMemberRepository.deleteByBoardId(boardId);
            boardColumnRepository.deleteByBoardId(boardId);
        }
        boardRepository.deleteByProjectId(id);
        tagRepository.deleteByProjectId(id);
        projectMemberRepository.deleteByProjectId(id);

        repository.delete(project);
    }

    @Override
    @Transactional
    public void archive(Long id) {
        Project project = find(id);

        accessGuard.requireProjectRole(project.getId(),  ProjectRole.EDITOR);

        project.setStatus(ProjectStatus.ARCHIVED);
        repository.save(project);
    }

    @Override
    @Transactional
    public void unarchive(Long id) {
        Project project = find(id);

        accessGuard.requireProjectRole(project.getId(),  ProjectRole.EDITOR);

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

}
