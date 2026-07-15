package kg.core.project.repository;

import kg.core.base.search.BaseRepository;
import kg.core.project.model.Project;
import kg.core.user.model.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends BaseRepository<Project, Long>{

    List<Project> findAllByOwner(User owner);
}
