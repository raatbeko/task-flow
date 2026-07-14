package kg.core.projectMember.service;

import kg.core.base.service.CrudService;
import kg.core.projectMember.model.InvitationStatus;
import kg.core.projectMember.model.ProjectMember;
import kg.core.projectMember.model.Role;

import java.util.List;

public interface ProjectMemberService extends CrudService<ProjectMember, Long> {

    ProjectMember invite (Long projectId, String email, String username, Role role);

    ProjectMember updateRole(Long memberId, Role role);

    ProjectMember respondToInvitation(Long memberId, InvitationStatus status);

    void removeMember(Long memberId);

    void leaveProject(Long projectId);

    List<ProjectMember> getByProject(Long projectId);
}
