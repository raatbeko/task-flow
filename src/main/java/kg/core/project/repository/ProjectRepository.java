package kg.core.project.repository;

import kg.core.base.search.BaseRepository;
import kg.core.project.model.Project;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends BaseRepository<Project, Long>{
}
