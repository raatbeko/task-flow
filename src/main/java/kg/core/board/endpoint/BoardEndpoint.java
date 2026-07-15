package kg.core.board.endpoint;

import kg.core.board.dtos.BoardCreateRequest;
import kg.core.board.dtos.BoardResponse;
import kg.core.board.dtos.BoardUpdateRequest;

public interface BoardEndpoint {

    BoardResponse create(BoardCreateRequest request);

    BoardResponse getById(Long id);

    BoardResponse update(Long id, BoardUpdateRequest request);

    void delete(Long id);

    void archive(Long id);

    BoardResponse duplicate(Long id);

}