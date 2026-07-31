package kg.core.project.endpoint;

import kg.core.project.dtos.ProjectResponse;
import kg.core.project.dtos.ProjectRequest;

import java.util.List;


public interface ProjectEndpoint {

    ProjectResponse create(ProjectRequest request);

    ProjectResponse get(Long id);

    List<ProjectResponse> getAll();

    projectRequest update(Long id, projectRequest dto);

    void archive(Long id);

    void unarchive(Long id);

    void delete(Long id);

}
