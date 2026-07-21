package kg.core.comment.mapper;

import kg.core.comment.dtos.CommentResponse;
import kg.core.comment.model.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CommentMapper {


    @Mapping(target = "parentId", source = "parent.id")
    @Mapping(target = "taskId", source = "task.id")
    @Mapping(target = "authorId", source = "author.id")
    @Mapping(target = "authorUsername", source = "author.username")
    CommentResponse toResponse(Comment comment);

    List<CommentResponse> toResponse(List<Comment> comments);


}
