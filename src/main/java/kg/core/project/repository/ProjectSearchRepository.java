package kg.core.project.repository;

import kg.core.project.model.ProjectDocument;
import kg.core.project.model.ProjectStatus;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface ProjectSearchRepository extends ElasticsearchRepository<ProjectDocument, Long> {

    List<ProjectDocument> findByNameContainingOrDescriptionContainingOrOwnerUsernameContaining(String name, String description, String ownerUsername);

    List<ProjectDocument> findByNameContainingOrDescriptionContainingOrOwnerUsernameContainingAndStatus(String name, String description, String ownerUsername, ProjectStatus status);
}
