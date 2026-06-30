package kg.core.taskAssignee.model;

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
@Table(
        name = "task_assignee",
        uniqueConstraints = @UniqueConstraint(columnNames = {"task_id", "user_id"})
)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TaskAssignee extends BaseEntity {

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id", nullable = false)
    Task task;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    User user;
}
