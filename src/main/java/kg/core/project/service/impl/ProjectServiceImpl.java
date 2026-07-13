package kg.core.project.service.impl;

import kg.core.base.service.impl.DefaultCrudService;
import kg.core.project.model.Project;
import kg.core.project.model.ProjectStatus;
import kg.core.project.repository.ProjectRepository;
import kg.core.project.service.ProjectService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProjectServiceImpl extends DefaultCrudService<Project, Long> implements ProjectService {

    ProjectRepository repository;

    public ProjectServiceImpl(ProjectRepository repository) {
        super(repository);
        this.repository = repository;
    }

    @Override
    public void delete(Long id) {
        Project project = get(id);
        repository.delete(project);
    }

    @Override
    public void archive(Long id) {
        Project project = get(id);
        project.setStatus(ProjectStatus.ARCHIVED);
        repository.save(project);

    }
}
