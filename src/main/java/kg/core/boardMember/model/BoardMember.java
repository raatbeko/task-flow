package kg.core.boardMember.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import kg.core.base.model.AuditableEntity;
import kg.core.board.model.Board;
import kg.core.projectMember.model.ProjectMember;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;


@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "board_member")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BoardMember extends AuditableEntity {

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    Board board;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_member_id", nullable = false)
    ProjectMember projectMember;

    @NotNull
    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    BoardRole role;

}

