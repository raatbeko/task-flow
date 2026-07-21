package kg.core.board.mapper;

import kg.core.board.dtos.BoardCreateRequest;
import kg.core.board.dtos.BoardResponse;
import kg.core.board.dtos.BoardUpdateRequest;
import kg.core.board.model.Board;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BoardMapper {

    @Mapping(target = "projectId", source = "project.id")
    BoardResponse toResponse(Board board);

    List<BoardResponse> toResponse(List<Board> board);

    @Mapping(target = "project", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "position", ignore = true)
    Board toEntity(BoardCreateRequest request);

    @Mapping(target = "project", ignore = true)
    Board update(BoardUpdateRequest request, @MappingTarget Board board);
}