package org.prof.it.soft.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.proxy.HibernateProxy;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "recruiters")
@Getter
@Setter
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@DiscriminatorValue("RECRUITER")
public class Recruiter extends Person {

    /**
     * The name of the company the recruiter works for.
     */
    @Column(name = "company_name", columnDefinition = "varchar", nullable = true, length = 255)
    protected String companyName;

    /**
     * The vacancies associated with the recruiter.
     */
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "recruiter")
    @Builder.Default
    @ToString.Exclude
    protected Set<Vacancy> vacancies = new HashSet<>();

    /**
     * This method adds a vacancy to the recruiter's vacancies.
     * It also sets the recruiter of the vacancy to this recruiter.
     */
    public void addVacancy(Vacancy vacancy) {
        vacancies.add(vacancy);
        vacancy.setRecruiter(this);
    }

    /**
     * This method removes a vacancy from the recruiter's vacancies.
     * It also sets the recruiter of the vacancy to null.
     */
    public void removeVacancy(Vacancy vacancy) {
        vacancies.remove(vacancy);
        vacancy.setRecruiter(null);
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy
                ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass()
                : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy
                ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass()
                : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Recruiter recruiter = (Recruiter) o;
        return getId() != null && Objects.equals(getId(), recruiter.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy
                ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode()
                : getClass().hashCode();
    }
}