package kg.core.comment.repository;

import kg.core.base.search.BaseRepository;
import kg.core.board.model.Board;
import kg.core.comment.model.Comment;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends BaseRepository<Comment, Long> {

    List<Comment> findByTaskIdOrderByCreatedAtAsc(Long taskId);

}
