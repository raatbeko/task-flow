package kg.core.project.endpoint;

import kg.core.project.dtos.ProjectDto;

import java.util.List;


public interface ProjectEndpoint {

    ProjectDto get(Long id);

    List<ProjectDto> getAll();

    ProjectDto create(ProjectDto dto);

    ProjectDto update(Long id, ProjectDto dto);

    void archive(Long id);

    void delete(Long id);

}
