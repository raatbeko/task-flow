package kg.core.projectMember.service;

import kg.core.base.service.CrudService;
import kg.core.projectMember.model.ProjectRole;
import kg.core.projectMember.model.InvitationStatus;
import kg.core.projectMember.model.ProjectMember;

import java.util.List;

public interface ProjectMemberService extends CrudService<ProjectMember, Long> {

    ProjectMember invite (Long projectId, String email, String username, ProjectRole role);

    ProjectMember updateRole(Long memberId, ProjectRole role);

    ProjectMember respondToInvitation(Long memberId, InvitationStatus status);

    void removeMember(Long memberId);

    void leaveProject(Long projectId);

    List<ProjectMember> getByProject(Long projectId);
}
