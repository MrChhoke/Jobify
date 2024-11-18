package org.prof.it.soft.repo;

import org.prof.it.soft.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Long> {
}
