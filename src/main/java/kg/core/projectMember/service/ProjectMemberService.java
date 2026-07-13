package kg.core.projectMember.service;

import kg.core.base.service.CrudService;
import kg.core.projectMember.dtos.InviteMemberRequest;
import kg.core.projectMember.dtos.UpdateMemberRoleRequest;
import kg.core.projectMember.model.ProjectMember;
import kg.core.projectMember.model.RespondInvitationRequest;

import java.util.List;

public interface ProjectMemberService extends CrudService<ProjectMember, Long> {

    ProjectMember invite (InviteMemberRequest request);

    ProjectMember updateRole(Long memberId, UpdateMemberRoleRequest request);

    ProjectMember respondToInvitation(Long memberId, RespondInvitationRequest request);

    void removeMember(Long memberId);

    void leaveProject(Long projectId);

    List<ProjectMember> getByProject(Long projectId);
}
