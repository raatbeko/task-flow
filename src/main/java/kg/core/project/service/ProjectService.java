package kg.core.project.service;

import kg.core.base.service.CrudService;
import kg.core.project.model.Project;
import kg.core.project.model.ProjectDocument;
import kg.core.project.model.ProjectStatus;

import java.util.List;

public interface ProjectService extends CrudService<Project, Long> {

    void delete(Long id);

    void archive(Long id);

    void unarchive(Long id);

    Project create(Project project);

    List<ProjectDocument> search(String query, ProjectStatus status);
}
