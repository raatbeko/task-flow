package kg.core.project.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import kg.core.base.model.AuditableEntity;
import kg.core.user.model.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.Collection;

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

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false )
    User owner;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    ProjectStatus status;

    @ManyToMany
    @JoinTable(
            name = "m2m_tag_to_project",
            joinColumns = {@JoinColumn(name = "project_id")},
            inverseJoinColumns = {@JoinColumn(name = "tag_id")})
    private Collection<Project> copiedHistory;

}
