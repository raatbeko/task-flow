package kg.core.board.service.impl;

import jakarta.persistence.EntityNotFoundException;
import kg.core.base.service.impl.DefaultCrudService;
import kg.core.board.dtos.BoardPositionRequest;
import kg.core.board.model.Board;
import kg.core.board.model.BoardStatus;
import kg.core.board.repository.BoardRepository;
import kg.core.board.service.BoardService;
import kg.core.boardColumn.dtos.BoardColumnPositionRequest;
import kg.core.boardColumn.model.BoardColumn;
import kg.core.project.model.Project;
import kg.core.project.repository.ProjectRepository;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BoardServiceImpl extends DefaultCrudService<Board, Long> implements BoardService {

    BoardRepository boardRepository;
    ProjectRepository projectRepository;

    protected BoardServiceImpl(BoardRepository boardRepository, ProjectRepository projectRepository) {
        super(boardRepository);
        this.boardRepository = boardRepository;
        this.projectRepository = projectRepository;
    }


    @Override
    @Transactional
    public Board create(Long projectId, Board board) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("Проект не найден"));

        int nextPosition = boardRepository.countByProjectId(project.getId());

        board.setProject(project);
        board.setStatus(BoardStatus.ACTIVE);
        board.setPosition(nextPosition);

        return save(board);
    }

    @Override
    @Transactional
    public void delete(Long id){
        Board board = find(id);
        boardRepository.delete(board);
    }

    @Override
    @Transactional
    public void updatePosition(Long id, BoardPositionRequest request) {
        Board board = find(id);
        Long projectId = board.getProject().getId();
        int oldPosition = board.getPosition();
        int newPosition = request.position() != null ? request.position().intValue() : -1;

        if (newPosition == oldPosition) return;

        List<Board> boards = boardRepository.findByProjectIdOrderByPositionAsc(projectId);

        int maxPosition = boards.size() - 1;
        if (newPosition < 0 || newPosition > maxPosition) {
            newPosition = maxPosition;
        }
        if (newPosition == oldPosition) return;

        boards.remove((int) oldPosition);
        boards.add(newPosition, board);

        for (int i = 0; i < boards.size(); i++) {
            boards.get(i).setPosition(i);
        }

        boardRepository.saveAll(boards);
    }

    @Override
    @Transactional
    public void archive(Long id) {
        Board board = find(id);
        board.setStatus(BoardStatus.ARCHIVED);
        save(board);
    }

    @Override
    @Transactional
    public Board duplicate(Long id) {
        Board originalBoard = find(id);
        int nextPosition = boardRepository.countByProjectId(originalBoard.getProject().getId());

        Board copyBoard =  new Board();
        copyBoard.setProject(originalBoard.getProject());
        copyBoard.setName(originalBoard.getName());
        copyBoard.setDescription(originalBoard.getDescription());
        copyBoard.setPosition(nextPosition);
        copyBoard.setStatus(BoardStatus.ACTIVE);

        return boardRepository.save(copyBoard);
    }
}
