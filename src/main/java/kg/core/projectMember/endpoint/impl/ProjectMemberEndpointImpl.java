package kg.core.projectMember.endpoint.impl;

import kg.core.projectMember.dtos.InviteMemberRequest;
import kg.core.projectMember.dtos.ProjectMemberResponse;
import kg.core.projectMember.dtos.RespondInvitationRequest;
import kg.core.projectMember.dtos.UpdateMemberRoleRequest;
import kg.core.projectMember.endpoint.ProjectMemberEndpoint;
import kg.core.projectMember.mapper.ProjectMemberMapper;
import kg.core.projectMember.model.ProjectMember;
import kg.core.projectMember.service.ProjectMemberService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProjectMemberEndpointImpl implements ProjectMemberEndpoint {

    ProjectMemberService projectMemberService;
    ProjectMemberMapper projectMemberMapper;

    @Override
    public ProjectMemberResponse invite(InviteMemberRequest request) {
        ProjectMember member =
                projectMemberService.invite(request.projectId(), request.email(),
                        request.username(), request.role());
        return projectMemberMapper.toResponse(member);
    }

    @Override
    public ProjectMemberResponse updateRole(Long memberId, UpdateMemberRoleRequest request) {
        ProjectMember member =  projectMemberService.updateRole(memberId, request.role());
        return  projectMemberMapper.toResponse(member);
    }

    @Override
    public ProjectMemberResponse respondToInvitation(Long memberId, RespondInvitationRequest request) {
        ProjectMember member = projectMemberService.respondToInvitation(memberId, request.status());
        return projectMemberMapper.toResponse(member);
    }

    @Override
    public void removeMember(Long memberId) {
        projectMemberService.removeMember(memberId);
    }

    @Override
    public void leaveProject(Long projectId) {
        projectMemberService.leaveProject(projectId);
    }
}
