package kg.core.board.endpoint;

import jakarta.validation.Valid;
import kg.core.board.dtos.BoardCreateRequest;
import kg.core.board.dtos.BoardPositionRequest;
import kg.core.board.dtos.BoardResponse;
import kg.core.board.dtos.BoardUpdateRequest;
import kg.core.task.dtos.UpdatePosition;

import java.util.List;

public interface BoardEndpoint {

    BoardResponse create(BoardCreateRequest request);

    BoardResponse getById(Long id);

    BoardResponse update(Long id, BoardUpdateRequest request);

    void delete(Long id);

    void archive(Long id);

    BoardResponse duplicate(Long id);

    BoardResponse changePosition(Long id, BoardPositionRequest request);

    List<BoardResponse> findByProjectId(Long id);


}