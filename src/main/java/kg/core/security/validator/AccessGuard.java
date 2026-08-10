package kg.core.security.validator;

import kg.core.base.exception.ForbiddenException;
import kg.core.boardMember.model.BoardMember;
import kg.core.boardMember.model.BoardRole;
import kg.core.boardMember.repository.BoardMemberRepository;
import kg.core.projectMember.model.ProjectMember;
import kg.core.projectMember.model.ProjectRole;
import kg.core.projectMember.repository.ProjectMemberRepository;
import kg.core.user.model.User;
import kg.core.utils.UserProvider;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AccessGuard {

    UserProvider userProvider;
    ProjectMemberRepository projectMemberRepository;
    BoardMemberRepository boardMemberRepository;

    public void requireProjectRole(Long projectId, ProjectRole minRole){
        ProjectMember projectMember = getProjectMember(projectId);

        if(projectMember.getRole().getId() < minRole.getId()){
            throw new ForbiddenException("У вас недостаточно прав. Требуется минимум роль: " + minRole);
        }
    }

    public void requireBoardRole(Long boardId, Long projectId, BoardRole minBoardRole){
        ProjectMember projectMember = getProjectMember(projectId);

        if(projectMember.getRole().getId() >= ProjectRole.EDITOR.getId()){
            return;
        }

        BoardMember boardMember = getBoardMember(boardId);

        if(boardMember.getRole().getId() < minBoardRole.getId()){
            throw new ForbiddenException("У вас недостаточно прав на этой доске. Требуется минимум роль: " + minBoardRole);
        }

    }

    private ProjectMember getProjectMember(Long projectId) {
        User currentUser = userProvider.getCurrentUser();
        return projectMemberRepository.findByProjectIdAndUserId(projectId, currentUser.getId())
                .orElseThrow(() -> new ForbiddenException("У вас нет доступа к этому проекту"));
    }

    private BoardMember getBoardMember(Long boardId) {
        User currentUser = userProvider.getCurrentUser();
        return boardMemberRepository.findByBoardIdAndProjectMemberUserId(boardId, currentUser.getId())
                .orElseThrow(() -> new ForbiddenException("У вас нет доступа к этой доске"));
    }

}

