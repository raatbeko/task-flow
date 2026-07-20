package kg.core.board.repository;

import kg.core.base.search.BaseRepository;
import kg.core.board.model.Board;
import kg.core.boardColumn.model.BoardColumn;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardRepository extends BaseRepository<Board, Long> {
    int countByProjectId(Long projectId);
    List<Board> findByProjectIdOrderByPositionAsc(Long projectId);

}
