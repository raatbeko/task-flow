package kg.core.project.repository;

import kg.core.base.search.BaseRepository;
import kg.core.project.model.Project;
import kg.core.projectMember.model.InvitationStatus;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends BaseRepository<Project, Long>{

    @Query("SELECT pm.project FROM ProjectMember pm WHERE pm.user.id = :userId AND pm.invitationStatus = :status")
    List<Project> findAllProjectsByUserIdAndStatus(@Param("userId") Long userId, @Param("status") InvitationStatus status);
}
