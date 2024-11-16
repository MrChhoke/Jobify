package org.prof.it.soft.entity.security;

import jakarta.persistence.*;
import lombok.*;
import org.prof.it.soft.entity.Person;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User implements UserDetails {

    @Id
    @EqualsAndHashCode.Include
    @SequenceGenerator(name = "user_id_seq", sequenceName = "users_seq_id", allocationSize = 1, initialValue = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_id_seq")
    @Column(name = "id", columnDefinition = "bigint", nullable = false)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "person_id")
    private Person person;

    @Column(name = "username", columnDefinition = "varchar", nullable = false, length = 255)
    private String username;

    @Column(name = "password", columnDefinition = "varchar", nullable = false, length = 255)
    private String password;

    @Column(name = "account_non_expired", columnDefinition = "boolean", nullable = false)
    private boolean accountNonExpired;

    @Column(name = "account_non_locked", columnDefinition = "boolean", nullable = false)
    private boolean accountNonLocked;

    @Column(name = "credentials_non_expired", columnDefinition = "boolean", nullable = false)
    private boolean credentialsNonExpired;

    @Column(name = "enabled", columnDefinition = "boolean", nullable = false)
    private boolean enabled;

    @ElementCollection(targetClass = Permission.class)
    @Column(name = "permission_name", nullable = false)
    @JoinTable(name = "user_permissions", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<Permission> permissions;

    @Override
    public Collection<Permission> getAuthorities() {
        return permissions;
    }

}
