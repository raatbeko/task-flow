package kg.core.board.service;

import kg.core.base.service.CrudService;
import kg.core.board.dtos.BoardCreateRequest;
import kg.core.board.dtos.BoardUpdateRequest;
import kg.core.board.model.Board;


public interface BoardService extends CrudService<Board, Long> {

    Board createBoard(BoardCreateRequest  boardCreateRequest);
    Board updateBoard(Long id, BoardUpdateRequest boardUpdateRequest);
    void delete(Long id);
    void archive(Long id);
    Board duplicate (Long id);
}
