package org.prof.it.soft.controller;

import lombok.RequiredArgsConstructor;
import org.prof.it.soft.dto.response.CandidateApplicationResponseDto;
import org.prof.it.soft.dto.response.ProfileResponseDto;
import org.prof.it.soft.dto.security.request.RegistrationRecruiterRequestDto;
import org.prof.it.soft.service.CandidateApplicationService;
import org.prof.it.soft.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/v1/recruiter")
@RequiredArgsConstructor
public class RecruiterController {

    protected final UserService userService;
    protected final CandidateApplicationService candidateApplicationService;

    /**
     * Register a recruiter.
     * @param recruiterRequestDto The request dto for the recruiter.
     */
    @PostMapping("/register")
    public ResponseEntity<ProfileResponseDto> registerRecruiter(@Validated(RegistrationRecruiterRequestDto.Save.class) @RequestBody RegistrationRecruiterRequestDto recruiterRequestDto) {
        return ResponseEntity.ok(userService.registerRecruiter(recruiterRequestDto));
    }

    /**
     * Get all applications which were sent to the recruiter.
     * @param userId The id of the recruiter.
     * @param pageNum The page number for pagination.
     */
    @GetMapping("/{id}/applications")
    public ResponseEntity<Page<CandidateApplicationResponseDto>> getRecruiterVacancies(@PathVariable(name = "id") Long userId,
                                                        @RequestParam Long pageNum) {
        Page<CandidateApplicationResponseDto> candidateApplicationResponseDto =
                candidateApplicationService.getCandidateApplicationsByRecruiterId(userId, pageNum);
        return ResponseEntity.ok(candidateApplicationResponseDto);
    }
}