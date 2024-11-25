package org.prof.it.soft.entity.security;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.prof.it.soft.entity.Person;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Set;

@Entity
@Table(name = "users")
@SuperBuilder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@NamedEntityGraph(
        name = "user",
        attributeNodes = {
                @NamedAttributeNode("id"),
                @NamedAttributeNode("username"),
                @NamedAttributeNode("password"),
                @NamedAttributeNode("accountNonExpired"),
                @NamedAttributeNode("accountNonLocked"),
                @NamedAttributeNode("credentialsNonExpired"),
                @NamedAttributeNode("enabled"),
                @NamedAttributeNode("permissions"),
                @NamedAttributeNode("createdAt"),
                @NamedAttributeNode("updatedAt")
        }
)
@Inheritance(strategy = InheritanceType.JOINED)
public class User implements UserDetails {

    @Id
    @EqualsAndHashCode.Include
    @SequenceGenerator(name = "user_id_seq", sequenceName = "users_seq_id", allocationSize = 1, initialValue = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_id_seq")
    @Column(name = "id", columnDefinition = "bigint", nullable = false)
    protected Long id;

    @Column(name = "username", columnDefinition = "varchar", nullable = false, length = 255)
    protected String username;

    @Column(name = "password", columnDefinition = "varchar", nullable = false, length = 255)
    protected String password;

    @Column(name = "account_non_expired", columnDefinition = "boolean", nullable = false)
    protected boolean accountNonExpired;

    @Column(name = "account_non_locked", columnDefinition = "boolean", nullable = false)
    protected boolean accountNonLocked;

    @Column(name = "credentials_non_expired", columnDefinition = "boolean", nullable = false)
    protected boolean credentialsNonExpired;

    @Column(name = "enabled", columnDefinition = "boolean", nullable = false)
    protected boolean enabled;

    @ElementCollection(targetClass = Permission.class, fetch = FetchType.EAGER)
    @Column(name = "permission_name", nullable = false)
    @JoinTable(name = "user_permissions", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    protected Set<Permission> permissions;

    /**
     * The timestamp when the person entity was created.
     */
    @Column(name = "created_at", columnDefinition = "timestamp", nullable = false)
    protected LocalDateTime createdAt;

    /**
     * The timestamp when the person entity was last updated.
     */
    @Column(name = "updated_at", columnDefinition = "timestamp", nullable = false)
    protected LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    @Override
    public Collection<Permission> getAuthorities() {
        return permissions;
    }

}
