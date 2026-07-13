package kg.core.task.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import kg.core.base.model.AuditableEntity;
import kg.core.boardColumn.model.BoardColumn;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "task")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Task extends AuditableEntity {

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_column_id", nullable = false)
    BoardColumn boardColumn;

    @NotNull
    @Column(name = "title", nullable = false)
    String title;

    @Column(name = "description")
    String description;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "priority",  nullable = false)
    Priority priority;

    @NotNull
    @Column(name = "due_date", nullable = false)
    LocalDateTime dueDate;

    @NotNull
    @Column(name = "position", nullable = false)
    int position;

}
