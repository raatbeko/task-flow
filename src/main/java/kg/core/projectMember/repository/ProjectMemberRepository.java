package kg.core.projectMember.repository;

import kg.core.base.search.BaseRepository;
import kg.core.projectMember.model.ProjectMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectMemberRepository extends BaseRepository<ProjectMember, Long> {
}
