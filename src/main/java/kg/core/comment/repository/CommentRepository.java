package kg.core.comment.repository;

import kg.core.base.search.BaseRepository;
import kg.core.comment.model.Comment;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends BaseRepository<Comment, Long> {

    List<Comment> findByTaskIdOrderByCreatedAtAsc(Long taskId);
    @Modifying
    @Query("delete from Comment c where c.task.id in (select t.id from Task t where t.boardColumn.id in (select bc.id from BoardColumn bc where bc.board.id = :boardId))")
    void deleteByBoardId(@Param("boardId") Long boardId);

}
