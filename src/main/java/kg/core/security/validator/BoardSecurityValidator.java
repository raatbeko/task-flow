package kg.core.security.validator;

import kg.core.base.exception.ForbiddenException;
import kg.core.boardMember.model.BoardMember;
import kg.core.boardMember.model.BoardRole;
import kg.core.boardMember.repository.BoardMemberRepository;
import kg.core.user.model.User;
import kg.core.utils.UserProvider;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BoardSecurityValidator {

    UserProvider userProvider;
    BoardMemberRepository boardMemberRepository;
    ProjectSecurityValidator projectSecurityValidator;

    public void checkBoardAccess(Long boardId, Long projectId) {

        if(projectSecurityValidator.isProjectOwner(projectId)){
            return;
        }

        User currentUser = userProvider.getCurrentUser();

        BoardMember boardMember = boardMemberRepository.findByBoardIdAndProjectMemberUserId(boardId, currentUser.getId())
                .orElseThrow(() -> new ForbiddenException("У вас нет доступа к этой доске"));

        if(boardMember.getRole() != BoardRole.OWNER){
            throw new ForbiddenException("Только владелец проекта или доски может выполнять это действие");
        }

    }
}