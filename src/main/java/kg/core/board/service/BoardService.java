package kg.core.board.service;

import kg.core.base.service.CrudService;
import kg.core.board.model.Board;


public interface BoardService extends CrudService<Board, Long> {

    Board create(Long projectId, Board board);

    void archive(Long id);

    Board duplicate (Long id);

    void delete (Long id);
}
