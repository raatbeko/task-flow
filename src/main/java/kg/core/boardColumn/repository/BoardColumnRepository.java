package kg.core.boardColumn.repository;

import kg.core.base.search.BaseRepository;
import kg.core.boardColumn.model.BoardColumn;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardColumnRepository extends BaseRepository<BoardColumn, Long> {
}
