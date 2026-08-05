package kg.core.projectMember.repository;

import kg.core.base.search.BaseRepository;
import kg.core.projectMember.model.InvitationStatus;
import kg.core.projectMember.model.ProjectMember;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectMemberRepository extends BaseRepository<ProjectMember, Long> {

    List<ProjectMember> findByProjectId(Long projectId);

    Optional<ProjectMember> findByProjectIdAndUserId(Long projectId, Long userId);

    boolean existsByProjectIdAndUserId(Long projectId, Long userId);

    boolean existsByProjectIdAndUserIdAndInvitationStatus(Long projectId, Long userId, InvitationStatus status);

    @Modifying
    @Query("delete from ProjectMember pm where pm.project.id = :projectId")
    void deleteByProjectId(@Param("projectId") Long projectId);

}
