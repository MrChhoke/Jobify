package org.prof.it.soft.service;

import org.prof.it.soft.dto.response.CandidateApplicationResponseDto;
import org.prof.it.soft.dto.response.CreatingCandidateApplicationResponseDto;
import org.prof.it.soft.entity.Person;
import org.prof.it.soft.entity.security.User;
import org.springframework.data.domain.Page;

public interface CandidateApplicationService {

    CreatingCandidateApplicationResponseDto createCandidateApplication(Long vacancyId, User user);

    Page<CandidateApplicationResponseDto> getCandidateApplicationsByVacancyId(Long vacancyId, Long pageNum);

    Page<CandidateApplicationResponseDto> getCandidateApplicationsByPersonId(Long personId, Long pageNum);
   
    Page<CandidateApplicationResponseDto> getCandidateApplicationsByRecruiterId(Long recruiterId, Long pageNum);

}
