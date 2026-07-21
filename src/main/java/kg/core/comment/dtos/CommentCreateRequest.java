package kg.core.comment.dtos;

public record CommentCreateRequest(

        Long parentId,

        String description,

        Long taskId

) {
}
