package org.prof.it.soft.repo;

import org.prof.it.soft.entity.CandidateApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface CandidateApplicationRepository extends JpaRepository<CandidateApplication, Long> {

    Page<CandidateApplication> findByPersonId(Long personId, Pageable pageable);

    Page<CandidateApplication> findByVacancyId(Long vacancyId, Pageable pageable);

    @Query("select ca from CandidateApplication ca join ca.vacancy v where v.recruiter.id = :recruiterId")
    Page<CandidateApplication> findByRecruiterId(Long recruiterId, Pageable pageable);

    @Query("select case when count(ca) > 0 then true else false end from" +
            " CandidateApplication ca where ca.person.id = :personId and ca.vacancy.id = :vacancyId")
    boolean existsByPersonIdAndVacancyId(Long personId, Long vacancyId);
}