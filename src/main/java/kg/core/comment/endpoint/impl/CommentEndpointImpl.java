package kg.core.comment.endpoint.impl;


import kg.core.comment.dtos.CommentCreateRequest;
import kg.core.comment.dtos.CommentResponse;
import kg.core.comment.dtos.CommentUpdateRequest;
import kg.core.comment.endpoint.CommentEndpoint;
import kg.core.comment.mapper.CommentMapper;
import kg.core.comment.model.Comment;
import kg.core.comment.service.CommentService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CommentEndpointImpl implements CommentEndpoint {

    CommentMapper commentMapper;
    CommentService commentService;


    @Override
    public CommentResponse create(CommentCreateRequest request) {
        return commentMapper.toResponse(commentService.create(request.taskId(),
                request.parentId(), request.description()));
    }

    @Override
    public CommentResponse update(Long id, CommentUpdateRequest request) {
        return commentMapper.toResponse(commentService.update(id, request.description()));
    }

    @Override
    public void delete(Long id) {
        commentService.delete(id);
    }


    @Override
    public List<CommentResponse> findByTaskId(Long taskId) {
        return commentMapper.toResponse(commentService.findByTaskId(taskId));
    }
}
