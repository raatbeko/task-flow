package kg.core.taskTag.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import kg.core.base.model.AuditableEntity;
import kg.core.base.model.BaseEntity;
import kg.core.tag.model.Tag;
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
@Table(
        name = "tag",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"task_id", "tag_id"})}
)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TaskTag extends BaseEntity {

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id", nullable = false)
    Task task;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id", nullable = false)
    Tag tag;

}
