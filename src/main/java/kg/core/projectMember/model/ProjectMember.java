package kg.core.projectMember.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import kg.core.base.model.AuditableEntity;
import kg.core.project.model.Project;
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
@Table(name = "project_member")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProjectMember extends AuditableEntity {

    @NotNull
    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    Project project;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    @NotNull
    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    Role role;

    @NotNull
    @Column(name = "invitation_status", nullable = false)
    @Enumerated(EnumType.STRING)
    InvitationStatus invitationStatus;

}
