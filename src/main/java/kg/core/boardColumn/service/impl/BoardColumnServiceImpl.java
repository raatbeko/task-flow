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
    public BoardColumn create(BoardColumnCreateRequest boardColumnCreateRequest) {
        Board board = boardRepository.findById(boardColumnCreateRequest.boardId())
                .orElseThrow(() -> new EntityNotFoundException("Доска не найдена"));

        int nextPosition = boardColumnRepository.countByBoardId(board.getId());

        BoardColumn boardColumn = boardColumnMapper.toEntity(boardColumnCreateRequest);
        boardColumn.setBoard(board);
        boardColumn.setPosition(nextPosition);

        return boardColumnRepository.save(boardColumn);
    }

    @Override
    @Transactional
    public BoardColumn update(Long id, BoardColumnUpdateRequest boardColumnUpdateRequest) {
        BoardColumn boardColumn = find(id);
        boardColumnMapper.update(boardColumnUpdateRequest, boardColumn);
        return boardColumnRepository.save(boardColumn);

    }

    @Override
    @Transactional
    public BoardColumn updatePosition(BoardColumnPositionRequest boardColumnPositionRequest) {
        return null;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        BoardColumn boardColumn = find(id);
        boardColumnRepository.delete(boardColumn);
    }
}
