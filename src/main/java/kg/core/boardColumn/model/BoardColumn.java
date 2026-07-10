package kg.core.boardColumn.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import kg.core.base.model.AuditableEntity;
import kg.core.board.model.Board;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "board_column")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BoardColumn extends AuditableEntity {

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    Board board;

    @NotNull
    @Column(name = "name", nullable = false)
    String name;

    @NotNull
    @Column(name = "position", nullable = false)
    Integer position;

}
