package org.prof.it.soft.controller;

import lombok.RequiredArgsConstructor;
import org.prof.it.soft.dto.response.CandidateApplicationResponseDto;
import org.prof.it.soft.dto.response.ProfileResponseDto;
import org.prof.it.soft.dto.response.VacancyResponseDto;
import org.prof.it.soft.dto.security.request.RegistrationRecruiterRequestDto;
import org.prof.it.soft.entity.security.User;
import org.prof.it.soft.service.CandidateApplicationService;
import org.prof.it.soft.service.UserService;
import org.prof.it.soft.service.VacancyService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/recruiter")
@RequiredArgsConstructor
public class RecruiterController {

    protected final UserService userService;
    protected final VacancyService vacancyService;
    protected final CandidateApplicationService candidateApplicationService;

    /**
     * Register a recruiter.
     *
     * @param recruiterRequestDto The request dto for the recruiter.
     */
    @PostMapping("/register")
    public ResponseEntity<ProfileResponseDto> registerRecruiter(@Validated(RegistrationRecruiterRequestDto.Save.class) @RequestBody RegistrationRecruiterRequestDto recruiterRequestDto) {
        return ResponseEntity.ok(userService.registerRecruiter(recruiterRequestDto));
    }

    /**
     * Get all applications which were sent to the recruiter.
     *
     * @param user    - authenticated recruiter.
     * @param pageNum The page number for pagination.
     */
    @GetMapping("/applications")
    public ResponseEntity<Page<CandidateApplicationResponseDto>> getRecruiterApplications(@AuthenticationPrincipal User user,
                                                                                          @RequestParam(defaultValue = "1") Long pageNum) {
        Page<CandidateApplicationResponseDto> candidateApplicationResponseDto =
                candidateApplicationService.getCandidateApplicationsByRecruiterId(user.getId(), pageNum - 1);
        return ResponseEntity.ok(candidateApplicationResponseDto);
    }

    /**
     * Get all vacancies which were created by the recruiter.
     *
     * @param user    - authenticated recruiter.
     * @param pageNum The page number for pagination.
     */
    @GetMapping("/vacancies")
    public ResponseEntity<Page<VacancyResponseDto>> getRecruiterVacancies(@AuthenticationPrincipal User user,
                                                                          @RequestParam(defaultValue = "1") Long pageNum) {
        Page<VacancyResponseDto> vacancyResponseDto = vacancyService.getVacanciesByRecruiterId(user.getId(), pageNum - 1);
        return ResponseEntity.ok(vacancyResponseDto);
    }
}