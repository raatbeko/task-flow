package kg.core.boardColumn.service.impl;

import jakarta.persistence.EntityNotFoundException;
import kg.core.base.service.impl.DefaultCrudService;
import kg.core.board.model.Board;
import kg.core.board.repository.BoardRepository;
import kg.core.boardColumn.dtos.BoardColumnCreateRequest;
import kg.core.boardColumn.dtos.BoardColumnPositionRequest;
import kg.core.boardColumn.dtos.BoardColumnUpdateRequest;
import kg.core.boardColumn.mapper.BoardColumnMapper;
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
    BoardColumnMapper boardColumnMapper;
    BoardRepository boardRepository;

    protected BoardColumnServiceImpl(BoardColumnRepository boardColumnRepository, BoardColumnMapper boardColumnMapper, BoardRepository boardRepository) {
        super(boardColumnRepository);
        this.boardColumnRepository = boardColumnRepository;
        this.boardColumnMapper = boardColumnMapper;
        this.boardRepository = boardRepository;
    }

    @Override
    @Transactional
    public BoardColumn create(BoardColumnCreateRequest request) {
        Board board = boardRepository.findById(request.boardId())
                .orElseThrow(() -> new EntityNotFoundException("Доска не найдена"));

        int nextPosition = boardColumnRepository.countByBoardId(board.getId());

        BoardColumn column = boardColumnMapper.toEntity(request);
        column.setBoard(board);
        column.setPosition(nextPosition);

        return boardColumnRepository.save(column);
    }

    @Override
    @Transactional
    public BoardColumn update(Long id, BoardColumnUpdateRequest request) {
        BoardColumn column = find(id);
        boardColumnMapper.update(request, column);
        return column;
    }

    @Override
    @Transactional
    public BoardColumn updatePosition(BoardColumnPositionRequest request) {
        return null;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        BoardColumn column = find(id);
        boardColumnRepository.delete(column);
    }
}
