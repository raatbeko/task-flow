package kg.core.task.repository;

import kg.core.task.model.TaskDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface TaskSearchRepository extends ElasticsearchRepository<TaskDocument, Long> {
    List<TaskDocument> findByTitleContainingOrDescriptionContaining(String title, String description);
}
