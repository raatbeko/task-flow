package kg.core.comment.service;

import kg.core.comment.dtos.CommentUpdateRequest;
import kg.core.comment.model.Comment;

import java.util.List;

public interface CommentService {

    Comment create(Long taskId, Long parentId, String description);

    Comment update(Long id, String description);

    void delete(Long id);

    List<Comment> findByTaskId(Long taskId);

}
