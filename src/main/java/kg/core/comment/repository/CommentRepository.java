package kg.core.comment.repository;

import kg.core.base.search.BaseRepository;
import kg.core.comment.model.Comment;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends BaseRepository<Comment, Long> {
}
