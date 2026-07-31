package kg.core.project.endpoint;

import kg.core.project.dtos.ProjectResponse;
import kg.core.project.dtos.projectRequest;

import java.util.List;


public interface ProjectEndpoint {

    ProjectResponse create(projectRequest dto);

    projectRequest get(Long id);

    List<projectRequest> getAll();

    projectRequest update(Long id, projectRequest dto);

    void archive(Long id);

    void unarchive(Long id);

    void delete(Long id);

}
