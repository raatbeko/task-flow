package kg.core.boardMember.mapper;

import kg.core.boardMember.dtos.BoardMemberResponse;
import kg.core.boardMember.model.BoardMember;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BoardMemberMapper {

        @Mapping(target = "userId", source = "projectMember.user.id")
        @Mapping(target = "boardId", source = "board.id")
        @Mapping(target = "projectMemberId", source = "projectMember.id")
        @Mapping(target = "email", source = "projectMember.user.email")
        @Mapping(target = "username", source = "projectMember.user.username")
    BoardMemberResponse toResponse(BoardMember boardMember);


}
