package kg.core.task.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import kg.core.base.model.AuditableEntity;
import kg.core.boardColumn.model.BoardColumn;
import kg.core.project.model.Project;
import kg.core.tag.model.Tag;
import kg.core.user.model.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

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

    @ManyToMany
    @JoinTable(
            name = "m2m_user_to_tasks",
            joinColumns = {@JoinColumn(name = "task_id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id")})
    private Collection<User> assignees = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "m2m_task_to_tag",
            joinColumns = {@JoinColumn(name = "task_id")},
            inverseJoinColumns = {@JoinColumn(name = "tag_id")})
    private Collection<Tag> tags = new HashSet<>();
}
