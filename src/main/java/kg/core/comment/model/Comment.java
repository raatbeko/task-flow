package kg.core.comment.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import kg.core.base.model.AuditableEntity;
import kg.core.base.model.BaseEntity;
import kg.core.task.model.Task;
import kg.core.user.model.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "comment")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Comment extends BaseEntity {

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id", nullable = false)
    Task task;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    User author;

    @NotNull
    @Column(name = "description", nullable = false)
    String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    Comment parent;

}
