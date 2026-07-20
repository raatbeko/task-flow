package kg.core.boardColumn.service.impl;

import jakarta.persistence.EntityNotFoundException;
import kg.core.base.service.impl.DefaultCrudService;
import kg.core.board.model.Board;
import kg.core.board.repository.BoardRepository;
import kg.core.boardColumn.dtos.BoardColumnPositionRequest;
import kg.core.boardColumn.model.BoardColumn;
import kg.core.boardColumn.repository.BoardColumnRepository;
import kg.core.boardColumn.service.BoardColumnService;
import kg.core.task.model.Task;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BoardColumnServiceImpl extends DefaultCrudService<BoardColumn, Long> implements BoardColumnService {

    BoardColumnRepository boardColumnRepository;
    BoardRepository boardRepository;

    protected BoardColumnServiceImpl(BoardColumnRepository boardColumnRepository, BoardRepository boardRepository) {
        super(boardColumnRepository);
        this.boardColumnRepository = boardColumnRepository;
        this.boardRepository = boardRepository;
    }

    @Override
    @Transactional
    public BoardColumn create(Long boardId, BoardColumn column) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new EntityNotFoundException("Доска не найдена"));

        int nextPosition = boardColumnRepository.countByBoardId(board.getId());

        column.setBoard(board);
        column.setPosition(nextPosition);

        return save(column);
    }

    @Override
    @Transactional
    public void updatePosition(Long id, BoardColumnPositionRequest request) {
        BoardColumn boardColumn = find(id);
        Long boardId = boardColumn.getBoard().getId();
        int oldPosition = boardColumn.getPosition();
        int newPosition = request.position() != null ? request.position().intValue() : -1;

        if (newPosition == oldPosition) return;

        List<BoardColumn> boardColumns = boardColumnRepository.findByBoardIdOrderByPositionAsc(boardId);

        int maxPosition = boardColumns.size() - 1;
        if (newPosition < 0 || newPosition > maxPosition) {
            newPosition = maxPosition;
        }
        if (newPosition == oldPosition) return;

        boardColumns.remove((int) oldPosition);
        boardColumns.add(newPosition, boardColumn);

        for (int i = 0; i < boardColumns.size(); i++) {
            boardColumns.get(i).setPosition(i);
        }

        boardColumnRepository.saveAll(boardColumns);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        BoardColumn boardColumn = find(id);
        delete(boardColumn.getId());
    }
}
