package org.prof.it.soft.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.proxy.HibernateProxy;
import org.prof.it.soft.entity.security.User;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "people")
@SuperBuilder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@NamedEntityGraph(
        name = "person",
        attributeNodes = {
                @NamedAttributeNode("firstName"),
                @NamedAttributeNode("lastName"),
        }
)
@DiscriminatorValue("PERSON")
public class Person extends User {

    /**
     * The primary key of the Person entity.
     */

    /**
     * The first name of the person.
     */
    @Column(name = "first_name", columnDefinition = "varchar", nullable = false, length = 255)
    protected String firstName;

    /**
     * The last name of the person.
     */
    @Column(name = "last_name", columnDefinition = "varchar", nullable = true, length = 255)
    protected String lastName;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy
                ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass()
                : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy
                ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass()
                : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Person person = (Person) o;
        return getId() != null && Objects.equals(getId(), person.getId());
    }

    @Override
    public int hashCode() {
        return this instanceof HibernateProxy
                ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode()
                : getClass().hashCode();
    }
}