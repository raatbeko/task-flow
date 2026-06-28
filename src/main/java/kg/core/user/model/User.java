package kg.core.user.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import kg.core.base.model.AuditableEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "users")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User extends AuditableEntity implements UserDetails {

    @Column(nullable = false, unique = true)
    String email;

    @Column(nullable = false, unique = true)
    String username;

    @Column(name = "phone_number", unique = true)
    String phoneNumber;

    /**
     * Encoded password (BCrypt). Nullable for pure OAuth users.
     */
    @Column
    String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    Role role = Role.USER;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    AuthProvider provider = AuthProvider.LOCAL;

    /**
     * External provider user id (e.g. Google sub). Optional.
     */
    @Column(name = "provider_id")
    String providerId;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Set.of(new SimpleGrantedAuthority(role.toString()));
    }
}
