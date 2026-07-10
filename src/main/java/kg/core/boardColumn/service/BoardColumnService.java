package kg.core.boardColumn.service;

import kg.core.base.service.CrudService;
import kg.core.boardColumn.dtos.BoardColumnCreateRequest;
import kg.core.boardColumn.dtos.BoardColumnPositionRequest;
import kg.core.boardColumn.dtos.BoardColumnUpdateRequest;
import kg.core.boardColumn.model.BoardColumn;

public interface BoardColumnService extends CrudService<BoardColumn, Long> {

    BoardColumn create(BoardColumnCreateRequest boardCreateRequest);

    BoardColumn update(BoardColumnUpdateRequest boardColumnUpdateRequest);

    BoardColumn updatePosition(BoardColumnPositionRequest boardColumnPositionRequest);

    void delete(Long id);
}
