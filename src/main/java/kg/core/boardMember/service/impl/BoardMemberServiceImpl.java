package kg.core.boardMember.service.impl;

import kg.core.base.exception.ConflictException;
import kg.core.base.exception.NotFoundException;
import kg.core.base.service.impl.DefaultCrudService;
import kg.core.board.model.Board;
import kg.core.board.repository.BoardRepository;
import kg.core.boardMember.model.BoardMember;
import kg.core.boardMember.model.BoardRole;
import kg.core.boardMember.repository.BoardMemberRepository;
import kg.core.boardMember.service.BoardMemberService;
import kg.core.projectMember.model.InvitationStatus;
import kg.core.projectMember.model.ProjectMember;
import kg.core.projectMember.model.ProjectRole;
import kg.core.projectMember.repository.ProjectMemberRepository;
import kg.core.utils.UserProvider;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BoardMemberServiceImpl extends DefaultCrudService<BoardMember, Long> implements BoardMemberService {

    BoardMemberRepository boardMemberRepository;
    BoardRepository boardRepository;
    ProjectMemberRepository projectMemberRepository;
    UserProvider userProvider;

    protected BoardMemberServiceImpl(BoardMemberRepository boardMemberRepository, BoardRepository boardRepository, ProjectMemberRepository projectMemberRepository, UserProvider userProvider) {
        super(boardMemberRepository);
        this.boardMemberRepository = boardMemberRepository;
        this.boardRepository = boardRepository;
        this.projectMemberRepository = projectMemberRepository;
        this.userProvider = userProvider;
    }

    @Override
    @Transactional
    public BoardMember invite(Long memberId, Long boardId, BoardRole role) {

        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new NotFoundException("Доска не найдена"));

        checkOwner(board.getProject().getId());

        ProjectMember projectMember = projectMemberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException("Участник проекта не найден"));

        if (projectMember.getInvitationStatus() != InvitationStatus.ACCEPTED) {
            throw new IllegalArgumentException("Участник не принял приглашение в проект");
        }

        if (!projectMember.getProject().getId().equals(board.getProject().getId())) {
            throw new ConflictException("Участник не состоит в этом проекте");
        }

        if (boardMemberRepository.existsByBoardIdAndProjectMemberId(boardId, memberId)) {
            throw new ConflictException("Участник уже добавлен в эту доску");
        }

        BoardMember boardMember = new BoardMember();
        boardMember.setBoard(board);
        boardMember.setProjectMember(projectMember);
        boardMember.setRole(role);

        return save(boardMember);
    }

    @Transactional
    @Override
    public BoardMember updateRole(Long memberId, BoardRole role) {
        BoardMember boardMember = find(memberId);
        checkOwner(boardMember.getBoard().getProject().getId());
        boardMember.setRole(role);
        return boardMemberRepository.save(boardMember);
    }

    @Transactional
    @Override
    public void removeMember(Long memberId) {
        BoardMember boardMember = find(memberId);
        checkOwner(boardMember.getBoard().getProject().getId());
        boardMemberRepository.delete(boardMember);

    }

    private void checkOwner(Long projectId) {
        Long currentUserId = userProvider.getCurrentUser().getId();
        ProjectMember currentMember = projectMemberRepository
                .findByProjectIdAndUserId(projectId, currentUserId)
                .orElseThrow(() -> new ConflictException("Вы не участник проекта"));
        if (currentMember.getRole() != ProjectRole.OWNER) {
            throw new ConflictException("Только владелец проекта может выполнить это действие");
        }
    }

}