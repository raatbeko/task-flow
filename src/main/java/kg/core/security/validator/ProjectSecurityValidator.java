package kg.core.security.validator;

import kg.core.base.exception.ForbiddenException;
import kg.core.projectMember.model.ProjectMember;
import kg.core.projectMember.model.ProjectRole;
import kg.core.projectMember.repository.ProjectMemberRepository;
import kg.core.user.model.User;
import kg.core.utils.UserProvider;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProjectSecurityValidator {

    UserProvider userProvider;
    ProjectMemberRepository projectMemberRepository;

    public boolean isProjectOwner(Long projectId) {
        User currentUser = userProvider.getCurrentUser();

        Optional<ProjectMember> memberOptional = projectMemberRepository.findByProjectIdAndUserId(projectId, currentUser.getId());

        if (memberOptional.isEmpty()) {
            return false;
        }

        ProjectMember member = memberOptional.get();
        return member.getRole() == ProjectRole.OWNER;
    }

    public void checkAccess(Long projectId) {
        if (!isProjectOwner(projectId)) {
            throw new ForbiddenException("Только владелец проекта может выполнять это действие");
        }
    }
}
