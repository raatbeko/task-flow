package kg.core.comment.dtos;

import java.time.LocalDateTime;

public record CommentResponse(
        Long id,
        Long parentId,
        Long taskId,
        Long authorId,
        String authorUsername,
        String description,
        LocalDateTime createdAt,
        LocalDateTime updatedAt

) {
}
