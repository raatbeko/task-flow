package kg.core.boardMember.service.impl;

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
import kg.core.projectMember.repository.ProjectMemberRepository;
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

    protected BoardMemberServiceImpl(BoardMemberRepository boardMemberRepository, BoardRepository boardRepository, ProjectMemberRepository projectMemberRepository) {
        super(boardMemberRepository);
        this.boardMemberRepository = boardMemberRepository;
        this.boardRepository = boardRepository;
        this.projectMemberRepository = projectMemberRepository;
    }

    @Override
    @Transactional
    public BoardMember invite(Long memberId, Long boardId, String email, BoardRole role) {

        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new NotFoundException("Доска не найдена"));

        ProjectMember projectMember = projectMemberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException("Участник проекта не найден"));

        if (projectMember.getInvitationStatus() != InvitationStatus.ACCEPTED) {
            throw new IllegalArgumentException("Участник не принял приглашение в проект");
        }

        if (!projectMember.getProject().getId().equals(board.getProject().getId())) {
            throw new IllegalArgumentException("Участник не добавлен в эту доску");
        }
        BoardMember boardMember = new BoardMember();
        boardMember.setBoard(board);
        boardMember.setProjectMember(projectMember);
        boardMember.setRole(role);

        return save(boardMember);


    }

    @Override
    public BoardMember updateRole(Long memberId, BoardRole role) {
        BoardMember boardMember = find(memberId);
        boardMember.setRole(role);
        return boardMemberRepository.save(boardMember);
    }

    @Override
    public void removeMember(Long memberId) {
        BoardMember boardMember = find(memberId);
        boardMemberRepository.delete(boardMember);

    }
}