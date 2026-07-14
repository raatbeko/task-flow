package kg.core.boardColumn.service.impl;

import jakarta.persistence.EntityNotFoundException;
import kg.core.base.service.impl.DefaultCrudService;
import kg.core.board.model.Board;
import kg.core.board.repository.BoardRepository;
import kg.core.boardColumn.model.BoardColumn;
import kg.core.boardColumn.repository.BoardColumnRepository;
import kg.core.boardColumn.service.BoardColumnService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


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

        return boardColumnRepository.save(column);
    }


    @Override
    @Transactional
    public BoardColumn updatePosition(Integer position) {
        return null;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        BoardColumn column = find(id);
        boardColumnRepository.delete(column);
    }
}
