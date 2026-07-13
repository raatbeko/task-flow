package kg.core.projectMember.service.impl;

import kg.core.auth.service.impl.DefaultAccountContextProvider;
import kg.core.base.exception.NotFoundException;
import kg.core.base.service.impl.DefaultCrudService;
import kg.core.project.model.Project;
import kg.core.project.repository.ProjectRepository;
import kg.core.projectMember.dtos.InviteMemberRequest;
import kg.core.projectMember.dtos.UpdateMemberRoleRequest;
import kg.core.projectMember.model.InvitationStatus;
import kg.core.projectMember.model.ProjectMember;
import kg.core.projectMember.model.RespondInvitationRequest;
import kg.core.projectMember.model.Role;
import kg.core.projectMember.repository.ProjectMemberRepository;
import kg.core.projectMember.service.ProjectMemberService;
import kg.core.user.model.User;
import kg.core.user.repository.UserRepository;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProjectMemberServiceImpl extends DefaultCrudService<ProjectMember, Long> implements ProjectMemberService {

    ProjectMemberRepository projectMemberRepository;
    ProjectRepository projectRepository;
    UserRepository userRepository;
    DefaultAccountContextProvider defaultAccountContextProvider;


    protected ProjectMemberServiceImpl(ProjectMemberRepository projectMemberRepository, ProjectRepository projectRepository, UserRepository userRepository, DefaultAccountContextProvider defaultAccountContextProvider) {
        super(projectMemberRepository);
        this.projectMemberRepository = projectMemberRepository;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.defaultAccountContextProvider = defaultAccountContextProvider;
    }

    @Override
    @Transactional
    public ProjectMember invite(InviteMemberRequest request) {
        if (request.email() == null && request.username() == null) {
            throw new IllegalArgumentException("Укадите email или username");
        }

        User user = request.email() != null
                ? userRepository.findByEmail(request.email()).orElseThrow(() -> new NotFoundException("Пользователь с таким email не найден"))
                : userRepository.findByUsername(request.username()).orElseThrow(() -> new NotFoundException("Пользователь с таким username не найден"));

        Project project = projectRepository.findById(request.projectId()).orElseThrow(() -> new NotFoundException("Проект не найден"));

        if (projectMemberRepository.existsByProjectIdAndUserId(request.projectId(), user.getId())) {
            throw new IllegalArgumentException("Пользователь уже является участником проекта");
        }

        if (request.role() == Role.OWNER) {
            throw new IllegalArgumentException("Нельзя пригласить пользователя с ролью OWNER");
        }

        ProjectMember projectMember = new ProjectMember();
        projectMember.setProject(project);
        projectMember.setUser(user);
        projectMember.setRole(request.role());
        projectMember.setInvitationStatus(InvitationStatus.PENDING);
        return projectMemberRepository.save(projectMember);
    }

    @Override
    @Transactional
    public ProjectMember updateRole(Long memberId, UpdateMemberRoleRequest request) {
        ProjectMember member = find(memberId);

        if(member.getRole() == Role.OWNER){
            throw new IllegalArgumentException("Нельзя изменить роль владельца");
        }

        if (request.role() == Role.OWNER) {
            throw new IllegalArgumentException("Нельзя назначить роль Owner");
        }

        member.setRole(request.role());
        return member;
    }

    @Override
    @Transactional
    public ProjectMember respondToInvitation(Long memberId, RespondInvitationRequest request) {
        ProjectMember member = find(memberId);

        if(member.getInvitationStatus() !=  InvitationStatus.PENDING) {
            throw new IllegalArgumentException("Приглашение уже было обработано");
        }

        member.setInvitationStatus(request.status());
        return member;
    }

    @Override
    @Transactional
    public void removeMember(Long memberId) {
        ProjectMember member = find(memberId);

        if (member.getRole() == Role.OWNER) {
            throw new IllegalArgumentException("Нельзя удалить пользователя с ролью Owner");
        }

        projectMemberRepository.delete(member);

    }

    @Override
    @Transactional
    public void leaveProject(Long projectId) {
        User currentUser = defaultAccountContextProvider.getCurrentUser();

        ProjectMember member = projectMemberRepository.findByProjectIdAndUserId(projectId, currentUser.getId())
                .orElseThrow(() -> new NotFoundException("Вы не являетесь участником проекта"));

        if (member.getRole() == Role.OWNER) {
            throw new IllegalArgumentException("Вы не можете покинуть проект, т.к являетесь владельцем");
        }

        projectMemberRepository.delete(member);
    }

    @Override
    public List<ProjectMember> getByProject(Long projectId) {
        return projectMemberRepository.findByProjectId(projectId);
    }

}

