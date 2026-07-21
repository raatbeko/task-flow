package kg.core.comment.endpoint;

import kg.core.comment.dtos.CommentCreateRequest;
import kg.core.comment.dtos.CommentResponse;
import kg.core.comment.dtos.CommentUpdateRequest;

import java.util.List;

public interface CommentEndpoint {

    CommentResponse create(CommentCreateRequest commentCreateRequest);

    CommentResponse update(Long id, CommentUpdateRequest commentUpdateRequest);

    void delete(Long id);

    List<CommentResponse> findByTaskId(Long taskId);

}
