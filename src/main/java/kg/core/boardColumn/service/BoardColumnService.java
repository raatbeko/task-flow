package kg.core.boardColumn.service;

import kg.core.base.service.CrudService;
import kg.core.boardColumn.dtos.BoardColumnPositionRequest;
import kg.core.boardColumn.model.BoardColumn;
import kg.core.task.dtos.UpdatePosition;

public interface BoardColumnService extends CrudService<BoardColumn, Long> {

    BoardColumn create(Long boardId, BoardColumn BoardColumn);

    void updatePosition(Long id, BoardColumnPositionRequest request);

    void delete(Long id);
}
