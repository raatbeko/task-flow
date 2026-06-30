package kg.core.project.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import kg.core.base.model.AuditableEntity;
import kg.core.project.enums.ProjectStatus;
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
@Table(name = "project")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Project extends AuditableEntity {

    @NotNull
    @Column(name = "name", nullable = false)
    String name;

    @Column(name = "description")
    String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false )
    User owner;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    ProjectStatus status;

}
