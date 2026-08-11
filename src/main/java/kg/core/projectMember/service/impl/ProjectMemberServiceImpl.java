package kg.core.projectMember.service.impl;

import kg.core.base.exception.ConflictException;
import kg.core.base.exception.NotFoundException;
import kg.core.base.service.impl.DefaultCrudService;
import kg.core.project.model.Project;
import kg.core.project.repository.ProjectRepository;
import kg.core.projectMember.model.InvitationStatus;
import kg.core.projectMember.model.ProjectMember;
import kg.core.projectMember.model.ProjectRole;
import kg.core.projectMember.repository.ProjectMemberRepository;
import kg.core.projectMember.service.ProjectMemberService;
import kg.core.security.validator.AccessGuard;
import kg.core.user.model.User;
import kg.core.user.repository.UserRepository;
import kg.core.utils.UserProvider;
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
    UserProvider userProvider;
    AccessGuard accessGuard;


    protected ProjectMemberServiceImpl(ProjectMemberRepository projectMemberRepository, ProjectRepository projectRepository, UserRepository userRepository, AccessGuard accessGuard, UserProvider userProvider) {
        super(projectMemberRepository);
        this.projectMemberRepository = projectMemberRepository;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.userProvider = userProvider;
        this.accessGuard = accessGuard;
    }

    @Override
    @Transactional
    public ProjectMember invite(Long projectId, String email, String username, ProjectRole role) {
        if (email == null && username == null) {
            throw new ConflictException("Укадите email или username");
        }

        accessGuard.requireProjectRole(projectId, ProjectRole.OWNER);

        User user = email != null
                ? userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("Пользователь с таким email не найден"))
                : userRepository.findByUsername(username).orElseThrow(() -> new NotFoundException("Пользователь с таким username не найден"));

        Project project = projectRepository.findById(projectId).orElseThrow(() -> new NotFoundException("Проект не найден"));

        if (projectMemberRepository.existsByProjectIdAndUserId(projectId, user.getId())) {
            throw new ConflictException("Пользователь уже является участником проекта");
        }

        if (role == ProjectRole.OWNER) {
            throw new ConflictException("Нельзя пригласить пользователя с ролью OWNER");
        }

        ProjectMember projectMember = new ProjectMember();
        projectMember.setProject(project);
        projectMember.setUser(user);
        projectMember.setRole(role);
        projectMember.setInvitationStatus(InvitationStatus.PENDING);
        return save(projectMember);
    }

    @Override
    @Transactional
    public ProjectMember updateRole(Long memberId, ProjectRole role) {
        ProjectMember member = find(memberId);

        accessGuard.requireProjectRole(member.getProject().getId(), ProjectRole.OWNER);

        if(member.getRole() == ProjectRole.OWNER){
            throw new ConflictException("Нельзя изменить роль владельца");
        }

        if (role == ProjectRole.OWNER) {
            throw new ConflictException("Нельзя назначить роль Owner");
        }

        member.setRole(role);
        return save(member);
    }

    @Override
    @Transactional
    public ProjectMember respondToInvitation(Long projectId, InvitationStatus status) {
        User currentUser = userProvider.getCurrentUser();

        ProjectMember member = projectMemberRepository.findByProjectIdAndUserId(projectId, currentUser.getId())
                .orElseThrow(() -> new NotFoundException("Приглашение в этот проект не найдено"));

        if(member.getInvitationStatus() !=  InvitationStatus.PENDING) {
            throw new ConflictException("Приглашение уже было обработано");
        }

        if (status == InvitationStatus.DECLINED) {
            projectMemberRepository.delete(member);
            return member;
        }

        member.setInvitationStatus(status);
        return save(member);
    }

    @Override
    @Transactional
    public void removeMember(Long memberId) {
        ProjectMember member = find(memberId);

        accessGuard.requireProjectRole(member.getProject().getId(), ProjectRole.OWNER);

        if (member.getRole() == ProjectRole.OWNER) {
            throw new ConflictException("Нельзя удалить пользователя с ролью Owner");
        }

        projectMemberRepository.delete(member);

    }

    @Override
    @Transactional
    public void leaveProject(Long projectId) {
        User currentUser = userProvider.getCurrentUser();

        ProjectMember member = projectMemberRepository.findByProjectIdAndUserId(projectId, currentUser.getId())
                .orElseThrow(() -> new NotFoundException("Вы не являетесь участником проекта"));

        if (member.getRole() == ProjectRole.OWNER) {
            throw new ConflictException("Вы не можете покинуть проект, т.к являетесь владельцем");
        }

        projectMemberRepository.delete(member);
    }

    @Override
    public List<ProjectMember> getByProject(Long projectId) {
        return projectMemberRepository.findByProjectId(projectId);
    }

}

