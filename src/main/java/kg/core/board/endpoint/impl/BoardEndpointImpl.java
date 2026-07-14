package kg.core.board.endpoint.impl;

import kg.core.board.dtos.BoardCreateRequest;
import kg.core.board.dtos.BoardResponse;
import kg.core.board.dtos.BoardUpdateRequest;
import kg.core.board.endpoint.BoardEndpoint;
import kg.core.board.mapper.BoardMapper;
import kg.core.board.model.Board;
import kg.core.board.service.BoardService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BoardEndpointImpl implements BoardEndpoint {

    BoardService boardService;
    BoardMapper boardMapper;

    @Override
    public BoardResponse create(BoardCreateRequest request) {
        Board board = boardMapper.toEntity(request);
        return boardMapper.toResponse(boardService.create(request.projectId(), board));
    }

    @Override
    public BoardResponse getById(Long id) {
        Board board = boardService.find(id);
        return boardMapper.toResponse(board);
    }

    @Override
    public BoardResponse update(Long id, BoardUpdateRequest request) {
        Board board = boardService.find(id);
        boardMapper.update(request, board);
        return boardMapper.toResponse(boardService.save(board));
    }

    @Override
    public void delete(Long id) {
        boardService.delete(id);
    }

    @Override
    public void archive(Long id) {
        boardService.archive(id);
    }

    @Override
    public BoardResponse duplicate(Long id) {
        Board copiedBoard =  boardService.duplicate(id);
        return boardMapper.toResponse(copiedBoard);
    }
}
