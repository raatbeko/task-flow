package kg.core.boardColumn.endpoint;

import kg.core.boardColumn.dtos.BoardColumnCreateRequest;
import kg.core.boardColumn.dtos.BoardColumnResponse;
import kg.core.boardColumn.dtos.BoardColumnUpdateRequest;

public interface BoardColumnEndpoint {

    BoardColumnResponse create(BoardColumnCreateRequest request);

    BoardColumnResponse update(Long id, BoardColumnUpdateRequest request);

    void delete(Long id);

    BoardColumnResponse getById(Long id);

}
