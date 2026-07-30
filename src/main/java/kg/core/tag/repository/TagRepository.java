package kg.core.tag.repository;

import kg.core.base.search.BaseRepository;
import kg.core.tag.model.Tag;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagRepository extends BaseRepository<Tag, Long> {

    List<Tag> findByProjectId(Long projectId);
}