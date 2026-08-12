package kg.core.tag.repository;

import kg.core.base.search.BaseRepository;
import kg.core.tag.model.Tag;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagRepository extends BaseRepository<Tag, Long> {

    List<Tag> findByProjectId(Long projectId);

    @Modifying
    @Query("delete from Tag t where t.project.id = :projectId")
    void deleteByProjectId(@Param("projectId") Long projectId);
}