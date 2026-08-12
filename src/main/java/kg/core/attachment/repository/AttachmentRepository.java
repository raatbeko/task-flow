package kg.core.attachment.repository;

import kg.core.attachment.model.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AttachmentRepository extends JpaRepository<Attachment, Long> {

    Optional<Attachment> findByUuid(String uuid);

    @Modifying
    @Query(value = "delete from attachments where task_id in (select t.id from task t join board_column bc on t.board_column_id = bc.id where bc.board_id = :boardId)", nativeQuery = true)
    void deleteByBoardId(@Param("boardId") Long boardId);
}
