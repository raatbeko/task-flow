package kg.core.project.service.impl;

import kg.core.base.exception.ConflictException;
import kg.core.base.exception.NotFoundException;
import kg.core.base.service.impl.DefaultCrudService;
import kg.core.project.model.Project;
import kg.core.project.model.ProjectDocument;
import kg.core.project.model.ProjectStatus;
import kg.core.project.repository.ProjectRepository;
import kg.core.project.repository.ProjectSearchRepository;
import kg.core.project.service.ProjectService;
import kg.core.projectMember.model.InvitationStatus;
import kg.core.projectMember.model.ProjectMember;
import kg.core.projectMember.model.ProjectRole;
import kg.core.projectMember.repository.ProjectMemberRepository;
import kg.core.user.model.User;
import kg.core.utils.UserProvider;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProjectServiceImpl extends DefaultCrudService<Project, Long> implements ProjectService {

    ProjectRepository repository;
    UserProvider userProvider;
    ProjectMemberRepository projectMemberRepository;
    ProjectSearchRepository searchRepository;

    public ProjectServiceImpl(ProjectRepository repository, UserProvider userProvider, ProjectMemberRepository projectMemberRepository, ProjectSearchRepository searchRepository) {
        super(repository);
        this.repository = repository;
        this.userProvider = userProvider;
        this.projectMemberRepository = projectMemberRepository;
        this.searchRepository = searchRepository;
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
        Project project = get(id);
        if (project.getStatus() == ProjectStatus.ACTIVE) {
            repository.delete(project);
        } else {
            throw new ConflictException("Проект с id " + id + " заархивирован. Сначала восстановите проект.");
        }
    }

    @Override
    @Transactional
    public void archive(Long id) {
        Project project = get(id);
        project.setStatus(ProjectStatus.ARCHIVED);
        repository.save(project);
    }

    @Override
    @Transactional
    public void unarchive(Long id) {
        Project project = get(id);
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
    public List<ProjectDocument> search(String query, ProjectStatus status) {
        if(query == null || query.isBlank()) {
            return new ArrayList<>();
        }
        if(status != null) {
            return searchRepository.findByNameContainingOrDescriptionContainingOrOwnerUsernameContainingAndStatus(query, query, query, status);
        }

        return  searchRepository.findByNameContainingOrDescriptionContainingOrOwnerUsernameContaining(query, query, query);

    }

}
