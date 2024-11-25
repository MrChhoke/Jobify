package org.prof.it.soft.repo;

import org.prof.it.soft.entity.Recruiter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.Set;

public interface RecruiterRepository extends JpaRepository<Recruiter, Long> {

    Optional<Recruiter> getRecruiterById(Long id);

}

