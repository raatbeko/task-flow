package kg.core.boardColumn.service;

import kg.core.base.service.CrudService;
import kg.core.boardColumn.model.BoardColumn;

public interface BoardColumnService extends CrudService<BoardColumn, Long> {

    BoardColumn create(Long boardId, BoardColumn BoardColumn);

    BoardColumn updatePosition(Integer position);

    void delete(Long id);
}
