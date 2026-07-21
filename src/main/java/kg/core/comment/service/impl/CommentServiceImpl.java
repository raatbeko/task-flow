package kg.core.comment.service.impl;

import kg.core.auth.service.impl.DefaultAccountContextProvider;
import kg.core.base.exception.NotFoundException;
import kg.core.base.service.impl.DefaultCrudService;
import kg.core.comment.model.Comment;
import kg.core.comment.repository.CommentRepository;
import kg.core.comment.service.CommentService;
import kg.core.task.model.Task;
import kg.core.task.repository.TaskRepository;
import kg.core.user.model.User;
import kg.core.utils.UserProvider;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CommentServiceImpl extends DefaultCrudService<Comment, Long> implements CommentService {

    TaskRepository taskRepository;
    CommentRepository commentRepository;
    UserProvider userProvider;

    protected CommentServiceImpl(CommentRepository commentRepository, TaskRepository taskRepository, UserProvider userProvider) {
        super(commentRepository);
        this.taskRepository = taskRepository;
        this.commentRepository = commentRepository;
        this.userProvider = userProvider;
    }

    @Override
    @Transactional
    public Comment create(Long taskId, Long parentId, String description) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new NotFoundException("Задача не найдена"));

        User currentUser = userProvider.getCurrentUser();

        Comment parent = null;

        if(parentId != null){
            parent = find(parentId);
        }

        Comment comment = new Comment();
        comment.setAuthor(currentUser);
        comment.setTask(task);
        comment.setDescription(description);
        comment.setParent(parent);

        return save(comment);
    }

    @Override
    @Transactional
    public Comment update(Long id, String description) {
        Comment comment = find(id);
        User currentUser = userProvider.getCurrentUser();

        if(!comment.getAuthor().equals(currentUser)) {
            throw new AccessDeniedException("Редактировать может только автор");
        }

        comment.setDescription(description);
        return save(comment);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Comment comment = find(id);
        User currentUser = userProvider.getCurrentUser();

        if(!comment.getAuthor().equals(currentUser)) {
            throw new AccessDeniedException("Удалять может только автор");
        }
        commentRepository.delete(comment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Comment> findByTaskId(Long taskId) {
        return commentRepository.findByTaskIdOrderByCreatedAtAsc(taskId);
    }


}
