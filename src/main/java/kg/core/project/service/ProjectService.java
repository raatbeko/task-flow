package kg.core.project.service;

import kg.core.base.service.CrudService;
import kg.core.project.model.Project;

public interface ProjectService extends CrudService<Project, Long> {
;
    void delete(Long id);

    void archive(Long id);

}
