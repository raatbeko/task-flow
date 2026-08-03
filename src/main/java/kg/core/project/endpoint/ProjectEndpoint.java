package kg.core.project.endpoint;

import kg.core.project.dtos.ProjectResponse;
import kg.core.project.dtos.ProjectRequest;

import java.util.List;


public interface ProjectEndpoint {

    ProjectResponse create(ProjectRequest request);

    ProjectResponse get(Long id);

    List<ProjectResponse> getAll();

    ProjectResponse update(Long id, ProjectRequest request);

    void archive(Long id);

    void unarchive(Long id);

    void delete(Long id);

}
