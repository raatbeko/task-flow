package kg.core.board.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import kg.core.base.model.AuditableEntity;
import kg.core.project.model.Project;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(name = "board")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Board extends AuditableEntity {

    @NotNull
    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    Project project;

    @NotNull
    @Column(name = "name", nullable = false)
    String name;

    @Column(name = "description")
    String description;

    @NotNull
    @Column(name = "position", nullable = false)
    Integer position;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    BoardStatus status;
}
