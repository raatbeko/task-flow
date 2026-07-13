package kg.core.boardColumn.mapper;

import kg.core.boardColumn.dtos.BoardColumnCreateRequest;
import kg.core.boardColumn.dtos.BoardColumnResponse;
import kg.core.boardColumn.dtos.BoardColumnUpdateRequest;
import kg.core.boardColumn.model.BoardColumn;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface BoardColumnMapper {

    @Mapping(target = "boardId", source = "board.id")
    BoardColumnResponse toResponse(BoardColumn boardColumn);

    @Mapping(target = "board", ignore = true)
    @Mapping(target = "position", ignore = true)
    BoardColumn toEntity(BoardColumnCreateRequest boardColumnCreateRequest);

    @Mapping(target = "board", ignore = true)
    @Mapping(target = "position", ignore = true)
    void update (BoardColumnUpdateRequest boardColumnUpdateRequest, @MappingTarget BoardColumn boardColumn);


}
