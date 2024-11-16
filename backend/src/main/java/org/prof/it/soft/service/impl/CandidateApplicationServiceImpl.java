package org.prof.it.soft.service.impl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.prof.it.soft.dto.response.CandidateApplicationResponseDto;
import org.prof.it.soft.dto.response.CreatingCandidateApplicationResponseDto;
import org.prof.it.soft.entity.CandidateApplication;
import org.prof.it.soft.entity.Vacancy;
import org.prof.it.soft.entity.security.User;
import org.prof.it.soft.exception.NotFoundException;
import org.prof.it.soft.repo.CandidateApplicationRepository;
import org.prof.it.soft.repo.VacancyRepository;
import org.prof.it.soft.service.CandidateApplicationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CandidateApplicationServiceImpl implements CandidateApplicationService {

    protected final VacancyRepository vacancyRepository;
    protected final CandidateApplicationRepository candidateApplicationRepository;
    protected final ModelMapper modelMapper;

    @Override
    public CreatingCandidateApplicationResponseDto createCandidateApplication(Long vacancyId, User user) {
        if (candidateApplicationRepository.existsByPersonIdAndVacancyId(user.getPerson().getId(), vacancyId)) {
            return CreatingCandidateApplicationResponseDto.builder()
                    .message("Application has been already created")
                    .build();
        }

        Vacancy vacancy = getVacancyById(vacancyId);

        CandidateApplication application = new CandidateApplication();
        application.setVacancy(vacancy);
        application.setPerson(user.getPerson());

        CandidateApplication savedApplication = candidateApplicationRepository.saveAndFlush(application);

        return CreatingCandidateApplicationResponseDto.builder()
                .candidateApplicationId(savedApplication.getId())
                .candidateId(user.getPerson().getId())
                .vacancyId(vacancyId)
                .message("Application created successfully")
                .build();
    }

    @Override
    public Page<CandidateApplicationResponseDto> getCandidateApplicationsByVacancyId(Long vacancyId, Long page) {
        return candidateApplicationRepository.findByVacancyId(vacancyId, PageRequest.of(page.intValue(), 10))
                .map(candidateApplication -> modelMapper.map(candidateApplication, CandidateApplicationResponseDto.class));

    }

    @Override
    public Page<CandidateApplicationResponseDto> getCandidateApplicationsByPersonId(Long personId, Long page) {
        return candidateApplicationRepository.findByVacancyId(personId, PageRequest.of(page.intValue(), 10))
                .map(candidateApplication -> modelMapper.map(candidateApplication, CandidateApplicationResponseDto.class));
    }

    @Override
    public Page<CandidateApplicationResponseDto> getCandidateApplicationsByRecruiterId(Long recruiterId, Long page) {
        return candidateApplicationRepository.findByRecruiterId(recruiterId, PageRequest.of(page.intValue(), 10))
                .map(candidateApplication -> modelMapper.map(candidateApplication, CandidateApplicationResponseDto.class));
    }

    private Vacancy getVacancyById(Long vacancyId) {
        return vacancyRepository.findById(vacancyId)
                .orElseThrow(() -> new NotFoundException("Vacancy with id " + vacancyId + " not found"));
    }
}
