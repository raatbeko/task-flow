package kg.core.boardColumn.endpoint.impl;


import kg.core.boardColumn.dtos.BoardColumnCreateRequest;
import kg.core.boardColumn.dtos.BoardColumnPositionRequest;
import kg.core.boardColumn.dtos.BoardColumnResponse;
import kg.core.boardColumn.dtos.BoardColumnUpdateRequest;
import kg.core.boardColumn.endpoint.BoardColumnEndpoint;
import kg.core.boardColumn.mapper.BoardColumnMapper;
import kg.core.boardColumn.model.BoardColumn;
import kg.core.boardColumn.service.BoardColumnService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BoardColumnEndpointImpl implements BoardColumnEndpoint {

    BoardColumnService boardColumnService;
    BoardColumnMapper boardColumnMapper;

    @Override
    public BoardColumnResponse create(BoardColumnCreateRequest request) {
        BoardColumn boardColumn = boardColumnMapper.toEntity(request);
        return boardColumnMapper.toResponse(boardColumnService.create(request.boardId(), boardColumn));
    }

    @Override
    public BoardColumnResponse update(Long id, BoardColumnUpdateRequest request) {
        BoardColumn column = boardColumnService.find(id);
        boardColumnMapper.update(request, column);
        return boardColumnMapper.toResponse(boardColumnService.save(column));
    }

    @Override
    public void delete(Long id) {
        boardColumnService.delete(id);
    }

    @Override
    public BoardColumnResponse getById(Long id) {
        BoardColumn column = boardColumnService.find(id);
        return boardColumnMapper.toResponse(column);
    }

    @Override
    public BoardColumnResponse changePosition(Long id, BoardColumnPositionRequest request){
        boardColumnService.updatePosition(id, request);
        return boardColumnMapper.toResponse(boardColumnService.find(id));
    }

    @Override
    public List<BoardColumnResponse> findByBoardId(Long boardId) {
        return boardColumnMapper.toResponse(boardColumnService.findByBoardId(boardId));
    }
}
