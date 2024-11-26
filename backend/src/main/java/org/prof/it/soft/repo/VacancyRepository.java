package org.prof.it.soft.repo;

import org.prof.it.soft.dto.response.VacancyResponseDto;
import org.prof.it.soft.entity.Vacancy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;

public interface VacancyRepository extends JpaRepository<Vacancy, Long>, JpaSpecificationExecutor<Vacancy> {

    Set<Vacancy> findAllByRecruiterId(Long recruiterId);

    @EntityGraph(attributePaths = {"recruiter", "technologyStack"})
    Page<Vacancy> findAll(Specification<Vacancy> spec, Pageable pageable);

    @Query("SELECT v.id " +
            "FROM Vacancy v " +
            "JOIN CandidateApplication ca ON ca.vacancy.id = v.id" +
            " WHERE ca.person.id = :candidateId")
    Set<Long> findAllVacancyIdAppliedByCandidate(@Param("candidateId") Long candidateId);

    @Query("SELECT CASE WHEN (COUNT(ca.id) > 0) THEN true ELSE false END " +
            "FROM CandidateApplication ca " +
            "WHERE ca.vacancy.id = :vacancyId AND ca.person.id = :candidateId")
    boolean isVacancyIsAppliedByCandidate(Long vacancyId, Long candidateId);
}
