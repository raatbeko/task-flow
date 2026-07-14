package kg.core.projectMember.endpoint;

import kg.core.projectMember.dtos.InviteMemberRequest;
import kg.core.projectMember.dtos.ProjectMemberResponse;
import kg.core.projectMember.dtos.RespondInvitationRequest;
import kg.core.projectMember.dtos.UpdateMemberRoleRequest;

public interface ProjectMemberEndpoint {

    ProjectMemberResponse invite(InviteMemberRequest request);

    ProjectMemberResponse updateRole(Long memberId, UpdateMemberRoleRequest request);

    ProjectMemberResponse respondToInvitation(Long memberId, RespondInvitationRequest request);

    void removeMember(Long memberId);

    void leaveProject(Long projectId);

}
