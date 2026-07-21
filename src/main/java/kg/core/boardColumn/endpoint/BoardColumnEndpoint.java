package kg.core.boardColumn.endpoint;

import kg.core.boardColumn.dtos.BoardColumnCreateRequest;
import kg.core.boardColumn.dtos.BoardColumnPositionRequest;
import kg.core.boardColumn.dtos.BoardColumnResponse;
import kg.core.boardColumn.dtos.BoardColumnUpdateRequest;

import java.util.List;

public interface BoardColumnEndpoint {

    BoardColumnResponse create(BoardColumnCreateRequest request);

    BoardColumnResponse update(Long id, BoardColumnUpdateRequest request);

    void delete(Long id);

    BoardColumnResponse getById(Long id);

    BoardColumnResponse changePosition(Long id, BoardColumnPositionRequest request);

    List<BoardColumnResponse> findByBoardId(Long boardId);
}
