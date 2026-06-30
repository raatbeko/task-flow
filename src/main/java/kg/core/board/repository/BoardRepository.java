package kg.core.board.repository;

import kg.core.base.search.BaseRepository;
import kg.core.board.model.Board;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends BaseRepository<Board, Long> {
}
